package org.minutebots.robot.subsystems;

import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;
import com.ctre.phoenix.motorcontrol.can.WPI_VictorSPX;
import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.Spark;
import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard;
import org.minutebots.lib.VirtualMotor;
import org.minutebots.lib.VirtualSolenoid;

public class Superstructure {
    //Talons
    private final int LEFT_FRONT_MOTOR = 0,
            LEFT_BACK_MOTOR = 3,
            RIGHT_FRONT_MOTOR = 1,
            RIGHT_BACK_MOTOR = 2;

    //Pneumatic Ports
    private final static int PISTON_1 = 6,
            PISTON_2 = 7;


    //Victor Ports
    private final static int SIDE_WHEEL = 0,
            WHEEL = 1;

    /*---------------------------------------Programmer's territory starts here----------------------------------*/

    HatchPiston hatchPiston;
    CargoOuttake cargoOutake;
    DepotArm depotArm;
    Drivetrain driveTrain;

    private static Superstructure instance = new Superstructure(RobotType.YEETER);

    private Superstructure(RobotType robot) {
        switch (robot) {
            case FRACTURE:
                driveTrain = new Drivetrain(
                        new Spark(LEFT_FRONT_MOTOR),
                        new Spark(RIGHT_FRONT_MOTOR),
                        new Spark(LEFT_BACK_MOTOR),
                        new Spark(RIGHT_BACK_MOTOR));
                cargoOutake = new CargoOuttake(
                        new VirtualMotor(4));
                depotArm = new DepotArm(
                        new VirtualMotor(5));
                hatchPiston = new HatchPiston(new VirtualSolenoid(PISTON_1, PISTON_2));
                break;
            case WATERGAME:
                driveTrain = new Drivetrain(
                        new VirtualMotor(LEFT_FRONT_MOTOR),
                        new VirtualMotor(RIGHT_FRONT_MOTOR),
                        new VirtualMotor(LEFT_BACK_MOTOR),
                        new VirtualMotor(RIGHT_BACK_MOTOR));
                cargoOutake = new CargoOuttake(
                        new VirtualMotor(4));
                depotArm = new DepotArm(
                        new VirtualMotor(5));
                hatchPiston = new HatchPiston(new VirtualSolenoid(PISTON_1, PISTON_2));
                break;
            default: {
                WPI_VictorSPX leftFront = new WPI_VictorSPX(LEFT_FRONT_MOTOR),
                        rightFront = new WPI_VictorSPX(RIGHT_FRONT_MOTOR),
                        leftBack = new WPI_VictorSPX(LEFT_BACK_MOTOR),
                        rightBack = new WPI_VictorSPX(RIGHT_BACK_MOTOR);
                WPI_TalonSRX wheelMotor = new WPI_TalonSRX(WHEEL), yeeter = new WPI_TalonSRX(SIDE_WHEEL);

                Shuffleboard.getTab("Motors").add("Left Front DM 2", leftFront);
                Shuffleboard.getTab("Motors").add("Right Front DM 0", rightFront);
                Shuffleboard.getTab("Motors").add("Left Back DM 1", leftBack);
                Shuffleboard.getTab("Motors").add("Right Back DM 3", rightBack);
                Shuffleboard.getTab("Motors").add("Cargo Outtake Motor", wheelMotor);
                Shuffleboard.getTab("Motors").add("Depot Yeeter", yeeter);

                driveTrain = new Drivetrain(leftFront, rightFront, leftBack, rightBack);
                cargoOutake = new CargoOuttake(wheelMotor);
                depotArm = new DepotArm(yeeter);
                hatchPiston = new HatchPiston(new DoubleSolenoid(PISTON_1, PISTON_2));
            }
        }
    }

    private enum RobotType {
        FRACTURE,
        SIDEWINDER,
        YEETER,
        WATERGAME
    }

    static Superstructure getInstance() {
        return instance;
    }
}

