package com.example.kiss5.a306_project;

public class Board {
    double x, y, width, height;

    public Board(double newX, double newY, double newWidth, double newHeight){
        x = newX;
        y = newY;
        width = newWidth;
        height = newHeight;
    }

    public float getLeft(){
        return (float)x;
    }

    public float getTop(){
        return (float)y;
    }

    public float getRight(){
        return (float)(x + width);
    }

    public float getBottom(){
        return (float)(y + height);
    }
}
