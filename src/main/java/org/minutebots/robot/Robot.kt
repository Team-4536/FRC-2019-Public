package org.minutebots.robot

import edu.wpi.first.wpilibj.TimedRobot
import edu.wpi.first.wpilibj.command.Scheduler
import org.minutebots.robot.hardwareconfigurations.*
import org.minutebots.robot.subsystems.Drivetrain
import org.minutebots.robot.utilities.VisionCommunication

class Robot : TimedRobot() {


    override fun robotInit() {
        hardwareManager.init()
        hardwareManager.closeSolenoids()
    }

    override fun robotPeriodic() {
        //VisionCommunication.getInstance().update();
    }

    override fun disabledPeriodic() {
        println(VisionCommunication.instance.angle)
    }

    override fun autonomousInit() {
        isAuto = true
        Drivetrain.instance.resetGyro()
        VisionCommunication.instance.highExposure()
        Drivetrain.instance.setSetpoint(Drivetrain.instance.getPosition())
        Drivetrain.instance.enable()
    }

    override fun autonomousPeriodic() {
        Scheduler.getInstance().run()
    }

    override fun teleopInit() {
        isAuto = false
        Drivetrain.instance.enable()
    }

    override fun teleopPeriodic() {
        Scheduler.getInstance().run()
    }

    override fun testPeriodic() {}

    override fun disabledInit() {
        Drivetrain.instance.disable()
    }

    companion object {

        var hardwareManager: HardwareManger = Asimov()
        var isAuto = false
    }
}
