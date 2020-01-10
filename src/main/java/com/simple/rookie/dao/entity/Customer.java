package com.simple.rookie.dao.entity;

import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Data
@Entity
@Table(name = "customer")
public class Customer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "customer_id")
    private Integer customerId;

    @Column(name = "user_name", nullable = false)
    private String userName;

    @Column(name = "password", nullable = false)
    private String password;

    @OneToMany(mappedBy = "customer", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Address> address;

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
