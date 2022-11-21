package service;

public class Generator {
    public static int getRandomID(int min, int max) {
        return (int) ((Math.random() * (max - min)) + min);
    }
}
