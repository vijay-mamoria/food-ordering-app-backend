package org.upgrad.services;
import org.springframework.stereotype.Service;
import org.upgrad.models.Payment;
import org.upgrad.repositories.PaymentRepository;
import javax.transaction.Transactional;
@Service
@Transactional
public class PaymentServiceImpl implements PaymentService{
    private final PaymentRepository paymentRepository;
    public PaymentServiceImpl(PaymentRepository paymentRepository) {
        this.paymentRepository = paymentRepository;
    }
    @Override
    public Iterable<Payment> getAllPaymentMethods(){
        return paymentRepository.getPaymentDetails();
    }
}