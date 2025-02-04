package com.kerbino.bcpredict.configuration.context;

/**
 * Helper para definir e recuperar o datasource atual.
 */
public class DatabaseContextHolder {

    private static final ThreadLocal<String> contextHolder = new ThreadLocal<>();

    public static void setCurrentDatabase(String databaseName) {
        contextHolder.set(databaseName);
    }

    public static String getCurrentDatabase() {
        return contextHolder.get();
    }

    public static void clear() {
        contextHolder.remove();
    }
}
