import io.prestosql.spi.eventlistener.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.LoggerContext;
import org.apache.logging.log4j.message.ObjectMessage;

import java.time.Duration;

public class QueryFileLoggingEventListener implements EventListener {
    private final Logger logger;

    public QueryFileLoggingEventListener(LoggerContext loggerContext) {
        this.logger = loggerContext.getLogger(this.getClass().getName());
    }

    public QueryFileLoggingEventListener() {
        this.logger = LogManager.getLogger();
    }

    @Override
    public void queryCompleted(QueryCompletedEvent queryCompletedEvent) {
        if (queryCompletedEvent == null) {
            return;
        }
        logger.info(new ObjectMessage(new QueryMessage(queryCompletedEvent)));
    }

    private static class QueryMessage {
        public final QueryMetadataMessage queryMetadataMessage;
        public final QueryContextMessage queryContextMessage;
        public final QueryStatisticsMessage queryStatisticsMessage;
        public final QueryFailureInfoMessage queryFailureInfoMessage;
        public final String createTime;
        public final String executionStartTime;
        public final String endTime;
        public final long elapsedTimeMilliseconds;
        public final long executionTimeMilliseconds;

        public QueryMessage(QueryCompletedEvent queryCompletedEvent) {
            this.queryMetadataMessage = new QueryMetadataMessage(queryCompletedEvent.getMetadata());
            this.queryContextMessage = new QueryContextMessage(queryCompletedEvent.getContext());
            this.queryStatisticsMessage = new QueryStatisticsMessage(queryCompletedEvent.getStatistics());
            // convert Optional object to non-Optional object for Jackson object mapper (used in log4j2)
            if (queryCompletedEvent.getFailureInfo().isPresent()) {
                this.queryFailureInfoMessage = new QueryFailureInfoMessage(queryCompletedEvent.getFailureInfo().get());
            } else {
                this.queryFailureInfoMessage = null;
            }
            this.createTime = queryCompletedEvent.getCreateTime().toString();
            this.executionStartTime = queryCompletedEvent.getExecutionStartTime().toString();
            this.endTime = queryCompletedEvent.getEndTime().toString();
            this.elapsedTimeMilliseconds = Duration.between(queryCompletedEvent.getCreateTime(), queryCompletedEvent.getEndTime()).toMillis();
            this.executionTimeMilliseconds = Duration.between(queryCompletedEvent.getExecutionStartTime(), queryCompletedEvent.getEndTime()).toMillis();
        }
    }

    // Message class for Presto's QueryMetadata class
    private static class QueryMetadataMessage {
        public final String queryId;
        public final String queryState;
        public final String query;

        private QueryMetadataMessage(QueryMetadata queryMetadata) {
            this.queryId = queryMetadata.getQueryId();
            this.queryState = queryMetadata.getQueryState();
            this.query = queryMetadata.getQuery();
        }
    }

    // Message class for Presto's QueryContext class
    private static class QueryContextMessage {
        public final String user;
        public final String source;
        public final String catalog;
        public final String schema;
        public final String remoteClientAddress;

        public QueryContextMessage(QueryContext queryContext) {
            this.user = queryContext.getUser();
            this.source = queryContext.getSource().orElse("-");
            this.catalog = queryContext.getCatalog().orElse("-");
            this.schema = queryContext.getSchema().orElse("-");
            this.remoteClientAddress = queryContext.getRemoteClientAddress().orElse("-");
        }
    }

    // Message class for Presto's QueryStatistics class
    private static class QueryStatisticsMessage {
        public final long cpuTimeMilliseconds;
        public final long peakUserMemoryBytes;
        public final long peakTaskUserMemoryBytes;
        public final long peakTaskTotalMemoryBytes;
        public final long physicalInputBytes;
        public final long physicalInputRows;
        public final long internalNetworkBytes;
        public final long internalNetworkRows;
        public final long totalBytes;
        public final long totalRows;
        public final long outputBytes;
        public final long outputRows;
        public final double cumulativeMemoryBytes;
        public final int completedSplits;

        private QueryStatisticsMessage(QueryStatistics queryStatistics) {
            this.cpuTimeMilliseconds = queryStatistics.getCpuTime().toMillis();
            this.peakUserMemoryBytes = queryStatistics.getPeakUserMemoryBytes();
            this.peakTaskUserMemoryBytes = queryStatistics.getPeakTaskUserMemory();
            this.peakTaskTotalMemoryBytes = queryStatistics.getPeakTaskTotalMemory();
            this.physicalInputBytes = queryStatistics.getPhysicalInputBytes();
            this.physicalInputRows = queryStatistics.getPhysicalInputRows();
            this.internalNetworkBytes = queryStatistics.getInternalNetworkBytes();
            this.internalNetworkRows = queryStatistics.getInternalNetworkRows();
            this.totalBytes = queryStatistics.getTotalBytes();
            this.totalRows = queryStatistics.getTotalRows();
            this.outputBytes = queryStatistics.getOutputBytes();
            this.outputRows = queryStatistics.getOutputRows();
            this.cumulativeMemoryBytes = queryStatistics.getCumulativeMemory();
            this.completedSplits = queryStatistics.getCompletedSplits();
        }
    }

    // Message class for Presto's QueryFailureInfo class
    private static class QueryFailureInfoMessage {
        public final String failureType;
        public final String failureMessage;
        public final String failureTask;
        public final String failureHost;

        private QueryFailureInfoMessage(QueryFailureInfo queryFailureInfo) {
            this.failureType = queryFailureInfo.getFailureType().orElse("-");
            this.failureMessage = queryFailureInfo.getFailureMessage().orElse("-");
            this.failureTask = queryFailureInfo.getFailureTask().orElse("-");
            this.failureHost = queryFailureInfo.getFailureHost().orElse("-");
        }
    }
}
