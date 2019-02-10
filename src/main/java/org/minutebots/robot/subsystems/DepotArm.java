package org.minutebots.robot.subsystems;

import edu.wpi.first.wpilibj.command.Subsystem;
import org.minutebots.robot.Robot;

public class DepotArm extends Subsystem {
    DepotArm() {
    }

    private void moveArm(double speed) {
        double motorSpeed = speed;
        if (Robot.robot.armUp()) motorSpeed = speed < 0 ? speed : 0;
        if (Robot.robot.armDown()) motorSpeed = speed > 0 ? speed : 0;
        Robot.robot.depotArm().set(motorSpeed);
    }

    private void spinWheel(double speed) {
        Robot.robot.depotRoller().set(speed);
    }

    public void initDefaultCommand() {
    }

    private static DepotArm depotArm = new DepotArm();

    public static DepotArm getInstance() {
        return depotArm;
    }
}
