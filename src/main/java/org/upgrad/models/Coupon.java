package org.upgrad.models;
import javax.persistence.*;
@Entity
@Table(name = "COUPON")
public class Coupon {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(name = "coupon_name")
    private String couponName;
    @Column(name = "percent",nullable = false)
    private int percent;
    public Coupon(){}
    public Coupon(String couponName, int percent) {
        this.couponName = couponName;
        this.percent = percent;
    }
    public Integer getId() {
        return id;
    }
    public void setId(Integer id) {
        this.id = id;
    }
    public String getCouponName() {
        return couponName;
    }
    public void setCouponName(String couponName) {
        this.couponName = couponName;
    }
    public int getPercent() {
        return percent;
    }
    public void setPercent(int percent) {
        this.percent = percent;
    }

}
