package model;


public class ChessPiece {
    // the owner of the chess
    private PlayerColor owner;

    private int originalRank;//用于跳出陷阱时恢复rank


    // Elephant? Cat? Dog? ...
    private String name;
    private int rank;

    private ChessboardPoint position;
    //用于记录位置进行最后的困局胜利判断操作
    public void setPosition(ChessboardPoint position) {
        this.position = position;
    }
    public ChessboardPoint getPosition(){
        return position;
    }

    public ChessPiece(PlayerColor owner, String name, int rank , ChessboardPoint position) {
        this.owner = owner;
        this.name = name;
        this.rank = rank;
        originalRank = rank;
        this.position = position;
    }

    public boolean canCapture(ChessPiece target) {
        // TODO: Finish this method!
        if (rank < target.rank || owner.equals(target.getOwner())) {return false;}
        else {return true;}
    }

    public String getName() {
        return name;
    }

    public int getOriginalRank(){return originalRank;}

    public PlayerColor getOwner() {
        return owner;
    }

    public int getRank(){return rank;}
    public void setRank(int rank){this.rank = rank;}
}
