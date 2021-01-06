package com.example.kiss5.a306_project;

import java.util.ArrayList;

public class Knight implements GamePiece, Cloneable {

    double size;
    Move currLoc;
    int team, score;

    public Knight(double newSize, int setTeam, Move newLoc){
        size = newSize;
        team = setTeam;
        currLoc = newLoc;
        score = 30;
    }

    @Override
    public GamePiece clone() throws CloneNotSupportedException{
        Knight copied = (Knight)super.clone();
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
    }

    @Override
    public ArrayList<Move> findMoves(ArrayList<GamePiece> pieces) {
        ArrayList<Move> newMoves = new ArrayList<>();
        if (currLoc.getX() + 2 < 9 && currLoc.getY() + 1 < 9) {
            newMoves.add(new Move(currLoc.getX() + 2, currLoc.getY() + 1));
        }
        if (currLoc.getX() + 1 < 9 && currLoc.getY() + 2 < 9) {
            newMoves.add(new Move(currLoc.getX() + 1, currLoc.getY() + 2));
        }
        if (currLoc.getX() + 2 < 9 && currLoc.getY() - 1 > 0) {
            newMoves.add(new Move(currLoc.getX() + 2, currLoc.getY() - 1));
        }
        if (currLoc.getX() + 1 < 9 && currLoc.getY() - 2 > 0) {
            newMoves.add(new Move(currLoc.getX() + 1, currLoc.getY() - 2));
        }
        if (currLoc.getX() - 2 > 0 && currLoc.getY() + 1 < 9) {
            newMoves.add(new Move(currLoc.getX() - 2, currLoc.getY() + 1));
        }
        if (currLoc.getX() - 1 > 0 && currLoc.getY() + 2 < 9) {
            newMoves.add(new Move(currLoc.getX() - 1, currLoc.getY() + 2));
        }
        if (currLoc.getX() - 2 > 0 && currLoc.getY() - 1 > 0) {
            newMoves.add(new Move(currLoc.getX() - 2, currLoc.getY() - 1));
        }
        if (currLoc.getX() - 1 > 9 && currLoc.getY() - 2 > 0) {
            newMoves.add(new Move(currLoc.getX() - 1, currLoc.getY() - 2));
        }
        for(GamePiece gp : pieces){
            if(gp.getTeam() == team) {
                Move temp = new Move(gp.currLocation().getX(), gp.currLocation().getY());
                if(newMoves.contains(temp)){
                    newMoves.remove(temp);
                }
            }
        }


        return newMoves;
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
        return "Knight";
    }

    @Override
    public void changeTeam(int i) {
        team = i;
    }

    @Override
    public void resetFirstMove() {

    }

}
