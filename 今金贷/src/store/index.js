import Vue from 'vue';
import Vuex from 'vuex';
import * as getters from './getters';
import * as actions from './actions';
import user from './modules/user';
import redpack from './modules/invest/redpack';
import activeType from './modules/activeType';

Vue.use(Vuex);

const debug = process.env.NODE_ENV !== 'production';

export default new Vuex.Store({
  strict: debug,
  getters,
  actions,
  modules: {
    user,
    activeType,
    redpack
  }
})
