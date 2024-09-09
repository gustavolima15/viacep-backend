package com.backendufbaendereco.demo.services;


import com.backendufbaendereco.demo.DTO.AddressRequestDTO;
import com.backendufbaendereco.demo.DTO.AddressResponseDTO;
import com.backendufbaendereco.demo.DTO.UserFindResponseDTO;
import com.backendufbaendereco.demo.DTO.UserResponseDTO;
import com.backendufbaendereco.demo.Exeption.ConflictException;
import com.backendufbaendereco.demo.Exeption.GlobalExceptionHandler;
import com.backendufbaendereco.demo.Exeption.ValidationException;
import com.backendufbaendereco.demo.Projections.AddressResponseProjections;
import com.backendufbaendereco.demo.entities.address.Address;
import com.backendufbaendereco.demo.entities.address.City;
import com.backendufbaendereco.demo.entities.address.State;
import com.backendufbaendereco.demo.entities.user.User;
import com.backendufbaendereco.demo.entities.user.UserAddressMapping;
import com.backendufbaendereco.demo.repositories.addressRepository.AddressRepository;
import com.backendufbaendereco.demo.repositories.CityRepository;
import com.backendufbaendereco.demo.repositories.userAddressMappingRepository.UserAddressMappingRepository;
import com.backendufbaendereco.demo.repositories.UserRepository;
import com.backendufbaendereco.demo.repositories.userAddressMappingRepository.UserAddressMappingRepositoryImpl;
import com.backendufbaendereco.demo.util.RandomString;
import jakarta.mail.MessagingException;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private MailService mailService;
    @Autowired
    private CityRepository cityRepository;
    @Autowired
    private AddressService addressService;


    @Autowired
    private AddressRepository addressRepository;

    private UserAddressMappingRepositoryImpl userAddressMappingRepositoryImpl;

    public UserService(UserAddressMappingRepositoryImpl userAddressMappingRepositoryImpl) {
        this.userAddressMappingRepositoryImpl = userAddressMappingRepositoryImpl;
    }

    @Transactional
    public UserResponseDTO registerUser(User user) throws MessagingException, UnsupportedEncodingException {

        if (userRepository.findByEmail(user.getEmail()) != null) {
            throw new ConflictException("This email already exists");
        }

        String passwordEncoded = passwordEncoder.encode(user.getPassword());
        user.setPassword(passwordEncoded);

        String randomCode = RandomString.generateRandomString(64);
        user.setVerificationCode(randomCode);
        user.setEnabled(true);

        User savedUser = userRepository.save(user);
        //  mailService.sendVerificationEmail(savedUser);

        return new UserResponseDTO(savedUser.getId(), savedUser.getName(), savedUser.getEmail(), savedUser.getVerificationCode());
    }

    public boolean verify(String verificationCode) {
        User user = userRepository.findByVerificationCode(verificationCode);
        if (user == null || user.isEnabled()) {
            return false;
        }
        user.setVerificationCode(null);
        user.setEnabled(true);
        userRepository.save(user);
        User savedUser = userRepository.save(user);


        return (savedUser != null) ? true : true;
    }

    public List<UserFindResponseDTO> findAll() {

        return userRepository.findAll()
                .stream()
                .map(UserFindResponseDTO::fromUser).toList();

    }

    public UserFindResponseDTO findById(Long id) {
        User response = userRepository.findById(id).orElseThrow(() -> new ValidationException("User not found"));
        return UserFindResponseDTO.fromUser(response);
    }

    @Transactional
    public void createUserAddress(AddressRequestDTO address, Long userId) {

        Address addressData = addressService.createAddress(address);
        User user = userRepository.findById(userId).orElseThrow(() -> new ValidationException("User not found"));


        if (address.getIsMainAddress()) {
            userAddressMappingRepositoryImpl.updateIsMainAddressByUserId(user);
        }

        UserAddressMapping userAddressMapping = new UserAddressMapping();
        userAddressMapping.setUserId(user);
        userAddressMapping.setAddressId(addressData);
        userAddressMapping.setMainAddress(address.getIsMainAddress());

        userAddressMappingRepositoryImpl.save(userAddressMapping);

    }

    @Transactional
    public AddressResponseDTO updateUserAddress(AddressRequestDTO address, Long userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new ValidationException("User not found"));

        City city = cityRepository.findById(address.getCityId())
                .orElseThrow(() -> new ValidationException("City not found with id: " + address.getCityId()));

        State state = addressService.getStateIdByCityId(address.getCityId());
        Address addressData = address.toPutAddress(city, state);


        UserAddressMapping userAddressMapping = userAddressMappingRepositoryImpl.findByUserIdAndByAddressId(user, addressData)
                .orElseThrow(() -> new ValidationException("not found for user ID " + user.getId() + " and address ID " + address.getId()));


        if (address.getIsMainAddress()) {
            userAddressMappingRepositoryImpl.updateIsMainAddressByUserId(user);
        }

        userAddressMapping.setMainAddress(address.getIsMainAddress());
        userAddressMappingRepositoryImpl.save(userAddressMapping);

        Address addressUpdated = addressRepository.save(addressData);
        return AddressResponseDTO.addressResponse(addressUpdated, userAddressMapping.isMainAddress());

    }

    @Transactional
    public void deleteUserAddress(Long addressId, Long userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new ValidationException("User not found"));
        Address address = new Address();
        address.setId(addressId);


        UserAddressMapping userAddressMapping = userAddressMappingRepositoryImpl.findByUserIdAndByAddressId(user, address)
                .orElseThrow(() -> new ValidationException("not found for user ID " + user.getId() + " and address ID " + address));

        userAddressMappingRepositoryImpl.deleteById(userAddressMapping.getId());

        addressRepository.deleteById(addressId);

    }

    public Page<AddressResponseDTO> findAllUserAddress(Long userId, Pageable pageable) {
        Page<UserAddressMapping> userAddressMappingPage = userAddressMappingRepositoryImpl.findAllUserAddress(pageable, userId);

        List<AddressResponseDTO> addressResponseDTOList = userAddressMappingPage.getContent()
                .stream()
                .map(userAddressMapping -> AddressResponseDTO.addressResponse(
                        userAddressMapping.getAddressId(),
                        userAddressMapping.isMainAddress()
                )).toList();


        return new PageImpl<>(addressResponseDTOList, pageable, userAddressMappingPage.getTotalElements());
    }

    public AddressResponseDTO findByAddressIdUserAddress(Long userId, Long addressId) {

        UserAddressMapping result = userAddressMappingRepositoryImpl.findByAddressIdUserAddress(userId, addressId)
                .orElseThrow(() -> new ValidationException("not found for user ID " + userId + " and address ID " + addressId));
        return AddressResponseDTO.addressResponse(result.getAddressId(), result.isMainAddress());

    }

    public void deleteUser(Long id) {
        if (userRepository.existsById(id)) {
            userRepository.deleteById(id);
        } else {
            throw new ValidationException("User not found");
        }
    }

    // public List<AddressResponseDTO> findAddressByUser(Long userId) {
      
    //     List<UserAddressMapping> userAddressMappingList = userAddressMappingRepositoryImpl.findAddressByUser(userId);
    
    //     return userAddressMappingList.stream()
    //         .map(userAddressMapping -> AddressResponseDTO.addressResponse(
    //                 userAddressMapping.getAddressId(),
    //                 userAddressMapping.isMainAddress()  
    //         )).toList();
    // }

    public List<User> findAllUsers() {
        return userRepository.findAll();
    }

    public List<User> findUsersWithAddresses() {
        return userRepository.findUsersWithAddresses();
    }

    public List<User> findUsersWithMultipleAddresses() {
        return userRepository.findUsersWithMultipleAddresses();
    }

    public List<User> findUserByEmail(String email) {
        return userRepository.findUserByEmail(email);
    }

}
