package hu.indicium.speurtocht.utils;

public class ScoreCalculator {
    public static long calculatedScore(long challengePoints, long picturesApproved) {
        double scoreMultiplier = 1 + (0.1 * picturesApproved);
        return (long) Math.ceil(challengePoints * scoreMultiplier);
    }
}
