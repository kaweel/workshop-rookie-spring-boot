package com.simple.rookie.dao.repository;

import com.simple.rookie.dao.entity.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface CustomerRepository extends JpaRepository<Customer, Integer> {

    @Query(
            value = "SELECT cu \n" +
                    "FROM Customer cu\n" +
                    "WHERE cu.userName = :userName\n" +
                    "AND cu.password = :password"
    )
    Optional<Customer> auth(@Param("userName") String userName, @Param("password") String password);
}
