import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;

import java.util.*;
import java.util.List;

public class Main {

	public static final int FOV  = 90;
	public static final int RAY_COUNT = 720;
	public static final double MAX_DISTANCE = 600;
	public static final int OUTLINE_THICKNESS = 5;

	public static Vec2D lastDrag = new Vec2D();
	public static Vec2D clickPosition;

	public static Map<Integer, Boolean> keyInputs;
	public static Player player;

	public static List<Obstacle> polygons;

	public static void main(String[] args) {
		JFrame frame = new JFrame("RayCast");
		frame.setResizable(false);
		frame.setLayout(null);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);
		Insets insets = frame.getInsets();
		frame.setSize(640*2+5 + insets.left + insets.right, 480 + insets.top + insets.bottom);

		keyInputs = new HashMap<>();
		player = new Player(250, 250);

		addControls(frame);
		polygons = createObstacles();
		runThread(frame);
	}

	public static List<Obstacle> createObstacles() {
		List<Obstacle> polygons = new ArrayList<>();
		for(int x = 0; x < 32; x+=4) {
			for(int y = 0; y < 24; y+=4) {
				Polygon p = new Polygon();
				p.addPoint(x*20, y*20);
				p.addPoint(x*20+20, y*20);
				p.addPoint(x*20+20, y*20+20);
				p.addPoint(x*20, y*20+20);

				polygons.add(new Obstacle(p, new Color(new Random().nextInt()), (float) Math.random(), Math.random() < 0.25));
			}
		}

		Polygon border = new Polygon();
		border.addPoint(0, 0);
		border.addPoint(0, 480);
		border.addPoint(640, 480);
		border.addPoint(640, 0);
		polygons.add(new Obstacle(border));

		return polygons;
	}

	public static void runThread(JFrame frame) {
		Insets insets = frame.getInsets();

		new Thread(()->{
			Graphics g2 = frame.getGraphics();
			BufferedImage img = new BufferedImage(640*2+5, 480, BufferedImage.TYPE_INT_ARGB);
			Graphics g = img.getGraphics();

			while(true) {
				List<Pair>[] rayCastResults = new ArrayList[RAY_COUNT];
				Vec2D rayHead = player.getLookingDirection().clone().degRotate(-(FOV/2.0));
				for(int i = 0; i < RAY_COUNT; i++) {
					rayCastResults[i] = RayCast.castRayIn(polygons, player.getPosition(), rayHead);
					rayHead.degRotate(((double) FOV / (double) RAY_COUNT));
				}

				g.drawImage(draw2D(rayCastResults), 0, 0, null);
				g.drawImage(draw3D(rayCastResults), 640+5, 0, null);

				g2.drawImage(img, insets.left, insets.top, null);

				player.update(keyInputs);
			}
		}).start();
	}

	public static BufferedImage draw3D(List<Pair>[] rayCastResults) {
		BufferedImage image = new BufferedImage(640, 480, BufferedImage.TYPE_INT_ARGB);
		Graphics graphics = image.getGraphics();

		graphics.setColor(Color.black);
		graphics.fillRect(0, 0, 640, 480);

		final double RAY_WIDTH = (double) image.getWidth() / (double) RAY_COUNT;

		for(int i = 0; i < RAY_COUNT; i++) {
			for(int j = rayCastResults[i].size() - 1; j >= 0; j--) {
				Vec2D rayResult = rayCastResults[i].get(j).point;
				Obstacle obstacle = rayCastResults[i].get(j).obstacle;

				if(rayResult == null || rayResult.distanceTo(player.getPosition()) > MAX_DISTANCE) continue;

				double angleBetweenObjectAndLookingDirection = player.getLookingDirection().radAngle(rayResult.clone().sub(player.getPosition()));
				double objectDistance = rayResult.distanceTo(player.getPosition());
				objectDistance *= Math.abs(Math.cos(angleBetweenObjectAndLookingDirection));	//counteracts fisheye distortion

				float objectHeight = obstacle.getHeight();
				int rayHeight = (int) Math.ceil( map(objectDistance, 0, MAX_DISTANCE, image.getHeight(), 0));	// the farther away the object is, the smaller it seems

				int yEnd = (int) Math.floor(((double) image.getHeight() - rayHeight)/2.0) + rayHeight;
				rayHeight = (int) (rayHeight * objectHeight);

				//ray hit outline of obstacle if within OUTLINE_THICKNESS rays lies a ray that hit a different object
				Color color = obstacle.getColor(objectDistance);
				if(obstacle.isHighlighted()) {
					for(int k = 1; k < OUTLINE_THICKNESS; k++) {
						if(i >= k && i < RAY_COUNT - k) {
							if(rayCastResults[i-k].stream().noneMatch(p -> p.obstacle == obstacle) || rayCastResults[i+k].stream().noneMatch(p -> p.obstacle == obstacle)) color = color.brighter();
						}
					}
				}

				graphics.setColor(color);
				graphics.fillRect((int) Math.floor(RAY_WIDTH * i), yEnd - rayHeight, (int) Math.ceil(RAY_WIDTH), rayHeight);

				//draw outline
				if(obstacle.isHighlighted()) {
					for(int k = 1; k < OUTLINE_THICKNESS; k++) {
						color = color.brighter();
						graphics.setColor(color);

						graphics.fillRect((int) Math.floor(RAY_WIDTH * i), yEnd - rayHeight, (int) Math.ceil(RAY_WIDTH), OUTLINE_THICKNESS - k);
						graphics.fillRect((int) Math.floor(RAY_WIDTH * i), yEnd - OUTLINE_THICKNESS + k, (int) Math.ceil(RAY_WIDTH), OUTLINE_THICKNESS - k);
					}
				}
			}
		}

		return image;
	}

	public static BufferedImage draw2D(List<Pair>[] rayCastResults) {
		BufferedImage image = new BufferedImage(640, 480, BufferedImage.TYPE_INT_ARGB);
		Graphics graphics = image.getGraphics();

		graphics.setColor(Color.black);
		graphics.fillRect(0, 0, 640, 480);

		//draw rays
		graphics.setColor(Color.WHITE);
		for (List<Pair> rayCastResult : rayCastResults) {
			if (rayCastResult.size() == 0 || rayCastResult.get(0).point == null) continue;
			graphics.drawLine((int) player.getPosition().x, (int) player.getPosition().y, (int) rayCastResult.get(0).point.x, (int) rayCastResult.get(0).point.y);
		}

		//draw players looking direction
		graphics.setColor(Color.GREEN.brighter());
		Vec2D playerHeading = player.getPosition().clone().add(player.getLookingDirection().clone().mult(50));
		graphics.drawLine((int) player.getPosition().x, (int) player.getPosition().y, (int) playerHeading.x, (int) playerHeading.y);

		//draw obstacles
		graphics.setColor(Color.cyan);
		for(Obstacle p: polygons) graphics.drawPolygon(p.getPolygon());

		//draw player
		graphics.setColor(Color.RED.brighter());
		graphics.fillOval((int) player.getPosition().x - 5, (int) player.getPosition().y - 5, 10, 10);

		return image;
	}

	public static void addControls(JFrame frame) {
		frame.getContentPane().addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				clickPosition = new Vec2D(e.getX(), e.getY());

				//left click sets position, right click sets looking direction
				if(SwingUtilities.isLeftMouseButton(e)) {
					player.setPosition(clickPosition);
				} else if(SwingUtilities.isRightMouseButton(e)) {
					player.setLookingDirection(clickPosition.clone().sub(player.getPosition()).normalize());
				}
			}
		});

		frame.getContentPane().addMouseMotionListener(new MouseMotionAdapter() {
			@Override
			public void mouseDragged(MouseEvent e) {
				clickPosition = new Vec2D(e.getX(), e.getY());

				//left sets position, right sets looking direction, middle rotates
				if(SwingUtilities.isLeftMouseButton(e)) {
					player.setPosition(clickPosition);
				} else if(SwingUtilities.isRightMouseButton(e)) {
					player.setLookingDirection(clickPosition.clone().sub(player.getPosition()).normalize());
				} else {
					player.setLookingDirection(player.getLookingDirection().degRotate(clickPosition.x - lastDrag.x));
				}

				lastDrag = new Vec2D(e.getX(), e.getY());
			}

			@Override
			public void mouseMoved(MouseEvent e) {
				lastDrag = new Vec2D(e.getX(), e.getY());
			}
		});

		frame.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				keyInputs.put(e.getKeyCode(), true);
			}

			@Override
			public void keyReleased(KeyEvent e) {
				keyInputs.put(e.getKeyCode(), false);
			}
		});
	}

	public static double map(double a, double f1, double t1, double f2, double t2) {
		return ((a-f1)/(t1-f1))*(t2-f2)+f2;
	}
}
