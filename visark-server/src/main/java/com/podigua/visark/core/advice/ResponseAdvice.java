package com.podigua.visark.core.advice;

import com.alibaba.fastjson.JSONObject;
import com.podigua.visark.core.Result;
import com.podigua.visark.core.annotation.WebResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.core.MethodParameter;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

/**
 * @author podigua
 */
@ControllerAdvice
@Slf4j
public class ResponseAdvice implements ResponseBodyAdvice<Object> {
    @Override
    public boolean supports(MethodParameter methodParameter, Class<? extends HttpMessageConverter<?>> aClass) {
        if (ObjectUtils.isEmpty(methodParameter) || ObjectUtils.isEmpty(methodParameter.getMethod())) {
            return false;
        }
        WebResult webResult = AnnotationUtils.getAnnotation(methodParameter.getMethod(), WebResult.class);
        if (ObjectUtils.isEmpty(webResult)) {
            webResult = AnnotationUtils.getAnnotation(methodParameter.getContainingClass(), WebResult.class);
        }
        if (ObjectUtils.isEmpty(webResult)) {
            return false;
        }
        return webResult.value();
    }

    @Override
    public Object beforeBodyWrite(Object body, MethodParameter methodParameter, MediaType mediaType, Class<? extends HttpMessageConverter<?>> aClass, ServerHttpRequest serverHttpRequest, ServerHttpResponse serverHttpResponse) {
        Result<Object> result = Result.builder()
                .status(Result.SUCCESS)
                .message(Result.OK)
                .timestamp(System.currentTimeMillis())
                .data(body)
                .build();
        if (body instanceof String) {
            return JSONObject.toJSONString(result);
        }
        return result;
    }
}
