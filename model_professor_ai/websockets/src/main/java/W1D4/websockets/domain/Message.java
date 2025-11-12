package W1D4.websockets.domain;

import jakarta.persistence.*;
import java.time.OffsetDateTime;

@Entity
public final class Message {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, updatable = false)
    private String sender;

    @Column(nullable = false, updatable = false)
    private String content;

    @Column(updatable = false)
    private OffsetDateTime timestamp;

    public Message() {} // Default constructor required by JPA

    public Message(String sender, String content) {
        this.sender = sender;
        this.content = content;
        this.timestamp = OffsetDateTime.now();
    }

    // only getters, no setters (immutable after creation)
    public Long getId() { return id; }
    public String getSender() { return sender; }
    public String getContent() { return content; }
    public OffsetDateTime getTimestamp() { return timestamp; }

    @Override
    public String toString() {
        return "Message{" + "id=" + id + ", sender='" + sender + '\'' + ", content='" + content + '\'' + ", timestamp=" + timestamp + '}';
    }
}
