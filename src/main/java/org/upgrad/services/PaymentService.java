package org.upgrad.services;
import org.upgrad.models.Payment;
public interface PaymentService {
    Iterable<Payment> getAllPaymentMethods();
}