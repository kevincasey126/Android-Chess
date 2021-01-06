package com.example.kiss5.a306_project;

import android.view.MotionEvent;

import java.util.Iterator;

public class SingleplayerController {

    enum state{
        //The only possible states need for this game to function
        READY, SELECTED
    }

    Model model;
    InteractionModel iModel;
    SingleplayerController.state currState;
    ChessAI ai;
    Object[] ai_response;

    /**
     * initialize the multiplayerController for the app and get an instance of the firebase database
     */
    public SingleplayerController(){
        currState = SingleplayerController.state.READY;
        ai_response = new Object[3];
        /*ai_response[0] = new Move(0,0);
        ai_response[1] = new Dud();
        ai_response[2] = 0;*/
    }

    public void setModel(Model newModel){
        model = newModel;
    }

    public void setIModel(InteractionModel newIModel){
        iModel = newIModel;
    }

    public void setAI(ChessAI AI){
        ai = AI;
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
                    currState = SingleplayerController.state.SELECTED;
                }
                else{
                    currState = SingleplayerController.state.READY;
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
                    if(model.checkKingHit(newX, newY)){
                        model.winGame();
                        model.removePiece(newLocation);
                        model.movePiece(iModel.selected, newLocation);
                        iModel.unSelectPiece();
                        model.flipTurn();
                        break;
                    }
                    model.removePiece(newLocation);
                    model.movePiece(iModel.selected, newLocation);
                    iModel.unSelectPiece();
                    model.flipTurn();
                    model.notifySubscribers();

                    ai_response = ai.miniMax(model.allPieces, model.turn, 4);

                    double moveX = ((Move)ai_response[0]).getX();
                    double moveY = ((Move)ai_response[0]).getY();
                    double gamePieceX = ((GamePiece)ai_response[1]).currLocation().getX();
                    double gamePieceY = ((GamePiece)ai_response[1]).currLocation().getY();

                    Iterator<GamePiece> iterator = model.allPieces.iterator();

                    while(iterator.hasNext()){
                        GamePiece gp = iterator.next();
                        if(gp.currLocation().equals(new Move( Math.abs(9 - moveX), Math.abs(9 - moveY)))){
                            if(gp.getPieceType().equals("King")){
                                model.loseGame();
                                model.removePiece(new Move(Math.abs(9 - moveX), Math.abs(9 - moveY)));
                                model.setTurn(-99);
                                break;
                            }
                            else {
                                model.removePiece(new Move(Math.abs(9 - moveX), Math.abs(9 - moveY)));
                                break;
                            }
                        }
                    }

                    for(GamePiece gp : model.allPieces){
                        if(gp.currLocation().equals(new Move( Math.abs(9 - gamePieceX), Math.abs(9 - gamePieceY)))){
                            gp.move(new Move(Math.abs(9 - moveX), Math.abs(9 - moveY)));
                            break;
                        }
                    }

                    currState = SingleplayerController.state.READY;
                    model.flipTurn();
                }
                else{
                    //if the tap is not on a possible move, unselect that piece
                    iModel.unSelectPiece();
                    currState = SingleplayerController.state.READY;
                }
                break;

        }
    }

}
