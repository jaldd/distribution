package weixin.cp;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import weixin.cp.model.*;

import java.util.List;
import java.util.Objects;

@Slf4j
public class WxCpDepartmentService extends WxCpService<WxCpDepart> {

    public String create(WxCpDepart depart) throws WxCpException {

        //新增部门部门已存在报的错时60123 部门id无效部门不存在通讯录中,坑
        String url = getDomain() + WxCpApiPathConsts.Department.DEPARTMENT_CREATE + "?access_token=" + getAccessToken();
        try {
            return CpRequestUtils.post(url, JSON.toJSONString(depart));
        } catch (WxCpException e) {
            if (Objects.equals(e.getErrcode(), WxCpErrorMsgEnum.CODE_60123.getCode())
                    || Objects.equals(e.getErrcode(), WxCpErrorMsgEnum.CODE_60008.getCode())) {
                log.info("create exists do update");
                url = getDomain() + WxCpApiPathConsts.Department.DEPARTMENT_UPDATE + "?access_token=" + getAccessToken();
                return CpRequestUtils.post(url, JSON.toJSONString(depart));
            } else {
                throw e;
            }
        }
    }


    public String update(WxCpDepart depart) throws WxCpException {
        String url = getDomain() + WxCpApiPathConsts.Department.DEPARTMENT_UPDATE + "?access_token=" + getAccessToken();
        try {
            return CpRequestUtils.post(url, JSON.toJSONString(depart));
        } catch (WxCpException e) {
            if (Objects.equals(e.getErrcode(), WxCpErrorMsgEnum.CODE_60003.getCode())) {
                url = getDomain() + WxCpApiPathConsts.Department.DEPARTMENT_CREATE + "?access_token=" + getAccessToken();
                return CpRequestUtils.post(url, JSON.toJSONString(depart));
            } else {
                throw e;
            }
        }
    }


    public String delete(Long departId) throws WxCpException {
        String url = getDomain() + WxCpApiPathConsts.Department.DEPARTMENT_DELETE + "?access_token=" + getAccessToken() + "&id=" + departId;
        return CpRequestUtils.get(url);
    }


    public List<WxCpDepart> simpleList(Long id) throws WxCpException {
        String url = getDomain() + WxCpApiPathConsts.Department.DEPARTMENT_SIMPLE_LIST + "?access_token=" + getAccessToken();
        if (id != null) {
            url += "&id=" + id;
        }

        String responseContent = CpRequestUtils.get(url);
        JSONObject childDepartmentJson = JSON.parseObject(responseContent);

        JSONArray departmentArray = childDepartmentJson.getJSONArray("department_id");

        List<WxCpDepart> wxCpDeparts = JSON.parseArray(departmentArray.toJSONString(), WxCpDepart.class);
        return wxCpDeparts;
    }

    public String execute(WxCpDepart depart) throws WxCpException {
        if (StringUtils.equals(getUri(), WxCpApiPathConsts.Department.DEPARTMENT_SYNC)) {
            return create(depart);
        }
        if (StringUtils.equals(getUri(), WxCpApiPathConsts.Department.DEPARTMENT_CREATE)) {
            return create(depart);
        }
        if (StringUtils.equals(getUri(), WxCpApiPathConsts.Department.DEPARTMENT_UPDATE)) {
            return update(depart);
        }
        if (StringUtils.equals(getUri(), WxCpApiPathConsts.Department.DEPARTMENT_DELETE)) {
            return delete(depart.getId());
        }
        JSONObject res = new JSONObject();
        res.put(WxConsts.ERR_MSG, "unsupported uri:" + getUri());
        return res.toJSONString();
    }

    @Override
    protected WxCpDepart convert(JSONObject body) throws WxCpException {
        return JSONObject.toJavaObject(body, WxCpDepart.class);
    }
}
