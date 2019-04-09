package com.leyou.auth.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 *@Description 用户信息
 *@author apdoer
 *@CreateDate 2019/3/26-22:16
 *@Version 1.0
 *===============================
**/
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserInfo {

    private Long id;

    private String username;


}