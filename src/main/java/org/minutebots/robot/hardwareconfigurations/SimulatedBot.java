package org.minutebots.robot.hardwareconfigurations;

import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.wpilibj.SpeedController;
import edu.wpi.first.wpilibj.shuffleboard.BuiltInWidgets;
import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard;
import org.minutebots.lib.VirtualMotor;

public class SimulatedBot implements HardwareManger {
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
            new VirtualMotor(Asimov.LEFT_FRONT_MOTOR),
            new VirtualMotor(Asimov.LEFT_BACK_MOTOR),
            new VirtualMotor(Asimov.RIGHT_FRONT_MOTOR),
            new VirtualMotor(Asimov.RIGHT_BACK_MOTOR)};

    private VirtualMotor arm = new VirtualMotor(Asimov.DEPOT_ARM),
            roller = new VirtualMotor(Asimov.DEPOT_WHEEL),
            ramp = new VirtualMotor(Asimov.RAMP);

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
    public void closeSolenoids() {
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
