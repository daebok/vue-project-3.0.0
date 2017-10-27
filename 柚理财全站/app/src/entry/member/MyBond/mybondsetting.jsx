import '../../../common/lib';
import ReactDOM from 'react-dom';
import React from 'react';
import Accountmenu from '../../../component/accountmenu/menu';
import './mybondsetting.less';
import  { ValidateFn } from '../../../common/validateFn';
import  { Reg } from '../../../common/regularFun';
import {Publiclib} from '../../../common/common';
import { Button, Form, Modal, Input,Checkbox,InputNumber} from 'antd';
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

let Mybondsetting = React.createClass({
  getInitialState() {
    return {
      checked: true,
      visible: false,
      content:'',
      protocolType:'债权协议'
    };
  },
  handleSubmit(e) {
    e.preventDefault();
    this.props.form.validateFields((errors, values) => {
      if (!!errors) {
        console.log('Errors in form!!!');
        return;
      }
      if(!this.state.checked){
        console.log('unchecked');
        return;
      }
      console.log('Submit!!!');
      console.log(values);
      values[this.props.tokenName]=this.props.tokenValue;
      $.ajax({
        url: '/member/myBond/bondSetCommit.html',
        type: 'POST',
        dataType: 'json',
        data: values
      })
      .done(function(data) {
        if(data.result){
          success(data.msg,data.url);
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
  onChange(e) {
    this.setState({
      checked:e.target.checked,
    });
  },
  compute(e){
    if(e){
      Publiclib.setdec(e.target);
    }
    let bondMoney = $("#bondMoney").val(), bondApr = $("#bondApr").val();
    if( bondMoney != "" && bondApr != "" && !isNaN(bondMoney) && !isNaN(bondApr) ){
      $("#j-computeResult").html(((1 + bondApr/100)*bondMoney).toFixed(2));
    }
  },
    handleCancel() {
        this.setState({ visible: false });
    },
    computeChange(value){
    this.props.form.setFieldsValue({
      bondApr:value
    });
    this.compute();
  },
  filter(e){
    Publiclib.setnegative(e.target);
  },
  checkNumber(rule, value, callback) {
    if(!value){
      callback();
    }else if(value > Number(this.props.max)){
        callback([new Error('抱歉，输入值不能高于最高溢价率')]);
    }else if(value < Number(this.props.min)){
        callback([new Error('抱歉，输入值不能低于最低溢价率')]);
    }else{
        callback();
    }
  },
    getInfo(){
        let self = this;
        $.ajax({
            url: '/member/myBond/getBondProtocolContent.html',
            type: 'POST',
            dataType: 'json',
            data: {protocolId:self.props.protocolId,bondId:''},
        })
            .done(function(data) {
                if(data.result){
                    self.setState({
                        visible: true,
                        content:data.content,
                        protocolType:data.protocolType
                    });
                }
                else
                {
                    error(data.msg);
                }
            })
            .fail(function() {
                error('网络异常，请重试！');
            });
    },
  render(){
      const { getFieldProps, getFieldError, isFieldValidating } = this.props.form;
      const bondMoneyProps = getFieldProps('bondMoney', {
          initialValue : this.props.bondMoney,
            validate: [
            {
              rules:[
                { required: true, message: '请输入转让债权金额' }
              ],
               trigger: ['onBlur'],
            }
            ]
          });
      const agreeProps = getFieldProps("agree",{
          initialValue:"1"
      });
      const bondAprProps = getFieldProps("bondApr",{
            validate: [
            {
              rules:[
                { required: true, message: '请输入折溢价率' }
              ],
               trigger: ['onBlur'],
            },
            {
              rules:[
                {validator:this.checkNumber},
              ], trigger: ['onBlur'],
            }
            ]
      });

      const projectIdProps = getFieldProps("projectId",{
          initialValue:this.props.projectId
      });
      const ruleIdProps = getFieldProps("ruleId",{
          initialValue:this.props.ruleId
      });
      const investIdProps = getFieldProps("investId",{
          initialValue:this.props.investId
      });
      const userIdProps = getFieldProps("userId",{
          initialValue:this.props.userId
      });
      const startPeriodProps = getFieldProps("startPeriod",{
          initialValue:this.props.startPeriod
      });
      const label = !this.state.checked ? '请阅读并同意《债权转让协议》' : '';

      const maxtext = "折溢价率范围为"+ this.props.min +"% ~ "+ this.props.max +"%";
      let inputSellAll = [];
      if(this.props.sellAll){
          inputSellAll=<Input {...bondMoneyProps} readOnly  placeholder="请输入转让债权金额" maxLength="10" onKeyUp={this.compute}  />
      }else{
          inputSellAll=<Input {...bondMoneyProps} placeholder="请输入转让债权金额" maxLength="10" onKeyUp={this.compute}  />
      }
      return (
        <div>
          <Form horizontal className="form-themes" style={{marginLeft:100}} form={this.props.form} onSubmit={this.handleSubmit}>
              <div className="row" style={{width:400}}>
                <div className="col-6 lab form-laber textR">可转让债权</div>
                <div className="col-18 pl20 form-text lab">
                   <span className="form-text"><span className="form-textsm">{this.props.bondMoney}</span>元</span>
                </div>
              </div>
              <div className="row" style={{width:400}}>
                <div className="col-6 lab form-laber textR">转让债权</div>
                <div className="col-18 pl20 hasunit">
                  <FormItem hasFeedback >{inputSellAll}</FormItem>
                  <span className="unit">元</span>
                </div>
              </div>
              <div className="row" style={{width:400}}>
                <div className="col-6 lab form-laber textR">折溢价率</div>
                <div className="col-18 pl20 hasunit hasunit-number">
                  <FormItem hasFeedback  extra={maxtext}><InputNumber placeholder="请输入折溢价率" {...bondAprProps} size="large"  step={0.01} min={Number(this.props.min)} max={Number(this.props.max)} onChange={this.computeChange} onKeyUp={this.filter} /></FormItem>
                  <span className="unit">%</span>
                </div>
              </div>
              <div className="row" style={{width:400}}>
                <div className="col-6 lab form-laber textR">转让价格</div>
                <div className="col-18 pl20 form-text lab">
                   <span className="form-text"><span className="form-textbig" id="j-computeResult">0</span>元</span>
                </div>
              </div>
              <div className="row" style={{width:400}}>
                <div className="col-6">&nbsp;</div>
                <div className="col-18 pl20">
                  <FormItem validateStatus="error" {...agreeProps} help = {label}><Checkbox checked={this.state.checked} onChange={this.onChange} ></Checkbox>我已阅读并同意<a href="javascript:;" onClick={this.getInfo}>《债权转让协议》</a></FormItem>
                </div>
              </div>
              <Modal
                  visible={this.state.visible}
                  title={this.state.protocolType} onCancel={this.handleCancel}
                  width={780}
                  footer={[
                      <Button key="back" type="ghost" size="large" onClick={this.handleCancel}>关闭</Button>
                  ]}
              >
                  <div style={{maxHeight:"400px",overflow:"auto"}} dangerouslySetInnerHTML={{__html: this.state.content}}></div>
              </Modal>
              <Input {...projectIdProps}  type="hidden" />
              <Input {...projectIdProps}  type="hidden" />
              <Input {...investIdProps}  type="hidden" />
              <Input {...userIdProps}  type="hidden" />
              <Input {...startPeriodProps}  type="hidden" />
              <Input name={this.props.tokenName}  value={this.props.tokenValue} type="hidden" />

              <div className="row" style={{width:400}}>
                <div className="col-6">&nbsp;</div>
                <div className="col-18 pl20">
                  <FormItem><Button className="subbutton" type="primary" htmlType="submit" >确定</Button></FormItem>
                </div>
              </div>
          </Form>
        </div>
        )
    }
});

Mybondsetting = createForm()(Mybondsetting);

ReactDOM.render(<Mybondsetting projectId={$("#projectId").val()} protocolId={$("#protocolId").val()} userId={$("#userId").val()} ruleId={$("#ruleId").val()} sellAll={$("#sellAll").val()} tokenName={"commitBondToken"} tokenValue={$("input[name='commitBondToken']").val()} investId={$("#investId").val()} startPeriod={$("#startPeriod").val()} bondMoney={$("#Money").val()} max={$("#max").val()} min={$("#min").val()}  bondApr={$("#bondApr").val()} />,  document.getElementById("j-mybond-setting"));

ReactDOM.render(<Accountmenu current = {"4"}/>,  document.getElementById("j-sider-menu"));
