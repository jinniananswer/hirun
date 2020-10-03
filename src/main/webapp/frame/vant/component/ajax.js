define(['vue','vant','axios'], function(Vue,vant,axios){
    let vm = new Vue({});
    let ajax = {
        get: function(url, request, successFunc, errFunc, needConfirm) {
            this.execute('get', url, request, successFunc, errFunc, needConfirm);
        },

        post: function(url, request, successFunc, errFunc, needConfirm) {
            this.execute('post', url, request, successFunc, errFunc, needConfirm);
        },

        execute: function(methodType, url, request, successFunc, errFunc, needConfirm) {

            let loading = vm.$toast.loading({
                duration: 0,
                forbidClick: true,
                mask: true,
                message: "加载中..."
            });
            let successMethod = function(info) {
                let data = info.data;
                let code = data.code;
                if (code != 0) {
                    vm.$toast({
                        message : '操作失败',
                        duration : 0,
                        overlay : true,
                        type : 'fail',
                        closeOnClickOverlay : true
                    });
                } else if (successFunc == null || typeof(successFunc) == "undefined") {
                    if (needConfirm) {
                        vm.$dialog.confirm({
                            title: '操作提示',
                            message: '点击确定按钮刷新，点击取消关闭',
                            theme: 'round-button'
                        }).then(() => {
                                // on confirm
                        }).catch(() => {
                                // on cancel
                        });
                    } else {
                        vm.$dialog.alert({
                            title: '提示',
                            message: '操作成功',
                            theme: 'round-button',
                        }).then(() => {
                            // on close
                        });
                    }

                } else {
                    successFunc(data.rows);
                }

                if (loading != null && code == '0') {
                    vm.$toast.clear();
                }
            }

            if (errFunc == null || typeof(errFunc) == "undefined") {
                errFunc = function(data) {
                    if (loading != null) {
                        vm.$toast.clear();
                    }
                    vm.$toast('对不起，我们的系统出错了，请赶紧联系管理员报告错误吧，'+data.statusText+'我们将第一时间解决您的问题');
                }

            }

            if (methodType == 'get') {

                axios.get(url,{
                    params: request
                })
                .then(successMethod)
                .catch(errFunc);
            } else if (methodType == 'post') {
                //let data = QS.stringify(request);
                axios.post(url, request).then(successMethod).catch(errFunc);
            }
        }
    }
    return ajax;
})