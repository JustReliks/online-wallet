package ru.onlinewallet.dto.account;

import lombok.Data;
import ru.onlinewallet.entity.account.CreditInfo;

import java.time.Instant;

@Data
public class CreditInfoDto {
    private double creditAmount;
    private Instant maturityDate;
    private double monthlyPayment;
    private double currentCreditBalance;

    public static CreditInfoDto toDto(CreditInfo creditInfo) {
        CreditInfoDto creditInfoDto = new CreditInfoDto();
        creditInfoDto.setCreditAmount(creditInfo.getCreditAmount());
        creditInfoDto.setMaturityDate(creditInfo.getMaturityDate());
        creditInfoDto.setMonthlyPayment(creditInfo.getMonthlyPayment());
        creditInfoDto.setCurrentCreditBalance(creditInfo.getCurrentCreditBalance());

        return creditInfoDto;
    }
}
