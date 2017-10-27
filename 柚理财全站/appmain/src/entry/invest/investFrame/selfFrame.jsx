import React from 'react';
import { ValidateFn } from '../../../common/validateFn';
import { Reg } from '../../../common/regularFun';
import './investFrameState.less';


export default class AssureUserFrame extends React.Component{   
  render(){
    var url=''
    if(this.props.data.tabProjectTypeId==undefined){
      url='/invest/index.html'
    }else{
      url="/invest/index.html?projectTypeId="+this.props.data.tabProjectTypeId
    }
    return (
      <div className="header-from site-col-7 site-col-last">        
              <ul className="investFrame-list">
                <li>
                  <span className="iconfont">&#xe647;</span>
                </li>
                <li>                    
                  <p className="txt1">当前是您的借款项目，无法投资。</p>                  
                </li>
                <li>
                  <a href={url} className="btn">看看其他项目</a>
                </li>               
              </ul>
          </div>
    )
  }
}