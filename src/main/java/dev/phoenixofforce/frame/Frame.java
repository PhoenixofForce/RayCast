package dev.phoenixofforce.frame;

import dev.phoenixofforce.gameobjects.Player;
import dev.phoenixofforce.frame.control.KeyInputs;
import dev.phoenixofforce.math.Vec2D;
import lombok.Data;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

@Data
public class Frame {

    public KeyInputs keyInputs;
    public Vec2D lastDrag = new Vec2D();
    public Vec2D clickPosition;

    private JFrame frame;
    private Player player;

    public Frame(Player player) {
        frame = new JFrame("");
        frame.setResizable(false);
        frame.setLayout(null);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
        Insets insets = frame.getInsets();
        frame.setSize(640 * 2 + 5 + insets.left + insets.right, 480 + insets.top + insets.bottom);

        keyInputs = new KeyInputs();
        addControls(frame);

        this.player = player;
    }

    public void addControls(JFrame frame) {
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

}
