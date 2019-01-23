package org.minutebots.robot.subsystems;

import edu.wpi.first.wpilibj.SpeedController;
import edu.wpi.first.wpilibj.command.Subsystem;

public class CargoOuttake extends Subsystem {

    private SpeedController wheel;

    CargoOuttake(SpeedController wheel){
        this.wheel = wheel;
    }

    public void setWheel(double speed){
        wheel.set(speed);
    }

    public void initDefaultCommand(){

    }

    public static CargoOuttake getInstance(){
        return Superstructure.getInstance().cargoOutake;
    }
}
