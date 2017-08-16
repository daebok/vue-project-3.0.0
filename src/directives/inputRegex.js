/**
 * Created by valley on 2016/12/7.
 */
import publiclib from '../libs/common'
export const setApr = {  //输入利率的指令
  bind (el, binding) {
    el.addEventListener('keyup', () => {
      publiclib.setApr(el)
    })
  }
}
export const setMoney = {  //输入金额的指令
  bind (el, binding) {
    el.addEventListener('keyup', () => {
      publiclib.setMoney(el, binding.arg)
    })

  }
}
export const setNumber = {  //输入金额的指令
  bind (el, binding) {
    el.addEventListener('keyup', () => {
      publiclib.setNumber(el, binding.arg)
    })
  }
}
