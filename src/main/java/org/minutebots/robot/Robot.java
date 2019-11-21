package org.minutebots.robot;

import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.command.Scheduler;
import org.minutebots.robot.hardwareconfigurations.*;
import org.minutebots.robot.subsystems.Drivetrain;
import org.minutebots.robot.utilities.VisionCommunication;

public class Robot extends TimedRobot {

  public static HardwareManger hardwareManager = new Asimov();
  public static boolean isAuto = false;
  

  @Override
  public void robotInit() {
    hardwareManager.init();
    hardwareManager.closeSolenoids();
  }

  @Override
  public void robotPeriodic() {
    OI.loop();
  }

  @Override
  public void disabledPeriodic(){
  }

  @Override
  public void autonomousInit() {
    isAuto = true;
    Drivetrain.getInstance().resetGyro();
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
