package com.rd.ifaes.common.generator;


import java.util.Arrays;
import java.util.List;
import java.util.Map;

import com.rd.ifaes.common.generator.config.SetupConfig;
import com.rd.ifaes.common.generator.config.TemplateMapping;
import com.rd.ifaes.common.generator.core.BuildFactory;
import com.rd.ifaes.common.generator.jdbc.AbstractDaoSupport;
import com.rd.ifaes.common.generator.util.MyUtils;
import com.rd.ifaes.common.generator.util.StringUtil;

/**
 * 代码生成工具入口
 *@author lh
 *@since 2017-03-21
 *@version 3.0
 */
public class Builder {

    /**
     * beetl factory
     */
    private static BuildFactory factory = new BuildFactory();
    /**
     * config instance
     */
    private static SetupConfig config = SetupConfig.getInstance();
        
    //待生成表集合
    private static String [] tables = {"sys_operator"};
    

    public void db2entry(){
        // iterator all template file
        TemplateMapping[] mappings = config.getMappings();
        List<String> tablesList =  (tables==null || tables.length ==0)?AbstractDaoSupport.getInstance().queryAllTables():Arrays.asList(tables);
        for (TemplateMapping m : mappings) {
            // iterator all databases tables.
            for (String tableName : tablesList) {
                String packagePath = m.buildPackage(config.getProject(), config.getPackagePath(), config.getModel());//MyUtils.getModelName(tableName, ".")
                Map<String, Object> data = factory.getParams(tableName, packagePath);                
                String className = StringUtil.className(tableName.replace(config.getIgnorePrefix(), ""));
                factory.build(MyUtils.getTemplatePath(m), data, MyUtils.getOutPutPath(m, className));
            }
        }
    }


    public static void main(String[] args) {
        BuildFactory.setLoadingDir(System.getProperty("user.dir") + "/src/main/resources/");
        Builder builder = new Builder();
        builder.db2entry();
        System.out.println("代码已生成!");
    }
    

}
