package dev.phoenixofforce;

import dev.phoenixofforce.gameobjects.Obstacle;
import dev.phoenixofforce.gameobjects.ObstacleGenerator;
import dev.phoenixofforce.gameobjects.Player;
import dev.phoenixofforce.frame.Frame;
import dev.phoenixofforce.frame.renderer.Renderer;
import dev.phoenixofforce.frame.renderer.Renderer2D;
import dev.phoenixofforce.frame.renderer.Renderer3D;
import dev.phoenixofforce.math.Vec2D;
import dev.phoenixofforce.raycast.RayCast;
import dev.phoenixofforce.raycast.RayCastResult;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

public class Main {

	public static final int FOV  = 90;
	public static final int RAY_COUNT = 320;
	public static final double MAX_DISTANCE = 600;
	public static final double VIEW_ANGLE_BONUS = MAX_DISTANCE / 3;

	public static Player player;
	public static List<Obstacle> obstacles;

	private static Frame frame;
	private static Renderer render2D;
	private static Renderer render3D;

	public static void main(String[] args) {
		render2D = new Renderer2D();
		render3D = new Renderer3D();

		player = new Player(250, 250);
		frame = new Frame(player);

		obstacles = ObstacleGenerator.createObstacles();
		runThread();
	}

	public static void runThread() {
		JFrame jFrame = frame.getFrame();
		Insets insets = jFrame.getInsets();

		new Thread(() -> {
			Graphics g2 = jFrame.getGraphics();
			BufferedImage img = new BufferedImage(640 * 2 + 5, 480, BufferedImage.TYPE_INT_ARGB);
			Graphics g = img.getGraphics();

			long lastTitleUpdate = System.currentTimeMillis();
			long lastUpdate = System.currentTimeMillis();
			long fps = 0;
			while(true) {
				long dt = System.currentTimeMillis() - lastUpdate;
				if(dt != 0) fps = 1000 / dt;
				lastUpdate = System.currentTimeMillis();

				if(System.currentTimeMillis() - lastTitleUpdate > 500) {
					frame.getFrame().setTitle("RayCast (" + fps + "fps)");
					lastTitleUpdate = System.currentTimeMillis();
				}

				List<RayCastResult>[] rayCastResults = new ArrayList[RAY_COUNT];
				Vec2D rayHead = player.getLookingDirection().clone().degRotate(-( FOV / 2.0 ));
				for(int i = 0; i < RAY_COUNT; i++) {
					rayCastResults[i] = RayCast.castRayIn(obstacles, player.getPosition(), rayHead);
					rayHead.degRotate(((double) FOV / (double) RAY_COUNT));
				}

				g.drawImage(render2D.draw(player, obstacles, rayCastResults), 0, 0, null);
				g.drawImage(render3D.draw(player, obstacles, rayCastResults), 640 + 5, 0, null);

				g2.drawImage(img, insets.left, insets.top, null);

				player.update(frame.getKeyInputs(), obstacles);
			}
		}).start();
	}
}