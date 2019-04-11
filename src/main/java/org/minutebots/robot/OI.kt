package org.minutebots.robot

import edu.wpi.first.wpilibj.Joystick
import edu.wpi.first.wpilibj.buttons.JoystickButton
import edu.wpi.first.wpilibj.command.InstantCommand
import org.minutebots.robot.subsystems.DepotArm
import org.minutebots.robot.subsystems.Drivetrain
import org.minutebots.robot.subsystems.HatchPiston
import org.minutebots.robot.subsystems.Ramp
import org.minutebots.robot.utilities.Constants
import org.minutebots.robot.utilities.VisionCommunication

object OI {

    /*---------------------------------------Programmer's territory starts here----------------------------------*/
    val primaryStick = Joystick(0)
    val secondaryStick = Joystick(1)
    val trigger = JoystickButton(primaryStick, 1)
    val angleAdjustLeft = JoystickButton(primaryStick, 7)
    val angleAdjustRight = JoystickButton(primaryStick, 8)
    val ejectPrimary = JoystickButton(primaryStick, 5)
    val ramp = JoystickButton(primaryStick, 3)
    val highExposure = JoystickButton(primaryStick, 2)
    val lockCargo = JoystickButton(primaryStick, 4)
    val grabHatchPrimary = JoystickButton(primaryStick, 6)
    val snapTo = JoystickButton(secondaryStick, 3)
    val fineTurn = JoystickButton(secondaryStick, 1)
    val defenseTurn = JoystickButton(secondaryStick, 7)
    val visionRotate = JoystickButton(secondaryStick, 2)
    val ejectSecondary = JoystickButton(secondaryStick, 5)
    val spinArmForwards = JoystickButton(secondaryStick, 6)
    val spinArmBackwards = JoystickButton(secondaryStick, 4)
    val strafe = JoystickButton(secondaryStick, 3)
    val closeSolenoids = JoystickButton(secondaryStick, 8)
    val visionOveride = JoystickButton(secondaryStick, 9)
    val resetGyro = JoystickButton(secondaryStick, 10)
    val grabHatch = JoystickButton(secondaryStick, 11)
    val unlockCargo = JoystickButton(secondaryStick, 12)


    init {
        snapTo.whileHeld(InstantCommand { Drivetrain.instance.setSetpoint(if (Drivetrain.instance.rocketMode.getBoolean(false)) Drivetrain.instance.nearestRocket else Drivetrain.instance.nearestSquare) })
        fineTurn.whenReleased(InstantCommand { Drivetrain.instance.setSetpoint(Drivetrain.instance.yaw) })
        defenseTurn.whenReleased(InstantCommand { Drivetrain.instance.setSetpoint(Drivetrain.instance.yaw) })
        angleAdjustLeft.whileHeld(InstantCommand { Drivetrain.instance.adjustAngle(3.0) })
        angleAdjustRight.whileHeld(InstantCommand { Drivetrain.instance.adjustAngle(-3.0) })
        highExposure.whenPressed(VisionCommunication.instance.highExposure())
        ejectPrimary.whenPressed(HatchPiston.extend())
        ejectPrimary.whenReleased(HatchPiston.retract())
        ejectSecondary.whenPressed(HatchPiston.extend())
        ejectSecondary.whenReleased(HatchPiston.retract())
        ramp.whenPressed(Ramp.spinWheel(Constants.RAMP_MAX_SPEED))
        lockCargo.whenPressed(Ramp.lockCargo())
        grabHatch.whenPressed(HatchPiston.grabHatch())
        grabHatch.whenReleased(HatchPiston.retractHatchM())
        grabHatchPrimary.whenPressed(HatchPiston.grabHatch())
        grabHatchPrimary.whenReleased(HatchPiston.retractHatchM())
        ramp.whenReleased(InstantCommand { Ramp.instance.setWheel(0.0) })
        unlockCargo.whenReleased(Ramp.retractLock())
        visionOveride.whenPressed(VisionCommunication.instance.highExposure())
        resetGyro.whenPressed(InstantCommand { Drivetrain.instance.resetGyro() })
        spinArmBackwards.whenPressed(DepotArm.armDown(false))
        spinArmForwards.whenPressed(DepotArm.armDown(true))
        spinArmBackwards.whenReleased(DepotArm.armUp())
        spinArmForwards.whenReleased(DepotArm.armUp())
        closeSolenoids.whenPressed(InstantCommand { Robot.hardwareManager.closeSolenoids() })
    }

}
