import axios from "./request";

/**
 * get application/x-www-form-urlencoded
 *
 * @param url
 * @param params
 * @param configs
 * @returns {AxiosPromise}
 */
const get = (url, params = {}, configs = {}) => {
    return axios({
        url: url,
        method: "get",
        params: params,
        ...configs,
    });
};


/**
 * get方式 application/x-www-form-urlencoded 下载文件
 * @param url
 * @param params
 * @param configs
 * @returns {AxiosPromise}
 */
const getBlob = (url, params = {}, configs = {}) => {
    return axios({
        url: url,
        method: "get",
        params: params,
        responseType: "blob",
        ...configs,
    });
};
/**
 * post方式 application/x-www-form-urlencoded
 * @param url
 * @param params
 * @param configs
 * @returns {AxiosPromise}
 */
const post = (url, params = {}, configs = {}) => {
    return axios({
        url: url,
        method: "post",
        data: params,
        ...configs,
    });
};

/**
 * post方式 application/json
 * @param url
 * @param params
 * @param configs
 * @returns {AxiosPromise}
 */
const postJson = (url, params = {}, configs = {}) => {
    return axios({
        url: url,
        method: "post",
        headers: {
            "Content-Type": "application/json",
        },
        data: params,
        ...configs,
    });
};

/***
 * post方式 application/x-www-form-urlencoded 下载文件
 * @param url
 * @param params
 * @param configs
 * @returns {AxiosPromise}
 */
const postBlob = (url, params = {}, configs = {}) => {
    return axios({
        url: url,
        method: "post",
        data: params,
        responseType: "blob",
        ...configs,
    });
};

/**
 * post方式 application/json 下载文件
 * @param url
 * @param params
 * @param configs
 * @returns {AxiosPromise}
 */
const postJsonBlob = (url, params = {}, configs = {}) => {
    return axios({
        url: url,
        method: "post",
        headers: {
            "Content-Type": "application/json",
        },
        data: params,
        responseType: "blob",
        ...configs,
    });
};

/**
 * post方式 multipart/form-data 上传文件
 * @param url
 * @param params
 * @param configs
 * @returns {AxiosPromise}
 */
const postFormData = (url, params = {}, configs = {}) => {
    return axios({
        url: url,
        method: "post",
        headers: {
            "Content-Type": "multipart/form-data",
        },
        data: params,
        ...configs,
    });
};
// ------post 方式------

/**
 * put方式 application/x-www-form-urlencoded
 * @param url
 * @param params
 * @param configs
 * @returns {AxiosPromise}
 */
const put = (url, params = {}, configs = {}) => {
    return axios({
        url: url,
        method: "put",
        data: params,
        ...configs,
    });
};

/**
 * del方式 application/x-www-form-urlencoded
 * @param url
 * @param params
 * @param configs
 * @returns {AxiosPromise}
 */
const del = (url, params = {}, configs = {}) => {
    return axios({
        url: url,
        method: "delete",
        params: params,
        data: params,
        ...configs,
    });
};
export {
    get,
    getBlob,
    post,
    postJson,
    postBlob,
    postJsonBlob,
    postFormData,
    put,
    del,
};
