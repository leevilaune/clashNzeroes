package com.onesnzeroes.clashnzeroes.datasource;

import jakarta.persistence.*;
import java.util.HashMap;
import java.util.Map;

public class MariaDbJpaConnection {

    private static EntityManagerFactory emf = null;
    private static EntityManager em = null;

    public static EntityManager getInstance() {
        try{
            if (em==null) {
                if (emf==null) {
                    Map<String, String> props = new HashMap<>();
                    props.put("jakarta.persistence.jdbc.user", System.getenv("DB_USER"));
                    props.put("jakarta.persistence.jdbc.password", System.getenv("DB_PASSWORD"));
                    props.put("jakarta.persistence.jdbc.url",System.getenv("DB_URL"));
                    emf = Persistence.createEntityManagerFactory("clashPU", props);
                    System.out.println(emf);
                }
                em = emf.createEntityManager();
                System.out.println(em);
            }
        }catch (Exception e){
            System.out.println("fuck me");
            e.printStackTrace();
            return null;
        }
        return em;
    }
}