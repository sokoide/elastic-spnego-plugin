package com.sokoide.elastic;

import static java.util.Collections.singletonList;

import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.function.Supplier;
import java.util.function.UnaryOperator;

import org.apache.logging.log4j.Logger;
import org.elasticsearch.action.ActionRequest;
import org.elasticsearch.action.ActionResponse;
import org.elasticsearch.action.support.ActionFilter;
import org.elasticsearch.client.node.NodeClient;
import org.elasticsearch.cluster.metadata.IndexNameExpressionResolver;
import org.elasticsearch.cluster.node.DiscoveryNodes;
import org.elasticsearch.cluster.service.ClusterService;
import org.elasticsearch.common.io.stream.NamedWriteableRegistry;
import org.elasticsearch.common.logging.ServerLoggers;
import org.elasticsearch.common.settings.ClusterSettings;
import org.elasticsearch.common.settings.IndexScopedSettings;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.settings.SettingsFilter;
import org.elasticsearch.common.util.concurrent.ThreadContext;
import org.elasticsearch.common.xcontent.NamedXContentRegistry;
import org.elasticsearch.env.Environment;
import org.elasticsearch.env.NodeEnvironment;
import org.elasticsearch.plugins.ActionPlugin;
import org.elasticsearch.plugins.Plugin;
import org.elasticsearch.rest.RestController;
import org.elasticsearch.rest.RestHandler;
import org.elasticsearch.script.ScriptService;
import org.elasticsearch.watcher.ResourceWatcherService;
import org.elasticsearch.client.Client;
import org.elasticsearch.client.node.NodeClient;
import org.elasticsearch.threadpool.ThreadPool;

// ref: http://david.pilato.fr/blog/2016/10/19/adding-a-new-rest-endpoint-to-elasticsearch-updated-for-ga/
// ref: http://david.pilato.fr/blog/2016/10/16/creating-a-plugin-for-elasticsearch-5-dot-0-using-maven-updated-for-ga/
// ref: https://qbox.io/blog/secure-elasticsearch-authentication-plugin-tutorial
// ref: https://github.com/rayjcwu/dummy-es-auth-plugin
public class TestPlugin extends Plugin implements ActionPlugin {
    private final Settings settings;
    private final Logger logger;
    private IndexLevelActionFilter ilaf;
    // private SettingsObservableImpl settingsObservable;
    private Environment environment;

    // @Override
    // public List<Class<? extends RestHandler>> getRestHandlers() {
    //     return Arrays.asList(SoKoideAuthRestHandler.class);
    // }

    public TestPlugin(Settings settings, Path p) {
        this.settings = settings;
        this.logger = ServerLoggers.getLogger(getClass(), settings);
    }

    @Override
    public Collection<Object> createComponents(Client client, ClusterService clusterService, ThreadPool threadPool,
            ResourceWatcherService resourceWatcherService, ScriptService scriptService,
            NamedXContentRegistry xContentRegistry, Environment environment, NodeEnvironment nodeEnvironment,
            NamedWriteableRegistry namedWriteableRegistry) {
        List<Object> components = new ArrayList<>(3);
        try {
            this.environment = environment;
            // this.settingsObservable = new SettingsObservableImpl((NodeClient) client, settings, environment);
            // this.ilaf = new IndexLevelActionFilter(settings, clusterService, (NodeClient) client, threadPool,
            this.ilaf = new IndexLevelActionFilter(settings, clusterService, (NodeClient) client, threadPool,
                    environment);
        } catch (IOException e) {
            e.printStackTrace();
        }
        //   components.add(settingsObservable);
        return components;
    }

    @Override
    public List<ActionFilter> getActionFilters() {
        return Collections.singletonList(ilaf);
    }

    // @Override
    // public void onIndexModule(IndexModule indexModule) {
    //   super.onIndexModule(indexModule);
    // }

    @Override
    @SuppressWarnings({ "unchecked", "rawtypes" })
    public List<ActionHandler<? extends ActionRequest, ? extends ActionResponse>> getActions() {
        logger.info("TestPlugin::getActions");
        return Collections
                .singletonList(new ActionHandler(SoKoideAuthAction.INSTANCE, TransportSoKoideAuthAction.class));
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