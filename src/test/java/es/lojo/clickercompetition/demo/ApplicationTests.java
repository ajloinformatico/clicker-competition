package es.lojo.clickercompetition.demo;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class ApplicationTests {

    @BeforeAll //STATIC it is called before any test

    @BeforeEach //Prepare the tests only one

    @AfterAll //STATIC it is called after all tests

    @AfterEach //Its called after one test

    @Test //interpret as test
    void contextLoads() {
    }

}
