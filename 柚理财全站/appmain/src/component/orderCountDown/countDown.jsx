import React from 'react';
//按分钟计算【顾大宝】
export default class CountDown extends React.Component{
    constructor(props){
        super(props);        
        this.timer = null;
        this.setDefault();
        this._changeTime = this.changeTime.bind(this);
        this._clearTime = this.clearTime.bind(this);
        this._setDefault = this.setDefault.bind(this);
        this.state = {
            timeHtml: null,
        };
    }
    setDefault(){
        this.minute = this.props.time -1;
        this.second = 59;
    }    
    changeTime(){
        let self = this;                
        this.timer = setTimeout(function(){
            self.second--;             
            if(self.second < 0){
                self.second = 59;
                self.minute--;
            }
            if(self.minute < 0){
                self._setDefault();//重置时间
                self._clearTime();//清除定时器
                self.props.closeOrderPayModal();//关闭支付框
            }
            self.setState({timeHtml: self.minute+"分"+self.second+"秒"});            
        }, 1000);
    }
    clearTime(){
        clearInterval(this.timer);
    }    
    render(){
        if(!this.props.state){
            this._setDefault();//重置时间
            this._clearTime();//清除定时器
        } else {
            this._changeTime();
        }
        return (
            <span>{this.state.timeHtml}</span>
        )
    }
}