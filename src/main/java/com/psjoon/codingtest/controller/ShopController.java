package com.psjoon.codingtest.controller;

import com.siot.IamportRestClient.IamportClient;
import com.siot.IamportRestClient.exception.IamportResponseException;
import com.siot.IamportRestClient.response.IamportResponse;
import com.siot.IamportRestClient.response.Payment;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.IOException;

@Controller
@RequestMapping("shop")
public class ShopController {

    @Value("${iamport.api.key}")
    private String apiKey;

    @GetMapping("/api-key")
    public String getApiKey() {
        return apiKey;
    }

    private final IamportClient iamportClient;

    public ShopController() {
        this.iamportClient = new IamportClient("REST_API_KEY",
                "REST_API_SECRET");
    }

    @GetMapping("/product")
    public String product(Model model) {
        model.addAttribute("apiKey", apiKey);
        return "/shop/product";
    }

    @ResponseBody
    @RequestMapping("/verify/{imp_uid}")
    public IamportResponse<Payment> paymentByImpUid(@PathVariable("imp_uid") String imp_uid)
            throws IamportResponseException, IOException {
        // 결제 정보를 아임포트 API로 조회
        IamportResponse<Payment> response = iamportClient.paymentByImpUid(imp_uid);

        // 결제 정보를 DB에 저장
        Payment payment = response.getResponse();
//        savePaymentToDatabase(payment);

        return response;
    }

    // 결제 정보를 DB에 저장하는 메소드
//    private void savePaymentToDatabase(Payment payment) {
//        PaymentEntity paymentEntity = new PaymentEntity();
//        paymentEntity.setImpUid(payment.getImpUid());
//        paymentEntity.setMerchantUid(payment.getMerchantUid());
//        paymentEntity.setAmount(payment.getAmount());
//        paymentEntity.setStatus(payment.getStatus());
//        paymentEntity.setPayMethod(payment.getPayMethod());
//
//        paymentRepository.save(paymentEntity);
//    }
}
