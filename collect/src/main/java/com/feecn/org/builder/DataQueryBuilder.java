package com.feecn.org.builder;


import org.elasticsearch.index.query.*;
import org.elasticsearch.index.query.functionscore.FunctionScoreQueryBuilder;

/**
 *
 * @author pczhangyu
 * @date 2018/10/18
 */
public class DataQueryBuilder {

    public DataQueryBuilder() {

        ConstantScoreQueryBuilder constantScoreQueryBuilder = new ConstantScoreQueryBuilder(QueryBuilders.termQuery("",""));//
        BoolQueryBuilder boolQueryBuilder = new BoolQueryBuilder();
        FunctionScoreQueryBuilder functionScoreQueryBuilder = new FunctionScoreQueryBuilder(QueryBuilders.boolQuery());
        DisMaxQueryBuilder disMaxQueryBuilder = new DisMaxQueryBuilder();

    }
}
