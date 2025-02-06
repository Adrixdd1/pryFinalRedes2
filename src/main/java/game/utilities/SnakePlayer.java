package game.utilities;

import java.awt.Color;
import java.awt.Point;
import java.util.ArrayList;

public class SnakePlayer {
    private ArrayList<Point> body;
    private String direction;
    private Color color;
    private int stepSize;
    private int boardWidth;
    private int boardHeight;

    public SnakePlayer(Point start, String direction, Color color, int stepSize, int boardWidth, int boardHeight) {
        this.body = new ArrayList<>();
        this.body.add(start);
        this.direction = direction;
        this.color = color;
        this.stepSize = stepSize;
        this.boardWidth = boardWidth;
        this.boardHeight = boardHeight;
    }

    public Point getHead() {
        return body.get(0);
    }

    public ArrayList<Point> getBody() {
        return body;
    }

    public String getDirection() {
        return direction;
    }

    // Evita la inversión directa de la dirección
    public void setDirection(String d) {
        if ((this.direction.equals("RIGHT") && d.equals("LEFT")) ||
                (this.direction.equals("LEFT") && d.equals("RIGHT")) ||
                (this.direction.equals("UP") && d.equals("DOWN")) ||
                (this.direction.equals("DOWN") && d.equals("UP"))) {
            return;
        }
        this.direction = d;
    }

    public boolean move() {
        Point head = getHead();
        Point newHead = new Point(head.x, head.y);

        switch (direction) {
            case "RIGHT" -> {
                newHead.x += stepSize;
                if (newHead.x >= boardWidth) {
                    newHead.x = 0;
                }
            }
            case "LEFT" -> {
                newHead.x -= stepSize;
                if (newHead.x < 0) {
                    newHead.x = boardWidth - stepSize;
                }
            }
            case "UP" -> {
                newHead.y -= stepSize;
                if (newHead.y < 0) {
                    newHead.y = boardHeight - stepSize;
                }
            }
            case "DOWN" -> {
                newHead.y += stepSize;
                if (newHead.y >= boardHeight) {
                    newHead.y = 0;
                }
            }
        }
        long colisiones=body.stream().filter(e->newHead.distance(e)<stepSize).count();
        if (colisiones>0){
            return true;
        }
        body.addFirst(newHead);
        body.removeLast();
        return false ;
        // Aquí podrías agregar comprobación de colisión consigo misma
    }

    // Hace crecer la serpiente (por ejemplo, al comer)
    public void grow() {
        Point head = getHead();
        Point newHead = new Point(head.x, head.y);
        if (direction.equals("RIGHT")) {
            newHead.x += stepSize;
            if (newHead.x >= boardWidth) {
                newHead.x = 0;
            }
        } else if (direction.equals("LEFT")) {
            newHead.x -= stepSize;
            if (newHead.x < 0) {
                newHead.x = boardWidth - stepSize;
            }
        } else if (direction.equals("UP")) {
            newHead.y -= stepSize;
            if (newHead.y < 0) {
                newHead.y = boardHeight - stepSize;
            }
        } else if (direction.equals("DOWN")) {
            newHead.y += stepSize;
            if (newHead.y >= boardHeight) {
                newHead.y = 0;
            }
        }
        body.add(0, newHead);
    }

    public Color getColor() {
        return color;
    }
}
