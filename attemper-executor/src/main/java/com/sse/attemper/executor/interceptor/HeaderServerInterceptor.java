package com.sse.attemper.executor.interceptor;

import com.sse.attemper.common.constant.CommonConstants;
import com.sse.attemper.common.exception.RTException;
import io.grpc.*;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class HeaderServerInterceptor implements ServerInterceptor {

    private static final Metadata.Key<String> CUSTOM_HEADER_KEY_TENANT_ID =
            Metadata.Key.of(CommonConstants.tenantId, Metadata.ASCII_STRING_MARSHALLER);

    @Override
    public <ReqT, RespT> ServerCall.Listener<ReqT> interceptCall(
            ServerCall<ReqT, RespT> call,
            final Metadata requestHeaders,
            ServerCallHandler<ReqT, RespT> next) {
        final String tenantId = requestHeaders.get(CUSTOM_HEADER_KEY_TENANT_ID);
        if (tenantId == null) {   // just validate header simply
            throw new RTException(1500, tenantId);
        }
        return next.startCall(new ForwardingServerCall.SimpleForwardingServerCall<ReqT, RespT>(call) {
            @Override
            public void sendHeaders(Metadata responseHeaders) {
                responseHeaders.put(CUSTOM_HEADER_KEY_TENANT_ID, tenantId);
                super.sendHeaders(responseHeaders);
            }
        }, requestHeaders);
    }
}
