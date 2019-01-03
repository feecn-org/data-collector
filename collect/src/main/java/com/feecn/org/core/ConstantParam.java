package com.feecn.org.core;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Created by pczhangyu on 2018/9/28.
 */
public class ConstantParam {

    public static final String JOB_CLASS_NAME="com.feecn.org.service.job.impl.SparkJob";

    private final static Logger logger = LoggerFactory.getLogger(ConstantParam.class);

    public static Properties loadProp(String fileName){
        InputStream is = null;
        Properties constant = null;
        try {
            constant = new Properties();
            is = ConstantParam.class.getClassLoader().getResourceAsStream(fileName);
            if (is != null) {
                constant.load(is);
            }
        } catch (Exception e) {
            logger.error(" load error", e);
        } finally {
            if (is != null) {
                try {
                    is.close();
                } catch (IOException e) {
                    logger.error("关闭文件流出错", e);
                }
            }
        }
        return constant;
    }

}
