package game;

import nn.model.Genome;
import nn.model.Population;
import nn.neural.NeuralNetwork;
import presentation.Drawable;

import java.awt.*;
import java.io.*;
import java.util.*;
import java.util.List;

public class GameNN implements Drawable, Steppable {

    public static final int BIRDS_NUMBER = 1000;
    List<Bird> birds;
    BottomPanel bottomPanel;
    PipesController pipesController;
    CollisionDetector collisionDetector;
    boolean collision;
    Font font;


    Population population;

    Map<Genome, Double> genomeFitnessMap;
    Map<NeuralNetwork, Genome> nnGenomeMap;
    Map<Bird, NeuralNetwork> birdNNMap;
    long allSteps;



    public static int WIDTH = 600;
    public static int HEIGHT = 800;
    private int attempt;

    public GameNN() {
        population = new Population(new Random(1), BIRDS_NUMBER, null);
        reCreateGame();
    }

    private void reCreateGame() {
        nnGenomeMap = new HashMap<>();
        birdNNMap = new HashMap<>();
        genomeFitnessMap = new HashMap<>();
        allSteps = 0;
        System.out.println("Generation: " + ++this.attempt);
        this.birds = new ArrayList<>();
        try {
            for (int i = 0; i < BIRDS_NUMBER; i++) {
                this.birds.add(new Bird(WIDTH / 6, HEIGHT / 2));
            }
            for (int i = 0; i < population.getGenomes().size(); i++) {
                var genome = population.getGenomes().get(i);
                NeuralNetwork neuralNetwork = new NeuralNetwork(genome);
                nnGenomeMap.put(neuralNetwork, genome);
                birdNNMap.put(birds.get(i), neuralNetwork);
            }

            this.bottomPanel = new BottomPanel(WIDTH, HEIGHT);
            this.pipesController = new PipesController(WIDTH, HEIGHT, WIDTH / 6);
            this.collisionDetector = new CollisionDetector(bottomPanel.getLavaLevel());
            this.font = Font.createFont(Font.TRUETYPE_FONT, new File("/Users/drmod/Downloads/imgs/font.ttf")).deriveFont(36f);
        } catch (IOException | FontFormatException e) {
            e.printStackTrace();
        }
    }

    public void action() {
//        bird.action();
    }

    @Override
    public void show(Graphics g) {
        pipesController.show(g);
//        for (Bird bird : birds) {
//            bird.show(g);
//        }

        Iterator<Bird> iterator = birds.iterator();
        int i = 0;
        while (iterator.hasNext() && i < 10) {
            iterator.next().show(g);
            i++;
        }

        bottomPanel.show(g);
        g.setFont(font);
        g.setColor(Color.WHITE);
        g.drawString(String.format("%04d", pipesController.getActiveCount()),WIDTH - 100,40);
    }

    public void step() {
        Iterator<Bird> iterator = birds.iterator();
        while (iterator.hasNext()) {
            Bird bird = iterator.next();
            try{
                shouldMakeAnAction(bird);
                bird.step();
            } catch (RuntimeException e) {
                saveFitness(bird, 0);
                iterator.remove();
            }

        }

        bottomPanel.step();
        pipesController.step();

//        if (collision) {
//            reCreateGame();
//        }

        iterator = birds.iterator();
        while (iterator.hasNext()) {
        Bird bird = iterator.next();
            if (collisionDetector.collision(bird, pipesController.getCurrentPipe())) {
                int activeCount = pipesController.getActiveCount();
                float stepsFitness = allSteps / 100f;

                int entranceCenter = Math.abs(pipesController.getCurrentPipe().getyOpeningEnd() - pipesController.getCurrentPipe().getyOpeningStart()) / 2 + pipesController.getCurrentPipe().getyOpeningStart();
                double fromEntranceFitness = 1 - (Math.abs(entranceCenter - bird.getY()) / (double) HEIGHT * 1);

                double fitness = Math.pow(activeCount * 10, 2) + stepsFitness + fromEntranceFitness;
                saveFitness(bird, fitness);
                if(birds.size() == 1) {
                    System.out.println("Pipes:" + pipesController.getActiveCount());
                }
                iterator.remove();
            }
        }
        if (birds.isEmpty()) {
            population.evaluate(genomeFitnessMap);
            reCreateGame();
//            this.collision = true;
        }
        allSteps++;


//        if (attempt >= 10 && pipesController.getActiveCount() > 30000) {
//            FileOutputStream fileOutputStream = null;
//            try {
//                for (int i = 0; i < birds.size(); i++) {
//                    fileOutputStream = new FileOutputStream("birdGenome_"+attempt+"_"+i+".genome");
//                    ObjectOutputStream objectOutputStream
//                            = new ObjectOutputStream(fileOutputStream);
////                Genome genome = population.best.getGenome();
//                    Genome genome = nnGenomeMap.get(birdNNMap.get(birds.get(i)));
//                    objectOutputStream.writeObject(genome);
//                    objectOutputStream.flush();
//                    objectOutputStream.close();
//                }
//
//            } catch (FileNotFoundException e) {
//                e.printStackTrace();
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//            System.out.println("Reached, exiting");
//            System.exit(0);
//        }
    }

    private void saveFitness(Bird bird, double fitness) {
        NeuralNetwork neuralNetwork = birdNNMap.get(bird);
        Genome genome = nnGenomeMap.get(neuralNetwork);
        genomeFitnessMap.put(genome, fitness);
    }

    private void shouldMakeAnAction(Bird bird) {
        NeuralNetwork neuralNetwork = this.birdNNMap.get(bird);
        double birdY = (float) bird.getY() / HEIGHT;
        double pipeX = (float) pipesController.getCurrentPipe().getX() / WIDTH;
        double pipeyStart = (float) pipesController.getCurrentPipe().getyOpeningStart() / HEIGHT;
        double pipeyEnd = (float) pipesController.getCurrentPipe().getyOpeningEnd() / HEIGHT;
        double velocity = Math.abs(bird.getVelocity() / -13) - 1;

        double[] calculate = neuralNetwork.calculate(new double[]{birdY, pipeX, pipeyStart, pipeyEnd});

        if (calculate[0] > 0.5) bird.action();
    }


}
