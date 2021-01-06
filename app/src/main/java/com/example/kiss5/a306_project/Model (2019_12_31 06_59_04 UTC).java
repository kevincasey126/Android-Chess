package com.example.kiss5.a306_project;

import android.content.res.Resources;

import java.util.ArrayList;
import java.util.Iterator;

//import android.util.DisplayMetrics;

public class Model {
    ArrayList<GamePiece> allPieces;
    Board gameBoard;
    int otherTeam, yourTeam, otherTeamCount, yourTeamCount;
    private ArrayList<ModelListener> subscribers;
    //DisplayMetrics metrics;
    int screenWidth, screenHeight;
    double modelSize;
    boolean win, lose;
    int turn;

    public Model(int team){
        turn = 0;
        yourTeam = team;
        otherTeam = Math.abs(yourTeam - 1);

        subscribers = new ArrayList<>();
        allPieces = new ArrayList<>();

        win = false;
        lose = false;

        otherTeamCount = 16;
        yourTeamCount = 16;

        screenWidth = Resources.getSystem().getDisplayMetrics().widthPixels;
        screenHeight = Resources.getSystem().getDisplayMetrics().heightPixels;
        modelSize = (0.9 * screenWidth)/8;

        gameBoard = new Board(screenWidth*0.05, ((screenHeight - (0.9 * screenWidth))/2),
                (0.9 * screenWidth), (0.9 * screenWidth));

        setBoard();

    }

    public void setBoard(){
        int count = 1;
        while(count <= 8){
            allPieces.add(new Pawn(modelSize, yourTeam, new Move(count,2)));
            allPieces.add(new Pawn(modelSize, otherTeam, new Move(count,7)));
            count ++;
        }
        allPieces.add(new Knight(modelSize, yourTeam, new Move(2,1)));
        allPieces.add(new Knight(modelSize, yourTeam, new Move(7,1)));
        allPieces.add(new Knight(modelSize, otherTeam, new Move(2,8)));
        allPieces.add(new Knight(modelSize, otherTeam, new Move(7,8)));

        allPieces.add(new Bishop(modelSize, yourTeam, new Move(6,1)));
        allPieces.add(new Bishop(modelSize, yourTeam, new Move(3,1)));
        allPieces.add(new Bishop(modelSize, otherTeam, new Move(6,8)));
        allPieces.add(new Bishop(modelSize, otherTeam, new Move(3,8)));

        allPieces.add(new Rook(modelSize, yourTeam, new Move(1,1)));
        allPieces.add(new Rook(modelSize, yourTeam, new Move(8,1)));
        allPieces.add(new Rook(modelSize, otherTeam, new Move(1,8)));
        allPieces.add(new Rook(modelSize, otherTeam, new Move(8,8)));

        allPieces.add(new King(modelSize, yourTeam, new Move(4, 1)));
        allPieces.add(new King(modelSize, otherTeam, new Move(4, 8)));

        allPieces.add(new Queen(modelSize, yourTeam, new Move(5, 1)));
        allPieces.add(new Queen(modelSize, otherTeam, new Move(5, 8)));
        notifySubscribers();
    }

    public int[] getTeamScores(){
        int[] scores = new int[2];
        int score0 = 0;
        int score1 = 0;
        for(GamePiece gp : allPieces){
            if(gp.getTeam() == 0){
                score0 += gp.getScore();
            }
            else{
                score1 += gp.getScore();
            }
        }
        scores[0] = score0;
        scores[1] = score1;
        return scores;
    }

    public void winGame(){
        win = true;
        notifySubscribers();
    }

    public void loseGame(){
        lose = true;
        notifySubscribers();
    }


    public boolean checkKingHit(int newX, int newY){
        for(GamePiece gp : allPieces){
            if(gp.getPieceType().equals("King")) {
                if(gp.currLocation().getX() == newX && gp.currLocation().getY() == newY && gp.getTeam() == otherTeam) {
                    return true;
                }
                /*else{
                    return false;
                }*/
            }
        }
        return false;
    }

    public GamePiece getPiece(int xTouched, int yTouched){
        for(GamePiece gp : allPieces){
            if(gp.currLocation().getX() == xTouched && gp.currLocation().getY() == yTouched && gp.getTeam() == yourTeam){
                return gp;
            }
        }
        return new Dud();
    }

    public void updateTeam(int newTeam){
        yourTeam = newTeam;
        for(GamePiece gp : allPieces){
            if(gp.getTeam() == yourTeam){
                gp.changeTeam(newTeam);
                gp.move(new Move(9 - gp.currLocation().getX(), 9 - gp.currLocation().getY()));
            }
            else{
                gp.changeTeam(Math.abs(yourTeam-1));
                gp.move(new Move(9 - gp.currLocation().getX(), 9 - gp.currLocation().getY()));
            }
            if(gp.getPieceType().equals("Pawn")){
                gp.resetFirstMove();
            }
        }
        notifySubscribers();
    }

    public double translateXtoXCord(int x){
        return ((0.05 * screenWidth) + ((x - 1)*modelSize));
    }

    public double translateYtoYCord(int y){
        return (((screenHeight - (0.9*screenWidth))/2) + ((Math.abs(9 - y))*modelSize));
    }

    public int translateXCordToX(double xCord){
        return (int)((8*xCord - (8 * 0.05 * screenWidth) + (0.9 * screenWidth))/(0.9 * screenWidth));
    }

    public int translateYCordToY(double yCord){
        return Math.abs(9 - (int)((8*yCord - (4*(screenHeight - (0.9 * screenWidth))) + (0.9 * screenWidth))/(0.9 * screenWidth)));
    }

    public void removePiece(Move location){
        /*Will remove a piece from the location if there is a piece there*/
        Iterator itr = allPieces.iterator();
        while(itr.hasNext()){
            GamePiece gp = (GamePiece)itr.next();
            if(gp.checkHit(location.getX(), location.getY())){
                itr.remove();
                if(gp.getTeam() == 0) {
                    this.yourTeamCount--;
                }else{
                    this.otherTeamCount--;
                }
            }
        }
        notifySubscribers();
    }

    public void movePiece(GamePiece gp, Move newLocation){
        gp.move(newLocation);
        notifySubscribers();
    }

    public void flipTurn(){
        turn = Math.abs(turn - 1);
    }

    public void setTurn(int i){
        turn = i;
        notifySubscribers();
    }

    public double getSize(){
        return modelSize;
    }

    public void addSubscriber(ModelListener sub){
        subscribers.add(sub);
    }

    public void notifySubscribers(){
        for (ModelListener subs : subscribers){
            subs.modelChanged();
        }
    }
}
