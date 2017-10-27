import React from 'react';
import ReactDOM from 'react-dom';
import { Modal,Button, Form, Input,Select } from "antd";
import './calculatorBottom.less';
import { Reg } from '../../common/regularFun';
const createForm = Form.create;
const FormItem = Form.Item;
function error(v) {
    Modal.error({
        title: v,
        okText:"关闭",
        wrapClassName:'tiperror'
    });
}

let ModalCal = React.createClass({
    getInitialState: function() {
        return {
           flag:false
        };
    },
    componentDidMount(){

    },

    render() {
        return(
            <div className={this.props.visible==true?"cal-bottom cal-show":"cal-bottom"} ref="box" >
               <div className="site-row">
                   {this.props.children}
               </div>
            </div>
        )
    }
})

let Calculator = React.createClass({
    getInitialState: function() {
        return {
            repayType: [{"itemValue":"1","itemName":"等额本息"},{"itemValue":"2","itemName":"一次性还款"},{"itemValue":"3","itemName":"每月还息到期还本"},{"itemValue":"4","itemName":"等额本金"}],
            periodType: '月',
            repayTypeValue:'1',
            periodTypeValue:'0',
            backData:'',
            visible:false,
        };
    },
    calculatorShow(){
         this.setState({
             visible:!this.state.visible
         })
    },
    closeBox(){
        this.setState({
            visible:false
        })
    },        
    handleReset(e) {
        e.preventDefault();
        this.props.form.resetFields();
        this.setState({
            backData:''
        })
    },
    handleCancel(){
        this.closeBox();
    },
    componentWillMount(){
        this.repayType()
    },
    repayType(){
        var _this=this;
    },
    changePeriod(e){
        if(e==0){
            this.setState({
                periodType: '月',
                periodTypeValue:e
            })
        }else if(e==2){
            this.setState({
                periodType: '季',
                periodTypeValue:e
            })
        }else if(e==1){
            this.setState({
                periodType: '天',
                periodTypeValue:e
            })
        }

    },
    changeRepay(e){
        this.setState({repayTypeValue:e})
    },
    handleSubmit(e) {
        e.preventDefault();
        var _this=this;
        this.props.form.validateFields((errors, values) => {
            if (!!errors) {
                console.log('Errors in form!!!');
                return;
            }
            console.log('Submit!!!');
            console.log(values);
            values["periodType"] = this.state.periodTypeValue;
            if(this.state.periodTypeValue==0){
                values["repayStyle"] = this.state.repayTypeValue;
            }else if(this.state.periodTypeValue==1){
                values["repayStyle"] = '2';
            }else if(this.state.periodTypeValue==2){
                values["repayStyle"] = '5';
            }
            $.ajax({
                url: '/member/myAssistant/calculateIncome.html',
                type: 'POST',
                dataType: 'json',
                data: values
            })
                .done(function(data) {
                    if(data.result){
                      _this.setState({
                          backData:data
                      })
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
    RxgYes(rule, value, callback){
        if(!value){
            callback();
        }else if(Reg.isNumber(value)){
            callback([new Error('抱歉，请输入整数金额')]);
        }else{
            callback();
        }
    },
    RxgTimeYes(rule, value, callback){
        if(!value ){
            callback();
        }else if(Reg.isNumber(value)){
            callback([new Error('抱歉，请输入时间')]);
        }else{
            callback();
        }
    },
    RxgAprYes(rule, value, callback){
        if(!value){
            callback();
        }else if(value != 0 && Reg.isApr(value)){
            callback([new Error('抱歉，请输入正确的年利率')]);
        }else if(value<0.01||value>24){
            callback([new Error('抱歉，利率范围为0.01~24')]);
        }else{
            callback();
        }
    },
    render() {
        const { getFieldProps, getFieldError, isFieldValidating } = this.props.form;
        const principal = getFieldProps('principal', {
            rules: [
                { required: true,message: '请输入金额' },
                { validator: this.RxgYes}
            ],
        });
        const period = getFieldProps('period', {
            rules: [
                { required: true,message: '请输入时间' },
                { validator: this.RxgTimeYes}
            ],
        });
        const apr = getFieldProps('apr', {
            rules: [
                { required: true,message: '请输入年化利率' },
                { validator: this.RxgAprYes}
            ],
        });
        const repayStyle = getFieldProps('repayStyle', {
            initialValue:this.state.repayTypeValue,
            rules: [
                { required: true, message: '请选择还款方式'}
            ],
        });
        if(!this.state.repayType){
            return false
        }
        var backDetail=this.state.backData.repayStyle==undefined?'1':this.state.backData.repayStyle;
        var html=''
        if (backDetail==1){
            html=<li className="clearfix">月收本息<i>{this.state.backData.avg==undefined?'0.00':this.state.backData.avg}元</i></li>
        }else if(backDetail==2){
            html=""
        }
        else if(backDetail==3){
            html=<li className="clearfix">月收利息<i>{this.state.backData.avg}元</i></li>
        }
        else if(backDetail==4){
            html=<li className="clearfix">月收本金<i>{this.state.backData.avg}元</i></li>
        }
        else if(backDetail==5){
            html=<li className="clearfix">季收利息<i>{this.state.backData.avg}元</i></li>
        }
        var typeSelect='';
        if(this.state.periodTypeValue==0){
            typeSelect=    <Select defaultValue={this.state.repayTypeValue}  onChange={this.changeRepay}  size="large" >
                {this.state.repayType.map(function (data) {
                    return <Option key={data.itemValue} value={data.itemValue}>{data.itemName}</Option>
                })}
            </Select>
        }else if(this.state.periodTypeValue==1){
            typeSelect=<Input type="text" value='一次性还款' readOnly />
        }else if(this.state.periodTypeValue==2){
            typeSelect=<Input type="text" value='每季还息到期还本' readOnly />
        }
        return (
           <div className="clearfix">
               <ModalCal ref="modal"
                      visible={this.state.visible}
                      title="理财计算器" onCancel={this.handleCancel}>
                   <div className="calculator clearfix">
                        <a href="javascript:;" className="cal-btn" onClick={this.calculatorShow}><i className={this.state.visible==true?"anticon anticon-down":"anticon anticon-up"}></i>理财计算器</a>                       
                        <div className="pull-left input-box" style={{width:'700px'}}>
                           <Form horizontal className="form-themes" form={this.props.form} onSubmit={this.handleSubmit}>
                            <ul>
                                <li className="clearfix"><label className="input-tip">投资金额</label>
                                    <div className="input-w">
                                        <FormItem hasFeedback>
                                        <Input {...principal} type="text" size="small" autoComplete="off" maxLength="10" />
                                        </FormItem>
                                        <span className="icon">元</span>
                                    </div>
                                </li>
                                <li className="clearfix"><label className="input-tip">年化利率</label>
                                    <div className="input-w">
                                        <FormItem hasFeedback>
                                            <Input {...apr} type="text" autoComplete="off" maxLength="5" />
                                        </FormItem>
                                        <span className="icon">%</span>
                                    </div>
                                </li>
                                <li style={{width:'200px'}}>
                                    <Button type="primary" htmlType="submit">计算</Button>
                                </li>
                                <li className="clearfix"><label className="input-tip">投资期限</label>
                                    <div className="input-w">
                                        <div className="site-col-2" style={{width:'60px'}}>
                                            <FormItem>
                                            <Select defaultValue={this.state.periodTypeValue} size="large" onChange={this.changePeriod}>
                                                <Option value="2">季</Option>
                                                <Option value="0">月</Option>
                                                <Option value="1">天</Option>
                                            </Select>
                                            </FormItem>
                                        </div>
                                        <div className="site-col-3" style={{marginRight:'0',width:'70px'}}>
                                            <FormItem hasFeedback>
                                                <Input {...period} type="text" autoComplete="off" maxLength="3" />
                                            </FormItem>
                                            <span className="icon">{this.state.periodType}</span>
                                        </div>

                                    </div>
                                </li>

                                <li className="clearfix"><label className="input-tip">还款方式</label>
                                    <div className="input-w">
                                        <FormItem>
                                            {typeSelect}
                                         </FormItem>

                                    </div>
                                </li>
                                <li  style={{width:'200px',textAlign:'center'}}>
                                    <a href="javascript:;" className="reset" onClick={this.handleReset}><i className="iconfont">&#xe67e;</i>清空</a>
                                </li>
                            </ul>
                            </Form>
                       </div>
                       <div className="pull-right">
                            <div className="detail">
                                <h2>计算结果<i>（仅供参考）</i></h2>
                                <ul className="clearfix">
                                    <li className="clearfix">本息总计<i>{this.state.backData.total==undefined?'0.00':this.state.backData.total}元</i></li>
                                    <li className="clearfix">应收本金<i>{this.state.backData.principal==undefined?'0.00':this.state.backData.principal}元</i></li>
                                    <li className="clearfix">应收利息<i>{this.state.backData.interest==undefined?'0.00':this.state.backData.interest}元</i></li>
                                    {html}
                                </ul>
                            </div>
                       </div>
                   </div>
               </ModalCal>
           </div>
        );
    },
});
Calculator = createForm()(Calculator);
export default Calculator;