package com.sse.attemper.config.scheduler.interceptor;

import com.sse.attemper.common.constant.CommonConstants;
import io.grpc.*;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class HeaderClientInterceptor implements ClientInterceptor {

    private static final Metadata.Key<String> CUSTOM_HEADER_KEY_TENANT_ID =
            Metadata.Key.of(CommonConstants.tenantId, Metadata.ASCII_STRING_MARSHALLER);

    protected String tenantId;

    public HeaderClientInterceptor(String tenantId) {
        this.tenantId = tenantId;
    }

    @Override
    public <ReqT, RespT> ClientCall<ReqT, RespT> interceptCall(MethodDescriptor<ReqT, RespT> method, CallOptions callOptions, Channel next) {
        return new ForwardingClientCall.SimpleForwardingClientCall<ReqT, RespT>(next.newCall(method, callOptions)) {

            @Override
            public void start(Listener<RespT> responseListener, Metadata headers) {
                /* put custom header */
                headers.put(CUSTOM_HEADER_KEY_TENANT_ID, tenantId);
                super.start(new ForwardingClientCallListener.SimpleForwardingClientCallListener<RespT>(responseListener) {
                    @Override
                    public void onHeaders(Metadata headers) {
                        /**
                         * if you don't need receive header from server,
                         * you can use {@link io.grpc.stub.MetadataUtils#attachHeaders}
                         * directly to send header
                         */
                        log.info("header received from server:" + headers);
                        super.onHeaders(headers);
                    }
                }, headers);
            }
        };
    }
}
