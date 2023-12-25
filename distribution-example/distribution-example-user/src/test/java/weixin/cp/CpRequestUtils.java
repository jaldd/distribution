package weixin.cp;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.DefaultRedirectStrategy;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.springframework.util.CollectionUtils;
import weixin.cp.model.WxConsts;
import weixin.cp.model.WxCpException;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.Objects;

@Slf4j
public class CpRequestUtils {


    public static String execute(int retrySleepMillis, int maxRetryTimes, String uri, String accessToken, String body) throws IOException, WxCpException {
        String responseBody = null;

        int retryTimes = 0;
        do {

            String uriWithAccessToken = uri + (uri.contains("?") ? "&" : "?") + "access_token=" + accessToken;
            responseBody = post(uriWithAccessToken, body);

            JSONObject jsonObject = JSON.parseObject(responseBody);
            if (CollectionUtils.isEmpty(jsonObject)) {
                if (Objects.equals(jsonObject.getInteger(WxConsts.ERR_CODE), 0)) {
                    return responseBody;
                }
                if (Objects.equals(jsonObject.getInteger(WxConsts.ERR_CODE), -1)) {

                    try {
                        log.debug("微信系统繁忙，{} ms 后重试(第{}次)", retrySleepMillis, retryTimes + 1);
                        Thread.sleep(retrySleepMillis);
                    } catch (InterruptedException e1) {
                        log.error(e1.getMessage(), e1);
                        Thread.currentThread().interrupt();
                    }
                }
            }

        } while (retryTimes++ < maxRetryTimes);
        return responseBody;
    }

    public static String get(String uri) throws WxCpException {

        log.info("uri:" + uri);
        HttpClientBuilder httpClientBuilder = HttpClients
                .custom()
                .setRedirectStrategy(new DefaultRedirectStrategy() {
                    @Override
                    protected boolean isRedirectable(String method) {
                        return true;
                    }
                });

        try (CloseableHttpClient client = httpClientBuilder.build()) {
            HttpGet get = new HttpGet(uri);
            try (CloseableHttpResponse response = client.execute(get)) {
                if (response.getEntity() != null) {
                    String responseBody = EntityUtils.toString(response.getEntity());
                    log.info("responseBody:" + responseBody);
                    try {
                        JSONObject jsonObject = JSON.parseObject(responseBody);
                        if (!jsonObject.containsKey(WxConsts.ERR_CODE)) {
                            throw new WxCpException(responseBody);
                        }
                        Integer errCode = jsonObject.getInteger(WxConsts.ERR_CODE);
                        if (!Objects.equals(errCode, 0)) {
                            throw new WxCpException(jsonObject.getString(WxConsts.ERR_MSG), errCode, responseBody);
                        }
                        return responseBody;
                    } catch (WxCpException e) {
                        throw e;
                    } catch (Exception e) {
                        log.error(e.getMessage(), e);
                        throw new WxCpException(responseBody);
                    }
                }
                throw new IllegalArgumentException("null response because illegal argument:" + uri);
            }
        } catch (IOException e) {
            throw new IllegalArgumentException(uri + " io exception:" + e.getMessage());
        }
    }

    public static String post(String uri, String body) throws WxCpException {

        log.info("uri:" + uri + ";body:" + body);
        HttpClientBuilder httpClientBuilder = HttpClients
                .custom()
                .setRedirectStrategy(new DefaultRedirectStrategy() {
                    @Override
                    protected boolean isRedirectable(String method) {
                        return true;
                    }
                });

        try (CloseableHttpClient client = httpClientBuilder.build()) {
            HttpPost post = new HttpPost(uri);
            post.setEntity(new StringEntity(body, Charset.defaultCharset()));
            try (CloseableHttpResponse response = client.execute(post)) {
                if (response.getEntity() != null) {
                    String responseBody = EntityUtils.toString(response.getEntity());
                    log.info("responseBody:" + responseBody);
                    try {
                        JSONObject jsonObject = JSON.parseObject(responseBody);
                        if (!jsonObject.containsKey(WxConsts.ERR_CODE)) {
                            throw new WxCpException(responseBody);
                        }
                        Integer errCode = jsonObject.getInteger(WxConsts.ERR_CODE);
                        if (!Objects.equals(errCode, 0)) {
                            throw new WxCpException(jsonObject.getString(WxConsts.ERR_MSG), errCode, responseBody);
                        }
                        return responseBody;
                    } catch (WxCpException e) {
                        log.error(e.getMessage(), e);
                        throw e;
                    } catch (Exception e) {
                        log.error(e.getMessage(), e);
                        throw new WxCpException(responseBody);
                    }
                }
                throw new IllegalArgumentException("null response because illegal argument:" + uri + ":" + body);
            }
        } catch (IOException e) {
            throw new IllegalArgumentException(uri + " io exception:" + e.getMessage());
        }
    }

    public String handleResponse(String responseContent) {

        if (responseContent.startsWith("<xml>")) {
            //xml格式输出直接返回
            return responseContent;
        }

//        WxError error = WxError.fromJson(responseContent, wxType);
//        if (error.getErrorCode() != 0) {
//            throw new WxErrorException(error);
//        }
        return responseContent;
    }

}
