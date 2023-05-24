package view;


import model.PlayerColor;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

/**
 * This is the equivalent of the ChessPiece class,
 * but this class only cares how to draw Chess on ChessboardComponent
 */
public class LeopardChessComponent extends AnimalComponent {
    private PlayerColor owner;

    private boolean selected;
    private BufferedImage imageleopard;

    public LeopardChessComponent(PlayerColor owner, int size) {
        super(owner,size);
        this.owner = owner;
        this.selected = false;
        setSize(size/2, size/2);
        setLocation(0,0);
        setVisible(true);
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }


    @Override
    protected void paintComponent(Graphics g) {
        try {
            if (owner.getColor() == Color.BLUE) {
                File file = new File("src/view/bluehound.png");
                imageleopard = ImageIO.read(file);
            } else if (owner.getColor() == Color.RED) {
                File file = new File("src/view/redhound.png");
                imageleopard = ImageIO.read(file);
            }
        } catch(IOException e){
            e.printStackTrace();
        }
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        if (imageleopard != null) { // 判断是否有导入的图片
            g2.drawImage(imageleopard, 0, 0, getWidth(), getHeight(), null);
        }
        if (isSelected()) { // Highlights the model if selected.
            g.setColor(Color.RED);
            g.drawOval(0, 0, getWidth(), getHeight());
        }
    }
}
