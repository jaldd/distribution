import cn.hutool.core.util.ReflectUtil;
import lombok.extern.slf4j.Slf4j;
import sun.misc.ClassLoaderUtil;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;
import java.net.URI;
import java.net.URL;
import java.net.URLClassLoader;

@Slf4j
public class TestClassLoader {
    public static final String CLASS_FULL_NAME = "test.RequestHandler";

    public static final String METHOD_NAME = "process";


    public void test1() throws Exception {

        for (int i = 0; i < 1000; i++) {

            try {

                File file = new File("D:\\project\\customize.jar");
                URI uri = file.toURI();
                URL dataCustomizeJar = uri.toURL();

                URLClassLoader classLoader = new URLClassLoader(new URL[]{dataCustomizeJar}, null);
                Class<?> apiClass = classLoader.loadClass(CLASS_FULL_NAME);
                Object instance = apiClass.newInstance();
                Method method = ReflectUtil.getMethodByName(apiClass, METHOD_NAME);
                Object invoke = method.invoke(instance);
                method = null;
                apiClass = null;
                log.info("after convert:" + invoke);
                try {
                    classLoader.close();
                    classLoader = null;
                    System.gc();
                } catch (IOException e) {
                    System.out.println("关闭外部jar失败：" + e.getMessage());
                }
//                Thread.sleep(100L);
                System.out.println(i);
            } catch (Throwable e) {
                log.error(e.getMessage(), e);
                throw e;
            }
        }
    }

    public void test2() throws Exception {

        for (int i = 0; i < 1000; i++) {

            try {
                URLClassLoader classLoader = new URLClassLoader(new URL[]{});

                File file = new File("D:\\project\\customize.jar");
                URI uri = file.toURI();
                URL dataCustomizeJar = uri.toURL();


                File file1 = new File("D:\\project\\customize1.jar");
                URI uri1 = file1.toURI();
                URL dataCustomizeJar1 = uri1.toURL();


                Method method = URLClassLoader.class.getDeclaredMethod("addURL", URL.class);
                if (!method.isAccessible()) {
                    method.setAccessible(true);
                }
                if (i % 2 == 0) {

                    method.invoke(classLoader, dataCustomizeJar1);
                } else {
                    method.invoke(classLoader, dataCustomizeJar);
                }

                System.out.println(classLoader.getURLs().length);


                Class<?> apiClass = classLoader.loadClass(CLASS_FULL_NAME);
                Object instance = apiClass.newInstance();
                Method method1 = ReflectUtil.getMethodByName(apiClass, METHOD_NAME);
                Object invoke = method1.invoke(instance);
                log.info("after convert:" + invoke);
//                try {
                classLoader.close();
                ClassLoaderUtil.releaseLoader(classLoader);
//                method1.invoke(instance);
                System.gc();
//                Thread.sleep(10000);
//                method1.invoke(instance);
//                } catch (IOException e) {
//                    System.out.println("关闭外部jar失败：" + e.getMessage());
//                }
//                Thread.sleep(100L);
                System.out.println(i + "==");
            } catch (Throwable e) {
                log.error(e.getMessage(), e);
                throw e;
            }
        }
    }


    public static void main(String[] args) throws Exception {

        new TestClassLoader().test2();
    }

}
