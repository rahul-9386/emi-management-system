package com.supernova.emims.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.util.Date;

/**
 * Entity class for LMS_RECEIPT_PAYMENT_DTL_17557 table
 * Stores user payment receipts
 *
 * Sonar-compliant: Proper naming, documentation, and structure
 */
@Entity
@Table(name = "LMS_RECEIPT_PAYMENT_DTL_17557")
public class LmsReceiptPaymentDtl17557 {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "RECEIPT_ID")
    private Long receiptId;

    @Column(name = "LOAN_ACCOUNT_NO", length = 20, nullable = false)
    private String loanAccountNo;

    @Column(name = "PAYMENT_AMOUNT", precision = 10, scale = 2, nullable = false)
    private BigDecimal paymentAmount;

    @Column(name = "PAYMENT_MODE", length = 20, nullable = false)
    private String paymentMode;

    @Temporal(TemporalType.DATE)
    @Column(name = "PAYMENT_DATE", nullable = false)
    private Date paymentDate;

    // Default constructor
    public LmsReceiptPaymentDtl17557() {
    }

    // Constructor with parameters
    public LmsReceiptPaymentDtl17557(String loanAccountNo, BigDecimal paymentAmount,
                                   String paymentMode, Date paymentDate) {
        this.loanAccountNo = loanAccountNo;
        this.paymentAmount = paymentAmount;
        this.paymentMode = paymentMode;
        this.paymentDate = paymentDate;
    }

    // Getters and Setters
    public Long getReceiptId() {
        return receiptId;
    }

    public void setReceiptId(Long receiptId) {
        this.receiptId = receiptId;
    }

    public String getLoanAccountNo() {
        return loanAccountNo;
    }

    public void setLoanAccountNo(String loanAccountNo) {
        this.loanAccountNo = loanAccountNo;
    }

    public BigDecimal getPaymentAmount() {
        return paymentAmount;
    }

    public void setPaymentAmount(BigDecimal paymentAmount) {
        this.paymentAmount = paymentAmount;
    }

    public String getPaymentMode() {
        return paymentMode;
    }

    public void setPaymentMode(String paymentMode) {
        this.paymentMode = paymentMode;
    }

    public Date getPaymentDate() {
        return paymentDate;
    }

    public void setPaymentDate(Date paymentDate) {
        this.paymentDate = paymentDate;
    }

    @Override
    public String toString() {
        return "LmsReceiptPaymentDtl17557{" +
                "receiptId=" + receiptId +
                ", loanAccountNo='" + loanAccountNo + '\'' +
                ", paymentAmount=" + paymentAmount +
                ", paymentMode='" + paymentMode + '\'' +
                ", paymentDate=" + paymentDate +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        LmsReceiptPaymentDtl17557 that = (LmsReceiptPaymentDtl17557) o;

        return receiptId != null ? receiptId.equals(that.receiptId) : that.receiptId == null;
    }

    @Override
    public int hashCode() {
        return receiptId != null ? receiptId.hashCode() : 0;
    }
}
