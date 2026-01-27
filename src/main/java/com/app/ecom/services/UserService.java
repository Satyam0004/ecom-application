package com.app.ecom.services;

import com.app.ecom.dto.AddressDto;
import com.app.ecom.dto.UserRequest;
import com.app.ecom.dto.UserResponse;
import com.app.ecom.model.Addresses;
import com.app.ecom.model.User;
import com.app.ecom.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class UserService {

    private final UserRepository userRepository;

    public List<UserResponse> fetchAllUsers() {
        return userRepository.findAll().stream()
                .map(this::mapToUserResponse)
                .collect(Collectors.toList());
    }

    public void addUser(UserRequest userRequest) {
        User user = new User();
        updateUserFormRequest(user, userRequest);
        userRepository.save(user);
    }

    public Optional<UserResponse> fetchUser(Long id) {
        return userRepository.findById(id)
                .map(this::mapToUserResponse);
    }

    public boolean updateUser(Long id, UserRequest updatedUserRequest) {

        return userRepository.findById(id)
                .map(existingUser -> {
                    updateUserFormRequest(existingUser, updatedUserRequest);
                    userRepository.save(existingUser);
                    return true;
                }).orElse(false);
    }

    private void updateUserFormRequest(User user, UserRequest userRequest) {
        user.setFirstName(userRequest.getFirstName());
        user.setLastName(userRequest.getLastName());
        user.setEmail(userRequest.getEmail());
        user.setPhoneNumber(String.valueOf(userRequest.getPhoneNumber()));

        if(userRequest.getAddress() != null) {
            Addresses addresses = new Addresses();
            addresses.setStreet(userRequest.getAddress().getStreet());
            addresses.setCity(userRequest.getAddress().getCity());
            addresses.setState(userRequest.getAddress().getState());
            addresses.setCountry(userRequest.getAddress().getCountry());
            addresses.setZipcode(userRequest.getAddress().getZipcode());
            user.setAddress(addresses);
        }
    }

    private UserResponse mapToUserResponse(User user) {
        UserResponse response = new UserResponse();
        response.setId(user.getId());
        response.setFirstName(user.getFirstName());
        response.setLastName(user.getLastName());
        response.setEmail(user.getEmail());
        response.setPhoneNumber(user.getPhoneNumber());
        response.setRole(user.getRole());

        if(user.getAddress() != null) {
            AddressDto addressDto = new AddressDto();
            addressDto.setStreet(user.getAddress().getStreet());
            addressDto.setCity(user.getAddress().getCity());
            addressDto.setCountry(user.getAddress().getCountry());
            addressDto.setState(user.getAddress().getState());
            addressDto.setZipcode(user.getAddress().getZipcode());
            response.setAddress(addressDto);
        }
        return response;
    }
}
