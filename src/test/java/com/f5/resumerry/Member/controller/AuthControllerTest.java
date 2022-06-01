package com.f5.resumerry.Member.controller;

import com.f5.resumerry.Member.domain.dto.SignUpDTO;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.core.AutoConfigureCache;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@AutoConfigureMockMvc
class AuthControllerTest {

    @Autowired
    AuthController authController;

    @Autowired
    MockMvc mockMvc;


    @Test
    void signUp() {
        SignUpDTO signUpDTO = new SignUpDTO();
        signUpDTO.setAccountName("hello");
        try {
            mockMvc.perform((RequestBuilder) authController.signUp(signUpDTO));
        } catch (Exception e) {
            System.out.println(e);
            e.printStackTrace();
        }
    }

    @Test
    void login() {
    }
}