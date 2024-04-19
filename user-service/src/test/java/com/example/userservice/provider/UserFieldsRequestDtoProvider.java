package com.example.userservice.provider;

import com.example.userservice.dto.request.UserFieldsRequestDto;

public class UserFieldsRequestDtoProvider {

    public static UserFieldsRequestDto getUpdateUserFieldsRequest() {
        return new UserFieldsRequestDto("Jon", "Jes", "ADMIN");
    }
}
