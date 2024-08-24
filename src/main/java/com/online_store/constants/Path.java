package com.online_store.constants;

public class Path {
    // TODO: check wtf with AuthEntryPoint when you add v1 to auth pathname
    //    public static final String API_VERSION = "/v1";
//    public static final String API_PATHNAME = "api" + API_VERSION;
//    public static final String AUTH = API_PATHNAME + "/auth";
    public static final String API_PATHNAME = "/api";
    public static final String AUTH = API_PATHNAME + "/auth";
    public static final String PRODUCT = API_PATHNAME + "/product";
    public static final String ORDER = API_PATHNAME + "/order";

    private Path() {
    }
}
