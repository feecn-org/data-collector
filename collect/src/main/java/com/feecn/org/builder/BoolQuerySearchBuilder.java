package com.feecn.org.builder;

import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilder;

import java.util.List;


/**
 * @author pczhangyu
 */
public class BoolQuerySearchBuilder extends AbstractSearchBuilder {

    @Override
    protected final QueryBuilder queryBuilder() {
        BoolQueryBuilder boolQueryBuilder = new BoolQueryBuilder();
        // 所有分句都必须匹配，与 AND 相同
        List<QueryBuilder> mustQueryBuilders = mustQueryBuilder();
        if(mustQueryBuilders != null){
            for(QueryBuilder queryBuilder : mustQueryBuilders){
                boolQueryBuilder.must(queryBuilder);
            }
        }

        // 所有分句都必须不匹配，与 NOT 相同
        List<QueryBuilder> mustNotQueryBuilders = mustNotQueryBuilder();
        if(mustNotQueryBuilders != null){
            for(QueryBuilder queryBuilder : mustNotQueryBuilders){
                boolQueryBuilder.mustNot(queryBuilder);
            }
        }

        // 至少有一个分句匹配，与 OR 相同
        List<QueryBuilder> shouldQueryBuilders = shouldQueryBuilder();
        if(shouldQueryBuilders != null){
            for(QueryBuilder queryBuilder : shouldQueryBuilders){
                boolQueryBuilder.should(queryBuilder);
            }
        }
        return boolQueryBuilder;
    }

    protected List<QueryBuilder> mustQueryBuilder(){
        // 需要子类重写
        throw new NullPointerException("mustQueryBuilder为null,需要子类重写");
    }

    protected List<QueryBuilder> mustNotQueryBuilder(){
        // 需要子类重写
        throw new NullPointerException("mustNotQueryBuilder为null,需要子类重写");
    }

    protected List<QueryBuilder> shouldQueryBuilder(){
        // 需要子类重写
        throw new NullPointerException("shouldQueryBuilder为null,需要子类重写");
    }

}
