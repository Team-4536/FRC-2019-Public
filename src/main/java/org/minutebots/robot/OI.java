package org.minutebots.robot;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.buttons.JoystickButton;
import edu.wpi.first.wpilibj.command.InstantCommand;
import org.minutebots.robot.subsystems.Drivetrain;
import org.minutebots.robot.subsystems.Intake;

public class OI {
    // Input Numbers
    private static final int PRIMARY_STICK_PORT = 0,
            EXTEND_INTAKE = 1,
            RETRACT_INTAKE = 2,
            ADJUST_ANGLE_LEFT = 7,
            ADJUST_ANGLE_RIGHT = 8;


    // Output Ports
    //Talons
    public static final int LEFT_FRONT_MOTOR = 2;
    public static final int LEFT_BACK_MOTOR = 1;
    public static final int RIGHT_FRONT_MOTOR = 0;
    public static final int RIGHT_BACK_MOTOR = 3;

    //Pneumatic Ports
    public final static int INTAKE_1 = 1;
    public final static int INTAKE_2 = 2;
    public final static int CONE_1 = 3;
    public final static int CONE_2 = 4;


    //Victor Ports
    public final static int SIDE_WHEEL = 0;
    public final static int WHEEL = 1;

    /*---------------------------------------Programmer's territory starts here----------------------------------*/
    public static final Joystick primaryStick = new Joystick(PRIMARY_STICK_PORT);
    public static final JoystickButton extend = new JoystickButton(primaryStick, EXTEND_INTAKE),
            retract = new JoystickButton(primaryStick, RETRACT_INTAKE),
            angleAdjustLeft = new JoystickButton(primaryStick, ADJUST_ANGLE_LEFT),
            angleAdjustRight = new JoystickButton(primaryStick, ADJUST_ANGLE_RIGHT);

    static{
        angleAdjustLeft.whileHeld(new InstantCommand(() -> Drivetrain.getInstance().adjustAngle(-3.0)));
        angleAdjustRight.whileHeld(new InstantCommand(() -> Drivetrain.getInstance().adjustAngle(3.0)));
        extend.whenPressed(Intake.extend());
        retract.whenPressed(Intake.retract());
    }

    }
