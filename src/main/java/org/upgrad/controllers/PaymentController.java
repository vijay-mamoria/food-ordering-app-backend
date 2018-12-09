package org.upgrad.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.upgrad.models.UserAuthToken;
import org.upgrad.services.PaymentService;
import org.upgrad.services.UserAuthTokenService;
@RestController
public class PaymentController {
    @Autowired
    private PaymentService paymentService;
    @Autowired
    private UserAuthTokenService userAuthTokenService;
    @GetMapping("/payment")
    @CrossOrigin
    ResponseEntity<?> allPaymentMethods(@RequestHeader String accessToken) {
        UserAuthToken usertoken = userAuthTokenService.isUserLoggedIn(accessToken);
        if (usertoken == null) {
            return new ResponseEntity<>("Please Login first to access this endpoint!", HttpStatus.UNAUTHORIZED);
        }
        else if (userAuthTokenService.isUserLoggedIn(accessToken).getLogoutAt() != null)  {
            return new ResponseEntity<>("You have already logged out. Please Login first to access this endpoint!", HttpStatus.UNAUTHORIZED);
        } else {
            return new ResponseEntity<>(paymentService.getAllPaymentMethods(), HttpStatus.OK);
        }
    }
}
