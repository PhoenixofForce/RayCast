package dev.phoenixofforce.gameobjects;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class ObstacleGenerator {

    public static List<Obstacle> createObstacles() {
        List<Obstacle> polygons = new ArrayList<>();
        for(int x = 0; x < 32; x+=4) {
            for(int y = 0; y < 24; y+=4) {
                Polygon p = new Polygon();
                p.addPoint(x*20, y*20);
                p.addPoint(x*20+20, y*20);
                p.addPoint(x*20+20, y*20+20);
                p.addPoint(x*20, y*20+20);

                polygons.add(new Obstacle(p, new Color(new Random().nextInt()), (float) Math.random() * 5));
            }
        }

        Polygon border = new Polygon();
        border.addPoint(0, 0);
        border.addPoint(0, 480);
        border.addPoint(640, 480);
        border.addPoint(640, 0);
        polygons.add(new Obstacle(border, Color.WHITE, 2));

        return polygons;
    }

}
