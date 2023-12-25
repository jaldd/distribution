package weixin.cp.model;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

import java.util.List;

/**
 * userid转换为open_userid
 * 将自建应用或代开发应用获取的userid转换为第三方应用的userid
 */
@Data
public class WxCpUseridToOpenUseridResult {


    @JSONField(name = "errcode")
    private Integer errCode;

    @JSONField(name = "errmsg")
    private String errMsg;

    @JSONField(name = "open_userid_list")
    private List<WxCpUseridToOpenUserid> openUseridList;

    @JSONField(name = "invalid_userid_list")
    private List<String> invalidUseridList;


    @Data
    public static class WxCpUseridToOpenUserid {

        @JSONField(name = "userid")
        private String userid;

        @JSONField(name = "open_userid")
        private String openUserid;

    }


}
