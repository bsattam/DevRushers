package com.appdev_soumitri.humbirds;

public class UserAPI {
    private String email,name, userID;
    private int age;
    private String gender;

    public UserAPI() {}

    public String getUserID() {
        return this.userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public UserAPI(String name, String email, int age, String gender, String userID) {
        this.email = email;
        this.name = name;
        this.age = age;
        this.gender = gender;
        this.userID = userID;
    }

    public String getEmail() {
        return this.email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return this.age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getGender() {
        return this.gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }


}
