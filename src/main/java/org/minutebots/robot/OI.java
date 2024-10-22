package org.minutebots.robot;

import org.minutebots.robot.subsystems.DepotArm;
import org.minutebots.robot.subsystems.Drivetrain;
import org.minutebots.robot.subsystems.HatchPiston;
import org.minutebots.robot.subsystems.Ramp;
import org.minutebots.robot.utilities.Constants;
import org.minutebots.robot.utilities.VisionCommunication;
import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj.GenericHID.Hand;

public class OI {

    /*---------------------------------------Programmer's territory starts here----------------------------------*/
    /*
    public static final Joystick primaryStick = new Joystick(0);
    public static final Joystick secondaryStick = new Joystick(1);
    public static final JoystickButton trigger = new JoystickButton(primaryStick, 1),
            angleAdjustLeft = new JoystickButton(primaryStick, 7),
            angleAdjustRight = new JoystickButton(primaryStick, 8),
            ejectPrimary = new JoystickButton(primaryStick, 5),
            ramp = new JoystickButton(primaryStick,3),
            highExposure = new JoystickButton(primaryStick, 2),
            lockCargo = new JoystickButton(primaryStick, 4),
            grabHatchPrimary = new JoystickButton(primaryStick, 6);
    public static final JoystickButton snapTo = new JoystickButton(secondaryStick, 3),
            fineTurn = new JoystickButton(secondaryStick, 1),
            defenseTurn = new JoystickButton(secondaryStick, 7),
            visionRotate = new JoystickButton(secondaryStick, 2),
            ejectSecondary = new JoystickButton(secondaryStick, 5),
            spinArmForwards = new JoystickButton(secondaryStick, 6),
            spinArmBackwards = new JoystickButton(secondaryStick, 4),
            strafe = new JoystickButton(secondaryStick, 3),
            closeSolenoids = new JoystickButton(secondaryStick, 8),
            visionOveride = new JoystickButton(secondaryStick, 9),
            resetGyro = new JoystickButton(secondaryStick, 10),
            grabHatch = new JoystickButton(secondaryStick, 11),
            unlockCargo = new JoystickButton(secondaryStick, 12);


    static {
        snapTo.whileHeld(new InstantCommand(() -> Drivetrain.getInstance().setSetpoint(Drivetrain.getInstance().rocketMode.getBoolean(false) ? Drivetrain.getInstance().getNearestRocket() : Drivetrain.getInstance().getNearestSquare())));
        //fineTurn.whenReleased(new InstantCommand(() -> Drivetrain.getInstance().setSetpoint(Drivetrain.getInstance().getYaw())));
        //defenseTurn.whenReleased(new InstantCommand(() -> Drivetrain.getInstance().setSetpoint(Drivetrain.getInstance().getYaw())));
        //angleAdjustLeft.whileHeld(new InstantCommand(() -> Drivetrain.getInstance().adjustAngle(3.0)));
        //angleAdjustRight.whileHeld(new InstantCommand(() -> Drivetrain.getInstance().adjustAngle(-3.0)));
        highExposure.whenPressed(VisionCommunication.getInstance().highExposure());
        ejectPrimary.whenPressed(HatchPiston.extend());
        ejectPrimary.whenReleased(HatchPiston.retract());
        //ejectSecondary.whenPressed(HatchPiston.extend());
        //ejectSecondary.whenReleased(HatchPiston.retract());
        ramp.whenPressed(Ramp.spinWheel(Constants.RAMP_MAX_SPEED));
        lockCargo.whenPressed(Ramp.lockCargo());
        //grabHatch.whenPressed(HatchPiston.grabHatch());
        //grabHatch.whenReleased(HatchPiston.retractHatchM());
        grabHatchPrimary.whenPressed(HatchPiston.grabHatch());
        grabHatchPrimary.whenReleased(HatchPiston.retractHatchM());
        ramp.whenReleased(new InstantCommand(() -> Ramp.getInstance().setWheel(0)));
        unlockCargo.whenReleased(Ramp.retractLock());
        visionOveride.whenPressed(VisionCommunication.getInstance().highExposure());
        resetGyro.whenPressed(new InstantCommand(() -> Drivetrain.getInstance().resetGyro()));
        spinArmBackwards.whenPressed(DepotArm.armDown(false));
        spinArmForwards.whenPressed(DepotArm.armDown(true));
        spinArmBackwards.whenReleased(DepotArm.armUp());
        spinArmForwards.whenReleased(DepotArm.armUp());
        closeSolenoids.whenPressed(new InstantCommand(() -> Robot.hardwareManager.closeSolenoids()));
    }
    */

    
    public static final XboxController xController = new XboxController(0);
    public static void loop(){
        /* 
        Ltrigger = rotate depot forward
        Rtrigger =
        Lstick = strafe f, b, l, r
        Rstick = rotate robot
        Lbumper = reset gyro
        Rbumber = 
        Y = spin ramp / unlock ramp
        B = eject hatch
        A = grab hatch
        X = lock ramp
        Start = 
        Back = 
        D-pad = 
        */
        ////highExposure.whenPressed(VisionCommunication.getInstance().highExposure());
        //ejectPrimary.whenPressed(HatchPiston.extend());
        //ejectPrimary.whenReleased(HatchPiston.retract());
        //ramp.whenPressed(Ramp.spinWheel(Constants.RAMP_MAX_SPEED));
        //lockCargo.whenPressed(Ramp.lockCargo());
        //grabHatchPrimary.whenPressed(HatchPiston.grabHatch());
        //grabHatchPrimary.whenReleased(HatchPiston.retractHatchM());
        //ramp.whenReleased(new InstantCommand(() -> Ramp.getInstance().setWheel(0)));
        //unlockCargo.whenReleased(Ramp.retractLock());
        ////visionOveride.whenPressed(VisionCommunication.getInstance().highExposure());
        //resetGyro.whenPressed(new InstantCommand(() -> Drivetrain.getInstance().resetGyro()));
        //spinArmBackwards.whenPressed(DepotArm.armDown(false));
        //spinArmForwards.whenPressed(DepotArm.armDown(true));
        //spinArmBackwards.whenReleased(DepotArm.armUp());
        //spinArmForwards.whenReleased(DepotArm.armUp());
        ////closeSolenoids.whenPressed(new InstantCommand(() -> Robot.hardwareManager.closeSolenoids()));

        if(xController.getBumperPressed(Hand.kLeft))Drivetrain.getInstance().resetGyro();
        if(xController.getTriggerAxis(Hand.kRight)>0.9){
            DepotArm.armDown(true).start();
        }else{
            DepotArm.armUp();
        }
        if(xController.getTriggerAxis(Hand.kLeft)>0.9){
            DepotArm.armDown(true).start();
        }else{
            DepotArm.armUp();
        }
        if(xController.getTriggerAxis(Hand.kRight)>0.5)DepotArm.armDown(true).start();
        if(xController.getTriggerAxis(Hand.kLeft)>0.5)DepotArm.armDown(true).start();
        if (OI.xController.getBumperPressed(Hand.kRight))
            VisionCommunication.getInstance().toggleDriverVision(false);
        if(OI.xController.getBumperReleased(Hand.kRight))
            VisionCommunication.getInstance().toggleDriverVision(true);
        if((xController.getTriggerAxis(Hand.kLeft)<0.5) && (xController.getTriggerAxis(Hand.kRight)<0.5))DepotArm.armUp().start();
        if(xController.getAButtonPressed())HatchPiston.grabHatch().start();
        if(xController.getAButtonReleased())HatchPiston.retractHatchM().start();
        if(xController.getBButtonPressed())HatchPiston.extend().start();
        if(xController.getBButtonReleased())HatchPiston.retract().start();
        if(xController.getXButtonPressed())Ramp.lockCargo().start();
        if(xController.getYButtonPressed()){
            Ramp.retractLock().start();
            Ramp.spinWheel(Constants.RAMP_MAX_SPEED).start();
        }else{
            Ramp.spinWheel(0).start();
        }
        if(xController.getYButtonReleased())Ramp.getInstance().setWheel(0);
        

        


    }



}
