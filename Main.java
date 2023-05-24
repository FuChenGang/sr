import controller.GameController;
import model.Chessboard;
import view.ChessGameFrame;

import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            ChessGameFrame mainFrame = new ChessGameFrame(1100, 810);
            //GameController gameController = new GameController(mainFrame.getChessboardComponent(), new Chessboard());
            mainFrame.setGameController(new GameController(mainFrame.getChessboardComponent(), new Chessboard()));
            mainFrame.getGameController().setFrame(mainFrame);//用于实现游戏结束展示Winner功能
            mainFrame.setVisible(true);
        });
    }
}
//000000