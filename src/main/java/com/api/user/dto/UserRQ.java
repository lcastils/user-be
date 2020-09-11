package com.api.user.dto;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserRQ implements Serializable {

    private static final long serialVersionUID = 1L;
    @JsonProperty(value="id", required = false)
    private Long id;
    private String name;
    private String email;
    private String password;
    private List<Phone> phones;
    @JsonProperty(value="token", required = false)
    private String token;
    @JsonProperty(value="creationDate", required = false)
    private LocalDateTime creationDate;
    @JsonProperty(value="updateDate", required = false)
    private LocalDateTime updateDate;
    @JsonProperty(value="lastLogin", required = false)
    private LocalDateTime lastLogin;
    @JsonProperty(value="isActive", required = false)
    private Boolean isActive;
 

}
