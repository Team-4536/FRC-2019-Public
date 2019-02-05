package org.minutebots.robot.subsystems;

import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.SpeedController;
import edu.wpi.first.wpilibj.command.InstantCommand;
import edu.wpi.first.wpilibj.command.Subsystem;

public class DepotYeeter extends Subsystem {
    private SpeedController spinner, arm;
    private DigitalInput up, down;

    DepotYeeter(SpeedController wheel, SpeedController arm, DigitalInput up, DigitalInput down) {
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

    public static InstantCommand extend(double speed) {
        return new InstantCommand(getInstance(), () -> {
            getInstance().setIntakeStatus(true);
            getInstance().setConeStatus(true);
        });
    }

    public static InstantCommand retract() {
        return new InstantCommand(getInstance(), () -> {
            getInstance().setIntakeStatus(false);
            getInstance().setConeStatus(false);
        });
    }

    public void initDefaultCommand() {
    }

    public static DepotYeeter getInstance() {
        return Superstructure.getInstance().depotYeeter;
    }
}
