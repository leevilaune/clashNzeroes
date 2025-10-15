package com.onesnzeroes.clashnzeroes.manager;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.onesnzeroes.clashnzeroes.dao.PlayerDao;
import com.onesnzeroes.clashnzeroes.model.PlayerEntity;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URL;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Date;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public class PlayerManager {

    private static final String API_TOKEN = System.getenv("CLASH_TOKEN");
    private static final String BASE_URL = "https://api.clashofclans.com/v1";

    private PlayerDao dao;
    private HttpClient client;
    private ObjectMapper mapper;


    public PlayerManager(){
        this.dao = new PlayerDao();
        this.client = HttpClient.newHttpClient();
        this.mapper = new ObjectMapper();
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

    public List<Integer> getTrophies(String tag){
        return this.dao.findTrophies(tag);
    }

    public void generateTrophyChartAsync(String tag) {
        CompletableFuture<Void> future = CompletableFuture.runAsync(() -> generateTrophyChart(tag))
                .thenRun(() -> System.out.println("Chart generation complete for " + tag))
                .exceptionally(ex -> {
                    ex.printStackTrace();
                    return null;
                });
        future.join();
    }

    private void generateTrophyChart(String tag) {
        List<Integer> trophies = getTrophies(tag);
        PlayerEntity player = this.dao.findLatestByTag(tag);
        String[] townHallImages = {
                "https://static.wikia.nocookie.net/clashofclans/images/f/fd/Town_Hall1.png",
                "https://static.wikia.nocookie.net/clashofclans/images/7/7d/Town_Hall2.png",
                "https://static.wikia.nocookie.net/clashofclans/images/d/dd/Town_Hall3.png",
                "https://static.wikia.nocookie.net/clashofclans/images/e/e7/Town_Hall4.png",
                "https://static.wikia.nocookie.net/clashofclans/images/a/a3/Town_Hall5.png",
                "https://static.wikia.nocookie.net/clashofclans/images/5/52/Town_Hall6.png",
                "https://static.wikia.nocookie.net/clashofclans/images/7/75/Town_Hall7.png",
                "https://static.wikia.nocookie.net/clashofclans/images/f/fa/Town_Hall8.png",
                "https://static.wikia.nocookie.net/clashofclans/images/e/e0/Town_Hall9.png",
                "https://static.wikia.nocookie.net/clashofclans/images/5/5c/Town_Hall10.png",
                "https://static.wikia.nocookie.net/clashofclans/images/9/96/Town_Hall11.png",
                "https://static.wikia.nocookie.net/clashofclans/images/6/64/Town_Hall_12.png",
                "https://static.wikia.nocookie.net/clashofclans/images/c/ca/Town_Hall_13.png",
                "https://static.wikia.nocookie.net/clashofclans/images/0/0d/Town_Hall_14.png",
                "https://static.wikia.nocookie.net/clashofclans/images/d/d4/Town_Hall15.png",
                "https://static.wikia.nocookie.net/clashofclans/images/5/53/Town_Hall16.png",
                "https://static.wikia.nocookie.net/clashofclans/images/2/24/Town_Hall17-1.png"
        };
        if (trophies.isEmpty()) {
            System.out.println("No trophies found for tag " + tag);
            return;
        }

        int width = 600;
        int height = 400;
        int badgeWidth = 50;
        int badgeHeight = 50;
        int badgeX = width - 75;
        int badgeY = height - 50;
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        Graphics2D g = image.createGraphics();

        g.setColor(Color.WHITE);
        g.fillRect(0, 0, width, height);
        g.setColor(Color.BLACK);
        //g.drawString(player.getName() + player.getTag() + " (TH" + player.getTownHallLevel() + ") - " + player.getClan().getName(), 50, 20);
        g.drawLine(50, height - 50, width - 30, height - 50);
        g.drawLine(50, 30, 50, height - 50);
        g.setFont(new Font("Dialog", Font.PLAIN, 18));
        g.drawString(player.getName()+player.getTag(),100,height-30);
        g.drawString(String.valueOf(player.getTownHallLevel()),125,height-10);
        g.drawString(String.valueOf(player.getTrophies()),180,height-10);

        g.setFont(new Font("Dialog", Font.PLAIN, 18));
        FontMetrics fm = g.getFontMetrics();
        String clanName = player.getClan().getName();
        String clanTag = player.getClan().getTag();
        g.drawString(clanName, badgeX - fm.stringWidth(clanName)-5, height-30);
        g.drawString(clanTag, badgeX - fm.stringWidth(clanTag)-5, height-10);

        try{
            g.drawImage(ImageIO.read(new URL(player.getLeague().getIconUrls().getSmall())),50,height-50,50,50,null);
            g.drawImage(ImageIO.read(new URL("https://static.wikia.nocookie.net/clashofclans/images/c/cd/Trophy.png")),155,height-25,20,20,null);
            g.drawImage(ImageIO.read(new URL(townHallImages[player.getTownHallLevel()-1])),100,height-25,20,20,null);
            g.drawImage(ImageIO.read(new URL(player.getClan().getBadgeUrls().getSmall())),badgeX, badgeY, badgeWidth, badgeHeight,null);

        }catch (Exception ignored){

        }
        g.setFont(new Font("Dialog", Font.PLAIN, 12));

        int maxTrophy = trophies.stream().max(Integer::compareTo).orElse(0);
        int minTrophy = trophies.stream().min(Integer::compareTo).orElse(0);
        int range = Math.max(1, maxTrophy - minTrophy);

        g.setColor(Color.BLACK);
        g.drawString(String.valueOf(maxTrophy),25,40);
        g.drawString(String.valueOf(minTrophy),25,height-50);

        minTrophy-=10;
        g.setColor(Color.ORANGE);
        int chartWidth = width - 100;
        int chartHeight = height - 100;
        int n = trophies.size();

        for (int i = 1; i < n; i++) {
            int x1 = 50 + (i - 1) * chartWidth / (n - 1);
            int x2 = 50 + i * chartWidth / (n - 1);
            int y1 = height - 50 - (trophies.get(i - 1) - minTrophy) * chartHeight / range;
            int y2 = height - 50 - (trophies.get(i) - minTrophy) * chartHeight / range;
            g.drawLine(x1, y1, x2, y2);
        }

        g.dispose();

        try {
            File outFile = new File("trophies_" + tag + ".png");
            ImageIO.write(image, "png", outFile);
            System.out.println("Chart saved: " + outFile.getAbsolutePath());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
