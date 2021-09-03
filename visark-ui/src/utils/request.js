import axios from "axios";

import {Notification} from "element-ui";


// create an axios instance
const service = axios.create({
    withCredentials: true, // send cookies when cross-domain requests
    timeout: 200000, // request timeout
});

// request interceptor
service.interceptors.request.use(
    config => {
        return config;
    },
    error => {
        // do something with request error
        console.log(error); // for debug
        return Promise.reject(error);
    }
);

// response interceptor
service.interceptors.response.use(
    /**
     * If you want to get http information such as headers or status
     * Please return  response => response
     */

    /**
     * Determine the request status by custom code
     */
    response => {
        const httpStatus = response.status;
        if (httpStatus != 203) {
            const res = response.data;
            const status = ("" + (res.status ?? res.code)).toLocaleLowerCase();
            const msg = res.message ?? res.msg ?? res.data;
            if (
                status !== "0" &&
                status !== "succeed" &&
                status !== "success" &&
                status !== "ok"
            ) {
                if (status < 100) {
                    Notification({
                        message: msg ?? "error",
                        type: "error",
                        duration: 3 * 1000,
                        showClose: true,
                    });
                }
                return Promise.reject(res);
            } else {
                return res;
            }
        }
    },
    error => {
        console.log("err" + error); // for debug
        let message = "";
        if (error.response) {
            const status = error.response.status;
            if (status === 404) {
                message = "404:请求资源未找到";
            } else if (status === 500) {
                message = "500:服务器出错";
            } else if (status === 403) {
                message = "403:拒绝访问";
            }
        } else {
            message = error.message;
            if (message.indexOf("timeout") !== -1) {
                message = "请求超时请重试";
            } else {
                message = "";
            }
        }
        if (message !== "") {
            Notification({
                message: message,
                type: "error",
                // duration: 3 * 1000,
                showClose: true,
            });
        }
        return Promise.reject(error);
    }
);

export default service;
