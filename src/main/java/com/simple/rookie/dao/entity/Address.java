package com.simple.rookie.dao.entity;

import com.simple.rookie.enums.AddressType;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.util.Date;

@Data
@Entity
@Table(name = "address")
public class Address {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "address_id")
    private Integer addressId;

    @Column(name = "type", nullable = false)
    @Enumerated(EnumType.STRING)
    private AddressType type;

    @Column(name = "address", nullable = false)
    private String address;

    @JoinColumn(name = "customer_id", nullable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    private Customer customer;

    @Column(name = "create_by", updatable = false)
    private String createBy;

    @CreationTimestamp
    @Column(name = "create_date", updatable = false)
    private Date createDate;

    @Column(name = "update_by", insertable = false)
    private String updateBy;

    @UpdateTimestamp
    @Column(name = "update_date", insertable = false)
    private Date updateDate;
}