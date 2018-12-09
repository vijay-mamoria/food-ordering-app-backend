
package org.upgrad.services;
import org.springframework.stereotype.Service;
import org.upgrad.models.Coupon;
import org.upgrad.models.Order;
import org.upgrad.models.OrderItem;
import org.upgrad.repositories.CouponRepository;
import org.upgrad.repositories.OrderRepository;
import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
@Service
@Transactional
public class OrderServiceImpl implements OrderService{
    private final CouponRepository couponRepository;
    private final OrderRepository orderRepository;
    public OrderServiceImpl(CouponRepository couponRepository,OrderRepository orderRepository) {
        this.couponRepository = couponRepository;
        this.orderRepository = orderRepository;
    }
    @Override
    public Coupon getCoupon(String couponName){
        return couponRepository.getCouponByName(couponName);
    }
    @Override
    public Iterable<OrderItem> getOrderItemsByOrderId(Integer id){ return orderRepository.getOrderItemsByOrderId(id); }
    @Override
    public void addOrderTempAddress(Integer orderId,double bill, Integer couponId, double discount, Date date, Integer paymentId, Integer userId, Integer addressId) {
        orderRepository.addOrder(orderId,bill,couponId,discount,date,paymentId,userId,addressId);

    }
    @Override
    public void addOrderWithPermAddress(Integer orderId,double bill, Integer couponId, double discount, Date date, Integer paymentId, Integer userId, Integer addressId) {
        orderRepository.addOrder(orderId,bill,couponId,discount,date,paymentId,userId,addressId);

    }
    @Override
    public Integer findLatestOrderId(){
        return orderRepository.findLatestOrderId();
    }
    @Override
    public Integer findLatestOrderItemId(){
        return orderRepository.findLatestOrderItemId();
    }
    @Override
    public void addOrderItem(Integer id, Integer orderId,Integer itemId,Integer quantity,Integer price) {

        orderRepository.addOrderItem(id,orderId,itemId,quantity,price);
    }
    @Override
    public List<Order> getOrdersByUser(Integer id){
        return orderRepository.getOrderByUser(id);
    }
}
