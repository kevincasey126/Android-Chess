package com.example.kiss5.a306_project;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.View;
import android.widget.Button;

public class MultiplayerChessView extends View implements ModelListener{

    Paint paint;
    Model model;
    MultiplayerController multiplayerController;
    InteractionModel iModel;
    boolean end;
    double width, height;
    Button backButton;

    public MultiplayerChessView(Context context) {
        super(context);
        paint = new Paint();
        this.setBackgroundColor(Color.rgb(221,162,106));
        end = false;
    }

    /**
     * set the model for the view
     * @param newModel the new view
     */
    public void setModel(Model newModel){
        model = newModel;
    }

    /**
     * set the multiplayerController for the view
     * @param newMultiplayerController the new multiplayerController
     */
    public void setMultiplayerController(MultiplayerController newMultiplayerController) {
        multiplayerController = newMultiplayerController;
    }

    /**
     * set the iModel for the view
     * @param newIModel the new iModel
     */
    public void setIModel(InteractionModel newIModel){
        iModel = newIModel;
    }

    /**
     * called to reset the view for the end game phase
     */
    public void endGame(){
        end = true;
        invalidate();
    }

    public void onDraw(Canvas c){
        /*Draws the scoreBoard*/
        paint.setColor(Color.BLACK);
        paint.setTextSize(80);
        c.drawText("You: " + model.yourTeamCount + " remaining", 10, 100, paint);
        c.drawText("Opponent: " + model.otherTeamCount + " remaining", 10, 200, paint);
        //c.drawText("Opponent: " + model.yourTeamCount + " remaining", 10, 300, paint);

        if(end && !model.lose){
            c.drawText("Opponent left, you win!", 10, 300, paint);
        }
        else if(model.win){
            c.drawText("You win!", 10, 300, paint);
        }
        else if(model.lose){
            c.drawText("You lose!", 10, 300, paint);
        }
        else if(model.yourTeam == model.turn){
            c.drawText("Your Turn!", 10, 300, paint);
        }
        else if(model.otherTeam == model.turn){
            c.drawText("Opponents Turn!", 10,300,paint);
        }
        else{
            c.drawText("Waiting for opponent", 10,300,paint);
        }

        /*Draws the gameBoard*/
        paint.setColor(Color.rgb(33,16,1));
        int xCount, yCount;
        float tempX, tempY, squareSize;
        c.drawRect(model.gameBoard.getLeft() - 20, model.gameBoard.getTop() - 20,
                model.gameBoard.getRight() + 20, model.gameBoard.getBottom() + 20, paint);
        yCount = 1;
        tempY = model.gameBoard.getTop();
        squareSize = (float)model.modelSize;
        paint.setColor(Color.rgb(130,85,44));
        while(yCount <= 8){
            xCount = 1;
            if(yCount%2 == 1) {
                tempX = model.gameBoard.getLeft();
            }
            else{
                tempX = model.gameBoard.getLeft() + squareSize;
            }
            while(xCount <= 4){
                c.drawRect(tempX, tempY, tempX + squareSize, tempY + squareSize, paint);
                tempX += 2*squareSize;
                xCount++;
            }
            tempY += squareSize;
            yCount++;
        }

        /*Draws the possible moves with the selected piece*/
        if(iModel.getPossibleMoves().size() > 0){
            for(Move highlighted : iModel.getPossibleMoves()){
                paint.setColor(Color.YELLOW);
                c.drawRect((float)model.translateXtoXCord((int)highlighted.getX()),
                        (float)model.translateYtoYCord((int)highlighted.getY() + 1),
                        (float)model.translateXtoXCord((int)highlighted.getX()) + squareSize,
                        (float)model.translateYtoYCord((int)highlighted.getY() + 1) + squareSize, paint);
            }
        }
        /*Draws the game pieces*/

        for(GamePiece gp : model.allPieces){

            if(gp.getTeam() == 1){
                paint.setColor(Color.BLACK);
            }
            else{
                paint.setColor(Color.WHITE);
            }
            c.drawOval((float)model.translateXtoXCord((int)gp.currLocation().currX) + 3,
                    (float)model.translateYtoYCord((int)gp.currLocation().getY()) - squareSize + 3,
                    (float)model.translateXtoXCord((int)gp.currLocation().currX) + squareSize - 3,
                    (float)model.translateYtoYCord((int)gp.currLocation().getY()) - 3, paint);
            paint.setTextSize(100);
            if(gp.getTeam() == 1){
                paint.setColor(Color.WHITE);
            }
            else{
                paint.setColor(Color.BLACK);
            }
            if(gp.getPieceType().equals("Pawn")) {
                c.drawText("P", (float) model.translateXtoXCord((int) gp.currLocation().getX()) + (squareSize/4),
                        (float) model.translateYtoYCord((int) gp.currLocation().getY()) - (squareSize/4), paint);
            }
            else if(gp.getPieceType().equals("Rook")) {
                c.drawText("R", (float) model.translateXtoXCord((int) gp.currLocation().getX()) + (squareSize/4),
                        (float) model.translateYtoYCord((int) gp.currLocation().getY()) - (squareSize/4), paint);
            }
            else if(gp.getPieceType().equals("King")) {
                c.drawText("K", (float) model.translateXtoXCord((int) gp.currLocation().getX()) + (squareSize/4),
                        (float) model.translateYtoYCord((int) gp.currLocation().getY()) - (squareSize/4), paint);
            }
            else if(gp.getPieceType().equals("Queen")) {
                c.drawText("Q", (float) model.translateXtoXCord((int) gp.currLocation().getX()) + (squareSize/4),
                        (float) model.translateYtoYCord((int) gp.currLocation().getY()) - (squareSize/4), paint);
            }
            else if(gp.getPieceType().equals("Knight")) {
                c.drawText("N", (float) model.translateXtoXCord((int) gp.currLocation().getX()) + (squareSize/4),
                        (float) model.translateYtoYCord((int) gp.currLocation().getY()) - (squareSize/4), paint);
            }
            else if(gp.getPieceType().equals("Bishop")) {
                c.drawText("B", (float) model.translateXtoXCord((int) gp.currLocation().getX()) + (squareSize/4),
                        (float) model.translateYtoYCord((int) gp.currLocation().getY()) - (squareSize/4), paint);
            }

        }
    }

    @Override
    public void modelChanged() {
        invalidate();
    }
}
