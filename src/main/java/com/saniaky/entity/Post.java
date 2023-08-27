package com.saniaky.entity;

import com.saniaky.entity.translation.StatusTranslation;
import lombok.Data;
import org.hibernate.annotations.Immutable;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;
import java.util.UUID;

@Data
@Entity
@Table(name = "post", schema = "app")
public class Post implements Serializable {

    @Id
    // @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private UUID id;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private Status status;

    /**
     * Used for mapping only.
     * Don't use it directly, since it might trigger additional selects.
     */
    @Immutable
    @OneToMany(fetch = FetchType.LAZY)
    @JoinColumn(name = "code", referencedColumnName = "status", insertable = false, updatable = false)
    private List<StatusTranslation> statusTranslations;

    @Transient
    private String statusName;

    public Post() {
        // default constructor
    }

    public Post(Post post, StatusTranslation statusTranslation) {
        this.id = post.getId();
        this.setStatusName(statusTranslation.getTranslation());
    }

    public String getStatusName() {
        String fallback = status != null ? status.name() : "";
        return statusName != null ? statusName : fallback;
    }

}
