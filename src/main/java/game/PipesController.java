package game;

import presentation.Drawable;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class PipesController implements Drawable, Steppable {

    List<Pipe> pipes;
    private Pipe currentPipe;
    private final int xMarker;
    private Pipe lastPipe;
    Random random = new Random(1);
    public static final int PIPES_GAP = 400;
    private int activeCount = 0;

    public PipesController(int width, int height, int xMarker) {
        this.pipes = new ArrayList<>();
        this.xMarker = xMarker;
        int pipesCount = width / PIPES_GAP + 1;
        for (int i = 0; i < pipesCount; i++) {
            pipes.add(new Pipe(width + PIPES_GAP * i, random.nextInt(500) + 50));
        }
        currentPipe = pipes.get(0);
        lastPipe = pipes.get(pipes.size() - 1);
    }

    @Override
    public void show(Graphics g) {
        pipes.forEach(it -> it.show(g));
    }

    @Override
    public void step() {
        pipes.forEach(Pipe::step);
        repurposePipes();
        checkCurrentPipe();
    }

    private void checkCurrentPipe() {
        if (currentPipe.getX() + currentPipe.getWidth() < this.xMarker) {
            Pipe nextCurrent = null;
            for(Pipe pipe : pipes) {
                if (pipe.getX() > this.xMarker) {
                    if (nextCurrent == null) nextCurrent = pipe;
                    else if (pipe.getX() < nextCurrent.getX()) nextCurrent = pipe;
                }
            }
            currentPipe = nextCurrent;
            activeCount++;
        }
    }

    private void repurposePipes() {
        if (pipes.get(0).getX() < -40) {
            Pipe pipe = pipes.remove(0);
            pipe.setX(lastPipe.getX() + PIPES_GAP);
            pipe.setyOpeningStart(random.nextInt(500) + 50);
            pipes.add(pipe);
            lastPipe = pipe;
        }
    }

    public int getActiveCount() {
        return activeCount;
    }

    public Pipe getCurrentPipe() {
        return currentPipe;
    }
}
