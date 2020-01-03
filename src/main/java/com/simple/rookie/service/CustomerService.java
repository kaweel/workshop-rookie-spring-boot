package com.simple.rookie.service;

import com.simple.rookie.config.BusinessException;
import com.simple.rookie.controller.request.CreateCustomerRequest;
import com.simple.rookie.controller.request.UpdateCustomerRequest;
import com.simple.rookie.controller.response.GetCustomerResponse;
import com.simple.rookie.dao.entity.Address;
import com.simple.rookie.dao.entity.Customer;
import com.simple.rookie.dao.mapping.CustomerAddressJQL;
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
    private AuthService authService;

    @Autowired
    public CustomerService(
            CustomerRepository customerRepository,
            AuthService authService
    ) {
        this.customerRepository = customerRepository;
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

        customer = new Customer();
        customer.setUserName(request.getUsername());
        customer.setPassword(request.getPassword());
        customer.setCreateBy(CREATOR_ROOKIE);

        List<Address> addressList = null;
        if (!CollectionUtils.isEmpty(request.getAddress())) {
            addressList = new ArrayList<>();
            for (CreateCustomerRequest.Address it : request.getAddress()) {
                Address address = new Address();
                address.setType(it.getType());
                address.setAddress(it.getAddress());
                address.setCustomer(customer);
                address.setCreateBy(CREATOR_ROOKIE);
                addressList.add(address);
            }
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

        List<Address> srcAddressList = customer.getAddress();
        List<Address> updateAddressList = null;

        if (!CollectionUtils.isEmpty(request.getAddress())) {
            updateAddressList = request.getAddress().stream()
                    .map(rq -> {

                        Address address;
                        if (null == rq.getId()) {
                            address = new Address();
                            address.setCustomer(customer);
                            address.setCreateBy(CREATOR_ROOKIE);
                        } else {
                            address = srcAddressList.stream()
                                    .filter(update -> update.getAddressId() == rq.getId())
                                    .findFirst()
                                    .orElse(null);

                            if (null == address) {
                                return null;
                            }

                            address.setUpdateBy(CREATOR_ROOKIE);
                        }
                        address.setType(rq.getType());
                        address.setAddress(rq.getAddress());
                        return address;
                    })
                    .filter(Objects::nonNull)
                    .collect(Collectors.toList());
        }
        customer.setUserName(request.getUsername());
        customer.setAddress(updateAddressList);
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
