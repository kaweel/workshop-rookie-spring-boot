package com.simple.rookie.dao.repository;

import com.simple.rookie.dao.entity.Address;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AddressRepository extends JpaRepository<Address, Integer> {
}
