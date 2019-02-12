package org.minutebots.robot.hardwareconfigurations;

import com.kauailabs.navx.frc.AHRS;
import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.wpilibj.AnalogInput;
import edu.wpi.first.wpilibj.SPI;
import edu.wpi.first.wpilibj.Spark;
import edu.wpi.first.wpilibj.SpeedController;
import edu.wpi.first.wpilibj.command.InstantCommand;
import edu.wpi.first.wpilibj.shuffleboard.*;
import org.minutebots.lib.VirtualMotor;
import org.minutebots.robot.commands.StrafeToVisionTarget;
import org.minutebots.robot.subsystems.Drivetrain;

import java.util.Map;

public class Fracture implements HardwareManger {

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

    /**
     * @return The distance from the ultrasonic sensor in inches.
     */
    @Override
    public double ultraSonicDist() {
        return ultraS.getVoltage()*103.481+1.64105; //106.007 for one value
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
