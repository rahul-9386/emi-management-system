package com.supernova.emims.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.util.Date;

/**
 * Entity class for LMS_ALLOCATION_DTL_17557_ table
 * Stores allocation details of payments
 *
 * Sonar-compliant: Proper naming, documentation, and structure
 */
@Entity
@Table(name = "LMS_ALLOCATION_DTL_17557_")
public class LmsAllocationDtl17557 {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "ALLOCATION_ID")
    private Long allocationId;

    @Column(name = "LOAN_ACCOUNT_NO", length = 20, nullable = false)
    private String loanAccountNo;

    @Column(name = "ALLOCATED_TO", length = 20, nullable = false)
    private String allocatedTo;

    @Column(name = "ALLOCATED_AMOUNT", precision = 10, scale = 2, nullable = false)
    private BigDecimal allocatedAmount;

    @Temporal(TemporalType.DATE)
    @Column(name = "ALLOCATION_DATE", nullable = false)
    private Date allocationDate;

    // Default constructor
    public LmsAllocationDtl17557() {
    }

    // Constructor with parameters
    public LmsAllocationDtl17557(String loanAccountNo, String allocatedTo,
                               BigDecimal allocatedAmount, Date allocationDate) {
        this.loanAccountNo = loanAccountNo;
        this.allocatedTo = allocatedTo;
        this.allocatedAmount = allocatedAmount;
        this.allocationDate = allocationDate;
    }

    // Getters and Setters
    public Long getAllocationId() {
        return allocationId;
    }

    public void setAllocationId(Long allocationId) {
        this.allocationId = allocationId;
    }

    public String getLoanAccountNo() {
        return loanAccountNo;
    }

    public void setLoanAccountNo(String loanAccountNo) {
        this.loanAccountNo = loanAccountNo;
    }

    public String getAllocatedTo() {
        return allocatedTo;
    }

    public void setAllocatedTo(String allocatedTo) {
        this.allocatedTo = allocatedTo;
    }

    public BigDecimal getAllocatedAmount() {
        return allocatedAmount;
    }

    public void setAllocatedAmount(BigDecimal allocatedAmount) {
        this.allocatedAmount = allocatedAmount;
    }

    public Date getAllocationDate() {
        return allocationDate;
    }

    public void setAllocationDate(Date allocationDate) {
        this.allocationDate = allocationDate;
    }

    @Override
    public String toString() {
        return "LmsAllocationDtl17557{" +
                "allocationId=" + allocationId +
                ", loanAccountNo='" + loanAccountNo + '\'' +
                ", allocatedTo='" + allocatedTo + '\'' +
                ", allocatedAmount=" + allocatedAmount +
                ", allocationDate=" + allocationDate +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        LmsAllocationDtl17557 that = (LmsAllocationDtl17557) o;

        return allocationId != null ? allocationId.equals(that.allocationId) : that.allocationId == null;
    }

    @Override
    public int hashCode() {
        return allocationId != null ? allocationId.hashCode() : 0;
    }
}
