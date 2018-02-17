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

import java.util.List;
import java.util.function.Supplier;

import static java.util.Collections.singletonList;

import org.apache.logging.log4j.Logger;
import org.elasticsearch.common.logging.ServerLoggers;

// ref: http://david.pilato.fr/blog/2016/10/19/adding-a-new-rest-endpoint-to-elasticsearch-updated-for-ga/
// ref: http://david.pilato.fr/blog/2016/10/16/creating-a-plugin-for-elasticsearch-5-dot-0-using-maven-updated-for-ga/
// ref: https://qbox.io/blog/secure-elasticsearch-authentication-plugin-tutorial
// ref: https://github.com/rayjcwu/dummy-es-auth-plugin
public class TestPlugin extends Plugin implements ActionPlugin {
    private Logger logger;

    // @Override
    // public List<Class<? extends RestHandler>> getRestHandlers() {
    //     return Arrays.asList(SoKoideAuthRestHandler.class);
    // }
    
    @Override
    public List<RestHandler> getRestHandlers(final Settings settings, final RestController restController,
            final ClusterSettings clusterSettings, final IndexScopedSettings indexScopedSettings,
            final SettingsFilter settingsFilter, final IndexNameExpressionResolver indexNameExpressionResolver,
            final Supplier<DiscoveryNodes> nodesInCluster) {
        this.logger = ServerLoggers.getLogger(getClass(), settings);

        logger.info("***** TestPlugin ctor");

        return singletonList(new SoKoideAuthRestHandler(settings, restController));
    }
}