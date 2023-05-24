package model;

import controller.GameController;

import java.util.ArrayList;
import java.util.List;

/**
 * This class store the real chess information.
 * The Chessboard has 9*7 cells, and each cell has a position for chess
 */
public class Chessboard {
    private Cell[][] grid;

    public Chessboard() {
        this.grid =
                new Cell[Constant.CHESSBOARD_ROW_SIZE.getNum()][Constant.CHESSBOARD_COL_SIZE.getNum()];//19X19
        initGrid();
        initPieces();
    }

    private void initGrid() {
        for (int i = 0; i < Constant.CHESSBOARD_ROW_SIZE.getNum(); i++) {
            for (int j = 0; j < Constant.CHESSBOARD_COL_SIZE.getNum(); j++) {
                grid[i][j] = new Cell();
            }
        }
    }
    public void removeAllPieces(){
        for (int i = 0; i < Constant.CHESSBOARD_ROW_SIZE.getNum(); i++) {
            for (int j = 0; j < Constant.CHESSBOARD_COL_SIZE.getNum(); j++) {
                if (grid[i][j].getPiece() != null)
                    grid[i][j].setPiece(null);
            }
        }
    }
    public List<String> getContent(){
        List<String> content = new ArrayList<>();
        String initcontentline = new String();
        for (int i = 0; i < Constant.CHESSBOARD_ROW_SIZE.getNum(); i++) {
            initcontentline = "0000000";
            StringBuilder contentline = new StringBuilder(initcontentline);
            for (int j = 0; j < Constant.CHESSBOARD_COL_SIZE.getNum(); j++) {
                if (grid[i][j].getPiece() == null){
                } else if (grid[i][j].getPiece().getName().equals("象") && grid[i][j].getPiece().getOwner().equals(PlayerColor.RED)) {
                    contentline.setCharAt(j, '8');
                } else if (grid[i][j].getPiece().getName().equals("狮") && grid[i][j].getPiece().getOwner().equals(PlayerColor.RED)) {
                    contentline.setCharAt(j, '7');
                } else if (grid[i][j].getPiece().getName().equals("虎") && grid[i][j].getPiece().getOwner().equals(PlayerColor.RED)) {
                    contentline.setCharAt(j, '6');
                } else if (grid[i][j].getPiece().getName().equals("豹") && grid[i][j].getPiece().getOwner().equals(PlayerColor.RED)) {
                    contentline.setCharAt(j, '5');
                } else if (grid[i][j].getPiece().getName().equals("狼") && grid[i][j].getPiece().getOwner().equals(PlayerColor.RED)) {
                    contentline.setCharAt(j, '4');
                } else if (grid[i][j].getPiece().getName().equals("狗") && grid[i][j].getPiece().getOwner().equals(PlayerColor.RED)) {
                    contentline.setCharAt(j, '3');
                } else if (grid[i][j].getPiece().getName().equals("猫") && grid[i][j].getPiece().getOwner().equals(PlayerColor.RED)) {
                    contentline.setCharAt(j, '2');
                } else if (grid[i][j].getPiece().getName().equals("鼠") && grid[i][j].getPiece().getOwner().equals(PlayerColor.RED)) {
                    contentline.setCharAt(j, '1');
                } else if (grid[i][j].getPiece().getName().equals("陷阱") && grid[i][j].getPiece().getOwner().equals(PlayerColor.RED)) {
                    contentline.setCharAt(j, '9');
                } else if (grid[i][j].getPiece().getName().equals("兽穴") && grid[i][j].getPiece().getOwner().equals(PlayerColor.RED)) {
                    contentline.setCharAt(j, 'o');
                } else if (grid[i][j].getPiece().getName().equals("象") && grid[i][j].getPiece().getOwner().equals(PlayerColor.BLUE)) {
                    contentline.setCharAt(j, 'H');
                } else if (grid[i][j].getPiece().getName().equals("狮") && grid[i][j].getPiece().getOwner().equals(PlayerColor.BLUE)) {
                    contentline.setCharAt(j, 'G');
                } else if (grid[i][j].getPiece().getName().equals("虎") && grid[i][j].getPiece().getOwner().equals(PlayerColor.BLUE)) {
                    contentline.setCharAt(j, 'F');
                } else if (grid[i][j].getPiece().getName().equals("豹") && grid[i][j].getPiece().getOwner().equals(PlayerColor.BLUE)) {
                    contentline.setCharAt(j, 'E');
                } else if (grid[i][j].getPiece().getName().equals("狼") && grid[i][j].getPiece().getOwner().equals(PlayerColor.BLUE)) {
                    contentline.setCharAt(j, 'D');
                } else if (grid[i][j].getPiece().getName().equals("狗") && grid[i][j].getPiece().getOwner().equals(PlayerColor.BLUE)) {
                    contentline.setCharAt(j, 'C');
                } else if (grid[i][j].getPiece().getName().equals("猫") && grid[i][j].getPiece().getOwner().equals(PlayerColor.BLUE)) {
                    contentline.setCharAt(j, 'B');
                } else if (grid[i][j].getPiece().getName().equals("鼠") && grid[i][j].getPiece().getOwner().equals(PlayerColor.BLUE)) {
                    contentline.setCharAt(j, 'A');
                } else if (grid[i][j].getPiece().getName().equals("陷阱") && grid[i][j].getPiece().getOwner().equals(PlayerColor.BLUE)) {
                    contentline.setCharAt(j, 'I');
                } else if (grid[i][j].getPiece().getName().equals("兽穴") && grid[i][j].getPiece().getOwner().equals(PlayerColor.BLUE)) {
                    contentline.setCharAt(j, 'J');
                }
            }
            initcontentline = contentline.toString();
            content.add(initcontentline);
        }
        return content;
    }

    public void initPieces() {
        grid[6][0].setPiece(new ChessPiece(PlayerColor.BLUE, "象",8 , new ChessboardPoint(6,0)));
        grid[2][6].setPiece(new ChessPiece(PlayerColor.RED, "象",8 , new ChessboardPoint(2,6)));
        grid[8][6].setPiece(new ChessPiece(PlayerColor.BLUE, "狮",7 , new ChessboardPoint(8,6)));
        grid[0][0].setPiece(new ChessPiece(PlayerColor.RED, "狮",7 , new ChessboardPoint(0,0)));
        grid[8][0].setPiece(new ChessPiece(PlayerColor.BLUE, "虎",6 , new ChessboardPoint(8,0)));
        grid[0][6].setPiece(new ChessPiece(PlayerColor.RED, "虎",6 , new ChessboardPoint(0,6)));
        grid[6][4].setPiece(new ChessPiece(PlayerColor.BLUE, "豹",5 , new ChessboardPoint(6,4)));
        grid[2][2].setPiece(new ChessPiece(PlayerColor.RED, "豹",5 , new ChessboardPoint(2,2)));
        grid[6][2].setPiece(new ChessPiece(PlayerColor.BLUE, "狼",4 , new ChessboardPoint(6,2)));
        grid[2][4].setPiece(new ChessPiece(PlayerColor.RED, "狼",4 , new ChessboardPoint(2,4)));
        grid[7][5].setPiece(new ChessPiece(PlayerColor.BLUE, "狗",3 , new ChessboardPoint(7,5)));
        grid[1][1].setPiece(new ChessPiece(PlayerColor.RED, "狗",3 , new ChessboardPoint(1,1)));
        grid[7][1].setPiece(new ChessPiece(PlayerColor.BLUE, "猫",2 , new ChessboardPoint(7,1)));
        grid[1][5].setPiece(new ChessPiece(PlayerColor.RED, "猫",2 , new ChessboardPoint(1,5)));
        grid[6][6].setPiece(new ChessPiece(PlayerColor.BLUE, "鼠",1 , new ChessboardPoint(6,6)));
        grid[2][0].setPiece(new ChessPiece(PlayerColor.RED, "鼠",1 , new ChessboardPoint(2,0)));
        grid[8][3].setPiece(new ChessPiece(PlayerColor.BLUE, "兽穴",0 , new ChessboardPoint(8,3)));
        grid[0][3].setPiece(new ChessPiece(PlayerColor.RED, "兽穴",0 , new ChessboardPoint(0,3)));
        grid[8][2].setPiece(new ChessPiece(PlayerColor.BLUE, "陷阱",0 , new ChessboardPoint(8,2)));
        grid[0][2].setPiece(new ChessPiece(PlayerColor.RED, "陷阱",0 , new ChessboardPoint(0,2)));
        grid[8][4].setPiece(new ChessPiece(PlayerColor.BLUE, "陷阱",0 , new ChessboardPoint(8,4)));
        grid[0][4].setPiece(new ChessPiece(PlayerColor.RED, "陷阱",0 , new ChessboardPoint(0,4)));
        grid[7][3].setPiece(new ChessPiece(PlayerColor.BLUE, "陷阱",0 , new ChessboardPoint(7,3)));
        grid[1][3].setPiece(new ChessPiece(PlayerColor.RED, "陷阱",0 , new ChessboardPoint(1,3)));
    }
    public void initPieces(List<String> lines){
        grid[8][3].setPiece(new ChessPiece(PlayerColor.BLUE, "兽穴",0 , new ChessboardPoint(8,3)));
        grid[0][3].setPiece(new ChessPiece(PlayerColor.RED, "兽穴",0 , new ChessboardPoint(0,3)));
        grid[8][2].setPiece(new ChessPiece(PlayerColor.BLUE, "陷阱",0 , new ChessboardPoint(8,2)));
        grid[0][2].setPiece(new ChessPiece(PlayerColor.RED, "陷阱",0 , new ChessboardPoint(0,2)));
        grid[8][4].setPiece(new ChessPiece(PlayerColor.BLUE, "陷阱",0 , new ChessboardPoint(8,4)));
        grid[0][4].setPiece(new ChessPiece(PlayerColor.RED, "陷阱",0 , new ChessboardPoint(0,4)));
        grid[7][3].setPiece(new ChessPiece(PlayerColor.BLUE, "陷阱",0 , new ChessboardPoint(7,3)));
        grid[1][3].setPiece(new ChessPiece(PlayerColor.RED, "陷阱",0 , new ChessboardPoint(1,3)));
        for (int i = 0; i < Constant.CHESSBOARD_ROW_SIZE.getNum(); i++) {
            for (int j = 0; j < Constant.CHESSBOARD_COL_SIZE.getNum(); j++) {
                if (lines.get(i).charAt(j) == '1') {
                    grid[i][j].setPiece(new ChessPiece(PlayerColor.RED, "鼠",1 , new ChessboardPoint(2,0)));
                }else if (lines.get(i).charAt(j) == 'A') {
                    grid[i][j].setPiece(new ChessPiece(PlayerColor.BLUE, "鼠",1 , new ChessboardPoint(6,6)));
                }else if (lines.get(i).charAt(j) == '2') {
                    grid[i][j].setPiece(new ChessPiece(PlayerColor.RED, "猫",2 , new ChessboardPoint(1,5)));
                }else if (lines.get(i).charAt(j) == 'B') {
                    grid[i][j].setPiece(new ChessPiece(PlayerColor.BLUE, "猫",2 , new ChessboardPoint(7,1)));
                }else if (lines.get(i).charAt(j) == '3') {
                    grid[i][j].setPiece(new ChessPiece(PlayerColor.RED, "狗",3 , new ChessboardPoint(1,1)));
                }else if (lines.get(i).charAt(j) == 'C') {
                    grid[i][j].setPiece(new ChessPiece(PlayerColor.BLUE, "狗",3 , new ChessboardPoint(7,5)));
                }else if (lines.get(i).charAt(j) == '4') {
                    grid[i][j].setPiece(new ChessPiece(PlayerColor.RED, "狼",4 , new ChessboardPoint(2,4)));
                }else if (lines.get(i).charAt(j) == 'D') {
                    grid[i][j].setPiece(new ChessPiece(PlayerColor.BLUE, "狼",4 , new ChessboardPoint(6,2)));
                }else if (lines.get(i).charAt(j) == '5') {
                    grid[i][j].setPiece(new ChessPiece(PlayerColor.RED, "豹",5 , new ChessboardPoint(2,2)));
                }else if (lines.get(i).charAt(j) == 'E') {
                    grid[i][j].setPiece(new ChessPiece(PlayerColor.BLUE, "豹",5 , new ChessboardPoint(6,4)));
                }else if (lines.get(i).charAt(j) == '6') {
                    grid[i][j].setPiece(new ChessPiece(PlayerColor.RED, "虎",6 , new ChessboardPoint(0,6)));
                }else if (lines.get(i).charAt(j) == 'F') {
                    grid[i][j].setPiece(new ChessPiece(PlayerColor.BLUE, "虎",6 , new ChessboardPoint(8,0)));
                }else if (lines.get(i).charAt(j) == '7') {
                    grid[i][j].setPiece(new ChessPiece(PlayerColor.RED, "狮",7 , new ChessboardPoint(0,0)));
                }else if (lines.get(i).charAt(j) == 'G') {
                    grid[i][j].setPiece(new ChessPiece(PlayerColor.BLUE, "狮",7 , new ChessboardPoint(8,6)));
                }else if (lines.get(i).charAt(j) == '8') {
                    grid[i][j].setPiece(new ChessPiece(PlayerColor.RED, "象",8 , new ChessboardPoint(2,6)));
                }else if (lines.get(i).charAt(j) == 'H') {
                    grid[i][j].setPiece(new ChessPiece(PlayerColor.BLUE, "象",8 , new ChessboardPoint(6,0)));
                }else if (lines.get(i).charAt(j) == '9') {
                    grid[i][j].setPiece(new ChessPiece(PlayerColor.RED, "陷阱",0 , new ChessboardPoint(i,j)));
                }else if (lines.get(i).charAt(j) == 'o') {
                    grid[i][j].setPiece(new ChessPiece(PlayerColor.RED, "兽穴",0 , new ChessboardPoint(i,j)));
                }else if (lines.get(i).charAt(j) == 'I') {
                    grid[i][j].setPiece(new ChessPiece(PlayerColor.BLUE, "陷阱",0 , new ChessboardPoint(i,j)));
                }else if (lines.get(i).charAt(j) == 'J') {
                    grid[i][j].setPiece(new ChessPiece(PlayerColor.BLUE, "兽穴",0 , new ChessboardPoint(i,j)));
                }
            }
        }
    }
    public ChessPiece getChessPieceAt(ChessboardPoint point) {
        return getGridAt(point).getPiece();
    }

    public Cell getGridAt(ChessboardPoint point) {
        return grid[point.getRow()][point.getCol()];
    }

    private double calculateDistance(ChessboardPoint src, ChessboardPoint dest) {
        //距离改写成坐标系平方开根距离
        return Math.sqrt((src.getRow() - dest.getRow()) *(src.getRow() - dest.getRow())
                + (src.getCol() - dest.getCol()) * (src.getCol() - dest.getCol()));
    }



    private ChessPiece removeChessPiece(ChessboardPoint point) {
        ChessPiece chessPiece = getChessPieceAt(point);
        getGridAt(point).removePiece();
        return chessPiece;
    }

    public void setChessPiece(ChessboardPoint point, ChessPiece chessPiece) {
        getGridAt(point).setPiece(chessPiece);
    }


    public void moveChessPiece(ChessboardPoint src, ChessboardPoint dest) {
       /*
       if (!isValidMove(src, dest)) {
            throw new IllegalArgumentException("Illegal chess move!");
        }
        */
        setChessPiece(dest, removeChessPiece(src));
    }

    /*
    public void captureChessPiece(ChessboardPoint src, ChessboardPoint dest) {
        if (isValidCapture(src, dest)) {
            throw new IllegalArgumentException("Illegal chess capture!");
        }
        // TODO: Finish the method.
    }
     */

    public Cell[][] getGrid() {
        return grid;
    }
    public PlayerColor getChessPieceOwner(ChessboardPoint point) {
        if (getGridAt(point).getPiece() != null) {
            return getGridAt(point).getPiece().getOwner();
        }else {return null;}
    }



    //用于记录river位置
    public final ChessboardPoint river1 = new ChessboardPoint(3,1);
    public final ChessboardPoint river2 = new ChessboardPoint(3,2);
    public final ChessboardPoint river3 = new ChessboardPoint(3,4);
    public final ChessboardPoint river4 = new ChessboardPoint(3,5);
    public final ChessboardPoint river5 = new ChessboardPoint(4,1);
    public final ChessboardPoint river6 = new ChessboardPoint(4,2);
    public final ChessboardPoint river7 = new ChessboardPoint(4,4);
    public final ChessboardPoint river8 = new ChessboardPoint(4,5);
    public final ChessboardPoint river9 = new ChessboardPoint(5,1);
    public final ChessboardPoint river10 = new ChessboardPoint(5,2);
    public final ChessboardPoint river11 = new ChessboardPoint(5,4);
    public final ChessboardPoint river12 = new ChessboardPoint(5,5);

    //记录河边位置，用于判断狮虎越过河：
    //1.河的左右两侧：
    public final ChessboardPoint riverSide1 = new ChessboardPoint(3,0);
    public final ChessboardPoint riverSide2 = new ChessboardPoint(3,3);
    public final ChessboardPoint riverSide3 = new ChessboardPoint(3,6);
    public final ChessboardPoint riverSide4 = new ChessboardPoint(4,0);
    public final ChessboardPoint riverSide5 = new ChessboardPoint(4,3);
    public final ChessboardPoint riverSide6 = new ChessboardPoint(4,6);
    public final ChessboardPoint riverSide7 = new ChessboardPoint(5,0);
    public final ChessboardPoint riverSide8 = new ChessboardPoint(5,3);
    public final ChessboardPoint riverSide9 = new ChessboardPoint(5,6);
    //2.河的上下两侧：
    public final ChessboardPoint riverSides1 = new ChessboardPoint(2,1);
    public final ChessboardPoint riverSides2 = new ChessboardPoint(2,2);
    public final ChessboardPoint riverSides3 = new ChessboardPoint(2,4);
    public final ChessboardPoint riverSides4 = new ChessboardPoint(2,5);
    public final ChessboardPoint riverSides5 = new ChessboardPoint(6,1);
    public final ChessboardPoint riverSides6 = new ChessboardPoint(6,2);
    public final ChessboardPoint riverSides7 = new ChessboardPoint(6,4);
    public final ChessboardPoint riverSides8 = new ChessboardPoint(6,5);


    //记得有写了一个判断同颜色不能吃，忘了写在哪了，在这里isValidMove方法中又再补了一个
    public boolean isValidMove(ChessboardPoint src, ChessboardPoint dest) {//传入始末位置
        if (getChessPieceAt(src) == null || getChessPieceAt(src).getName().equals("陷阱") ||
                getChessPieceAt(src).getName().equals("兽穴")) {//原位置为空或陷阱或兽穴
            return false;
        } else if(getChessPieceAt(src).getRank() == 0 && getChessPieceAt(dest) != null
            && getChessPieceAt(dest).getRank() != 0){//起始点为对方陷阱且目标点有除兽穴外棋子
            return false;
        } else if (calculateDistance(src, dest) != 1) {//距离不为1(距离重新定义为坐标系平方开根double)，发现这样也还得进行一样的逻辑判断，但懒得改回去

            //老虎狮子的特殊过河规则：
            if (getChessPieceAt(src).getName().equals("狮") ||
                    getChessPieceAt(src).getName().equals("虎")){
                if(calculateDistance(src, dest) == 3 &&
                src.getRow() == dest.getRow()){//左右跳，始末同一行且距离为3
                    //约束起跳位置:
                    if(src.equals(riverSide1) || src.equals(riverSide2) || src.equals(riverSide3) ||
                            src.equals(riverSide4) || src.equals(riverSide5) || src.equals(riverSide6) ||
                            src.equals(riverSide7) || src.equals(riverSide8) || src.equals(riverSide9) ){
                        //判定河中有无老鼠，若无老鼠：能否吃掉目标：
                        if (src.getCol() == 0 &&
                                getChessPieceAt(new ChessboardPoint(src.getRow(),1)) == null &&
                                getChessPieceAt(new ChessboardPoint(src.getRow(),2)) == null ){
                            if(getChessPieceAt(dest) == null ||
                                    getChessPieceAt(src).canCapture(getChessPieceAt(dest))){return true;}
                        }else if (src.getCol() == 6&&
                                getChessPieceAt(new ChessboardPoint(src.getRow(),4)) == null &&
                                getChessPieceAt(new ChessboardPoint(src.getRow(),5)) == null){
                            if(getChessPieceAt(dest) == null ||
                                    getChessPieceAt(src).canCapture(getChessPieceAt(dest))){return true;}
                        }else if (dest.getCol() == 0 &&
                                getChessPieceAt(new ChessboardPoint(src.getRow(),1)) == null &&
                                getChessPieceAt(new ChessboardPoint(src.getRow(),2)) == null){
                            if(getChessPieceAt(dest) == null ||
                                    getChessPieceAt(src).canCapture(getChessPieceAt(dest))){return true;}
                        }else if (dest.getCol() == 6 &&
                                getChessPieceAt(new ChessboardPoint(src.getRow(),4)) == null &&
                                getChessPieceAt(new ChessboardPoint(src.getRow(),5)) == null){
                            if(getChessPieceAt(dest) == null ||
                                    getChessPieceAt(src).canCapture(getChessPieceAt(dest))){return true;}
                        }
                    }

                } else if (calculateDistance(src, dest) == 4 &&
                        src.getCol() == dest.getCol()) {//上下跳，距离为4且同一列
                    if (getChessPieceAt(src).getName().equals("狮") ||
                            getChessPieceAt(src).getName().equals("虎")){
                        //约束起跳位置:
                        if(src.equals(riverSides1) || src.equals(riverSides2) || src.equals(riverSides3) ||
                                src.equals(riverSides4) || src.equals(riverSides5) || src.equals(riverSides6) ||
                                src.equals(riverSides7) || src.equals(riverSides8)){
                            //判定河中有无老鼠，若无老鼠：能否吃掉目标：
                            if (getChessPieceAt(new ChessboardPoint(3 , src.getCol())) == null &&
                                    getChessPieceAt(new ChessboardPoint(4 , src.getCol())) == null &&
                                    getChessPieceAt(new ChessboardPoint(5 , src.getCol())) == null ){
                                if(getChessPieceAt(dest) == null ||
                                        getChessPieceAt(src).canCapture(getChessPieceAt(dest))){return true;}
                            }
                        }
                    }
                }
            } else {return false;}

        } else if (dest.equals(river1) || dest.equals(river2) || dest.equals(river3) ||
                dest.equals(river4) || dest.equals(river5) || dest.equals(river6) ||
                dest.equals(river7) || dest.equals(river8) || dest.equals(river9) ||
                dest.equals(river10) || dest.equals(river11) || dest.equals(river12)  ) {//判断能否进河
                if(getChessPieceAt(src).getName().equals("鼠")){
                    if(getChessPieceAt(dest) == null){//移动棋子为老鼠，且目标为河无动物，可以入河
                    return true;}else if(src.equals(river1) || src.equals(river2) || src.equals(river3) ||
                            src.equals(river4) || src.equals(river5) || src.equals(river6) ||
                            src.equals(river7) || src.equals(river8) || src.equals(river9) ||
                            src.equals(river10) || src.equals(river11) || src.equals(river12)){
                        //移动棋子为老鼠，且起点、目标均为为河，可以move
                        return true;}
                    else {return false;}
                }else {return false;}
        } else if (getChessPieceAt(dest) == null) {//目标位置为空
            return true;
        } else if (src.equals(river1) || src.equals(river2) || src.equals(river3) ||
                src.equals(river4) || src.equals(river5) || src.equals(river6) ||
                src.equals(river7) || src.equals(river8) || src.equals(river9) ||
                src.equals(river10) || src.equals(river11) || src.equals(river12)
                //||
                //dest.equals(river1) || dest.equals(river2) || dest.equals(river3) ||
                //dest.equals(river4) || dest.equals(river5) || dest.equals(river6) ||
                //dest.equals(river7) || dest.equals(river8) || dest.equals(river9) ||
                //dest.equals(river10) || dest.equals(river11) || dest.equals(river12)
                 ){//目标位置非空老鼠不能出、入河
                return false;
        } else if (getChessPieceAt(dest).getName().equals("陷阱")) {//两边均可进入陷阱
            return true;
        } else if (getChessPieceAt(src).getOwner().equals(getChessPieceAt(dest).getOwner()))
        {//除陷阱外始末位置同色为非法移动
            return false;
        }
        else if (getChessPieceAt(src).getName().equals("鼠")) {//鼠吃象或鼠以及进兽穴
            if(getChessPieceAt(dest).getName().equals("象")){
                return true;} else  if(getChessPieceAt(dest).getName().equals("鼠")){
                return true;} else if (getChessPieceAt(dest).getName().equals("兽穴")) {
                return true;} //新改：老鼠可以进入兽穴
            else {return false;}
        } else if (getChessPieceAt(src).getName().equals("象")) {//大象不能吃老鼠，除非老鼠在陷阱里
            if(getChessPieceAt(dest).getName().equals("鼠")&&
                    getChessPieceAt(dest).getRank() > 0//老鼠不在陷阱里
            ){return false;
            }else {return true;}
        } else if(getChessPieceAt(src).canCapture(getChessPieceAt(dest)) )//两边rank的比较
        {return true;}
        else {return false;}
    return false;/*前面一直补return false，废了好长时间，到最后突然想起来其实前面大部分不用补，
    不true的在最后补个false就好了.....
    */
    }

/*
    public boolean isValidCapture(ChessboardPoint src, ChessboardPoint dest) {
        // TODO:Fix this method
        return false;
    }

 */
}
