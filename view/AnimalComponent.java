package view;
import model.PlayerColor;
import javax.swing.*;
import java.awt.*;
import model.ChessboardPoint;



/**
 * This is the equivalent of the ChessPiece class,
 * but this class only cares how to draw Chess on ChessboardComponent
 */
public class AnimalComponent extends ChessComponent {
    private PlayerColor owner;
    private boolean selected;

    /*
    private ChessboardPoint position;
    public void setPosition(ChessboardPoint position) {
        this.position = position;
    }
    public ChessboardPoint getPosition(){
        return position;
    }

     */

    private static int bluesEaten = 0;//记录被吃掉的蓝色动物数量
    public static void setBluesEaten(int bluesEaten){AnimalComponent.bluesEaten = bluesEaten;
        System.out.println("蓝棋被吃数量" + AnimalComponent.bluesEaten);
    }
    public static int getBluesEaten(){return bluesEaten;}

    private static int redsEaten = 0;//记录被吃掉的红色动物数量
    public static void setRedsEaten(int redsEaten){AnimalComponent.redsEaten = redsEaten;
    System.out.println("红棋被吃数量" + AnimalComponent.redsEaten);
    }
    public static int getRedsEaten(){return redsEaten;}

    public AnimalComponent(PlayerColor owner, int size) {
        super(owner , size);
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
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        Font font = new Font("楷体", Font.PLAIN, getWidth() / 2);
        g2.setFont(font);
        g2.setColor(owner.getColor());
        g2.drawString(" ", getWidth() / 4, getHeight() * 5 / 8); // FIXME: Use library to find the correct offset.
        if (isSelected()) { // Highlights the model if selected.
            g.setColor(Color.RED);
            g.drawOval(0, 0, getWidth() , getHeight());
        }
    }
}
