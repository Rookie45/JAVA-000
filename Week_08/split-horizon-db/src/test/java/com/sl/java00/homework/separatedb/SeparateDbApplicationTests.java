package com.sl.java00.homework.separatedb;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;

//@SpringBootTest
class SeparateDbApplicationTests {

//    @Test
    void contextLoads() {
    }

    public static void main(String[] args) {
        for (int i = 0; i < 32; i ++) {
            int db = i % 2;
            int table = ((i / 16) + i) % 16;
            System.out.println("i=" + i + "||" +db + "-" + table);
        }
    }

}
