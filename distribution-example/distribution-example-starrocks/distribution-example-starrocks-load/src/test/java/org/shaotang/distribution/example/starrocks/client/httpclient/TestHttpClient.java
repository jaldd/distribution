package org.shaotang.distribution.example.starrocks.client.httpclient;

import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpHeaders;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.DefaultRedirectStrategy;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.junit.Test;

import javax.net.ssl.*;
import java.io.IOException;
import java.security.SecureRandom;
import java.security.cert.X509Certificate;
import java.util.UUID;

@Slf4j
public class TestHttpClient {


    static {
        try {
            HttpsURLConnection.setDefaultSSLSocketFactory(createSslSocketFactory());
            HttpsURLConnection.setDefaultHostnameVerifier(new NoopHostnameVerifier());
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
    }

    public static SSLSocketFactory createSslSocketFactory() {
        TrustManager[] byPassTrustManagers = new TrustManager[]{new X509TrustManager() {
            public X509Certificate[] getAcceptedIssuers() {
                return new X509Certificate[0];
            }

            public void checkClientTrusted(X509Certificate[] chain, String authType) {
            }

            public void checkServerTrusted(X509Certificate[] chain, String authType) {
            }
        }};
        try {
            SSLContext sslContext = SSLContext.getInstance("SSL");
            sslContext.init(null, byPassTrustManagers, new SecureRandom());
            return sslContext.getSocketFactory();
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new RuntimeException(e);
        }
    }

    private CloseableHttpClient getClient() {
        HttpClientBuilder httpClientBuilder = HttpClients
                .custom()
                .setRedirectStrategy(new DefaultRedirectStrategy() {
                    @Override
                    protected boolean isRedirectable(String method) {
                        return true;
                    }
                });
        return httpClientBuilder.build();
    }

    @Test
    public void testLoad() {

        String loadUrl = "http://127.0.0.1:8001/echo?sleepMillis=200000";
        HttpPut put = new HttpPut(loadUrl);
        StringEntity entity = new StringEntity("{\"code\":999999,\"msg\":\"未知错误，需要开发人员进一步排查\",\"data\":null,\"success\":false,\"traceId\":null}", "UTF-8");
        put.setHeader(HttpHeaders.EXPECT, "100-continue");
        // the label header is optional, not necessary
        // use label header can ensure at most once semantics
        put.setHeader("label", UUID.randomUUID().toString());
        put.setHeader("format", "json");
//            put.setHeader("column_separator", ",");
//            put.setHeader("columns", StringUtils.join(fieldEnNames, ","));
        put.setEntity(entity);


        try (CloseableHttpResponse response = getClient().execute(put)) {
            String loadResult = "";
            if (response.getEntity() != null) {
                loadResult = EntityUtils.toString(response.getEntity());
            }
            log.info("loadResult:" + loadResult);
            final int statusCode = response.getStatusLine().getStatusCode();
            // statusCode 200 just indicates that starrocks be service is ok, not stream load
            // you should see the output content to find whether stream load is success
            if (statusCode == 200) {


            } else {

            }
        } catch (IOException e) {
            log.error("output failed" + e.getMessage(), e);
        } finally {
            put.releaseConnection();
        }
    }

}
