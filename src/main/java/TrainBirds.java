import game.GameNN;

public class TrainBirds {
    public static void main(String[] args) {
        GameNN game = new GameNN();
        while (true) {
            game.step();
        }
    }
}
