package com.p2p.service;

import com.p2p.domain.*;
import java.math.BigDecimal;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class LoanService {
    private static final Logger logger = LogManager.getLogger(LoanService.class);

    public Loan createLoan(Borrower borrower, BigDecimal amount) {
        logger.info("Memproses pengajuan pinjaman, nominal: {}", amount);

        // =========================
        // VALIDASI (delegasi ke domain)
        // =========================
        validateBorrower(borrower);

        // =========================
        // CREATE LOAN (domain object)
        // =========================
        Loan loan = new Loan();

        // =========================
        // BUSINESS ACTION (domain behavior)
        // =========================
        if (borrower.getCreditScore() >= 600) {
            loan.approve();
            logger.info("Pinjaman DISETUJUI - credit score: {}", borrower.getCreditScore());
        } else {
            loan.reject();
            logger.info("Pinjaman DITOLAK - credit score di bawah batas: {}", borrower.getCreditScore());
        }

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
}
