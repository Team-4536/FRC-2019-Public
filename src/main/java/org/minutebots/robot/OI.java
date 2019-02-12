package org.minutebots.robot;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.buttons.JoystickButton;
import edu.wpi.first.wpilibj.command.InstantCommand;
import org.minutebots.robot.subsystems.DepotArm;
import org.minutebots.robot.subsystems.Drivetrain;
import org.minutebots.robot.subsystems.HatchPiston;

public class OI {

    /*---------------------------------------Programmer's territory starts here----------------------------------*/
    public static final Joystick primaryStick = new Joystick(0);
    public static final JoystickButton trigger = new JoystickButton(primaryStick, 1),
            eject = new JoystickButton(primaryStick, 5),
            strafe = new JoystickButton(primaryStick, 3),
            angleAdjustLeft = new JoystickButton(primaryStick, 7),
            angleAdjustRight = new JoystickButton(primaryStick, 8),
            vision = new JoystickButton(primaryStick, 2),
            depotArm = new JoystickButton(primaryStick, 9),
            spinArmForwards = new JoystickButton(primaryStick, 6),
            spinArmBackwards = new JoystickButton(primaryStick, 4);


    static {
        angleAdjustLeft.whileHeld(new InstantCommand(() -> Drivetrain.getInstance().adjustAngle(3.0)));
        angleAdjustRight.whileHeld(new InstantCommand(() -> Drivetrain.getInstance().adjustAngle(-3.0)));
        eject.whenPressed(HatchPiston.extend());
        eject.whenReleased(HatchPiston.retract());
        depotArm.whenPressed(DepotArm.toggleArm());
    }

}
