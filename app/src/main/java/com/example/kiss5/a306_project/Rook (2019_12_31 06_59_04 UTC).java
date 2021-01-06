package com.example.kiss5.a306_project;

import java.util.ArrayList;

public class Rook implements GamePiece, Cloneable {

    double size;
    Move currLoc;
    int team, score;

    public Rook(double newSize, int setTeam, Move newLoc){
        size = newSize;
        team = setTeam;
        currLoc = newLoc;
        score = 50;
    }

    @Override
    public GamePiece clone() throws CloneNotSupportedException{
        Rook copied = (Rook)super.clone();
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
        /*It adds all the possible moves then checks the remaining pieces
        * to remove whichever are blocked off or a piece is there*/
        ArrayList<Move> newMoves = new ArrayList<>();
        double counter = 1;
        while(counter <= 8){
            if(counter != currLoc.getX()) {
                newMoves.add(new Move(counter, currLoc.getY()));
            }
            if(counter != currLoc.getY()) {
                newMoves.add(new Move(currLoc.getX(), counter));
            }
            counter ++;
        }
        for(GamePiece gp : pieces){
            if(newMoves.contains(gp.currLocation())){
                if(gp.currLocation().getX() > currLoc.getX()){
                    counter = gp.currLocation().getX();
                    if(team != gp.getTeam()){
                        counter++;
                    }
                    while(counter <= 8){
                        newMoves.remove(new Move(counter, currLoc.getY()));
                        counter++;
                    }
                }else if (gp.currLocation().getX() < currLoc.getX()){
                    counter = gp.currLocation().getX();
                    if(team != gp.getTeam()){
                        counter--;
                    }
                    while(counter > 0){
                        newMoves.remove(new Move(counter, currLoc.getY()));
                        counter--;
                    }
                }else if(gp.currLocation().getY() > currLoc.getY()){
                    counter = gp.currLocation().getY();
                    if(team != gp.getTeam()){
                        counter++;
                    }
                    while(counter <= 8){
                        newMoves.remove(new Move(currLoc.getX(), counter));
                        counter++;
                    }
                }else{
                    counter = gp.currLocation().getY();
                    if(team != gp.getTeam()){
                        counter--;
                    }
                    while(counter > 0){
                        newMoves.remove(new Move(currLoc.getX(), counter));
                        counter--;
                    }
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
        return "Rook";
    }

    @Override
    public void changeTeam(int i) {
        team = i;
    }

    @Override
    public void resetFirstMove() {

    }
}
