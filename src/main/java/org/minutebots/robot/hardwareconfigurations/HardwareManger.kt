package org.minutebots.robot.hardwareconfigurations

import edu.wpi.first.wpilibj.*


/**
 * This is our way to abstract hardware from the software. Everything that is an output or an imput
 * must be in here.
 */

interface HardwareManger {

    val angle: Double
    fun drivetrainMotors(): Array<SpeedController>

    fun resetGyro()

    fun depotArm(): SpeedController

    fun depotRoller(): SpeedController

    fun rampMotor(): SpeedController

    fun extendIntakePiston()

    fun retractIntakePiston()

    fun extendActiveHatch()

    fun retractActiveHatch()

    fun closeSolenoids()

    /**
     * @return The distance from the ultrasonic sensor in inches.
     */
    fun ultraSonicDist(): Double

    fun armUp(): Boolean

    fun armDown(): Boolean

    fun extendRampLock()

    fun retractRampLock()

    fun init()
}

