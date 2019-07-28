package com.argumentResolver;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;

/**
 * Created by where
 * 2019/7/28 17:26
 */
public interface ArgumentResolver {
    boolean support(Class<?> type, int index, Method method);

    Object argumentResolver(HttpServletRequest request,
                            HttpServletResponse response,
                            Class<?> type, int index, Method method);
}
