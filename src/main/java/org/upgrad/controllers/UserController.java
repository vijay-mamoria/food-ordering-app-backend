package org.upgrad.controllers;

import com.google.common.base.Charsets;
import com.google.common.hash.Hashing;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.upgrad.common.Util;
import org.upgrad.models.User;
import org.upgrad.models.UserAuthToken;
import org.upgrad.services.UserAuthTokenService;
import org.upgrad.services.UserService;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private UserAuthTokenService userAuthTokenService;

    /*
    * This endpoint is used to login a user.
    * Here contact number and password has to be provided to match the credentials.
    */
    @PostMapping("/login")
    @CrossOrigin
    public ResponseEntity<?> login(@RequestParam String contactNumber, @RequestParam String password){
        String passwordByUser = String.valueOf(userService.findUserPassword(contactNumber));
        String sha256hex = Hashing.sha256()
                .hashString(password, Charsets.US_ASCII)
                .toString();
        if(userService.findUserPassword(contactNumber)==null) return new ResponseEntity<>("This contact number has not been registered!",HttpStatus.OK);
        else if (!(passwordByUser.equalsIgnoreCase(sha256hex))) {
            return new ResponseEntity<>("Invalid Credentials",HttpStatus.UNAUTHORIZED);
        }
        else{
            User user = userService.findUser(contactNumber);
            String accessToken = UUID.randomUUID().toString();
            userAuthTokenService.addAccessToken(user.getId(),accessToken);
            HttpHeaders headers = new HttpHeaders();
            headers.add("access-token", accessToken);
            List<String> header = new ArrayList<>();
            header.add("access-token");
            headers.setAccessControlExposeHeaders(header);
            return new ResponseEntity<>(user,headers,HttpStatus.OK);
        }
    }

    /*
    * This endpoint is used to logout a user.
    * Authentication is required to access this endpoint, so accessToken is taken as request header to make sure user is authenticated.
    */
    @PutMapping("/logout")
    @CrossOrigin
    public ResponseEntity<String> logout(@RequestHeader String accessToken){
        if(userAuthTokenService.isUserLoggedIn(accessToken) == null){
            return new ResponseEntity<>("Please Login first to access this endpoint!", HttpStatus.UNAUTHORIZED);
        }
        else if(userAuthTokenService.isUserLoggedIn(accessToken).getLogoutAt()!=null){
            return new ResponseEntity<>("You have already logged out. Please Login first to access this endpoint!", HttpStatus.UNAUTHORIZED);
        }  else{
            userAuthTokenService.removeAccessToken(accessToken);
            return new ResponseEntity<>("You have logged out successfully!",HttpStatus.OK);}
    }

    /*
     * This endpoint is used to sign up a new user.
     * Here contact number should be unique.
     */
    @PostMapping("/signup")
    @CrossOrigin
    public ResponseEntity<String> signup( @RequestParam("firstName") String firstName, @RequestParam(required = false,name ="lastName") String lastName, @RequestParam("email") String email,  @RequestParam("contactNumber") String phoneNum, @RequestParam("password") String password) {
        if(!Util.validateEmailFormat(email)){
            return new ResponseEntity("Invalid email-id format!", HttpStatus.BAD_REQUEST);
        }

        if(!Util.validateContactNumber(phoneNum)){
            return new ResponseEntity("Invalid contact number!", HttpStatus.BAD_REQUEST);
        }

        if(!Util.validatePassword(password)){
            return new ResponseEntity("Weak password!", HttpStatus.BAD_REQUEST);
        }

        User existingUser = userService.findUser(phoneNum);
        if(existingUser != null){
            return new ResponseEntity("Try any other contact number, this contact number has already been registered!", HttpStatus.BAD_REQUEST);
        }

        String hashedPassword = Util.hashPassword(password);

        User userProfile = new User(firstName,lastName,email,phoneNum,hashedPassword);
        userProfile.setFirstName(firstName);
        userProfile.setLastName(lastName);
        userProfile.setContactNumber(phoneNum);
        userProfile.setEmail(email);

        userService.save(userProfile);
        return new ResponseEntity("User with contact number " + phoneNum + " successfully registered!", HttpStatus.OK);

    }
    /*
     * This endpoint is used to update firstname and lastname of an already registered user.
     * Authentication is required to access this endpoint, so accessToken is taken as request header to make sure user is authenticated.
     */
    @PutMapping("")
    @CrossOrigin
    public ResponseEntity<?> updateUser(@RequestParam("firstName") String firstName, @RequestParam(required = false,name ="lastName") String lastName,@RequestHeader String accessToken) {
        if (userAuthTokenService.isUserLoggedIn(accessToken) == null) {
            return new ResponseEntity<>("Please Login first to access this endpoint!", HttpStatus.UNAUTHORIZED);
        } else if (userAuthTokenService.isUserLoggedIn(accessToken).getLogoutAt() != null) {
            return new ResponseEntity<>("You have already logged out. Please Login first to access this endpoint!", HttpStatus.UNAUTHORIZED);
        }
        else{
            UserAuthToken authToken = userAuthTokenService.isUserLoggedIn(accessToken);
            Integer userId = userAuthTokenService.getUserId(accessToken);
            User userDetails = authToken.getUser();
            String lastNameChanged = lastName;
            if(lastName == null || lastName.isEmpty()){
                lastNameChanged = userDetails.getLastName();
            }
            User userChangedDetails = userService.updateUser(firstName,lastNameChanged,userId );
            return new ResponseEntity<>(userChangedDetails,HttpStatus.OK);
        }

    }

    /*
     * This endpoint is used to update password of an already registered user.
     * Authentication is required to access this endpoint, so accessToken is taken as request header to make sure user is authenticated.
     */
    @PutMapping("/password")
    @CrossOrigin
    public ResponseEntity<?> updatePassword(@RequestParam("oldPassword") String oldPassword, @RequestParam("newPassword") String newPassword,@RequestHeader String accessToken) {
        if (userAuthTokenService.isUserLoggedIn(accessToken) == null) {
            return new ResponseEntity<>("Please Login first to access this endpoint!", HttpStatus.UNAUTHORIZED);
        } else if (userAuthTokenService.isUserLoggedIn(accessToken).getLogoutAt() != null) {
            return new ResponseEntity<>("You have already logged out. Please Login first to access this endpoint!", HttpStatus.UNAUTHORIZED);
        }
        else{
            UserAuthToken authToken = userAuthTokenService.isUserLoggedIn(accessToken);
            Integer userId = userAuthTokenService.getUserId(accessToken);
            User user  =  userService.getUserById(userId);
            String hashedOldPassword = Util.hashPassword(oldPassword);

            if(!user.getPassword().equals(hashedOldPassword)){
                return new ResponseEntity<>("Your password did not match to your old password!", HttpStatus.UNAUTHORIZED);

            }
            else if(!Util.validatePassword(newPassword)){
                return new ResponseEntity("Weak password!", HttpStatus.BAD_REQUEST);
            }

            String hashedNewPassword = Util.hashPassword(newPassword);

            userService.updatePassword(hashedNewPassword,userId );
            return new ResponseEntity<>("Password updated successfully!",HttpStatus.OK);
        }

    }
}
