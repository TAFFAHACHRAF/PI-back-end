package org.sid.paymentservice.services;

import org.sid.paymentservice.entity.Student;
import org.springframework.stereotype.Service;

@Service
public interface PaymentService {
    //Student getStudentByPayment(String codeStudent);
    public String getWalo(String token);
}