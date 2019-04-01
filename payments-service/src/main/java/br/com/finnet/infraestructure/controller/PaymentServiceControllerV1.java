package br.com.finnet.infraestructure.controller;

import br.com.finnet.domain.payment.Payment;
import br.com.finnet.domain.payment.PaymentDTO;
import br.com.finnet.domain.payment.PaymentServiceFacade;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Slf4j
@RestController
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class PaymentServiceControllerV1 {

    PaymentServiceFacade paymentServiceFacade;

    @GetMapping("/payments")
    public ResponseEntity<List<PaymentDTO>> getPayments() {
        List<PaymentDTO> payments = this.paymentServiceFacade.findAllPayments();

        if (payments.isEmpty()) {
            ResponseEntity.status(HttpStatus.OK).body(Collections.EMPTY_LIST);
        }

        return ResponseEntity.status(HttpStatus.OK).body(payments);
    }

    @GetMapping("/payment/{paymentCode}")
    public ResponseEntity<PaymentDTO> getPaymentById(@PathVariable(value = "paymentCode") String paymentCode) {
        Optional<Payment> payment = this.paymentServiceFacade.findById(paymentCode);
        return payment.map(p -> ResponseEntity.status(HttpStatus.OK).body(PaymentDTO.from(p)))
                .orElseGet(ResponseEntity.status(HttpStatus.NOT_FOUND)::build);
    }

    @PostMapping("/payment")
    public ResponseEntity createPayment(@Valid @RequestBody PaymentDTO paymentDTO) {
        PaymentDTO responsepaymentDTO = this.paymentServiceFacade.insertPayment(paymentDTO);
        return new ResponseEntity<>(responsepaymentDTO, HttpStatus.CREATED);
    }

    @PutMapping("/payment/{paymentCode}")
    public ResponseEntity<PaymentDTO> updatePayment(@PathVariable(value = "paymentCode") String paymentCode,
                                                    @RequestBody PaymentDTO paymentDTO) {
        PaymentDTO responsepaymentDTO = this.paymentServiceFacade.updatePayment(paymentCode, paymentDTO);
        return new ResponseEntity<>(responsepaymentDTO, HttpStatus.OK);
    }

}
