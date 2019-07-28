package com.servlet;

import com.annotation.YLWController;
import com.annotation.YLWQualifier;
import com.annotation.YLWRequestMapping;
import com.annotation.YLWService;
import com.controller.MyController;
import com.hand.HandToolsService;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by where
 * 2019/7/28 13:47
 */
public class DispatcherServlet extends HttpServlet {

    private List<String> classNames = new ArrayList<>();

    private Map<String, Object> beans = new HashMap<>();

    private Map<String, Object> handMap = new HashMap<>();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        this.doPost(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        //get client address
        String uri = req.getRequestURI();
        String context  = req.getContextPath();
        String path = uri.replaceAll(context, "");
        Method method = (Method) handMap.get(path);

        MyController instance = (MyController) beans.get("/"+path.split("/")[1]);

        HandToolsService hand = (HandToolsService) beans.get("MyServiceHandTool");
        Object[] args = hand.hand(req,resp,method,beans);

        try {
            method.invoke(instance, args);
        } catch (IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void init(ServletConfig config) throws ServletException {

        //1.scan class
        doScanPackage("com");
        for (String name : classNames){
            System.out.println(name);
        }

        //2.instance className
        doInstance();

        //3. DI injection
        iocDi();

        //4.url -- method
        handMapper();
        for (Map.Entry<String, Object> entry : handMap.entrySet()){
            System.out.println(entry.getKey()+":"+entry.getValue());
        }
    }

    private void handMapper() {
        if (beans.isEmpty()){
            System.out.println("类没有被实例化");
            return;
        }

        for (Map.Entry<String, Object> entry : beans.entrySet()){
            Object instance = entry.getValue();
            Class<?> clazz = instance.getClass();

            if (clazz.isAnnotationPresent(YLWController.class)){
                YLWRequestMapping requestMapping = clazz.getAnnotation(YLWRequestMapping.class);
                String classPath = requestMapping.value();

                Method[] methods = clazz.getMethods();
                for (Method method : methods){
                    if (method.isAnnotationPresent(YLWRequestMapping.class)){
                        YLWRequestMapping mapping = method.getAnnotation(YLWRequestMapping.class);
                        String methodUrl = mapping.value();
                        handMap.put(classPath+methodUrl, method);
                    }
                }
            }
        }
    }

    private void iocDi() {
        if (beans.isEmpty()){
            System.out.println("类没有被实例化");
            return;
        }

        for (Map.Entry<String, Object> entry: beans.entrySet()){
            Object instance = entry.getValue();
            Class<?> clazz = instance.getClass();
            if (clazz.isAnnotationPresent(YLWController.class)){
                Field[] fields = clazz.getDeclaredFields();
                for (Field field : fields){
                    if (field.isAnnotationPresent(YLWQualifier.class)){
                        YLWQualifier ylwQualifier = field.getAnnotation(YLWQualifier.class);
                        String vlaue = ylwQualifier.value();
                        field.setAccessible(true);
                        try {
                            field.set(instance, beans.get(vlaue));
                        } catch (IllegalAccessException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        }
    }

    private void doInstance() {
        if (classNames.size() <= 0){
            System.out.println("doScan failed ----");
            return;
        }
        for (String className : classNames){
            String cn = className.replaceAll(".class","");
            try {
                Class<?> clazz = Class.forName(cn);
                if (clazz.isAnnotationPresent(YLWController.class)){
                    YLWController ylwController = clazz.getAnnotation(YLWController.class);
                    Object instance = clazz.newInstance();

                    YLWRequestMapping ylwRequestMapping = clazz.getAnnotation(YLWRequestMapping.class);
                    String key = ylwRequestMapping.value();
                    beans.put(key, instance);
                }else if (clazz.isAnnotationPresent(YLWService.class)){
                    YLWService ylwService = clazz.getAnnotation(YLWService.class);
                    Object instance = clazz.newInstance();
                    beans.put(ylwService.value(), instance);
                }
            } catch (ClassNotFoundException | IllegalAccessException | InstantiationException e) {
                e.printStackTrace();
            }
        }

    }

    private void doScanPackage(String basePackage) {
        URL url = this.getClass().getClassLoader().getResource("/"+basePackage.replaceAll("\\.","/"));
        File file = new File(url.getFile());

        String[] files = file.list();
        for (String path : files){
            File filepath = new File(url.getFile() + path);

            if (filepath.isDirectory()){
                doScanPackage(basePackage + "." + path);
            }else {
                classNames.add(basePackage+"."+filepath.getName());
            }
        }
    }
}
