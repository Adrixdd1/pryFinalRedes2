package game.utilities;

import java.awt.*;
import java.io.Serializable;
import java.util.Arrays;

public class SoftSnakePlayer implements Serializable {
    private Point[] body;
    private final Color color;

    public SoftSnakePlayer(Point[] body, Color color) {
        this.body = body;
        this.color = color;
    }

    public Point[] getBody() {
        return body;
    }

    public void setBody(Point[] body) {
        this.body = body;
    }

    public Color getColor() {
        return color;
    }

    @Override
    public String toString() {
        return "SoftSnakePlayer{" +
                "body=" + Arrays.toString(body) +
                ", color=" + color +
                '}';
    }
}
