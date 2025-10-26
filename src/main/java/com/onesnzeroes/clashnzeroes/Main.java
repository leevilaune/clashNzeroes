package com.onesnzeroes.clashnzeroes;

import com.onesnzeroes.clashnzeroes.dao.PlayerDao;
import com.onesnzeroes.clashnzeroes.dao.WarDao;
import com.onesnzeroes.clashnzeroes.logic.discord.Bot;
import com.onesnzeroes.clashnzeroes.logic.graphic.WarGraphicManager;
import com.onesnzeroes.clashnzeroes.logic.manager.PlayerManager;
import com.onesnzeroes.clashnzeroes.logic.manager.WarManager;
import com.onesnzeroes.clashnzeroes.logic.graphic.PlayerGraphicManager;
import com.onesnzeroes.clashnzeroes.logic.scheduler.PlayerDataScheduler;
import com.onesnzeroes.clashnzeroes.logic.scheduler.WarDataScheduler;
import com.onesnzeroes.clashnzeroes.util.Trace;

import java.time.Instant;

public class Main {

    public static void main(String[] args) throws Exception {
        WarManager wm = new WarManager();
        WarDataScheduler wds = new WarDataScheduler(wm);
        PlayerManager pm = new PlayerManager(wds);
        PlayerGraphicManager pmg = new PlayerGraphicManager(new PlayerDao(),pm);
        WarGraphicManager wmg = new WarGraphicManager(new WarDao(),new PlayerDao(),pm);
        PlayerDataScheduler scheduler = new PlayerDataScheduler(pm,3600000,wds);
        scheduler.schedule();
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            System.out.println("Shutting down scheduler " + Instant.now());
            scheduler.stop();
        }));
        Bot b = new Bot(wmg,pmg);
        b.start();
    }
}