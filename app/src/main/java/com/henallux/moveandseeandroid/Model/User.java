package com.henallux.moveandseeandroid.Model;

import java.util.Date;

/**
 * Created by Alexandre on 14-11-17.
 */

public class User {

    public long idUser;
    public String pseudo;
    public String password;
    public boolean isCertified;
    public String nameCertified;
    public String email;
    public String language;
    public boolean isMale;
    public Date birthDate;
    public boolean isAdmin;
    public byte[] rowVersion;

    public User(long idUser, String pseudo, String password, boolean isCertified, String nameCertified, String email, String language, boolean isMale, Date birthDate, boolean isAdmin, byte[] rowVersion) {
        this.idUser = idUser;
        this.pseudo = pseudo;
        this.password = password;
        this.isCertified = isCertified;
        this.nameCertified = nameCertified;
        this.email = email;
        this.language = language;
        this.isMale = isMale;
        this.birthDate = birthDate;
        this.isAdmin = isAdmin;
        this.rowVersion = rowVersion;
    }
}
