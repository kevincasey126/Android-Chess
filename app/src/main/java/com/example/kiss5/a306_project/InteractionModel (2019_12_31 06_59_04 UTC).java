package com.example.kiss5.a306_project;

import java.util.ArrayList;

public class InteractionModel {
    private ArrayList<ModelListener> subscribers;
    GamePiece selected;
    Move selectedLocation;
    ArrayList<Move> possibleMoves;

    public InteractionModel(){
        subscribers = new ArrayList<>();
        possibleMoves = new ArrayList<>();
        selected = null;
        selectedLocation = null;
    }

    /**
     * add a specific piece to the selected position and find all the pieces possible moves
     * @param piece the piece to make selected
     * @param allPieces all the pieces left on the game board
     */
    public void selectPiece(GamePiece piece, ArrayList<GamePiece> allPieces){
        selected = piece;
        selectedLocation = piece.currLocation();
        possibleMoves = piece.findMoves(allPieces);
        notifySubscribers();
    }

    /**
     * find all the possible moves for the selected game piece
     * @return an array list of moves that the piece is allowed to make
     */
    public ArrayList<Move> getPossibleMoves(){
        return possibleMoves;
    }

    /**
     * clear all selections within the iModel
     */
    public void unSelectPiece(){
        selected = null;
        selectedLocation = null;
        possibleMoves.clear();
        notifySubscribers();
    }

    /**
     * Add a subscriber to the subs list
     * @param sub the new sub
     */
    public void addSubscriber(ModelListener sub){
        subscribers.add(sub);
    }

    /**
     * update all the subscribers to the new changes
     */
    public void notifySubscribers(){
        for (ModelListener subs : subscribers){
            subs.modelChanged();
        }
    }
}
