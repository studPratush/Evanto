package com.example.root.evanto;

/**
 * Created by root on 24/9/17.
 */

public class UserInformation {
    String Fullname,Email,Password,Mobile,Gender,DOB,Joined,Participated;

    public UserInformation(String fullname, String email, String password, String mobile, String gender, String DOB,String joined,String participated) {
        this.Fullname = fullname;
        this.Email = email;
        this.Password = password;
        this.Mobile = mobile;
        this.Gender = gender;
        this.DOB = DOB;
        this.Joined = joined;
        this.Participated = participated;
    }
}
