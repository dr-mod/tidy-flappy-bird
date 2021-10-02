package nn.model;

public abstract class Innovator {

     private int counter = 1;

    public Innovator(int counter) {
        this.counter = counter;
    }

    public int nextInnovation() {
         return this.counter++;
     }
}
