package com.onesnzeroes.clashnzeroes.logic.manager;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.onesnzeroes.clashnzeroes.dao.PlayerDao;
import com.onesnzeroes.clashnzeroes.logic.scheduler.WarDataScheduler;
import com.onesnzeroes.clashnzeroes.model.player.PlayerEntity;
import com.onesnzeroes.clashnzeroes.model.war.WarEntity;
import com.onesnzeroes.clashnzeroes.util.Trace;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class PlayerManager {

    private static final String API_TOKEN = System.getenv("CLASH_TOKEN");
    private static final String BASE_URL = "https://api.clashofclans.com/v1";

    private PlayerDao dao;
    private HttpClient client;
    private ObjectMapper mapper;

    private WarDataScheduler warScheduler;

    public PlayerManager(WarDataScheduler warScheduler){
        this.dao = new PlayerDao();
        this.client = HttpClient.newHttpClient();
        this.mapper = new ObjectMapper();
        this.warScheduler = warScheduler;
    }

    public void savePlayer(String playerTag) throws IOException, InterruptedException {
        String encodedTag = playerTag.replace("#", "%23");

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + "/players/" + encodedTag))
                .header("Authorization", "Bearer " + API_TOKEN)
                .header("Accept", "application/json")
                .GET()
                .build();

        HttpResponse<String> response = this.client.send(request, HttpResponse.BodyHandlers.ofString());

        System.out.println("Response code: " + response.statusCode());
        System.out.println(response.body());
        PlayerEntity player = mapper.readValue(response.body(), PlayerEntity.class);
        String clanTag = player.getClan().getTag();
        WarEntity we = warScheduler.getWarManager().getCurrentWar(clanTag);
        if(we.getState() == null){
            Trace.warn("Clan " + clanTag + " doesnt have public war log");
        }else{
            warScheduler.scheduleWarRecording(clanTag,we.getEndTime());
        }
        this.dao.persist(player);
    }


    public void savePlayers(){
        this.dao.findUniquePlayerTags().forEach(tag -> {
            try {
                savePlayer(tag);
            } catch (IOException e) {
                throw new RuntimeException(e);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
    }

    public String verifyPlayerStatus(String playerTag, String token) throws IOException, InterruptedException {
        String encodedTag = playerTag.replace("#", "%23");

        String requestBody = String.format("{\"token\":\"%s\"}", token);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + "/players/" + encodedTag + "/verifytoken"))
                .header("Authorization", "Bearer " + API_TOKEN)
                .header("Accept", "application/json")
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(requestBody))
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        System.out.println("Verify Response code: " + response.statusCode());
        System.out.println(response.body());

        JsonNode jsonNode = mapper.readTree(response.body());
        return jsonNode.path("status").asText();
    }
}
