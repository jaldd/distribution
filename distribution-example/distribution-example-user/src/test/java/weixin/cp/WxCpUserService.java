package weixin.cp;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import weixin.cp.model.*;

import java.util.*;
import java.util.stream.Collectors;


public class WxCpUserService extends WxCpService<WxCpUser> {

    public void authenticate(String userId) throws WxCpException {

        String url = getDomain() + WxCpApiPathConsts.User.USER_AUTHENTICATE + userId + "&access_token=" + getAccessToken();
        CpRequestUtils.get(url);
    }

    public String create(WxCpUser user) throws WxCpException {

        String url = getDomain() + WxCpApiPathConsts.User.USER_CREATE + "?access_token=" + getAccessToken();
        try {
            return CpRequestUtils.post(url, JSON.toJSONString(user));

        } catch (WxCpException e) {
            if (Objects.equals(e.getErrcode(), WxCpErrorMsgEnum.CODE_60102.getCode())
                    || Objects.equals(e.getErrcode(), WxCpErrorMsgEnum.CODE_60104.getCode())
                    || Objects.equals(e.getErrcode(), WxCpErrorMsgEnum.CODE_60106.getCode())) {
                //变更时数据存在，会先报邮箱和手机号的错，再报userid重复，所以不能只判断userid

                return CpRequestUtils.post(getDomain() + WxCpApiPathConsts.User.USER_UPDATE + "?access_token=" + getAccessToken(),
                        JSON.toJSONString(user));
            }
            throw e;
        }
    }


    public String update(WxCpUser user) throws WxCpException {

        String url = getDomain() + WxCpApiPathConsts.User.USER_UPDATE + "?access_token=" + getAccessToken();
        try {
            return CpRequestUtils.post(url, JSON.toJSONString(user));

        } catch (WxCpException e) {
            if (Objects.equals(e.getErrcode(), WxCpErrorMsgEnum.CODE_60111.getCode())) {
                return CpRequestUtils.post(getDomain() + WxCpApiPathConsts.User.USER_CREATE + "?access_token=" + getAccessToken(), JSON.toJSONString(user));
            }
            throw e;
        }
    }

    public String delete(String... userIds) throws WxCpException {
        if (userIds.length == 1) {
            String url = getDomain() + WxCpApiPathConsts.User.USER_DELETE + userIds[0] + "&access_token=" + getAccessToken();
            return CpRequestUtils.get(url);
        }

        JSONObject jsonObject = new JSONObject();
        JSONArray jsonArray = new JSONArray();
        jsonArray.addAll(Arrays.asList(userIds));

        jsonObject.put("useridlist", jsonArray);
        return CpRequestUtils.post(getDomain() + WxCpApiPathConsts.User.USER_BATCH_DELETE + "?access_token=" + getAccessToken(),
                jsonObject.toString());

    }

    public String invite(WxCpUser user)
            throws WxCpException {
        List<String> userIds = Collections.singletonList(user.getUserId());

        List<String> partyIds = user.getDepartment().stream().map(String::valueOf).collect(Collectors.toList());
        return invite(userIds, partyIds, null);

    }

    public String invite(List<String> userIds, List<String> partyIds, List<String> tagIds)
            throws WxCpException {

        JSONObject jsonObject = new JSONObject();
        if (!CollectionUtils.isEmpty(userIds)) {
            JSONArray jsonArray = new JSONArray();
            jsonArray.addAll(userIds);
            jsonObject.put("user", jsonArray);
        }

        if (!CollectionUtils.isEmpty(partyIds)) {
            JSONArray jsonArray = new JSONArray();
            jsonArray.addAll(partyIds);
            jsonObject.put("party", jsonArray);
        }

        if (!CollectionUtils.isEmpty(tagIds)) {
            JSONArray jsonArray = new JSONArray();
            jsonArray.addAll(tagIds);
            jsonObject.put("tag", jsonArray);
        }
        return CpRequestUtils.post(getDomain() + WxCpApiPathConsts.User.BATCH_INVITE + "?access_token=" + getAccessToken(), jsonObject.toString());
    }

    public Map<String, String> userId2Openid(String userId, Integer agentId) throws WxCpException {

        String url = getDomain() + WxCpApiPathConsts.User.USER_CONVERT_TO_OPENID + "?access_token=" + getAccessToken();
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("userid", userId);
        if (agentId != null) {
            jsonObject.put("agentid", agentId);
        }
        String responseBody = CpRequestUtils.post(getDomain() + WxCpApiPathConsts.User.BATCH_INVITE + "?access_token=" + getAccessToken(), jsonObject.toString());

        JSONObject tmpJson = JSON.parseObject(responseBody);
        Map<String, String> result = new HashMap<>();
        if (tmpJson.get("openid") != null) {
            result.put("openid", tmpJson.getString("openid"));
        }

        if (tmpJson.get("appid") != null) {
            result.put("appid", tmpJson.getString("appid"));
        }

        return result;
    }

    public String openid2UserId(String openid) throws WxCpException {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("openid", openid);
        String responseBody = CpRequestUtils.post(getDomain() + WxCpApiPathConsts.User.USER_CONVERT_TO_USERID + "?access_token=" + getAccessToken(), jsonObject.toString());
        JSONObject tmpJson = JSON.parseObject(responseBody);
        return tmpJson.getString("userid");
    }

    public String getUserId(String mobile) throws WxCpException {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("mobile", mobile);
        String responseContent = CpRequestUtils.post(getDomain() + WxCpApiPathConsts.User.GET_USER_ID + "?access_token=" + getAccessToken(), jsonObject.toString());
        JSONObject tmpJson = JSON.parseObject(responseContent);
        return tmpJson.getString("userid");
    }

    public String getUserIdByEmail(String email, int emailType) throws WxCpException {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("email", email);
        jsonObject.put("email_type", emailType);
        String responseContent = CpRequestUtils.post(getDomain() + WxCpApiPathConsts.User.GET_USER_ID_BY_EMAIL + "?access_token=" + getAccessToken(), jsonObject.toString());
        JSONObject tmpJson = JSON.parseObject(responseContent);
        return tmpJson.getString("userid");
    }

    public String getJoinQrCode(int sizeType) throws WxCpException {
        String responseContent = CpRequestUtils.get(getDomain() + WxCpApiPathConsts.User.GET_JOIN_QR_CODE + sizeType + "&access_token=" + getAccessToken());
        JSONObject tmpJson = JSON.parseObject(responseContent);
        return tmpJson.getString("join_qrcode");
    }

    public WxCpUseridToOpenUseridResult useridToOpenUserid(ArrayList<String> useridList) throws WxCpException {
        JSONObject jsonObject = new JSONObject();
        JSONArray jsonArray = new JSONArray();
        jsonArray.addAll(useridList);
        jsonObject.put("userid_list", jsonArray);
        String responseContent = CpRequestUtils.post(getDomain() + WxCpApiPathConsts.User.USERID_TO_OPEN_USERID + "?access_token=" + getAccessToken(), jsonObject.toString());
        return JSON.parseObject(responseContent, WxCpUseridToOpenUseridResult.class);
    }

    public WxCpOpenUseridToUseridResult openUseridToUserid(List<String> openUseridList, String sourceAgentId) throws WxCpException {
        JSONObject jsonObject = new JSONObject();
        JSONArray jsonArray = new JSONArray();
        jsonArray.addAll(openUseridList);
        jsonObject.put("open_userid_list", jsonArray);
        jsonObject.put("source_agentid", sourceAgentId);
        String responseContent = CpRequestUtils.post(getDomain() + WxCpApiPathConsts.User.OPEN_USERID_TO_USERID + "?access_token=" + getAccessToken(), jsonObject.toString());
        return JSON.parseObject(responseContent, WxCpOpenUseridToUseridResult.class);
    }

    public WxCpDeptUserResult getUserListId(String cursor, Integer limit) throws WxCpException {
        JSONObject jsonObject = new JSONObject();
        if (cursor != null) {
            jsonObject.put("cursor", cursor);
        }
        if (limit != null) {
            jsonObject.put("limit", limit);
        }
        String responseContent = CpRequestUtils.post(getDomain() + WxCpApiPathConsts.User.USER_LIST_ID + "?access_token=" + getAccessToken(), jsonObject.toString());
        return JSON.parseObject(responseContent, WxCpDeptUserResult.class);
    }


    @Override
    protected String execute(WxCpUser user) throws WxCpException {
        if (StringUtils.equals(getUri(), WxCpApiPathConsts.User.USER_SYNC)) {
            return create(user);
        }
        if (StringUtils.equals(getUri(), WxCpApiPathConsts.User.USER_CREATE)) {
            return create(user);
        }
        if (StringUtils.equals(getUri(), WxCpApiPathConsts.User.USER_UPDATE)) {
            return update(user);
        }
        if (StringUtils.equals(getUri(), WxCpApiPathConsts.User.USER_DELETE)) {
            return delete(user.getUserId());
        }
        if (StringUtils.equals(getUri(), WxCpApiPathConsts.User.BATCH_INVITE)) {
            return invite(user);
        }
        JSONObject res = new JSONObject();
        res.put(WxConsts.ERR_MSG, "unsupported uri:" + getUri());
        return res.toJSONString();
    }

    @Override
    public WxCpUser convert(JSONObject userJson) throws WxCpException {
        WxCpUser user = new WxCpUser();
        user.setUserId(userJson.getString("userid"));
        user.setAlias(userJson.getString("alias"));
        user.setName(userJson.getString("name"));
        user.setMobile(userJson.getString("mobile"));
        user.setEmail(userJson.getString("email"));
        user.setBizMail(userJson.getString("biz_mail"));
        user.setGender(userJson.getString("gender"));
        user.setPosition(userJson.getString("position"));
        user.setEnable(userJson.getInteger("enable"));
        user.setAvatarMediaId(userJson.getString("avatar_mediaid"));
        user.setTelephone(userJson.getString("telephone"));
        user.setAddress(userJson.getString("address"));
        user.setMainDepartment(userJson.getString("main_department"));
        user.setToInvite(userJson.getBoolean("to_invite"));
        user.setExternalPosition(userJson.getString("external_position"));
        if (userJson.containsKey("department")) {
            JSONArray jsonArray = userJson.getJSONArray("department");
            List<Long> departmentIds = new ArrayList<>();
            for (Object o : jsonArray) {
                if (o instanceof JSONObject) {
                    Long departmentId = ((JSONObject) o).getLong("departmentId");
                    departmentIds.add(departmentId);
                }
            }
            user.setDepartment(departmentIds);
        }
        if (userJson.containsKey("order")) {
            JSONArray jsonArray = userJson.getJSONArray("order");
            List<Integer> orders = new ArrayList<>();
            for (Object o : jsonArray) {
                if (o instanceof JSONObject) {
                    Integer order = ((JSONObject) o).getInteger("order");
                    orders.add(order);
                }
            }
            user.setOrder(orders);
        }
        if (userJson.containsKey("direct_leader")) {
            JSONArray jsonArray = userJson.getJSONArray("direct_leader");
            List<String> directLeaders = new ArrayList<>();
            for (Object o : jsonArray) {
                if (o instanceof JSONObject) {
                    String directLeader = ((JSONObject) o).getString("userid");
                    directLeaders.add(directLeader);
                }
            }
            user.setDirectLeader(directLeaders);
        }
        return user;
    }
}
