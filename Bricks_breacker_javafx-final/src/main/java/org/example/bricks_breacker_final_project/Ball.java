package org.example.bricks_breacker_final_project;

import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

public class Ball extends Circle {
    private double vx;
    private double vy;

    public Ball(double centerX, double centerY, double radius, double vx, double vy) {
        super(centerX, centerY, radius, Color.LIGHTPINK);
        this.vx = vx;
        this.vy = vy;
        setStroke(Color.RED);
        setStrokeWidth(3);
    }

    public void updatePosition() {
        setCenterX(getCenterX() + vx);
        setCenterY(getCenterY() + vy);
    }

    public void reverseX() {
        vx *= -1;
    }

    public void reverseY() {
        vy *= -1;
    }
    public double getVx() {
        return vx;
    }

    public double getVy() {
        return vy;
    }

    public void setVx(double vx) {
        this.vx = vx;
    }

    public void setVy(double vy) {
        this.vy = vy;
    }
}
