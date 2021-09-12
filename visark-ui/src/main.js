import Vue from 'vue'
import App from './App.vue'
import ElementUI from 'element-ui';
import 'element-ui/lib/theme-chalk/index.css';
import './assets/icon/iconfont.css'
Vue.use(ElementUI,{size:'mini'});
Vue.config.productionTip = false
import axios from "axios";
Vue.prototype.$http = axios;

new Vue({
  render: h => h(App),
}).$mount('#app')
