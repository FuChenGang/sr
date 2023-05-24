package controller;


import listener.GameListener;
import model.*;
import view.*;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Stack;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import static model.Constant.CHESSBOARD_COL_SIZE;
import static model.Constant.CHESSBOARD_ROW_SIZE;

/**
 * Controller is the connection between model and view,
 * when a Controller receive a request from a view, the Controller
 * analyzes and then hands over to the model for processing
 * [in this demo the request methods are onPlayerClickCell() and onPlayerClickChessPiece()]
 *
*/
public class GameController implements GameListener {
    private ChessGameFrame frame;//用于实现游戏结束展示Winner功能


    private ChessboardPoint[] movableCell = new ChessboardPoint[7];
    //用于存储被选中的棋子能走的位置，内含可能重复，最大重复数量为7
    //num为非空个数：
    int num = 0;

    private Chessboard model;
    private ChessboardComponent view;
    private PlayerColor currentPlayer;
    public PlayerColor getCurrentPlayer(){
        return currentPlayer;
    }

    private String winner = null;
    public void setRedWins(){winner = "Red";currentPlayer = null;System.out.println( );System.out.println("red wins");}
    public void setBlueWins(){winner = "Blue";currentPlayer = null;System.out.println( );System.out.println("blue wins");}
    public String getWinner(){return winner;}

    // Record whether there is a selected piece before
    private ChessboardPoint selectedPoint;
    private void setSelectedPointNull(){
        if(selectedPoint != null) {
            if(view.getChessComponentAtGrid(selectedPoint)!=null){
            view.getChessComponentAtGrid(selectedPoint).setSelected(false);
            }
            selectedPoint = null;
        }
    }

    //用于实现悔棋功能：
    //记录上一步的起始点和目标点：
    private ChessboardPoint start;//起始点
    private ChessboardPoint target;//目标点
    //记录起始点和目标点原本的棋子：
    private ChessComponent chessAtStart;
    private ChessComponent chessAtTarget;
    private ChessPiece chessPieceAtStart;
    private ChessPiece chessPieceAtTarget;
    PlayerColor eatOrNot = null;    //记录上一回合是否有动物被吃，null：无；RED：红色被吃；BLUE：蓝色被吃
    Stack huiqi = new Stack<>();//记录行棋内容
    Object[] step = new Object[7];//记录每一步行棋内容

    //用于实现AI模式
    private boolean AIModeOrNot = false;
    private boolean AIModeGreedyOrNot = false;

    public void setAIModeGreedyOrNot(boolean AIModeGreedyOrNot) {
        this.AIModeGreedyOrNot = AIModeGreedyOrNot;
    }

    public GameController(ChessboardComponent view, Chessboard model) {
        this.view = view;
        this.model = model;
        this.currentPlayer = PlayerColor.BLUE;

        view.registerController(this);
        //initialize();
        view.initiateChessComponent(model);
        view.repaint();
        //System.out.println(currentPlayer + "这个restart到底是怎么回事");//测试restart
    }


    /*private void initialize() {
        for (int i = 0; i < Constant.CHESSBOARD_ROW_SIZE.getNum(); i++) {
            for (int j = 0; j < Constant.CHESSBOARD_COL_SIZE.getNum(); j++) {

            }
        }
    }

     */



    // after a valid move swap the player
    private void swapColor() {
        currentPlayer = currentPlayer == PlayerColor.BLUE ? PlayerColor.RED : PlayerColor.BLUE;
    }

    private boolean win() {
        // TODO: Check the board if there is a winner
        if(winner.equals(null)){System.out.println("null");
            return false;}
        else {System.out.println(winner);
            return true;}
    }

    public ChessboardComponent getView(){
        return view;
    }

    public Chessboard getModel(){
        return model;
    }


    // click an empty cell

//定义chessboard用于校验兽穴与陷阱位置
ChessboardPoint trapblue1 = new ChessboardPoint(8 , 2);
ChessboardPoint trapblue2 = new ChessboardPoint(8 , 4);
ChessboardPoint trapblue3 = new ChessboardPoint(7 , 3);
ChessboardPoint trapred1 = new ChessboardPoint(0 , 2);
ChessboardPoint trapred2 = new ChessboardPoint(0 , 4);
ChessboardPoint trapred3 = new ChessboardPoint(1 , 3);
ChessboardPoint DenBlue = new ChessboardPoint(8 , 3);
ChessboardPoint DenRed = new ChessboardPoint(0 , 3);
    @Override
    public void onPlayerClickCell(ChessboardPoint point, CellComponent component) {
        if (selectedPoint != null && model.isValidMove(selectedPoint, point)) {
            //选中非空且移动合法

            //用于实现悔棋功能：
            eatOrNot = null;//重置
            step = new Object[7];//重置

            //统计被吃的数量,用于胜利判断
            if(model.getChessPieceAt(point) != null){//目的地非空
                if(model.getChessPieceAt(point).getName().equals("陷阱") ||
                        model.getChessPieceAt(point).getName().equals("兽穴")){//目的地为陷阱或兽穴
                    //啥也不干
                }else {//目的地为动物，即目的地动物被吃掉
                    if (model.getChessPieceAt(point).getOwner().equals(PlayerColor.RED)){
                        //目标为红色
                        AnimalComponent.setRedsEaten(AnimalComponent.getRedsEaten() + 1);
                        //红色被吃的棋子加一
                        eatOrNot = PlayerColor.RED;//该回合红色被吃
                    }else {//目标为蓝色
                        AnimalComponent.setBluesEaten(AnimalComponent.getBluesEaten() + 1);
                        //蓝色被吃的棋子加一
                        eatOrNot = PlayerColor.BLUE;//该回合蓝色被吃
                    }
                }
            }
            //测试被吃子：
            //System.out.println("蓝色被吃掉的动物数量"+AnimalComponent.getBluesEaten());
            //System.out.println("红色被吃掉的动物数量"+AnimalComponent.getRedsEaten());

            //用于实现悔棋功能：
            start = selectedPoint;//记录起始点
            target = point;//记录目标点
            chessPieceAtStart = model.getChessPieceAt(start);//记录起始点原本棋子
            chessPieceAtTarget = model.getChessPieceAt(target);//记录目标点原本棋子
            chessAtStart = view.getChessComponentAtGrid(start);//记录起始点原本棋子
            chessAtTarget = view.getChessComponentAtGrid(target);//记录目标点原本棋子
                //将记录数据存入step：
            step[0] = start;
            step[1] = target;
            step[2] = chessPieceAtStart;
            step[3] = chessPieceAtTarget;
            step[4] = chessAtStart;
            step[5] = chessAtTarget;
            step[6] = eatOrNot;
            huiqi.add(step);//将step存入huiqi
            //System.out.println("悔棋stack的长度："+huiqi.size());//测试


            //执行移动棋子操作：
            model.moveChessPiece(selectedPoint, point);//对chessboard的操作
            view.setChessComponentAtGrid(point, view.removeChessComponentAtGrid(selectedPoint));//对chessboardcomponent的操作
            model.getChessPieceAt(point).setPosition(point);//更新棋子位置

            if(selectedPoint.equals(trapred3) || selectedPoint.equals(trapred2) || selectedPoint.equals(trapred1)){
                //蓝方动物跳出红方陷阱时恢复原来的rank，并将原位置恢复为陷阱
                view.setChessComponentAtGrid(selectedPoint, new TrapChessComponent(PlayerColor.RED,view.getCHESS_SIZE()));
                model.getChessPieceAt(point).setRank(model.getChessPieceAt(point).getOriginalRank());
            }
            if( selectedPoint.equals(trapblue1) || selectedPoint.equals(trapblue2) || selectedPoint.equals(trapblue3)){
                //红方跳出蓝方陷阱时恢复原来的rank，并将原位置恢复为陷阱
                view.setChessComponentAtGrid(selectedPoint, new TrapChessComponent(PlayerColor.BLUE,view.getCHESS_SIZE()));
                model.getChessPieceAt(point).setRank(model.getChessPieceAt(point).getOriginalRank());
            }

            setSelectedPointNull();//走一步后选中的位置为空
            swapColor();//交换行棋颜色
            view.repaint();

            //进入陷阱的特殊设置
            if (model.getChessPieceAt(point).getOwner().equals(PlayerColor.RED)){
                if(point.equals(trapblue1) || point.equals(trapblue2) ||point.equals(trapblue3)){
                    model.getChessPieceAt(point).setRank(0);
                }
            } else if (model.getChessPieceAt(point).getOwner().equals(PlayerColor.BLUE)) {
                if(point.equals(trapred1) || point.equals(trapred2) ||point.equals(trapred3)){
                    model.getChessPieceAt(point).setRank(0);
                }
            }//进入陷阱时陷阱和被吃掉一样



            //两阵营被吃的棋子数量统计见该方法开头
            //胜利判断：
            //1.有无剩余棋子：
            if (AnimalComponent.getRedsEaten() == 8){setBlueWins();System.out.println("红子被吃光，蓝方胜");}//红子被吃光，蓝方胜
            if (AnimalComponent.getBluesEaten() == 8){setRedWins();System.out.println("蓝子被吃光，红方胜");}//蓝子被吃光，红方胜
            //2.进入对方兽穴：
            if (point.equals(DenRed)){setBlueWins();System.out.println("蓝色棋进入红方兽穴，蓝方胜");}//进入红方兽穴，蓝方胜
            if (point.equals(DenBlue)){setRedWins();System.out.println("红色棋进入红方兽穴，红方胜");}//进入蓝方兽穴，红方胜
            //3.困死：
            ChessboardPoint[] posiRed = new ChessboardPoint[12];
            ChessboardPoint[] posiBlue = new ChessboardPoint[12];
            int blue = 0;//记录红棋数量
            int red = 0;//记录蓝棋数量
            for (int i = 0; i < CHESSBOARD_ROW_SIZE.getNum(); i++) {//遍历棋盘,并记录棋子位置
                for (int j = 0; j < CHESSBOARD_COL_SIZE.getNum(); j++) {
                    if(model.getChessPieceAt(new ChessboardPoint(i,j)) != null){//非空
                        if (model.getChessPieceAt(new ChessboardPoint(i,j)).getOwner().equals(PlayerColor.RED)){
                            //判断为红色棋子，记录位置，红棋数量+1，
                            posiRed[red] = new ChessboardPoint(i,j);
                            red++;
                        }else {//判断为蓝色棋子，记录位置，蓝棋数量+1
                            posiBlue[blue] = new ChessboardPoint(i,j);
                            blue++;
                        }
                    }
                }
            }
            boolean blueCanMove = false;
            boolean redCanMove = false;
            //判定是否困死：
            //红色：
            for (int i = 0; i < red; i++) {//第i个红色棋子
                if(posiRed[i].getCol() > 0 && posiRed[i].getCol() < 6){//中间的列：
                    if(model.isValidMove(posiRed[i] , new ChessboardPoint(posiRed[i].getRow(),posiRed[i].getCol() - 1))
                            || model.isValidMove(posiRed[i] , new ChessboardPoint(posiRed[i].getRow(),posiRed[i].getCol() + 1)))
                    //if(model.isValidMove(posiRed[i] , new ChessboardPoint(posiRed[i].getRow() - 1,posiRed[i].getCol()))
                    //        || model.isValidMove(posiRed[i] , new ChessboardPoint(posiRed[i].getRow() + 1,posiRed[i].getCol())))
                    {//左右任一能动，则未被困死
                        redCanMove = true;
                        break;
                    }
                }
                if(posiRed[i].getRow() > 0 && posiRed[i].getRow() < 8){//中间的行：
                    if(model.isValidMove(posiRed[i] , new ChessboardPoint(posiRed[i].getRow() - 1,posiRed[i].getCol()))
                            || model.isValidMove(posiRed[i] , new ChessboardPoint(posiRed[i].getRow() + 1,posiRed[i].getCol())))
                    //if(model.isValidMove(posiRed[i] , new ChessboardPoint(posiRed[i].getRow(),posiRed[i].getCol() - 1))
                    //        || model.isValidMove(posiRed[i] , new ChessboardPoint(posiRed[i].getRow(),posiRed[i].getCol() + 1)))
                    {//上下任一能动，则未被困死
                        redCanMove = true;
                        break;
                    }
                }
                if(posiRed[i].getRow() == 0){//第一行：
                    if(model.isValidMove(posiRed[i] , new ChessboardPoint(posiRed[i].getRow() + 1,posiRed[i].getCol())))
                    {//向下能动，则未被困死
                        redCanMove = true;
                        break;
                    }
                }
                if(posiRed[i].getCol() == 0){//第一列：
                    if(model.isValidMove(posiRed[i] , new ChessboardPoint(posiRed[i].getRow(),posiRed[i].getCol() + 1)))
                    {//向右能动，则未被困死
                        redCanMove = true;
                        break;
                    }
                }
                if(posiRed[i].getRow() == 8){//最后一行：
                    if(model.isValidMove(posiRed[i] , new ChessboardPoint(posiRed[i].getRow() - 1,posiRed[i].getCol())))
                    {//向上能动，则未被困死
                        redCanMove = true;
                        break;
                    }
                }
                if(posiRed[i].getCol() == 6){//最后一列：
                    if(model.isValidMove(posiRed[i] , new ChessboardPoint(posiRed[i].getRow(),posiRed[i].getCol() - 1)))
                    {//向左能动，则未被困死
                        redCanMove = true;
                        break;
                    }
                }
                if (model.getChessPieceAt(posiRed[i]).getName().equals("狮") ||
                        model.getChessPieceAt(posiRed[i]).getName().equals("虎")){
                    //狮虎
                    if(model.isValidMove(posiRed[i] , model.riverSide1 ) ||
                            model.isValidMove(posiRed[i] , model.riverSide2 )||
                            model.isValidMove(posiRed[i] , model.riverSide3 )||
                            model.isValidMove(posiRed[i] , model.riverSide4 )||
                            model.isValidMove(posiRed[i] , model.riverSide5 )||
                            model.isValidMove(posiRed[i] , model.riverSide6 )||
                            model.isValidMove(posiRed[i] , model.riverSide7 )||
                            model.isValidMove(posiRed[i] , model.riverSide8 )||
                            model.isValidMove(posiRed[i] , model.riverSide9 )||
                            model.isValidMove(posiRed[i] , model.riverSides1 )||
                            model.isValidMove(posiRed[i] , model.riverSides1 )||
                            model.isValidMove(posiRed[i] , model.riverSides3 )||
                            model.isValidMove(posiRed[i] , model.riverSides4 )||
                            model.isValidMove(posiRed[i] , model.riverSides5 )||
                            model.isValidMove(posiRed[i] , model.riverSides6 )||
                            model.isValidMove(posiRed[i] , model.riverSides7 )||
                            model.isValidMove(posiRed[i] , model.riverSides8 ))
                    {//能动，则未被困死
                        redCanMove = true;
                        break;
                    }
                }
            }
            //蓝色：
            for (int i = 0; i < blue; i++) {//第i个蓝色棋子
                if(posiBlue[i].getCol() > 0 && posiBlue[i].getCol() < 6){//中间的列：
                    if(model.isValidMove(posiBlue[i] , new ChessboardPoint(posiBlue[i].getRow(),posiBlue[i].getCol() - 1))
                            || model.isValidMove(posiBlue[i] , new ChessboardPoint(posiBlue[i].getRow(),posiBlue[i].getCol() + 1)))
                    //if(model.isValidMove(posiRed[i] , new ChessboardPoint(posiRed[i].getRow() - 1,posiRed[i].getCol()))
                    //        || model.isValidMove(posiRed[i] , new ChessboardPoint(posiRed[i].getRow() + 1,posiRed[i].getCol())))
                    {//左右任一能动，则未被困死
                        blueCanMove = true;
                        break;
                    }
                }
                if(posiBlue[i].getRow() > 0 && posiBlue[i].getRow() < 8){//中间的行：
                    if(model.isValidMove(posiBlue[i] , new ChessboardPoint(posiBlue[i].getRow() - 1,posiBlue[i].getCol()))
                            || model.isValidMove(posiBlue[i] , new ChessboardPoint(posiBlue[i].getRow() + 1,posiBlue[i].getCol())))
                    //if(model.isValidMove(posiRed[i] , new ChessboardPoint(posiRed[i].getRow(),posiRed[i].getCol() - 1))
                    //        || model.isValidMove(posiRed[i] , new ChessboardPoint(posiRed[i].getRow(),posiRed[i].getCol() + 1)))
                    {//上下任一能动，则未被困死
                        blueCanMove = true;
                        break;
                    }
                }
                if(posiBlue[i].getRow() == 0){//第一行：
                    if(model.isValidMove(posiBlue[i] , new ChessboardPoint(posiBlue[i].getRow() + 1,posiBlue[i].getCol())))
                    {//向下能动，则未被困死
                        blueCanMove = true;
                        break;
                    }
                }
                if(posiBlue[i].getCol() == 0){//第一列：
                    if(model.isValidMove(posiBlue[i] , new ChessboardPoint(posiBlue[i].getRow(),posiBlue[i].getCol() + 1)))
                    {//向右能动，则未被困死
                        blueCanMove = true;
                        break;
                    }
                }
                if(posiBlue[i].getRow() == 8){//最后一行：
                    if(model.isValidMove(posiBlue[i] , new ChessboardPoint(posiBlue[i].getRow() - 1,posiBlue[i].getCol())))
                    {//向上能动，则未被困死
                        blueCanMove = true;
                        break;
                    }
                }
                if(posiBlue[i].getCol() == 6){//最后一列：
                    if(model.isValidMove(posiBlue[i] , new ChessboardPoint(posiBlue[i].getRow(),posiBlue[i].getCol() - 1)))
                    {//向左能动，则未被困死
                        blueCanMove = true;
                        break;
                    }
                }
                if (model.getChessPieceAt(posiBlue[i]).getName().equals("狮") ||
                        model.getChessPieceAt(posiBlue[i]).getName().equals("虎"))
                {//狮虎
                    if (model.isValidMove(posiBlue[i], model.riverSide1) ||
                            model.isValidMove(posiBlue[i], model.riverSide2) ||
                            model.isValidMove(posiBlue[i], model.riverSide3) ||
                            model.isValidMove(posiBlue[i], model.riverSide4) ||
                            model.isValidMove(posiBlue[i], model.riverSide5) ||
                            model.isValidMove(posiBlue[i], model.riverSide6) ||
                            model.isValidMove(posiBlue[i], model.riverSide7) ||
                            model.isValidMove(posiBlue[i], model.riverSide8) ||
                            model.isValidMove(posiBlue[i], model.riverSide9) ||
                            model.isValidMove(posiBlue[i], model.riverSides1) ||
                            model.isValidMove(posiBlue[i], model.riverSides1) ||
                            model.isValidMove(posiBlue[i], model.riverSides3) ||
                            model.isValidMove(posiBlue[i], model.riverSides4) ||
                            model.isValidMove(posiBlue[i], model.riverSides5) ||
                            model.isValidMove(posiBlue[i], model.riverSides6) ||
                            model.isValidMove(posiBlue[i], model.riverSides7) ||
                            model.isValidMove(posiBlue[i], model.riverSides8)) {//能动，则未被困死
                        redCanMove = true;
                        break;
                    }
                }
            }

            if (redCanMove == false){setBlueWins();
                System.out.println("红方被困死，蓝方胜");//测试
            }//红色被困死，蓝色胜
            if (blueCanMove == false){setRedWins();
                System.out.println("蓝方被困死，红方胜");//测试
            }//蓝色被困死，红色胜

            /*//测试：
            for (int i = 0; i < 9; i++) {
                for (int j = 0; j < 7; j++) {
                    if (view.getChessComponentAtGrid(new ChessboardPoint(i,j))!=null)
                    {String a =  model.getChessPieceAt(new ChessboardPoint(i,j)).getName();
                        System.out.print(a);}
                }
                System.out.println();
            }
             */

        //添加AI模式自动走子：
            //System.out.println(currentPlayer);
            if (AIModeOrNot == true && currentPlayer == PlayerColor.RED){//随机板AI
                for (int i = 0; i < 9 ; i++) {
                    boolean a = false;//用于结束循环
                    for (int j = 0; j < 7 ; j++) {
                        JComponent clickedComponent = (JComponent) view.getComponentAt(i,j);//循环遍历
                        ChessPiece chessPiece = model.getChessPieceAt(new ChessboardPoint(i,j));
                        if (chessPiece == null) {//遍历处无AI棋子:啥也不干
                            System.out.print(" None chess here and "+ i + j);
                            //this.onPlayerClickCell(new ChessboardPoint(i , j), (CellComponent) clickedComponent);
                        } else {//遍历处有AI棋子
                            System.out.print("One chess here and ");
                            this.onPlayerClickCell(new ChessboardPoint(i , j), (CellComponent) clickedComponent);
                            this.onPlayerClickChessPiece(new ChessboardPoint(i , j), view.getChessComponentAtGrid(new ChessboardPoint(i,j)));
                            if (num != 0){//有可走的位置
                                double r = num * Math.random();
                                int randomNumber = (int)r;
                                int c = movableCell[randomNumber].getRow();
                                int d = movableCell[randomNumber].getCol();
                                JComponent clickedComponent1 = (JComponent) view.getComponentAt(c,d);
                                this.onPlayerClickCell(movableCell[randomNumber], (CellComponent) clickedComponent1);
                                if( clickedComponent1.getComponentCount() != 0) {
                                    this.onPlayerClickChessPiece(movableCell[randomNumber], (ChessComponent) clickedComponent1.getComponents()[0]);}
                                setSelectedPointNull();
                                a = true;//用于结束遍历
                                break;
                            }
                            //else {//该棋子不能走
                            //    selectedPoint = null;
                            //}
                        }
                        setSelectedPointNull();
                        this.frame.repaint();
                    }
                    if (a == true){break;}//结束遍历
                }
            //System.out.println(currentPlayer);


            }else if(AIModeGreedyOrNot == true && currentPlayer == PlayerColor.RED){//贪心板AI
                //用于记录第一个能走的点（：
                double r;
                int randomNumber = 0;
                int c;
                int d;
                JComponent clickedComponent1 = null;
                //记录起始点：
                JComponent clickedComponent2 = null;
                int i1 = 0;
                int j1 = 0;

                boolean a = false;//用于结束循环
                //）
                for (int i = 0; i < 9 ; i++) {
                    for (int j = 0; j < 7 ; j++) {
                        JComponent clickedComponent = (JComponent) view.getComponentAt(i,j);//循环遍历
                        ChessPiece chessPiece = model.getChessPieceAt(new ChessboardPoint(i,j));
                        if (chessPiece == null) {//遍历处无AI棋子:啥也不干
                            System.out.print(" None chess here and "+ i + j);
                            //this.onPlayerClickCell(new ChessboardPoint(i , j), (CellComponent) clickedComponent);
                        } else {//遍历处有AI棋子
                            System.out.print("One chess here and ");
                            this.onPlayerClickCell(new ChessboardPoint(i , j), (CellComponent) clickedComponent);
                            this.onPlayerClickChessPiece(new ChessboardPoint(i , j), view.getChessComponentAtGrid(new ChessboardPoint(i,j)));

                            if (num != 0){//有可走的位置先随机记录一个
                                r = num * Math.random();
                                randomNumber = (int)r;
                                c = movableCell[randomNumber].getRow();
                                d = movableCell[randomNumber].getCol();
                                clickedComponent1 = (JComponent) view.getComponentAt(c,d);
                                clickedComponent2 = clickedComponent;
                                i1 = i;
                                j1 = j;

                                //循环遍历所有能走的地方，看有无能吃
                                int k = 0;
                                while (k < num){
                                    if(model.getChessPieceAt(movableCell[k]) == null ||
                                        model.getChessPieceAt(movableCell[k]).getOwner() == PlayerColor.RED ||
                                        model.getChessPieceAt(movableCell[k]).getName().equals("陷阱")){//没得吃
                                    k++;}else {//有得吃
                                    c = movableCell[k].getRow();
                                    d = movableCell[k].getCol();
                                    clickedComponent1 = (JComponent) view.getComponentAt(c,d);
                                    a=true;
                                    break;
                                    }
                                }

                                if(a == true) {
                                    this.onPlayerClickCell(movableCell[k], (CellComponent) clickedComponent1);
                                    if (clickedComponent1.getComponentCount() != 0) {
                                        this.onPlayerClickChessPiece(movableCell[k], (ChessComponent) clickedComponent1.getComponents()[0]);
                                    }
                                    setSelectedPointNull();
                                    component.repaint();
                                    a = true;//用于结束遍历
                                    break;
                                }else {setSelectedPointNull();
                                    component.repaint();}
                            }
                        }
                        setSelectedPointNull();
                        component.repaint();
                    }
                    if (a == true){break;}//结束遍历
                }
                if(a == false){//没有能吃的子
                    this.onPlayerClickCell(new ChessboardPoint(i1 , j1), (CellComponent) clickedComponent2);
                    this.onPlayerClickChessPiece(new ChessboardPoint(i1 , j1), view.getChessComponentAtGrid(new ChessboardPoint(i1,j1)));
                    this.onPlayerClickCell(movableCell[randomNumber], (CellComponent) clickedComponent1);
                    if( clickedComponent1.getComponentCount() != 0) {
                        this.onPlayerClickChessPiece(movableCell[randomNumber], (ChessComponent) clickedComponent1.getComponents()[0]);}
                    setSelectedPointNull();
                    a = true;//用于结束遍历
                }
            }
            this.frame.repaint();



            // TODO: if the chess enter Dens or Traps and so on
        }
        else if(selectedPoint == null && model.getChessPieceAt(point) == null){
            //选中空且点击处无棋子，非法点击
            System.out.println("Illegal!");
        }else if (selectedPoint == null && model.getChessPieceAt(point).getName().equals("陷阱")){
            //选中空且点击处为陷阱，非法点击
            System.out.println("Illegal!");
        }else if (selectedPoint == null && model.getChessPieceAt(point).getName().equals("兽穴")){
            //选中空且点击处为兽穴，非法点击
            System.out.println("Illegal!");
        }else if(selectedPoint == null && model.getChessPieceAt(point).getOwner().equals(currentPlayer)){
            //选中空且点击处能被选中，啥也不干
        } else if (selectedPoint != null && selectedPoint.equals(point)) {
            //选中非空且点击处与选中相同，啥也不干
        } else {System.out.println("Illegal!");}//非法点击
        //{throw new IllegalArgumentException("Illegal!");}
        frame.addCurrentPlayer();
        //frame.setVisible(true);
    }

    // click a cell with a chess
    @Override
    public void onPlayerClickChessPiece(ChessboardPoint point, ChessComponent component) {
        //点击时执行选中与取消选中
        if (model.getChessPieceAt(point) != null && (
                model.getChessPieceAt(point).getName().equals("陷阱") ||
                model.getChessPieceAt(point).getName().equals("兽穴")))
        {
            component.setSelected(false);//不能选中陷阱和兽穴
        } else if (selectedPoint == null) {
            if(model.getChessPieceOwner(point) != null) {
                if (model.getChessPieceOwner(point).equals(currentPlayer)) {
                    selectedPoint = point;
                    component.setSelected(true);//与当前玩家同色动物可被选中
                    component.repaint();
                }
            }
        } else if (selectedPoint.equals(point)) {//取消选中
            setSelectedPointNull();
            component.setSelected(false);
            component.repaint();
        }


        //添加可走位置显示的功能：可走位置存储在数组movable中
        for (int i = 0; i <6 ; i++) {//重置movableCell（将能走的位置先设置为空）
            movableCell[i] = null;
        }
        int a = 0;//用于表示放进movableCell数组的chessboardPoint数量（含重复），先重置为0
        if(selectedPoint != null) {//如果存在被选中的点
            //设置被选中的棋子能移动的位置的列和行：
            int r = point.getRow();//point的行
            int c = point.getCol();//point的列
            //被选中的棋子的上下左右四个chessboardpoint
            ChessboardPoint up = new ChessboardPoint(r-1 , c);
            ChessboardPoint down = new ChessboardPoint(r+1 , c);
            ChessboardPoint left = new ChessboardPoint(r , c-1);
            ChessboardPoint right = new ChessboardPoint(r , c+1);
            //判断上下左右四个格子能不能走，若能走，填入movableCell
            if (r - 1 >= 0) {if (model.isValidMove(selectedPoint,up)){
                movableCell[a] = up;
                a++;}
            }
            if (r + 1 <= 8) {if (model.isValidMove(selectedPoint,down)){
                movableCell[a] = down;
                a++;}
            }
            if (c - 1 >= 0) {if (model.isValidMove(selectedPoint,left)){
                movableCell[a] = left;
                a++;}
            }
            if (c + 1 <= 6) {if (model.isValidMove(selectedPoint,right)){
                movableCell[a] = right;
                a++;}
            }
            if (model.getChessPieceAt(selectedPoint).getName().equals("狮") ||
                    model.getChessPieceAt(selectedPoint).getName().equals("虎")){
                //选中棋子为狮虎时的特殊判断（河边位置的判断）：
                if (model.isValidMove(selectedPoint, model.riverSide1)){
                    movableCell[a] = model.riverSide1;
                    a++;}
                if (model.isValidMove(selectedPoint, model.riverSide2)){
                    movableCell[a] = model.riverSide2;
                    a++;}
                if (model.isValidMove(selectedPoint, model.riverSide3)){
                    movableCell[a] = model.riverSide3;
                    a++;}
                if (model.isValidMove(selectedPoint, model.riverSide4)){
                    movableCell[a] = model.riverSide4;
                    a++;}
                if (model.isValidMove(selectedPoint, model.riverSide5)){
                    movableCell[a] = model.riverSide5;
                    a++;}
                if (model.isValidMove(selectedPoint, model.riverSide6)){
                    movableCell[a] = model.riverSide6;
                    a++;}
                if (model.isValidMove(selectedPoint, model.riverSide7)){
                    movableCell[a] = model.riverSide7;
                    a++;}
                if (model.isValidMove(selectedPoint, model.riverSide8)){
                    movableCell[a] = model.riverSide8;
                    a++;}
                if (model.isValidMove(selectedPoint, model.riverSide9)){
                    movableCell[a] = model.riverSide9;
                    a++;}
                if (model.isValidMove(selectedPoint, model.riverSides1)){
                    movableCell[a] = model.riverSides1;
                    a++;}
                if (model.isValidMove(selectedPoint, model.riverSides2)){
                    movableCell[a] = model.riverSides2;
                    a++;}
                if (model.isValidMove(selectedPoint, model.riverSides3)){
                    movableCell[a] = model.riverSides3;
                    a++;}
                if (model.isValidMove(selectedPoint, model.riverSides4)){
                    movableCell[a] = model.riverSides4;
                    a++;}
                if (model.isValidMove(selectedPoint, model.riverSides5)){
                    movableCell[a] = model.riverSides5;
                    a++;}
                if (model.isValidMove(selectedPoint, model.riverSides6)){
                    movableCell[a] = model.riverSides6;
                    a++;}
                if (model.isValidMove(selectedPoint, model.riverSides7)){
                    movableCell[a] = model.riverSides7;
                    a++;}
                if (model.isValidMove(selectedPoint, model.riverSides8)){
                    movableCell[a] = model.riverSides8;
                    a++;}
            }
        }

        //测试可走位置显示的功能：
        //System.out.println(a);
        //num为非空个数：
        num=0;//重置
        if (movableCell[0]!=null){System.out.println(movableCell[0].movable());num = 1;}
        if (movableCell[1]!=null){System.out.println(movableCell[1].movable());num = 2;}
        if (movableCell[2]!=null){System.out.println(movableCell[2].movable());num = 3;}
        if (movableCell[3]!=null){System.out.println(movableCell[3].movable());num = 4;}
        if (movableCell[4]!=null){System.out.println(movableCell[4].movable());num = 5;}
        if (movableCell[5]!=null){System.out.println(movableCell[5].movable());num = 6;}

        //component.repaint();
    }



    public void giveUp() {//认输功能
        if (currentPlayer == PlayerColor.BLUE) {
            setRedWins();
            System.out.println("蓝方认输，红方胜");//测试
        }
        if (currentPlayer == PlayerColor.RED) {
            setBlueWins();
            System.out.println("红方认输，蓝方胜");//测试
        }
    }


    /*
            step[0] = start;
            step[1] = target;
            step[2] = chessPieceAtStart;
            step[3] = chessPieceAtTarget;
            step[4] = chessAtStart;
            step[5] = chessAtTarget;
            step[6] = eatOrNot;
     */
    public void undo(){//悔棋功能：
        if (huiqi.size() > 0){
        Object[] step = (Object[])huiqi.pop();
        ChessboardPoint start;
        ChessboardPoint target;
        ChessPiece chessPieceAtStart;
        ChessPiece chessPieceAtTarget;
        ChessComponent chessAtStart;
        ChessComponent chessAtTarget;
        PlayerColor eatOrNot;
        start = (ChessboardPoint)step[0];
        target = (ChessboardPoint)step[1];
        chessPieceAtStart = (ChessPiece)step[2];
        chessPieceAtTarget = (ChessPiece)step[3];
        chessAtStart = (ChessComponent)step[4];
        chessAtTarget = (ChessComponent)step[5];
        eatOrNot = (PlayerColor)step[6];
        if (eatOrNot == PlayerColor.RED) {//上一回合红色被吃
            AnimalComponent.setRedsEaten(AnimalComponent.getRedsEaten() - 1);
            //红色被吃的棋子减一
        }    else if (eatOrNot == PlayerColor.BLUE) {//上一回合蓝色被吃
            AnimalComponent.setBluesEaten(AnimalComponent.getBluesEaten() - 1);
            //蓝色被吃的棋子减一
        }
        model.setChessPiece(start , chessPieceAtStart);
        model.setChessPiece(target , chessPieceAtTarget);
        view.setChessComponentAtGrid(start , chessAtStart);
        view.setChessComponentAtGrid(target , chessAtTarget);
        setSelectedPointNull();//选中的位置为空
        swapColor();//交换行棋颜色
        view.repaint();
        }else {
            System.out.println("illegal");
        }
        if(AIModeOrNot == true){//AI模式按一次悔棋两边都往前退一步
            Object[] step = (Object[])huiqi.pop();
            ChessboardPoint start;
            ChessboardPoint target;
            ChessPiece chessPieceAtStart;
            ChessPiece chessPieceAtTarget;
            ChessComponent chessAtStart;
            ChessComponent chessAtTarget;
            PlayerColor eatOrNot;
            start = (ChessboardPoint)step[0];
            target = (ChessboardPoint)step[1];
            chessPieceAtStart = (ChessPiece)step[2];
            chessPieceAtTarget = (ChessPiece)step[3];
            chessAtStart = (ChessComponent)step[4];
            chessAtTarget = (ChessComponent)step[5];
            eatOrNot = (PlayerColor)step[6];
            if (eatOrNot == PlayerColor.RED) {//上一回合红色被吃
                AnimalComponent.setRedsEaten(AnimalComponent.getRedsEaten() - 1);
                //红色被吃的棋子减一
            }    else if (eatOrNot == PlayerColor.BLUE) {//上一回合蓝色被吃
                AnimalComponent.setBluesEaten(AnimalComponent.getBluesEaten() - 1);
                //蓝色被吃的棋子减一
            }
            model.setChessPiece(start , chessPieceAtStart);
            model.setChessPiece(target , chessPieceAtTarget);
            view.setChessComponentAtGrid(start , chessAtStart);
            view.setChessComponentAtGrid(target , chessAtTarget);
            setSelectedPointNull();//选中的位置为空
            swapColor();//交换行棋颜色
            view.repaint();
        }
        this.frame.repaint();
        //System.out.println("undo");//测试
        //System.out.println("悔棋stack的长度："+huiqi.size());//测试

        /*
        if (eatOrNot == PlayerColor.RED) {//上一回合红色被吃
            AnimalComponent.setRedsEaten(AnimalComponent.getRedsEaten() - 1);
            //红色被吃的棋子减一
        }    else if (eatOrNot == PlayerColor.BLUE) {//上一回合蓝色被吃
            AnimalComponent.setBluesEaten(AnimalComponent.getBluesEaten() - 1);
            //蓝色被吃的棋子减一
        }
        model.setChessPiece(start , chessPieceAtStart);
        model.setChessPiece(target , chessPieceAtTarget);
        view.setChessComponentAtGrid(start , chessAtStart);
        view.setChessComponentAtGrid(target , chessAtTarget);
        selectedPoint = null;//选中的位置为空
        swapColor();//交换行棋颜色
        view.repaint();
         */
    }
    public void restart(){
        model.removeAllPieces();
        model.initPieces();
        view.removeAllPieces();
        view.initiateChessComponent(model);
        view.repaint();
        setSelectedPointNull();
        currentPlayer = PlayerColor.BLUE;
        AnimalComponent.setRedsEaten(0) ;//重置被吃棋子数量
        AnimalComponent.setBluesEaten(0) ;//重置被吃棋子数量
        setAIModeOrNot(false);//restart默认为玩家对战
        setAIModeGreedyOrNot(false);//restart默认为玩家对战
        //if (currentPlayer == PlayerColor.RED){swapColor();}
    }
    public void loadGameFromFile(String path){
        try {
            List<String> lines = Files.readAllLines(Path.of(path));
            //错误判断
            for (String s: lines) {
                System.out.println(s);
            }
            model.removeAllPieces();
            model.initPieces(lines);
            view.removeAllPieces();
            view.initiateChessComponent(model);
            view.repaint();
            if (lines.get(9).charAt(0) == 'B') {
                currentPlayer = PlayerColor.BLUE;
            }else if (lines.get(9).charAt(0) == 'R'){
                currentPlayer = PlayerColor.RED;
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    public String getFileColor(){
        if (currentPlayer == PlayerColor.BLUE){
            return "BLUTURN";
        }else {
            return "REDTURN";
        }
    }
    public void saveGameToFile(String filename){
        try {
            FileWriter writer = new FileWriter(filename + ".txt");
            BufferedWriter bufferedWriter = new BufferedWriter(writer);
            List<String> content = model.getContent();
            content.add(getFileColor());
            for (int i = 0; i < content.size(); i++) {
                bufferedWriter.write(content.get(i));
                bufferedWriter.newLine();
            }
            bufferedWriter.close();
            System.out.println("Txt file generated successfully.");
        } catch (IOException e) {
            System.out.println("An error occurred while generating txt file.");
            e.printStackTrace();
        }

    }

    public ChessGameFrame getFrame() {
        return frame;
    }

    public void setFrame(ChessGameFrame frame) {
        this.frame = frame;
    }

    //public boolean isAIModeOrNot() {
    //    return AIModeOrNot;
    //}

    public void setAIModeOrNot(boolean AIModeOrNot) {
        this.AIModeOrNot = AIModeOrNot;
    }
}
