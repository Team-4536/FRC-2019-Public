package org.minutebots.robot.subsystems;

import com.ctre.phoenix.motorcontrol.can.WPI_VictorSPX;
import edu.wpi.first.wpilibj.command.Subsystem;
import org.minutebots.robot.OI;

public class CargoOuttake extends Subsystem {

    private WPI_VictorSPX wheel = new WPI_VictorSPX(OI.WHEEL);

    private CargoOuttake(){
    }

    public void setWheel(double speed){
        wheel.set(speed);
    }

    public void initDefaultCommand(){

    }

    private static CargoOuttake cargoOuttake = new CargoOuttake();
    public static CargoOuttake getInstance(){
        return cargoOuttake;
    }
}
