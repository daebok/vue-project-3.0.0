import '../../../common/lib';
import React from 'react';
import ReactDOM from 'react-dom';
import { Modal,Button,Pagination,Checkbox} from "antd";
import './letter.less'
import Accountmenu from '../../../component/accountmenu/menu';
import { Publiclib } from '../../../common/common';
function error(v) {
  Modal.error({
    title: "错误提示",
    okText:"关闭",
      content:v,
    wrapClassName:'tiperror'
  });
}
const LetterList=React.createClass({
    getInitialState: function() {
        return {
            checked:false,
            ready:false
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
        if(this.props.data.readFlag==1){
            this.setState({
                ready:true
            })
        }
        $('.list .name').on('click',function () {
           if($(this).parent('div').next('.content').css('display')=='none'){
               $(this).parent('div').next('.content').show()
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
                    _this.setState({
                        ready:true
                    })
                }
                else
                {
                    error(data.msg);
                }
            })

    },
    render(){
        let i=<i className="iconfont" style={{'fontSize':'16px','color':'#f95a28'}}>&#xe678;</i>;
        if(this.state.ready==true){
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
                   <span className={this.state.ready==false?'ready name':'name'} onClick={this.changeReady} data-id={this.props.data.id}>{this.props.data.title}</span>
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
                let a='list'+i
                this.refs[a].setCheckState(1)
            }
        }else{
            this.setState({
                allChose:true,
                allArray:array
            })
            for(let i=0;i<this.state.data.rows.length;i++){
                let a='list'+i
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
        })
            .done(function(data) {
                if(data.result){
                    _this.setState({
                        data:data.data
                    })
                }
                else
                {
                    error(data.msg);
                }
            })
    },
    componentWillMount(){
      this.getLIst(1)
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
        this.setState({
            allArray:array
        })

    },
    changeBtn(e){
        var _this=this;
        if(this.state.allArray.length==0){
            error('请勾选邮件')
            return false
        }
        $.ajax({
            url: '/member/letter/batchSet.html',
            type: 'POST',
            dataType: 'json',
            data:{
                ids:this.state.allArray,
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
       this.getLIst(e)
    },
    render(){
        if(!this.state.data){
            return false
        }
        var _this=this;
        return (
            <div>
                <div className="top-title">
                    <label>
                        <Checkbox checked={this.state.allChose} onChange={this.allChose} />
                        全选
                    </label>
                    <div className="fr"><a href="javascript:;" data-type="1" onClick={this.changeBtn}>删除信息</a><span>|</span><a href="javascript:;" data-type="2" onClick={this.changeBtn}>标记已读</a><span>|</span><a href="javascript:;" data-type="3" onClick={this.changeBtn}>标记未读</a></div>
                </div>{
                this.state.data.rows.map(function (v,k) {
                    return  <LetterList ref={'list'+k} checked={_this.state.allChose} key={k} data={v} setAllstate={_this.setAllstate}/>;
                })
                }
                <div className="mt30">
                <Pagination defaultCurrent={1} total={this.state.data.count} onChange={this.changePage} pageSize={this.state.data.pageSize}/>
                </div>
            </div>
        )
    }
})
ReactDOM.render(<Letter/>,  document.getElementById("j-letter"));
ReactDOM.render(<Accountmenu/>,  document.getElementById("j-sider-menu"));
