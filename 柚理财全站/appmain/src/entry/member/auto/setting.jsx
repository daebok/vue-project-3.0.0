import '../../../common/lib';
import ReactDOM from 'react-dom';
import React from 'react';
import { Button,Switch,Form,Input,Modal,Checkbox} from 'antd';
const createForm = Form.create;
const FormItem = Form.Item;
const CheckboxGroup = Checkbox.Group;
import  { ValidateFn } from '../../../common/validateFn';
import  { Reg } from '../../../common/regularFun';
import Accountmenu from '../../../component/accountmenu/menu';
import {Publiclib} from '../../../common/common';
import './setting.less';

function error(v) {
  Modal.error({
    title: v,
    okText:"关闭",
    wrapClassName:'tiperror'
  });
}

function redirect(v,url) {
  Modal.error({
    title: "错误提示",
    content: v,
    okText:"跳转",
    wrapClassName:'tiperror',
    onOk() {
      if(url){
        window.location.href = url;
      }
    }
  });
}

function success(v,url) {
  Modal.success({
    title: '提交成功',
    content: v,
    okText:"确定",
    onOk() {
      if(url){
        window.location.href = url;
      }
    }   
  });
}

class Auto extends React.Component{
  constructor(props){
    super(props);
    this.state = {
      data : null,
      monthType:'',
      dayType:'',
      plainOptions:[],
      CheckboxGroup:[],
      OptionsId:[],
      display:true,
      realizeUseful:'',
      bondUseful:''
    }
    this.handleSubmit = this.handleSubmit.bind(this); 
    this.onChange = this.onChange.bind(this);
    this.getInfo = this.getInfo.bind(this); 
    this.CheckboxChange = this.CheckboxChange.bind(this);
    this.handleReset = this.handleReset.bind(this);
    this.switch = this.switch.bind(this);
    this.filter = this.filter.bind(this);
    this.close = this.close.bind(this);
    this.canuse = this.canuse.bind(this);
    this.setdefault = this.setdefault.bind(this);  
  }
  handleSubmit(e) {
    this.props.form.validateFields((errors, values) => {
      e.preventDefault();
      if (!!errors) {        
        console.log('Errors in form!!!');
        return;
      }
      if(!this.state.OptionsId.length){
        error("请至少选择一项收益方式");
        return;
      }
      let monthLimitMin = $("#monthLimitMin").val(),
          monthLimitMax = $("#monthLimitMax").val(),
          dayLimitMax = $("#dayLimitMax").val(),
          dayLimitMin = $("#dayLimitMin").val();
      if( this.state.monthType == "1" ){
        if( monthLimitMin == ""  || monthLimitMax == "" ||  Reg.isNumber(monthLimitMax) ||  Reg.isNumber(monthLimitMin) )
          {
            error("请填写月期限");
            return;
          }
          if( Number(monthLimitMin) > Number(monthLimitMax) )
          {
            error("月范围最大值不能小于最小值");
            return;
          }
      }
      if( this.state.dayType == "1" ){
        if( dayLimitMin == ""  || dayLimitMax == "" ||  Reg.isNumber(dayLimitMax) ||  Reg.isNumber(dayLimitMin) )
          {
            error("请填写天期限");
            return;
          }
          if( Number(dayLimitMin) > Number(dayLimitMax) )
          {
            error("天范围最大值不能小于最小值");
            return;
          }         
      }      
      console.log('Submit!!!');
      console.log(values);
      let self = this;
      $.ajax({
        url: '/member/addAutoInvest.html',
        type: 'POST',
        dataType: 'json',
        data: values
      })
      .done(function(data) {
        if(typeof data.result != undefined && data.result){
          success(data.msg,data.url);
        }
        else
        {
          error(data.msg,data.url);
        }  
      })
      .fail(function() {
        error('网络异常，请重试！');
      });

    });

  }
  getInfo(){
      let self = this;
      $.ajax({
        url: '/member/autoInvestRule.html',
        type: 'POST',
        dataType: 'json'
      })
      .done(function(data) {
        if(!data.rule){//未使用自动投资
          data["rule"]={
            "status":true,
            "repayStyles":""
          }
        }
        if(typeof data.result != undefined && data.result){

            let OptionsArry = [];
            data.repayStyles.map(function(k,v){
              OptionsArry[v] = k.itemName
            });//获取所有收益方式

            let CheckboxGroup = [],selectOptionsLen = data.rule.repayStyles.split(",");
            selectOptionsLen.map(function(k,v){
              CheckboxGroup[v] = OptionsArry[k-1]
            });//获取已经选择的收益方式

            let OptionsId = [],display = data.rule.status == "1" ? true : false;
            CheckboxGroup.map(function(k,v){
              OptionsId.push(OptionsArry.indexOf(CheckboxGroup[v]) + 1);
            });//设置收益方式对应value提交后端接收

           if(OptionsId[0] == 0)
           {
            OptionsId = [];
           }

            self.setState({
              data:data,
              plainOptions:OptionsArry,
              CheckboxGroup:CheckboxGroup,
              monthType:data.rule.monthType,
              dayType:data.rule.dayType,
              OptionsId:OptionsId,
              display:display,
              realizeUseful:data.rule.realizeUseful,
              bondUseful:data.rule.bondUseful         
           });

           self.setdefault(data);                
        }
        else
        {
          redirect(data.msg,data.url);
        }  
      })
      .fail(function() {
        error('网络异常，请重试！');
      });
  }
  onChange(value){
    let OptionsId = [],OptionsArry = this.state.plainOptions;
    value.map(function(k,v){
      OptionsId.push(OptionsArry.indexOf(value[v]) + 1);
    });
    if(OptionsId.length){
      OptionsId.remove(0);
    }
    this.setState({
      CheckboxGroup:value,
      OptionsId:OptionsId
    });
    
  }
  setdefault(data){
    this.props.form.setFieldsValue({
      amountDayMax:data.rule.amountDayMax ? data.rule.amountDayMax.toString() : "",
      aprMin:data.rule.aprMin ? data.rule.aprMin.toString() : ""
    });
    if(data.rule.monthType == "1"){
      this.props.form.setFieldsValue({
        monthLimitMin:data.rule.monthLimitMin ? data.rule.monthLimitMin.toString() : "",
        monthLimitMax:data.rule.monthLimitMax ? data.rule.monthLimitMax.toString() : "",
      });     
    }
    if(data.rule.dayType == "1"){
      this.props.form.setFieldsValue({
        dayLimitMin:data.rule.dayLimitMin ? data.rule.dayLimitMin.toString() : "",
        dayLimitMax:data.rule.dayLimitMax ? data.rule.dayLimitMax.toString() : ""
      });     
    }    
  }  
  componentWillMount(){
    this.getInfo();
  }
  CheckboxChange(e){
    if(e.target.id == "j-monthType"){
      this.setState({
        monthType:e.target.checked ? "1" : "0",
      });
    }
    else
    {
      this.setState({
        dayType:e.target.checked ? "1" : "0",
      });      
    }  
  }
  canuse(e){   
    if(e.target.id == "j-realizeUseful"){
      this.setState({
        realizeUseful:e.target.checked ? "1" : "0",
      });
    }
    else
    {
      this.setState({
        bondUseful:e.target.checked ? "1" : "0",
      });      
    }    
  }
  handleReset(){
    this.props.form.resetFields();
  }
  close(){
    let self = this;
    $.ajax({
      url: '/member/closeAutoInvest.html',
      type: 'POST',
      dataType: 'json',
    })
    .done(function(data) {
        if(data.result){
           self.setState({
            display:false
           });
           self.handleReset();
        }
        else
        {
          error(data.msg,data.url);
        }     
    })
    .fail(function() {
      error('网络异常，请重试！');
    });
    
  }
  switch(e){

    if(e){
      this.setState({
        display:true
      });
      //this.getInfo();
    }
    else
    {
      this.close();
    }  

  }
  filter(e){
    let that = e.target;
    Publiclib.setdec(that);
  } 
  render(){
        let data = this.state.data;
        if( data == null ){
          return false;
        }
        const { getFieldProps, getFieldError, isFieldValidating , setFieldsValue } = this.props.form;        
        const amountDayMaxProps = getFieldProps('amountDayMax', {
            validate: [
            {
              rules:[
                { required: true, message: '请输入金额' }
              ],
               trigger: ['onBlur'],
            },
            {
              rules:[
                {validator:ValidateFn.isNumber},
              ], trigger: ['onBlur'],
            }
            ] 
          });       
        const aprMinProps = getFieldProps('aprMin', {
              validate: [
              {
                rules:[
                  { required: true, message: '请输入投资收益' }
                ],
                 trigger: ['onBlur'],
              }
              ] 
            });
        const monthLimitMinProps = getFieldProps('monthLimitMin', {
              validate: [
              {
                rules:[
                  { required:false}
                ],
                 trigger: ['onChange'],
              }
              ] 
            });        
         const monthLimitMaxProps = getFieldProps('monthLimitMax', {
              validate: [
              {
                rules:[
                  { required:false}
                ],
                 trigger: ['onChange'],
              }
              ] 
            });
         const dayLimitMinProps = getFieldProps('dayLimitMin', {
              validate: [
              {
                rules:[
                  { required:false}
                ],
                 trigger: ['onChange'],
              }
              ] 
            });
         const dayLimitMaxProps = getFieldProps('dayLimitMax', {
              validate: [
              {
                rules:[
                  { required:false}
                ],
                 trigger: ['onChange'],
              }
              ] 
            });

        const repayStylesProps = getFieldProps("repayStyle",{
            initialValue:this.state.OptionsId
        });
        const monthTypeProps = getFieldProps("monthType",{
            initialValue:this.state.monthType
        });
        const dayTypeProps = getFieldProps("dayType",{
            initialValue:this.state.dayType
        });
        const realizeUsefulProps = getFieldProps("realizeUseful",{
            initialValue:this.state.realizeUseful
        });
        const bondUsefulProps = getFieldProps("bondUseful",{
            initialValue:this.state.bondUseful
        });  
    return(
      <div>
        <div className="auto-nav">
          <div className="auto-control"><spn className="control-status">{this.state.display ? "已经开启" : "已经关闭"}</spn><Switch defaultChecked={data.rule.status == "1" ? true : false} onChange={this.switch} /></div>
          <div className={data.num ? "ranking" : "ranking hidden"} >当前排名<span className="ranking-num">{data.num}/{data.sum}</span></div>
        </div>
        <div className={this.state.display ? "auto-form" : "auto-form hidden"}>
             <Form horizontal className="form-themes" form={this.props.form} onSubmit={this.handleSubmit}>
                <div className="row" style={{width:800}}>
                  <div className="col-3 lab form-laber textR">可用余额</div>
                  <div className="col-21 pl20 form-text lab">
                    <span className="money"><span className="money-num">{data.account.useMoney ? data.account.useMoney : "0"}</span>元<a href="/member/recharge/detail.html">充值</a></span>
                  </div>
                </div>                                                                
                <div className="row" style={{width:800}}>
                  <div className="col-3 lab form-laber textR">单日最高投</div>
                  <div className="col-9 pl20 hasunit">
                  <FormItem hasFeedback ><Input {...amountDayMaxProps}  placeholder="请输入投资金额" maxLength="10" /></FormItem>
                  <span className="unit">元</span>
                  </div>
                </div>
                <div className="row" style={{width:800}}>
                  <div className="col-3 lab form-laber textR">收益方式</div>
                  <div className="col-12 pl20 lab" style={{height:"auto"}}>
                  <FormItem hasFeedback ><CheckboxGroup options={this.state.plainOptions} defaultValue={this.state.CheckboxGroup} onChange={this.onChange} /></FormItem>
                  </div>
                </div>
                <div className="row" style={{width:800,height:"auto",overflow:"hidden"}}>
                  <div style={{overflow:"hidden"}}>
                    <div className="col-3 lab form-laber textR">投资期限</div>                  
                    <div className="col-2 ml20">
                      <FormItem className="fl"><Checkbox id="j-monthType" checked={this.state.monthType == "1" ? true : false} onChange={this.CheckboxChange} ></Checkbox></FormItem><span className="fl checkboxtext">月范围</span>
                    </div>
                    <div className="col-2">
                      <FormItem ><Input className="input-sm" {...monthLimitMinProps} maxLength="3" /></FormItem>
                    </div>
                    <div className="col-1 lab lab-line">-</div>
                    <div className="col-2">
                      <FormItem ><Input className="input-sm" {...monthLimitMaxProps} maxLength="3" /></FormItem>
                    </div>
                    <div  className="col-2 pl20 lab">个月</div>
                  </div>
                  <div style={{overflow:"hidden"}}>
                    <div className="col-3 lab form-laber textR"> </div>                  
                    <div className="col-2 ml20">
                      <FormItem className="fl"><Checkbox id="j-dayType" checked={this.state.dayType == "1" ? true : false} onChange={this.CheckboxChange} ></Checkbox></FormItem><span className="fl checkboxtext">天范围</span>
                    </div>
                    <div className="col-2">
                      <FormItem ><Input className="input-sm" {...dayLimitMinProps} maxLength="3" /></FormItem>
                    </div>
                    <div className="col-1 lab lab-line">-</div>
                    <div className="col-2">
                      <FormItem ><Input className="input-sm" {...dayLimitMaxProps} maxLength="3" /></FormItem>
                    </div>
                    <div  className="col-2 pl20 lab">天</div>
                  </div>                 
                </div>
                <div className="row" style={{width:800}}>
                  <div className="col-3 lab form-laber textR">投资收益</div>
                  <div className="col-9 pl20 hasunit">
                  <FormItem hasFeedback ><Input {...aprMinProps} onInput={this.filter} onPaste={this.filter} placeholder="请输入投资收益" maxLength="7" /></FormItem>
                  <span className="unit">%</span>
                  </div>
                  <div  className="col-1 pl20 lab">起</div>
                </div>
                <div className="row" style={{width:800,position:"relative",top:-17,height:49,overflow:"hidden"}}>
                  <div className="col-3 lab form-laber textR"> </div>
                  <div className="col-5 pl20">
                    <FormItem className="fl"><Checkbox id="j-realizeUseful" checked={this.state.realizeUseful == "1" ? true : false} onChange={this.canuse}></Checkbox></FormItem><span className="fl checkboxtext"  >仅可投变现产品</span>
                  </div>
                  <div className="col-5">
                    <FormItem className="fl"><Checkbox id="j-bondUseful" checked={this.state.bondUseful == "1" ? true : false} onChange={this.canuse} ></Checkbox></FormItem><span className="fl checkboxtext"  >仅可投转让产品</span>
                  </div>                  
                </div>                                                                  
                <Input {...monthTypeProps}  type="hidden" />                      
                <Input {...dayTypeProps}  type="hidden" />                      
                <div className="row" style={{width:800}}>
                  <div className="col-3">&nbsp;</div>
                  <div className="col-21 pl20">
                    <FormItem><Button className="subbutton" type="primary" htmlType="submit" >保存设置</Button></FormItem>
                  </div>
                </div>                               
            </Form>         
        </div>
      </div>
      );
  }
}
Auto = createForm()(Auto);
ReactDOM.render(<Auto />,  document.getElementById("j-auto"));
ReactDOM.render(<Accountmenu current = {"1"}/>,  document.getElementById("j-sider-menu"));