package org.minutebots.robot.commands;
import edu.wpi.first.wpilibj.livewindow.LiveWindow;

import edu.wpi.first.wpilibj.command.PIDCommand;
import org.minutebots.robot.OI;
import org.minutebots.robot.subsystems.Drivetrain;

public class DrivePID extends PIDCommand {
    private double turnThrottle = 0, setpoint = 0;

    public DrivePID() {
        super(0.032, 0.0, 0.1);
        setpoint = Drivetrain.getInstance().getAngle();
        LiveWindow.add(this.getPIDController());
    }

    @Override
    protected void initialize() {
        setInputRange(-180.0, 180.0);
        getPIDController().setContinuous(true);
    }

    @Override
    protected void execute() {
        if(OI.primaryStick.getPOV() != -1)
        setSetpoint(setpoint);
        Drivetrain.getInstance().mecanumDrive(OI.primaryStick.getX(), -OI.primaryStick.getY(), turnThrottle);
    }

    @Override
    protected boolean isFinished() {
        return false;
    }

    @Override
    protected double returnPIDInput() {
        return Drivetrain.getInstance().getAngle();
    }

    @Override
    protected void usePIDOutput(double output) {
        this.turnThrottle = output;
    }
}