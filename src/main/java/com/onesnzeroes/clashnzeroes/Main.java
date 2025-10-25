package com.onesnzeroes.clashnzeroes;

import com.onesnzeroes.clashnzeroes.dao.PlayerDao;
import com.onesnzeroes.clashnzeroes.dao.WarDao;
import com.onesnzeroes.clashnzeroes.logic.graphic.WarGraphicManager;
import com.onesnzeroes.clashnzeroes.logic.manager.PlayerManager;
import com.onesnzeroes.clashnzeroes.logic.manager.WarManager;
import com.onesnzeroes.clashnzeroes.logic.graphic.PlayerGraphicManager;
import com.onesnzeroes.clashnzeroes.logic.scheduler.PlayerDataScheduler;
import com.onesnzeroes.clashnzeroes.logic.scheduler.WarDataScheduler;
import com.onesnzeroes.clashnzeroes.model.war.AttackEntity;
import com.onesnzeroes.clashnzeroes.model.war.WarEntity;
import com.onesnzeroes.clashnzeroes.util.Trace;

import java.time.Instant;

public class Main {

    public static void main(String[] args) throws Exception {
        PlayerGraphicManager pmg = new PlayerGraphicManager(new PlayerDao());
        WarGraphicManager wmg = new WarGraphicManager(new WarDao(),new PlayerDao());
        WarManager wm = new WarManager();
        WarDataScheduler wds = new WarDataScheduler(wm);
        PlayerManager pm = new PlayerManager(wds);
        PlayerDataScheduler scheduler = new PlayerDataScheduler(pm,3600000,wds);
        //scheduler.schedule();
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            System.out.println("Shutting down scheduler " + Instant.now());
            scheduler.stop();
        }));
        //wm.getAttacks("#QUJLCCU").forEach(System.out::println);
        pmg.setTag("#8L2RQ29G0");
        pmg.setField("donations");
        String playerTag = "#QCPYVQRJ2";
        wmg.setTag(playerTag);
        wmg.setTitle("war");
        //pmg.generateChartAsync();
        wmg.generateChartAsync();
        wm.getDefences(playerTag).forEach(d -> Trace.info(d.toString()));
        wm.getAttacks(playerTag).forEach(a -> Trace.info(a.toString()));


    }
}