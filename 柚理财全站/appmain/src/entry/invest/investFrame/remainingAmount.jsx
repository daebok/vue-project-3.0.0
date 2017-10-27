import React from 'react';
var remainingAmount=React.createClass({
    getInitialState() {
        return {
            money:''
        };
    },
    componentWillMount(){
        var _this=this;
            $.ajax({
                url: '/project/remainAccount.html',
                type: 'POST',
                dataType: 'json',
                data: {"projectId":_this.props.id}
            })
            .done(function(msgdata) {
                _this.setState({money: msgdata.remainAccount});
            })
            .fail(function() {
                //error("网络异常，请重试！");
            })
    },
    getRemainAccount(){
        return this.state.money
    },
    render:function () {
        let progressVal = (1-this.state.money/this.props.account)*100 + "%";
        var Nmoney='';
        if(this.props.type=='份'){
            Nmoney=this.state.money/this.props.copyAccount
        }else{
            Nmoney=this.state.money
        }
        return(<div>
            <div className="surplus">剩余可投<s>{Nmoney}{this.props.type}</s></div>
            </div>
        )
    }
});
module.exports=remainingAmount;