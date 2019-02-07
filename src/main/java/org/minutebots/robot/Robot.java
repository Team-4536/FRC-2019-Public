package org.minutebots.robot;

import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.command.InstantCommand;
import edu.wpi.first.wpilibj.command.Scheduler;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import org.minutebots.robot.commands.StrafeToVisionTarget;
import org.minutebots.robot.subsystems.Drivetrain;
import org.minutebots.robot.utilities.VisionCommunication;

public class Robot extends TimedRobot {

  @Override
  public void robotInit() {
  }

  @Override
  public void robotPeriodic() {
    VisionCommunication.getInstance().update();
  }

  @Override
  public void autonomousInit() {
  }

  @Override
  public void autonomousPeriodic() {
  }

  @Override
  public void teleopInit(){
    Drivetrain.getInstance().enable();
    Drivetrain.getInstance().backupDrive = false;
    SmartDashboard.putData("Strafe To Target", new StrafeToVisionTarget());
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
