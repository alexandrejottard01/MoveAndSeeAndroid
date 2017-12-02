package com.henallux.moveandseeandroid.Model;

import java.util.Date;

/**
 * Created by Alexandre on 14-11-17.
 */

public class User {

    public String id;
    public String userName;
    public boolean isCertified;
    public String nameCertified;
    public String password;
    public String email;
    public String language;
    public boolean isMale;
    public Date birthDate;

    public User(String id, String userName, boolean isCertified, String nameCertified, String email, String language, boolean isMale, Date birthDate) {
        this.id = id;
        this.userName = userName;
        this.isCertified = isCertified;
        this.nameCertified = nameCertified;
        this.email = email;
        this.language = language;
        this.isMale = isMale;
        this.birthDate = birthDate;
    }

    public User(String userName, boolean isCertified, String password, String email, String language) {
        this.userName = userName;
        this.isCertified = isCertified;
        this.password = password;
        this.email = email;
        this.language = language;
    }
}
