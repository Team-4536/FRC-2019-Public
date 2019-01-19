package org.minutebots.robot;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.buttons.JoystickButton;
import edu.wpi.first.wpilibj.command.InstantCommand;
import org.minutebots.robot.subsystems.Drivetrain;

public class OI {
    // Input
    // Ports

    public static final int PRIMARY_STICK_PORT = 0;
    public static final int TRIGGER_BUTTON = 1;

    // Objects

    public static final Joystick primaryStick = new Joystick(PRIMARY_STICK_PORT);
    public static final JoystickButton trigger = new JoystickButton(primaryStick, TRIGGER_BUTTON);

    private static final JoystickButton angleAdjustLeft = new JoystickButton(primaryStick, 7);
    private static final JoystickButton angleAdjustRight = new JoystickButton(primaryStick, 8);

    // Output
        //Talons
        public static final int LEFT_FRONT_MOTOR = 2;
        public static final int LEFT_BACK_MOTOR = 1;
        public static final int RIGHT_FRONT_MOTOR = 0;
        public static final int RIGHT_BACK_MOTOR = 3;
        public static final int GEAR_SLIDE_MOTOR = 5;
        public static final int CLIMBER_MOTOR = 4;

        //Pneumatic Ports
        public final static int INTAKE_1 = 1;
        public final static int INTAKE_2 = 2;
        public final static int CONE_1 = 3;
        public final static int CONE_2 = 4;


        //Victor Ports
        public final static int SIDE_WHEEL = 0;
        public final static int WHEEL = 1;
    
    static{
        angleAdjustLeft.whileHeld(new InstantCommand(() -> Drivetrain.getInstance().adjustAngle(-3.0)));
        angleAdjustRight.whileHeld(new InstantCommand(() -> Drivetrain.getInstance().adjustAngle(3.0)));
    }
    
    }
