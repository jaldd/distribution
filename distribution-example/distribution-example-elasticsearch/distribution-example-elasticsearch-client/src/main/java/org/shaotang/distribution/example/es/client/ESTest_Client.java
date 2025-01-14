
package org.shaotang.distribution.example.es.client;

import org.apache.http.HttpHost;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.elasticsearch.action.admin.indices.delete.DeleteIndexRequest;
import org.elasticsearch.action.support.master.AcknowledgedResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.client.indices.CreateIndexRequest;
import org.elasticsearch.client.indices.CreateIndexResponse;
import org.elasticsearch.client.indices.GetIndexRequest;
import org.elasticsearch.client.indices.GetIndexResponse;

import java.io.IOException;
public class ESTest_Client {

    public static void main(String[] args) throws IOException {

        CredentialsProvider credentialsProvider = new BasicCredentialsProvider();
        credentialsProvider.setCredentials(AuthScope.ANY,
                new UsernamePasswordCredentials("username", "password"));

        RestHighLevelClient client = new RestHighLevelClient(
                RestClient.builder(
                        new HttpHost("10.0.1.1", 29200, "http")
                ).setHttpClientConfigCallback(httpAsyncClientBuilder ->
                        httpAsyncClientBuilder.setDefaultCredentialsProvider(credentialsProvider)
                )
        );
        /**
         *该处为索引，⽂档操作，⾼级查询
         */
        //创建
        CreateIndexRequest createIndexRequest = new CreateIndexRequest("shopping");
        CreateIndexResponse createIndexResponse =
                client.indices().create(createIndexRequest, RequestOptions.DEFAULT);
        //响应状态
        boolean createAcknowledged = createIndexResponse.isAcknowledged();
        System.out.println("索引操作：" + createAcknowledged);


        //获取索引
        GetIndexRequest request = new GetIndexRequest("shopping");
        GetIndexResponse getIndexResponse =
                client.indices().get(request, RequestOptions.DEFAULT);
        //别名
        System.out.println(getIndexResponse.getAliases());
        //结构
        System.out.println(getIndexResponse.getMappings());
        //设置
        System.out.println(getIndexResponse.getSettings());

        //删除索引
        DeleteIndexRequest deleteRequest = new DeleteIndexRequest("shopping");
        AcknowledgedResponse response =
                client.indices().delete(deleteRequest, RequestOptions.DEFAULT);
        //响应状态
        boolean deleteAcknowledged = response.isAcknowledged();
        System.out.println("索引操作：" + deleteAcknowledged);
        //关闭客户端连接
        client.close();
    }
}