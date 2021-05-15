package com.saniaky.entity.translation;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;

@Data
@Embeddable
public class EnumTranslationId implements Serializable {

    @Column(name = "code")
    private String code;

    @Column(name = "locale")
    private String locale;

}
