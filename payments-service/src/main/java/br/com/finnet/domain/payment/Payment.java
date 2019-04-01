package br.com.finnet.domain.payment;

import lombok.*;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Getter
@Setter
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "TB_PAYMENT")
public class Payment {

    @Id
    @Column(name = "PAYMENT_CODE", nullable = false)
    private String paymentCode;

    @Column(name = "USER_ID", nullable = false)
    private String userId;

    @Column(name = "PAYMENT_DESCRIPTION")
    private String paymentDescription;

    @Column(name = "REFERENCE_NUMBER", nullable = false)
    private String referenceNumber;

    @Column(name = "PAYMENT_DATE", nullable = false)
    private LocalDate paymentDate;

    @Column(name = "AMOUNT", nullable = false)
    private BigDecimal amount;

    @Column(name = "STATUS", nullable = false)
    private String status;

    @Column(name = "CREATE_DATE", nullable = false)
    private LocalDate createDate;

    @Column(name = "UPDATE_DATE", nullable = false)
    private LocalDate updateDate;

    public static Payment from(PaymentDTO paymentDTO) {
        Payment payment = new Payment();
        payment.setPaymentCode(paymentDTO.getPaymentCode());
        payment.setUserId(paymentDTO.getUserId());
        payment.setPaymentDescription(paymentDTO.getPaymentDescription());
        payment.setReferenceNumber(paymentDTO.getReferenceNumber());
        payment.setPaymentDate(paymentDTO.getPaymentDate());
        payment.setAmount(paymentDTO.getAmount());
        return payment;
    }
}
