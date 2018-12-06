package org.upgrad.services;

import org.springframework.stereotype.Service;
import org.upgrad.models.User;
import org.upgrad.models.UserAuthToken;
import org.upgrad.repositories.UserAuthTokenRepository;
import org.upgrad.repositories.UserRepository;

import javax.transaction.Transactional;
import java.util.Optional;

@Service
@Transactional
public class UserServiceImpl implements UserService{

    private final UserRepository userRepository;

    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public String findUserPassword(String contactNumber) {
        return userRepository.findUserPassword(contactNumber);
    }

    @Override
    public User findUser(String contactNumber) {
        return userRepository.findUser(contactNumber);
    }

    @Override
    public void save(User profile){
        userRepository.save(profile);
    }

    @Override
    public User updateUser( String firstName, String lastName, Integer userId){
        Optional<User> userObj = userRepository.findById(userId);
        User user = userObj.get();
        user.setFirstName(firstName);
        user.setLastName(lastName);
        userRepository.save(user);
        return user;
    }

    @Override
    public User getUserById(Integer userId){
        Optional<User> user = userRepository.findById(userId);
        return user.get();
    }

    @Override
    public void updatePassword(String newPassword,Integer userId){
        userRepository.updatePassword(newPassword,userId);
    }


}
