package com.argumentResolver;

import com.annotation.YLWRequestParam;
import com.annotation.YLWService;

import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

/**
 * Created by where
 * 2019/7/28 17:31
 */

@YLWService("RequestParamArgResolver")
public class RequestParamArgResolver implements ArgumentResolver {
    @Override
    public boolean support(Class<?> type, int index, Method method) {

        Annotation[][] annotations = method.getParameterAnnotations();
        Annotation[] paramAnnos = annotations[index];

        for (Annotation an : paramAnnos){
            if (YLWRequestParam.class.isAssignableFrom(an.getClass())){
                return true;
            }
        }

        return false;
    }

    @Override
    public Object argumentResolver(HttpServletRequest request, HttpServletResponse response, Class<?> type, int index, Method method) {
        Annotation[][] annotations = method.getParameterAnnotations();
        Annotation[] paramAnnos = annotations[index];

        for (Annotation an : paramAnnos){
            if (YLWRequestParam.class.isAssignableFrom(an.getClass())){
                YLWRequestParam er = (YLWRequestParam) an;
                String vlaue = er.value();
                return request.getParameter(vlaue);
            }
        }

        return null;
    }
}
