package org.minutebots.robot.subsystems;

import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;
import com.ctre.phoenix.motorcontrol.can.WPI_VictorSPX;
import edu.wpi.first.wpilibj.DigitalInput;
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
    private final static int DEPOT_ARM = 4,
            DEPOT_WHEEL = 5,
            RAMP = 6;

    private final static int UP_LIMIT_SWITCH = 0, DOWN_LIMIT_SWITCH = 1;

    /*---------------------------------------Programmer's territory starts here----------------------------------*/

    HatchPiston hatchPiston;
    Ramp ramp;
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
                ramp = new Ramp(
                        new VirtualMotor(4));
                depotArm = new DepotArm(
                        new VirtualMotor(DEPOT_ARM),
                        new VirtualMotor(DEPOT_WHEEL),
                        new DigitalInput(UP_LIMIT_SWITCH),
                        new DigitalInput(DOWN_LIMIT_SWITCH));
                hatchPiston = new HatchPiston(new VirtualSolenoid(PISTON_1, PISTON_2));
                break;
            case WATERGAME:
                driveTrain = new Drivetrain(
                        new VirtualMotor(LEFT_FRONT_MOTOR),
                        new VirtualMotor(RIGHT_FRONT_MOTOR),
                        new VirtualMotor(LEFT_BACK_MOTOR),
                        new VirtualMotor(RIGHT_BACK_MOTOR));
                ramp = new Ramp(
                        new VirtualMotor(4));
                depotArm = new DepotArm(
                        new VirtualMotor(DEPOT_ARM),
                        new VirtualMotor(DEPOT_WHEEL),
                        new DigitalInput(UP_LIMIT_SWITCH),
                        new DigitalInput(DOWN_LIMIT_SWITCH));
                hatchPiston = new HatchPiston(new VirtualSolenoid(PISTON_1, PISTON_2));
                break;
            default: {
                WPI_VictorSPX leftFront = new WPI_VictorSPX(LEFT_FRONT_MOTOR),
                        rightFront = new WPI_VictorSPX(RIGHT_FRONT_MOTOR),
                        leftBack = new WPI_VictorSPX(LEFT_BACK_MOTOR),
                        rightBack = new WPI_VictorSPX(RIGHT_BACK_MOTOR);

                WPI_TalonSRX armMotor = new WPI_TalonSRX(DEPOT_ARM),
                        wheelMotor = new WPI_TalonSRX(DEPOT_WHEEL);

                DigitalInput upLimit = new DigitalInput(UP_LIMIT_SWITCH),
                downLimit = new DigitalInput(DOWN_LIMIT_SWITCH);

                Shuffleboard.getTab("Motors").add("Left Front DM 2", leftFront);
                Shuffleboard.getTab("Motors").add("Right Front DM 0", rightFront);
                Shuffleboard.getTab("Motors").add("Left Back DM 1", leftBack);
                Shuffleboard.getTab("Motors").add("Right Back DM 3", rightBack);
                Shuffleboard.getTab("Motors").add("Depot Arm", armMotor);
                Shuffleboard.getTab("Motors").add("Depot Wheel", wheelMotor);
                Shuffleboard.getTab("Motors").add("Arm Up", upLimit);
                Shuffleboard.getTab("Motors").add("Arm Down", downLimit);

                driveTrain = new Drivetrain(leftFront, rightFront, leftBack, rightBack);
                ramp = new Ramp(new VirtualMotor(RAMP));
                depotArm = new DepotArm(armMotor, wheelMotor, upLimit, downLimit);
                hatchPiston = new HatchPiston(new DoubleSolenoid(PISTON_1, PISTON_2));
            }
        }
    }

    private enum RobotType {
        FRACTURE,
        YEETER,
        WATERGAME
    }

    static Superstructure getInstance() {
        return instance;
    }
}

