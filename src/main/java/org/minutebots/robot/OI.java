package org.minutebots.robot;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.buttons.JoystickButton;
import edu.wpi.first.wpilibj.command.InstantCommand;
import org.minutebots.robot.subsystems.Drivetrain;
import org.minutebots.robot.subsystems.HatchPiston;

public class OI {
    // Input Numbers
    private static final int PRIMARY_STICK_PORT = 0,
            TOGGLE_TURN = 1,
            EXTEND_INTAKE = 5,
            RETRACT_INTAKE = 3,
            ADJUST_ANGLE_LEFT = 7,
            ADJUST_ANGLE_RIGHT = 8,
            ACTIVATE_VISION = 2;

    /*---------------------------------------Programmer's territory starts here----------------------------------*/
    public static final Joystick primaryStick = new Joystick(PRIMARY_STICK_PORT);
    public static final JoystickButton trigger = new JoystickButton(primaryStick, TOGGLE_TURN),
            extend = new JoystickButton(primaryStick, EXTEND_INTAKE),
            retract = new JoystickButton(primaryStick, RETRACT_INTAKE),
            angleAdjustLeft = new JoystickButton(primaryStick, ADJUST_ANGLE_LEFT),
            angleAdjustRight = new JoystickButton(primaryStick, ADJUST_ANGLE_RIGHT),
            vision = new JoystickButton(primaryStick, ACTIVATE_VISION);

    static {
        angleAdjustLeft.whileHeld(new InstantCommand(() -> Drivetrain.getInstance().adjustAngle(3.0)));
        angleAdjustRight.whileHeld(new InstantCommand(() -> Drivetrain.getInstance().adjustAngle(-3.0)));
        extend.whenPressed(HatchPiston.extend());
        retract.whenPressed(HatchPiston.retract());
    }

}
