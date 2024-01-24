package com.kpro.common.utils;

import org.modelmapper.AbstractConverter;
import org.modelmapper.ModelMapper;

import java.util.UUID;

public class KproModelMapper {
    private static volatile ModelMapper modelMapper = null;

    private KproModelMapper() {
        throw new IllegalStateException("Singleton class");
    }

    public static ModelMapper getInstance() {
        ModelMapper localMapper = modelMapper;
        if (localMapper == null) {
            synchronized (KproModelMapper.class) {
                localMapper = modelMapper;
                if (localMapper == null) {
                    var mapper = new ModelMapper();
                    mapper.addConverter(new AbstractConverter<String, UUID>() {
                        protected UUID convert(String s) {
                            return UUID.fromString(s);
                        }
                    });
                    modelMapper = localMapper = mapper;
                }
            }
        }
        return localMapper;
    }
}
