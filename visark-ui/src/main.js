import Vue from 'vue';
import App from './App.vue';
import ElementUI from 'element-ui';
import 'element-ui/lib/theme-chalk/index.css';

Vue.use(ElementUI, {size: "mini"});
import './assets/icon/iconfont.css'
Vue.config.productionTip = false
import axios from 'axios'
// import { DatePicker } from 'ant-design-vue';
// Vue.use(DatePicker);
// import 'ant-design-vue/dist/antd.css'
axios.defaults.baseURL = "/";
new Vue({
    render: h => h(App),
}).$mount('#app');
