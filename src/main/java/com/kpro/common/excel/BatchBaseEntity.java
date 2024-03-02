package com.kpro.common.excel;

import com.kpro.common.entity.CommonEntity;
import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.Objects;
import java.util.UUID;

@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public abstract class BatchBaseEntity extends CommonEntity {
    @Column(name = "status")
    private String verifyStatus;

    @Column(name = "error_desc")
    private String errorDesc;

    @Column(name = "order_num")
    private int orderNum;

    public BatchBaseEntity() {
    }

    protected BatchBaseEntity(String verifyStatus, String errorDesc, int orderNum) {
        this.verifyStatus = verifyStatus;
        this.errorDesc = errorDesc;
        this.orderNum = orderNum;
    }

    protected BatchBaseEntity(UUID id, boolean isDeleted, LocalDateTime deletedAt, Date createdAt, Date updatedAt, boolean isTraveledTime, String createdBy, String verifyStatus, String errorDesc, int orderNum) {
        super(id, isDeleted, deletedAt, createdAt, updatedAt, isTraveledTime, createdBy);
        this.verifyStatus = verifyStatus;
        this.errorDesc = errorDesc;
        this.orderNum = orderNum;
    }

    public String getVerifyStatus() {
        return verifyStatus;
    }

    public void setVerifyStatus(String verifyStatus) {
        this.verifyStatus = verifyStatus;
    }

    public String getErrorDesc() {
        return errorDesc;
    }

    public void setErrorDesc(String errorDesc) {
        this.errorDesc = errorDesc;
    }

    public int getOrderNum() {
        return orderNum;
    }

    public void setOrderNum(int orderNum) {
        this.orderNum = orderNum;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof BatchBaseEntity)) return false;
        if (!super.equals(o)) return false;
        BatchBaseEntity that = (BatchBaseEntity) o;
        return getOrderNum() == that.getOrderNum() && Objects.equals(getVerifyStatus(), that.getVerifyStatus()) && Objects.equals(getErrorDesc(), that.getErrorDesc());
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), getVerifyStatus(), getErrorDesc(), getOrderNum());
    }

    @Override
    public String toString() {
        return "BatchBaseEntity{" +
                "verifyStatus='" + verifyStatus + '\'' +
                ", errorDesc='" + errorDesc + '\'' +
                ", orderNum=" + orderNum +
                '}';
    }
}
