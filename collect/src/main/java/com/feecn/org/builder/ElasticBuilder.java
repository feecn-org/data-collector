package com.feecn.org.builder;

import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.client.Client;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.TransportAddress;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.transport.client.PreBuiltTransportClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;

/**
 *
 * @author pczhangyu
 * @date 2018/10/18
 */
public class ElasticBuilder {

    public static Client client;

    private static final String HOST = "192.168.128.148";

    private static final String CLUSTER_NAME = "ape-area";

    private static final int PORT = 9300;

    private static final Logger logger = LoggerFactory.getLogger(ElasticBuilder.class);

    public ElasticBuilder() {
        System.out.println("Aaaa");
    }

    static {
        if (client==null){
            Settings settings = Settings.builder().put("cluster.name",CLUSTER_NAME).build();
            try {
                client = new PreBuiltTransportClient(settings)
                        .addTransportAddress(new TransportAddress(InetAddress.getByName(HOST), PORT));
            } catch (UnknownHostException e) {
                logger.error(e.getMessage());
            }
        }
    }

    public static void main(String[] args) {
        ArrayList<QueryBuilder> objects = new ArrayList<>();
        SearchRequestBuilder searchRequestBuilder = client.prepareSearch();
        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
        objects.add(QueryBuilders.termQuery("",""));
        searchRequestBuilder.setQuery(QueryBuilders.boolQuery());
    }
}
