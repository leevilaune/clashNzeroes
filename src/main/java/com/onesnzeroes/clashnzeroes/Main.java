package com.onesnzeroes.clashnzeroes;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.onesnzeroes.clashnzeroes.dao.PlayerDao;
import com.onesnzeroes.clashnzeroes.model.PlayerEntity;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class Main {
    private static final String API_TOKEN = System.getenv("CLASH_TOKEN");
    private static final String BASE_URL = "https://api.clashofclans.com/v1";

    public static void main(String[] args) throws Exception {
        String playerTag = "#QCPYVQRJ2";
        String encodedTag = playerTag.replace("#", "%23");

        HttpClient client = HttpClient.newHttpClient();

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + "/players/" + encodedTag))
                .header("Authorization", "Bearer " + API_TOKEN)
                .header("Accept", "application/json")
                .GET()
                .build();

        System.out.println(request.headers());

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        System.out.println("Response code: " + response.statusCode());
        ObjectMapper mapper = new ObjectMapper();
        System.out.println(response.body());
        PlayerEntity player = mapper.readValue(response.body(), PlayerEntity.class);
        //System.out.println(player);
        PlayerDao dao = new PlayerDao();
        dao.persist(player);

        System.out.println(dao.findByTag(playerTag));
    }
}