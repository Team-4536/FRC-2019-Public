package org.minutebots.robot.hardwareconfigurations;

import edu.wpi.first.wpilibj.*;


/**
 * This is our way to abstract hardware from the software. Everything that is an output or an imput
 * must be in here.
 */

public interface HardwareManger {
    SpeedController[] drivetrainMotors();

    void resetGyro();

    SpeedController depotArm();

    SpeedController depotRoller();

    SpeedController rampMotor();

    void extendIntakePiston();

    void retractIntakePiston();

    double getAngle();

    /**
     * @return The distance from the ultrasonic sensor in inches.
     */
    double ultraSonicDist();

    boolean armUp();

    boolean armDown();

    void init();
}

