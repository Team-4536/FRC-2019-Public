package org.minutebots.robot.utilities;

import org.minutebots.robot.OI;

import edu.wpi.first.networktables.EntryListenerFlags;
import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.networktables.NetworkTableInstance;
import edu.wpi.first.wpilibj.command.InstantCommand;
import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;

public class VisionCommunication{
    NetworkTable table = NetworkTableInstance.getDefault().getTable("Vision");
    private NetworkTableEntry angles = table.getEntry("Target Angles"),
    exposure = table.getEntry("Exposure"),
    switchCamera = table.getEntry("BackCamera"),
    brightness = table.getEntry("Brightness");
    private SendableChooser<VisionCommunication.TargetSelection> visionMode = new SendableChooser<>();

    public NetworkTableEntry cargoMode = Shuffleboard.getTab("Drive")
            .add("Cargo Mode", false)
            .withWidget("Toggle Button")
            .getEntry();

    private double[] angleOffsets;
    //private TargetSelection selection = TargetSelection.MIDDLE;

    private VisionCommunication(){
        NetworkTableInstance.getDefault()
            .getEntry("/CameraPublisher/PiCamera/streams")
            .setStringArray(new String[]{"http://frcvision.local:1181/stream.mjpg"});

        cargoMode.addListener(event -> {
            switchCamera.setBoolean(event.value.getBoolean());
            System.out.println("Back Camera Activated: " + event.value.getBoolean());
        }, EntryListenerFlags.kNew | EntryListenerFlags.kUpdate);

        table.getEntry("Connected").setBoolean(true);

        visionMode.setName("Vision Mode");
        visionMode.addObject("Left", TargetSelection.LEFT);
        visionMode.addObject("Right", TargetSelection.RIGHT);
        visionMode.addDefault("Middle", TargetSelection.MIDDLE);

        Shuffleboard.getTab("Drive").add(visionMode);
    //.setStringArray(new String[]{"mjpeg:http://" + "10.0.0.9" + ":" + "1181" + "/?action=stream"});
    }

    public void update(){
        
    }

    private static VisionCommunication instance = new VisionCommunication();

    public static VisionCommunication getInstance() {
        return instance;
    }

    /*
    public InstantCommand setSelection(TargetSelection s){
        return new InstantCommand("Target " + s.name(),() -> selection = s);
    }
    */

    public double getAngle() {
        if(OI.visionOveride.get()){
            exposure.setDouble(40);
            return 0;
        }
        exposure.setDouble(0.0);
        angleOffsets = angles.getDoubleArray(new double[]{0});
        TargetSelection selection = visionMode.getSelected();
        if(selection == TargetSelection.LEFT && angleOffsets.length > 0) return angleOffsets[0];
        if(selection == TargetSelection.RIGHT && angleOffsets.length > 0) return angleOffsets[angleOffsets.length-1];
        if(selection == TargetSelection.MIDDLE && angleOffsets.length > 0) return angleOffsets[angleOffsets.length/2];
        return 0;
    }

    public boolean getCargoMode(){
        return cargoMode.getBoolean(false);
    }

    public InstantCommand highExposure(){
        return new InstantCommand(() -> {
            exposure.setDouble(40);
        });
    } 

    public enum TargetSelection{
        LEFT,
        MIDDLE,
        RIGHT
    }
}

