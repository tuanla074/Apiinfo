package com.example.apiinfo.service;

import co.elastic.clients.elasticsearch.core.IndexRequest;
import com.example.apiinfo.model.UserInfo;
import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch.core.IndexResponse;
import co.elastic.clients.json.jackson.JacksonJsonpMapper;
import co.elastic.clients.transport.ElasticsearchTransport;
import co.elastic.clients.transport.rest_client.RestClientTransport;
import org.apache.http.HttpHost;
import org.elasticsearch.client.RestClient;
import org.springframework.stereotype.Service;
import java.io.ByteArrayInputStream;
import java.nio.charset.StandardCharsets;

@Service
public class ElasticSearchService {

    private final ElasticsearchClient elasticsearchClient;

    public ElasticSearchService() {
        RestClient restClient = RestClient.builder(new HttpHost("localhost", 9200, "http")).build();
        ElasticsearchTransport transport = new RestClientTransport(restClient, new JacksonJsonpMapper());
        this.elasticsearchClient = new ElasticsearchClient(transport);
    }

    public void saveJsonToElasticsearch(String id, String json) {
        try {
            // Convert JSON string to InputStream
            ByteArrayInputStream jsonStream = new ByteArrayInputStream(json.getBytes(StandardCharsets.UTF_8));

            // Build the IndexRequest
            IndexRequest<Object> request = new IndexRequest.Builder<>()
                    .index("user_info_index")
                    .id(id)
                    .withJson(jsonStream)
                    .build();

            // Send the request
            elasticsearchClient.index(request);
            System.out.println("Document indexed successfully with ID: " + id);

        } catch (Exception e) {
            System.err.println("Error indexing to Elasticsearch: " + e.getMessage());
        }
    }

}
