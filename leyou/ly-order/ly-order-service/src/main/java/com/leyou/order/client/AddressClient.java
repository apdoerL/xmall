package com.leyou.order.client;

import com.leyou.order.dto.AddressDto;

import java.util.ArrayList;
import java.util.List;

/**
 *@Description 模拟的地址查询
 *@author apdoer
 *@CreateDate 2019/3/29-10:04
 *@Version 1.0
 *===============================
**/
public abstract class AddressClient {
    public static final List<AddressDto> addressList = new ArrayList<AddressDto>(){
        {

            AddressDto addressDto = new AddressDto();
            addressDto.setId(1L);
            addressDto.setName("apdoer");
            addressDto.setAddress("飞机场");
            addressDto.setCity("深圳");
            addressDto.setDistrict("宝安机场");
            addressDto.setPhone("123456789");
            addressDto.setState("深圳");
            addressDto.setZipCode("210000");
            addressDto.setIsDefault(true);


            AddressDto addressDto1 = new AddressDto();
            addressDto1.setId(2L);
            addressDto1.setName("apdoer1");
            addressDto1.setAddress("飞机场2");
            addressDto1.setCity("香港");
            addressDto1.setDistrict("香港机场");
            addressDto1.setPhone("123456789");
            addressDto1.setState("香港");
            addressDto1.setZipCode("310000");
            addressDto1.setIsDefault(true);
        }
    };

    public static AddressDto findById(Long id){
        for (AddressDto addressDto : addressList) {
            if (addressDto.getId()==id){
                return addressDto;
            }
        }
        return null;
    }
}
