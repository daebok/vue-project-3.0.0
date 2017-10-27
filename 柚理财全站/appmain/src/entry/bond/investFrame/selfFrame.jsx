import React from 'react';
import { ValidateFn } from '../../../common/validateFn';
import { Reg } from '../../../common/regularFun';
import './investFrameState.less';


export default class AssureUserFrame extends React.Component{   
  render(){   
    return (
      <div className="header-from site-col-7 site-col-last">        
              <ul className="investFrame-list">
                <li>
                  <span className="iconfont">&#xe647;</span>
                </li>
                <li>                    
                  <p className="txt1">当前是您的转让项目，无法投资。</p>                  
                </li>
                <li>
                  <a href="/bond/index.html" className="btn">看看其他项目</a>
                </li>               
              </ul>
          </div>
    )
  }
}