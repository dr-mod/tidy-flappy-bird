package game;

import presentation.Drawable;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class BottomPanel implements Drawable, Steppable {

    private final BufferedImage image;

    private int x[];
    private int y;

    private final int imageWidth;
    private final int lavaLevel;

    public BottomPanel(int width, int height) throws IOException {
        image = ImageIO.read(new File("/src/main/resources/imgs/base.png"));
        imageWidth = image.getWidth();
        lavaLevel = height - image.getHeight();
        int numberOfPlates = width / imageWidth + 2;
        this.x = new int[numberOfPlates];
        for (int i = 0; i < this.x.length; i++) {
            this.x[i] = i * imageWidth;
        }
        this.y = height - image.getHeight();
    }


    @Override
    public void step() {
        for (int i = 0; i < this.x.length; i++) {
            if (this.x[i] + imageWidth < 0) {
                int max = -1;
                for (int j = 0; j < this.x.length; j++) {
                    max = Math.max(this.x[j], max);
                }
                this.x[i] = max + imageWidth;
            }
        }
        for (int i = 0; i < this.x.length; i++) {
            this.x[i] -= 5;
        }
    }

    @Override
    public void show(Graphics g) {
        for (int i = 0; i < this.x.length; i++) {
            g.drawImage(image, this.x[i], y, null);
        }
    }

    public int getLavaLevel() {
        return lavaLevel;
    }
}
