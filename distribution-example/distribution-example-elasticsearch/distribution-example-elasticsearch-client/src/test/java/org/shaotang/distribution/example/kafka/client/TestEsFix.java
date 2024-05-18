package org.shaotang.distribution.example.kafka.client;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
public class TestEsFix {


    public static void main(String[] args) throws IOException {

        Map<String, Org> orgmap = new HashMap<>();
        List<String> needdeleteids = new ArrayList<>();
        String filepath = "test.json";
        ClassLoader classloader = Thread.currentThread().getContextClassLoader();
        try (InputStream inputStream = classloader.getResourceAsStream(filepath)) {
            String result = new BufferedReader(new InputStreamReader(inputStream)).lines().collect(Collectors.joining(System.lineSeparator()));
//            System.out.println(result);
            JSONArray jsonArray = JSON.parseArray(result);
            for (Object o : jsonArray) {
                JSONObject org = (JSONObject) o;
                String id = org.getString("id");
                String orgId = ((JSONObject) (org.getJSONArray("sourceFieldValues").get(0))).getString("fieldValue");
                String integrateDataStatus = org.getString("integrateDataStatus");
                if (orgmap.containsKey(orgId)) {
                    Org oorg = orgmap.get(orgId);
                    if (Objects.nonNull(oorg)) {
                        String cstatus = oorg.getStatus();
                        String cid = oorg.getId();
                        if (new BigDecimal(cid).compareTo(new BigDecimal(id)) < 0) {
//                            continue;
                            needdeleteids.add(id);
                        } else {
                            oorg = new Org();
                            oorg.setId(id);
                            oorg.setStatus(integrateDataStatus);
                            orgmap.put(orgId, oorg);
                            needdeleteids.add(cid);
                        }
                    } else {
                        oorg = new Org();
                        oorg.setId(id);
                        oorg.setStatus(integrateDataStatus);
                        orgmap.put(orgId, oorg);
                    }

                } else {
                    Org oorg = new Org();
                    oorg.setId(id);
                    oorg.setStatus(integrateDataStatus);
                    orgmap.put(orgId, oorg);
                }
//                String status = orDefault.getOrDefault(orgId, new HashMap<>());

            }


        }

        log.info(JSON.toJSONString(needdeleteids));
        log.info(JSON.toJSONString(orgmap));

        String str = "curl --location 'http://elasticsearch-0.elasticsearch.public-service:9200/sync-mdm-integrate-task-detail-log-pro-180008/_doc/iiiidddd/_update' --header 'Content-Type: application/json' --data '{\"doc\":{\"taskId\":\"-3309115\"}}'";

        for (String needdeleteid : needdeleteids) {
            String iiiidddd = str.replaceAll("iiiidddd", needdeleteid);
            System.out.println(iiiidddd);
        }
    }

}
