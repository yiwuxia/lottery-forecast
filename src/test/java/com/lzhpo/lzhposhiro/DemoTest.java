package com.lzhpo.lzhposhiro;

import org.junit.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Optional;


@SpringBootTest
public class DemoTest {




    @Test
    public  void a(){


        Optional<String> empty=Optional.empty();
        System.out.println(empty.isPresent());


    }

}
