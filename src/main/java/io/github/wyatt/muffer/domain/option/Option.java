package io.github.wyatt.muffer.domain.option;

import io.github.wyatt.muffer.global.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;

@Entity
@Getter
public class Option extends BaseEntity {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @Enumerated(EnumType.STRING)
    OptionType type;

    String name;

    @Builder
    private Option(OptionType type, String name) {
        this.type = type;
        this.name = name;
    }

    public static Option create(OptionType type, String name) {
        return Option.builder()
                .type(type)
                .name(name)
                .build();
    }
}
