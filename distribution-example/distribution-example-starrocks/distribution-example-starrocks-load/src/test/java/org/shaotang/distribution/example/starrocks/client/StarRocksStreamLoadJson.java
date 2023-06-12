package org.shaotang.distribution.example.starrocks.client;

import org.apache.commons.codec.binary.Base64;
import org.apache.http.HttpHeaders;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.DefaultRedirectStrategy;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class StarRocksStreamLoadJson {
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

        final HttpClientBuilder httpClientBuilder = HttpClients
                .custom()
                .setRedirectStrategy(new DefaultRedirectStrategy() {
                    @Override
                    protected boolean isRedirectable(String method) {
                        return true;
                    }
                });

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
            put.setHeader("format","json");

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

    public static void main(String[] args) throws Exception {
        int id1 = 1;
        int id2 = 10;
        String id3 = "Simon";
        int rowNumber = 2;
        String oneRow = "2022-05-11,1,12312,1236232,1211\n";

        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < rowNumber; i++) {
            stringBuilder.append(oneRow);
        }

        String loadData = "{\"recruit_date\":\"2022-03-12\",\"region_num\":2,\"num_plate\":1212,\"tel\":1231231231,\"id\":123412341234,\"password\":\"123452342342343324\",\"name\":\"hello\",\"profile\":\"welcome\",\"hobby\":\"starrocks\",\"leave_time\":\"2022-03-1512:21:32\",\"channel\":123.04,\"income\":21.12345,\"account\":123456.1235,\"ispass\":0}";
        System.out.println(loadData);
        StarRocksStreamLoadJson starrocksStreamLoad = new StarRocksStreamLoadJson();
        starrocksStreamLoad.sendData(loadData+loadData);
    }
}