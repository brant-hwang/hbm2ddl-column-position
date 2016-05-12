package io.brant.examples.entity;

import io.brant.examples.annotation.ColumnPosition;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class CompanyTest {

    @Id
    @ColumnPosition(1)
    private String companyCode;

    @ColumnPosition(2)
    private String companyName;

    @ColumnPosition(3)
    private String address;

    @ColumnPosition(4)
    private String zipCode;

    @ColumnPosition(5)
    private String ceoName;

}
