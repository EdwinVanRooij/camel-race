package io.github.edwinvanrooij.domain.mexican;

/**
 * Created by eddy
 * on 8/17/17.
 */
public class DiceThrower {
    private Dice firstDice;
    private Dice secondDice;

    public DiceThrower() {
        firstDice = new Dice();
        secondDice = new Dice();
    }

    public Throw generateThrow() {
        firstDice.roll();
        int firstScore = firstDice.getDots();

        secondDice.roll();
        int secondScore = secondDice.getDots();

        int score = calculateScore(firstScore, secondScore);

        return new Throw(firstScore, secondScore, score);
    }

    private int calculateScore(int firstScore, int secondScore) {
        // Handle doubles. 22 becomes 200, 55 becomes 500
        if (firstScore == secondScore) {
            return firstScore * 100;
        }

        // Handle singles. 45 becomes 54, 54 stays 54
        if (firstScore > secondScore) {
            // First score is 4, second is 3. Just add up in string style, to a total of 43.
            return Integer.valueOf(String.valueOf(firstScore) + String.valueOf(secondScore));
        } else {
            // First score is 3, second score is 4. Swap numbers, add up in string style.
            return Integer.valueOf(String.valueOf(secondScore) + String.valueOf(firstScore));
        }
    }
}
