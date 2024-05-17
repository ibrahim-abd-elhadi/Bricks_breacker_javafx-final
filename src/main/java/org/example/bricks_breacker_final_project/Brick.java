package org.example.bricks_breacker_final_project;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;

public class Brick extends Rectangle {
    private int health;
    private Text healthText;

    public Brick(double x, double y, double size, int health, Color color) {
        super(x, y, size, size);
        this.health = health;
        setFill(color);
        setArcWidth(20);
        setArcHeight(20);
        setStroke(Color.AZURE);
        setStrokeWidth(3);

        healthText = new Text(Integer.toString(health));
        healthText.setFont(javafx.scene.text.Font.font("Arial", javafx.scene.text.FontWeight.BOLD, 14));
        healthText.setFill(Color.WHITE);
        healthText.setX(x + size / 2 - 7);
        healthText.setY(y + size / 2 + 5);
    }

    public int getHealth() {
        return health;
    }

    public Text getHealthText() {
        return healthText;
    }

    public void decreaseHealth() {
        health--;
        if (health <= 0) {
            setVisible(false);
            healthText.setVisible(false);
        } else {
            healthText.setText(Integer.toString(health));
        }
    }
}
