package org.upgrad.models;
import javax.persistence.*;
@Entity
@Table(name = "payment")
public class Payment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(name = "payment_name")
    private String paymentname;
    public Payment(){}
    public Payment(String paymentname) {
        this.paymentname = paymentname;
    }
    public Integer getId() {
        return id;
    }
    public void setId(Integer id) {
        this.id = id;
    }
    public String getPaymentname() {
        return paymentname;
    }
    public void setPaymentname(String paymentname) {
        this.paymentname = paymentname;
    }
}

