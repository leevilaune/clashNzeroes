package com.onesnzeroes.clashnzeroes;

import com.onesnzeroes.clashnzeroes.dao.PlayerDao;
import com.onesnzeroes.clashnzeroes.logic.manager.PlayerManager;
import com.onesnzeroes.clashnzeroes.logic.manager.WarManager;
import com.onesnzeroes.clashnzeroes.logic.graphic.PlayerGraphicManager;
import com.onesnzeroes.clashnzeroes.logic.scheduler.PlayerDataScheduler;
import com.onesnzeroes.clashnzeroes.logic.scheduler.WarDataScheduler;
import com.onesnzeroes.clashnzeroes.model.war.WarEntity;

import java.time.Instant;

public class Main {

    public static void main(String[] args) throws Exception {
        PlayerGraphicManager pmg = new PlayerGraphicManager(new PlayerDao());
        WarManager wm = new WarManager();
        WarDataScheduler wds = new WarDataScheduler(wm);
        PlayerManager pm = new PlayerManager(wds);
        PlayerDataScheduler scheduler = new PlayerDataScheduler(pm,3600000,wds);
        scheduler.schedule();
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            System.out.println("Shutting down scheduler " + Instant.now());
            scheduler.stop();
        }));

        //pm.savePlayer("#G0Y0GRLGQ");
        //pm.verifyPlayerStatus("#G0Y0GRLGQ","akpezsdg");
        //pm.savePlayers();
        //wm.saveCurrentWar(clanTag);

        //pm.savePlayers();
        /*
        pmg.generateChartAsync("#QCPYVQRJ2","donations");
        pmg.generateChartAsync("#8L2RQ29G0","donations");
        pmg.generateChartAsync("#QUJLCCU","donations");
        pmg.generateChartAsync("#QCPYVQRJ2","trophies");
        pmg.generateChartAsync("#8L2RQ29G0","trophies");
        pmg.generateChartAsync("#QUJLCCU","trophies");

         */


        //pm.generateTrophyChartAsync("#8L2RQ29G0");
        //pm.generateTrophyChartAsync("#QUJLCCU");
        //pm.savePlayer("#QCPYVQRJ2");
        //pm.savePlayer("#8L2RQ29G0");
        //pm.savePlayer("#QUJLCCU");
    }
}