package org.xbib.elasticsearch.action;

import org.elasticsearch.common.inject.AbstractModule;
import org.elasticsearch.common.settings.Settings;
import org.xbib.elasticsearch.s3.S3;
import org.xbib.elasticsearch.s3.S3Impl;
import org.xbib.elasticsearch.s3.S3Noop;

public class RestHandlerModule extends AbstractModule {

    private final Settings settings;

    public RestHandlerModule(Settings settings) {
        this.settings = settings;
    }

    @Override
    protected void configure() {
        bind(RestExportAction.class).asEagerSingleton();
        bind(RestImportAction.class).asEagerSingleton();

        if(settings.getAsBoolean("s3.bucket_name", null) != null){
            bind(S3.class).to(S3Impl.class);
        } else {
            bind(S3.class).to(S3Noop.class);
        }

    }
}
