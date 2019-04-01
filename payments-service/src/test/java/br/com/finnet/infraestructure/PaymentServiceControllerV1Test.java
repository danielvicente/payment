package br.com.finnet.infraestructure;

import br.com.finnet.domain.payment.PaymentDTO;
import br.com.finnet.domain.payment.enums.StatusEnum;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Slf4j
@RunWith(SpringRunner.class)
@AutoConfigureMockMvc
@SpringBootTest
@ActiveProfiles("integration_test")
@Transactional
@Sql(scripts = {"classpath:script_sql/insert-payments.sql"})
public class PaymentServiceControllerV1Test {

    @Autowired
    private MockMvc mvc;

    @Test
    public void shouldReturnOnePayment() throws Exception {

        mvc.perform(get("/payment/ab12cd")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("paymentCode").value("ab12cd"))
                .andExpect(jsonPath("referenceNumber").value("REFERENCE_NUMBER 1"));
    }

    @Test
    public void shouldCreatePayment() throws Exception {

        PaymentDTO entity = new PaymentDTO();
        entity.setUserId(UUID.randomUUID().toString());
        entity.setAmount(new BigDecimal(20.00));
        entity.setPaymentDate(LocalDate.now());
        entity.setReferenceNumber("12345678910");
        entity.setUserId(UUID.randomUUID().toString());

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        String entityJson = objectMapper.writeValueAsString(entity);

        mvc.perform(post("/payment")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(entityJson))
                .andExpect(status().is(HttpStatus.CREATED.value()))
                .andExpect(result -> {
                    String json = result.getResponse().getContentAsString();
                    PaymentDTO paymentDTO = objectMapper.readValue(json, PaymentDTO.class);
                    entity.setStatus(StatusEnum.PENDING.name());
                    assertThat(paymentDTO).isEqualToIgnoringGivenFields(entity, "paymentCode");
                });

    }

    @Test
    public void shouldReturnBadRequestCreatePayment() throws Exception {

        PaymentDTO entity = new PaymentDTO();
        String entityJson = new ObjectMapper().writeValueAsString(entity);

        mvc.perform(post("/payment")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(entityJson))
                .andExpect(status().is(HttpStatus.BAD_REQUEST.value()));

    }

    @Test
    public void shouldUpdatePayment() throws Exception {

        PaymentDTO entity = new PaymentDTO();
        entity.setUserId(UUID.randomUUID().toString());
        entity.setPaymentCode("ab14cd");
        entity.setPaymentDescription("Change payment date");
        entity.setPaymentDate(LocalDate.now().plusMonths(1));
        entity.setAmount(new BigDecimal(30.00));
        entity.setReferenceNumber("REFERENCE_NUMBER 3");
        entity.setStatus(StatusEnum.CANCELED.name());

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        String entityJson = objectMapper.writeValueAsString(entity);

        mvc.perform(put("/payment/ab14cd")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(entityJson))
                .andExpect(status().is(HttpStatus.OK.value()))
                .andExpect(result -> {
                    String json = result.getResponse().getContentAsString();
                    PaymentDTO paymentDTO = objectMapper.readValue(json, PaymentDTO.class);
                    assertThat(paymentDTO).isEqualToIgnoringGivenFields(entity, "paymentCode");
                });

    }

    @Test
    public void shouldNotFoundUpdatePayment() throws Exception {

        PaymentDTO entity = new PaymentDTO();
        entity.setPaymentCode("99");
        String entityJson = new ObjectMapper().writeValueAsString(entity);

        mvc.perform(put("/payment/99")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(entityJson))
                .andExpect(status().is(HttpStatus.NOT_FOUND.value()));

    }
}
