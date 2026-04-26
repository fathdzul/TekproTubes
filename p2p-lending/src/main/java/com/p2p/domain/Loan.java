package com.p2p.domain;

import java.math.BigDecimal;

public class Loan {
    // Enum untuk status loan
    public enum Status {
        PENDING, APPROVED, REJECTED
    }

    private Status status;

    // Saat loan dibuat, status awal adalah PENDING
    public Loan() {
        this.status = Status.PENDING;
    }

    // Setter untuk mengubah status loan
    public void setStatus(Status status) {
        this.status = status;
    }

    // Getter untuk membaca status loan
    public Status getStatus() {
        return status;
    }

    // =========================
    // DOMAIN BEHAVIOR
    // =========================
    // Step 3 (domain)
    public void approve() {
        this.status = Status.APPROVED;
    }

    // step 5 (domain)
    public void reject() {
        this.status = Status.REJECTED;
    }

    // Validasi apakah amount pinjaman valid
    public static boolean isValidAmount(BigDecimal amount) {
        return amount.compareTo(BigDecimal.ZERO) > 0;
    }
}