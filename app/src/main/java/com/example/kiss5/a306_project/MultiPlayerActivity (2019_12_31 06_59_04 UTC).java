package com.example.kiss5.a306_project;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


public class MultiPlayerActivity extends AppCompatActivity implements GestureDetector.OnGestureListener,
        View.OnTouchListener{

    FirebaseDatabase database;
    DatabaseReference dbRef;
    Model model;
    InteractionModel iModel;
    MultiplayerController multiplayerController;
    MultiplayerChessView view;
    LinearLayout box;
    GestureDetector detector;
    boolean beenInitialized, boardSet;
    String nextGameBoard;
    int boardCount;
    String gBoard, init;
    boolean end;


    //Used for when the app is closed, but not destroyed
    @Override
    protected void onStop() {
        super.onStop();
        if(end){
            dbRef.child(gBoard).removeValue();
        }else {
            dbRef.child(gBoard).child("Move").setValue("99999");
        }
    }

    //used when the instance of the app is destoryed
    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(end){
            dbRef.child(gBoard).removeValue();
        }else {
            dbRef.child(gBoard).child("Move").setValue("99999");
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.multiplayeractivity);

        backButtonConfiguration();

        box = new LinearLayout(this);

        database = FirebaseDatabase.getInstance();
        dbRef = database.getReference();
        init = "0";
        boardCount = 0;
        nextGameBoard = "g";
        beenInitialized = false;
        boardSet = false;
        gBoard = "gBoard dummy";
        end = false;

        multiplayerController = new MultiplayerController();
        model = new Model(0);
        model.turn = -1;
        view = new MultiplayerChessView(this);
        iModel = new InteractionModel();


        dbRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                /**
                 * Iterates through the current database to find either the first
                 * usable board to join and record the board number or find the first
                 * not used game board number to create a new game board
                 */
                if(!boardSet) {
                    for (DataSnapshot ds : dataSnapshot.getChildren()) {
                        if (!ds.getValue().equals("Dummy")) {
                            String intNum = ds.child("Initialize").getValue(String.class);
                            if (intNum.equals("1")) {
                                nextGameBoard = ds.child("BoardNum").getValue(String.class);
                                init = intNum;
                                break;
                            } else if(boardCount == Integer.parseInt(ds.child("BoardNum").getValue(String.class))){
                                boardCount++;
                            }
                        }
                    }

                    /**
                     * checks if the game board found a playable board or start a new one
                     */
                    if(!nextGameBoard.equals("g") && !boardSet){
                        gBoard = "Game" + nextGameBoard;
                        dbRef.child(gBoard).child("BoardNum").setValue(nextGameBoard);
                        boardSet = true;
                        end = false;
                    }
                    else{
                        gBoard = "Game" + boardCount;
                        dbRef.child(gBoard).child("Initialize").setValue("0");
                        dbRef.child(gBoard).child("BoardNum").setValue(boardCount + "");
                        boardSet = true;
                        end = true;
                    }

                    multiplayerController.setgBoard(gBoard);

                    dbRef.child(gBoard).child("Move").setValue("None");
                }


                dbRef.child(gBoard).child("Initialize").addListenerForSingleValueEvent(new ValueEventListener() {
                    /**
                     * initializes the board if a new board has been created or
                     * set the value to in use and start the game
                     * @param dataSnapshot
                     */
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        String move = dataSnapshot.getValue(String.class);
                        if(move.equals("0")){
                            dbRef.child(gBoard).child("Initialize").setValue("1");
                            beenInitialized = true;
                        }
                        else if(move.equals("1") && !beenInitialized){
                            model.updateTeam(1);
                            dbRef.child(gBoard).child("Initialize").setValue("2");
                            model.turn = 0;
                            dbRef.child(gBoard).child("Move").setValue("99997");
                        }

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });


                dbRef.child(gBoard).child("Move").addListenerForSingleValueEvent(new ValueEventListener() {
                    /**
                     * Uses a coded move value to state the next possible action to be done by the
                     * server
                     * 99999: a user has left the game and therefor can be destroyed when the other
                     * user leaves the game
                     * 99997: a second user has joined the game and can therefore set the turn to
                     * 99998: when this is set, that means a king has been removed. Therefore both
                     * instances of the app will check for their current status of model.win, which
                     * will only be true for one user then the proper message will be presented and
                     * the game can end
                     * the first user to have joined (white team)
                     * Possible move: the next possible move is dictated by a 5 digit code
                     * first: the player making the move (0 or 1)
                     * second: the old x value
                     * third the old y value
                     * fourth: the new x value
                     * fifth: the new y value
                     *
                     */
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        String move = dataSnapshot.getValue(String.class);
                        if(move.equals("99999")){
                            view.endGame();
                            end = true;
                        }
                        else if(move.equals("99997")){
                            model.setTurn(0);

                            end = false;
                        }
                        else if(move.equals("99998")){
                            model.turn = -2;
                            if(model.win){
                                model.winGame();
                            }
                            else{
                                model.loseGame();
                            }
                        }
                        else if(Character.getNumericValue(move.charAt(0)) != model.yourTeam && !move.equals("None")){
                            int oldX = 9 - Character.getNumericValue(move.charAt(1));
                            int oldY = 9 - Character.getNumericValue(move.charAt(2));
                            int newX = 9 - Character.getNumericValue(move.charAt(3));
                            int newY = 9 - Character.getNumericValue(move.charAt(4));

                            if(model.checkKingHit(newX, newY)){
                                dbRef.child(gBoard).child("Move").setValue("99998");
                            }
                            else{
                                model.flipTurn();
                            }
                            model.removePiece(new Move(newX, newY));
                            for (GamePiece gp : model.allPieces) {
                                if (gp.currLocation().getX() == oldX && gp.currLocation().getY() == oldY) {
                                    gp.move(new Move(newX, newY));
                                    model.notifySubscribers();
                                }
                            }


                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }


            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        view.setMultiplayerController(multiplayerController);
        view.setIModel(iModel);
        view.setModel(model);
        multiplayerController.setModel(model);
        multiplayerController.setIModel(iModel);
        model.addSubscriber(view);
        iModel.addSubscriber(view);
        view.setOnTouchListener(this);

        detector = new GestureDetector(this, this);

        box.setOrientation(LinearLayout.VERTICAL);
        box.addView(view);
        setContentView(box);
    }

    private void backButtonConfiguration(){
        Button backButton = (Button)findViewById(R.id.backButton);

    }


    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        detector.onTouchEvent(motionEvent);
        return true;
    }

    @Override
    public boolean onDown(MotionEvent motionEvent) {
        multiplayerController.handleDown(motionEvent);
        return true;
    }

    @Override
    public void onShowPress(MotionEvent motionEvent) {

    }

    @Override
    public boolean onSingleTapUp(MotionEvent motionEvent) {
        return false;
    }

    @Override
    public boolean onScroll(MotionEvent motionEvent, MotionEvent motionEvent1, float v, float v1) {
        return false;
    }

    @Override
    public void onLongPress(MotionEvent motionEvent) {

    }

    @Override
    public boolean onFling(MotionEvent motionEvent, MotionEvent motionEvent1, float v, float v1) {
        return false;
    }


}
