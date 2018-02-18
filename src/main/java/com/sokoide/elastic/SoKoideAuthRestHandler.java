package com.sokoide.elastic;

import org.elasticsearch.ElasticsearchException;
import org.elasticsearch.client.node.NodeClient;
import org.elasticsearch.common.inject.Inject;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.rest.*;

import java.io.IOException;
import java.util.Map;
import java.util.List;

import org.apache.logging.log4j.Logger;
import org.elasticsearch.common.logging.ServerLoggers;

// Reference
// http://david.pilato.fr/blog/2016/10/19/adding-a-new-rest-endpoint-to-elasticsearch-updated-for-ga/
public class SoKoideAuthRestHandler extends BaseRestHandler {
    private final Logger logger;

    @Inject
    public SoKoideAuthRestHandler(Settings settings, RestController controller) {
        super(settings);
        this.logger = ServerLoggers.getLogger(getClass(), settings);

        logger.info("SoKoideAuthResthandler::ctor");
        controller.registerHandler(RestRequest.Method.GET, "/external/1", this);
    }

    // @Override
    // public void handleRequest(RestRequest request, RestChannel channel, NodeClient client) throws IOException {
    //     logger.info("handleRequest");
    //     List<String> header = request.getHeaders().get("auth");
    //     if (authenticate(header)) {
    //         // execute the actual request
    //         logger.info("header {}", header);
    //     } else
    //         throw new RuntimeException("authentication failed");
    // }

    private boolean authenticate(List<String> header) {
        logger.info("authenticate");
        // TODO Use external services to authenticate further
        return true;
    }

    @Override
    public String getName() {
        logger.info("getName");
        return "SoKoideAuthRestHandler";
    }

    @Override
    protected RestChannelConsumer prepareRequest(RestRequest restRequest, NodeClient client) throws IOException {
        logger.info("prepareRequest");
        // Actually this method should not be invoked, since there is no corresponding PATH for this handler
        // Return {} in case.
        return channel -> {
            XContentBuilder builder = channel.newBuilder();
            builder.startObject();
            builder.endObject();
            channel.sendResponse(new BytesRestResponse(RestStatus.OK, builder));
        };
    }
}
