package com.example.controller;

import com.example.model.entity.Payment;
import com.example.service.PaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.Optional;

@Controller
@RequiredArgsConstructor
public class PaymentController {

    private final PaymentService paymentService;

    @GetMapping("/payment/{uuid}")
    public String paymentPage(@PathVariable String uuid, Model model) {
        Optional<Payment> paymentOptional = this.paymentService.getById(uuid);
        if (paymentOptional.isPresent()) {
            model.addAttribute("uuid", uuid);
            return "checkout";
        } else {
            model.addAttribute("errorMessage", "Order with id: " + uuid + " not found");
            return "payment-error";
        }
    }

    @GetMapping("/payment/{uuid}/success")
    public String paymentSuccessPage(@PathVariable String uuid, Model model) {
        model.addAttribute("transactionId", uuid);
        return "success";
    }
}
