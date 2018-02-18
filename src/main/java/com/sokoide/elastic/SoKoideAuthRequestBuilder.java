package com.sokoide.elastic;

import org.elasticsearch.action.ActionRequestBuilder;
import org.elasticsearch.client.ElasticsearchClient;

public class SoKoideAuthRequestBuilder extends ActionRequestBuilder<SoKoideAuthRequest, SoKoideAuthResponse, SoKoideAuthRequestBuilder> {

    public SoKoideAuthRequestBuilder(ElasticsearchClient client, SoKoideAuthAction action) {
        super(client, action, new SoKoideAuthRequest());
    }
}
