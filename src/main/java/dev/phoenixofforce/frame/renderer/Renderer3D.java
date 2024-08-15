package dev.phoenixofforce.frame.renderer;

import dev.phoenixofforce.Main;
import dev.phoenixofforce.gameobjects.Obstacle;
import dev.phoenixofforce.gameobjects.Player;
import dev.phoenixofforce.math.Line;
import dev.phoenixofforce.math.Util;
import dev.phoenixofforce.math.Vec2D;
import dev.phoenixofforce.raycast.RayCastResult;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.List;

public class Renderer3D implements Renderer {

    private double t = 0;
    private double center = 640 / 2;

    @Override
    public BufferedImage draw(Player player, List<Obstacle> obstacles, List<RayCastResult>[] rayCastResults) {
        int rayCount = rayCastResults.length;
        t += 0.01;
        double sinVal = Math.sin(t) * 10;

        BufferedImage image = new BufferedImage(640, 480, BufferedImage.TYPE_INT_ARGB);
        Graphics graphics = image.getGraphics();

        graphics.setColor(Color.black);
        graphics.fillRect(0, 0, 640, 480);

        final double RAY_WIDTH = (double) image.getWidth() / (double) rayCount;

        for(int i = 0; i < rayCount; i++) {
            for(int j = rayCastResults[i].size() - 1; j >= 0; j--) {
                RayCastResult currentResult = rayCastResults[i].get(j);

                Vec2D rayResult = currentResult.getHitPoint();
                Line hitLine = currentResult.getHitSide();
                Obstacle obstacle = currentResult.getHitObstacle();

                if(rayResult == null || rayResult.distanceTo(player.getPosition()) > Main.MAX_DISTANCE) continue;

                double angleBetweenObjectAndLookingDirection = rayResult.clone().sub(player.getPosition()).radAngle(player.getLookingDirection());
                double objectDistance = rayResult.distanceTo(player.getPosition());
                objectDistance *= Math.abs(Math.cos(angleBetweenObjectAndLookingDirection));	//counteracts fisheye distortion

                double rayHeight = 640 / objectDistance * 10; // the farther away the object is, the smaller it seems

                double playerHeight = Math.max(1, sinVal);   //jump or crouch
                double lookUpDown = 0;//sinVal * 10;
                int bottomYValue = (int) (center + rayHeight / 2 * playerHeight);
                bottomYValue += lookUpDown;
                rayHeight *= obstacle.getHeight();

                Vec2D sideNormal = Vec2D.getOrthogonalVector(hitLine.getDirection());
                double viewingAngleFactor = player.getLookingDirection().clone().normalize().scalar(sideNormal.clone().normalize()) / 2.0 + 0.5;

                Color color = obstacle.getColor(objectDistance + viewingAngleFactor * Main.VIEW_ANGLE_BONUS);
                graphics.setColor(color);
                graphics.fillRect((int) Math.floor(RAY_WIDTH * i), (int) (bottomYValue - rayHeight), (int) Math.ceil(RAY_WIDTH), (int) rayHeight);
            }
        }

        return image;
    }
}
