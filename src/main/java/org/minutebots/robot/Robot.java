package org.minutebots.robot;

import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.command.Scheduler;
import org.minutebots.robot.hardwareconfigurations.*;
import org.minutebots.robot.subsystems.Drivetrain;
import org.minutebots.robot.utilities.VisionCommunication;

public class Robot extends TimedRobot {

  public static HardwareManger hardwareManager = new Fracture();

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
