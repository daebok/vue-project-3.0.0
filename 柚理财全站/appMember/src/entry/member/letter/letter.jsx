import '../../../common/lib';
import React from 'react';
import ReactDOM from 'react-dom';
import { Modal,Button,Pagination,Checkbox} from "antd";
import './letter.less'
import Accountmenu from '../../../component/accountmenu/menu';
import { Publiclib } from '../../../common/common';
function error(v) {
  Modal.error({
    title: v,
    okText:"关闭",
    wrapClassName:'tiperror'
  });
}
const LetterList=React.createClass({
    getInitialState: function() {
        return {
            checked:false
        };
    },
    changeChecked(e){
        if(e.target.checked==false){
            this.setState({
                checked:false
            })
            this.props.setAllstate(e.target.value,'delete')
        }else{
            this.setState({
                checked:true
            })
            this.props.setAllstate(e.target.value,'add')
        }
    },
    setCheckState(v){
        if(v==1){
            this.setState({
                checked:false
            })
        }else{
            this.setState({
                checked:true
            })
        }

    },
    componentDidMount(){
        $('.list .name').off('click').on('click',function () {
            if($(this).parents('.list').find('.content').css('display')=='none'){
                $('.list').find('.content').hide();
                $(this).parents('.list').find('.content').show();
            } else{

                $('.list').find('.content').hide()
            }
        })
    },
    changeReady(e){
        let _this=this;
        $.ajax({
            url: '/member/letter/batchSet.html',
            type: 'POST',
            dataType: 'json',
            data: {'ids':$(e.target).attr('data-id'),'batchType':'2'}
        })
            .done(function(data) {
                if(data.result){
                    _this.props.readyChange(_this.props.listNumber)
                }
                else
                {
                    error(data.msg);
                }
            })

    },
        render(){
        let i=<i className="iconfont" style={{'fontSize':'16px','color':'#f95a28'}}>&#xe678;</i>;
        if(this.props.data.readFlag==true){
            i=<i className="iconfont">&#xe679;</i>;
        }
        let time =Publiclib.formatDate(this.props.data.createTime,2)
        return (
            <div className="list">
               <div className="listTop clearfix">
                   <label>
                       <Checkbox checked={this.state.checked} value={this.props.data.id} onChange={this.changeChecked}  />
                       {i}
                   </label>
                   <span className={this.props.data.readFlag==0?'ready name':'name'} ref='spanName' onClick={this.changeReady} data-id={this.props.data.id}>{this.props.data.title}</span>
                   <span className="fr">{time}</span>
               </div>
                <div className="content">
                    {this.props.data.content}
                </div>
            </div>
        )
    }
})
const Letter=React.createClass({
    getInitialState: function() {
        return {
            allChose:false,
            allArray:[],
            chooseArray:[],
            data:''
        };
    },
    allChose(e){
        let array=[];

        for(let i=0;i<this.state.data.rows.length;i++){
            array.push(this.state.data.rows[i].id)
        }
        if(e.target.checked==false){
            this.setState({
                allChose:false,
                allArray:[]
            })
            for(let i=0;i<this.state.data.rows.length;i++){
                let a='list'+i;
                this.refs[a].setCheckState(1)
            }
        }else{
            this.setState({
                allChose:true,
                allArray:array
            })
            for(let i=0;i<this.state.data.rows.length;i++){
                let a='list'+i;
                this.refs[a].setCheckState(2)
            }
        }

    },
    getLIst(e){
        var _this=this;
        $.ajax({
            url: '/member/letter/list.html',
            type: 'POST',
            dataType: 'json',
            data:{'page.page':e}
        }).done(function(data) {
            if(data.result){
                    _this.setState({
                        data:data.data,
                        allChose:false,
                        allArray:[]
                    })
                }
                else
                {
                    error(data.msg);
                }
            })
    },
    componentWillMount(){
      this.getLIst(1);
      let array=[];

    },

    setAllstate(value,type){
        var array=this.state.allArray;
        Array.prototype.indexOf = function(val) {
            for (var i = 0; i < this.length; i++) {
                if (this[i] == val) return i;
            }
            return -1;
        };
        Array.prototype.remove = function(val) {

            var index = this.indexOf(val);
            if (index > -1) {
                this.splice(index, 1);
            }
        };
        if(type=='add'){
            if(array.length==0){
                array.push(value)
            }else if(array.indexOf(value)=='-1'){
                array.push(value)
            }
        }else{
            array.remove(value);
        }
        let Achose=[];
        for(let i=0;i<this.state.data.rows.length;i++){
            Achose.push(this.state.data.rows[i].id)
        }
        let Bchoose='';
        let _a = array.sort();
        let _b = Achose.sort();
        if(JSON.stringify(_a) === JSON.stringify(_b)){
            Bchoose=true
        }else{
            Bchoose=false
        }
        this.setState({
            allArray:array,
            allChose:Bchoose
        })

    },
    changeBtn(e){
        var _this=this;
        if(this.state.allArray.length==0){
            error('请勾选站内信');
            return false
        }
        $.ajax({
            url: '/member/letter/batchSet.html',
            type: 'POST',
            dataType: 'json',
            data:{
                ids:this.state.allArray.toString(),
                'batchType':$(e.target).attr('data-type')
            }
        })
            .done(function(data) {
                if(data.result){
                   window.location.reload()
                }
                else
                {
                    error(data.msg);
                }
            })
    },
    changePage(e){
       this.getLIst(e);
        $('.list').find('.content').hide();
        for(let i=0;i<this.state.data.rows.length;i++){
            let a='list'+i;
            this.refs[a].setCheckState(1)
        }
    },
    readyChange(a){
        let dataReady=this.state.data;
        dataReady.rows[a].readFlag=1;
        this.setState({
            data:dataReady
        })
    },
    render(){
        if(!this.state.data){
            return false
        }
        var _this=this;
        var html='';
        var html1=''
        if(this.state.data.rows==undefined){
            html=<div className="noLetter"><i className="iconfont">&#xe638;</i><p>暂无消息</p></div>
        }else{
            html= this.state.data.rows.map(function (v,k) {
                return  <LetterList ref={'list'+k} checked={_this.state.allChose} readyChange={_this.readyChange} listNumber={k} key={k} data={v} setAllstate={_this.setAllstate}/>;
            })
            html1= <Pagination defaultCurrent={1} total={this.state.data.count} onChange={this.changePage} pageSize={this.state.data.pageSize}/>
        }
        return (
            <div>
                <div className="top-title">
                    <label>
                        <Checkbox checked={this.state.allChose} onChange={this.allChose} />
                        全选
                    </label>
                    <div className="fr"><a href="javascript:;" data-type="1" onClick={this.changeBtn}>删除信息</a><span>|</span><a href="javascript:;" data-type="2" onClick={this.changeBtn}>标记已读</a><span>|</span><a href="javascript:;" data-type="3" onClick={this.changeBtn}>标记未读</a></div>
                </div>{html}
                <div className="mt30 mb30">
                    {html1}
                </div>
            </div>
        )
    }
})
ReactDOM.render(<Letter/>,  document.getElementById("j-letter"));
ReactDOM.render(<Accountmenu/>,  document.getElementById("j-sider-menu"));
