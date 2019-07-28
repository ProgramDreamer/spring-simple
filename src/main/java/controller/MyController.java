package controller;

import annotation.YLWController;
import annotation.YLWQualifier;
import annotation.YLWRequestMapping;
import annotation.YLWRequestParam;
import service.MyService;
import service.impl.MyServiceImpl;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringReader;

/**
 * Created by where
 * 2019/7/28 13:34
 */

@YLWController
@YLWRequestMapping("/ylw")
public class MyController {

    @YLWQualifier("MyServiceImpl")
    private MyService myService;

    @YLWRequestMapping("/query")
    public void query(HttpServletRequest request, HttpServletResponse response,
                      @YLWRequestParam("name") String name,
                      @YLWRequestParam("age") String age){

        PrintWriter printWriter = null;
        try {
            printWriter = response.getWriter();
            String result = myService.query(name, age);
            printWriter.write(result);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
