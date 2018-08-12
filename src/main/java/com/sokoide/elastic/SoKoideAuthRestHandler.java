package com.sokoide.elastic;

import java.io.IOException;

import org.apache.logging.log4j.Logger;
import org.elasticsearch.client.node.NodeClient;
import org.elasticsearch.common.inject.Inject;
import org.elasticsearch.common.logging.ServerLoggers;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.rest.BaseRestHandler;
import org.elasticsearch.rest.RestController;
import org.elasticsearch.rest.RestHandler;
import org.elasticsearch.rest.RestRequest;
import org.elasticsearch.rest.action.RestToXContentListener;

// Reference
// http://david.pilato.fr/blog/2016/10/19/adding-a-new-rest-endpoint-to-elasticsearch-updated-for-ga/
public class SoKoideAuthRestHandler extends BaseRestHandler implements RestHandler {
    private final Logger logger;

    public String getName() {
        return "SoKoideAuthRestHandler";
    }

    @Inject
    public SoKoideAuthRestHandler(Settings settings, RestController controller) {
        super(settings);
        this.logger = ServerLoggers.getLogger(getClass(), settings);

        logger.info("SoKoideAuthResthandler::ctor");

        // SoKoideAuthRestHanlder is NOT used for ACL check
        // ACL is checked by IndexLevelActionFilter.java
        //
        // controller.registerHandler(RestRequest.Method.GET, "/customer/external/1", this);
        // controller.registerHandler(RestRequest.Method.GET, "/hoge1/*", this);
        // controller.registerHandler(RestRequest.Method.GET, "/hoge2/*/*", this);
        // controller.registerHandler(RestRequest.Method.GET, "/hoge2/*/*/*", this);
        // controller.registerHandler(RestRequest.Method.GET, "/customer/external/*", this);
    }

    @Override
    protected RestChannelConsumer prepareRequest(RestRequest request, NodeClient client) throws IOException {
        logger.info("prepareRequest");
        return (channel) -> {
            client.execute(
              new SoKoideAuthAction(),
              new SoKoideAuthRequest(request.method().name(), request.path(), request.content().utf8ToString()),
              new RestToXContentListener<SoKoideAuthResponse>(channel)
            );
          };
    }
}
