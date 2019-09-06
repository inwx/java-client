package com.inwx.domrobot;

public enum ApiType {
    JSON_RPC("/jsonrpc/");

    private String path;

    ApiType(String path) {
        this.path = path;
    }

    public String getPath() {
        return path;
    }
}
