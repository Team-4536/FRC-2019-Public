package org.minutebots.robot.subsystems;

import edu.wpi.first.wpilibj.command.CommandGroup;
import edu.wpi.first.wpilibj.command.InstantCommand;
import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.command.WaitCommand;

import org.minutebots.robot.OI;
import org.minutebots.robot.Robot;

public class Ramp extends Subsystem {

    public void periodic(){
        setWheel(OI.fineTurn.get() ? -OI.secondaryStick.getY() : 0);
    }

    public void setWheel(double speed){
        Robot.hardwareManager.rampMotor().set(speed);
    }

    public static InstantCommand spinWheel(double speed){
        return new InstantCommand(() -> {
            getInstance().setWheel(speed);
            if(speed > 0.5) Ramp.retractLock().start();
        });
    }

    public static InstantCommand extendLock(){
        return new InstantCommand(() -> Robot.hardwareManager.extendRampLock());
    }

    public static InstantCommand retractLock(){
        return new InstantCommand(() -> Robot.hardwareManager.retractRampLock());
    }

    public static CommandGroup lockCargo(){
        return new CommandGroup(){
            {
                addSequential(extendLock());
                addSequential(spinWheel(0.3));
                addSequential(new WaitCommand(0.1));
                addSequential(spinWheel(0));
            }
        };
    }

    public void initDefaultCommand(){
    }

    private static Ramp ramp = new Ramp();

    public static Ramp getInstance(){
        return ramp;
    }
}
