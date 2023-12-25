package weixin.cp.model;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

@Data
public class WxCpDepart {

    private Long id;
    private String name;
    @JSONField(name = "name_en")
    private String enName;
    @JSONField(name = "parentid")
    private Long parentId;
    private Long order;
}