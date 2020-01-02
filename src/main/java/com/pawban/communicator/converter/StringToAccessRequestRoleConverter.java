package com.pawban.communicator.converter;

import com.pawban.communicator.type.AccessRequestRole;
import org.springframework.core.convert.converter.Converter;

public class StringToAccessRequestRoleConverter implements Converter<String, AccessRequestRole> {

    @Override
    public AccessRequestRole convert(String source) {
        return AccessRequestRole.valueOf(source.toUpperCase());
    }

}
