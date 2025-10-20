package com.onesnzeroes.clashnzeroes.logic.manager;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.onesnzeroes.clashnzeroes.dao.WarDao;
import com.onesnzeroes.clashnzeroes.logic.scheduler.WarDataScheduler;
import com.onesnzeroes.clashnzeroes.model.war.WarEntity;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class WarManager {

    private static final String API_TOKEN = System.getenv("CLASH_TOKEN");
    private static final String BASE_URL = "https://api.clashofclans.com/v1";

    private final WarDao dao;
    private final HttpClient client;
    private final ObjectMapper mapper;

    public WarManager() {
        this.dao = new WarDao();
        this.client = HttpClient.newHttpClient();
        this.mapper = new ObjectMapper();
    }

    public void saveCurrentWar(String clanTag) throws IOException, InterruptedException {
        String encodedTag = clanTag.replace("#", "%23");

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + "/clans/" + encodedTag + "/currentwar"))
                .header("Authorization", "Bearer " + API_TOKEN)
                .header("Accept", "application/json")
                .GET()
                .build();

        HttpResponse<String> response = this.client.send(request, HttpResponse.BodyHandlers.ofString());

        System.out.println("Response code: " + response.statusCode());
        System.out.println(response.body());

        WarEntity war = mapper.readValue(response.body(), WarEntity.class);
        this.dao.saveOrUpdate(war);
    }

    public WarEntity getCurrentWar(String clanTag) throws IOException, InterruptedException {
        String encodedTag = clanTag.replace("#", "%23");

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + "/clans/" + encodedTag + "/currentwar"))
                .header("Authorization", "Bearer " + API_TOKEN)
                .header("Accept", "application/json")
                .GET()
                .build();

        HttpResponse<String> response = this.client.send(request, HttpResponse.BodyHandlers.ofString());

        System.out.println("Response code: " + response.statusCode());
        System.out.println(response.body());

        return mapper.readValue(response.body(), WarEntity.class);
    }

    public void saveWarFromLog(String clanTag, int index) throws IOException, InterruptedException {
        String encodedTag = clanTag.replace("#", "%23");

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + "/clans/" + encodedTag + "/warlog?limit=" + (index + 1)))
                .header("Authorization", "Bearer " + API_TOKEN)
                .header("Accept", "application/json")
                .GET()
                .build();

        HttpResponse<String> response = this.client.send(request, HttpResponse.BodyHandlers.ofString());

        System.out.println("Response code: " + response.statusCode());
        System.out.println(response.body());
    }

}