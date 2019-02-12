package org.minutebots.robot.subsystems;

import edu.wpi.first.wpilibj.command.InstantCommand;
import edu.wpi.first.wpilibj.command.Subsystem;
import org.minutebots.robot.Robot;

public class DepotArm extends Subsystem {

    private boolean isDown = false;

    private void moveArm(double speed) {
        double motorSpeed = speed;
        if (Robot.hardwareManager.armUp()) motorSpeed = speed < 0 ? speed : 0;
        if (Robot.hardwareManager.armDown()) motorSpeed = speed > 0 ? speed : 0;
        Robot.hardwareManager.depotArm().set(motorSpeed);
    }

    public void periodic(){
        if (isDown) moveArm(-0.2);
        else moveArm(0.3);
    }


    public static InstantCommand toggleArm(){
        return new InstantCommand(() -> getInstance().isDown = !getInstance().isDown);
    }

    public void spinWheel(double speed) {
        Robot.hardwareManager.depotRoller().set(speed);
    }

    public void initDefaultCommand() {
    }

    private static DepotArm depotArm = new DepotArm();

    public static DepotArm getInstance() {
        return depotArm;
    }
}
