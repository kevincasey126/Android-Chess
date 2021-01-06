package com.example.kiss5.a306_project;

public class Move {
    double currX, currY;

    public Move(double x, double y){
        currX = x;
        currY = y;
    }

    public double getX(){
        return this.currX;
    }

    public double getY() {
        return currY;
    }

    @Override
    public boolean equals(Object o){
        Move temp = (Move)o;
        return temp.getX() == this.getX() && this.getY() == temp.getY();
    }
}
