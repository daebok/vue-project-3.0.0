import ReactDOM from 'react-dom';
import React from 'react';
import  { ValidateFn } from '../../../common/validateFn';
import  { Reg } from '../../../common/regularFun';
import { Form, Select, Input, Button } from 'antd';
const createForm = Form.create;
const FormItem = Form.Item;
const Option = Select.Option;

let uuid = 0;
let Shareholder = React.createClass({
  remove(k) {
    const { form } = this.props;
    // can use data-binding to get
    let keys = form.getFieldValue('keys');
    keys = keys.filter((key) => {
      return key !== k;
    });
    // can use data-binding to set
    form.setFieldsValue({
      keys,
    });
  },
  add() {
    uuid++;
    const { form } = this.props;
    // can use data-binding to get
    let keys = form.getFieldValue('keys');    
    keys = keys.concat(uuid);
    // can use data-binding to set
    // important! notify form to detect changes
    form.setFieldsValue({
      keys,
    });
  },
  render() {
    const { getFieldProps, getFieldValue } = this.props.form;    
    getFieldProps('keys', {
      initialValue: [0],
    });
    const shareholderInfoProps = getFieldProps('shareholderInfo', {
      validate: [{
        rules:[
          { required: true, message: '请输入股东信息' },         
        ],    
      }]
    });       
    const formItems = getFieldValue('keys').map((k) => {      
      return (
        <ul>
          <li>           
            <FormItem key={k}>
               <span className="span" style={{fontSize: 14}}>{`${k}.`}</span>
              <Select defaultValue="1" style={{width: 100, marginLeft: 10}}>
                <Option value="1">自然人</Option>
                <Option value="2">企业法人</Option>                
              </Select>
              <Input {...shareholderInfoProps} style={{ width: 170, marginLeft: 20 }} maxLength="10" placeholder="不超过10个字符"/>              
            </FormItem>
          </li>
        </ul>        
      );
    });
    return (
      <div className="form-item">
        {formItems}                    
        <FormItem>
          <Button onClick={this.add} style={{ marginLeft: 22 }}>添加信息</Button>          
        </FormItem>                  
      </div>
    );
  },
});

export default Shareholder = createForm()(Shareholder);
