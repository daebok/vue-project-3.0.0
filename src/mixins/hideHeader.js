export default {
  data() {
    return {
      hideHeader: false
    }
  },
  created() {
    this.showOrHideHeader()
  },
  methods: {
    showOrHideHeader() {
      if(this.$route.path.indexOf('appH5') != -1) {
        this.hideHeader = true
      }else{
        this.hideHeader = false
      }
    }
  }
}
