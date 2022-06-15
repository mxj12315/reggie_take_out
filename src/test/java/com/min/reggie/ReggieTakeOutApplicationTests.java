package com.min.reggie;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.util.DigestUtils;

//@SpringBootTest
class ReggieTakeOutApplicationTests {

    @Test
    void contextLoads() {
       String name =  "dfasdgag-sgaag-agag.jpg";
        int i = name.lastIndexOf(".");
        String substring = name.substring(i);
        System.out.println(substring);

    }

}
