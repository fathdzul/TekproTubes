package com.p2p.service;

import com.p2p.domain.*;
import java.math.BigDecimal;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class LoanService {
    private static final Logger logger = LogManager.getLogger(LoanService.class);

    public Loan createLoan(Borrower borrower, BigDecimal amount) {
        logger.info("Memproses pengajuan pinjaman, amount: {}", amount);

        // =========================
        // VALIDASI (delegasi ke domain)
        // =========================
        validateBorrower(borrower);
        validateAmount(amount);

        // =========================
        // CREATE LOAN (domain object)
        // =========================
        Loan loan = new Loan();

        // =========================
        // BUSINESS ACTION (domain behavior)
        // =========================
        processCreditScoring(borrower, loan);

        return loan;
    }

    // =========================
    // PRIVATE VALIDATION METHOD
    // =========================
    private void validateBorrower(Borrower borrower) {
        if (!borrower.canApplyLoan()) {
            logger.error("Pinjaman DITOLAK - borrower belum terverifikasi");
            throw new IllegalArgumentException("Borrower not verified");
        }
        logger.debug("Borrower verified, lanjut proses...");
    }

    private void validateAmount(BigDecimal amount) {
        if (!Loan.isValidAmount(amount)) {
            logger.error("Pinjaman DITOLAK - amount tidak valid: {}", amount);
            throw new IllegalArgumentException("Amount harus lebih dari 0");
        }
        logger.debug("Amount valid: {}", amount);
    }

    private void processCreditScoring(Borrower borrower, Loan loan) {
        if (borrower.isEligible()) {
            loan.approve();
            logger.info("Pinjaman DISETUJUI - credit score: {}", borrower.getCreditScore());
        } else {
            loan.reject();
            logger.info("Pinjaman DITOLAK - credit score di bawah batas: {}", borrower.getCreditScore());
        }
    }
}
