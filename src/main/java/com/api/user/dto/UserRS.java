package com.api.user.dto;

import java.time.LocalDateTime;
import java.util.List;

import lombok.Data;

@Data
public class UserRS {

    private Long id;

    private String name;

    private String email;

    private String token;

    private LocalDateTime creationDate;

    private LocalDateTime updateDate;

    private LocalDateTime lastLogin;

    private boolean isActive;

    private List<Phone> phones;

}
