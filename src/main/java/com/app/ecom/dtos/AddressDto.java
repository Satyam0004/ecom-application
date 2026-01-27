package com.app.ecom.dtos;

import lombok.Data;

@Data
public class AddressDto {
    private String street;
    private String city;
    private String country;
    private String state;
    private String zipcode;
}
