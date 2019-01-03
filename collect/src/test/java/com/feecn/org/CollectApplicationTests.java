package com.feecn.org;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.feecn.org.builder.DefaultQuerySearchBuilder;
import com.feecn.org.builder.ElasticBuilder;
import com.feecn.org.builder.IndexBuilder;
import com.feecn.org.service.job.MetricsCollector;
import org.elasticsearch.action.delete.DeleteResponse;
import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.RangeQueryBuilder;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.aggregations.Aggregation;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.Aggregations;
import org.elasticsearch.search.collapse.CollapseBuilder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

@RunWith(SpringRunner.class)
@SpringBootTest
public class CollectApplicationTests {

	private static final Logger logger = LoggerFactory.getLogger(CollectApplicationTests.class);
	private Thread t;

	@Test
	public void defaultQuery() {
		ArrayList<QueryBuilder> mustQuery = new ArrayList<>();
		RangeQueryBuilder rangeQueryBuilder = QueryBuilders.rangeQuery("timestamp");
		rangeQueryBuilder.gte(System.currentTimeMillis()-72 * 60 * 60 * 1000);
		rangeQueryBuilder.lte(System.currentTimeMillis());
		mustQuery.add(rangeQueryBuilder);
		DefaultQuerySearchBuilder querySearchBuilder = new DefaultQuerySearchBuilder(mustQuery);
		try {
			Map<String, Object> resultMap = querySearchBuilder.mutiSearch("msisvc_test@@20181112", "doc", 1, 5000);
			JSONArray jsonArray = (JSONArray) resultMap.get("data");
			for (Object json : jsonArray) {
				JSONObject body = (JSONObject) json;
				String serviceId = body.getString("serviceid");
				String id = body.getString("id");
				ArrayList<QueryBuilder> secondMustQuery = new ArrayList<>();
				secondMustQuery.add(QueryBuilders.termQuery("serviceid.keyword",serviceId));
				DefaultQuerySearchBuilder secondQuerySearchBuilder = new DefaultQuerySearchBuilder(secondMustQuery);
				Map<String, Object> secondResultMap = secondQuerySearchBuilder.mutiSearch("msisvc_test@@20181112", "doc", 1, 5000);
				if (((Long) secondResultMap.get("totalCount"))>1){
					JSONArray mulit = ((JSONArray) secondResultMap.get("data"));
					logger.info(mulit.toJSONString());
					for (int i = 0; i < mulit.size(); i++) {
						try {
							JSONObject current= ((JSONObject) mulit.get(i));
							JSONObject next= ((JSONObject) mulit.get(i+1));
							long current_timestamp = current.getLongValue("timestamp");
							String current_id = current.getString("id");
							long next_timestamp = next.getLongValue("timestamp");
							String next_id = next.getString("id");
							if (current_timestamp>next_timestamp){
                                DeleteResponse delete = IndexBuilder.delete("msisvc_test@@20181112", "doc", current_id);
                                logger.info(delete.toString());
                            }else {
                                DeleteResponse delete = IndexBuilder.delete("msisvc_test@@20181112", "doc", next_id);
                                logger.info(delete.toString());
                            }
						} catch (Exception e) {
							logger.info(e.getMessage());
						}
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Test
	public void groupIp(){
		ArrayList<QueryBuilder> mustQuery = new ArrayList<>();
		DefaultQuerySearchBuilder querySearchBuilder = new DefaultQuerySearchBuilder(mustQuery);
		try {
			Map<String, Object> resultMap = querySearchBuilder.mutiSearchNoPage("es_index_jboss_accesslog@@20181225", "doc");
			Object data = resultMap.get("data");
			JSONArray data1 = (JSONArray) data;
			System.out.println(data1.size());
		} catch (Exception e) {
			e.printStackTrace();
		}

	}


	@Test
	public void getGroupIp(){
		SearchRequestBuilder searchRequestBuilder = ElasticBuilder.client.prepareSearch();
		try {
			List<Aggregation> aggregations = searchRequestBuilder
					.addAggregation(AggregationBuilders.terms("ipCount").field("ip").size(Integer.MAX_VALUE))
					.setIndices("es_index_jboss_accesslog@@20181225")
					.setTypes("doc")
					.execute()
					.get()
					.getAggregations()
					.asList();
			Aggregation aggregation = aggregations.get(0);
			JSONObject aggr = JSON.parseObject(aggregation.toString());
			JSONArray buckets = aggr.getJSONObject("ipCount").getJSONArray("buckets");
			logger.info(String.format("ipCount is %s",buckets.size()));
			logger.info(String.format("ipList size is %s",aggr));
			for (Object bucket : buckets) {
				JSONObject group = (JSONObject) bucket;
				logger.info(String.format("single ip is >> %s",group.getString("key")));
			}
		} catch (Exception e) {
			logger.error("group by ip search error >>>", e.getMessage());
			e.printStackTrace();
		}
	}

	@Test
	public void getIpDistinctCount(){
//		SearchRequestBuilder searchRequestBuilder = ElasticBuilder.client.prepareSearch();
//				searchRequestBuilder
//				.setQuery(QueryBuilders.termQuery("ip",""));
		try {
			SearchResponse searchResponse = ElasticBuilder.client.prepareSearch()
					.setCollapse(new CollapseBuilder("ip"))
					.setIndices("es_index_jboss_accesslog@@20181225")
					.setTypes("doc")
					.execute()
					.get();
			logger.info(searchResponse.toString());
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (ExecutionException e) {
			e.printStackTrace();
		}

	}

	@Test
	public void contextLoads() {
		MetricsCollector metricsCollector = new MetricsCollector();
		metricsCollector.run();
	}

}
