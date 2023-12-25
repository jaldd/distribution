package weixin.cp.model;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

import java.util.List;

/**
 * userid转换
 * 将代开发应用或第三方应用获取的密文open_userid转换为明文userid
 */
@Data
public class WxCpOpenUseridToUseridResult {

    @JSONField(name = "errcode")
    private Integer errCode;

    @JSONField(name = "errmsg")
    private String errMsg;

    @JSONField(name = "userid_list")
    private List<WxCpUseridToOpenUserid> useridList;

    @JSONField(name = "invalid_open_userid_list")
    private List<String> invalidOpenUseridList;

    /**
     * userid转换为open_userid
     * 将自建应用或代开发应用获取的userid转换为第三方应用的userid
     * 中间对象
     */
    @Data
    public static class WxCpUseridToOpenUserid {

        @JSONField(name = "userid")
        private String userid;

        @JSONField(name = "open_userid")
        private String openUserid;

    }

}