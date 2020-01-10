package com.simple.rookie.dao.repository;

import com.simple.rookie.dao.entity.Customer;
import com.simple.rookie.dao.mapping.CustomerAddressJQL;
import com.simple.rookie.dao.mapping.CustomerAddressNative;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface CustomerRepository extends JpaRepository<Customer, Integer> {

    Optional<Customer> findByUserName(String userName);


    @Modifying
    @Query(
            value = "DELETE FROM Customer cu WHERE cu.userName = :userName"
    )
    void deleteByUserName(@Param("userName") String userName);

    @Query(
            value = "SELECT " +
                    "cu.customer_id as customerId,\n" +
                    "cu.user_name as userName,\n" +
                    "ad.address_id as addressId,\n" +
                    "ad.type as type,\n" +
                    "ad.address as address\n" +
                    "FROM customer cu\n" +
                    "LEFT JOIN address ad ON cu.customer_id = ad.customer_id\n" +
                    "WHERE cu.user_name = ?1",
            nativeQuery = true
    )
    List<CustomerAddressNative> findByUserNameNative(String userName);


    @Query(
            value = "SELECT " +
                    "new com.simple.rookie.dao.mapping.CustomerAddressJQL(\n" +
                    "cu.customerId,\n" +
                    "cu.userName,\n" +
                    "ad.addressId,\n" +
                    "ad.type,\n" +
                    "ad.address" +
                    ")\n" +
                    "FROM Customer cu\n" +
                    "JOIN cu.address ad\n" +
                    "WHERE cu.userName = :userName"
    )
    List<CustomerAddressJQL> findByUserNameJQL(@Param("userName") String userName);

    @Query(
            value = "SELECT " +
                    "new com.simple.rookie.dao.mapping.CustomerAddressJQL(\n" +
                    "cu.customerId,\n" +
                    "cu.userName,\n" +
                    "ad.addressId,\n" +
                    "ad.type,\n" +
                    "ad.address" +
                    ")\n" +
                    "FROM Customer cu\n" +
                    "JOIN cu.address ad\n" +
                    "WHERE cu.customerId = :customerId"
    )
    List<CustomerAddressJQL> findByCustomerId(@Param("customerId") Integer customerId);

    @Query(
            value = "SELECT cu \n" +
                    "FROM Customer cu\n" +
                    "WHERE cu.userName = :userName\n" +
                    "AND cu.password = :password"
    )
    Optional<Customer> auth(@Param("userName") String userName, @Param("password") String password);
}
