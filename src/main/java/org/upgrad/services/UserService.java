package org.upgrad.services;

import org.upgrad.models.User;
import org.upgrad.models.UserAuthToken;

import java.util.Optional;

/*
 * This UserService interface gives the list of all the service that exist in the user service implementation class.
 * Controller class will be calling the service methods by this interface.
 */
public interface UserService {

    String findUserPassword(String contactNumber);

    User findUser(String contactNumber);

    void save(User profile);

    User updateUser(String firstName, String lastName, Integer userId);

    User getUserById(Integer userId);

    void updatePassword(String newPassword,Integer userId);
}
