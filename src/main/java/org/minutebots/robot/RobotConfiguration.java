package org.minutebots.robot;

import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;
import com.ctre.phoenix.motorcontrol.can.WPI_VictorSPX;
import com.kauailabs.navx.frc.AHRS;
import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.wpilibj.*;
import edu.wpi.first.wpilibj.shuffleboard.BuiltInWidgets;
import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard;
import org.minutebots.lib.VirtualMotor;

import java.util.Map;

/**
 * This is our way to abstract hardware from the software. Everything that is an output or an imput
 * must be in here.
 */

public interface RobotConfiguration {
    SpeedController[] drivetrainMotors();

    void resetGyro();

    SpeedController depotArm();

    SpeedController depotRoller();

    SpeedController rampMotor();

    void extendIntakePiston();

    void retractIntakePiston();

    double getAngle();

    double ultraSonicDist();

    boolean armUp();

    boolean armDown();
}

class Yeeter implements RobotConfiguration {

    Yeeter() {
        Shuffleboard.getTab("Motors").add("Left Front DM 2", leftFront);
        Shuffleboard.getTab("Motors").add("Right Front DM 0", rightFront);
        Shuffleboard.getTab("Motors").add("Left Back DM 1", leftBack);
        Shuffleboard.getTab("Motors").add("Right Back DM 3", rightBack);
        Shuffleboard.getTab("Motors").add("Depot Arm", armMotor);
        Shuffleboard.getTab("Motors").add("Depot Wheel", wheelMotor);
        Shuffleboard.getTab("Motors").add("Arm Up", upLimit);
        Shuffleboard.getTab("Motors").add("Arm Down", downLimit);
    }

    //Victor
    final static int LEFT_FRONT_MOTOR = 0,
            LEFT_BACK_MOTOR = 3,
            RIGHT_FRONT_MOTOR = 1,
            RIGHT_BACK_MOTOR = 2;
    //Pneumatic Ports
    final static int PISTON_1 = 6,
            PISTON_2 = 7;

    //Talons Ports
    final static int DEPOT_ARM = 4,
            DEPOT_WHEEL = 5,
            RAMP = 6;

    final static int UP_LIMIT_SWITCH = 0, DOWN_LIMIT_SWITCH = 1;

    private WPI_VictorSPX leftFront = new WPI_VictorSPX(LEFT_FRONT_MOTOR),
            rightFront = new WPI_VictorSPX(RIGHT_FRONT_MOTOR),
            leftBack = new WPI_VictorSPX(LEFT_BACK_MOTOR),
            rightBack = new WPI_VictorSPX(RIGHT_BACK_MOTOR);

    private WPI_TalonSRX armMotor = new WPI_TalonSRX(DEPOT_ARM),
            wheelMotor = new WPI_TalonSRX(DEPOT_WHEEL),
            rampMotor = new WPI_TalonSRX(RAMP);

    private DigitalInput upLimit = new DigitalInput(UP_LIMIT_SWITCH),
            downLimit = new DigitalInput(DOWN_LIMIT_SWITCH);

    private DoubleSolenoid piston = new DoubleSolenoid(PISTON_1, PISTON_2);

    private AHRS navX = new AHRS(SPI.Port.kMXP);

    @Override
    public SpeedController[] drivetrainMotors() {
        return new SpeedController[]{leftFront, leftBack, rightFront, rightBack};
    }

    @Override
    public void resetGyro() {
        navX.reset();
    }

    @Override
    public SpeedController rampMotor() {
        return rampMotor;
    }

    @Override
    public SpeedController depotArm() {
        return armMotor;
    }

    @Override
    public SpeedController depotRoller() {
        return wheelMotor;
    }

    @Override
    public void extendIntakePiston() {
        piston.set(DoubleSolenoid.Value.kReverse); //Yes, this value are intentional. The solenoid is backwards.
    }

    @Override
    public void retractIntakePiston() {
        piston.set(DoubleSolenoid.Value.kForward); //Yes, this value are intentional. The solenoid is backwards.
    }

    @Override
    public double getAngle() {
        return navX.getAngle();
    }

    @Override
    public double ultraSonicDist() {
        return 0;
    }

    @Override
    public boolean armUp() {
        return !upLimit.get();
    }

    @Override
    public boolean armDown() {
        return !downLimit.get();
    }
}

class SimulatedBot implements RobotConfiguration {
    private NetworkTableEntry piston = Shuffleboard.getTab("Virtual Motors")
            .add("Intake Piston", false).getEntry(),
            gyro = Shuffleboard.getTab("Virtual Motors")
                    .add("Gyro Angle", 0)
                    .withWidget(BuiltInWidgets.kTextView)
                    .getEntry(),
            topLimit = Shuffleboard.getTab("Virtual Motors")
                    .add("Top Limit Switch", false)
                    .withWidget(BuiltInWidgets.kToggleButton)
                    .getEntry(),
            bottomLimit = Shuffleboard.getTab("Virtual Motors")
                    .add("Bottom Limit Switch", false)
                    .withWidget(BuiltInWidgets.kToggleButton)
                    .getEntry();

    private SpeedController[] motors = new SpeedController[]{
            new VirtualMotor(Yeeter.LEFT_FRONT_MOTOR),
            new VirtualMotor(Yeeter.LEFT_BACK_MOTOR),
            new VirtualMotor(Yeeter.RIGHT_FRONT_MOTOR),
            new VirtualMotor(Yeeter.RIGHT_BACK_MOTOR)};

    private VirtualMotor arm = new VirtualMotor(Yeeter.DEPOT_ARM),
            roller = new VirtualMotor(Yeeter.DEPOT_WHEEL),
            ramp = new VirtualMotor(Yeeter.RAMP);

    @Override
    public SpeedController[] drivetrainMotors() {
        return motors;
    }

    @Override
    public void resetGyro() {
        gyro.setDouble(0);
    }

    @Override
    public SpeedController depotArm() {
        return arm;
    }

    @Override
    public SpeedController depotRoller() {
        return roller;
    }

    @Override
    public SpeedController rampMotor() {
        return ramp;
    }

    @Override
    public void extendIntakePiston() {
        piston.setBoolean(true);
    }

    @Override
    public void retractIntakePiston() {
        piston.setBoolean(false);
    }

    @Override
    public double getAngle() {
        return -gyro.getDouble(0);
    }

    @Override
    public double ultraSonicDist() {
        return 0;
    }

    @Override
    public boolean armUp() {
        return topLimit.getBoolean(false);
    }

    @Override
    public boolean armDown() {
        return bottomLimit.getBoolean(false);
    }
}

class Fracture implements RobotConfiguration {

    private AHRS navX = new AHRS(SPI.Port.kMXP);
    private Servo servo = new Servo(5);

    private SpeedController[] driveTrainMotors = new SpeedController[]{
            new Spark(2),
            new Spark(1),
            new Spark(0),
            new Spark(3)
    };

    private VirtualMotor arm = new VirtualMotor(Yeeter.DEPOT_ARM),
            roller = new VirtualMotor(Yeeter.DEPOT_WHEEL),
            ramp = new VirtualMotor(Yeeter.RAMP);

    @Override
    public SpeedController[] drivetrainMotors() {
        return driveTrainMotors;
    }

    @Override
    public void resetGyro() {
        navX.reset();
    }

    @Override
    public SpeedController depotArm() {
        return arm;
    }

    @Override
    public SpeedController depotRoller() {
        return roller;
    }

    @Override
    public SpeedController rampMotor() {
        return ramp;
    }

    @Override
    public void extendIntakePiston() {
        servo.set(1);
    }

    @Override
    public void retractIntakePiston() {
        servo.set(0);
    }

    @Override
    public double getAngle() {
        return navX.getAngle();
    }

    @Override
    public double ultraSonicDist() {
        return 0;
    }

    @Override
    public boolean armUp() {
        return false;
    }

    @Override
    public boolean armDown() {
        return false;
    }
}