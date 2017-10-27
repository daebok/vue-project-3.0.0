import * as types from '../../mutation-type';

const state = {
  selectedList: [],
  selected_total: '0'
};

const getters = {
  selectedList: state => state.selectedList,
  selected_total: state => state.selected_total
};

const mutations = {
  [types.SELECT_REDPACKET](state, arr){
    state.selectedList = arr;
  },
  [types.SELECT_REDPACKET_TOTAL](state, total){
    state.selected_total = total;
  }
};

const actions = {
  selectRedpacket({commit}, arr) {
    commit(types.SELECT_REDPACKET, arr);
  },
  selectRedpacketTotal({ commit }, total) {
    commit(types.SELECT_REDPACKET_TOTAL, total);
  }
};

export default {
  state,
  getters,
  mutations,
  actions
}
