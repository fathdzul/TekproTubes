package com.p2p;

import org.junit.jupiter.api.Test;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.p2p.domain.Borrower;
import com.p2p.domain.Loan;
import com.p2p.service.LoanService;

import static org.junit.jupiter.api.Assertions.*;
import java.math.BigDecimal;

public class LoanServiceTest {
    private static final Logger logger = LogManager.getLogger(LoanServiceTest.class);

    @Test
    void shouldRejectLoanWhenBorrowerNotVerified() {
        logger.info("TC-01: shouldRejectLoanWhenBorrowerNotVerified");

        // =====================================================
        // SCENARIO:
        // Borrower tidak terverifikasi (KYC = false)
        // Ketika borrower mengajukan pinjaman
        // Maka sistem harus menolak dengan melempar exception
        // =====================================================

        // =========================
        // Arrange (Initial Condition)
        // =========================
        logger.debug("Arrange: membuat borrower belum terverifikasi");
        // Borrower belum lolos proses KYC
        Borrower borrower = new Borrower(false, 700);

        // Service untuk pengajuan loan
        LoanService loanService = new LoanService();

        // Jumlah pinjaman valid
        BigDecimal amount = BigDecimal.valueOf(1000);

        // =========================
        // ACTION + ASSERT (Act & Assert)
        // =========================
        // Ketika borrower mengajukan loan,
        // sistem harus MENOLAK dengan melempar exception
        logger.debug("Act: borrower mencoba mengajukan pinjaman sebesar {}", amount);
        assertThrows(IllegalArgumentException.class, () -> {
            loanService.createLoan(borrower, amount);
        });

        logger.info("TC-01 Berhasil - exception berhasil dilempar seperti yang diharapkan");
    }

    @Test
    void shouldRejectLoanWhenAmountIsZeroOrNegative() {
        logger.info("TC-02: shouldRejectLoanWhenAmountIsZeroOrNegative");

        // =====================================================
        // SCENARIO:
        // Borrower sudah terverifikasi (KYC = true)
        // Ketika borrower mengajukan pinjaman dengan amount <= 0
        // Maka sistem harus menolak dengan melempar exception
        // =====================================================

        // =========================
        // Arrange (Initial Condition)
        // =========================
        logger.debug("Arrange: membuat borrower valid dengan amount nol");
        // Borrower sudah lolos KYC
        Borrower borrower = new Borrower(true, 700);

        LoanService loanService = new LoanService();

        // Amount tidak valid (nol)
        BigDecimal amount = BigDecimal.ZERO;

        // =========================
        // ACTION + ASSERT (Act & Assert)
        // =========================
        logger.debug("Act: borrower mencoba mengajukan pinjaman sebesar {}", amount);
        assertThrows(IllegalArgumentException.class, () -> {
            loanService.createLoan(borrower, amount);
        });

        logger.info("TC-02 Berhasil - exception berhasil dilempar untuk amount tidak valid");
    }

    @Test
    void shouldApproveLoanWhenCreditScoreHigh() {
        logger.info("TC-03: shouldApproveLoanWhenCreditScoreHigh");

        // =====================================================
        // SCENARIO:
        // Borrower sudah terverifikasi (KYC = true)
        // Credit score >= 600 (di atas threshold)
        // Ketika borrower mengajukan pinjaman
        // Maka sistem harus menyetujui loan
        // =====================================================

        // =========================
        // Arrange (Initial Condition)
        // =========================
        logger.debug("Arrange: membuat borrower verified dengan credit score tinggi");
        // Borrower sudah KYC, credit score di atas threshold
        Borrower borrower = new Borrower(true, 700);

        LoanService loanService = new LoanService();

        BigDecimal amount = BigDecimal.valueOf(1000);

        // =========================
        // Act (Action)
        // =========================
        logger.debug("Act: borrower mengajukan pinjaman sebesar {}", amount);
        Loan loan = loanService.createLoan(borrower, amount);

        // =========================
        // Assert (Expected Result)
        // =========================
        assertEquals(Loan.Status.APPROVED, loan.getStatus());
        logger.info("TC-03 Berhasil - loan berstatus APPROVED sesuai yang diharapkan");
    }
}
