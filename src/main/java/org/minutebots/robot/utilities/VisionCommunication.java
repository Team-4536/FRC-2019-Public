package org.minutebots.robot.utilities;

import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.networktables.NetworkTableInstance;

import java.lang.annotation.Target;

public class VisionCommunication{
    private NetworkTableEntry angleOffsestEntry;
    private double[] angleOffsets;
    private TargetSelection selection = TargetSelection.MIDDLE;

    private VisionCommunication(){
        NetworkTable table = NetworkTableInstance.getDefault().getTable("Vision");
        angleOffsestEntry = table.getEntry("Target Angles");
        NetworkTableInstance.getDefault()
            .getEntry("/CameraPublisher/PiCamera/streams")
            .setStringArray(new String[]{"http://frcvision.local:1181/stream.mjpg"});
    //.setStringArray(new String[]{"mjpeg:http://" + "10.0.0.9" + ":" + "1181" + "/?action=stream"});
    }

    public void update(){
        angleOffsets = angleOffsestEntry.getDoubleArray(new double[]{0});
    }

    private static VisionCommunication instance = new VisionCommunication();

    public static VisionCommunication getInstance() {
        return instance;
    }

    public void setSelection(TargetSelection s){
        selection = s;
    }

    public double getAngle() {
        if(selection == TargetSelection.LEFT && angleOffsets.length > 0) return angleOffsets[0];
        if(selection == TargetSelection.RIGHT && angleOffsets.length > 0) return angleOffsets[angleOffsets.length-1];
        if(selection == TargetSelection.MIDDLE && angleOffsets.length > 0) return angleOffsets[angleOffsets.length/2];
        return 0;
    }

    public enum TargetSelection{
        LEFT,
        MIDDLE,
        RIGHT
    }
}

