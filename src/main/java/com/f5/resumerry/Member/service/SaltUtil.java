package com.f5.resumerry.Member.service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Service;

@Service
public class SaltUtil {

    public String encodePassword(String salt, String password){
        return BCrypt.hashpw(password,salt);
    }

    public String genSalt(){
        return BCrypt.gensalt();
    }

}