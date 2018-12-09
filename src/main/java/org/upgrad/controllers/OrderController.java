package org.upgrad.controllers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.upgrad.models.*;
import org.upgrad.requestResponseEntity.ItemQuantity;
import org.upgrad.services.*;
import java.util.Date;
import java.util.List;
@RestController
@RequestMapping("/order")
public class OrderController {
    @Autowired
    private OrderService orderService;
    @Autowired
    private UserAuthTokenService userAuthTokenService;
    @Autowired
    private AddressService addressService;
    @Autowired
    private ItemService itemService;
    @GetMapping("/coupon/{couponName}")
    @CrossOrigin
    ResponseEntity<?> getCouponByCouponName(@PathVariable String couponName, @RequestHeader String accessToken) {
        UserAuthToken usertoken = userAuthTokenService.isUserLoggedIn(accessToken);
        if (usertoken == null) {
            return new ResponseEntity<>("Please Login first to access this endpoint!", HttpStatus.UNAUTHORIZED);
        }
        else if (userAuthTokenService.isUserLoggedIn(accessToken).getLogoutAt() != null)  {
            return new ResponseEntity<>("You have already logged out. Please Login first to access this endpoint!", HttpStatus.UNAUTHORIZED);
        }
        else {
            Coupon coupon = orderService.getCoupon(couponName);
            if (coupon != null){
                return new ResponseEntity<>(coupon, HttpStatus.OK);
            }
            else {
                return new ResponseEntity<>("Invalid Coupon!", HttpStatus.NOT_FOUND);
            }
        }
    }
    @PostMapping("")
    @CrossOrigin
    public ResponseEntity<?> saveOrder(@RequestParam Integer addressId, @RequestParam(required = false) String flatBuilNo,
                                       @RequestParam(required = false) String locality, @RequestParam(required = false) String city, @RequestParam(required = false) String zipcode,
                                       @RequestParam(required = false) Integer stateId, @RequestParam(required = false) String type ,
                                       @RequestParam(required = false) Integer paymentId, @RequestBody List<ItemQuantity> itemQuantity, @RequestParam double bill,
                                       @RequestParam(required = false) Integer couponId, @RequestParam(defaultValue = "0") double discount, @RequestHeader String accessToken) {

        UserAuthToken usertoken = userAuthTokenService.isUserLoggedIn(accessToken);
        if (usertoken == null) {
            return new ResponseEntity<>("Please Login first to access this endpoint!", HttpStatus.UNAUTHORIZED);
        }
        else if (userAuthTokenService.isUserLoggedIn(accessToken).getLogoutAt() != null)  {
            return new ResponseEntity<>("You have already logged out. Please Login first to access this endpoint!", HttpStatus.UNAUTHORIZED);
        }
        else if (String.valueOf(flatBuilNo) != null && locality != null && city != null &&
                String.valueOf(zipcode) != null && stateId != null) {
            Integer userId = userAuthTokenService.getUserId(accessToken);
            if (zipcode.length() == 6 && zipcode.matches("[0-9]+")) {
                States state = addressService.isValidState(stateId);
                Integer addId = addressService.countAddress() + 1;
                String type1 = "temp";
                Address address = new Address(addId, flatBuilNo, locality, city, zipcode, state);
                addressService.addAddress(address);
                addressService.addUserAddress(type1, userId, addId);
                Date date = new Date();
                Integer orderId = orderService.findLatestOrderId();
                if (paymentId == null) {
                    paymentId = 1;
                }
                if (couponId == null) {
                    couponId = 1;
                }
                orderService.addOrderTempAddress(orderId+1,bill, couponId, discount, date,  paymentId,userId, addId);
                Integer orderItemId = orderService.findLatestOrderItemId();
                for (ItemQuantity quant : itemQuantity ){
                    orderItemId = orderItemId + 1;
                    Item item = itemService.getItemId(quant.getItemId());
                    Integer price = (item.getPrice()*quant.getQuantity());
                    //save orderItem
                    orderService.addOrderItem(orderItemId,orderId,quant.getItemId(),quant.getQuantity(),price);
                }
                orderId = orderService.findLatestOrderId();
                return new ResponseEntity<>(orderId, HttpStatus.OK);
            } else {
                return new ResponseEntity<>("Invalid zipcode!", HttpStatus.BAD_REQUEST);
            }
        }
        else if (addressService.getAddress(addressId)) {
            Date date = new Date();
            Integer userId = userAuthTokenService.getUserId(accessToken);
            Integer orderId = orderService.findLatestOrderId();
            if (paymentId == null) {
                paymentId = 1;
            }
            if (couponId == null) {
                couponId = 1;
            }
            orderService.addOrderWithPermAddress(orderId+1,bill, couponId, discount, date, paymentId,userId,addressId);
            Integer orderItemId = orderService.findLatestOrderItemId();
            for (ItemQuantity quant : itemQuantity ) {
                orderItemId = orderItemId + 1;
                Item item = itemService.getItemId(quant.getItemId());
                Integer price = (item.getPrice()* quant.getQuantity());
                orderService.addOrderItem(orderItemId,orderId,quant.getItemId(),quant.getQuantity(),price);
            }
            orderId = orderService.findLatestOrderId();

            return new ResponseEntity<>(orderId, HttpStatus.OK);
        }
        else {
            return new ResponseEntity<>("Order not saved", HttpStatus.BAD_REQUEST);
        }
    }
}
