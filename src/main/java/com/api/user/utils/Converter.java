
package com.api.user.utils;

import org.modelmapper.ModelMapper;

public final class Converter {
    private static ModelMapper modelMapper;

    private Converter() {
    }

    public static ModelMapper getMapper() {
        if (modelMapper == null) {
            modelMapper = new ModelMapper();
        }
        return modelMapper;
    }
}
