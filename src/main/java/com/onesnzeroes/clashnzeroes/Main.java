package com.onesnzeroes.clashnzeroes;

import com.onesnzeroes.clashnzeroes.dao.PlayerDao;
import com.onesnzeroes.clashnzeroes.logic.manager.PlayerManager;
import com.onesnzeroes.clashnzeroes.logic.manager.WarManager;
import com.onesnzeroes.clashnzeroes.logic.graphic.PlayerGraphicManager;
import com.onesnzeroes.clashnzeroes.logic.scheduler.PlayerDataScheduler;
import com.onesnzeroes.clashnzeroes.logic.scheduler.WarDataScheduler;
import com.onesnzeroes.clashnzeroes.model.war.AttackEntity;
import com.onesnzeroes.clashnzeroes.model.war.WarEntity;

import java.time.Instant;

public class Main {

    public static void main(String[] args) throws Exception {
        PlayerGraphicManager pmg = new PlayerGraphicManager(new PlayerDao());
        WarManager wm = new WarManager();
        WarDataScheduler wds = new WarDataScheduler(wm);
        PlayerManager pm = new PlayerManager(wds);
        PlayerDataScheduler scheduler = new PlayerDataScheduler(pm,3600000,wds);
        //scheduler.schedule();
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            System.out.println("Shutting down scheduler " + Instant.now());
            scheduler.stop();
        }));
        wm.getAttacks("#QUJLCCU").forEach(System.out::println);
        wm.getDefences("#QUJLCCU").forEach(System.out::println);
        //pmg.generateChartAsync("#8L2RQ29G0","trophies");
    }
}