package com.saniaky.entity.translation;

import lombok.Data;
import org.hibernate.annotations.Immutable;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.io.Serializable;

@Data
@Immutable
@Entity
@Table(name = "status_translation", schema = "app")
public class StatusTranslation implements Serializable {

    @EmbeddedId
    private EnumTranslationId id;

    @Column(name = "translation")
    private String translation;

}
