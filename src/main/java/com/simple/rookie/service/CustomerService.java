package com.simple.rookie.service;

import com.simple.rookie.config.BusinessException;
import com.simple.rookie.controller.request.CreateCustomerRequest;
import com.simple.rookie.controller.request.UpdateCustomerRequest;
import com.simple.rookie.controller.response.GetCustomerResponse;
import com.simple.rookie.dao.entity.Address;
import com.simple.rookie.dao.entity.Customer;
import com.simple.rookie.dao.mapping.CustomerAddressJQL;
import com.simple.rookie.dao.repository.AddressRepository;
import com.simple.rookie.dao.repository.CustomerRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
public class CustomerService {

    public static final String CREATOR_ROOKIE = "rookie";

    private CustomerRepository customerRepository;
    private AddressRepository addressRepository;
    private AuthService authService;

    @Autowired
    public CustomerService(
            CustomerRepository customerRepository,
            AddressRepository addressRepository,
            AuthService authService
    ) {
        this.customerRepository = customerRepository;
        this.addressRepository = addressRepository;
        this.authService = authService;
    }

    public List<Customer> findAll() {
        return customerRepository.findAll();
    }

    public GetCustomerResponse get(String token, Integer id) {
        Boolean tokenIsValid = authService.tokenIsValid(token);
        if (!tokenIsValid) {
            throw new BusinessException(HttpStatus.UNAUTHORIZED, "invalid token");
        }

        List<CustomerAddressJQL> customerAddressJQLS = customerRepository.findByCustomerId(id);
        if (CollectionUtils.isEmpty(customerAddressJQLS)) {
            throw new BusinessException(HttpStatus.NOT_FOUND, "customer not found");
        }

        Map<Integer, String> customerMap = new HashMap<>();
        Map<Integer, List<GetCustomerResponse.Address>> addressMap = new HashMap<>();
        GetCustomerResponse response = customerAddressJQLS.stream()
                .map(it -> {

                    String username = customerMap.get(it.getCustomerId());
                    if (null == username) {
                        customerMap.put(it.getCustomerId(), it.getUserName());
                    }

                    if (null == it.getAddressId()) {
                        return it.getCustomerId();
                    }

                    GetCustomerResponse.Address address = new GetCustomerResponse.Address();
                    address.setId(it.getAddressId());
                    address.setType(it.getAddressType());
                    address.setAddress(it.getAddress());

                    List<GetCustomerResponse.Address> addresses = addressMap.get(it.getCustomerId());
                    if (addresses == null) {
                        addresses = new ArrayList<>();
                    }
                    addresses.add(address);
                    addressMap.put(it.getCustomerId(), addresses);
                    return it.getCustomerId();
                })
                .collect(Collectors.toList())
                .stream().distinct()
                .map(it -> {
                    GetCustomerResponse getCustomerResponse = new GetCustomerResponse();
                    getCustomerResponse.setId(it);
                    getCustomerResponse.setUsername(customerMap.get(it));
                    getCustomerResponse.setAddress(addressMap.get(it));
                    return getCustomerResponse;
                }).findFirst().orElseThrow(() -> {
                    throw new BusinessException(HttpStatus.NOT_FOUND);
                });
        return response;
    }

    @Transactional
    public void create(String token, CreateCustomerRequest request) {

        Boolean tokenIsValid = authService.tokenIsValid(token);
        if (!tokenIsValid) {
            throw new BusinessException(HttpStatus.UNAUTHORIZED, "invalid token");
        }

        Customer customer = customerRepository.findByUserName(request.getUsername()).orElse(null);
        if (null != customer) {
            throw new BusinessException(HttpStatus.CONFLICT, "duplicate username");
        }

        if (CollectionUtils.isEmpty(request.getAddress())) {
            throw new BusinessException(HttpStatus.BAD_REQUEST, "address can't be empty");
        }

        customer = new Customer();
        customer.setUserName(request.getUsername());
        customer.setPassword(request.getPassword());
        customer.setCreateBy(CREATOR_ROOKIE);

        List<Address> addressList = new ArrayList<>();
        for (CreateCustomerRequest.Address it : request.getAddress()) {
            Address address = new Address();
            address.setType(it.getType());
            address.setAddress(it.getAddress());
            address.setCustomer(customer);
            address.setCreateBy(CREATOR_ROOKIE);
            addressList.add(address);
        }
        customer.setAddress(addressList);
        customerRepository.save(customer);
    }

    @Transactional
    public void update(String token, UpdateCustomerRequest request) {
        Boolean tokenIsValid = authService.tokenIsValid(token);
        if (!tokenIsValid) {
            throw new BusinessException(HttpStatus.UNAUTHORIZED, "invalid token");
        }

        Customer customer = customerRepository.findById(request.getId()).orElse(null);
        if (null == customer) {
            throw new BusinessException(HttpStatus.NOT_FOUND, "customer not found");
        }

        if (CollectionUtils.isEmpty(request.getAddress())) {
            throw new BusinessException(HttpStatus.BAD_REQUEST, "address can't be null");
        }

        List<Address> srcAddressList = customer.getAddress();

        List<Address> insertAddressList = request.getAddress().stream()
                .map(it -> {
                    if (null != it.getId()) {
                        return null;
                    }
                    Address address = new Address();
                    address.setType(it.getType());
                    address.setAddress(it.getAddress());
                    address.setCustomer(customer);
                    address.setCreateBy(CREATOR_ROOKIE);
                    return address;
                })
                .filter(Objects::nonNull)
                .collect(Collectors.toList());

        List<Address> updateAddressList = request.getAddress().stream()
                .map(it -> {
                    if (null == it.getId()) {
                        return null;
                    }
                    Address address = srcAddressList.stream()
                            .filter(update -> update.getAddressId() == it.getId())
                            .findAny()
                            .orElse(null);

                    address.setAddress(it.getAddress());
                    address.setType(it.getType());

                    return address;
                })
                .filter(Objects::nonNull)
                .collect(Collectors.toList());

        log.info("insert {}", insertAddressList.size());
        log.info("update {}", updateAddressList.size());

        List<Address> actionList = new ArrayList<>();
        actionList.addAll(insertAddressList);
        actionList.addAll(updateAddressList);

        customer.setUserName(request.getUsername());
        customer.getAddress().clear();
        customer.setAddress(actionList);
        log.info("actual {}", customer.getAddress().size());
        customerRepository.save(customer);
    }

    @Transactional
    public void delete(String token, Integer id) {
        Boolean tokenIsValid = authService.tokenIsValid(token);
        if (!tokenIsValid) {
            throw new BusinessException(HttpStatus.UNAUTHORIZED, "invalid token");
        }
        customerRepository.deleteById(id);
    }

}
