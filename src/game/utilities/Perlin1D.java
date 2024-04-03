package game.utilities;

public class Perlin1D {
    public double amp;
    public double freq;

    public Perlin1D(double amp, double freq) {
        this.amp = amp;
        this.freq = freq;
    }

    public double getHeight(double x){
        return x;
    }
}
