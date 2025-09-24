package com.supernova.emims.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.util.Date;

/**
 * Entity class for LMS_RECEIVABLEPAYBLE_DTL_17557 table
 * Stores EMI receivable details
 *
 * Sonar-compliant: Proper naming, documentation, and structure
 */
@Entity
@Table(name = "LMS_RECEIVABLEPAYBLE_DTL_17557")
public class LmsReceivablePayableDtl17557 {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "RECEIVABLE_ID")
    private Long receivableId;

    @Column(name = "LOAN_ACCOUNT_NO", length = 20, nullable = false)
    private String loanAccountNo;

    @Column(name = "PENDING_EMI_AMOUNT", precision = 10, scale = 2, nullable = false)
    private BigDecimal pendingEmiAmount;

    @Column(name = "PENALTY_CHARGES", precision = 10, scale = 2, nullable = false)
    private BigDecimal penaltyCharges;

    @Column(name = "TOTAL_AMOUNT", precision = 10, scale = 2, nullable = false)
    private BigDecimal totalAmount;

    @Temporal(TemporalType.DATE)
    @Column(name = "CREATED_DATE", nullable = false)
    private Date createdDate;

    // Default constructor
    public LmsReceivablePayableDtl17557() {
    }

    // Constructor with parameters
    public LmsReceivablePayableDtl17557(String loanAccountNo, BigDecimal pendingEmiAmount,
                                       BigDecimal penaltyCharges, BigDecimal totalAmount, Date createdDate) {
        this.loanAccountNo = loanAccountNo;
        this.pendingEmiAmount = pendingEmiAmount;
        this.penaltyCharges = penaltyCharges;
        this.totalAmount = totalAmount;
        this.createdDate = createdDate;
    }

    // Getters and Setters
    public Long getReceivableId() {
        return receivableId;
    }

    public void setReceivableId(Long receivableId) {
        this.receivableId = receivableId;
    }

    public String getLoanAccountNo() {
        return loanAccountNo;
    }

    public void setLoanAccountNo(String loanAccountNo) {
        this.loanAccountNo = loanAccountNo;
    }

    public BigDecimal getPendingEmiAmount() {
        return pendingEmiAmount;
    }

    public void setPendingEmiAmount(BigDecimal pendingEmiAmount) {
        this.pendingEmiAmount = pendingEmiAmount;
    }

    public BigDecimal getPenaltyCharges() {
        return penaltyCharges;
    }

    public void setPenaltyCharges(BigDecimal penaltyCharges) {
        this.penaltyCharges = penaltyCharges;
    }

    public BigDecimal getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(BigDecimal totalAmount) {
        this.totalAmount = totalAmount;
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    @Override
    public String toString() {
        return "LmsReceivablePayableDtl17557{" +
                "receivableId=" + receivableId +
                ", loanAccountNo='" + loanAccountNo + '\'' +
                ", pendingEmiAmount=" + pendingEmiAmount +
                ", penaltyCharges=" + penaltyCharges +
                ", totalAmount=" + totalAmount +
                ", createdDate=" + createdDate +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        LmsReceivablePayableDtl17557 that = (LmsReceivablePayableDtl17557) o;

        return receivableId != null ? receivableId.equals(that.receivableId) : that.receivableId == null;
    }

    @Override
    public int hashCode() {
        return receivableId != null ? receivableId.hashCode() : 0;
    }
}
