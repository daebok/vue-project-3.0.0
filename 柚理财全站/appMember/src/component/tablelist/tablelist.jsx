import React from 'react';
import ReactDOM from 'react-dom';
import { Table,Select,Modal} from 'antd';
const Option = Select.Option;
import Datepicker from '../datepicker/datepicker';


function error(a,url) {
  Modal.error({
    title: a,
    okText:"确定",
    wrapClassName:'tiperror',
    onOk() {
      if(url){
        console.log(url);
        window.location.href = url;
      }
    }
  });
}

export default class Tablelist extends React.Component{
  constructor(props) {
    super(props);
    this.state={
      section:0,
      data:'',
      selectvalue:'',
      selectitem :<Option value="">全部状态</Option>
    };
    this.status = [];
    this.set = this.set.bind(this);
    this.datatype = this.props.singNoAll ? '0' : '00';
    this.handleChange = this.handleChange.bind(this);
    this.initialize = this.initialize.bind(this);
    this.getselectitem = this.getselectitem.bind(this);    
  }

  set(e){
    this.refs.clean.clean();
    if(e.target.nodeName == "SPAN"){
      $("#"+ this.props.tableid + "filter .filter-fixedtime span").removeClass('on');
      $(e.target).addClass('on');
      this.datatype = e.target.getAttribute("data-type");
      this.getlist(this.props.url,'','',this.state.selectvalue);
    }
  }

  getlist(urls,stime,etime,selectvalue,page){
    const that = this;
    let pages = 1;
    if(page){
      pages = page;
    }

    let datas = {dateType:that.datatype,startTime:stime,endTime:etime,"page.page":pages,"page.pageSize":that.props.pageSize ? that.props.pageSize : "10"};
    if(!this.props.selectname)
    {
      datas.status = selectvalue;
    }  
    else
    {
      datas[this.props.selectname] = selectvalue;
    }
    $.ajax({
        url: urls,
        type: 'POST',
        dataType: 'json',
        data: datas
      })
      .done(function(datas) {
        if(!datas.result)
        {
          error(datas.msg,datas.url);
          return false;
        }

        let data = datas.data;
        if(data==undefined){
          data
        }
        if(data.rows && data.rows.length){
          $.each(data.rows,function(index, el) {
            el.key = index;
          });

          if( datas.typePage && datas.typePage.rows && datas.typePage.rows.length ){
            window.typeName = datas.typePage.rows;
          }

          that.setState({
            data: data.rows
          });
          const columns = that.props.columns;
          const pagination = {
            total: data.count,
            showSizeChanger: false,
            pageSize:data.pageSize,
            defaultCurrent:1,
            onChange(current) {              
              const time = that.refs.clean.returntime();
              that.getlist(that.props.url,time[0],time[1],that.state.selectvalue,current);
            },
          };          
          ReactDOM.render(<Table columns={columns}  dataSource={that.state.data} pagination={pagination} />,  document.getElementById(that.props.tableid));
          if(pages === 1){
            $(".ant-table-pagination li").eq(1)[0].click();
          }
                    
        }
        else
        {
          that.setState({
            data: []
          });
          $("#"+that.props.tableid).html("<div class='nolist'><em class='iconfont'>&#xe638;</em>暂无记录</div>");
        }  
      })
      .fail(function() {
        error('网络异常，请重试！');
      });
  }
  getselectitem(){
    if(!this.props.select){
      return;
    }
    if(this.props.select.url == "" ){
        if(!this.props.select.list){
          return;
        }
        let selectitem = this.props.select.list.map(function(k,v){
          return <Option key={v} value={k.itemValue} title={k.itemName}>{k.itemName}</Option>
        });
        this.setState({
          selectitem: selectitem
        });
    }
    else{
      let that = this;
      $.ajax({
        url: that.props.select.url,
        type: 'POST',
        dataType: 'json',
      })
      .done(function(data) {
        if( data.result != undefined && !data.result){
          if( data.url != undefined && data.url!='' ){
            error(data.msg,data.url);
          }
          else
          {
            error(data.msg);
          } 
          return false;
        }

        if(data.length){
          let selectitem = data.map(function(k,v){
            return <Option key={v} value={k.itemValue} title={k.itemName}>{k.itemName}</Option>
          });
          selectitem.unshift(that.state.selectitem);
          that.setState({
            selectitem: selectitem,
          });
        }        

      })
      .fail(function() {
        error('网络异常，请重试！');
      });
    }
  }
  componentDidMount(){
    this.getlist(this.props.url);
    this.getselectitem();
  }
  initialize(){
    this.datatype = '00';
    $("#"+ this.props.tableid + "filter .filter-fixedtime span").removeClass('on');
    $("#"+ this.props.tableid + "filter .filter-fixedtime span").eq(0).addClass('on');
  }
  handleChange(value) {
    console.log(`selected ${value}`);
    const time = this.refs.clean.returntime();
    this.getlist(this.props.url,time[0],time[1],value);
    this.setState({
      selectvalue:value
    });
  }
  render() {
    if(!this.state.data){
      return false;
    }
    let filterclassname ="row filter",filterSelectClassname ="filter-select",filterSectiontime="filter-sectiontime",filterFixedtime="filter-fixedtime";
    if(this.props.config){
      if(!this.props.config.single && !this.props.config.datetime && !this.props.config.select)
      {
          filterclassname = "row filter hide";
      }else{
        if(!this.props.config.single)
        {
            filterFixedtime = "filter-fixedtime hide";
        }
        if(!this.props.config.datetime){
            filterSectiontime = "filter-sectiontime hide";
        }
        if(!this.props.config.select){
            filterSelectClassname = "filter-select hide";
        }
      }
    }

    let singleArr =<ul className="clearfix" onClick={this.set}>
                <li><span className="on" data-type="">全部</span></li>
                <li><span data-type="01">近7天</span></li>
                <li><span data-type="02">近1个月</span></li>
                <li><span data-type="03">近3个月</span></li>
                <li><span data-type="04">近1年</span></li>
              </ul>;
   

    if(this.props.singleArr){
      let liList = "";
      if(this.props.singNoAll)
      {
        let self = this;
        liList = this.props.singleArr.map(function(el, index) {
          let on = "";
          if(index == 0){
            on = "on";
          }
          return <li key={index+1}><span className={on} data-type={self.props.singleArrId[index]}>{el}</span></li>;
        });

        singleArr = <ul className="clearfix" onClick={this.set}>{liList}</ul>;

      }
      else
      {

        liList = this.props.singleArr.map(function(el, index) {
          return <li key={index+1}><span data-type={index}>{el}</span></li>;
        });

        singleArr = <ul className="clearfix" onClick={this.set}><li  key="0"><span className="on" data-type="">全部</span></li>{liList}</ul>;

      }
    }            
                
    const filterid = this.props.tableid + "filter";
    return (
          <div className="tablelist">
            <div className={filterclassname} id={filterid}>
              <div className={filterFixedtime}>
                {singleArr}
              </div>
              <div className={filterSectiontime}>
                <dl className="dataselect clearfix">
                  <dd><Datepicker ref="clean" callback={this.initialize} selectvalue = {this.state.selectvalue} columns={this.props.columns} tableid={this.props.tableid} url={this.props.url} selectname={this.props.selectname} pageSize={this.props.pageSize ? this.props.pageSize : "10"} /></dd>
                </dl>
              </div>
              <div className={filterSelectClassname}>
                <dl>
                  <dt>状态</dt>
                  <dd>
                    <Select defaultValue="全部状态" style={{ width: 130 }} ref="select" onChange={this.handleChange}>
                      {this.state.selectitem}
                    </Select>
                  </dd>
                </dl>
              </div>
            </div>
            <div id={this.props.tableid}></div>
          </div>
    );
  }
}