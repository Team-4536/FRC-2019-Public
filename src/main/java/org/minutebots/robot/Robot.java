package org.minutebots.robot;

import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.command.InstantCommand;
import edu.wpi.first.wpilibj.command.Scheduler;
import edu.wpi.first.wpilibj.shuffleboard.BuiltInLayouts;
import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard;
import edu.wpi.first.wpilibj.shuffleboard.ShuffleboardLayout;

import java.util.Map;

import org.minutebots.robot.commands.StrafeToVisionTarget;
import org.minutebots.robot.hardwareconfigurations.*;
import org.minutebots.robot.subsystems.Drivetrain;
import org.minutebots.robot.subsystems.HatchPiston;
import org.minutebots.robot.utilities.VisionCommunication;

public class Robot extends TimedRobot {

  public static HardwareManger hardwareManager = new Fracture();
  public static boolean isAuto = false;

  @Override
  public void robotInit() {
    hardwareManager.init();
    VisionCommunication.getInstance().highExposure();
    ShuffleboardLayout drivetrainInfo = Shuffleboard.getTab("Debugging")
                .getLayout("Drivetrain", BuiltInLayouts.kList);
                drivetrainInfo.add("PID Controller", Drivetrain.getInstance().getPIDController());
                ShuffleboardLayout drivetrainCommands = Shuffleboard.getTab("Debugging")
                .getLayout("Drivetrain Commands", BuiltInLayouts.kList).withProperties(Map.of("Label position", "HIDDEN"));
        drivetrainCommands.add("Current Commands", Drivetrain.getInstance());
        drivetrainCommands.add("Remove Current Command", new InstantCommand(() -> Drivetrain.getInstance().getCurrentCommand().cancel()));
        drivetrainCommands.add(new StrafeToVisionTarget());

        ShuffleboardLayout intake = Shuffleboard.getTab("Debugging")
                .getLayout("Intake", BuiltInLayouts.kList).withProperties(Map.of("Label position", "HIDDEN"));
        intake.add(HatchPiston.extend());
        intake.add(HatchPiston.retract());
        intake.add(HatchPiston.eject());
  }

  @Override
  public void robotPeriodic() {
  }

  @Override
  public void autonomousInit() {
    isAuto = true;
    VisionCommunication.getInstance().highExposure();
    Drivetrain.getInstance().setSetpoint(Drivetrain.getInstance().getPosition());
    Drivetrain.getInstance().enable();
  }

  @Override
  public void autonomousPeriodic() {
    Scheduler.getInstance().run();
  }

  @Override
  public void teleopInit(){
    isAuto = false;
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
