package com.psjoon.codingtest.controller;

import com.psjoon.codingtest.entity.Member;
import com.psjoon.codingtest.entity.OrderProduct;
import com.psjoon.codingtest.repository.MemberRepository;
import com.psjoon.codingtest.repository.OrderProductRepository;
import com.siot.IamportRestClient.IamportClient;
import com.siot.IamportRestClient.exception.IamportResponseException;
import com.siot.IamportRestClient.response.IamportResponse;
import com.siot.IamportRestClient.response.Payment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.IOException;

@Controller
public class ShopController {
    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private OrderProductRepository orderProductRepository;

    @Value("${iamport.api.key}")
    private String apiKey;


    @GetMapping("/api-key")
    public String getApiKey() {
        return apiKey;
    }

    private final IamportClient iamportClient;

    // 생성자에서 @Value를 사용하여 직접 주입
    public ShopController(@Value("${iamport.rest.api.key}") String REST_API_KEY,
                          @Value("${iamport.rest.api.secret}") String REST_API_SECRET) {
        this.iamportClient = new IamportClient(REST_API_KEY, REST_API_SECRET);
    }



    @GetMapping("/product")
    public String product(Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String userId = auth.getName();
        Member member = memberRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("해당 ID의 사용자가 없습니다."));
        model.addAttribute("member", member);
        model.addAttribute("userId", userId);
        model.addAttribute("apiKey", apiKey);
        return "/shop/product";
    }

    @ResponseBody
    @RequestMapping("/verify/{imp_uid}")
    public ResponseEntity<?> paymentByImpUid(@PathVariable("imp_uid") String imp_uid) {
        try {
            // 아임포트 API로 결제 정보 조회
            IamportResponse<Payment> response = iamportClient.paymentByImpUid(imp_uid);
            Payment payment = response.getResponse();

            if (payment == null) {
                // 결제 정보 조회 실패 시
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("결제 정보를 찾을 수 없습니다.");
            }

            // 결제 정보 DB 저장
            savePaymentToDatabase(payment);

            // 정상적으로 처리된 경우 응답 반환
            return ResponseEntity.ok(payment);

        } catch (IamportResponseException e) {
            // 아임포트 API 오류 처리
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("아임포트 API 요청 실패: " + e.getMessage());
        } catch (IOException e) {
            // 입출력 예외 처리
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("서버 오류 발생: " + e.getMessage());
        } catch (Exception e) {
            // 기타 예외 처리
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("알 수 없는 오류 발생: " + e.getMessage());
        }
    }

    private void savePaymentToDatabase(Payment payment) {
        try {
            OrderProduct orderProduct = new OrderProduct();
            orderProduct.setImpUid(payment.getImpUid());
            orderProduct.setMerchantUid(payment.getMerchantUid());
            orderProduct.setAmount(payment.getAmount());
            orderProduct.setPayMethod(payment.getPayMethod());
            orderProduct.setStatus(payment.getStatus());  // 상태 저장

            // 현재 로그인된 사용자 정보 가져오기
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            String userId = auth.getName();

            // Member 엔티티와 결제 정보 연결
            Member member = memberRepository.findById(userId)
                    .orElseThrow(() -> new RuntimeException("사용자 정보를 찾을 수 없습니다."));
            orderProduct.setMember(member);

            // 결제 정보 저장
            orderProductRepository.save(orderProduct);

        } catch (Exception e) {
            // DB 저장 실패 시 예외 처리
            throw new RuntimeException("결제 정보 저장 중 오류 발생: " + e.getMessage());
        }
    }

    // 결제 정보를 DB에 저장하는 메소드
//    private void savePaymentToDatabase(Payment payment) {
//        Payment paymentEntity = new Payment();
//        paymentEntity.setImpUid(payment.getImpUid());
//        paymentEntity.setMerchantUid(payment.getMerchantUid());
//        paymentEntity.setAmount(payment.getAmount());
//        paymentEntity.setStatus(payment.getStatus());
//        paymentEntity.setPayMethod(payment.getPayMethod());
//    Authentication auth = SecurityContextHolder.getContext().getAuthentication();
//    String userId = auth.getName();
//    paymentEntity.setId(userId);
//        paymentRepository.save(paymentEntity);
//    }
}
