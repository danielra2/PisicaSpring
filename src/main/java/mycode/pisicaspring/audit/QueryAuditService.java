package mycode.pisicaspring.audit;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.Map;

@Component
public class QueryAuditService {
    private static final Logger LOGGER = LoggerFactory.getLogger(QueryAuditService.class);
    private final QueryAuditLogRepository repository;
    private final ObjectMapper objectMapper = new ObjectMapper();

    public QueryAuditService(QueryAuditLogRepository repository) {
        this.repository = repository;
    }

    @Transactional
    public void record(String endpoint, Map<String, Object> metadata) {
        LOGGER.info("Query endpoint invoked: {} - {}", endpoint, metadata);
        String serialized = serialize(metadata);
        QueryAuditLog log = new QueryAuditLog(endpoint, serialized, Instant.now());
        repository.save(log);
    }

    private String serialize(Map<String, Object> metadata) {
        try {
            return objectMapper.writeValueAsString(metadata);
        } catch (JsonProcessingException e) {
            LOGGER.warn("Failed to serialize audit metadata, falling back to toString()", e);
            return String.valueOf(metadata);
        }
    }
}
