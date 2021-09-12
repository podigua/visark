import axios from "axios";



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
    response => {
        const httpStatus = response.status;
        if (httpStatus != 203) {
            const res = response.data;
            const status = ("" + (res.status ?? res.code)).toLocaleLowerCase();
            if (
                status !== "0" &&
                status !== "succeed" &&
                status !== "success" &&
                status !== "ok"
            ) {
                return Promise.reject(res);
            } else {
                return res;
            }
        }
    },
    error => {
        return Promise.reject(error);
    }
);

export default service;
