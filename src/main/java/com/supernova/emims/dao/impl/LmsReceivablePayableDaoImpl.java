package com.supernova.emims.dao.impl;

import com.supernova.emims.dao.LmsReceivablePayableDao;
import com.supernova.emims.entity.LmsReceivablePayableDtl17557;
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
 * Hibernate DAO implementation for LMS_RECEIVABLEPAYBLE_DTL_17557 operations
 * Uses Hibernate ORM for database operations
 *
 * Sonar-compliant: Proper exception handling and resource management
 */
@Repository
@Transactional
public class LmsReceivablePayableDaoImpl implements LmsReceivablePayableDao {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public LmsReceivablePayableDtl17557 save(LmsReceivablePayableDtl17557 receivable) {
        if (receivable.getReceivableId() == null) {
            entityManager.persist(receivable);
            return receivable;
        } else {
            return entityManager.merge(receivable);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<LmsReceivablePayableDtl17557> findById(Long id) {
        try {
            LmsReceivablePayableDtl17557 receivable = entityManager.find(LmsReceivablePayableDtl17557.class, id);
            return Optional.ofNullable(receivable);
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    @Override
    @Transactional(readOnly = true)
    public List<LmsReceivablePayableDtl17557> findByLoanAccountNo(String loanAccountNo) {
        TypedQuery<LmsReceivablePayableDtl17557> query = entityManager.createQuery(
                "SELECT r FROM LmsReceivablePayableDtl17557 r WHERE r.loanAccountNo = :loanAccountNo ORDER BY r.createdDate DESC",
                LmsReceivablePayableDtl17557.class);
        query.setParameter("loanAccountNo", loanAccountNo);
        return query.getResultList();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<LmsReceivablePayableDtl17557> findLatestByLoanAccountNo(String loanAccountNo) {
        try {
            TypedQuery<LmsReceivablePayableDtl17557> query = entityManager.createQuery(
                    "SELECT r FROM LmsReceivablePayableDtl17557 r WHERE r.loanAccountNo = :loanAccountNo ORDER BY r.createdDate DESC",
                    LmsReceivablePayableDtl17557.class);
            query.setParameter("loanAccountNo", loanAccountNo);
            query.setMaxResults(1);
            return Optional.of(query.getSingleResult());
        } catch (NoResultException e) {
            return Optional.empty();
        }
    }

    @Override
    @Transactional(readOnly = true)
    public boolean existsByLoanAccountNo(String loanAccountNo) {
        TypedQuery<Long> query = entityManager.createQuery(
                "SELECT COUNT(r) FROM LmsReceivablePayableDtl17557 r WHERE r.loanAccountNo = :loanAccountNo",
                Long.class);
        query.setParameter("loanAccountNo", loanAccountNo);
        return query.getSingleResult() > 0;
    }

    @Override
    public void deleteByLoanAccountNo(String loanAccountNo) {
        TypedQuery<LmsReceivablePayableDtl17557> query = entityManager.createQuery(
                "SELECT r FROM LmsReceivablePayableDtl17557 r WHERE r.loanAccountNo = :loanAccountNo",
                LmsReceivablePayableDtl17557.class);
        query.setParameter("loanAccountNo", loanAccountNo);

        List<LmsReceivablePayableDtl17557> receivables = query.getResultList();
        for (LmsReceivablePayableDtl17557 receivable : receivables) {
            entityManager.remove(receivable);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public BigDecimal getTotalPendingAmount(String loanAccountNo) {
        TypedQuery<BigDecimal> query = entityManager.createQuery(
                "SELECT COALESCE(SUM(r.totalAmount), 0) FROM LmsReceivablePayableDtl17557 r WHERE r.loanAccountNo = :loanAccountNo",
                BigDecimal.class);
        query.setParameter("loanAccountNo", loanAccountNo);
        return query.getSingleResult();
    }
}
