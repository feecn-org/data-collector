package com.feecn.org.builder;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.search.SearchType;
import org.elasticsearch.client.Client;
import org.elasticsearch.common.Nullable;
import org.elasticsearch.common.unit.TimeValue;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.springframework.util.StringUtils;

import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author pczhangyu
 */
public abstract class AbstractSearchBuilder extends ElasticBuilder{


    /**
     * 复合查询
     * @param indexName 参与搜索的索引
     * @param typeName 参与搜索的索引类型
     * @param currentPage 分页 当前页码
     * @param pageSize 分页 页结果数
     * @param clazz
     * @param <T>
     * @return
     * @throws Exception
     */
    public <T> Map<String, Object> mutiSearch(String indexName, String typeName, Integer currentPage, Integer pageSize, Class<T> clazz) throws Exception {
        SearchRequestBuilder searchRequestBuilder = client.prepareSearch();
        // 参与搜索的索引
        searchRequestBuilder.setIndices(indexName);
        // 参与搜索的索引类型
        searchRequestBuilder.setTypes(typeName);
        // 设置分页
        searchRequestBuilder.setFrom((currentPage - 1) * pageSize).setSize(pageSize);
        // init QueryBuilder
        QueryBuilder queryBuilder = queryBuilder();
        if(queryBuilder == null){
            throw new NullPointerException("QueryBuilder can not be null !");
        }
        searchRequestBuilder.setQuery(queryBuilder);
        SearchResponse searchResponse = searchRequestBuilder.execute().get();
        SearchHits searchHits = searchResponse.getHits();
        Map<String, Object> map = new HashMap<>();
        map.put("totalCount", searchHits.getTotalHits());
        SearchHit[] hits = searchHits.getHits();
        List<T> tlist = new ArrayList<T>();
        for (SearchHit hit : hits) {
            String json = hit.getSourceAsString();
            JSONObject jsonObject = JSONObject.parseObject(json);
            tlist.add(jsonObject.toJavaObject(jsonObject, clazz));
        }
        map.put("data", tlist);
        return map;
    }

    public Map<String, Object> mutiSearch(String indexName, String typeName, Integer currentPage, Integer pageSize) throws Exception {
        SearchRequestBuilder searchRequestBuilder = client.prepareSearch();
        // 参与搜索的索引
        searchRequestBuilder.setIndices(indexName);
        // 参与搜索的索引类型
        searchRequestBuilder.setTypes(typeName);
        // 设置分页
        searchRequestBuilder.setFrom((currentPage - 1) * pageSize).setSize(pageSize);
        // init QueryBuilder
        QueryBuilder queryBuilder = queryBuilder();
        if(queryBuilder == null){
            throw new NullPointerException("QueryBuilder can not be null !");
        }
        searchRequestBuilder.setQuery(queryBuilder);
        SearchResponse searchResponse = searchRequestBuilder.execute().get();
        SearchHits searchHits = searchResponse.getHits();
        Map<String, Object> map = new HashMap<>();
        map.put("totalCount", searchHits.getTotalHits());
        SearchHit[] hits = searchHits.getHits();
        JSONArray searchResult = new JSONArray();
        for (SearchHit hit : hits) {
            String json = hit.getSourceAsString();
            String id=hit.getId();
            JSONObject jsonObject = JSONObject.parseObject(json);
            jsonObject.put("id",id);
            searchResult.add(jsonObject);
        }
        map.put("data", searchResult);
        return map;
    }

    public Map<String,Object> getListDataByScrollId(String scrollId,String indexName, String typeName,Integer pageSize) throws UnknownHostException {
        Map<String,Object> returnData=new HashMap<>();//存放返回值
        SearchRequestBuilder searchRequestBuilder = client.prepareSearch();
        // 索引名称
        searchRequestBuilder.setIndices(indexName);
        // 索引类型
        searchRequestBuilder.setTypes(typeName);
        if (scrollId==null||"".equals(scrollId)){
            //没有scrollId
            searchRequestBuilder.setSearchType(SearchType.DEFAULT).setScroll(TimeValue.timeValueMinutes(1)).setSize(pageSize);
            // init QueryBuilder
            QueryBuilder queryBuilder = queryBuilder();
            if(queryBuilder == null){
                throw new NullPointerException("QueryBuilder can not be null !");
            }
            SearchResponse searchResponse = searchRequestBuilder.setQuery(queryBuilder).get();
            returnData.put("count",searchResponse.getHits().getTotalHits());
            returnData.put("scrollId",searchResponse.getScrollId());
            returnData.put("data",null);
        }else {
            SearchResponse searchResponse = client.prepareSearchScroll(scrollId).setScroll(TimeValue.timeValueHours(1)).get();
            SearchHits searchHits = searchResponse.getHits();
            SearchHit[] hits = searchHits.getHits();
            JSONArray searchResult = new JSONArray();
            for (SearchHit hit : hits) {
                String json = hit.getSourceAsString();
                JSONObject jsonObject = JSONObject.parseObject(json);
                searchResult.add(jsonObject);
            }
            returnData.put("data",searchResult);
            returnData.put("scrollId",searchResponse.getScrollId());
            returnData.put("count",searchResponse.getHits().getTotalHits());
        }
        return returnData;
    }

//    public Map<String,Object> addIndexByConfig(JSONObject indexConfig){
//        try {
//            String indexName = indexConfig.getString("indexName");
//            if (!StringUtils.isEmpty(indexName)){
//                boolean isExist = IndexBuilder.isExists(indexName);
//                if (isExist){
//                    IndexBuilder.create(indexName);
//                }else {
//                    IndexBuilder.delete(indexName);
//                    IndexBuilder.create(indexName);
//                }
//            }
//        } catch (UnknownHostException e) {
//            e.printStackTrace();
//        }
//        return new HashMap<>();
//    }

    public Map<String,Object> queryByDocId(String docId, @Nullable String  index, @Nullable String type) throws Exception {
        GetResponse getFields = client.prepareGet(index, type, docId).get();
        return getFields.getSource();
    }



    protected abstract QueryBuilder queryBuilder();



}
