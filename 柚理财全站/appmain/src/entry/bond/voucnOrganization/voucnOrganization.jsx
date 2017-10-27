import React from 'react';
import './voucnOrganization.less';

export default class VoucnOrganization extends React.Component{
    constructor(props){
        super(props);
        this._getVoucnData = this.getVoucnData.bind(this);
        this.state = {
            data:　null,
        }
    }
    //获取担保用户数据
    getVoucnData(){
        let _this = this;
        $.ajax({
            url: "/project/borrower.html",
            type: "POST",
            dataType: "json",
            data: {"projectId": $("#projectId").val()}
        }).done(function(data){           
            if(data.result){
                _this.setState({data: data});
            }
        })
    }
    componentDidMount(){
        this._getVoucnData();//获取担保用户数据
    }
    render(){
        let obj = this.state.data;
        if(!obj){return false;}        
        if(!obj.isVouch || obj.isVouch == "0"){
            return false;
        }        
        return (
            <ul className="voucnList">
                <li>
                    <span className="pic"><img src={obj.vouchLogo} style={{width:'160px',height:"60px"}}/></span>
                    <p className="txt">由<strong>{obj.vouchName}</strong>提供担保</p>
                </li>
            </ul>
        )
    }
}