package org.minutebots.robot.utilities;

import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.networktables.NetworkTableInstance;

public class VisionCommunication{
    private NetworkTableEntry angleOffsetEntry, targetSizeEntry;
    private double angleOffset = 0, targetSize = 0;

    private VisionCommunication(){
        NetworkTable table = NetworkTableInstance.getDefault().getTable("Vision");
        angleOffsetEntry = table.getEntry("Angle Offset");
        targetSizeEntry = table.getEntry("Target Size");
        angleOffsetEntry.setDouble(angleOffset);
        targetSizeEntry.setDouble(targetSize);
        NetworkTableInstance.getDefault()
            .getEntry("/CameraPublisher/PiCamera/streams")
            .setStringArray(new String[]{"http://frcvision.local:1181/stream.mjpg"});
    //.setStringArray(new String[]{"mjpeg:http://" + "10.0.0.9" + ":" + "1181" + "/?action=stream"});
    }

    public void update(){
        angleOffset = angleOffsetEntry.getDouble(0);
        targetSize = targetSizeEntry.getDouble(0);
    }

    private static VisionCommunication instance = new VisionCommunication();

    public static VisionCommunication getInstance() {
        return instance;
    }

    public double getAngle() {
        return angleOffset;
    }

    public double getSize() {
        return targetSize;
    }
}
