package org.minutebots.robot.commands;


import edu.wpi.first.wpilibj.command.Command;
import org.minutebots.robot.Robot;
import org.minutebots.robot.subsystems.Drivetrain;

public class    StopAtWall extends Command{

    @Override
    protected boolean isFinished() {
        return false;
    }
    public void execute(){
        Robot.hardwareManager.ultraSonicDist();
    }
    StopAtWall(){
        requires(Drivetrain.getInstance());
        if(Robot.hardwareManager.ultraSonicDist()>.0){
            //Drivetrain.getInstance().mecanumDrive();
        }
    }
}
