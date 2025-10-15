package com.onesnzeroes.clashnzeroes;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.onesnzeroes.clashnzeroes.dao.PlayerDao;
import com.onesnzeroes.clashnzeroes.manager.PlayerDataScheduler;
import com.onesnzeroes.clashnzeroes.manager.PlayerManager;
import com.onesnzeroes.clashnzeroes.model.PlayerEntity;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Instant;

public class Main {

    public static void main(String[] args) throws Exception {
        PlayerManager pm = new PlayerManager();
        PlayerDataScheduler scheduler = new PlayerDataScheduler(pm);
        scheduler.startEvenHourSave();
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            System.out.println("Shutting down scheduler " + Instant.now());
            scheduler.stop();
        }));
        //pm.savePlayers();
        //pm.generateTrophyChartAsync("#QCPYVQRJ2");
        //pm.generateTrophyChartAsync("#8L2RQ29G0");
        //pm.generateTrophyChartAsync("#QUJLCCU");
        //pm.savePlayer("#QCPYVQRJ2");
        //pm.savePlayer("#8L2RQ29G0");
        //pm.savePlayer("#QUJLCCU");
    }
}