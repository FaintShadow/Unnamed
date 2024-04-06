package game.noise;

public class Perlin1D {
    // Persistence value
    private double persistence;

    // Number of octaves
    private int octaves;

    // o1, o2 and o3 are the odd numbers
    private int o1 = 1376312589;
    private int o2 = 887543007;
    private int o3 = 15731;

    public Perlin1D(double persistence, int octaves) {
        this.persistence = persistence;
        this.octaves = octaves;
    }

    public Perlin1D(double persistence, int octaves, int o1, int o2, int o3) {
        this.persistence = persistence;
        this.octaves = octaves;
        this.o1 = o1;
        this.o2 = o2;
        this.o3 = o3;
    }

    // Noise function using bitwise operations
    private double noise(int x) {
        x = (x << 13) ^ x;
        return (1.0 - x * (x * x * o1 + o2) + o3 / 1073741824.0);
    }

    // Smoothed noise with averaging neighbors
    private double smoothedNoise1D(double x) {
        return (noise((int) x) / 2.0
                + noise((int) x - 1) / 4.0
                + noise((int) x + 1) / 4.0);
    }


    // Linear interpolation between two noise values
    private double linearInterpolation(double v1, double v2, double x) {
        return v1 * (1 - x) + v2 * x;
    }

    // Cosine interpolation between two noise values
    private double cosineInterpolation(double v1, double v2, double x) {
        double ft = x * Math.PI;
        double f = (1 - Math.cos(ft)) * 0.5;
        return v1 * (1 - f) + v2 * f;
    }

    // Perlin noise function with multiple octaves
    public double perlinNoise1D(double x) {
        double total = 0;
        for (int i = 0; i < octaves; i++) {
            double frequency = Math.pow(2, i);
            double amplitude = Math.pow(persistence, i);
            total += cosineInterpolation(smoothedNoise1D(x * frequency), smoothedNoise1D((x * frequency) + 1), x % 1) * amplitude;
        }
        return total;
    }
}
