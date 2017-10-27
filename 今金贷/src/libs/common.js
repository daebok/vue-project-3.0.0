
export default {
  setApr(that){ //利率正则
    if(that.value != '' )
    {
      if(that.value.search(/^([1-9]\d{0,1}|0)(?:\.\d{0,2})?$/)==-1)
      {
        that.value = '';
        that.value= that.value2 ? that.value2 : '';
      }
      else
      {
        that.value2 = that.value;
      }
    } else {
      that.value2 = ''
    }
  },
  setMoney(that, lens){ //金额正则
    if(that.value != '' )
    {
      if(that.value.search(/^([1-9]\d{0,7}|0)(?:\.\d{0,2})?$/)==-1)
      {
        that.value = '';
        that.value= that.value2 ? that.value2 : '';
      }
      else
      {
        that.value2 = that.value;
      }
    }else{
      that.value2 = ''
    }
  },
  setNumber(that, lens){ //金额正则
    if(that.value != '' )
    {
      if(that.value.search(/^([0-9]\d*)?$/)==-1)
      {
        that.value = '';
        that.value= that.value2 ? that.value2 : '';
      }
      else
      {
        that.value2 = that.value;
      }
    }else{
      that.value2 = ''
    }
  },
}
