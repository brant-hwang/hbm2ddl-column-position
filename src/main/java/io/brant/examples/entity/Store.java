package io.brant.examples.entity;

import io.brant.examples.annotation.ColumnPosition;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class Store {

    @Id
    @ColumnPosition(1)
    private String storeCode;

    @ColumnPosition(2)
    private String storeName;

    private String address;

    @ColumnPosition(3)
    private String zipCode;

    private String ceoName;

}
