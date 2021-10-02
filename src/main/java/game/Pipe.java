package game;

import presentation.Drawable;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class Pipe implements Drawable, Steppable {

    private static final int OPENING = 200;

    private static BufferedImage IMAGE;
    private static BufferedImage IMAGE2;

    private int x;
    private int yOpeningStart;
    private int yOpeningEnd;

    private final int width;

    static {
        try {
            IMAGE = ImageIO.read(new File("/src/main/resources/imgs/pipe2.png"));
            IMAGE2 = rotateImage(IMAGE, 180);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Pipe(int x, int yOpeningStart) {
        this.x = x;
        this.yOpeningStart = yOpeningStart;
        this.yOpeningEnd = yOpeningStart + OPENING;
        width = IMAGE.getWidth();
    }

    @Override
    public void step() {
        this.x -= 5;
    }

    @Override
    public void show(Graphics g) {
        g.drawImage(IMAGE, x, yOpeningStart + OPENING, null);
        g.drawImage(IMAGE2, x, yOpeningStart - IMAGE2.getHeight(), null);
    }


    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }


    public int getyOpeningStart() {
        return this.yOpeningStart;
    }

    public int getyOpeningEnd() {
        return this.yOpeningEnd;
    }

    public void setyOpeningStart(int yOpeningStart) {
        this.yOpeningStart = yOpeningStart;
        this.yOpeningEnd = yOpeningStart + OPENING;
    }

    public int getWidth() {
        return width;
    }

    public static BufferedImage rotateImage(BufferedImage imag, int n) {
        double rotationRequired = Math.toRadians(n);
        double locationX = imag.getWidth() / 2;
        double locationY = imag.getHeight() / 2;
        AffineTransform tx = AffineTransform.getRotateInstance(rotationRequired, locationX, locationY);
        AffineTransformOp op = new AffineTransformOp(tx, AffineTransformOp.TYPE_BILINEAR);
        BufferedImage newImage =new BufferedImage(imag.getWidth(), imag.getHeight(), BufferedImage.TYPE_INT_ARGB);
        op.filter(imag, newImage);

        return newImage;
    }
}
