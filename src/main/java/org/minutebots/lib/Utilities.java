package org.minutebots.lib;

import edu.wpi.first.wpilibj.Timer;

public final class Utilities {

    private Utilities() {
    }

    private static Timer timer = new Timer();


    /**
     * This initiates the timer object.
     */
    public static void startTimer() {

        timer.start();
    }

    /**
     * @return How long the timer has been running.
     */
    public static double getTime() {

        return timer.get();
    }

    /**
     * @param a The first number
     * @param b The second number.
     * @param epsilon how far apart they should be
     * @return True or false based on if the 2 numbers are epsilon apart from each other.
     */
    public static boolean epsilonEquals(double a, double b, double epsilon) {
        return (a - epsilon <= b) && (a + epsilon >= b);
    }

    /**
     * @param input Number in question
     * @param lowerBound How low the number should be
     * @param upperBound How high the number should be
     * @return If the input is within the bounds it will return the input, if not it will return the nearest bound.
     */

    public static double limit(double input, double lowerBound, double upperBound) {

        if (input < lowerBound)
            return lowerBound;
        else if (input > upperBound)
            return upperBound;
        else
            return input;
    }


    /**
     * @param input Number in question.
     * @param bound This is the upper bound, the lower bound is bound*-1.
     * @return If the input is within the bounds it will return the input, if not it will return the nearest bound.
     */
    public static double limit(double input, double bound) {

        return limit(input, -bound, bound);
    }


    /**
     * @param input Number being affected.
     * @param curve Power that the input is raised to.
     * @return input to the power of curve
     */
    public static double speedCurve(double input, double curve) {
        //negative curves cause asymptotes, leading to overflow errors. Curves smaller than 0.1 aren't very useful.
        double adjustedCurve = limit(curve, 0.10, Double.MAX_VALUE);
        double adjustedInput = limit(input, 1.0);
        //if the input is negative, outputs can be undefined and positive for certain curves
        if (input < 0.0) {
            return -Math.pow(Math.abs(adjustedInput), adjustedCurve);
        }

        return Math.pow(adjustedInput, adjustedCurve);
    }

    /**
     * @param input Number that is being checked.
     * @return If input is outside of deadzone, return input. If not return 0.0.
     */
    static double deadZone(double input) {

        if ((input > -0) && (input < 0))  //needs a constant where 0 is, keep the negative on the first one
            return 0.0;
        else
            return input;
    }

    /**
     * @param throttle How fast we are telling the robot to go.
     * @param prevThrottle What the last throttle was.
     * @param fullSpeedTime How long it should take to get to full speed *up for rephrasing
     * @return Return how much to speed up.
     */
    public static double accelLimit(double throttle, double prevThrottle, double fullSpeedTime) {

        double finalThrottle = throttle;

        double throttleDiff = throttle - prevThrottle;

        double accelerationLimit = 0.02 / fullSpeedTime;


        if (throttleDiff > accelerationLimit)
            finalThrottle = prevThrottle + accelerationLimit;
        else if (throttleDiff < -accelerationLimit)
            finalThrottle = prevThrottle - accelerationLimit;

        return finalThrottle;
    }

    /**
     * @param startingAngle What angle the robot is at.
     * @param desiredAngle What angle you want the robot to be at.
     * @return Returns the difference between the two angels.
     */
    public static double angleDifference(double startingAngle, double desiredAngle) {
        double difference = desiredAngle - startingAngle;
        return angleConverter(difference);
    }

    /**
     * @param ang Angle being converted.
     * @return Angle but from -180 to 180 instead of 0 to 360.
     */
    private static double angleConverter(double ang) {

        ang = ang % 360.0;
        if (ang > 180.0) {
            ang = ang - 360.0;
        }
        if (ang < -180.0) {
            ang = ang + 360.0;
        }
        return (ang);
    }

    /**
     * @param velocity    The current velocity of the motor system in feet per second
     * @param stiction    The static friction of the motor system in throttle
     * @param maxVelocity The max velocity of the motor system in feet per second
     * @return throttle A throttle within the focused range of values which will actuate
     */
    public static double adjustForStiction(double velocity, double stiction, double maxVelocity) {

        if (velocity > -0.001 && velocity < 0.001) {

            return 0.0;
        } else {

            double velocityToThrottle = velocity / maxVelocity;

            double focusedRange = 1.0 - stiction;

            if (velocity < 0.0) {

                return velocityToThrottle * focusedRange - stiction;
            } else {

                return velocityToThrottle * focusedRange + stiction;
            }
        }
    }


}
