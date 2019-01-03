package com.feecn.org.core;

import com.alibaba.fastjson.JSON;

import java.io.*;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by pczhangyu on 2018/12/26.
 */
public class ReaderFileLine {

    /**
     * @author:
     * @date:2018/8/30
     * @description:从txt文件读取List<String>
     */
    public static Set<String> getFileContent(String path) {
//        List<String> strList = new ArrayList<String>();
        Set<String> strList = new HashSet<>();
        File file = new File(path);
        InputStreamReader read = null;
        BufferedReader reader = null;
        try {
            read = new InputStreamReader(new FileInputStream(file),"utf-8");
            reader = new BufferedReader(read);
            String line;
            while ((line = reader.readLine()) != null) {
                strList.add(line);
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (read != null) {
                try {
                    read.close();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }

        }
        return strList;
    }

    public static void main(String[] args) {
        Set<String> fileContent = ReaderFileLine.getFileContent("C:\\Users\\pczhangyu\\Desktop\\workfile\\jboss_access_result");
        Set<String> fileContent1 = ReaderFileLine.getFileContent("C:\\Users\\pczhangyu\\Downloads\\jboss_list");
        boolean b = fileContent1.removeAll(fileContent);
        System.out.println(b);
        System.out.println(JSON.toJSONString(fileContent1));
    }

}
