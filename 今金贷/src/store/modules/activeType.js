import * as types from '../mutation-type';

const state = {
  activeType: 'index'
};

const mutations = {
  [types.SET_AL_ACTIVE_TYPE](state, type){
    state.activeType = type;
  }
};

const actions = {
  changeActiveType({commit, dispatch, state}, type) {
    commit(types.SET_AL_ACTIVE_TYPE, type);
  }
};

const getters = {
  activeType: state => state.activeType
};

export default {
  state,
  mutations,
  actions,
  getters
}
