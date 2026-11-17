package com.drs.smartreview_ai.dto;

import com.drs.smartreview_ai.enums.UserRole;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserResponse {

    private Long id;
    private String name;
    private String email;
    private UserRole role;
}