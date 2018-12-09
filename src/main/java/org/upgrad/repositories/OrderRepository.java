package org.upgrad.repositories;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import org.upgrad.models.Order;
import org.upgrad.models.OrderItem;

import java.util.Date;
import java.util.List;
@Repository
public interface OrderRepository extends CrudRepository<Order, Integer> {
    @Query(nativeQuery = true,value = "SELECT * FROM ORDERS WHERE USER_ID=?1 ORDER BY DATE DESC")
    Iterable<Order> getOrderByUserId(Integer id);
    @Query(nativeQuery = true,value = "SELECT * FROM ORDERS WHERE USER_ID=?1 ORDER BY DATE DESC")
    List<Order> getOrderByUser(Integer id);
    @Transactional
    @Modifying
    @Query(nativeQuery = true,value="INSERT INTO ORDERS (id,bill,coupon_id,discount,date,payment_id,user_id,address_id) VALUES (?1,?2,?3,?4,?5,?6,?7,?8)")
    void addOrder(Integer orderId,double bill, Integer couponId, double discount, Date date, Integer paymentId,Integer userId, Integer addressId);
    @Query(nativeQuery = true,value = "SELECT max(ID) FROM ORDERS")
    Integer findLatestOrderId();
    @Query(nativeQuery = true,value = "SELECT a.* FROM ORDER_ITEM a,ORDERS b WHERE b.ID=a.ORDER_ID and ORDER_ID=?1 ")
    Iterable<OrderItem> getOrderItemsByOrderId(Integer id);
    @Transactional
    @Modifying
    @Query(nativeQuery = true,value="INSERT INTO ORDER_ITEM (id,order_id,item_id,quantity,price) VALUES (?1,?2,?3,?4,?5)")
    void addOrderItem(Integer id,Integer orderId,Integer itemId,Integer quantity,Integer price);
    @Query(nativeQuery = true,value = "SELECT max(ID) FROM ORDER_ITEM")
    Integer findLatestOrderItemId();
}
