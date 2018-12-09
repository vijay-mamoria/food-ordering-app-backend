package org.upgrad.controllers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.upgrad.models.Address;
import org.upgrad.models.States;
import org.upgrad.models.UserAuthToken;
import org.upgrad.services.AddressService;
import org.upgrad.services.UserAuthTokenService;
import java.util.List;
@RestController
public class AddressController {
    @Autowired
    private AddressService addressService;
    @Autowired
    private UserAuthTokenService userAuthTokenService;
    @PostMapping("/address")
    @CrossOrigin
    public ResponseEntity<?> address(@RequestParam String flatBuilNo, @RequestParam String locality, @RequestParam String city, @RequestParam Integer stateId, @RequestParam String zipcode, @RequestParam(required = false) String type , @RequestHeader String accessToken) {
        String message = "" ;
        HttpStatus httpStatus = HttpStatus.OK ;
        UserAuthToken usertoken = userAuthTokenService.isUserLoggedIn(accessToken);
        if (null == usertoken) {
            message = "Please Login first to access this endpoint!" ;
            httpStatus =  HttpStatus.UNAUTHORIZED ;
        }
        else if (null != userAuthTokenService.isUserLoggedIn(accessToken).getLogoutAt()) {
            message = "You have already logged out. Please Login first to access this endpoint!" ;
            httpStatus =  HttpStatus.UNAUTHORIZED ;
        } else {
            Integer userId = userAuthTokenService.getUserId(accessToken);
            if (6 == zipcode.length() && zipcode.matches("[0-9]+")) {
                States state =   addressService.isValidState(stateId) ;
                int addressId  = addressService.countAddress() + 1 ;
                String addType = "temp" ;
                Address address = new Address(addressId ,flatBuilNo, locality, city, zipcode, state);
                addressService.addAddress(address);
                if ( null == type )
                    addType = type ;
                addressService.addUserAddress(type, userId, addressId) ;
                message ="Address has been saved successfully!" ;
                httpStatus = HttpStatus.CREATED ;
            } else {
                message = "Invalid zipcode!" ;
                httpStatus = HttpStatus.BAD_REQUEST ;
            }
        }
        return new ResponseEntity<>(message , httpStatus);
    }
    @PutMapping("/address/{addressId}")
    @CrossOrigin
    public ResponseEntity<?> updateAddressById(@PathVariable Integer addressId , @RequestParam(required = false) String flatBuilNo , @RequestParam(required = false) String locality , @RequestParam(required = false) String city , @RequestParam(required = false) String zipcode , @RequestParam(required = false) Integer stateId , @RequestHeader String accessToken) {
        String message = "" ;
        HttpStatus httpStatus = HttpStatus.OK ;
        UserAuthToken usertoken = userAuthTokenService.isUserLoggedIn(accessToken);
        if (null == usertoken) {
            message = "Please Login first to access this endpoint!" ;
            httpStatus =  HttpStatus.UNAUTHORIZED ;
        }
        else if (null != userAuthTokenService.isUserLoggedIn(accessToken).getLogoutAt() ) {
            message = "You have already logged out. Please Login first to access this endpoint!" ;
            httpStatus =  HttpStatus.UNAUTHORIZED ;
        } else {
            Integer userId = userAuthTokenService.getUserId(accessToken);
            if (zipcode != null ){
                if (! ( zipcode.length() == 6 && zipcode.matches("[0-9]+") )) {
                    return new ResponseEntity<>("Invalid zipcode!" , HttpStatus.BAD_REQUEST);
                }
            }
            Boolean  addStatus = addressService.getAddress(addressId);
            Address add = addressService.getaddressById(addressId);
            if (false == addStatus) {
                message = "No address with this address id!";
                httpStatus = HttpStatus.BAD_REQUEST;
            } else {
                if (null == flatBuilNo)
                    flatBuilNo = add.getFlatNumber();
                if (null == locality)
                    locality = add.getLocality();
                if (null == city)
                    city = add.getCity();
                if (null == zipcode)
                    zipcode = add.getZipcode();
                if (null == stateId)
                    stateId = add.getState().getId() ;
                addressService.updateAddressById(flatBuilNo, locality, city, zipcode, stateId, addressId);
                message = "Address has been updated successfully!";
                httpStatus = HttpStatus.OK ;
            }
        }
        return new ResponseEntity<>(message , httpStatus);
    }
    @DeleteMapping("/address/{addressId}")
    @CrossOrigin
    public ResponseEntity<?> deleteAddressById(@PathVariable Integer addressId , @RequestHeader String accessToken) {
        String message = "";
        HttpStatus httpStatus = HttpStatus.OK;
        UserAuthToken usertoken = userAuthTokenService.isUserLoggedIn(accessToken);
        if (null == usertoken) {
            message = "Please Login first to access this endpoint!";
            httpStatus = HttpStatus.UNAUTHORIZED;
        }
        else if (null != userAuthTokenService.isUserLoggedIn(accessToken).getLogoutAt() ) {
            message = "You have already logged out. Please Login first to access this endpoint!";
            httpStatus = HttpStatus.UNAUTHORIZED;
        } else {
            Boolean add = addressService.getAddress(addressId);
            if (false == add ) {
                message = "No address with this address id!";
                httpStatus = HttpStatus.BAD_REQUEST;
            } else {
                addressService.deleteAddressById(addressId) ;
                addressService.deleteUserAddressById(addressId);
                message = "Address has been deleted successfully!" ;
                httpStatus = HttpStatus.OK ;
            }
        }
        return new ResponseEntity<>(message , httpStatus);
    }
    @GetMapping("/address/user")
    @CrossOrigin
    public ResponseEntity<?> getAllPermanentAddress(@RequestHeader String accessToken) {
        String message = "" ;
        HttpStatus httpStatus = HttpStatus.OK ;
        UserAuthToken usertoken = userAuthTokenService.isUserLoggedIn(accessToken);
        if (null == usertoken) {
            message = "Please Login first to access this endpoint!" ;
            httpStatus =  HttpStatus.UNAUTHORIZED ;
        }
        else if (null != userAuthTokenService.isUserLoggedIn(accessToken).getLogoutAt() ) {
            message = "You have already logged out. Please Login first to access this endpoint!" ;
            httpStatus =  HttpStatus.UNAUTHORIZED ;
        } else {
            Integer userId = userAuthTokenService.getUserId(accessToken);
            List<Address> userList = (List<Address>) addressService.getPermAddress(userId);
            if ( null == userList || userList.isEmpty())
            {
                message = "No permanent address found!" ;
                httpStatus = HttpStatus.BAD_REQUEST ;
                return new ResponseEntity<>(message , httpStatus);
            }
            else
            {
                return new ResponseEntity<>(addressService.getPermAddress(userId), HttpStatus.OK);
            }
        }
        return new ResponseEntity<>(message , httpStatus);
    }
    @GetMapping("/states")
    @CrossOrigin
    public ResponseEntity<?> getAllStates() {
        return new ResponseEntity<>( addressService.getAllStates() , HttpStatus.OK);
    }

}