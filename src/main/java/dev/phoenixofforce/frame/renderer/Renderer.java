package dev.phoenixofforce.frame.renderer;

import dev.phoenixofforce.gameobjects.Obstacle;
import dev.phoenixofforce.gameobjects.Player;
import dev.phoenixofforce.raycast.RayCastResult;

import java.awt.image.BufferedImage;
import java.util.List;

public interface Renderer {

    BufferedImage draw(Player player, List<Obstacle> obstacles, List<RayCastResult>[] rayCastResults);

}
