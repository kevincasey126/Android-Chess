package com.example.kiss5.a306_project;

import java.util.ArrayList;

public interface GamePiece extends Cloneable {

    GamePiece clone() throws CloneNotSupportedException;

    /**
     * has a form of move for each piece
     * @param newMove the new location for the piece
     */
    void move(Move newMove);

    /**
     * find all the possible moves that a piece can make
     * @param pieces all game pieces on the board
     * @return the list of possible moves for the piece
     */
    ArrayList<Move> findMoves(ArrayList<GamePiece> pieces);

    /**
     * get the current location of the piece
     * @return the current location
     */
    Move currLocation();

    /**
     * check if the piece is in this specific current location
     * @param x new x location
     * @param y new y location
     * @return true if in the new location, false otherwise
     */
    boolean checkHit(double x, double y);

    /**
     * get which team a piece is on
     * @return the team number
     */
    int getTeam();

    /**
     * get the piece type
     * @return  a string specifying the piece type
     */
    String getPieceType();

    /**
     * switch the team of the piece
     * @param i the new team number
     */
    void changeTeam(int i);

    /**
     * used to reset a pawns first move
     */
    void resetFirstMove();

    /**
     * used to get score from piece
     * @return score value
     */
    int getScore();
    }

