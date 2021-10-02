package game;

import nn.model.Genome;
import nn.neural.NeuralNetwork;
import presentation.Drawable;

import java.awt.*;
import java.io.*;

public class Game implements Drawable, Steppable {

    Bird bird;
    NeuralNetwork neuralNetwork;
    BottomPanel bottomPanel;
    PipesController pipesController;
    CollisionDetector collisionDetector;
    boolean collision;
    Font font;

    long allSteps;


    public static int WIDTH = 600;
    public static int HEIGHT = 800;

    public Game() {
        reCreateGame();
    }

    private void reCreateGame() {
        collision = false;
        allSteps = 0;
        try {
            this.bird = new Bird(WIDTH / 6, HEIGHT / 2);
            this.neuralNetwork = new NeuralNetwork(loadGenome("/birdGenome_10_0.genome"));
            this.bottomPanel = new BottomPanel(WIDTH, HEIGHT);
            this.pipesController = new PipesController(WIDTH, HEIGHT, WIDTH / 6);
            this.collisionDetector = new CollisionDetector(bottomPanel.getLavaLevel());
            this.font = Font.createFont(Font.TRUETYPE_FONT, new File("/src/main/resources/imgs/font.ttf")).deriveFont(36f);
        } catch (IOException | FontFormatException e) {
            e.printStackTrace();
        }
    }

    public void action() {
//        bird.action();
    }

    @Override
    public void show(Graphics g) {
        g.setColor(Color.CYAN);
        g.fillRect(0,0,480,720);
        pipesController.show(g);
        bird.show(g);

        bottomPanel.show(g);
        g.setFont(font);
        g.setColor(Color.BLACK);
        g.drawString(String.format("%05d", pipesController.getActiveCount()), 200, 40);
//        g.drawString(String.format("%04d", pipesController.getActiveCount()), WIDTH - 100, 40);
    }

    public void step() {
        shouldMakeAnAction(bird);
        bird.step();


        bottomPanel.step();
        pipesController.step();

        if (collision) {
            reCreateGame();
        }
        collision = collisionDetector.collision(bird, pipesController.getCurrentPipe());
        allSteps++;
    }

    private void shouldMakeAnAction(Bird bird) {
        double birdY = (float) bird.getY() / HEIGHT;
        double pipeX = (float) pipesController.getCurrentPipe().getX() / WIDTH;
        double pipeyStart = (float) pipesController.getCurrentPipe().getyOpeningStart() / HEIGHT;
        double pipeyEnd = (float) pipesController.getCurrentPipe().getyOpeningEnd() / HEIGHT;
        double velocity = Math.abs(bird.getVelocity() / -13) - 1;

        double[] calculate = this.neuralNetwork.calculate(new double[]{birdY, pipeX, pipeyStart, pipeyEnd});

        if (calculate[0] > 0.5) bird.action();
    }

    private Genome loadGenome(String filename) {
        FileInputStream fileInputStream
                = null;
        Genome genome2 = null;
        try {
            fileInputStream = new FileInputStream(filename);

        ObjectInputStream objectInputStream
                = new ObjectInputStream(fileInputStream);
         genome2 = (Genome) objectInputStream.readObject();
        objectInputStream.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return genome2;
    }
}
