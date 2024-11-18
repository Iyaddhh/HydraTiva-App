package com.example.hydrativa.models;

public class LoginResponse {
    private String token;
    private User user;

    // Getters and setters
    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    // Inner class for user details
    public class User {
        private String username;
        private String email;
        private String name;

        public User(String username, String gender, String name, String phone) {

        }

        // Getters and setters for User fields
        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }
}
