package com.github.attemper.core.ext.notice.channel;

import com.github.attemper.core.ext.notice.MessageBean;

public interface Sender {

    void send(MessageBean messageBean) throws Exception;

    int getIndex();
}
