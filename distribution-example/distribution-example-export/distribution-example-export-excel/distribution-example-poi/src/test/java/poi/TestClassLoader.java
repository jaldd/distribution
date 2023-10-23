package poi;

import cn.hutool.core.util.ReflectUtil;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;
import java.net.URI;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.HashMap;
import java.util.Map;

@Slf4j
public class TestClassLoader {
    public static final String CLASS_FULL_NAME = "RequestHandler";

    public static final String METHOD_NAME = "process";

    public static void main(String[] args) throws Exception {


        for (int i = 0; i < 1000; i++) {

            try {
                Map<String, String> params = new HashMap<>();
                Map<String, String> systemParam = new HashMap<>();
                log.info("before convert:" + params);
                log.info("systemParam:" + systemParam);


                File file = new File("D:\\project\\customize.jar");
                URI uri = file.toURI();
                URL dataCustomizeJar = uri.toURL();

                URLClassLoader classLoader = new URLClassLoader(new URL[]{dataCustomizeJar}, Thread.currentThread().getContextClassLoader());
                Class<?> apiClass = classLoader.loadClass(CLASS_FULL_NAME);
                Object instance = apiClass.newInstance();
                Method method = ReflectUtil.getMethodByName(apiClass, METHOD_NAME);
                Object invoke = method.invoke(instance, systemParam, params);

                log.info("after convert:" + invoke);
                try {
                    classLoader.close();
                } catch (IOException e) {
                    System.out.println("关闭外部jar失败："+e.getMessage());
                }
                Thread.sleep(100L);
                System.out.println(i);
            } catch (Throwable e) {
                log.error(e.getMessage(), e);
                throw e;
            }
        }
    }

}
