package mycode.pisicaspring.audit;

import jakarta.persistence.*;

import java.time.Instant;

@Entity
@Table(name = "query_audit_log")
public class QueryAuditLog {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 255)
    private String endpoint;

    @Column(nullable = false, length = 2048)
    private String metadata;

    @Column(nullable = false)
    private Instant createdAt;

    protected QueryAuditLog() {
    }

    public QueryAuditLog(String endpoint, String metadata, Instant createdAt) {
        this.endpoint = endpoint;
        this.metadata = metadata;
        this.createdAt = createdAt;
    }

    public Long getId() {
        return id;
    }

    public String getEndpoint() {
        return endpoint;
    }

    public String getMetadata() {
        return metadata;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }
}
