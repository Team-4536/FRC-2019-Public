package org.minutebots.robot.subsystems;

import com.ctre.phoenix.motorcontrol.can.WPI_VictorSPX;
import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.Spark;
import org.minutebots.lib.VirtualMotor;
import org.minutebots.lib.VirtualSolenoid;
import org.minutebots.robot.OI;

public class Superstructure {

    private static Superstructure instance = new Superstructure(RobotType.FRACTURE);

    Intake intake;
    CargoOuttake cargoOutake;
    DepotYeeter depotYeeter;
    Drivetrain driveTrain;

    private Superstructure(RobotType robot){
        switch(robot){
            case FRACTURE:
                driveTrain = new Drivetrain(
                        new Spark(OI.LEFT_FRONT_MOTOR),
                        new Spark(OI.RIGHT_FRONT_MOTOR),
                        new Spark(OI.LEFT_BACK_MOTOR),
                        new Spark(OI.RIGHT_BACK_MOTOR));
                cargoOutake = new CargoOuttake(
                        new VirtualMotor(OI.WHEEL));
                depotYeeter = new DepotYeeter(
                        new VirtualMotor(OI.SIDE_WHEEL));
                intake = new Intake(
                        new VirtualSolenoid(OI.INTAKE_1, OI.INTAKE_2),
                        new VirtualSolenoid(OI.CONE_1, OI.CONE_2));
            default:
                driveTrain = new Drivetrain(
                        new Spark(OI.LEFT_FRONT_MOTOR),
                        new Spark(OI.RIGHT_FRONT_MOTOR),
                        new Spark(OI.LEFT_BACK_MOTOR),
                        new Spark(OI.RIGHT_BACK_MOTOR));
                cargoOutake = new CargoOuttake(
                        new WPI_VictorSPX(OI.WHEEL));
                depotYeeter = new DepotYeeter(
                        new WPI_VictorSPX(OI.SIDE_WHEEL));
                intake = new Intake(
                        new DoubleSolenoid(OI.INTAKE_1, OI.INTAKE_2),
                        new DoubleSolenoid(OI.CONE_1, OI.CONE_2));
        }
    }

    private enum RobotType{
        FRACTURE,
        SIDEWINDER,
        YEETER
    }

    static Superstructure getInstance(){
        return instance;
    }
}

