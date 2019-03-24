package org.minutebots.robot;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.buttons.JoystickButton;
import edu.wpi.first.wpilibj.command.InstantCommand;
import org.minutebots.robot.subsystems.DepotArm;
import org.minutebots.robot.subsystems.Drivetrain;
import org.minutebots.robot.subsystems.HatchPiston;
import org.minutebots.robot.subsystems.Ramp;
import org.minutebots.robot.utilities.Constants;
import org.minutebots.robot.utilities.VisionCommunication;

public class OI {

    /*---------------------------------------Programmer's territory starts here----------------------------------*/
    public static final Joystick primaryStick = new Joystick(0);
    public static final Joystick secondaryStick = new Joystick(1);
    public static final JoystickButton trigger = new JoystickButton(primaryStick, 1),
            angleAdjustLeft = new JoystickButton(primaryStick, 7),
            angleAdjustRight = new JoystickButton(primaryStick, 8),
            ejectPrimary = new JoystickButton(primaryStick, 5),
            ramp = new JoystickButton(primaryStick,3),
            highExposure = new JoystickButton(primaryStick, 2),
            lockCargo = new JoystickButton(primaryStick, 4),
            grabHatchPrimary = new JoystickButton(primaryStick, 6);
    public static final JoystickButton snapTo = new JoystickButton(secondaryStick, 3),
            fineTurn = new JoystickButton(secondaryStick, 1),
            visionRotate = new JoystickButton(secondaryStick, 2),
            ejectSecondary = new JoystickButton(secondaryStick, 5),
            spinArmForwards = new JoystickButton(secondaryStick, 6),
            spinArmBackwards = new JoystickButton(secondaryStick, 4),
            strafe = new JoystickButton(secondaryStick, 3),
            resetGyro = new JoystickButton(secondaryStick, 10),
            grabHatch = new JoystickButton(secondaryStick, 11);


    static {
        snapTo.whileHeld(new InstantCommand(() -> Drivetrain.getInstance().setSetpoint(Drivetrain.getInstance().rocketMode.getBoolean(false) ? Drivetrain.getInstance().getNearestRocket() : Drivetrain.getInstance().getNearestSquare())));
        fineTurn.whenReleased(new InstantCommand(() -> Drivetrain.getInstance().setSetpoint(Drivetrain.getInstance().getYaw())));
        angleAdjustLeft.whileHeld(new InstantCommand(() -> Drivetrain.getInstance().adjustAngle(3.0)));
        angleAdjustRight.whileHeld(new InstantCommand(() -> Drivetrain.getInstance().adjustAngle(-3.0)));
        highExposure.whenPressed(VisionCommunication.getInstance().highExposure());
        ejectPrimary.whenPressed(HatchPiston.extend());
        ejectPrimary.whenReleased(HatchPiston.retract());
        ejectSecondary.whenPressed(HatchPiston.extend());
        ejectSecondary.whenReleased(HatchPiston.retract());
        ramp.whenPressed(Ramp.spinWheel(Constants.RAMP_MAX_SPEED));
        //ramp.whenReleased(Ramp.spinWheel(0));
        lockCargo.whenPressed(Ramp.lockCargo());
        grabHatch.whenPressed(HatchPiston.grabHatch());
        grabHatch.whenReleased(HatchPiston.retractHatchM());
        grabHatchPrimary.whenPressed(HatchPiston.grabHatch());
        grabHatchPrimary.whenReleased(HatchPiston.retractHatchM());
        ramp.whenReleased(new InstantCommand(() -> Ramp.getInstance().setWheel(0)));
        resetGyro.whenPressed(new InstantCommand(() ->Drivetrain.getInstance().resetGyro()));
        spinArmBackwards.whenPressed(DepotArm.armDown(false));
        spinArmForwards.whenPressed(DepotArm.armDown(true));
        spinArmBackwards.whenReleased(DepotArm.armUp());
        spinArmForwards.whenReleased(DepotArm.armUp());
    }

}
