<template>
  <div class="page">
    <div class="content" :class="{'margin-0': useCount >= maxCount}">
      <mt-header class="bar-nav aui-border-b" title="收货地址">
        <mt-button slot="left" icon="back" v-back-link></mt-button>
      </mt-header>
      <div v-for="(item, index) in list" class="address-box">
        <div class="add-address" @click="selectAddress(index)" >
          <div class="text">
            <p class="user" >{{item.name}}<span>{{item.mobile.substring(0,3)}}****{{item.mobile.substring(7,11)}}</span></p>
            <p class="address">{{item.provinceStr}}{{item.cityStr}}{{item.areaStr}}{{item.address | cutStr(22)}}</p>
          </div>
        </div>
        <div class="aui-border-t opt">
          <label v-if="item.isDefult == 1" class="default main-color"><img src="../../assets/images/shop/checked.png" />默认地址</label>
          <label v-else @click="setDefault(item.id)" class="default"><img src="../../assets/images/shop/uncheck.png" />默认地址</label>
          <span class="pull-right">
            <label class="edit" @click="editAddress(item)"><img src="../../assets/images/shop/edit.png" />编辑</label>
            <label class="delete" @click="deleteAddress(item.id)"><img src="../../assets/images/shop/delete.png" />删除</label>
          </span>
        </div>
      </div>
    </div>
    <router-link to="/shop/address/add"><div v-if="useCount < maxCount" class="bottom">新增地址</div></router-link>
  </div>
</template>

<script>
  import * as ajaxUrl from '../../ajax.config'
  export default {
    data() {
      return {
        list: [],
        maxCount: 0,
        useCount: 0,
        getParams: {
          userId: this.$store.state.user.userId,
          __sid: this.$store.state.user.__sid
        }
      }
    },
    created() {
      this.init()
    },
    methods: {
      init () {
        this.$indicator.open({spinnerType: 'fading-circle'}) //提示初始化加载
        this.$http.get(ajaxUrl.getReceivingInfo, {params: this.getParams }).then((res) => {
          this.list = res.data.resData.list
          this.maxCount = res.data.resData.maxCount  //最多可添加地址数
          this.useCount = res.data.resData.useCount // 已添加多少个地址
          this.$indicator.close()
        })
      },
      selectAddress(index) { // 选择收货地址
        let fromName = sessionStorage.fromName
        let selectedAddress = sessionStorage.selectedAddress
        // if(fromName && fromName == 'order') {
          let addressObj = this.list[index]
          sessionStorage.selectedAddress = JSON.stringify(addressObj)
          this.$router.go(-1)
        //}

      },
      sameOpt (url, id) {
        const data = Object.assign({}, this.getParams, {id: id })
        this.$http.get(ajaxUrl[url], {params: data}).then((res) => {
          this.$toast(res.data.resMsg)
          if(res.data.resCode == 39321){
            this.init()
          }
        })
      },
      setDefault (id) {
        this.sameOpt('configDefaultReceivingInfo', id)
      },
      deleteAddress (id) {
        this.sameOpt('deleteReceivingInfo', id)
      },
      editAddress (item) {
        this.$router.push({name: 'addressAdd',query: item})
        sessionStorage.addressItem = item
      }
    }
  }
</script>

<style lang="scss" rel="stylesheet/scss" scoped>
  @import "../../assets/scss/var";
  .address-box {
    margin: 0 0 .1rem;
    .add-address {
      background: #fff;
      display: table;
      width: 100%;
      padding: .2rem .25rem .2rem .15rem;
      div {
        display: table-cell;
        vertical-align: middle;
        .user {
          font-size: .16rem;
          margin-bottom: .05rem;
          span { margin-left: .25rem; }
        }
        .address {
          color: #333;
          line-height: .2rem;
        }
      }
    }
    .opt {
      line-height: .4rem;
      padding: 0 .15rem;
      background: #fff;
      img { width: .16rem; vertical-align: middle; margin-right: .08rem; }
      .pull-right img { margin-right: .05rem; }
      .edit { margin-right: .3rem; }
    }
  }
  .bottom {
    position: absolute;
    bottom: 0;
    width: 100%;
    line-height: .5rem;
    background: $main-color;
    font-size: .17rem;
    color: #fff;
    text-align: center;
  }
  .margin-0 { margin-bottom: 0; }
</style>