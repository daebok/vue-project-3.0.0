package com.rd.ifaes.common.generator.config;

import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;

/**
 * @author lh@erongdu.com
 */
public class SetupConfig {

    private static SetupConfig instance;
    // project work dir
    public static final String USER_DIR = System.getProperty("user.dir");
    public static final String RESOURCES_PATH = "/src/main/resources/";
    public static final String SEPARATOR = File.separator;

    private String project;
    private String packagePath;
    private String author = "lh";	// default 'king'
    private String model;

    private String ignorePrefix;

    private DbConfig dbConfig;
    private String templateDir;
    private TemplateMapping[] mappings;
    private Group[] groups;


    public static String getUserDir() {
        return USER_DIR;
    }

    public static String getSeparator() {
        return SEPARATOR;
    }

    public String getProject() {
        return project;
    }

    public void setProject(String project) {
        this.project = project;
    }

    public String getPackagePath() {
        return packagePath;
    }

    public void setPackagePath(String packagePath) {
        this.packagePath = packagePath;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }
    
	public String getModel() {
		return model;
	}

	public void setModel(String model) {
		this.model = model;
	}

	public String getIgnorePrefix() {
        return ignorePrefix;
    }

    public void setIgnorePrefix(String ignorePrefix) {
        this.ignorePrefix = ignorePrefix;
    }

    public DbConfig getDbConfig() {
        return dbConfig;
    }

    public void setDbConfig(DbConfig dbConfig) {
        this.dbConfig = dbConfig;
    }

    public String getTemplateDir() {
        return templateDir;
    }

    public void setTemplateDir(String templateDir) {
        this.templateDir = templateDir;
    }

    public TemplateMapping[] getMappings() {
        return mappings;
    }

    public void setMappings(TemplateMapping[] mappings) {
        this.mappings = mappings;
    }

    public Group[] getGroups() {
        return groups;
    }

    public void setGroups(Group[] groups) {
        this.groups = groups;
    }

    private static String loadJson() {
        StringBuilder sb = new StringBuilder("");
        try {
            BufferedReader in = new BufferedReader(new FileReader(USER_DIR + RESOURCES_PATH + "config-ssm.json"));
            String str = "";
            while ((str = in.readLine()) != null) {
                int contentIndex = str.indexOf("//");		// 处理行注释
                if (contentIndex != -1) {
                    str = str.substring(0, contentIndex);
                }
                sb.append(str);
            }
            in.close();
        } catch (IOException e) {
            System.out.println("找不到配置文件:" + USER_DIR + RESOURCES_PATH +  "config-ssm.json");
        }
        return sb.toString();
    }


    public static SetupConfig getInstance() {
        if (instance == null) {
            instance = new Gson().fromJson(loadJson(), SetupConfig.class);
        }
        System.out.println(instance);
        return instance;
    }

    @Override
    public String toString() {
        return "SetupConfig{" +
                "project='" + project + '\'' +
                ", packagePath='" + packagePath + '\'' +
                ", author='" + author + '\'' +
                ", model='" + model + '\'' +
                ", ignorePrefix='" + ignorePrefix + '\'' +
                ", dbConfig=" + dbConfig +
                ", templateDir='" + templateDir + '\'' +
                ", mappings=" + Arrays.toString(mappings) +
                ", groups=" + Arrays.toString(groups) +
                '}';
    }
}
