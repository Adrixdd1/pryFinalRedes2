package game.utilities;

import java.awt.Color;
import java.awt.Point;
import java.io.Serial;
import java.io.Serializable;
import java.util.ArrayList;

public class SnakePlayer {
    private ArrayList<Point> body;
    private String direction;
    private final Color color;
    private final int stepSize;
    private final int boardWidth;
    private final int boardHeight;
    private boolean active;

    public SnakePlayer(Point start, String direction, Color color, int stepSize, int boardWidth, int boardHeight) {
        this.body = new ArrayList<>();
        this.body.add(start);
        this.direction = direction;
        this.color = color;
        this.stepSize = stepSize;
        this.boardWidth = boardWidth;
        this.boardHeight = boardHeight;
        this.active = true;
    }

    public boolean isActive() { return active; }
    public void setActive(boolean active) { this.active = active; }
    public Point getHead() {
        return body.get(0);
    }

    public ArrayList<Point> getBody() {
        return body;
    }

    public String getDirection() {
        return direction;
    }

    public void setDirection(String d) {
        if ((this.direction.equals("RIGHT") && d.equals("LEFT")) ||
                (this.direction.equals("LEFT") && d.equals("RIGHT")) ||
                (this.direction.equals("UP") && d.equals("DOWN")) ||
                (this.direction.equals("DOWN") && d.equals("UP"))) {
            return;
        }
        this.direction = d;
    }

    private Point getNextHead() {
        Point head = getHead();
        Point newHead = new Point(head);

        switch (direction) {
            case "RIGHT" -> newHead.x = (newHead.x + stepSize) % boardWidth;
            case "LEFT" -> newHead.x = (newHead.x - stepSize + boardWidth) % boardWidth;
            case "UP" -> newHead.y = (newHead.y - stepSize + boardHeight) % boardHeight;
            case "DOWN" -> newHead.y = (newHead.y + stepSize) % boardHeight;
        }
        return newHead;
    }

    public boolean move() {
        Point newHead = getNextHead();
        if (body.contains(newHead)) {
            return true; // Colisión con sí mismo
        }
        body.add(0, newHead);
        body.remove(body.size() - 1);
        return false;
    }

    public void grow() {
        body.add(0, getNextHead());
    }

    public Color getColor() {
        return color;
    }
}
