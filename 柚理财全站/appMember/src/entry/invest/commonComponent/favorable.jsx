import React from 'react';
import { Button, Modal, Pagination } from 'antd';
import './favorable.less';
import { Publiclib } from '../../../common/common';

function error(v) {
  Modal.error({
    title: v,
    okText:"关闭",
    wrapClassName:'tiperror'
  });
}

function success(v) {
  Modal.success({
    title: '提交成功',
    content: v,
  });
}

//时间到期
class TimeComponent extends React.Component{
    render(){
        let useExpireTimeHtml = this.props.item.useExpireTime ? <span>{Publiclib.formatDate(this.props.item.useExpireTime, 2)} 到期</span> : '';
        return (           
            <p className="favorable-text">
                {useExpireTimeHtml}
            </p>            
        )
    }
}

//红包
class RedPacket extends React.Component{
    constructor(props){
        super(props);
        this.objVal = {};
        this._selectRedPacket = this.selectRedPacket.bind(this);
        this._pageHander = this.pageHander.bind(this);
    }
    pageHander(current){
        this.props.PageHander("red",current);
    }
    componentDidMount(){
        //选择红包
        this._selectRedPacket();
    }
    selectRedPacket(){
        const _this = this;
        let sum = 0;       
        let self = this;
        let flag = true;
        let ids = [];
        $("body").on("click", ".redPacketList li", function(){                     
            let icon = $(this).find(".favorable-icon");
            let value = parseInt($(this).find(".favorable-value").attr("data-val"));                                                             
            if(icon.css("display") == "none" && flag && (sum+value) <= self.objVal.moneyYes){               
                icon.css({"display": "block"});                            
                sum += value;
                if(sum >= self.objVal.moneyYes){
                    flag = false;
                }
                ids.push($(this).attr("data-id"));               
            } else if(icon.css("display") == "block"){
                icon.css({"display": "none"});
                sum -= value;
                flag = true;
                ids = _this.removeIds(ids, $(this).attr("data-id"));
            }            
            //设置选择红包总额
            self.props.selectFn(sum, ids);                   
        });
    }
    removeIds(ids, id){
        let len = ids.length;
        for(let i = 0; i < len; i++){
            if(ids[i] == id){
                ids.splice(i, 1);
            }
        }
        return ids;
    }  
    render(){
        let data = this.props.data;
        let amountVal = this.props.amountVal;//投资金额
        if(!data || !amountVal){
            return false;
        }
        //可用额度比
        let redEnvelopeRateMax = $("#redEnvelopeRateMax").val();
        //可用额度
        this.objVal.moneyYes = (isNaN(amountVal) ? 0 : isNaN(amountVal * redEnvelopeRateMax * 0.01) ? 0 : amountVal * redEnvelopeRateMax * 0.01);
        let html = <li className="noText">
            <span className="iconfont">&#xe663;</span>
            <p>暂无红包</p>
        </li>;
        let iconHtml = <span className="iconfont icon hide">&#xe64e;</span>;
        if(data.rows){
            iconHtml = <span className="iconfont icon iconChecked hide">&#xe64e;</span>;
            html = data.rows.map(function(item, k){
                return <li key={k} data-id={item.id}>
                    <div className="favorable-value" data-val={item.amount}>
                        <em>￥</em>{item.amount}
                        <span className="iconfont favorable-icon">&#xe63a;</span>
                    </div>
                    <TimeComponent item={item}/>                    
                </li>
            });
        }      
        return (
            <div className="favorable-content redPacket">
                <h3 className="title">{iconHtml}可用额度{this.objVal.moneyYes ? this.objVal.moneyYes.toFixed(2) : ""}元<em>（投资金额×{redEnvelopeRateMax}%）</em></h3>
                <ul className="list redPacketList clearfix">
                    {html} 
                </ul>
                <div className="page page-center mt30"><Pagination defaultCurrent={data.page} pageSize={data.pageSize} total={data.count} onChange={this._pageHander} /></div>
            </div>
        )
    }
}

//加息券
class AddInterest extends React.Component{
    constructor(props){
        super(props);
        this._selectAddInterest = this.selectAddInterest.bind(this);
        this._pageHander = this.pageHander.bind(this);
    }
    pageHander(current){
        this.props.PageHander("Interest",current);
    }
    componentDidMount(){
        //选择加息券
        this._selectAddInterest();
    }
    selectAddInterest(){        
        let self = this;
        $("body").on("click", ".addInterestList li", function(){               
            let icon = $(this).find(".favorable-icon");
            let value = parseFloat($(this).find(".favorable-value").attr("data-val"));
            let id = "";
            let sum = 0;
            if(icon.css("display") == "block"){
               icon.css({"display": "none"}); 
            }
            else
            { 
                $(".addInterestList li").find(".favorable-icon").css({"display": "none"});
                icon.css({"display": "block"});
                sum += value;
                id = $(this).attr("data-id");
            }
            // if(icon.css("display") == "none" && flag){               
            //     icon.css({"display": "block"});                            
            //     sum += value;
            //     flag = false; 
            //     id = $(this).attr("data-id");             
            // } else if(icon.css("display") == "block"){
            //     icon.css({"display": "none"});
            //     sum -= value;
            //     flag = true;
            //     id = "";
            // }            
            //设置选择加息券
            self.props.selectFn(sum, id);                   
        });
    }
    render(){
        let data = this.props.data;
        if(!data){
            return false;
        }
        let html = <li className="noText">
            <span className="iconfont">&#xe662;</span>
            <p>暂无加息券</p>
        </li>;
        if(data.rows){
            html = data.rows.map(function(item, k){                    
                return <li key={k} data-id={item.id}>
                    <div className="favorable-value" data-val={item.upApr}>
                        {item.upApr}<em>%</em>
                        <span className="iconfont favorable-icon">&#xe63a;</span>
                    </div>
                    <TimeComponent item={item}/>                    
                </li>
            });
        }    
        return (
            <div className="favorable-content addInterest">
                <h3 className="title">每次投资仅限选择一张！</h3>
                <ul className="list addInterestList clearfix">
                    {html}  
                </ul>
                <div className="page page-center mt30"><Pagination defaultCurrent={data.page} pageSize={data.pageSize} total={data.count} onChange={this._pageHander} /></div>
            </div>
        )
    }
}

export default class Favorable extends React.Component{
    constructor(props){
        super(props);        
        this._selectFavorableHander = this.selectFavorableHander.bind(this);
        this._handleOk = this.handleOk.bind(this);
        this._handleCancel = this.handleCancel.bind(this); 
        this._favorableHtmlChange = this.favorableHtmlChange.bind(this); 
        this._getRedPacketData = this.getRedPacketData.bind(this);
        this._getAddInterestData = this.getAddInterestData.bind(this); 
        this._selectRedPacketRentalHander = this.selectRedPacketRentalHander.bind(this);//选择红包总额
        this._selectAddInterestHander = this.selectAddInterestHander.bind(this);//选择加息券
        this._hasSelectFavorable = this.hasSelectFavorable.bind(this);//判断是否选择优惠
        this._PageHander = this.PageHander.bind(this);    
        this.amountVal = null;//投资金额
        this.useMoney = $("#useMoney").val();//加息
        this.state = {        
            favorableVisible:false,
            favorableStatus: 0,
            redPacketData: null,
            addInterestData: null,
            selectRedPacketRental: 0,//红包总额
            selectRedPacketIds: "",//选中的红包
            selectAddInteres: 0,//加息券
            selectAddInteresId: "",//选中的加息券
            selectStatus: false,
        };
    }
    selectFavorableHander(){  //选择优惠方法
        let amountVal = $("#amount").val();
        if(amountVal == ""){
            error("投资金额不能为空！请输入");
            return false;
        }
        //红包、加息券弹框
        this.setState({ favorableVisible: true });        
        //获取可用红包
        this._getRedPacketData(this.props.id, amountVal);
        //获取可用加息券
        this._getAddInterestData(this.props.id, amountVal); 
                  
    }
    componentDidMount(){
        //投资金额
        this.amountVal = $("#amount").val();
        //红包、加息券切换
        let self = this;
        $("body").on("click", "#favorableTab a", function(){
            if($(this).hasClass("active")) return;
            $(this).addClass("active").siblings().removeClass("active");
            //内容切换
            self._favorableHtmlChange($(this).index());
        });    
    }
    handleOk() {
        //关闭红包、加息券弹框
        this._handleCancel();
        //是否选择优惠
        this._hasSelectFavorable();        
        //计算实际支付
        this.props.setRealPayAmount(parseFloat(this.amountVal - this.state.selectRedPacketRental));            
    } 
    handleCancel() {
        this.setState({ favorableVisible: false });      
    }
    favorableHtmlChange(index){
        this.setState({favorableStatus: index});
    }
    PageHander(flag,current){
        let amountVal = $("#amount").val();
        if(amountVal == ""){
            error("投资金额不能为空！请输入");
            return false;
        }

        flag == "red" ? this._getRedPacketData(this.props.id, amountVal,current) : this._getAddInterestData(this.props.id, amountVal,current);
    
    }
    getRedPacketData(id, amountVal,current){       
        let _this = this;
        if(!current){
            current = 1;
        }  
        $.ajax({
            url: '/member/coupon/availableRedList.html',
            type: 'POST',
            dataType: 'json',
            data: {"projectId": id, "tenderMoney": amountVal, "page.pageSize": 6,"page.page":current}
        }).done(function(data) {  
            _this.amountVal = amountVal;          
            _this.setState({redPacketData: data.data});
        }).fail(function() {
            error('网络异常，请重试！');
        });
    }
    getAddInterestData(id, amountVal,current){
        let _this = this;
        if(!current){
            current = 1;
        }        
        $.ajax({
            url: '/member/coupon/availableRateList.html',
            type: 'POST',
            dataType: 'json',
            data: {"projectId": id, "tenderMoney": amountVal, "page.pageSize": 6,"page.page":current}
        }).done(function(data) {            
             _this.setState({addInterestData: data.data});
        }).fail(function() {
            error('网络异常，请重试！');
        });
    }
    //选择红包总额
    selectRedPacketRentalHander(val,ids){        
        let id = this.getIdsString(ids);        
        this.setState({selectRedPacketRental: val, selectRedPacketIds: ids});
    }
    getIdsString(ids){
        let len = ids.length;
        let str = "";
        for(let i = 0; i < len; i++){
            if(i == len-1){
                str += ids[i];
            }else{
                str += ids[i] + ",";
            }            
        }
        return str;
    }
    selectAddInterestHander(val, id){  
        this.setState({selectAddInteres: val, selectAddInteresId: id});
    }
    //判断是否选择优惠
    hasSelectFavorable(){
        if(this.state.selectRedPacketRental || this.state.selectAddInteres){
            this.setState({selectStatus: true});
        }
    } 
    render(){
        //选择优惠弹窗标题
        let favorableModalTitle = <div className="favorable-modal-title" id="favorableTab"><a href="javascript:;" className="active">红包</a><a href="javascript:;">加息券</a></div>;
        //红包组件
        let redPacketHtml = <RedPacket data={this.state.redPacketData} amountVal = {this.amountVal} PageHander = {this._PageHander} selectFn = {this._selectRedPacketRentalHander} />;
        let redPacketFooterHtml = <span>选中红包总额<em>{this.state.selectRedPacketRental}</em>元</span>;
        //加息券组件
        let addInterestHtml = <AddInterest data={this.state.addInterestData} PageHander = {this._PageHander}  selectFn = {this._selectAddInterestHander} />;
        let addInterestFooterHtml = <span>&nbsp;&nbsp;&nbsp;&nbsp;加息券<em>{this.state.selectAddInteres}%</em></span>;
        
        //判断是否使用红包
        if($("#redEnvelopeUseful").val() != '1' && $("#additionalRateUseful").val() == "1"){//无红包 有加息券
            favorableModalTitle = <div className="favorable-modal-title" id="favorableTab"><a href="javascript:;">加息券</a></div>;
            redPacketHtml = "";
            redPacketFooterHtml = "";
            this.state.favorableStatus = 1;//显示加息券内容
        }
        //判断是否已使用加息券                     
        if($("#redEnvelopeUseful").val() == "1" && parseFloat(this.useMoney) > 0 && $("#additionalRateUseful").val() != "1"){//有红包 无加息券
            favorableModalTitle = <div className="favorable-modal-title" id="favorableTab"><a href="javascript:;" className="active">红包</a></div>;
            addInterestHtml = "";
            addInterestFooterHtml = "";
        }
        //无红包 无加息券

        //红包、加息券
        let redPacketDisplay = "block";
        let AddInterestDisplay = "none";
        if(this.state.favorableStatus == 0){
            redPacketDisplay = "block";
            AddInterestDisplay = "none";
        } else {
            redPacketDisplay = "none";
            AddInterestDisplay = "block";
        }
        //选择与重新选择
        let selectHtml = <span className="select-btn" onClick={this._selectFavorableHander}>请选择优惠</span>;
        if((this.state.selectRedPacketRental > 0)&&(this.state.selectAddInteres==0)){//已选择红包与加息券的状态
            selectHtml = <p id="favorableText" data-red={this.state.selectRedPacketRental}><input type="hidden" name="redEnvelopeIds" value={this.state.selectRedPacketIds} /><input type="hidden" name="rateCouponId" value={this.state.selectAddInteresId} /><span className="icon iconfont">&#xe64e;</span>已选择{this.state.selectRedPacketRental}元红包<span className="select-btn" onClick={this._selectFavorableHander}>重新选择</span></p>
        }else if((this.state.selectAddInteres > 0)&&(this.state.selectRedPacketRental == 0)){
            selectHtml = <p id="favorableText" data-red={this.state.selectRedPacketRental}><input type="hidden" name="redEnvelopeIds" value={this.state.selectRedPacketIds} /><input type="hidden" name="rateCouponId" value={this.state.selectAddInteresId} /><span className="icon iconfont">&#xe64e;</span>已选择{this.state.selectAddInteres}%加息券<span className="select-btn" onClick={this._selectFavorableHander}>重新选择</span></p>
        }else if((this.state.selectRedPacketRental>0)&&(this.state.selectAddInteres>0)){
            selectHtml = <p id="favorableText" data-red={this.state.selectRedPacketRental}><input type="hidden" name="redEnvelopeIds" value={this.state.selectRedPacketIds} /><input type="hidden" name="rateCouponId" value={this.state.selectAddInteresId} /><span className="icon iconfont">&#xe64e;</span>已选择{this.state.selectRedPacketRental}元红包，{this.state.selectAddInteres}%加息券<span className="select-btn" onClick={this._selectFavorableHander}>重新选择</span></p>
        }else{
            selectHtml = <p id="favorableText" data-red={this.state.selectRedPacketRental}><input type="hidden" name="redEnvelopeIds" value={this.state.selectRedPacketIds} /><input type="hidden" name="rateCouponId" value={this.state.selectAddInteresId} /><span className="select-btn" onClick={this._selectFavorableHander}>请选择优惠</span></p>
        }       
        return (
            <div className="favorable">
                {selectHtml}
                <Modal ref="favorableModal" className="favorable-modal"
                    visible={this.state.favorableVisible}
                    title={favorableModalTitle} onOk={this._handleOk} onCancel={this._handleCancel}
                    width={880}
                    footer={[
                    <span key={0} className="msg">{redPacketFooterHtml}{addInterestFooterHtml}</span>,                    
                    <Button key="submit" type="primary" size="large" onClick={this._handleOk}>&nbsp;确认&nbsp;</Button>,
                    ]}
                >
                    <div className="modal-content">
                        <div style={{"display": redPacketDisplay}}>
                            {redPacketHtml}
                        </div> 
                        <div style={{"display": AddInterestDisplay}}>                     
                            {addInterestHtml}
                        </div>               
                    </div>
                </Modal>
            </div>
        )
    }
}