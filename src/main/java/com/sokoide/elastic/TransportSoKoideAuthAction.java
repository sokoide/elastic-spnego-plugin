package com.sokoide.elastic;

// import cz.seznam.euphoria.shaded.guava.com.google.common.util.concurrent.FutureCallback;
import org.elasticsearch.action.ActionListener;
import org.elasticsearch.action.support.ActionFilters;
import org.elasticsearch.action.support.HandledTransportAction;
import org.elasticsearch.client.node.NodeClient;
import org.elasticsearch.cluster.metadata.IndexNameExpressionResolver;
import org.elasticsearch.common.inject.Inject;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.threadpool.ThreadPool;
import org.elasticsearch.transport.TransportService;
// import tech.beshu.ror.commons.settings.RawSettings;
// import tech.beshu.ror.commons.settings.SettingsUtils;
// import tech.beshu.ror.es.SettingsObservableImpl;

// import static tech.beshu.ror.commons.Constants.REST_CONFIGURATION_FILE_PATH;
// import static tech.beshu.ror.commons.Constants.REST_CONFIGURATION_PATH;
// import static tech.beshu.ror.commons.Constants.REST_REFRESH_PATH;

public class TransportSoKoideAuthAction extends HandledTransportAction<SoKoideAuthRequest, SoKoideAuthResponse> {

    private final NodeClient client;
    // private final SettingsObservableImpl settingsObservable;

    @Inject
    public TransportSoKoideAuthAction(Settings settings, ThreadPool threadPool, TransportService transportService,
            ActionFilters actionFilters, IndexNameExpressionResolver indexNameExpressionResolver, NodeClient client/*,
            SettingsObservableImpl settingsObservable*/) {
        super(settings, SoKoideAuthAction.NAME, threadPool, transportService, actionFilters,
                indexNameExpressionResolver, SoKoideAuthRequest::new);
        this.client = client;
        // this.settingsObservable = settingsObservable;

    }

    private String normalizePath(String s) {
        return s.substring(0, s.length() - (s.endsWith("/") ? 1 : 0));
    }

    @Override
    protected void doExecute(SoKoideAuthRequest request, ActionListener<SoKoideAuthResponse> listener) {
        try {
            String method = request.getMethod().toUpperCase();
            // String body = request.getContent();
            // String path = request.getPath();

            if ("POST".equals(method)) {
                // if (REST_REFRESH_PATH.equals(normalisePath(path))) {
                //     settingsObservable.refreshFromIndex();
                //     listener.onResponse(new SoKoideAuthResponse("ok refreshed"));
                //     return;
                // }
                // if (REST_CONFIGURATION_PATH.equals(normalisePath(path))) {
                //     if (body.length() == 0) {
                //         listener.onFailure(new Exception("empty body"));
                //         return;
                //     }
                //     // Can throw SettingsMalformedException
                //     settingsObservable.refreshFromStringAndPersist(
                //             new RawSettings(SettingsUtils.extractYAMLfromJSONStorage(body)), new FutureCallback() {
                //                 @Override
                //                 public void onSuccess(Object result) {
                //                     listener.onResponse(new SoKoideAuthResponse("updated settings"));
                //                 }

                //                 @Override
                //                 public void onFailure(Throwable t) {
                //                     listener.onFailure(new Exception("could not update settings ", t));
                //                 }
                //             });
                //     return;
                // }
            }

            if ("GET".equals(method)) {
                // if (REST_CONFIGURATION_FILE_PATH.equals(normalisePath(path))) {
                //     try {
                //         String currentSettingsYAML = settingsObservable.getFromFile().yaml();
                //         listener.onResponse(new SoKoideAuthResponse(currentSettingsYAML));
                //         System.out.println(currentSettingsYAML);
                //     } catch (Exception e) {
                //         listener.onFailure(e);
                //     }
                //     return;
                // }
                // if (REST_CONFIGURATION_PATH.equals(normalisePath(path))) {
                //     String currentSettingsYAML = settingsObservable.getCurrent().yaml();
                //     System.out.println(currentSettingsYAML);
                //     listener.onResponse(new SoKoideAuthResponse(currentSettingsYAML));
                //     return;
                // }
            }

            listener.onFailure(new Exception("Didn't find anything to handle this request"));

        } catch (Exception e) {
            listener.onResponse(new SoKoideAuthResponse(e));
        }
    }
}
