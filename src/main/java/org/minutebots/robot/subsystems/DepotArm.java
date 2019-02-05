package org.minutebots.robot.subsystems;

import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.SpeedController;
import edu.wpi.first.wpilibj.command.InstantCommand;
import edu.wpi.first.wpilibj.command.Subsystem;

public class DepotArm extends Subsystem {
    private SpeedController spinner, arm;
    private DigitalInput up, down;

    DepotArm(SpeedController arm, SpeedController wheel, DigitalInput up, DigitalInput down) {
        this.spinner = wheel;
        this.arm = arm;
        this.up = up;
        this.down = down;
    }

    private void moveArm(double speed) {
        double motorSpeed = speed;
        if (up.get()) motorSpeed = 0;
        if (down.get()) motorSpeed = 0;
        spinner.set(motorSpeed);
    }

    private void spinWheel(double speed) {
        spinner.set(speed);
    }

    public void initDefaultCommand() {
    }

    public static DepotArm getInstance() {
        return Superstructure.getInstance().depotArm;
    }
}
