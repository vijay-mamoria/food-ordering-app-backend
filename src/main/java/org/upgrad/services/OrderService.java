package org.upgrad.services;
import org.upgrad.models.Coupon;
import org.upgrad.models.Order;
import org.upgrad.models.OrderItem;
import java.util.Date;
import java.util.List;
public interface OrderService {
    Coupon getCoupon(String couponName);
    Iterable<OrderItem> getOrderItemsByOrderId(Integer id);
    List<Order> getOrdersByUser(Integer id);
    void addOrderTempAddress(Integer orderId,double bill, Integer couponId, double discount, Date date, Integer paymentId,Integer userId, Integer addressId);
    void addOrderWithPermAddress(Integer orderId,double bill, Integer couponId, double discount, Date date, Integer paymentId,Integer userId, Integer addressId);
    void addOrderItem(Integer id, Integer orderId,Integer itemId,Integer quantity,Integer price);
    Integer findLatestOrderId();
    Integer findLatestOrderItemId();
}
