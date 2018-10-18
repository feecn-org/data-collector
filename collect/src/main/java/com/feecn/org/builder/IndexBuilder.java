package com.feecn.org.builder;


import org.elasticsearch.action.admin.indices.create.CreateIndexResponse;
import org.elasticsearch.action.admin.indices.exists.indices.IndicesExistsResponse;
import org.elasticsearch.action.delete.DeleteResponse;
import org.elasticsearch.action.update.UpdateResponse;

import java.util.Map;

/**
 *
 * @author pczhangyu
 * @date 2018/10/18
 */
public class IndexBuilder extends ElasticBuilder {

    public IndexBuilder() {
        super();
    }

    /**
     * 创建索引
     * @param index
     * @param source
     * @return
     */
    public static CreateIndexResponse create(String index, Map<String,?> source) {
       return client.admin().indices().prepareCreate(index).setSource(source).get();
    }

    /**
     * 更新文档
     * @param index
     * @param type
     * @param id
     * @param source
     * @return
     */
    public static UpdateResponse update(String index, String type, String id, Map<String,?> source) {
        return client.prepareUpdate(index,type,id).setDoc(source).get();
    }

    /**
     * 删除文档
     * @param index
     * @param type
     * @param id
     * @return
     */
    public DeleteResponse delete(String index, String type, String id) {
        return client.prepareDelete(index, type, id).get();
    }

    /**
     * 是否存在
     * @param param
     * @return
     */
    public static IndicesExistsResponse exist(Object... param) {
        return client.admin().indices().prepareExists((String[]) param).get();
    }

    public static void main(String[] args) {

    }
}
