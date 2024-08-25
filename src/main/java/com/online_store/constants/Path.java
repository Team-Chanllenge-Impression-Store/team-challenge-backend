package com.online_store.constants;

public class Path {
    public static final String API_VERSION = "/v1";
    public static final String API_PATHNAME = "/api" + API_VERSION;
    public static final String AUTH = API_PATHNAME + "/auth";
    public static final String PRODUCT = API_PATHNAME + "/product";
    public static final String ORDER = API_PATHNAME + "/order";
    public static final String CATEGORY = API_PATHNAME + "/category";

    private Path() {
    }
}
