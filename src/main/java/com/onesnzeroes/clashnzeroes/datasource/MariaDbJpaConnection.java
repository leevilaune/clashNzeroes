package com.onesnzeroes.clashnzeroes.datasource;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import java.util.HashMap;
import java.util.Map;

public class MariaDbJpaConnection {

    private static final EntityManagerFactory emf = createEntityManagerFactory();

    private static EntityManagerFactory createEntityManagerFactory() {
        try {
            Map<String, String> props = new HashMap<>();
            props.put("jakarta.persistence.jdbc.user", System.getenv("DB_USER"));
            props.put("jakarta.persistence.jdbc.password", System.getenv("DB_PASSWORD"));
            props.put("jakarta.persistence.jdbc.url", System.getenv("DB_URL"));
            props.put("jakarta.persistence.jdbc.driver", "org.mariadb.jdbc.Driver");

            EntityManagerFactory factory = Persistence.createEntityManagerFactory("clashPU", props);
            System.out.println("EntityManagerFactory initialized: " + factory);
            return factory;
        } catch (Exception e) {
            System.err.println("Failed to initialize EntityManagerFactory:");
            e.printStackTrace();
            throw new RuntimeException("Could not initialize JPA connection", e);
        }
    }

    public static EntityManager getEntityManager() {
        return emf.createEntityManager();
    }
}