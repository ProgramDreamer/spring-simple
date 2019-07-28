package com.argumentResolver;

import com.annotation.YLWService;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;

/**
 * Created by where
 * 2019/7/28 17:31
 */

@YLWService("httpServletRequestArgResolver")
public class HttpServletRequestArgResolver implements ArgumentResolver {
    @Override
    public boolean support(Class<?> type, int index, Method method) {
        return ServletRequest.class.isAssignableFrom(type);
    }

    @Override
    public Object argumentResolver(HttpServletRequest request, HttpServletResponse response, Class<?> type, int index, Method method) {
        return request;
    }
}
