package com.henallux.moveandseeandroid.Model;

import com.henallux.moveandseeandroid.Enumeration.RegistrationCodeErrorEnumeration;

import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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

    public RegistrationCodeErrorEnumeration isValid(String confirmationPassword){
        if(!pseudoIsValid()){
            return RegistrationCodeErrorEnumeration.BAD_PSEUDO;
        }
        if(!emailIsValid()){
            return RegistrationCodeErrorEnumeration.BAD_EMAIL;
        }
        if(!passwordIsValid()){
            return RegistrationCodeErrorEnumeration.BAD_PASSWORD;
        }
        if(!passwordEqualsPasswordConfirmation(confirmationPassword)){
            return RegistrationCodeErrorEnumeration.PASSWORD_MISS_MATCH;
        }
        return RegistrationCodeErrorEnumeration.OK;
    }


    public boolean emailIsValid(){
        Pattern VALID_EMAIL_ADDRESS_REGEX = Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);
        Matcher matcher = VALID_EMAIL_ADDRESS_REGEX.matcher(email);

        return matcher.find();
    }

    public boolean pseudoIsValid(){
        return userName.length() >= 4;
    }

    public boolean passwordIsValid(){
        return password.length() >= 8;
    }

    public boolean passwordEqualsPasswordConfirmation(String confirmationPassword){
        return password.compareTo(confirmationPassword) == 0;
    }
}
