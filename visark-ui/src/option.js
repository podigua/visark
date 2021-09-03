import Vue from 'vue';
import Option from './plugin/option/view/Option';
import ElementUI from 'element-ui';
import 'element-ui/lib/theme-chalk/index.css';

Vue.use(ElementUI, {size: "mini"});
import './assets/icon/iconfont.css'
Vue.config.productionTip = false
import axios from 'axios'

axios.defaults.baseURL = "/";
new Vue({
    render: h => h(Option),
}).$mount('#app');
