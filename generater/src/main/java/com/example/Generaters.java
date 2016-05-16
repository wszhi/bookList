package com.example;

import de.greenrobot.daogenerator.DaoGenerator;
import de.greenrobot.daogenerator.Entity;
import de.greenrobot.daogenerator.Schema;

public class Generaters {
    private static final String PACKAGE_DAO = "com.booklist.db";
    private static final String PACKAGE_BEAN = "com.booklist.bean";

    public static void main(String[] args) throws Exception {
        Schema schema = new Schema(1, "com.booklist.db");
        schema.enableKeepSectionsByDefault();
        //调用方法创建Entity和DAO，在有新的实体类需要创建只需要新建一个方法进行类似的创建操作即可
        addUserInfo(schema);
        new DaoGenerator().generateAll(schema, "app/src/main/java");
    }
    public static void addUserInfo(Schema schema){
        //添加实体类
        Entity history = schema.addEntity("Book");
        history.addStringProperty("id").notNull();
        history.addStringProperty("title").notNull();
        history.addStringProperty("pubdate").notNull();
        history.addStringProperty("publisher").notNull();
        history.addStringProperty("image").notNull();
        history.addStringProperty("summary").notNull();
        //设置实体类的包名路径
        history.setJavaPackage(PACKAGE_BEAN);
        //设置DAO类的包名路径
        history.setJavaPackageDao(PACKAGE_DAO);
    }
}
