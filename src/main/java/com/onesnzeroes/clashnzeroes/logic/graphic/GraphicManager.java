package com.onesnzeroes.clashnzeroes.logic.graphic;

import com.onesnzeroes.clashnzeroes.dao.PlayerDao;
import com.onesnzeroes.clashnzeroes.logic.manager.PlayerManager;
import com.onesnzeroes.clashnzeroes.model.TsField;
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

public class GraphicManager {


    protected static final int WIDTH = 600;
    protected static final int HEIGHT = 400;

    private PlayerDao dao;
    private PlayerManager pm;
    private String tag;
    private String title;

    public GraphicManager(PlayerDao dao, PlayerManager pm){
        this.dao = dao;
        this.pm = pm;
    }
    public GraphicManager(PlayerDao dao, String tag, String title){
        this.dao = dao;
        this.tag = tag;
        this.title = title;
    }

    public CompletableFuture<File> generateChartAsync() {
        return CompletableFuture.supplyAsync(() -> {
            try {
                return generateChart();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });
    }

    protected File generateChart() {
        PlayerEntity player = null;
        try {
            player = this.pm.getPlayer(this.tag);
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }
        BufferedImage image = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB);
        Graphics2D g = image.createGraphics();

        createBaseFrame(g, player, this.title);
        drawGraph(g);

        drawRotatedCornerText(g,"[C] onesNzeroes 2025",WIDTH,HEIGHT);
        g.dispose();

        try {
            File outFile = new File(title+"_" + tag + ".png");
            ImageIO.write(image, "png", outFile);
            System.out.println("Chart saved: " + outFile.getAbsolutePath());
            return outFile;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void drawGraph(Graphics2D g){
        g.drawString("Override this method", 250,190);
    }

    private void createBaseFrame(Graphics2D g, PlayerEntity player, String field) {
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
        int badgeWidth = 50;
        int badgeHeight = 50;
        int badgeX = WIDTH - 75;
        int badgeY = HEIGHT - 50;

        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);

        g.setColor(Color.WHITE);
        g.fillRect(0, 0, WIDTH, HEIGHT);

        g.setColor(Color.BLACK);
        g.drawLine(50, HEIGHT - 50, WIDTH - 30, HEIGHT - 50);
        g.drawLine(50, 30, 50, HEIGHT - 50);

        g.setFont(new Font("Dialog", Font.PLAIN, 18));
        g.drawString(player.getName()+player.getTag(),100,HEIGHT-30);
        g.drawString(String.valueOf(player.getTownHallLevel()),125,HEIGHT-10);
        g.drawString(String.valueOf(player.getTrophies()),180,HEIGHT-10);

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        g.drawString(LocalDateTime.ofInstant(Instant.now(), ZoneOffset.UTC).format(formatter),WIDTH-175,25);

        String title = field.substring(0, 1).toUpperCase() + field.substring(1);
        g.drawString(title + " History", 50, 25);

        g.setFont(new Font("Dialog", Font.PLAIN, 18));
        FontMetrics fm = g.getFontMetrics();
        String clanName = player.getClan().getName();
        String clanTag = player.getClan().getTag();
        g.drawString(clanName, badgeX - fm.stringWidth(clanName)-5, HEIGHT-30);
        g.drawString(clanTag, badgeX - fm.stringWidth(clanTag)-5, HEIGHT-10);

        try{
            g.drawImage(ImageIO.read(new URL(player.getLeague().getIconUrls().getSmall())),50,HEIGHT-50,50,50,null);
            g.drawImage(ImageIO.read(new URL("https://static.wikia.nocookie.net/clashofclans/images/c/cd/Trophy.png")),155,HEIGHT-25,20,20,null);
            g.drawImage(ImageIO.read(new URL(townHallImages[player.getTownHallLevel()-1])),100,HEIGHT-25,20,20,null);
            g.drawImage(ImageIO.read(new URL(player.getClan().getBadgeUrls().getSmall())),badgeX, badgeY, badgeWidth, badgeHeight,null);

        }catch (Exception ignored){

        }
        g.setFont(new Font("Dialog", Font.PLAIN, 12));
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

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    protected int chartX(double x) {
        int chartLeft = 50;
        return (int) Math.round(chartLeft + x);
    }

    protected int chartY(double y) {
        int chartTop = 30;
        return (int) Math.round(chartTop + y);
    }
}
