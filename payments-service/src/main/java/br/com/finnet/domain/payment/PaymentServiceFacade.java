package br.com.finnet.domain.payment;

import br.com.finnet.api.ApplicationMessages;
import br.com.finnet.api.exception.EntityNotFoundException;
import br.com.finnet.api.exception.ResourceNotModifiedException;
import br.com.finnet.domain.payment.enums.StatusEnum;
import br.com.finnet.infraestructure.repository.PaymentRepository;
import com.google.common.hash.HashFunction;
import com.google.common.hash.Hashing;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class PaymentServiceFacade {

    @Autowired
    private PaymentRepository paymentRepository;

    @Autowired
    private ApplicationMessages messages;

    public Optional<Payment> findById(String paymentCode) {
        return this.paymentRepository.findById(paymentCode);
    }

    public PaymentDTO insertPayment(PaymentDTO requestPaymentDTO) {

        HashFunction hashFunction = Hashing.murmur3_32(4512589);
        if (requestPaymentDTO.getPaymentCode() == null) {
            String paymentCode = hashFunction.hashString(requestPaymentDTO.getReferenceNumber(), StandardCharsets.UTF_8).toString();
            requestPaymentDTO.setPaymentCode(paymentCode);
        }

        Payment payment = Payment.from(requestPaymentDTO);

        Optional<Payment> optionalPayment = this.paymentRepository.findById(payment.getPaymentCode());
        if (optionalPayment.isPresent()) {
            throw new ResourceNotModifiedException(messages.getExistingPayment());
        }

        payment.setStatus(StatusEnum.PENDING.name());

        Payment paymentPersisted = this.paymentRepository.saveAndFlush(payment);
        return PaymentDTO.from(this.paymentRepository.findById(paymentPersisted.getPaymentCode()).get());
    }

    public PaymentDTO updatePayment(String paymentCode, PaymentDTO requestPaymentDTO) {

        if (requestPaymentDTO.getPaymentCode() == null || requestPaymentDTO.getPaymentCode().equals(NumberUtils.LONG_ZERO)) {
            throw new EntityNotFoundException(messages.getNotFoundPayment());
        }

        Optional<Payment> payment = this.findById(paymentCode);

        if (!payment.isPresent()) {
            throw new EntityNotFoundException(messages.getNotFoundPayment());
        }

        Payment paymentToPersist = Payment.from(requestPaymentDTO);

        if (StringUtils.isNotEmpty(requestPaymentDTO.getStatus())) {
            String status = StatusEnum.valueOf(requestPaymentDTO.getStatus()).name();
            if (StringUtils.isEmpty(status)) {
                throw new EntityNotFoundException(messages.getNotFoundPayment());
            }
            paymentToPersist.setStatus(status);
        }

        return PaymentDTO.from(this.paymentRepository.saveAndFlush(paymentToPersist));
    }

    public List<PaymentDTO> findAllPayments() {
        return this.paymentRepository.findAll()
                .stream()
                .map(PaymentDTO::from)
                .collect(Collectors.toList());
    }
}
