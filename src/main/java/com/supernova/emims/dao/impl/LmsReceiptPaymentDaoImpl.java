package com.supernova.emims.dao.impl;

import com.supernova.emims.dao.LmsReceiptPaymentDao;
import com.supernova.emims.entity.LmsReceiptPaymentDtl17557;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

/**
 * Hibernate DAO implementation for LMS_RECEIPT_PAYMENT_DTL_17557 operations
 * Uses Hibernate ORM for database operations
 *
 * Sonar-compliant: Proper exception handling and resource management
 */
@Repository
@Transactional
public class LmsReceiptPaymentDaoImpl implements LmsReceiptPaymentDao {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public LmsReceiptPaymentDtl17557 save(LmsReceiptPaymentDtl17557 receipt) {
        if (receipt.getReceiptId() == null) {
            entityManager.persist(receipt);
            return receipt;
        } else {
            return entityManager.merge(receipt);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<LmsReceiptPaymentDtl17557> findById(Long id) {
        try {
            LmsReceiptPaymentDtl17557 receipt = entityManager.find(LmsReceiptPaymentDtl17557.class, id);
            return Optional.ofNullable(receipt);
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    @Override
    @Transactional(readOnly = true)
    public List<LmsReceiptPaymentDtl17557> findByLoanAccountNo(String loanAccountNo) {
        TypedQuery<LmsReceiptPaymentDtl17557> query = entityManager.createQuery(
                "SELECT r FROM LmsReceiptPaymentDtl17557 r WHERE r.loanAccountNo = :loanAccountNo ORDER BY r.paymentDate DESC",
                LmsReceiptPaymentDtl17557.class);
        query.setParameter("loanAccountNo", loanAccountNo);
        return query.getResultList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<LmsReceiptPaymentDtl17557> findByPaymentMode(String paymentMode) {
        TypedQuery<LmsReceiptPaymentDtl17557> query = entityManager.createQuery(
                "SELECT r FROM LmsReceiptPaymentDtl17557 r WHERE r.paymentMode = :paymentMode ORDER BY r.paymentDate DESC",
                LmsReceiptPaymentDtl17557.class);
        query.setParameter("paymentMode", paymentMode);
        return query.getResultList();
    }

    @Override
    @Transactional(readOnly = true)
    public BigDecimal getTotalPaymentAmount(String loanAccountNo) {
        TypedQuery<BigDecimal> query = entityManager.createQuery(
                "SELECT COALESCE(SUM(r.paymentAmount), 0) FROM LmsReceiptPaymentDtl17557 r WHERE r.loanAccountNo = :loanAccountNo",
                BigDecimal.class);
        query.setParameter("loanAccountNo", loanAccountNo);
        return query.getSingleResult();
    }

    @Override
    public void deleteById(Long id) {
        LmsReceiptPaymentDtl17557 receipt = entityManager.find(LmsReceiptPaymentDtl17557.class, id);
        if (receipt != null) {
            entityManager.remove(receipt);
        }
    }
}
