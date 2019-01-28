package org.minutebots.robot.commands;

import edu.wpi.first.wpilibj.command.Command;
import org.minutebots.robot.OI;
import org.minutebots.robot.subsystems.Drivetrain;
import org.minutebots.robot.utilities.VisionCommunication;

public class StrafeToVisionTarget extends Command {

    StrafeToVisionTarget(){
        requires(Drivetrain.getInstance());
    }

    @Override
    protected void execute() {
        double strafeThrottle = 0.05* VisionCommunication.getInstance().getSize();
        Drivetrain.getInstance().mecanumDrive(strafeThrottle,-OI.primaryStick.getY(), Drivetrain.getInstance().getTurnThrottle(), -Drivetrain.getInstance().getAngle());
    }

    @Override
    protected boolean isFinished() {
        return false;
    }
}
