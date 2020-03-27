package com.github.mazemaze567.presto_query_logger;

import io.prestosql.spi.eventlistener.EventListener;
import io.prestosql.spi.eventlistener.EventListenerFactory;
import org.apache.logging.log4j.core.LoggerContext;
import org.apache.logging.log4j.core.config.Configurator;

import java.util.Map;

public class QueryFileLoggingEventListenerFactory implements EventListenerFactory {
    private static final String KEY_LOG4J2_CONFIG_LOCATION = "presto-query-logger.log4j2.config-location";

    @Override
    public String getName() {
        return "query-file-logging-event-listener";
    }

    @Override
    public EventListener create(Map<String, String> configMap) {
        String log4j2ConfigLocation = configMap.get(KEY_LOG4J2_CONFIG_LOCATION);
        if (log4j2ConfigLocation == null) {
            return new QueryFileLoggingEventListener();
        }
        LoggerContext loggerContext = Configurator.initialize("query-file-logging-event-listener", log4j2ConfigLocation);
        return new QueryFileLoggingEventListener(loggerContext);
    }
}
