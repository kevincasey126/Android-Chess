package com.example.kiss5.a306_project;

import android.util.Pair;

import java.util.ArrayList;
import java.util.Iterator;

public class ChessAI {

    public ChessAI(){

    }

    /**
     * evaluate the value on the board based on each pieces value
     * @param board is the current gameboard to evaluate
     * @return the evaluation value
     */
    public double evaluate(ArrayList<GamePiece> board){
        double score = 0;
        for(GamePiece gp : board){
            if(gp.getTeam() == 0){
                score -= gp.getScore();
            }
            else{
                score += gp.getScore();
            }
        }
        return score;
    }

    /**
     * MiniMax finds the largest scoring play based on the current game board using recursion with miniMin
     * @param board current game board to examine
     * @param turn who's turn it is
     * @param depth how many levels should be examined before hitting a base case
     * @return return the move and game piece that was used in this iteration to find the highest score
     */
    public Object[] miniMax(ArrayList<GamePiece> board, int turn, int depth){
        Object[] playerAndMove = new Object[3];
        double max = Double.NEGATIVE_INFINITY;
        playerAndMove[0] = null;
        playerAndMove[1] = null;
        playerAndMove[2] = evaluate(board);

        if(depth == 0){
            return playerAndMove;
        }
        //deep clone with a board flip and king check
        int kingCount = 0;
        ArrayList<GamePiece> flippedBoard = new ArrayList<>();
        for(GamePiece gp : board){
            try {
                GamePiece copied = gp.clone();
                if(copied.getPieceType().equals("King")){
                    kingCount += 1;
                }
                copied.move(new Move(Math.abs(9 - gp.currLocation().getX()), Math.abs(9 - gp.currLocation().getY())));
                flippedBoard.add(copied);
            }
            catch (Exception e){}
        }
        //checking if there are enough kings on the board to continue
        if(kingCount < 2){
            return playerAndMove;
        }
        ArrayList<Pair<Move, GamePiece>> flippedMoves = new ArrayList<>();

        for(GamePiece gp : flippedBoard) {
            if(gp.getTeam() == turn) {
                for (Move move : gp.findMoves(flippedBoard)) {
                    flippedMoves.add(new Pair<>(move, gp));
                }
            }
        }


         for(Pair<Move, GamePiece> pair : flippedMoves){
            ArrayList<GamePiece> newBoard = new ArrayList<>();
            for(GamePiece flippedGP : flippedBoard){
                try{
                    GamePiece copy = flippedGP.clone();
                    newBoard.add(copy);
                }
                catch (Exception e) {}
            }

            Iterator<GamePiece> iterator = newBoard.iterator();

            while(iterator.hasNext()){
                GamePiece gamePiece = iterator.next();
                if(gamePiece.currLocation().equals(pair.first)){
                    newBoard.remove(gamePiece);
                    break;
                }
            }

            for(GamePiece newGP : newBoard){
                if(newGP.currLocation().equals(pair.second.currLocation())){
                    newGP.move(pair.first);
                    break;
                }
            }

            Object[] score = miniMin(newBoard, Math.abs(turn - 1), depth - 1);

             if((double)score[2] > max){
                playerAndMove[0] = pair.first;
                playerAndMove[1] = pair.second;
                playerAndMove[2] = score[2];

                max = (double)score[2];
            }

        }
        return playerAndMove;
    }

    /**
     * MiniMin finds the smallest scoring play based on the current game board using recursion with miniMax
     * @param board current game board to examine
     * @param turn who's turn it is
     * @param depth how many levels should be examined before hitting a base case
     * @return return the move and game piece that was used in this iteration to find the lowest score
     */
    public Object[] miniMin(ArrayList<GamePiece> board, int turn, int depth){
        Object[] playerAndMove = new Object[3];
        double min = Double.POSITIVE_INFINITY;
        playerAndMove[0] = null;
        playerAndMove[1] = null;
        playerAndMove[2] = evaluate(board);

        if(depth == 0){
            return playerAndMove;
        }
        //deep clone with a board flip and king check
        int kingCount = 0;
        ArrayList<GamePiece> flippedBoard = new ArrayList<>();
        for(GamePiece gp : board){
            try {
                GamePiece copied = gp.clone();
                if(copied.getPieceType().equals("King")){
                    kingCount += 1;
                }
                copied.move(new Move(Math.abs(9 - gp.currLocation().getX()), Math.abs(9 - gp.currLocation().getY())));
                flippedBoard.add(copied);
            }
            catch (Exception e){}
        }
        //checking if there are enough kings on the board to continue
        if(kingCount < 2){
            return playerAndMove;
        }

        ArrayList<Pair<Move, GamePiece>> flippedMoves = new ArrayList<>();

        for(GamePiece gp : flippedBoard) {
            if(gp.getTeam() == turn) {
                for (Move move : gp.findMoves(flippedBoard)) {
                    flippedMoves.add(new Pair<>(move, gp));
                }
            }
        }


        for(Pair<Move, GamePiece> pair : flippedMoves){
            ArrayList<GamePiece> newBoard = new ArrayList<>();
            for(GamePiece flippedGP : flippedBoard){
                try{
                    GamePiece copy = flippedGP.clone();
                    newBoard.add(copy);
                }
                catch (Exception e) {}
            }

            Iterator<GamePiece> iterator = newBoard.iterator();

            while(iterator.hasNext()){
                GamePiece gamePiece = iterator.next();
                if(gamePiece.currLocation().equals(pair.first)){
                    newBoard.remove(gamePiece);
                    break;
                }
            }

            for(GamePiece newGP : newBoard){
                if(newGP.currLocation().equals(pair.second.currLocation())){
                    newGP.move(pair.first);
                    break;
                }
            }

            Object[] score = miniMax(newBoard, Math.abs(turn - 1), depth - 1);

            if((double)score[2] < (min)){
                playerAndMove[0] = pair.first;
                playerAndMove[1] = pair.second;
                playerAndMove[2] = score[2];

                min = (double)score[2];
            }

        }

        return playerAndMove;
    }



}
