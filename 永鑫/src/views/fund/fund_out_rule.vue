<template>
  <div class="bg-fff">
    <h3>快速转出</h3>
    <p class="color-666">2小时内到账，当日没有收益；单笔最高5万元，单日累计上限25万元。</p>
    <h3 class="dis-top">普通转出</h3>
    <p class="color-666 text-center">T+1日24:00前到账，当日有收益。</p>
    <dl>
      <dt><span class="left">转出时间</span><span class="right">到账时间</span></dt>
      <dd>
        <span class="left">周一15:00(含)-周二15:00(不含)点</span>
        <span class="right">周三 23:59前</span>
      </dd>
      <dd>
        <span class="left">周二15:00(含)-周三15:00(不含)点</span>
        <span class="right">周四 23:59前</span>
      </dd>
      <dd>
        <span class="left">周三15:00(含)-周四15:00(不含)点</span>
        <span class="right">周五 23:59前</span>
      </dd>
      <dd>
        <span class="left">周四15:00(含)-周五15:00(不含)点</span>
        <span class="right">下周一23:59前</span>
      </dd>
      <dd>
        <span class="left">周五15:00(含)-周一15:00(不含)点</span>
        <span class="right">下周二 23:59前</span>
      </dd>
    </dl>
  </div>
</template>

<style lang="scss" rel="stylesheet/scss" scoped>
  @import '../../assets/scss/border_1px';
  .bg-fff {
    padding:.3rem .15rem;
  }
  h3 {
    font-size: .15rem;
    color: #333;
    margin-bottom: .2rem;
    &.dis-top {
      margin-top: .25rem;
    }
  }
  dl {
    margin: .2rem .25rem;
    line-height: .35rem;
    font-size: .13rem;
    color: #666;
    @include hairline(all, #ddd, 5px);
    span {
      display: inline-block;
      vertical-align: top;
      padding-left: .1rem;
      font-size: 0.14rem;;
    }
    dt {
      background: #f8f8f8;
      font-size: 0;;
    }
    dd {
      @include hairline(bottom, #ddd);
      font-size: 0;
      &:last-child {
        @include hairline(none);
      }
    }
  }
  .left{
    width: 60%;
  }
  .right{
    width: 40%;
  }
</style>
