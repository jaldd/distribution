package weixin.cp;

import com.alibaba.fastjson.JSONObject;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import weixin.cp.model.WxCpErrorMsgEnum;
import weixin.cp.model.WxCpException;

import java.util.Objects;

@Getter
@Slf4j
public abstract class WxCpService<T> {


    private String uri;
    private String domain;
    private String method;
    private int retrySleepMillis;
    private int maxRetryTimes;
    private String accessToken;


    public void setUri(String uri) {
        this.uri = uri;
    }

    public void setDomain(String domain) {
        this.domain = domain;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public void setRetrySleepMillis(int retrySleepMillis) {
        this.retrySleepMillis = retrySleepMillis;
    }

    public void setMaxRetryTimes(int maxRetryTimes) {
        this.maxRetryTimes = maxRetryTimes;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }


    protected abstract String execute(T body) throws WxCpException;

    public String execute(JSONObject body) {

        int retryTimes = 0;
        do {

            try {
                T data = convert(body);
                log.info("after convert data:" + data);
                return execute(data);
            } catch (WxCpException e) {
                if (Objects.equals(WxCpErrorMsgEnum.CODE_1.getCode(), e.getErrcode())) {
                    if (retryTimes + 1 > this.maxRetryTimes) {
                        log.warn("重试达到最大次数【{}】", this.maxRetryTimes);
                        //最后一次重试失败后，直接抛出异常，不再等待
                        return "微信服务端异常，超出重试次数";
                    }
                }

                if (Objects.equals(WxCpErrorMsgEnum.CODE_0.getCode(), e.getErrcode())) {

                    int sleepMillis = this.retrySleepMillis * (1 << retryTimes);
                    try {
                        log.info("微信系统繁忙，{} ms 后重试(第{}次)", sleepMillis, retryTimes + 1);
                        Thread.sleep(sleepMillis);
                    } catch (InterruptedException e1) {
                        Thread.currentThread().interrupt();
                    }
                } else {
                    return e.getResponseBody();
                }
            }

        } while (retryTimes++ < maxRetryTimes);

        log.warn("重试达到最大次数【{}】", this.maxRetryTimes);
        return "微信服务端异常，超出重试次数" + maxRetryTimes;
    }

    protected abstract T convert(JSONObject body) throws WxCpException;
}
