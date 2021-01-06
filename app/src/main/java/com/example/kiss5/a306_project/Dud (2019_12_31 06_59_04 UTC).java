package com.example.kiss5.a306_project;

import java.util.ArrayList;

public class Dud implements GamePiece, Cloneable {

    public Dud(){

    }

    @Override
    public GamePiece clone() throws CloneNotSupportedException{
        GamePiece copied = (GamePiece) super.clone();
        return copied;
    }

    @Override
    public void move(Move newMove) {

    }

    @Override
    public ArrayList<Move> findMoves(ArrayList<GamePiece> pieces) {
        return null;
    }

    @Override
    public Move currLocation() {
        return null;
    }

    @Override
    public boolean checkHit(double x, double y) {
        return false;
    }

    @Override
    public int getTeam() {
        return 0;
    }

    @Override
    public String getPieceType() {
        return "Dud";
    }

    @Override
    public void changeTeam(int i) {

    }

    @Override
    public void resetFirstMove() {

    }

    @Override
    public int getScore() {
        return 0;
    }
}
