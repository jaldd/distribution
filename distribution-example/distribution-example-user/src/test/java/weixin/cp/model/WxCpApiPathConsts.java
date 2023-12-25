package weixin.cp.model;


/**
 * 企业微信api地址常量类
 */
public interface WxCpApiPathConsts {
    /**
     * The constant DEFAULT_CP_BASE_URL.
     */
    String DEFAULT_CP_BASE_URL = "https://qyapi.weixin.qq.com";

    /**
     * The constant GET_JSAPI_TICKET.
     */
    String GET_JSAPI_TICKET = "/cgi-bin/get_jsapi_ticket";
    /**
     * The constant GET_AGENT_CONFIG_TICKET.
     */
    String GET_AGENT_CONFIG_TICKET = "/cgi-bin/ticket/get?&type=agent_config";
    /**
     * The constant GET_CALLBACK_IP.
     */
    String GET_CALLBACK_IP = "/cgi-bin/getcallbackip";
    /**
     * The constant BATCH_REPLACE_PARTY.
     */
    String BATCH_REPLACE_PARTY = "/cgi-bin/batch/replaceparty";
    /**
     * The constant BATCH_SYNC_USER.
     */
    String BATCH_SYNC_USER = "/cgi-bin/batch/syncuser";
    /**
     * The constant BATCH_REPLACE_USER.
     */
    String BATCH_REPLACE_USER = "/cgi-bin/batch/replaceuser";
    /**
     * The constant BATCH_GET_RESULT.
     */
    String BATCH_GET_RESULT = "/cgi-bin/batch/getresult?jobid=";
    /**
     * The constant JSCODE_TO_SESSION.
     */
    String JSCODE_TO_SESSION = "/cgi-bin/miniprogram/jscode2session";
    /**
     * The constant GET_TOKEN.
     */
    String GET_TOKEN = "/cgi-bin/gettoken?corpid=%s&corpsecret=%s";
    /**
     * The constant WEBHOOK_SEND.
     */
    String WEBHOOK_SEND = "/cgi-bin/webhook/send?key=";

    /**
     * The interface O auth 2.
     */
    interface OAuth2 {
        /**
         * The constant GET_USER_INFO.
         */
        String GET_USER_INFO = "/cgi-bin/user/getuserinfo?code=%s&agentid=%d";
        /**
         * The constant GET_SCHOOL_USER_INFO.
         */
        String GET_SCHOOL_USER_INFO = "/cgi-bin/school/getuserinfo?code=%s";
        /**
         * The constant GET_USER_DETAIL.
         */
        String GET_USER_DETAIL = "/cgi-bin/user/getuserdetail";
        /**
         * The constant URL_OAUTH2_AUTHORIZE.
         */
        String URL_OAUTH2_AUTHORIZE = "https://open.weixin.qq.com/connect/oauth2/authorize";
        /**
         * The constant GET_USER_INFO without agentId.
         */
        String GET_USER_AUTH_INFO = "/cgi-bin/auth/getuserinfo?code=%s";
        /**
         * The constant GET_TFA_INFO.
         */
        String GET_TFA_INFO = "/cgi-bin/auth/get_tfa_info";
    }

    /**
     * The interface Department.
     */
    interface Department {
        /**
         * The constant DEPARTMENT_SYNC
         * 简化配置，jar包自动判断进行新增或变更
         */
        String DEPARTMENT_SYNC = "/cgi-bin/department/sync";
        /**
         * The constant DEPARTMENT_CREATE.
         */
        String DEPARTMENT_CREATE = "/cgi-bin/department/create";
        /**
         * The constant DEPARTMENT_UPDATE.
         */
        String DEPARTMENT_UPDATE = "/cgi-bin/department/update";
        /**
         * The constant DEPARTMENT_DELETE.
         */
        String DEPARTMENT_DELETE = "/cgi-bin/department/delete";
        /**
         * The constant DEPARTMENT_SIMPLE_LIST.
         */
        String DEPARTMENT_SIMPLE_LIST = "/cgi-bin/department/simplelist";
    }

    /**
     * The interface User.
     */
    interface User {
        /**
         * The constant USER_SYNC
         * 简化配置，jar包自动判断进行新增或变更
         */
        String USER_SYNC = "/cgi-bin/user/sync";
        /**
         * The constant USER_AUTHENTICATE.
         */
        String USER_AUTHENTICATE = "/cgi-bin/user/authsucc?userid=";
        /**
         * The constant USER_CREATE.
         */
        String USER_CREATE = "/cgi-bin/user/create";
        /**
         * The constant USER_UPDATE.
         */
        String USER_UPDATE = "/cgi-bin/user/update";
        /**
         * The constant USER_DELETE.
         */
        String USER_DELETE = "/cgi-bin/user/delete?userid=";
        /**
         * The constant USER_BATCH_DELETE.
         */
        String USER_BATCH_DELETE = "/cgi-bin/user/batchdelete";
        /**
         * The constant BATCH_INVITE.
         */
        String BATCH_INVITE = "/cgi-bin/batch/invite";
        /**
         * The constant USER_CONVERT_TO_OPENID.
         */
        String USER_CONVERT_TO_OPENID = "/cgi-bin/user/convert_to_openid";
        /**
         * The constant USER_CONVERT_TO_USERID.
         */
        String USER_CONVERT_TO_USERID = "/cgi-bin/user/convert_to_userid";
        /**
         * The constant GET_USER_ID.
         */
        String GET_USER_ID = "/cgi-bin/user/getuserid";
        /**
         * The constant GET_USER_ID_BY_EMAIL.
         */
        String GET_USER_ID_BY_EMAIL = "/cgi-bin/user/get_userid_by_email";
        /**
         * The constant GET_JOIN_QR_CODE.
         */
        String GET_JOIN_QR_CODE = "/cgi-bin/corp/get_join_qrcode?size_type=";
        /**
         * The constant USERID_TO_OPEN_USERID.
         */
        String USERID_TO_OPEN_USERID = "/cgi-bin/batch/userid_to_openuserid";
        /**
         * The constant OPEN_USERID_TO_USERID.
         */
        String OPEN_USERID_TO_USERID = "/cgi-bin/batch/openuserid_to_userid";

        /**
         * The constant USER_LIST_ID.
         */
        String USER_LIST_ID = "/cgi-bin/user/list_id";
    }

}
