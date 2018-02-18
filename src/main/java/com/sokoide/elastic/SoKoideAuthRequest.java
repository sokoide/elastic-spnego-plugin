package com.sokoide.elastic;

import org.elasticsearch.action.ActionRequest;
import org.elasticsearch.action.ActionRequestValidationException;

public class SoKoideAuthRequest extends ActionRequest {

    private String method;
    private String content;
    private String path;

    public SoKoideAuthRequest() {
    }

    public SoKoideAuthRequest(String method, String path, String content) {
        this.method = method;
        this.content = content;
        this.path = path;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public String getContent() {

        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    @Override
    public ActionRequestValidationException validate() {
        return null;
    }

}