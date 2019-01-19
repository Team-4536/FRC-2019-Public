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
    private static final JoystickButton resetGyro = new JoystickButton(primaryStick, 12);

    // Output

    public static final int LEFT_FRONT_MOTOR = 2;
    public static final int LEFT_BACK_MOTOR = 1;
    public static final int RIGHT_FRONT_MOTOR = 0;
    public static final int RIGHT_BACK_MOTOR = 3;
    public static final int GEAR_SLIDE_MOTOR = 5;
    public static final int CLIMBER_MOTOR = 4;
    
    static{
        resetGyro.whenPressed(new InstantCommand(() -> Drivetrain.getInstance().resetGyro()));
    }
    
    }
