import Vue from 'vue';
import Cluster from './plugin/cluster/view/Cluster'
import ElementUI from 'element-ui';
import 'element-ui/lib/theme-chalk/index.css';

Vue.use(ElementUI, {size: "mini"});

Vue.config.productionTip = false
import axios from 'axios'

axios.defaults.baseURL = "/";
new Vue({
    render: h => h(Cluster),
}).$mount('#app');
