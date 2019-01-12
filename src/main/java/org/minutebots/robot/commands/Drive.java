package org.minutebots.robot.commands;

import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import org.minutebots.robot.OI;
import org.minutebots.robot.subsystems.Drivetrain;

public class Drive extends Command {

    @Override
    protected void initialize(){
    }

    @Override
    protected void execute(){
        Drivetrain.getInstance().mecanumDrive(OI.primaryStick.getX(), -OI.primaryStick.getY(),
        (OI.primaryStick.getPOV()==-1) ? 0 : Math.min(angleDifference(Drivetrain.getInstance().getAngle() % 360, OI.primaryStick.getPOV()) * 0.005, 0.4));
        SmartDashboard.putNumber("Angle Difference", angleDifference(Drivetrain.getInstance().getAngle() % 360, OI.primaryStick.getPOV()));
        SmartDashboard.putNumber("Hat POV", OI.primaryStick.getPOV());
        SmartDashboard.putNumber("Gyro Angle", Drivetrain.getInstance().getAngle());
        System.out.println(Drivetrain.getInstance().getAngle());
        SmartDashboard.updateValues();
    }


    @Override
    protected boolean isFinished() {
        return false;
    }

    private static double angleDifference(double startingAngle, double desiredAngle) {
        double difference;
        difference = startingAngle - desiredAngle;
        if (difference > -180 && difference <= 180)
            return -difference;
        else if (difference <= -180)
            return -(difference + 360);
        else if (difference > 180)
            return -(difference - 360);
        else
            return 0;
    }
}
