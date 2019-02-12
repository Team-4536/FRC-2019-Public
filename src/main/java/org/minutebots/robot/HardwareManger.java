package org.minutebots.robot;

import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;
import com.ctre.phoenix.motorcontrol.can.WPI_VictorSPX;
import com.kauailabs.navx.frc.AHRS;
import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.wpilibj.*;
import edu.wpi.first.wpilibj.command.InstantCommand;
import edu.wpi.first.wpilibj.shuffleboard.*;
import org.minutebots.lib.VirtualMotor;
import org.minutebots.robot.commands.StrafeToVisionTarget;
import org.minutebots.robot.subsystems.Drivetrain;
import org.minutebots.robot.subsystems.HatchPiston;

import java.util.Map;


/**
 * This is our way to abstract hardware from the software. Everything that is an output or an imput
 * must be in here.
 */

public interface HardwareManger {
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

    void init();
}

class Yeeter implements HardwareManger {

    @Override
    public void init(){
        ShuffleboardLayout drivetrainInfo = Shuffleboard.getTab("Debugging")
                .getLayout("Drivetrain", BuiltInLayouts.kList);
        drivetrainInfo.add("Left Front DM 2", leftFront);
        drivetrainInfo.add("Right Front DM 0", rightFront);
        drivetrainInfo.add("Left Back DM 1", leftBack);
        drivetrainInfo.add("Right Back DM 3", rightBack);
        drivetrainInfo.add("PID Controller", Drivetrain.getInstance().getPIDController());


        ShuffleboardLayout drivetrainCommands = Shuffleboard.getTab("Debugging")
                .getLayout("Drivetrain Commands", BuiltInLayouts.kList).withProperties(Map.of("Label position", "HIDDEN"));
        drivetrainCommands.add("Current Commands", Drivetrain.getInstance());
        drivetrainCommands.add("Remove Current Command", new InstantCommand(() -> Drivetrain.getInstance().getCurrentCommand().cancel()));
        drivetrainCommands.add(new InstantCommand("Set Setpoint 0",() -> Drivetrain.getInstance().setSetpoint(0)));
        drivetrainCommands.add(new InstantCommand("Set Setpoint 90",() -> Drivetrain.getInstance().setSetpoint(90)));
        drivetrainCommands.add(new InstantCommand("Set Setpoint 180",() -> Drivetrain.getInstance().setSetpoint(180)));
        drivetrainCommands.add(new InstantCommand("Set Setpoint 270",() -> Drivetrain.getInstance().setSetpoint(270)));
        drivetrainCommands.add(new StrafeToVisionTarget());

        ShuffleboardLayout depotArm = Shuffleboard.getTab("Debugging")
                .getLayout("Depot Arm", BuiltInLayouts.kList);
        depotArm.add("Depot Arm", armMotor);
        depotArm.add("Depot Wheel", wheelMotor);
        depotArm.add("Arm Up", upLimit);
        depotArm.add("Arm Down", downLimit);

        ShuffleboardLayout intake = Shuffleboard.getTab("Debugging")
                .getLayout("Intake", BuiltInLayouts.kList).withProperties(Map.of("Label position", "HIDDEN"));
        intake.add("Piston", piston);
        intake.add(HatchPiston.extend());
        intake.add(HatchPiston.retract());
        intake.add(HatchPiston.eject());

        Shuffleboard.getTab("Debugging").add("Ramp Motor", rampMotor);
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

class SimulatedBot implements HardwareManger {
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

    @Override
    public void init() {

    }
}

class Fracture implements HardwareManger {

    private AHRS navX = new AHRS(SPI.Port.kMXP);
    private AnalogInput ultraS = new AnalogInput(0);

    private NetworkTableEntry piston = Shuffleboard.getTab("Virtual Motors")
            .add("Intake Piston", false).getEntry(),
            topLimit = Shuffleboard.getTab("Virtual Motors")
            .add("Top Limit Switch", false)
            .withWidget(BuiltInWidgets.kToggleButton)
            .getEntry(),
            bottomLimit = Shuffleboard.getTab("Virtual Motors")
                    .add("Bottom Limit Switch", false)
                    .withWidget(BuiltInWidgets.kToggleButton)
                    .getEntry();

    private SpeedController[] driveTrainMotors = new SpeedController[]{
            new Spark(2),
            new Spark(1),
            new Spark(0),
            new Spark(3)
    };

    private VirtualMotor arm = new VirtualMotor(Yeeter.DEPOT_ARM),
            roller = new VirtualMotor(Yeeter.DEPOT_WHEEL),
            ramp = new VirtualMotor(Yeeter.RAMP);

    Servo servo = new Servo(9);

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
        piston.setBoolean(true);
    }

    @Override
    public void retractIntakePiston() {
        piston.setBoolean(false);
    }

    @Override
    public double getAngle() {
        return navX.getAngle();
    }

    @Override
    public double ultraSonicDist() {
        return ultraS.getVoltage()/0.0097;
    }

    @Override
    public boolean armUp() {
        return topLimit.getBoolean(false);
    }

    @Override
    public boolean armDown() {
        return bottomLimit.getBoolean(false);
    }

    @Override
    public void init() {
        ShuffleboardTab debug = Shuffleboard.getTab("Debugging");
        debug.add("Ultrasonic", ultraS);

        ShuffleboardLayout drivetrainCommands = Shuffleboard.getTab("Debugging")
                .getLayout("Drivetrain Commands", BuiltInLayouts.kList).withProperties(Map.of("Label position", "HIDDEN"));
        drivetrainCommands.add("Current Commands", Drivetrain.getInstance());
        drivetrainCommands.add("Remove Current Command", new InstantCommand(() -> Drivetrain.getInstance().getCurrentCommand().cancel()));
        drivetrainCommands.add(new InstantCommand("Set Setpoint 0",() -> Drivetrain.getInstance().setSetpoint(0)));
        drivetrainCommands.add(new InstantCommand("Set Setpoint 90",() -> Drivetrain.getInstance().setSetpoint(90)));
        drivetrainCommands.add(new InstantCommand("Set Setpoint 180",() -> Drivetrain.getInstance().setSetpoint(180)));
        drivetrainCommands.add(new InstantCommand("Set Setpoint 270",() -> Drivetrain.getInstance().setSetpoint(270)));
        drivetrainCommands.add(new StrafeToVisionTarget());
    }
}