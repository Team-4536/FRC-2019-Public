package org.minutebots.robot;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.buttons.JoystickButton;

public class OI {

    // Input

    // Ports

    public static final int PRIMARY_STICK_PORT = 0;
    public static final int TRIGGER_BUTTON = 1;

    // Objects

    public static final Joystick primaryStick = new Joystick(PRIMARY_STICK_PORT);
    public static final JoystickButton trigger = new JoystickButton(primaryStick, TRIGGER_BUTTON);

    // Output

    public static final int LEFT_FRONT_MOTOR = 2;
    public static final int LEFT_BACK_MOTOR = 1;
    public static final int RIGHT_FRONT_MOTOR = 0;
    public static final int RIGHT_BACK_MOTOR = 3;
    
    }
