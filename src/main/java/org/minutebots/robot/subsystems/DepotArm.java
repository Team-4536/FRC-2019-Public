package org.minutebots.robot.subsystems;

import edu.wpi.first.wpilibj.command.InstantCommand;
import edu.wpi.first.wpilibj.command.Subsystem;
import org.minutebots.robot.Robot;
import org.minutebots.robot.utilities.Constants;

public class DepotArm extends Subsystem {

    private boolean isDown = false;

    private void moveArm(double speed) {
        double motorSpeed = speed;
        if (Robot.hardwareManager.armUp()) motorSpeed = speed < 0 ? speed : 0;
        if (Robot.hardwareManager.armDown()) motorSpeed = speed > 0 ? speed : 0;
        Robot.hardwareManager.depotArm().set(motorSpeed);
    }

    public void periodic(){
        if (isDown) moveArm(-Constants.DEPOT_DOWN_MAX_SPEED);
        else moveArm(Constants.DEPOT_UP_MAX_SPEED);
    }

    public static InstantCommand armDown(boolean forwards){
        return new InstantCommand(() -> {
            getInstance().isDown = true;
            getInstance().spinWheel((forwards ? 1 : -1) * Constants.DEPOT_SPIN_MAX_SPEED);
        });
    }

    public static InstantCommand armUp(){
        return new InstantCommand(() -> {
            getInstance().isDown = false;
            getInstance().spinWheel(0);
        });
    }


    public static InstantCommand toggleArm(){
        return new InstantCommand(() -> getInstance().isDown = !getInstance().isDown);
    }

    private void spinWheel(double speed) {
        Robot.hardwareManager.depotRoller().set(speed);
    }

    public void initDefaultCommand() {
    }

    private static DepotArm depotArm = new DepotArm();

    public static DepotArm getInstance() {
        return depotArm;
    }
}
