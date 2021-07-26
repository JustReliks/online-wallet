package ru.onlinewallet.entity.account;

import lombok.Data;

import java.time.Instant;

@Data
public class CreditInfo {
    private double creditAmount;
    private Instant maturityDate;
    private double monthlyPayment;
    private double currentCreditBalance;
}