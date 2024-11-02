package game.generation.noise;

public class Perlin1D {
    private final double persistence;
    private final int octaves;
    private final int seed;
    private double incremental = 0.0005;

    private static final int DEFAULT_SEED = 42;

    public Perlin1D(double persistence, int octaves) {
        this(persistence, octaves, DEFAULT_SEED);
    }

    public Perlin1D(double persistence, int octaves, int seed) {
        this.persistence = persistence;
        this.octaves = octaves;
        this.seed = seed;
    }

    private double noise(int x) {
        x = (x << 13) ^ x;
        x = (x * (x * x * 15731 + 789221) + 1376312589) & 0x7fffffff;
        return 1.0 - (x / 1073741824.0);
    }

    private double smoothedNoise(double x) {
        int intX = (int) x;
        double fractX = x - intX;
        return interpolate(
                noise(intX),
                noise(intX + 1),
                fractX
        );
    }

    private double interpolate(double a, double b, double t) {
        double ft = t * Math.PI;
        double f = (1 - Math.cos(ft)) * 0.5;
        return a * (1 - f) + b * f;
    }

    public double getHeight(double x) {
        double total = 0;
        double frequency = 1;
        double amplitude = 1;
        double maxValue = 0;
        x += seed;

        for (int i = 0; i < octaves; i++) {
            total += smoothedNoise(x * frequency) * amplitude;
            maxValue += amplitude;
            frequency *= 2;
            amplitude *= persistence;
        }

        return maxValue == 0 ? 0 : total / maxValue;
    }

    public double getIncremental() {
        return incremental;
    }

    public void setIncremental(double incremental) {
        this.incremental = incremental;
    }
}