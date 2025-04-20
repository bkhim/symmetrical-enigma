package com.studentportal.dto;

import lombok.Getter;

@Getter
public class LoginResponse {
    private String token;
    private UserData user;

    public LoginResponse(String token, String firstName, String lastName) {
        this.token = token;
        this.user = new UserData(firstName, lastName);
    }

    @Getter
    public static class UserData {
        private String firstName;
        private String lastName;

        public UserData(String firstName, String lastName) {
            this.firstName = firstName;
            this.lastName = lastName;
        }

    }
}
