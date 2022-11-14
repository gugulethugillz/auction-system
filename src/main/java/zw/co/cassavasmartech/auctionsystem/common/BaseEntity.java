package zw.co.cassavasmartech.auctionsystem.common;

import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import zw.co.cassavasmartech.auctionsystem.common.enums.EntityStatus;

import javax.persistence.*;
import java.time.OffsetDateTime;

/**
 * Created by alfred on 18 September 2020
 */
@Data
@MappedSuperclass
public abstract class BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @CreationTimestamp
    @Column(columnDefinition = "TIMESTAMP WITH TIME ZONE")
    private OffsetDateTime dateCreated;

    @UpdateTimestamp
    @Column(columnDefinition = "TIMESTAMP WITH TIME ZONE")
    private OffsetDateTime lastUpdated;

    @Enumerated(EnumType.STRING)
    private EntityStatus status;

    private String createdBy;
    private String lastUpdatedBy;

    @Version
    private Long version;

    @PrePersist
    public void initialSetup() {
        if(status == null) status = EntityStatus.ACTIVE;
    }


}
