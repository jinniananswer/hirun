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
        'bottom': '/frame/vant/component/bottom',
        'moment': '/frame/vant/date/moment.min',
        'util': '/frame/vant/component/util',
        'vant-upload-img': '/frame/vant/component/upload.img',


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
    Vue.use(vant.Dialog);
    Vue.use(vant.ImagePreview);
    axios.defaults.baseURL = 'http://bss.hi-run.net/';
    axios.interceptors.request.use(
        config => {
            const token = sessionStorage.getItem('hirun-helper-jwt')
            if (token ) { // 判断是否存在 token，如果存在的话，则每个 http header 都加上 token
                config.headers.authorization = token  // 请求头加上token
            }
            return config
        },
        err => {
            return Promise.reject(err)
        }
    )
});














