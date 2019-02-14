package org.minutebots.robot;

import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.command.InstantCommand;
import edu.wpi.first.wpilibj.command.Scheduler;
import edu.wpi.first.wpilibj.shuffleboard.BuiltInLayouts;
import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard;
import edu.wpi.first.wpilibj.shuffleboard.ShuffleboardLayout;
import org.minutebots.robot.commands.StrafeToVisionTarget;
import org.minutebots.robot.hardwareconfigurations.*;
import org.minutebots.robot.subsystems.Drivetrain;
import org.minutebots.robot.subsystems.HatchPiston;
import org.minutebots.robot.utilities.VisionCommunication;

import java.util.Map;

public class Robot extends TimedRobot {

  public static HardwareManger hardwareManager = new Yeeter();

  @Override
  public void robotInit() {
    hardwareManager.init();
  }

  @Override
  public void robotPeriodic() {
    VisionCommunication.getInstance().update();
  }

  @Override
  public void autonomousInit() {

    ShuffleboardLayout drivetrainInfo = Shuffleboard.getTab("Debugging")
            .getLayout("Drivetrain", BuiltInLayouts.kList);
    drivetrainInfo.add("PID Controller", Drivetrain.getInstance().getPIDController());

    ShuffleboardLayout drivetrainCommands = Shuffleboard.getTab("Debugging")
            .getLayout("Drivetrain Commands", BuiltInLayouts.kList).withProperties(Map.of("Label position", "HIDDEN"));
    drivetrainCommands.add("Current Commands", Drivetrain.getInstance());
    if(Drivetrain.getInstance().getCurrentCommand() != null)
      drivetrainCommands.add("Remove Current Command", new InstantCommand(() -> {Drivetrain.getInstance().getCurrentCommand().cancel();}));
    drivetrainCommands.add(new InstantCommand("Set Setpoint 0",() -> Drivetrain.getInstance().setSetpoint(0)));
    drivetrainCommands.add(new InstantCommand("Set Setpoint 90",() -> Drivetrain.getInstance().setSetpoint(90)));
    drivetrainCommands.add(new InstantCommand("Set Setpoint 180",() -> Drivetrain.getInstance().setSetpoint(180)));
    drivetrainCommands.add(new InstantCommand("Set Setpoint 270",() -> Drivetrain.getInstance().setSetpoint(270)));
    drivetrainCommands.add(new StrafeToVisionTarget());

    ShuffleboardLayout intake = Shuffleboard.getTab("Debugging")
            .getLayout("Intake", BuiltInLayouts.kList).withProperties(Map.of("Label position", "HIDDEN"));
    intake.add(HatchPiston.extend());
    intake.add(HatchPiston.retract());
    intake.add(HatchPiston.eject());

  }

  @Override
  public void autonomousPeriodic() {
  }

  @Override
  public void teleopInit(){
    Drivetrain.getInstance().setSetpoint(Drivetrain.getInstance().getPosition());
    Drivetrain.getInstance().enable();
  }

  @Override
  public void teleopPeriodic() {
    Scheduler.getInstance().run();
  }

  @Override
  public void testPeriodic() {
  }

  @Override
  public void disabledInit(){
    Drivetrain.getInstance().disable();
  }
}
