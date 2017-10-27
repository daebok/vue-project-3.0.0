import '../../../common/lib';
import ReactDOM from 'react-dom';
import React from 'react';
import  { ValidateFn } from '../../../common/validateFn';
import { Modal, Button ,Input, Form, Checkbox} from 'antd';
const createForm = Form.create;
const FormItem = Form.Item;
import './dopay.less';

function error(v) {
  Modal.error({
    title: v,
    okText:"关闭",
    wrapClassName:'tiperror'
  });
}

function success(v) {
  Modal.success({
    title: '垫付成功',
    content: v,
  });
}

export default class Dopay extends React.Component{
  constructor(props){
    super(props);  
    this.showModal = this.showModal.bind(this);
    this.handleCancel = this.handleCancel.bind(this);  
    this.goinvest = this.goinvest.bind(this); 
    this.onChange = this.onChange.bind(this);
    this.handleKeyDown = this.handleKeyDown.bind(this);       
    this.state = {
      data: null,
      visible:false,
      checked:false,
    };
  }
  showModal() {
    //console.log('showModal');
    this.setState({ visible: true });
  }  
  handleOk() {
    window.location.href = "";
  } 
  handleCancel() {
    this.setState({ visible: false });
  }
  goinvest() {
    
    if(this.refs.tipCheckbox.props.checked){
        $(".submitdom").html("<Button class='subBtn' type='primary' htmlType='submit'>立即投资</Button>");
        this.setState({ visible: false });
    }
    else
    {
      this.refs.error.className = "ant-form-explain";
    }  
    
  }
  onChange(e) { 
      if(e.target.checked==true){        
        this.setState({
          checked:true
        });
        this.refs.error.className = "ant-form-explain hide";
      }
      else{
        this.setState({
          checked:false
        });        
        this.refs.error.className = "ant-form-explain";
      }
  }
  handleKeyDown(e){
    //console.log(2222)
    if(e.keyCode=='13'){
       let msgdata = this.props.msgdata;
     if(msgdata.isTip == true){
        this.setState({ visible: true });
      }else{
        return true
      }
    }
  }
  componentDidMount(){ 
    //初始化投资金额的数值 
    window.addEventListener('keydown', this.handleKeyDown);  

  } 
  render() {
    let msgdata = this.props.msgdata;
    let btndom="";
    let opencontent="";
    let title="";
    if(msgdata.isTip == true){
      btndom = <a onClick={this.showModal}>立即投资</a>
    }
    else{
      btndom = <Button className='subBtn' type='primary' htmlType='submit'>立即投资</Button>
    }
    if(msgdata.isNeedDoRisk == true){
      btndom = <a onClick={this.showModal}>立即投资</a>;
      opencontent = <div><p className="assessmentTips">首次投资请您完成风险承受能力评估测试</p>
      <div className="clearfix"><a className="sureBtn" href="/member/risk/userRiskPapers.html">确定</a><span onClick={this.handleCancel} className="assessment">暂不评估</span></div></div>;
      title = "评估测试";
    }
    else{
      opencontent = <div className="has-error"><p>投资本项目所需风险承受能力为<font>{msgdata.userRiskLevel}</font></p><p>已超出您的风险承受能力<em>{msgdata.riskLevel}</em></p>
                <div className="riskcheck"><Checkbox ref="tipCheckbox" checked={this.state.checked} onChange={this.onChange}></Checkbox>本人已充分认识并愿意承受本项目可能存在的风险,同意继续投资</div>
                <div className="ant-form-explain hide" ref="error">请勾选确认此项提示</div>
                <div onClick={this.goinvest} className="goinvestBtn">继续投资</div></div>;
      title = "风险提示";
    }
    return (
      <div>
      <span className="tablelink">{btndom}</span>
        <Modal ref="modal"
          visible={this.state.visible}
          title={title} onCancel={this.handleCancel}
          footer={[
          ]}
        >
          <div className="modal-content riskTips">
             {opencontent}
          </div>
        </Modal>
      </div>
    );
  }
}