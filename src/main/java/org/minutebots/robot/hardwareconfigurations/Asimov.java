package org.minutebots.robot.hardwareconfigurations;

import com.ctre.phoenix.motorcontrol.can.WPI_VictorSPX;
import com.kauailabs.navx.frc.AHRS;
import edu.wpi.first.wpilibj.*;
import edu.wpi.first.wpilibj.DoubleSolenoid.Value;
import edu.wpi.first.wpilibj.command.InstantCommand;
import edu.wpi.first.wpilibj.shuffleboard.BuiltInLayouts;
import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard;
import edu.wpi.first.wpilibj.shuffleboard.ShuffleboardLayout;
import org.minutebots.robot.subsystems.Drivetrain;
import org.minutebots.robot.subsystems.HatchPiston;

import java.util.Map;

public class Asimov implements HardwareManger {
    //Victor
    final static int LEFT_FRONT_MOTOR = 0,
            LEFT_BACK_MOTOR = 3,
            RIGHT_FRONT_MOTOR = 1,
            RIGHT_BACK_MOTOR = 2;
    //Pneumatic Ports
    final static int PISTON_1 = 6,
            PISTON_2 = 7,
            ACTIVEH_1 = 4,
            ACTIVEH_2 = 5,
            RAMPLOCK_1 = 3,
            RAMPLOCK_2 = 2;

    //Talons Ports
    final static int DEPOT_ARM = 1,
            DEPOT_WHEEL = 0,
            RAMP = 2;

    final static int UP_LIMIT_SWITCH = 0, DOWN_LIMIT_SWITCH = 1;

    private WPI_VictorSPX leftFront = new WPI_VictorSPX(LEFT_FRONT_MOTOR),
            rightFront = new WPI_VictorSPX(RIGHT_FRONT_MOTOR),
            leftBack = new WPI_VictorSPX(LEFT_BACK_MOTOR),
            rightBack = new WPI_VictorSPX(RIGHT_BACK_MOTOR);

    private Talon armMotor = new Talon(DEPOT_ARM);

    private VictorSP wheelMotor = new VictorSP(DEPOT_WHEEL),
            rampMotor = new VictorSP(RAMP);

    private DigitalInput upLimit = new DigitalInput(UP_LIMIT_SWITCH),
            downLimit = new DigitalInput(DOWN_LIMIT_SWITCH);

    private DoubleSolenoid piston = new DoubleSolenoid(PISTON_1, PISTON_2),
    activeHatch = new DoubleSolenoid(ACTIVEH_1, ACTIVEH_2),
    rampLock = new DoubleSolenoid(RAMPLOCK_1, RAMPLOCK_2);

    private AHRS navX = new AHRS(SPI.Port.kMXP);

    private Ultrasonic ultraS =  new Ultrasonic(2, 3);

    //private PowerDistributionPanel pdp = new PowerDistributionPanel();

    @Override
    public void init(){
        //Shuffleboard.getTab("Debugging").add(pdp);
        ultraS.setAutomaticMode(true);
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

        ShuffleboardLayout depotArm = Shuffleboard.getTab("Debugging")
                .getLayout("Depot Arm", BuiltInLayouts.kList);
        depotArm.add("Depot Arm", armMotor);
        depotArm.add("Depot Wheel", wheelMotor);
        depotArm.add("Arm Up", upLimit);
        depotArm.add("Arm Down", downLimit);

        ShuffleboardLayout intake = Shuffleboard.getTab("Debugging")
                .getLayout("Intake", BuiltInLayouts.kList).withProperties(Map.of("Label position", "HIDDEN"));
        intake.add("Piston", piston);
        intake.add("Ultrasonic", ultraS);
        intake.add(HatchPiston.extend());
        intake.add(HatchPiston.retract());
        intake.add(HatchPiston.eject());
        intake.add(HatchPiston.grabHatch());

        Shuffleboard.getTab("Debugging").add("Ramp Motor", rampMotor);
    }

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
        piston.set(DoubleSolenoid.Value.kForward); //Yes, this value are intentional. The solenoid is backwards.
    }

    @Override
    public void retractIntakePiston() {
        piston.set(DoubleSolenoid.Value.kReverse); //Yes, this value are intentional. The solenoid is backwards.
    }

    @Override
    public void closeSolenoids() {
        piston.set(DoubleSolenoid.Value.kOff);
        activeHatch.set(Value.kOff);
        rampLock.set(Value.kOff);
    }


    @Override
    public double getAngle() {
        return navX.getAngle();
    }

    @Override
    public void extendActiveHatch() {
        activeHatch.set(DoubleSolenoid.Value.kForward);
    }

    @Override
    public void retractActiveHatch() {
        activeHatch.set(DoubleSolenoid.Value.kReverse);
        
    }

    @Override
    public void extendRampLock(){
        rampLock.set(Value.kForward);
    };

    @Override
    public void retractRampLock(){
        rampLock.set(Value.kReverse);
    };


    /**
     * @return The distance from the ultrasonic sensor in inches.
     */
    @Override
    public double ultraSonicDist() {
        return ultraS.getRangeInches();
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
