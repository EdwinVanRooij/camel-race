package io.github.edwinvanrooij.domain.mexican;

import java.util.Random;

/**
 * Created by eddy
 * on 8/17/17.
 */
public class Dice {
    private int dots;
    private static int sides = 6;
    private static Random random = new Random();

    public int getDots() {
        return dots;
    }

    public void setDots(int dots) {
        this.dots = dots;
    }

    public static int getSides() {
        return sides;
    }

    public static void setSides(int sides) {
        Dice.sides = sides;
    }

    public Dice() {
    }

    public void roll() {
        dots = random.nextInt(sides) + 1;
    }
}
