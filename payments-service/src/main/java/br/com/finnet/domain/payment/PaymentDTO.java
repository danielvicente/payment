package br.com.finnet.domain.payment;

import lombok.*;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class PaymentDTO {

    private String paymentCode;

    @NotNull(message = "{payment.userId.not_blank}")
    private String userId;

    private String paymentDescription;

    @NotNull(message = "{payment.referenceNumber.not_blank}")
    private String referenceNumber;

    @NotNull(message = "{payment.paymentDate.not_blank}")
    private LocalDate paymentDate;

    @NotNull(message = "{payment.amount.not_blank}")
    private BigDecimal amount;

    private String status;

    public static PaymentDTO from(Payment payment) {
        PaymentDTO paymentDTO = new PaymentDTO();
        paymentDTO.setPaymentCode(payment.getPaymentCode());
        paymentDTO.setUserId(payment.getUserId());
        paymentDTO.setPaymentDescription(payment.getPaymentDescription());
        paymentDTO.setReferenceNumber(payment.getReferenceNumber());
        paymentDTO.setPaymentDate(payment.getPaymentDate());
        paymentDTO.setAmount(payment.getAmount());
        paymentDTO.setStatus(payment.getStatus());
        return paymentDTO;
    }
}
