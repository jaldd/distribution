package weixin.cp.model;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * 获取成员ID列表返回参数
 *
 */
@Data
public class WxCpDeptUserResult {

    @JSONField(name = "next_cursor")
    private String nextCursor;

    @JSONField(name = "dept_user")
    private List<DeptUserList> deptUser;

    /**
     * The type Dept user list.
     */
    @Getter
    @Setter
    public static class DeptUserList {

        @JSONField(name = "userid")
        private String userId;

        @JSONField(name = "department")
        private Long department;

    }

}