package org.minutebots.lib

import edu.wpi.first.wpilibj.Timer

object Utilities {

    private val timer = Timer()

    /**
     * @return How long the timer has been running.
     */
    val time: Double
        get() = timer.get()


    /**
     * This initiates the timer object.
     */
    fun startTimer() {

        timer.start()
    }

    /**
     * @param a The first number
     * @param b The second number.
     * @param epsilon how far apart they should be
     * @return True or false based on if the 2 numbers are epsilon apart from each other.
     */
    fun epsilonEquals(a: Double, b: Double, epsilon: Double): Boolean {
        return a - epsilon <= b && a + epsilon >= b
    }

    /**
     * @param input Number in question
     * @param lowerBound How low the number should be
     * @param upperBound How high the number should be
     * @return If the input is within the bounds it will return the input, if not it will return the nearest bound.
     */

    fun limit(input: Double, lowerBound: Double, upperBound: Double): Double {

        return if (input < lowerBound)
            lowerBound
        else if (input > upperBound)
            upperBound
        else
            input
    }


    /**
     * @param input Number in question.
     * @param bound This is the upper bound, the lower bound is bound*-1.
     * @return If the input is within the bounds it will return the input, if not it will return the nearest bound.
     */
    fun limit(input: Double, bound: Double): Double {

        return limit(input, -bound, bound)
    }


    /**
     * @param input Number being affected.
     * @param curve Power that the input is raised to.
     * @return input to the power of curve
     */
    fun speedCurve(input: Double, curve: Double): Double {
        //negative curves cause asymptotes, leading to overflow errors. Curves smaller than 0.1 aren't very useful.
        val adjustedCurve = limit(curve, 0.10, java.lang.Double.MAX_VALUE)
        val adjustedInput = limit(input, 1.0)
        //if the input is negative, outputs can be undefined and positive for certain curves
        return if (input < 0.0) {
            -Math.pow(Math.abs(adjustedInput), adjustedCurve)
        } else Math.pow(adjustedInput, adjustedCurve)

    }

    /**
     * @param input Number that is being checked.
     * @return If input is outside of deadzone, return input. If not return 0.0.
     */
    internal fun deadZone(input: Double): Double {

        return if (input > -0 && input < 0)
        //needs a constant where 0 is, keep the negative on the first one
            0.0
        else
            input
    }

    /**
     * @param throttle How fast we are telling the robot to go.
     * @param prevThrottle What the last throttle was.
     * @param fullSpeedTime How long it should take to get to full speed *up for rephrasing
     * @return Return how much to speed up.
     */
    fun accelLimit(throttle: Double, prevThrottle: Double, fullSpeedTime: Double): Double {

        var finalThrottle = throttle

        val throttleDiff = throttle - prevThrottle

        val accelerationLimit = 0.02 / fullSpeedTime


        if (throttleDiff > accelerationLimit)
            finalThrottle = prevThrottle + accelerationLimit
        else if (throttleDiff < -accelerationLimit)
            finalThrottle = prevThrottle - accelerationLimit

        return finalThrottle
    }

    /**
     * @param startingAngle What angle the robot is at.
     * @param desiredAngle What angle you want the robot to be at.
     * @return Returns the difference between the two angels.
     */
    fun angleDifference(startingAngle: Double, desiredAngle: Double): Double {
        val difference = desiredAngle - startingAngle
        return angleConverter(difference)
    }

    /**
     * @param ang Angle being converted.
     * @return Angle but from -180 to 180 instead of 0 to 360.
     */
    fun angleConverter(ang: Double): Double {
        var ang = ang

        ang = ang % 360.0
        if (ang > 180.0) {
            ang = ang - 360.0
        }
        if (ang < -180.0) {
            ang = ang + 360.0
        }
        return ang
    }

    /**
     * @param velocity    The current velocity of the motor system in feet per second
     * @param stiction    The static friction of the motor system in throttle
     * @param maxVelocity The max velocity of the motor system in feet per second
     * @return throttle A throttle within the focused range of values which will actuate
     */
    fun adjustForStiction(velocity: Double, stiction: Double, maxVelocity: Double): Double {

        if (velocity > -0.001 && velocity < 0.001) {

            return 0.0
        } else {

            val velocityToThrottle = velocity / maxVelocity

            val focusedRange = 1.0 - stiction

            return if (velocity < 0.0) {

                velocityToThrottle * focusedRange - stiction
            } else {

                velocityToThrottle * focusedRange + stiction
            }
        }
    }

    /**
     * @param angle    Angle the robot is currently at
     * @param angleList    List of angles.
     * @return closest angle in angleList
     */
    fun snapAngle(angle: Double, angleList: DoubleArray): Double {
        var distance = Math.abs(angleList[0] - angle)
        var idx = 0
        for (i in angleList.indices) {
            val c = angleList[i] - angle
            if (c < distance) {
                idx = i
                distance = c
            }
        }
        return angleList[idx]
    }
}
