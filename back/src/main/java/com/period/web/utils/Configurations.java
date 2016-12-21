package com.period.web.utils;

import org.apache.commons.configuration.Configuration;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * Created by aaron on 2016/4/12.
 */
public class Configurations {

    static {
        Configurations.initialize("back.properties");
    }
    private final static Logger log = LoggerFactory.getLogger(Configuration.class);

    private final static ConcurrentMap<String, Configuration> cacheList = new ConcurrentHashMap<String, Configuration>(17);
    private static String DEFAULT_CONFIG;

    private Configurations() {
    }

    public static void initialize(String defaultConfig) {
        DEFAULT_CONFIG = defaultConfig;
    }

    private static Configuration findConfiguration(String name) throws Exception {
        if (!cacheList.containsKey(name)) {
            try {
                Configuration conf = new PropertiesConfiguration(name);
                Configuration result = cacheList.putIfAbsent(name, conf);
                return result == null ? conf : result;
            } catch (ConfigurationException ex) {
                throw new Exception(ex.getMessage(), ex);
            }
        }
        return cacheList.get(name);
    }

    public static boolean getBoolean(String name, String key) throws Exception {
        Configuration conf = findConfiguration(name);
        return conf.getBoolean(key);
    }

    public static boolean getBoolean(String name, String key, boolean defaultValue)
            throws Exception {
        Configuration conf = findConfiguration(name);
        return conf.getBoolean(key, defaultValue);
    }

    public static int getInt(String name, String key) throws Exception {
        Configuration conf = findConfiguration(name);
        return conf.getInt(key);
    }

    public static int getInt(String name, String key, int defaultValue) throws Exception {
        Configuration conf = findConfiguration(name);
        return conf.getInt(key, defaultValue);
    }

    public static int[] getIntArray(String name, String key) throws Exception {
        String temp = getString(name, key);
        if (!StringUtils.isEmpty(temp)) {
            return null;
        }
        String[] array = getStringArray(name, key);
        int[] result = null;
        if (array != null) {
            result = new int[array.length];
            for (int i = 0; i < array.length; i++) {
                result[i] = Integer.parseInt(array[i]);
            }
        }
        return result;
    }

    public static long getLong(String name, String key) throws Exception {
        Configuration conf = findConfiguration(name);
        return conf.getLong(key);
    }

    public static long getLong(String name, String key, long defaultValue) throws Exception {
        Configuration conf = findConfiguration(name);
        return conf.getLong(key, defaultValue);
    }

    public static double getDouble(String name, String key) throws Exception {
        Configuration conf = findConfiguration(name);
        return conf.getDouble(key);
    }

    public static double getDouble(String name, String key, double defaultValue) throws Exception {
        Configuration conf = findConfiguration(name);
        return conf.getDouble(key, defaultValue);
    }

    public static String getString(String name, String key){
        Configuration conf = null;
        try {
            conf = findConfiguration(name);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return "";
        }
        return conf.getString(key);
    }

    public static String getString(String name, String key, String defaultValue) throws Exception {
        Configuration conf = findConfiguration(name);
        return conf.getString(key, defaultValue);
    }

    public static String[] getStringArray(String name, String key) throws Exception {
        Configuration conf = findConfiguration(name);
        return conf.getStringArray(key);
    }

    public static List<?> getList(String name, String key) throws Exception {
        Configuration conf = findConfiguration(name);
        return conf.getList(key);
    }

    @SuppressWarnings("unchecked")
    public static List<?> getList(String name, String key, List<?> defaultValue) throws Exception {
        Configuration conf = findConfiguration(name);
        return conf.getList(key, (List<Object>) defaultValue);
    }

    public static boolean getBoolean(String key) {
        try {
            return getBoolean(DEFAULT_CONFIG, key);
        } catch (Exception ex) {
            log.error(ex.getMessage(), ex);
            return false;
        }
    }

    public static int getInt(String key) {
        try {
            return getInt(DEFAULT_CONFIG, key);
        } catch (Exception ex) {
            log.error(ex.getMessage(), ex);
            return 0;
        }
    }

    public static int[] getIntArray(String key) {
        try {
            return getIntArray(DEFAULT_CONFIG, key);
        } catch (Exception ex) {
            log.error(ex.getMessage(), ex);
            return null;
        }
    }

    public static long getLong(String key) {
        try {
            return getLong(DEFAULT_CONFIG, key);
        } catch (Exception ex) {
            log.error(ex.getMessage(), ex);
            return 0L;
        }
    }

    public static double getDouble(String key) {
        try {
            return getDouble(DEFAULT_CONFIG, key);
        } catch (Exception ex) {
            log.error(ex.getMessage(), ex);
            return 0D;
        }
    }

    public static String getString(String key) {
        return getString(DEFAULT_CONFIG, key);
    }

    public static String[] getStringArray(String key) {
        try {
            return getStringArray(DEFAULT_CONFIG, key);
        } catch (Exception ex) {
            log.error(ex.getMessage(), ex);
            return new String[0];
        }
    }

    public static List<?> getList(String key) {
        try {
            return getList(DEFAULT_CONFIG, key);
        } catch (Exception ex) {
            log.error(ex.getMessage(), ex);
            return Collections.EMPTY_LIST;
        }
    }
}
