package game;

import presentation.Drawable;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class Bird implements Drawable, Steppable {

    private static final double GRAVITY = 1;
    private int x;
    private int y;

    private final int birdHeight;
    private final int birdWidth;

    private double velocity;
    private int count;

    private BufferedImage[] images;

    public Bird(int x, int y) throws IOException {
        this.images = new BufferedImage[]{
                ImageIO.read(new File("/src/main/resources/imgs/bird1.png")),
                ImageIO.read(new File("/src/main/resources/imgs/bird2.png")),
                ImageIO.read(new File("/src/main/resources/imgs/bird3.png")),
                ImageIO.read(new File("/src/main/resources/imgs/bird2.png"))
        };
        this.x = x;
        this.y = y;
        birdHeight = images[0].getHeight();
        birdWidth = images[0].getWidth();
    }

    @Override
    public void show(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;

        int an = (int) Math.min(Math.abs(velocity), 10) * (60 / 10);
        int angle = velocity < 0 ? an * -1 : an;

        int image = count / 3;
        if (image > 3) {
            count = 0;
            image = 0;
        }

        g2.drawImage(rotateImage(images[image], angle), x, y, null);
    }


    public void step() {
        count++;
        velocity += GRAVITY;
        y = y + (int) velocity;
        if (y < 0) y = 0;
    }

    public void action(){
        velocity = -13;
        count = 0;
    }

    public int getBirdHeight() {
        return birdHeight;
    }

    public int getBirdWidth() {
        return birdWidth;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public BufferedImage rotateImage(BufferedImage imag, int n) {
        AffineTransform tx = AffineTransform.getRotateInstance(Math.toRadians(n),
                imag.getWidth() / 2, imag.getHeight() / 2);
        AffineTransformOp op = new AffineTransformOp(tx, AffineTransformOp.TYPE_BILINEAR);
        BufferedImage newImage =new BufferedImage(imag.getWidth(), imag.getHeight(), BufferedImage.TYPE_INT_ARGB);
        op.filter(imag, newImage);
        return newImage;
    }

    public double getVelocity() {
        return velocity;
    }
}
