import '../../common/lib';
import ReactDOM from 'react-dom';
import React from 'react';
import './qualification.less';
import  { ValidateFn} from '../../common/validateFn';
import  { Reg } from '../../common/regularFun';
import {Publiclib} from '../../common/common';
import Imgupload from '../../component/ImageUpload/uploadAddBorrowPage';
import {Select, Button, Form, Input,Modal, Icon,Radio} from 'antd';
const Option = Select.Option;
const RadioGroup = Radio.Group;
const createForm = Form.create;
const FormItem = Form.Item;

function error(v) {
  Modal.error({
    title: v,
    okText:"关闭",
    wrapClassName:'tiperror'
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

function showerror(msg,url,btnname) {
  if(!btnname){
    btnname = "点击跳转";
  }
  Modal.error({
    title: '温馨提示',
    content: msg,
    okText:btnname,
    iconType:'question-circle',
    align:{top:'200px'},
    onOk() {
        window.location.href = url;
    },
    onCancel() {},
  });
}

let flagObj = $("body").data("flag");
if(!flagObj.result){
     showerror(flagObj.msg,flagObj.url,flagObj.buttonName);
     //showerror(flagObj.msg,flagObj.url);
}

let BorrowForm = React.createClass({
  picPath:[],
  mtgPath:[],
  getInitialState() {
    return {
      data: '',
      timeType:"0",
      pawnClassName : false,
      pawnValue:"0",
      guaranteeValue:"0",
      disabled_2:true,
      disabled_5:true,
      borrowUse:""
    };
  },
   handleSubmit(e) {
    e.preventDefault();  
    this.props.form.validateFields((errors, values) => {
      if (!!errors) {        
        console.log('Errors in form!!!',errors);        
        return;
      }

      if(values.repayStyle == ""){
        console.log(values);
        error("请选择还款方式！");
        return;
      }

      if( values.repayStyle != "2" && this.state.timeType == "0" && values.timeLimit == "1"){
        error("借款期限为一个月，还款方式只能为一次性还款！");
        return;
      }

      if( values["repayStyle"] == "5" && (Number(values.timeLimit) % 3 != 0)){
        this.props.form.resetFields(["timeLimit"]);
        this.props.form.validateFields(["timeLimit"]); 
        error("当前还款方式为每季还息到期还本，借款期限需为3的整数倍。");               
        return;          
      }

      if(!this.picPath.length){
        error("请上传借款资料图片");
        return;
      }

      if(!this.mtgPath.length && this.state.pawnValue == "1"){
        error("请上传抵押物资料图片");
        return;
      }
      else if(this.state.pawnValue == "0")
      {
        this.mtgPath = "";
      }
      
      values.picPaths = this.picPath;
      values.mtgPaths = this.mtgPath;
      let self = this;
      values[$("body").data("tokenname")] = $("body").data("token");
      values["timeType"] = this.state.timeType;
      if(values["repayStyle"] == "5"){
        values["fixedRepayDay"] = values["fixedRepayDayQuarter"];
      }
      $.ajax({
        url: '/borrow/addBorrowDetail.html',
        type: 'POST',
        dataType: 'json',
        data: values
      })
      .done(function(data) {
        if(data.result){
          //success(data.msg,'/borrow/addBorrow.html');
          window.location.href = "/borrow/addBorrow.html";
        }
        else
        {
          error(data.msg);
        }  
      })
      .fail(function() {
        error('网络异常，请重试！');
      });
    });
  },  
  getInfo(){
    const self = this;
    $.ajax({
      url: '/borrow/getBorrowData.html',
      type: 'POST',
      dataType: 'json',
    })
    .done(function(data) {
    
      if(!data.borrow){
        data.borrow = {}
      }

      let borrowUse = "";

      if(data.borrow.borrowUse){
        borrowUse = data.borrow.borrowUse;
      }
      else
      {
        borrowUse = data.borrowUseList[0].itemValue;
      }  

      self.setState({
        data:data,
        borrowUse:borrowUse,
        timeType:data.borrow.timeType ? data.borrow.timeType : "0",
        pawnValue: data.borrow.mtgPath ? "1" : "0",
        guaranteeValue:data.borrow.isVouch 
      });


      let isvouch = "0";

      if(data.borrow.isVouch && data.borrow.isVouch == "1"){
        isvouch = "1";
      }

      self.props.form.setFieldsValue({
        repayStyle:data.borrow.repayStyle,
        content:data.borrow.content,
        pawn:data.borrow.mtgPath ? "1" : "0",
        isVouch:isvouch
      });

      if(data.borrow.projectName){
        self.props.form.setFieldsValue({
          projectName:data.borrow.projectName
        });
      }

      if(data.borrow.fixedRepayDay){
          if(data.borrow.repayStyle == "3"){
            self.props.form.setFieldsValue({
              fixedRepayDay: data.borrow.fixedRepayDay.toString()
            });
            self.setState({
              disabled_2 : false
            });            
          }
          if(data.borrow.repayStyle == "5"){
            self.props.form.setFieldsValue({
              fixedRepayDayQuarter: data.borrow.fixedRepayDay.toString()
            });
            self.setState({
              disabled_5 : false
            });                        
          }
      }

      if(data.borrow.timeLimit){
          self.props.form.setFieldsValue({
            timeLimit: data.borrow.timeLimit.toString()
          });
      }      
      if(data.borrow.apr){
          self.props.form.setFieldsValue({
            apr: data.borrow.apr.toString()
          });
      }
      if(data.borrow.account){
        self.props.form.setFieldsValue({
          account:data.borrow.account.toString()
        });     
      }

    })
    .fail(function() {
      error('网络异常，请重试！');
    });
    
  },
  componentDidMount(){
    this.getInfo();
  },
  floating(rule, value, callback) {
    if(!value){
      callback();
    }else if(this.moneyRrate(value)){
        callback([new Error('请输'+ this.state.data.borrowMinApr +'-'+ this.state.data.borrowMaxApr +'%并且最多两位小数的年利率')]);        
    }else{
      callback();
    }    
  },
  changeTimeType(value){
    console.log(`selected ${value}`);
    this.setState({
      timeType : value
    });
    if(value == "1"){
      $(".ant-radio-inner")[0].click();
    }
    else
    {
      $(".ant-radio-checked").removeClass("ant-radio-checked");
      this.props.form.setFieldsValue({
        repayStyle:""
      });     
    }  
  },
  changePawn(e){
    console.log('radio checked', e.target.value);
    this.setState({
      pawnValue: e.target.value,
    });
    this.props.form.setFieldsValue({
      pawn:e.target.value,
    });     
  },
  changeGuarantee(e){
    console.log('radio checked', e.target.value);
    this.setState({
      guaranteeValue: e.target.value
    });

    this.props.form.setFieldsValue({
      isVouch:e.target.value,
    });     
  },
  changeRepayStyle(e){
    //if(this.state.repayStyle != e.target.value){
      this.props.form.setFieldsValue({
        repayStyle:e.target.value
      });
      // this.props.form.setFieldsValue({
      //   fixedRepayDay: "",
      //   fixedRepayDayQuarter:""
      // });      
    //}
    if( e.target.value != "5" || e.target.value != "3"){
      this.setState({
        disabled_2 : true,
        disabled_5 : true
      });
    }
    if(e.target.value == "3"){
      this.setState({
        disabled_2 : false
      });
    }    
    if(e.target.value == "5"){
      this.setState({
        disabled_5 : false
      });
    }
  },
  checkNumber(e){
    let that = e.target;
    if(e.target.value<=0){
      e.target.value=''
    }
    Publiclib.setint(that);
  },
  checkAccount(rule, value, callback) {
    if(!value){
      callback();
    }else if(Reg.isNumber(value)){        
        callback([new Error('抱歉，请输入正整数')]);        
    }else if(value > this.state.data.borrowMaxAccount || value < this.state.data.borrowMinAccount){        
        callback([new Error('必须大于等于'+ this.state.data.borrowMinAccount +'小于等于'+ this.state.data.borrowMaxAccount)]);        
    }else{
      callback();
    }    
  },
  moneyRrate(value){
    return value < this.state.data.borrowMinApr || value > this.state.data.borrowMaxApr  ||  ! /^([1-9]\d*|0)(?:\.\d{0,2})?$/.test(value);
  },   
  render() {
    let data = this.state.data;
    if(!data){
      return false;
    }
    const { getFieldProps, getFieldError, isFieldValidating,setFieldsValue} = this.props.form;

    const projectNameProps = getFieldProps('projectName', {
      validate: [{
      rules:[
        { required: true,whitespace: true, message: '请输入借款标题' }
      ],trigger: ['onBlur'],        
    }]        
    });
    const accountProps = getFieldProps('account', {
          validate: [
          {
            rules:[
              { required: true, message: '请输入借款金额' }
            ],
             trigger: ['onBlur'],
          },
          {
            rules:[
              {validator:this.checkAccount},
            ], trigger: ['onBlur'],
          }
          ]  
        });  
    const aprProps = getFieldProps('apr', {
          validate: [
          {
            rules:[
              { required: true, message: '请输入年利率' }
            ],
             trigger: ['onBlur'],
          },
          {
            rules:[
              {validator:this.floating},
            ], trigger: ['onBlur'],
          }
          ]  
        });

    const timeLimitProps = getFieldProps('timeLimit', {
          validate: [
          {
            rules:[
              { required: true, message: '请输入借款期限' }
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
    // const timeTypeProps =  getFieldProps('timeType', {
    //         initialValue:this.state.timeType,
    //         rules: [
    //             { required: true, message: '选择类型期限' },
    //         ],
    //     });
    const borrowUseProps = getFieldProps('borrowUse', {
      initialValue:this.state.borrowUse,
      rules:[
        { required: true, message: '请选择借款用途' }
      ]               
    });    
    const repayStyleProps = getFieldProps('repayStyle', {
      rules:[
        { required: false, message: '请选择还款方式' }
      ]               
    });      
    const contentProps = getFieldProps('content', {
      rules:[
        { required: true,max:2000,message: '请输入借款详情，允许输入最大长度为2000。' }
      ]               
    });     
    const pawnProps = getFieldProps('pawn', {
      rules:[
        { required: true, message: '请选择是否有抵押物' }
      ]               
    });
    const guaranteeProps = getFieldProps('isVouch', {
      rules:[
        { required: true, message: '请选择是否有担保公司' }
      ]               
    });

    //let guaranteeSelectValue = "";

    // if(data.userCompanyList.length){
    //   guaranteeSelectValue = data.userCompanyList[0].userId;
    // }

    const guaranteelistProps = getFieldProps('vouchId', {
      initialValue:data.borrow.vouchId ? data.borrow.vouchId : "9999",
      rules:[
        { required: false, message: '请选择担保公司' }
      ]               
    });

    const fixedRepayDayProps = getFieldProps('fixedRepayDay', {
      rules:[
        { required: false, message: '请输入固定还款日' }
      ]
    });

    const fixedRepayDayQuarterProps = getFieldProps('fixedRepayDayQuarter', {
      rules:[
        { required: false, message: '请输入固定还款日' }
      ]
    });

    const timeTypeMonth =<RadioGroup {...repayStyleProps} defaultValue="2" onChange={this.changeRepayStyle}>                
                            <Radio value="2">一次性还款</Radio>
                            <Radio value="1">等额本息</Radio>
                            <Radio value="4">等额本金</Radio>
                            <Radio value="3">每月还息到期还本（固定还款日<FormItem className="sminput" style={{display:"inline-block",width:40,lineHeight:"30px",height:"30px",margin:"0 5px"}}><Input style={{height:"30px",borderRadius:'2px'}}  maxLength="2"  {...fixedRepayDayProps} disabled={this.state.disabled_2} onInput={this.checkNumber} /></FormItem>日）</Radio>
                            <Radio value="5">每季还息到期还本（固定还款日<FormItem className="sminput" style={{display:"inline-block",width:40,lineHeight:"30px",height:"30px",margin:"0 5px"}}><Input style={{height:"30px",borderRadius:'2px'}}  maxLength="2"  {...fixedRepayDayQuarterProps} disabled={this.state.disabled_5} onInput={this.checkNumber} /></FormItem>日）</Radio>
                          </RadioGroup>;
    const timeTypeDay = <RadioGroup {...repayStyleProps} defaultValue="2" onChange={this.changeRepayStyle}>            
                          <Radio value="2">一次性还款</Radio>
                        </RadioGroup>;
    let timeTypeHtml =  this.state.timeType == "1" ?  timeTypeDay : timeTypeMonth;                                     
    return (
      <Form horizontal className="form-themes" form={this.props.form} onSubmit={this.handleSubmit}>
        <div className="add-borrow">
          <div className="row" style={{width:840}}>
          <div className="col-4 lab form-laber textR pr20"><span className="redflag">*</span>借款标题</div>
            <div className="col-20">
              <FormItem hasFeedback><Input {...projectNameProps} placeholder="请输入借款标题" maxLength="25" /></FormItem>
            </div>                                                    
          </div>
           <div className="row" style={{width:840}}>
            <div className="col-12">
              <div className="row" style={{width:"100%"}}>
                <div className="col-8 lab form-laber textR pr20">
                  <span className="redflag">*</span>借款金额
                </div>
                <div className="col-16 hasunit">
                  <FormItem hasFeedback><Input {...accountProps} placeholder="请输入借款金额" maxLength="10" /></FormItem>
                  <span className="unit" >元</span>
                </div>                               
              </div>
            </div>
            <div className="col-12">
                <div className="row" style={{width:"100%"}}>
                  <div className="col-8 lab form-laber textR pr20">
                    <span className="redflag">*</span>年利率
                  </div>
                  <div className="col-16 hasunit">
                    <FormItem hasFeedback><Input {...aprProps} placeholder="请输入年利率" maxLength="7" /></FormItem>
                    <span className="unit" >%</span>
                  </div>                               
                </div>
            </div>                                                    
          </div>
           <div className="row" style={{width:840}}>
            <div className="col-12">
              <div className="row" style={{width:"100%"}}>
                <div className="col-8 lab form-laber textR pr20">
                  <span className="redflag">*</span>借款期限
                </div>
                <div className="col-10">
                  <FormItem hasFeedback><Input {...timeLimitProps} placeholder="请输入借款期限" maxLength="5" /></FormItem>
                </div>
                <div className="col-6 pl20">
                  <FormItem>
                      <Select defaultValue={this.state.timeType} style={{ width: '100%' }}  onChange={this.changeTimeType} >
                          <Option value="0">月</Option>
                          <Option value="1">天</Option>                         
                      </Select>
                  </FormItem>
                </div>                                             
              </div>
            </div>
            <div className="col-12">
                <div className="row" style={{width:"100%"}}>
                  <div className="col-8 lab form-laber textR pr20">
                    <span className="redflag">*</span>借款用途
                  </div>
                  <div className="col-16">
                  <FormItem>
                      <Select {...borrowUseProps}  style={{ width: '100%' }}>
                          {
                            data.borrowUseList.map(function(k,v){                                    
                              return <Option key={v} value={k.itemValue} title={k.itemName}>{k.itemName}</Option>
                            })
                          }                 
                      </Select>
                  </FormItem>
                  </div>                               
                </div>
            </div>                                                   
          </div>
          <div className="row" style={{width:840,margin:"0 0 24px"}}>
            <div className="col-4 lab form-laber textR pr20"><span className="redflag">*</span>还款方式</div>
            <div className="col-20 radio_type">
              <FormItem>
                  {timeTypeHtml}
              </FormItem>  
            </div>                                                    
          </div>
          <div className="row" style={{width:840}}>
            <div className="col-4 lab form-laber textR pr20"><span className="redflag">*</span>借款详情</div>
            <div className="col-20">
              <FormItem>
                <Input type="textarea" {...contentProps} placeholder="请输入借款详情"  style={{height:100}} />
              </FormItem>
            </div>                                                    
          </div>

          <div className="row" style={{width:840}}>
            <div className="col-4 lab form-laber textR pr20"><span className="redflag">*</span>借款资料</div>
            <div className="col-20" style={{padding:"11px 0 20px 0"}}>
              <dl>
                <dt><span className="help-text">格式要求：支持jpg、jpeg、bmp、gif、png格式图片，大小不超过1M,最多十五张。</span></dt>
                <dd className="pic-list">
                  <Imgupload eq={0} data = {data.borrow.picPath} imgArray = {this.picPath} len ={15} />
                </dd>
              </dl>
            </div>
          </div>
         
          <div className="row" style={{width:840}}>
            <div className="col-4 lab form-laber textR pr20"><span className="redflag">*</span>抵押物</div>
            <div className="col-20 lab">
              <FormItem>
                <RadioGroup {...pawnProps} onChange={this.changePawn}>
                  <Radio value="0">无</Radio>
                  <Radio value="1">有</Radio>                                
                </RadioGroup>
              </FormItem>                 
            </div>                                                    
          </div>
          <div className={this.state.pawnValue == "0" ? "row hide" : "row"} style={{width:840}}>
            <div className="col-4 lab form-laber textR pr20"><span className="redflag">*</span>抵押物资料</div>
            <div className="col-20" style={{padding:"11px 0 20px 0"}}>
              <dl>
                <dt><span className="help-text">格式要求：支持jpg、jpeg、bmp、gif、png格式图片，大小不超过1M,最多十张。</span></dt>
                <dd className="pic-list">
                  <Imgupload eq={1} data = {data.borrow.mtgPath} imgArray = {this.mtgPath} len ={10} />
                </dd>
              </dl>
            </div>                                                    
          </div>
          <div className={data.userCompanyList.length ? "row" : "row hide"} style={{width:840}}>
            <div className="col-4 lab form-laber textR pr20"><span className="redflag">*</span>是否担保</div>
            <div className="col-20 lab">
            <FormItem>
                <RadioGroup {...guaranteeProps} onChange={this.changeGuarantee}>
                  <Radio value="0">否</Radio>
                  <Radio value="1">是</Radio>
                </RadioGroup>
            </FormItem>                   
            </div>                                                    
          </div> 
          <div className={this.state.guaranteeValue == "1" ? "row" : "row hide"} style={{width:840}}>
            <div className="col-4 lab form-laber textR pr20"><span className="redflag">*</span>担保公司</div>
            <div className="col-20 lab">
                  <FormItem>
                      <Select {...guaranteelistProps} style={{ width: '50%' }}>
                        <Option value="9999">请选择</Option>
                          {
                            data.userCompanyList.map(function(k,v){                                    
                              return <Option key={v} value={k.id} title={k.realName}>{k.realName}</Option>
                            })
                          }                       
                      </Select>
                  </FormItem>              
            </div>                                                    
          </div>
        </div>
        <div className="btn"><FormItem><Button className="subbutton" type="primary" htmlType="submit" >确认提交</Button></FormItem></div>                
      </Form>
    )
  }
});
BorrowForm = createForm()(BorrowForm);
ReactDOM.render(<BorrowForm />, document.getElementById("j-addBorrowPage"));