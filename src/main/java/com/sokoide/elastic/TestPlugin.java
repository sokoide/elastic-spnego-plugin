package com.sokoide.elastic;

// import org.elasticsearch.plugins.ActionPlugin;
// import org.elasticsearch.plugins.Plugin;
// import org.elasticsearch.rest.RestHandler;

// import java.util.Arrays;
// import java.util.List;

import org.elasticsearch.cluster.metadata.IndexNameExpressionResolver;
import org.elasticsearch.cluster.node.DiscoveryNodes;
import org.elasticsearch.common.settings.ClusterSettings;
import org.elasticsearch.common.settings.IndexScopedSettings;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.settings.SettingsFilter;
import org.elasticsearch.plugins.ActionPlugin;
import org.elasticsearch.plugins.Plugin;
import org.elasticsearch.rest.RestController;
import org.elasticsearch.rest.RestHandler;
import org.elasticsearch.common.logging.ServerLoggers;
import org.elasticsearch.common.util.concurrent.*;

import java.util.List;
import java.util.function.Supplier;
import java.util.function.UnaryOperator;

import static java.util.Collections.singletonList;

import java.nio.file.Path;
import org.apache.logging.log4j.Logger;

// ref: http://david.pilato.fr/blog/2016/10/19/adding-a-new-rest-endpoint-to-elasticsearch-updated-for-ga/
// ref: http://david.pilato.fr/blog/2016/10/16/creating-a-plugin-for-elasticsearch-5-dot-0-using-maven-updated-for-ga/
// ref: https://qbox.io/blog/secure-elasticsearch-authentication-plugin-tutorial
// ref: https://github.com/rayjcwu/dummy-es-auth-plugin
public class TestPlugin extends Plugin implements ActionPlugin {
    private final Settings settings;
    private final Logger logger;

    // @Override
    // public List<Class<? extends RestHandler>> getRestHandlers() {
    //     return Arrays.asList(SoKoideAuthRestHandler.class);
    // }

    public TestPlugin(Settings settings, Path p) {
        this.settings = settings;
        this.logger = ServerLoggers.getLogger(getClass(), settings);
    }

    @Override
    public List<RestHandler> getRestHandlers(final Settings settings, final RestController restController,
            final ClusterSettings clusterSettings, final IndexScopedSettings indexScopedSettings,
            final SettingsFilter settingsFilter, final IndexNameExpressionResolver indexNameExpressionResolver,
            final Supplier<DiscoveryNodes> nodesInCluster) {
        // this.logger = ServerLoggers.getLogger(getClass(), settings);

        logger.info("TestPlugin::getRestHandlers");

        return singletonList(new SoKoideAuthRestHandler(settings, restController));
    }

    // This is called per REST call
    @Override
    public UnaryOperator<RestHandler> getRestHandlerWrapper(ThreadContext threadContext) {
      return restHandler -> (RestHandler) (request, channel, client) -> {
        logger.info("TestPlugin::getRestHandlerWrapper");
        logger.info("request: {}", request);
        logger.info("channel: {}", channel);
        logger.info("client: {}", client);
        logger.info("restHandler: {}", restHandler);
        logger.info("restHandler.getClass: {}", restHandler.getClass());

        // Need to make sure we've fetched cluster-wide configuration at least once. This is super fast, so NP.
        // ThreadRepo.channel.set(channel);
        restHandler.handleRequest(request, channel, client);
      };
    }
}