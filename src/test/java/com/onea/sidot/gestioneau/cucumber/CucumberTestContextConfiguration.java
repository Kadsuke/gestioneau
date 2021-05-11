package com.onea.sidot.gestioneau.cucumber;

import com.onea.sidot.gestioneau.GestioneauApp;
import io.cucumber.spring.CucumberContextConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.web.WebAppConfiguration;

@CucumberContextConfiguration
@SpringBootTest(classes = GestioneauApp.class)
@WebAppConfiguration
public class CucumberTestContextConfiguration {}
