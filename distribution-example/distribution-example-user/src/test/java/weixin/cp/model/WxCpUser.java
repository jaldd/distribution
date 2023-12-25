package weixin.cp.model;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

/**
 * 微信用户信息.
 */
@Data
@Accessors(chain = true)
public class WxCpUser {

    @JSONField(name = "userid")
    private String userId;
    private String name;
    /**
     * 别名；第三方仅通讯录应用可获取
     */
    private String alias;
    private List<Long> department;
    private List<Integer> order;
    private String position;
    private String mobile;
    private String gender;
    private String email;
    @JSONField(name = "biz_mail")
    private String bizMail;
    /**
     * is_leader_in_dept.
     * 个数必须和department一致，表示在所在的部门内是否为上级。1表示为上级，0表示非上级。在审批等应用里可以用来标识上级审批人
     */
    @JSONField(name = "is_leader_in_dept")
    private List<Integer> isLeaderInDept;

    @JSONField(name = "direct_leader")
    private List<String> directLeader;
    private Integer enable;

    @JSONField(name = "avatar_mediaid")
    private String avatarMediaId;
    private String telephone;

    /**
     * 地址。长度最大128个字符
     */
    private String address;
    @JSONField(name = "main_department")
    private String mainDepartment;

    @JSONField(name = "to_invite")
    private Boolean toInvite;
    @JSONField(name = "external_position")
    private String externalPosition;
}