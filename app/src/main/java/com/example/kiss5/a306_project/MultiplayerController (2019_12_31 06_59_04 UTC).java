package com.example.kiss5.a306_project;

import android.view.MotionEvent;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class MultiplayerController {

    enum state{
        //The only possible states need for this game to function
        READY, SELECTED
    }

    FirebaseDatabase db;
    DatabaseReference dbRef;
    Model model;
    InteractionModel iModel;
    state currState;
    String gBoard;

    /**
     * initialize the multiplayerController for the app and get an instance of the firebase database
     */
    public MultiplayerController(){
        currState = state.READY;
        db = FirebaseDatabase.getInstance();
        dbRef = db.getReference();
    }

    public void setModel(Model newModel){
        model = newModel;
    }

    public void setIModel(InteractionModel newIModel){
        iModel = newIModel;
    }

    public void setgBoard(String gb){
        gBoard = gb;
    }

    /**
     * Is called for when there is a touch down on the screen from the user
     */
    public void handleDown(MotionEvent event){
        GamePiece piece;
        int newX = model.translateXCordToX(event.getX());
        int newY = model.translateYCordToY(event.getY());

        boolean safeMove = false;
        switch(currState){
            case READY:
                piece = model.getPiece(newX, newY);
                //if there was a piece selected then add it to the iModel, otherwise do nothing
                if(!piece.getPieceType().equals("Dud") && piece.getTeam() == model.yourTeam && model.turn == model.yourTeam){
                    iModel.selectPiece(piece, model.allPieces);
                    currState = state.SELECTED;
                }
                else{
                    currState = state.READY;
                }
                break;
            case SELECTED:
                //once selected, if the user taps on a possible move, move the piece there
                for(Move nextMove : iModel.getPossibleMoves()){
                    if(nextMove.getX() == newX && nextMove.getY() == newY){
                        safeMove = true;
                    }
                }
                if(safeMove){
                    //move the piece to its new location, removing a piece if there is one in that space
                    Move newLocation = new Move(newX, newY);
                    dbRef.child(gBoard).child("Move").setValue("" + model.yourTeam + (int)iModel.selected.currLocation().getX() + (int)iModel.selected.currLocation().getY() +  newX + newY);
                    if(model.checkKingHit(newX, newY)){
                        model.winGame();
                        dbRef.child(gBoard).child("Move").setValue("99998");
                    }
                    model.removePiece(newLocation);
                    model.movePiece(iModel.selected, newLocation);
                    iModel.unSelectPiece();
                    model.flipTurn();
                    currState = state.READY;
                }
                else{
                    //if the tap is not on a possible move, unselect that piece
                    iModel.unSelectPiece();
                    currState = state.READY;
                }
                break;

        }
    }

}
