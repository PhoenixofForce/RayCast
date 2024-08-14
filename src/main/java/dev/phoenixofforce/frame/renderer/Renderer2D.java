package dev.phoenixofforce.frame.renderer;

import dev.phoenixofforce.Main;
import dev.phoenixofforce.gameobjects.Obstacle;
import dev.phoenixofforce.gameobjects.Player;
import dev.phoenixofforce.math.Vec2D;
import dev.phoenixofforce.raycast.RayCastResult;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.List;

public class Renderer2D implements Renderer {

    @Override
    public BufferedImage draw(Player player, List<Obstacle> obstacles, List<RayCastResult>[] rayCastResults) {
        BufferedImage image = new BufferedImage(640, 480, BufferedImage.TYPE_INT_ARGB);
        Graphics graphics = image.getGraphics();

        graphics.setColor(Color.black);
        graphics.fillRect(0, 0, 640, 480);

        //draw rays
        graphics.setColor(Color.WHITE);
        for (List<RayCastResult> rayCastResult : rayCastResults) {
            if (rayCastResult.isEmpty() || rayCastResult.get(0).getHitPoint() == null) continue;

            double angleBetweenObjectAndLookingDirection = player.getLookingDirection().radAngle(rayCastResult.get(0).getHitPoint().clone().sub(player.getPosition()));
            float objectDistance = (float) rayCastResult.get(0).getHitPoint().distanceTo(player.getPosition());
            objectDistance *= Math.abs(Math.cos(angleBetweenObjectAndLookingDirection));
            objectDistance /= Main.MAX_DISTANCE;
            objectDistance = 1 - objectDistance;
            objectDistance = Math.max(0, objectDistance);

            graphics.setColor(new Color(objectDistance, objectDistance, objectDistance));
            graphics.drawLine((int) player.getPosition().x, (int) player.getPosition().y, (int) rayCastResult.get(0).getHitPoint().x, (int) rayCastResult.get(0).getHitPoint().y);
        }

        //draw players looking direction
        graphics.setColor(Color.GREEN.brighter());
        Vec2D playerHeading = player.getPosition().clone().add(player.getLookingDirection().clone().mult(50));
        graphics.drawLine((int) player.getPosition().x, (int) player.getPosition().y, (int) playerHeading.x, (int) playerHeading.y);

        //draw obstacles
        for(Obstacle p: obstacles) {
            graphics.setColor(p.getColor());
            graphics.drawPolygon(p.getPolygon());
        }

        //draw player
        graphics.setColor(Color.RED.brighter());
        graphics.fillOval((int) player.getPosition().x - 5, (int) player.getPosition().y - 5, 10, 10);

        return image;
    }
}
