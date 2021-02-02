package com.example.agensgraph.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;

@Configuration
public class WebAppConfig {

    Statement stmt  =null;

    //初始化变量
    @EventListener
    public void onApplicationEvent(ContextRefreshedEvent event) {
        getStatement();
    }

    public  Statement getStatementInstance() {
        return stmt;
    }

    private Statement getStatement() {
        try{
            Class.forName("net.bitnine.agensgraph.Driver");
            Connection conn = DriverManager.getConnection("jdbc:agensgraph://127.0.0.1:5432/agens","agens","agens");
            stmt  = conn.createStatement();
            return stmt;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

}
