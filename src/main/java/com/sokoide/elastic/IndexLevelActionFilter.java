package com.sokoide.elastic;

import java.io.IOException;
import java.util.HashMap;

import org.apache.logging.log4j.Logger;
import org.elasticsearch.action.ActionListener;
import org.elasticsearch.action.ActionRequest;
import org.elasticsearch.action.ActionResponse;
import org.elasticsearch.action.support.ActionFilter;
import org.elasticsearch.action.support.ActionFilterChain;
import org.elasticsearch.client.node.NodeClient;
import org.elasticsearch.cluster.service.ClusterService;
import org.elasticsearch.common.component.AbstractComponent;
import org.elasticsearch.common.inject.Singleton;
import org.elasticsearch.common.logging.ServerLoggers;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.env.Environment;
import org.elasticsearch.tasks.Task;
import org.elasticsearch.threadpool.ThreadPool;
import org.elasticsearch.ElasticsearchStatusException;
import org.elasticsearch.rest.RestStatus;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.IndicesRequest;
import org.elasticsearch.cluster.metadata.IndexNameExpressionResolver;
import org.elasticsearch.action.support.IndicesOptions;
import org.elasticsearch.action.get.GetRequest;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.action.delete.DeleteRequest;

@Singleton
public class IndexLevelActionFilter extends AbstractComponent implements ActionFilter {

    //private final ThreadPool threadPool;
    //private final ClusterService clusterService;
    //private final IndexNameExpressionResolver indexResolver;
    private final Settings settings;
    private final Logger logger;
    private HashMap<String, Boolean> restrictedIndices = new HashMap<String, Boolean>();


    // private final AtomicReference<Optional<ACL>> acl;
    // private final AtomicReference<ESContext> context = new AtomicReference<>();
    // private final LoggerShim loggerShim;

    public IndexLevelActionFilter(Settings settings, ClusterService clusterService, NodeClient client,
            ThreadPool threadPool, Environment env) throws IOException {
        super(settings);

        this.settings = settings;
        //this.threadPool = threadPool;
        //this.clusterService = clusterService;
        //this.indexResolver = new IndexNameExpressionResolver(this.settings);
        this.logger = ServerLoggers.getLogger(getClass(), this.settings);
        this.restrictedIndices.put("hoge2", false);
        this.restrictedIndices.put("hoge3", false);
    }

    //   public IndexLevelActionFilter(Settings settings,
    //                                 ClusterService clusterService,
    //                                 NodeClient client,
    //                                 ThreadPool threadPool,
    //                                 SettingsObservableImpl settingsObservable,
    //                                 Environment env
    //   )
    //     throws IOException {
    //     super(settings);
    //     loggerShim = ESContextImpl.mkLoggerShim(logger);

    //     BasicSettings baseSettings = BasicSettings.fromFileObj(loggerShim, env.configFile().toAbsolutePath(), settings);

    //     this.context.set(new ESContextImpl(client, baseSettings));

    //     this.clusterService = clusterService;
    //     this.indexResolver = new IndexNameExpressionResolver(settings);
    //     this.threadPool = threadPool;
    //     this.acl = new AtomicReference<>(Optional.empty());

    //     settingsObservable.addObserver((o, arg) -> {
    //       logger.info("Settings observer refreshing...");
    //       Environment newEnv = new Environment(settings, env.configFile().toAbsolutePath());
    //       BasicSettings newBasicSettings = new BasicSettings(settingsObservable.getCurrent(), newEnv.configFile().toAbsolutePath());
    //       ESContext newContext = new ESContextImpl(client, newBasicSettings);
    //       this.context.set(newContext);

    //       if (newContext.getSettings().isEnabled()) {
    //         try {
    //           ACL newAcl = new ACL(newContext);
    //           acl.set(Optional.of(newAcl));
    //           logger.info("Configuration reloaded - ReadonlyREST enabled");
    //         } catch (Exception ex) {
    //           logger.error("Cannot configure ReadonlyREST plugin", ex);
    //           throw ex;
    //         }
    //       }
    //       else {
    //         acl.set(Optional.empty());
    //         logger.info("Configuration reloaded - ReadonlyREST disabled");
    //       }
    //     });

    //     settingsObservable.forceRefresh();
    //     logger.info("Readonly REST plugin was loaded...");

    //     settingsObservable.pollForIndex(context.get());

    //   }

    @Override
    public int order() {
        return 0;
    }

    @Override
    public <Request extends ActionRequest, Response extends ActionResponse> void apply(Task task, String action,
            Request request, ActionListener<Response> listener, ActionFilterChain<Request, Response> chain) {
        logger.info("apply task:{}, action:{}, request:{}", task, action, request);
        String index = null;

        // dump indices
        if (request instanceof IndexRequest) {
            IndexRequest r = (IndexRequest) request;
            logger.info("IndexRequst:{}", r);
        }
        if (request instanceof GetRequest) {
            GetRequest r = (GetRequest) request;
            logger.info("GetRequest index:{},type:{},id:{}", r.index(), r.type(), r.id());
            index = r.index();
        }
        if (request instanceof UpdateRequest) {
            UpdateRequest r = (UpdateRequest) request;
            logger.info("UpdateRequest index:{},type:{},id:{}", r.index(), r.type(), r.id());
            index = r.index();
        }
        if (request instanceof DeleteRequest) {
            DeleteRequest r = (DeleteRequest) request;
            logger.info("DeleteRequest index:{},type:{},id:{}", r.index(), r.type(), r.id());
            index = r.index();
        }
        logger.info("request-class:{}", request.getClass());

        // TODO: check ACL here
        if (null != index && restrictedIndices.containsKey(index)) {
            logger.info("Rejecting request:{}", request);
            ElasticsearchStatusException exc = new ElasticsearchStatusException("Unauthorized",
                    RestStatus.UNAUTHORIZED);
            exc.addHeader("WWW-Authenticate", "Negotiate");
            listener.onFailure(exc);
        } else {
            chain.proceed(task, action, request, listener);
        }

        // Optional<ACL> acl = this.acl.get();
        // if (acl.isPresent()) {
        //     handleRequest(acl.get(), task, action, request, listener, chain);
        // } else {
        //     chain.proceed(task, action, request, listener);
        // }
    }

    // private <Request extends ActionRequest, Response extends ActionResponse> void handleRequest(ACL acl, Task task,
    //         String action, Request request, ActionListener<Response> listener,
    //         ActionFilterChain<Request, Response> chain) {
    //     RestChannel channel = ThreadRepo.channel.get();
    //     if (channel != null) {
    //         ThreadRepo.channel.remove();
    //     }

    //     boolean chanNull = channel == null;
    //     boolean reqNull = channel == null ? true : channel.request() == null;
    //     if (ACL.shouldSkipACL(chanNull, reqNull)) {
    //         chain.proceed(task, action, request, listener);
    //         return;
    //     }
    //     RequestInfo requestInfo = new RequestInfo(channel, task.getId(), action, request, clusterService, threadPool,
    //             context.get(), indexResolver);
    //     acl.check(requestInfo, new ACLHandler() {
    //         @Override
    //         public void onForbidden() {
    //             ElasticsearchStatusException exc = new ElasticsearchStatusException(
    //                     context.get().getSettings().getForbiddenMessage(),
    //                     acl.doesRequirePassword() ? RestStatus.UNAUTHORIZED : RestStatus.FORBIDDEN);
    //             if (acl.doesRequirePassword()) {
    //                 exc.addHeader("WWW-Authenticate", "Basic");
    //             }
    //             listener.onFailure(exc);
    //         }

    //         @Override
    //         public void onAllow(Object blockExitResult) {
    //             boolean hasProceeded = false;
    //             try {
    //                 //         @SuppressWarnings("unchecked")
    //                 //          ActionListener<Response> aclActionListener = (ActionListener<Response>) new ACLActionListener(
    //                 //            request, (ActionListener<ActionResponse>) listener, rc, blockExitResult, context, acl
    //                 //          );
    //                 //          chain.proceed(task, action, request, aclActionListener);

    //                 chain.proceed(task, action, request, listener);
    //                 hasProceeded = true;
    //                 return;
    //             } catch (Throwable e) {
    //                 e.printStackTrace();
    //             }
    //             if (!hasProceeded) {
    //                 chain.proceed(task, action, request, listener);
    //             }
    //         }

    //         @Override
    //         public boolean isNotFound(Throwable throwable) {
    //             return throwable.getCause() instanceof ResourceNotFoundException;
    //         }

    //         @Override
    //         public void onNotFound(Throwable throwable) {
    //             listener.onFailure((ResourceNotFoundException) throwable.getCause());
    //         }

    //         @Override
    //         public void onErrored(Throwable t) {
    //             listener.onFailure((Exception) t);
    //         }
    //     });

    // }

}
