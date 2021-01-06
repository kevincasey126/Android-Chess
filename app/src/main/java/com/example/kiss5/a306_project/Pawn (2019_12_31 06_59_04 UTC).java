package com.example.kiss5.a306_project;

import java.util.ArrayList;

public class Pawn implements GamePiece, Cloneable {

    double size;
    Move currLoc;
    int team, score;
    boolean firstMove;

    public Pawn(double newSize, int setTeam, Move newLoc){
        size = newSize;
        team = setTeam;
        currLoc = newLoc;
        firstMove = true;
        score = 10;
    }

    @Override
    public GamePiece clone() throws CloneNotSupportedException{
        Pawn copied = (Pawn)super.clone();
        copied.size = this.size;
        copied.team = this.team;
        copied.currLoc = this.currLoc;
        copied.score = this.score;
        return copied;
    }

    public int getScore(){
        return score;
    }

    @Override
    public void move(Move newMove) {
        currLoc = newMove;
        if(firstMove){
            firstMove = false;
        }
    }

    @Override
    public Move currLocation() {
        return currLoc;
    }

    @Override
    public boolean checkHit(double x, double y) {
        return (currLoc.getX() == x && currLoc.getY() == y);
    }

    @Override
    public int getTeam() {
        return team;
    }

    @Override
    public String getPieceType() {
        return "Pawn";
    }

    @Override
    public void changeTeam(int i) {
        team = i;
    }

    @Override
    public void resetFirstMove() {
        firstMove = true;
    }

    @Override
    public ArrayList<Move> findMoves(ArrayList<GamePiece> pieces) {
        ArrayList<Move> newMoves = new ArrayList<>();
        boolean oneFront = false;
        boolean blocked = false;

        if (currLoc.getY() == 7) {
            oneFront = true;
        }
        for (GamePiece gp : pieces) {
            if (gp.getTeam() != team) {
                if (gp.checkHit(currLoc.getX() + 1, currLoc.getY() + 1)) {
                    newMoves.add(new Move(currLoc.getX() + 1, currLoc.getY() + 1));
                } else if (gp.checkHit(currLoc.getX() - 1, currLoc.getY() + 1)) {
                    newMoves.add(new Move(currLoc.getX() - 1, currLoc.getY() + 1));
                } else if (gp.checkHit(currLoc.getX(), currLoc.getY() + 1)) {
                    blocked = true;
                }
            } else {
                if (gp.checkHit(currLoc.getX(), currLoc.getY() + 1)) {
                    blocked = true;
                }if (gp.checkHit(currLoc.getX(), currLoc.getY() + 2)) {
                    oneFront = true;
                }
            }
        }
        if (oneFront && !blocked) {
            newMoves.add(new Move(currLoc.getX(), currLoc.getY() + 1));
        } else if (!blocked) {
            newMoves.add(new Move(currLoc.getX(), currLoc.getY() + 1));
            if(firstMove) {
                newMoves.add(new Move(currLoc.getX(), currLoc.getY() + 2));
            }
        }


        return newMoves;
    }
}
