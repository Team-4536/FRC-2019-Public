package org.minutebots.robot.commands;

import edu.wpi.first.wpilibj.command.Command;
import org.minutebots.robot.OI;
import org.minutebots.robot.subsystems.Drivetrain;
import org.minutebots.robot.utilities.Constants;
import org.minutebots.robot.utilities.VisionCommunication;

public class StrafeToVisionTarget extends Command {

    private double error;

    public StrafeToVisionTarget() {
        setName("Drivetrain","Strafe To Target");
        requires(Drivetrain.getInstance());
    }

    @Override
    protected void execute() {
        error = VisionCommunication.getInstance().getAngle();
        Drivetrain.getInstance().mecanumDrive(Constants.VISION_STRAFE_P * VisionCommunication.getInstance().getAngle(), -OI.primaryStick.getY(), Drivetrain.getInstance().getTurnThrottle());
    }

    @Override
    protected boolean isFinished() {
        return Math.abs(error) < 3 && error != 0.0;
    }
}
