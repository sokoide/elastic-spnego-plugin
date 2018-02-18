package com.sokoide.elastic;

import org.elasticsearch.action.Action;
import org.elasticsearch.client.ElasticsearchClient;

public class SoKoideAuthAction extends Action<SoKoideAuthRequest, SoKoideAuthResponse, SoKoideAuthRequestBuilder> {

    public static final String NAME = "cluster:admin/SoKoideAuth/refreshsettings";
    public static final SoKoideAuthAction INSTANCE = new SoKoideAuthAction();

    public SoKoideAuthAction() {
        super(NAME);
    }

    @Override
    public SoKoideAuthRequestBuilder newRequestBuilder(ElasticsearchClient client) {
        return new SoKoideAuthRequestBuilder(client, INSTANCE);
    }

    @Override
    public SoKoideAuthResponse newResponse() {
        return new SoKoideAuthResponse("static instance");
    }
}