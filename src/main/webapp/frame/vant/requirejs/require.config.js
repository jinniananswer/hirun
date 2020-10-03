require.config({
    map: { //map告诉RequireJS在任何模块之前，都先载入这个css模块
        '*': {
            css: '/frame/vant/requirejs/css.min.js'
        }
    },
    paths: {
        'vue': '/frame/vant/vue.min',
        'vant': '/frame/vant/vant.min',
        'axios': '/frame/vant/axios/axios.min',
        'qs': '/frame/vant/axios/qs.min',
        'ajax': '/frame/vant/component/ajax',
        'vant-select': '/frame/vant/component/select',
        'redirect': '/frame/vant/component/redirect',
        'page-title': '/frame/vant/component/page.title',
        'bottom': '/frame/vant/component/bottom'
    },
    shim: {
        'vant': {
            deps: ['vue', 'css!/frame/vant/index.css']
        }
    }
});


require(['vue', 'vant', 'vant-select', 'axios'], function(Vue, vant, vantSelect, axios) {
    vant.install(Vue);
    Vue.use(vant.Lazyload);
    axios.defaults.baseURL = 'http://localhost:8082/';
});











