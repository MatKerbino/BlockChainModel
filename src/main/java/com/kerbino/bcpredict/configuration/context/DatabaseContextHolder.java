package com.kerbino.bcpredict.configuration.context;

public class DatabaseContextHolder {
    private static final ThreadLocal<String> CONTEXT = new ThreadLocal<>();

    public static void setCurrentDatabase(String db){
        CONTEXT.set(db);
    }

    public static String getCurrentDatabase(){
        return CONTEXT.get();
    }

    public static void clear(){
        CONTEXT.remove();
    }
}
