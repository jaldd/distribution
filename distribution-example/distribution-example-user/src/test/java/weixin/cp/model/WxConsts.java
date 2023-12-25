package weixin.cp.model;

import java.util.Arrays;
import java.util.List;

public class WxConsts {

    /**
     * access_token 相关错误代码
     * <pre>
     * 发生以下情况时尝试刷新access_token
     * 40001 获取access_token时AppSecret错误，或者access_token无效
     * 42001 access_token超时
     * 40014 不合法的access_token，请开发者认真比对access_token的有效性（如是否过期），或查看是否正在为恰当的公众号调用接口
     * </pre>
     */
    public static final List<Integer> ACCESS_TOKEN_ERROR_CODES = Arrays.asList(WxCpErrorMsgEnum.CODE_40001.getCode(),
            WxCpErrorMsgEnum.CODE_40014.getCode(), WxCpErrorMsgEnum.CODE_42001.getCode());


    /**
     * 微信接口返回的参数errcode.
     */
    public static final String ERR_CODE = "errcode";

    /**
     * 微信接口返回的参数errmsg.
     */
    public static final String ERR_MSG = "errmsg";


    public static final String POST = "post";
    public static final String GET = "get";
}