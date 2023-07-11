package org.shaotang.distribution.example.starrocks.web;

import lombok.extern.slf4j.Slf4j;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.DefaultRedirectStrategy;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.io.BufferedReader;
import java.io.IOException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestController
public class TestController {


    //调用http://127.0.0.1:8001/register 测试
    @RequestMapping("test")
    public void test(@RequestBody String content) throws Exception {
        sendData(content);
    }

    final HttpClientBuilder httpClientBuilder = HttpClients
            .custom()
            .setRedirectStrategy(new DefaultRedirectStrategy() {
                @Override
                protected boolean isRedirectable(String method) {
                    return true;
                }
            });
    private final static String STARROCKS_HOST = "starrocks-dev";
    private final static String STARROCKS_DB = "test";
    private final static String STARROCKS_TABLE = "detailDemo";
    private final static String STARROCKS_USER = "test";
    private final static String STARROCKS_PASSWORD = "test";
    private final static int STARROCKS_HTTP_PORT = 31127;

    private void sendData(String content) throws Exception {
        final String loadUrl = String.format("http://%s:%s/api/%s/%s/_stream_load",
                STARROCKS_HOST,
                STARROCKS_HTTP_PORT,
                STARROCKS_DB,
                STARROCKS_TABLE);


        try (CloseableHttpClient client = httpClientBuilder.build()) {
            HttpPut put = new HttpPut(loadUrl);
            StringEntity entity = new StringEntity(content, "UTF-8");
            put.setHeader(HttpHeaders.EXPECT, "100-continue");
            put.setHeader(HttpHeaders.AUTHORIZATION, basicAuthHeader(STARROCKS_USER, STARROCKS_PASSWORD));
//            put.setHeader("column_separator", ",");
//            put.setHeader("columns","event_time,user_id,event_type,device_code,channel");
            // the label header is optional, not necessary
            // use label header can ensure at most once semantics
//            put.setHeader("label", "39c25a5c-7000-496e-a98e-348a264c81de3");
            put.setHeader("format", "json");

            put.setEntity(entity);

            try (CloseableHttpResponse response = client.execute(put)) {
                String loadResult = "";
                if (response.getEntity() != null) {
                    loadResult = EntityUtils.toString(response.getEntity());
                }
                final int statusCode = response.getStatusLine().getStatusCode();
                // statusCode 200 just indicates that starrocks be service is ok, not stream load
                // you should see the output content to find whether stream load is success
                if (statusCode != 200) {
                    throw new IOException(
                            String.format("Stream load failed, statusCode=%s load result=%s", statusCode, loadResult));
                }

                System.out.println(loadResult);
            }
        }
    }

    private String basicAuthHeader(String username, String password) {
        final String tobeEncode = username + ":" + password;
        byte[] encoded = Base64.encodeBase64(tobeEncode.getBytes(StandardCharsets.UTF_8));
        return "Basic " + new String(encoded);
    }

    @RequestMapping("echo")
    public Map<String, Object> echo(@RequestHeader(value = "print-method", required = false) Boolean printMethod,
                                    @RequestHeader(value = "print-headers", required = false) Boolean printHeaders,
                                    @RequestHeader(value = "print-params", required = false) Boolean printParams,
                                    @RequestHeader(value = "print-body", required = false) Boolean printBody,
                                    @RequestParam(value = "errorMessage", required = false) String errorMessage,
                                    @RequestParam(value = "sleepMillis", required = false) Long sleepMillis,
                                    HttpServletRequest request) throws InterruptedException, IOException {
        log.info("echo print {} {} {} {}", printMethod, printHeaders, printParams, printBody);
        Map<String, Object> headers = new HashMap<>();
        Enumeration<String> headersEnumeration = request.getHeaderNames();
        while (headersEnumeration.hasMoreElements()) {
            String header = headersEnumeration.nextElement();
            headers.put(header, URLDecoder.decode(request.getHeader(header), StandardCharsets.UTF_8.name()));
        }
        Map<String, Object> params = new HashMap<>();
        request.getParameterMap().forEach((k, v) ->
                params.put(k, Arrays.asList(v))
        );
        StringBuilder body = new StringBuilder();
        try (BufferedReader br = request.getReader()) {
            String line;
            while ((line = br.readLine()) != null) {
                body.append(line);
            }
        }
//        if (BooleanUtils.isTrue(printMethod)) {
        log.info("method {}", request.getMethod());
//        }
//        if (BooleanUtils.isTrue(printHeaders)) {
        log.info("headers \n {}", headers);
//        }
//        if (BooleanUtils.isTrue(printParams)) {
        log.info("params \n {}", params);
//        }
//        if (BooleanUtils.isTrue(printBody)) {
        log.info("body \n {}", body.toString());
//        }
        if (sleepMillis != null && sleepMillis > 0L) {
            Thread.sleep(sleepMillis);
        }
        Map<String, Object> map = new HashMap<>();
        map.put("method", request.getMethod());
        map.put("headers", headers);
        map.put("params", params);
        map.put("body", body);

        return map;
    }
}