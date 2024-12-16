package com.example.apiinfo.service;

import com.example.apiinfo.model.UserInfo;
import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch.core.IndexResponse;
import co.elastic.clients.json.jackson.JacksonJsonpMapper;
import co.elastic.clients.transport.ElasticsearchTransport;
import co.elastic.clients.transport.rest_client.RestClientTransport;
import org.apache.http.HttpHost;
import org.elasticsearch.client.RestClient;
import org.springframework.stereotype.Service;

@Service
public class ElasticSearchService {

    private final ElasticsearchClient elasticsearchClient;

    public ElasticSearchService() {
        RestClient restClient = RestClient.builder(new HttpHost("localhost", 9200, "http")).build();
        ElasticsearchTransport transport = new RestClientTransport(restClient, new JacksonJsonpMapper());
        this.elasticsearchClient = new ElasticsearchClient(transport);
    }

    public void saveUserInfo(UserInfo userInfo) {
        try {
            // Index the document
            IndexResponse response = elasticsearchClient.index(i -> i
                    .index("user_info_index")
                    .id(userInfo.getUserId().toString())
                    .document(userInfo)
            );

            // Log the response
            System.out.println("Document indexed successfully!");
            System.out.println("Index: " + response.index());
            System.out.println("ID: " + response.id());
            System.out.println("Result: " + response.result());

        } catch (Exception e) {
            System.err.println("Error indexing to Elasticsearch: " + e.getMessage());
        }
    }
}
