const state = {

};

const mutations = {
    login(state, user) {
      // ES6语法，从一个对象复制所有的属性到另一个对象，返回state对象
      Object.assign(state, user);
    },
    logout(state) {
       localStorage.removeItem('user');
       Object.assign(state, {userId: undefined});
    }
};

const actions = {
    login({ commit }, user) {
        commit('login', user);
    },
    logout({ commit }) {
        commit('logout');
    }
};

export default {
    state,
    mutations,
    actions
}
