package org.minutebots.robot.utilities;

import java.lang.Math;

import edu.wpi.first.wpilibj.Timer;

public final class utilities {

    private utilities() {
    }
    private static Timer timer = new Timer();


    //this starts a timer
    public static void startTimer() {

        timer.start();
    }

    //this gets how long the timer has been running
    public static double getTime() {

        return timer.get();
    }

    //checks if 2 numbers are a certain distance apart.
    public static boolean epsilonEquals(double a, double b, double epsilon) {
        return (a - epsilon <= b) && (a + epsilon >= b);
    }

    //this checks if the input is within the bounds, and if it isn't, it matches to the nearest bound.
    public static double limit(double input, double lowerBound, double upperBound) {

        if (input < lowerBound)
            return lowerBound;
        else if (input > upperBound)
            return upperBound;
        else
            return input;
    }


    //check if input is within an upper bound and the upper bound *-1
    public static double limit(double input, double bound) {

        return limit(input, -bound, bound);
    }


    //input value raised an exponent (curved)
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

    //input needs to be at a certain magnitude, if not return 0, if it is return input
    static double deadZone(double input) {

        if ((input > -0) && (input < 0))  //needs a constant where 0 is, keep the negative on the first one
            return 0.0;
        else
            return input;
    }

    //this limits how fast you can accelerate
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

    //finds the difference between 2 angles
    public static double angleDifference(double startingAngle, double desiredAngle) {
        double difference = desiredAngle - startingAngle;
        return angleConverter(difference);
    }

    //converts angles to between -180 and 180
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

    //adjusts for wheels getting stuck
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