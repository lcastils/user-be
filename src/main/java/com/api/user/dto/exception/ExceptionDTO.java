package com.api.user.dto.exception;

import java.io.Serializable;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ExceptionDTO implements Serializable {

    private static final long serialVersionUID = 1L;
    
    private String message;
    
    public ExceptionDTO() {
        
    }
    
    public ExceptionDTO(String message) {
        this.message = message; 
    }

}
