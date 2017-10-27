import React from 'react';
export default class Safeinfotext extends React.Component{

  render() {
    let strtext1='',strtext2='',strtext3='';    
    if(!!this.props.str1){
      strtext1 = this.props.str1;
    }
    if(!!this.props.str2){
      strtext2 = <span className="text">{this.props.str2}</span>;
    }
    if(!!this.props.str3){
      strtext3 = this.props.str3;     
    }    
    return (
      <span>{strtext1}{strtext2}{strtext3}</span>
    );
  }
}
