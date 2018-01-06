package com.henallux.moveandseeandroid.Enumeration;

/**
 * Created by Alexandre on 04-01-18.
 */

public enum RegistrationCodeErrorEnumeration {
    OK (2000),
    BAD_PSEUDO (4001),
    BAD_EMAIL (4002),
    BAD_PASSWORD (4003),
    PASSWORD_MISS_MATCH (4004)
    ;

    public final int errorCode;

    RegistrationCodeErrorEnumeration(int errorCode){
        this.errorCode = errorCode;
    }
}

