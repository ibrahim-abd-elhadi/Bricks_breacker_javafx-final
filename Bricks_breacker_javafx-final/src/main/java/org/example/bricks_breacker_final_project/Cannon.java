package org.example.bricks_breacker_final_project;


import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class Cannon extends Rectangle {
    public Cannon(double x, double y, double width, double height) {
        super(x, y, width, height);
        setFill(Color.DARKORANGE);
        setArcHeight(5);
        setArcWidth(5);
    }
}
