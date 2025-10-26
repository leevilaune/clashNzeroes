package com.onesnzeroes.clashnzeroes.logic.scheduler;

import com.onesnzeroes.clashnzeroes.util.Trace;

import java.io.File;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ImageRemovalScheduler extends DataScheduler{

    private static List<String> images = Collections.synchronizedList(new ArrayList<>());

    public ImageRemovalScheduler(long periodMillis) {
        super(periodMillis);
    }

    @Override
    public void scheduledAction() {
        for(String imagePath : images){
            File imageFile = new File(imagePath);
            if (imageFile.exists()) {
                boolean deleted = imageFile.delete();
                if (deleted) {
                    Trace.info("Image "+ imagePath + " deleteed at " + Instant.now());
                } else {
                    Trace.warn("Failed to delete "+ imagePath + " at " + Instant.now());
                }
            } else {
                Trace.warn(imagePath + " not found at " + imagePath);
            }
        }
    }

    @Override
    public void onFinish() {
        Trace.info("Removed " + images.size() + " images at " + Instant.now());
        images = Collections.synchronizedList(new ArrayList<>());
    }

    public void addImage(String imagePath){
        images.add(imagePath);
    }
}
