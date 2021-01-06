package com.example.kiss5.a306_project;

import java.util.ArrayList;

public class Queen implements GamePiece, Cloneable{
    double size;
    Move currLoc;
    int team, score;

    public Queen(double newSize, int setTeam, Move newLoc){
        size = newSize;
        team = setTeam;
        currLoc = newLoc;
        score = 90;
    }

    @Override
    public GamePiece clone() throws CloneNotSupportedException{
        Queen copied = (Queen)super.clone();
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
        double x = currLoc.getX();
        double y = currLoc.getY();
        while(counter <= 8){
            if(y + counter <= 8) {
                if (x - counter > 0) {
                    newMoves.add(new Move(x - counter, y + counter));
                }
                if(counter + x <= 8){
                    newMoves.add(new Move(x + counter, y + counter));
                }
            }
            if(y - counter > 0){
                if(x - counter > 0){
                    newMoves.add(new Move(x - counter, y - counter));
                }
                if(counter + x <= 8){
                    newMoves.add(new Move(x + counter, y - counter));
                }
            }
            if(counter != x) {
                newMoves.add(new Move(counter, currLoc.getY()));
            }
            if(counter != y) {
                newMoves.add(new Move(currLoc.getX(), counter));
            }
            counter++;
        }
        for(GamePiece gp : pieces) {
            if (newMoves.contains(gp.currLocation())) {
                double gpX = gp.currLocation().getX();
                double gpY = gp.currLocation().getY();
                if (gpY == y) {
                    if (gpX > x) {
                        counter = gp.currLocation().getX();
                        if (team != gp.getTeam()) {
                            counter++;
                        }
                        while (counter <= 8) {
                            newMoves.remove(new Move(counter, y));
                            counter++;
                        }
                    } else if (gpX < x) {
                        counter = gp.currLocation().getX();
                        if (team != gp.getTeam()) {
                            counter--;
                        }
                        while (counter > 0) {
                            newMoves.remove(new Move(counter, y));
                            counter--;
                        }
                    }
                } else if (gpY > y) {
                    if (gpX > x) {
                        if (team != gp.getTeam()) {
                            gpX++;
                            gpY++;
                        }
                        while (gpX <= 8 && gpY <= 8) {
                            newMoves.remove(new Move(gpX, gpY));
                            gpX++;
                            gpY++;
                        }
                    } else if (gpX < x) {
                        if (team != gp.getTeam()) {
                            gpX--;
                            gpY++;
                        }
                        while (gpX > 0 && gpY <= 8) {
                            newMoves.remove(new Move(gpX, gpY));
                            gpX--;
                            gpY++;
                        }
                    } else if (gpX == x) {
                        counter = gp.currLocation().getY();
                        if (team != gp.getTeam()) {
                            counter++;
                        }
                        while (counter <= 8) {
                            newMoves.remove(new Move(currLoc.getX(), counter));
                            counter++;
                        }
                    }
                } else if (gpY < y) {
                        if (gpX > x) {
                            if (team != gp.getTeam()) {
                                gpX++;
                                gpY--;
                            }
                            while (gpX <= 8 && gpY > 0) {
                                newMoves.remove(new Move(gpX, gpY));
                                gpX++;
                                gpY--;
                            }
                        } else if (gpX < x) {
                            if (team != gp.getTeam()) {
                                gpX--;
                                gpY--;
                            }
                            while (gpX > 0 && gpY > 0) {
                                newMoves.remove(new Move(gpX, gpY));
                                gpX--;
                                gpY--;
                            }
                        } else if (gpX == x) {
                            counter = gp.currLocation().getY();
                            if (team != gp.getTeam()) {
                                counter--;
                            }
                            while (counter > 0) {
                                newMoves.remove(new Move(x, counter));
                                counter--;
                            }
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
        return "Queen";
    }

    @Override
    public void changeTeam(int i) {
        team = i;
    }

    @Override
    public void resetFirstMove() {

    }
}
