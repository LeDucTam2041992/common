package com.kpro.common.entity;

import jakarta.persistence.*;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.Objects;
import java.util.UUID;

@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public abstract class CommonEntity {

    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "uuid2")
    private UUID id;

    @Column(name = "is_deleted", columnDefinition = "boolean default false", nullable = false)
    private boolean isDeleted;

    @Column(name = "deleted_at")
    private LocalDateTime deletedAt;

    @Column(name = "created_at")
    @CreatedDate
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdAt;

    @Column(name = "updated_at")
    @LastModifiedDate
    @Temporal(TemporalType.TIMESTAMP)
    private Date updatedAt;

    @Column(name = "is_traveled_time", columnDefinition = "boolean default false", nullable = false)
    private boolean isTraveledTime;

    @CreatedBy
    @Column(name = "created_by")
    private String createdBy;

    protected CommonEntity() {
    }

    protected CommonEntity(UUID id, boolean isDeleted, LocalDateTime deletedAt, Date createdAt, Date updatedAt, boolean isTraveledTime, String createdBy) {
        this.id = id;
        this.isDeleted = isDeleted;
        this.deletedAt = deletedAt;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.isTraveledTime = isTraveledTime;
        this.createdBy = createdBy;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public boolean isDeleted() {
        return isDeleted;
    }

    public LocalDateTime getDeletedAt() {
        return deletedAt;
    }

    public void setDeletedAt(LocalDateTime deletedAt) {
        this.deletedAt = deletedAt;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public Date getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Date updatedAt) {
        this.updatedAt = updatedAt;
    }

    public boolean isTraveledTime() {
        return isTraveledTime;
    }

    public void setTraveledTime(boolean traveledTime) {
        isTraveledTime = traveledTime;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public void setDeleted(boolean deleted) {
        this.isDeleted = deleted;
        this.deletedAt = LocalDateTime.now();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CommonEntity that = (CommonEntity) o;
        return isDeleted == that.isDeleted && isTraveledTime == that.isTraveledTime && Objects.equals(id, that.id) && Objects.equals(deletedAt, that.deletedAt) && Objects.equals(createdAt, that.createdAt) && Objects.equals(updatedAt, that.updatedAt) && Objects.equals(createdBy, that.createdBy);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, isDeleted, deletedAt, createdAt, updatedAt, isTraveledTime, createdBy);
    }

    @Override
    public String toString() {
        return "CommonEntity{" +
                "id=" + id +
                ", isDeleted=" + isDeleted +
                ", deletedAt=" + deletedAt +
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                ", isTraveledTime=" + isTraveledTime +
                ", createdBy='" + createdBy + '\'' +
                '}';
    }
}
