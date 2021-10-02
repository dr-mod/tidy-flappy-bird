package game;

public class CollisionDetector {

    private final int screenBottom;

    public CollisionDetector(int screenBottom) {
        this.screenBottom = screenBottom;
    }

    public boolean collision(Bird bird, Pipe pipe) {

        int birdBottom = bird.getY() + bird.getBirdHeight();
        if (screenBottom < birdBottom) return true;


        int birdsFurthestPoint = bird.getX() + bird.getBirdWidth();
        if (pipe.getX() < birdsFurthestPoint  && pipe.getX() + pipe.getWidth() > bird.getX()) {
            if (bird.getY() < pipe.getyOpeningStart() || birdBottom > pipe.getyOpeningEnd()) {
                return true;
            }
        }
        return false;
    }
}
