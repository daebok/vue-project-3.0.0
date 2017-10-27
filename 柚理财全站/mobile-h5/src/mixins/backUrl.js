export default {
  methods: {
    backUrl(url) {
      if (this.$route.query.from) {
        this.$router.push(url);
      } else {
        this.$router.go(-1);
      }
    }
  }
}
