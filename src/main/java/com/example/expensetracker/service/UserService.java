package com.example.expensetracker.service;

import com.example.expensetracker.dtos.UserDto;
import com.example.expensetracker.exception.ResourceExistsException;
import com.example.expensetracker.exception.ResourceNotFoundException;
import com.example.expensetracker.model.User;
import com.example.expensetracker.repository.UserRepository;
import com.googlecode.jmapper.JMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserService {
    private final UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public UserDto getUserById(Long userId) {
        JMapper<UserDto, User> userMapper = new JMapper<>(
                UserDto.class, User.class
        );

        var user = userRepository.findUserById(userId).orElseThrow(
                () -> new ResourceNotFoundException("User", "userId", userId)
        );
        return userMapper.getDestination(user);
    }

    public UserDto getUserByEmail(String userEmail) {
        JMapper<UserDto, User> userMapper = new JMapper<>(
                UserDto.class, User.class
        );

        var user = userRepository.findUserByEmail(userEmail).orElseThrow(
                () -> new ResourceNotFoundException("User", "userEmail", userEmail)
        );

        return userMapper.getDestination(user);
    }

    public List<UserDto> getAllUsers(){
        JMapper<UserDto, User> userMapper= new JMapper<>(
                UserDto.class, User.class);
        var users = userRepository.findAll();
        var usersDto = new ArrayList<UserDto>();
        users.forEach(u -> usersDto.add(userMapper.getDestination(u)));
        return usersDto;
    }

    public UserDto updateUser(Long userId, UserDto userDto) {
        JMapper<User, UserDto> userMapper= new JMapper<>(
                User.class, UserDto.class);

        var user = userMapper.getDestination(userDto);

        var dbUser = userRepository.findUserById(userId).orElseThrow(
                () -> new ResourceNotFoundException("User", "userId", userId)
        );

        dbUser.setFullName(user.getFullName());
        dbUser.setEmail(user.getEmail());
        dbUser.setPhoneNumber(user.getPhoneNumber());
        dbUser.setAvatarUri(user.getAvatarUri());

        var updatedUser = userRepository.save(dbUser);
        JMapper<UserDto, User> userMapperBack = new JMapper<>(
                UserDto.class, User.class);
        return userMapperBack.getDestination(updatedUser);
    }

    public UserDto saveUser(UserDto userDto) {
        JMapper<User, UserDto> userMapper= new JMapper<>(
                User.class, UserDto.class);

        var user = userMapper.getDestination(userDto);

        var checkUser = userRepository.findUserByEmail(user.getEmail());
        if (checkUser.isPresent()) {
            throw new ResourceExistsException("User", "userEmail", checkUser.get().getEmail());
        }

        var savedUser = userRepository.save(user);

        JMapper<UserDto, User> userMapperBack = new JMapper<>(
                UserDto.class, User.class);

        return userMapperBack.getDestination(savedUser);
    }
}
