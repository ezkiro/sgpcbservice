package com.toyfactory.pcb.service;

import static org.junit.Assert.*;

import javax.annotation.Resource;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class CryptoServiceTest {
	
//    @Autowired
//    @Qualifier("anotherDAO")
//    private SomeDAO someDAO;

    @Resource(name = "crpytoService")
    private CrpytoService crpytoService;

//    @Autowired
//    private SomeController someController;

    @Test
    public void generateHashTest() {
    	String plainText = "12qwaszx";
    	String expectedText = "267f40c803c9906d180ee614dbcb225e84c8082d";
    	String hashText = crpytoService.generateSHA1Hash(plainText);
    	
    	System.out.println("[generateHashTest] hashText:" + hashText);
    	
    	//assertEquals(expectedText,hashText);
    }
	
}
