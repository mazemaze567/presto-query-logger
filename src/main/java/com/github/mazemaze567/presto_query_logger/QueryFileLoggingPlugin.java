package com.github.mazemaze567.presto_query_logger;

import io.prestosql.spi.Plugin;
import io.prestosql.spi.eventlistener.EventListenerFactory;

import java.util.Collections;

public class QueryFileLoggingPlugin implements Plugin {
    @Override
    public Iterable<EventListenerFactory> getEventListenerFactories() {
        EventListenerFactory listenerFactory = new QueryFileLoggingEventListenerFactory();
        return Collections.singleton(listenerFactory);
    }
}
