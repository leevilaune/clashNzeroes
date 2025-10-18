package com.onesnzeroes.clashnzeroes.logic.graphic;

import com.onesnzeroes.clashnzeroes.dao.PlayerDao;
import com.onesnzeroes.clashnzeroes.model.player.PlayerEntity;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public class PlayerGraphicManager {

    private PlayerDao dao;

    public PlayerGraphicManager(PlayerDao dao){
        this.dao = dao;
    }

    public void generateChartAsync(String tag, String type) {
        CompletableFuture<Void> future = CompletableFuture.runAsync(() -> generateChart(tag,type))
                .thenRun(() -> System.out.println("Chart generation complete for " + tag))
                .exceptionally(ex -> {
                    ex.printStackTrace();
                    return null;
                });
        future.join();
    }

    private void generateChart(String tag, String field) {
        List<Integer> data = new ArrayList<>();
        if(field.equalsIgnoreCase("trophies")){
            data = dao.findTrophies(tag);
        } else if (field.equalsIgnoreCase("donations")) {
            data = dao.findDonations(tag);
        }
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
        if (data.isEmpty()) {
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
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);
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
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        g.drawString(LocalDateTime.ofInstant(Instant.now(), ZoneOffset.UTC).format(formatter),width-175,25);
        String title = field.substring(0, 1).toUpperCase() + field.substring(1);
        g.drawString(title + " History", 50, 25);
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

        int maxTrophy = data.stream().max(Integer::compareTo).orElse(0);
        int minTrophy = data.stream().min(Integer::compareTo).orElse(0);
        int range = Math.max(25, maxTrophy - minTrophy);
        int margin = (int) (range * 0.05);


        g.setColor(Color.BLACK);
        g.drawString(String.valueOf(maxTrophy),15,40);
        g.drawString(String.valueOf(minTrophy),15,height-60);

        maxTrophy+=margin;
        minTrophy-=margin;
        range = Math.max(25, maxTrophy - minTrophy);

        g.setColor(Color.ORANGE);
        int chartWidth = width - 100;
        int chartHeight = height - 100;
        int n = data.size();

        for (int i = 1; i < n; i++) {
            int x1 = 50 + (i - 1) * chartWidth / (n - 1);
            int x2 = 50 + i * chartWidth / (n - 1);
            int y1 = height - 50 - (data.get(i - 1) - minTrophy) * chartHeight / range;
            int y2 = height - 50 - (data.get(i) - minTrophy) * chartHeight / range;
            g.drawLine(x1, y1, x2, y2);
        }
        drawRotatedCornerText(g,"© onesNzeroes 2025",width,height);
        g.dispose();

        try {
            File outFile = new File(field+"_" + tag + ".png");
            ImageIO.write(image, "png", outFile);
            System.out.println("Chart saved: " + outFile.getAbsolutePath());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private void drawRotatedCornerText(Graphics2D g2d, String text, int imageWidth, int imageHeight) {
        AffineTransform oldTransform = g2d.getTransform();
        Composite oldComposite = g2d.getComposite();

        g2d.setFont(new Font("Arial", Font.PLAIN, 14));
        g2d.setColor(Color.BLACK);
        g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.75f));
        g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

        int margin = 5;

        int x = imageWidth - margin;
        int y = imageHeight - margin;
        g2d.rotate(Math.toRadians(-90), x, y);

        g2d.drawString(text, imageWidth - margin, imageHeight - margin);

        g2d.setTransform(oldTransform);
        g2d.setComposite(oldComposite);
    }
}
