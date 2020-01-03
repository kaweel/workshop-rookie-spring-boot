package com.simple.rookie.dao.repository;

import com.simple.rookie.dao.entity.Address;
import com.simple.rookie.dao.entity.Customer;
import com.simple.rookie.dao.mapping.CustomerAddressJQL;
import com.simple.rookie.dao.mapping.CustomerAddressNative;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.springframework.test.context.jdbc.Sql.ExecutionPhase;

@ExtendWith(SpringExtension.class)
@DataJpaTest
@Sql(value = {"/sql_script/customer_data.sql"}, executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
@Sql(value = {"/sql_script/customer_cleanup.sql"}, executionPhase = ExecutionPhase.AFTER_TEST_METHOD)
public class CustomerRepositoryTest {

    public static final String INK_USERNAME = "ink";
    public static final String VIOLETTE_USERNAME = "violette";

    @Autowired
    private CustomerRepository customerRepository;

    @Test
    @DisplayName("find customer other by native query not found")
    public void findByUserNameNative_found() {
        List<CustomerAddressNative> actual = customerRepository.findByUserNameNative(INK_USERNAME);
        Assertions.assertEquals(2, actual.size());
    }

    @Test
    @DisplayName("find customer other by native query not found")
    public void findByUserNameNative_not_found() {
        List<CustomerAddressNative> actual = customerRepository.findByUserNameNative("");
        Assertions.assertEquals(0, actual.size());
    }

    @Test
    @DisplayName("find customer other by JQL without relation found")
    public void findByUserName_found() {
        List<CustomerAddressJQL> actual = customerRepository.findByUserNameJQL(VIOLETTE_USERNAME);
        Assertions.assertEquals(1, actual.size());
    }

    @Test
    @DisplayName("find customer other by JQL without relation not found")
    public void findByUserName_not_found() {
        List<CustomerAddressJQL> actual = customerRepository.findByUserNameJQL("");
        Assertions.assertEquals(0, actual.size());
    }

    @Test
    @DisplayName("find by username")
    @Transactional
    public void findByUserName() {
        Customer actual = customerRepository.findByUserName(INK_USERNAME).orElse(null);
        List<Address> addressList = actual.getAddress();
        System.out.println(addressList.get(0).getAddress());
    }
}
