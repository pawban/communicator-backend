package com.pawban.communicator.converter;

import com.pawban.communicator.type.AccessRequestStatus;
import org.springframework.core.convert.converter.Converter;

public class StringToAccessRequestStatusConverter implements Converter<String, AccessRequestStatus> {

    @Override
    public AccessRequestStatus convert(String source) {
        return AccessRequestStatus.valueOf(source.toUpperCase());
    }

}
