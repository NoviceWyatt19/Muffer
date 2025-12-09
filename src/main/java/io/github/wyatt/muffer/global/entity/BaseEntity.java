package io.github.wyatt.muffer.global.entity;

import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import java.time.LocalDateTime;

@MappedSuperclass
@Getter
public class BaseEntity {
    @CreatedDate
    protected LocalDateTime createdAt;
    @LastModifiedDate
    protected LocalDateTime modifiedAt;
    protected LocalDateTime deletedAt;

    public void deActivated() {
        this.deletedAt = LocalDateTime.now();
    }
    public Boolean isActivated() {
        return deletedAt == null;
    }
}
