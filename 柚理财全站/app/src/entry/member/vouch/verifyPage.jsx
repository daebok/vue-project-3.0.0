import '../../../common/lib';
import ReactDOM from 'react-dom';
import React from 'react';
import { Button, Form, Radio, Input, Modal, Tabs} from 'antd';
const createForm = Form.create;
const FormItem = Form.Item;
const RadioGroup = Radio.Group;
const TabPane = Tabs.TabPane;
import  { ValidateFn } from '../../../common/validateFn';
import AccountVouchMenu from '../../../component/accountmenu/AccountVouchMenu';
import './verifyPage.less';
import {Publiclib} from '../../../common/common';

function error(v,url) {
  Modal.error({
    title: v,
    okText:"关闭",
    wrapClassName:'tiperror',
    onOk() {
        if(url){
          window.location.href = url;
        }
      }

  });
}

function success(v) {
  Modal.success({
    title: '提醒',
    content: v,
  });
}

function showsuccess() {
  Modal.success({
    title: '温馨提示',
    content: '您已审核成功！',
    okText:'确定',
    onOk() {
        window.location.href = "/member/vouch/verify.html";
    }
  });
}

class Detail extends React.Component{
  constructor(props){
    super(props);
    this._getData = this.getData.bind(this);
    this.state = {
      data: null,
    };
  }
  componentDidMount(){
    this._getData();
  }
  getData(){
    let self = this;
    $.ajax({
      url: '/member/vouch/getProjectInfo.html',
      type: 'POST',
      dataType: 'json',
      data: {"uuid":$("#uuid").val()}
    })
    .done(function(data) {
        self.setState({data: data});
    })
    .fail(function() {
      //error("网络异常，请重试！");
    })
  }
  render() {
    let data = this.state.data;
    if(!data){return false;}
    let createTime = Publiclib.formatDate(data.borrow.createTime, 2);
    let borrow_nature = Publiclib.borrowNature(data.borrow.borrowNature, 2);
    let repay_style = Publiclib.repayStyle(data.borrow.repayStyle, 2);
    let imgurl = data.image_server_url;
    var timeType = [];
    if(data.borrow.timeType==1){
      timeType='天'
    }else{
      timeType='个月'
    }
    return (
      <div>
        <div className="title">借款信息</div>
        <div className="detail">
            <table>
              <tbody>
                <tr>
                  <th className="titlename">项目编号</th>
                  <th>{data.borrow.projectNo}</th>
                  <th className="titlename">项目名称</th>
                  <th style={{'lineHeight':'30px'}}>{data.borrow.projectName}</th>
                </tr>
                <tr>
                  <th className="titlename">借款方</th>
                  <th>{data.borrow.realName}</th>
                  <th className="titlename">预期年化收益率</th>
                  <th>{data.borrow.apr}%</th>
                </tr>
                <tr>
                  <th className="titlename">项目金额</th>
                  <th>{data.borrow.account} 元</th>
                  <th className="titlename">项目期限</th>
                  <th>{data.borrow.timeLimit}{timeType}</th>
                </tr>
                <tr>
                  <th className="titlename">借款性质</th>
                  <th>{borrow_nature}</th>
                  <th className="titlename">借款用途</th>
                  <th>{data.borrow.borrowUse}</th>
                </tr>
                <tr>
                  <th className="titlename">最低起投金额</th>
                  <th>{data.borrow.lowestAccount} 元</th>
                  <th className="titlename">最高投资总额</th>
                  <th>{data.borrow.mostAccount}元</th>
                </tr>
                <tr>
                  <th className="titlename">还款方式</th>
                  <th>{repay_style}</th>
                  <th className="titlename">申请时间</th>
                  <th>{createTime}</th>
                </tr>
                </tbody>
            </table>
        </div>
        <div className="detail">
          <div className="content clearfix">
            <span className="span">借款详情</span>
            <div className="htmlstr" dangerouslySetInnerHTML={{__html: data.borrow.content}}></div>
          </div>
          <div className="contentImg">
            <span className="span">借款资料</span>
            <ul className="clearfix">
              {data.picList.map(function(item, k){
                let imgUrl= ""+imgurl+""+item.filePath+"";
                return <a href={imgUrl}  key={k} className="fancybox" rel="group1"><li className="clearfix"><img src={imgUrl}/></li></a>
              })}
            </ul>
          </div>
          <div className="contentImg">
            <span className="span">抵押物资料</span>
            <ul className="clearfix">
              {data.mtgList.map(function(item, k){
                let imgUrl= ""+imgurl+""+item.filePath+"";
                return <a href={imgUrl}  key={k} className="fancybox" rel="group2"><li key={k} className="clearfix"><img src={imgUrl}/></li></a>
              })}
            </ul>
          </div>
        </div>
      </div>
    );
  }
}

let FormDetail = React.createClass({
  handleSubmit(e) {
    e.preventDefault();
    this.props.form.validateFields((errors, values) => {
      if (!!errors) {
        console.log('Errors in form!!!');
        return;
      }
      console.log('Submit!!!');
      console.log(values);

       $.ajax({
         url: '/member/vouch/doVerify.html',
         type: 'POST',
         dataType: 'json',
         data: values
       })
       .done(function(data) {
         if(data.result){
            showsuccess();
         }
         else
         {
            error(data.msg,"/member/vouch/verify.html");
         } 
       })
       .fail(function() {
         error('网络异常，请重试！');
       });
    });

  },
  render:function(){
    const uuid = $("#uuid").val();
    const { getFieldProps } = this.props.form;
    const formItemLayout = {
      labelCol: { span: 7 },
      wrapperCol: { span: 12 },
    };
    const radioProps = getFieldProps('vouchStatus', {
      rules: [
        { required: true, message: '请选择审核意见' },
      ],
    });
    const textareaProps = getFieldProps('remark', {
      rules: [
        { required: true,max:100, message: '请填写备注信息,长度为100。' },
      ],
    });
    const uuidProps = getFieldProps("uuid",{
        initialValue:uuid
    });
      return (
        <div className="auditingbox">
        <Detail />
          <Form horizontal form={this.props.form} onSubmit={this.handleSubmit}>
              <div className="title">审核</div>
                <Input {...uuidProps} type="hidden"/>

              <div className="auditingcon clearfix">
                <p>审核意见<em className="star">*</em></p>
                <div className="auditingr">
                  <FormItem
                    {...formItemLayout}
                  >
                      <RadioGroup {...radioProps}>
                        <Radio value="1">同意担保</Radio>
                        <Radio value="-1">拒绝担保</Radio>
                      </RadioGroup>
                  </FormItem>
                </div>
              </div>

              <div className="auditingcon clearfix">
                <p>备注信息<em className="star">*</em></p>
                <div className="auditingr">
                  <FormItem
                    {...formItemLayout}
                  >
                      <Input {...textareaProps} type="textarea"  id="remark" name="remark" rows="8"/>
                  </FormItem>
                </div>
              </div>

               <FormItem wrapperCol={{ span: 12, offset: 7 }}>
                  <Button className="submit-btn" type="primary" htmlType="submit">提交</Button>
               </FormItem>

           </Form>
        </div>
      )
    }
});

FormDetail = createForm()(FormDetail);

ReactDOM.render(<FormDetail />,  document.getElementById("j-mybond-list"));

ReactDOM.render(<AccountVouchMenu current = {"4"}/>,  document.getElementById("j-sider-menu"));
