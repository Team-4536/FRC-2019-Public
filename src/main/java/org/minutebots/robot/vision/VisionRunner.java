package org.minutebots.robot.vision;

import edu.wpi.cscore.UsbCamera;
import edu.wpi.first.cameraserver.CameraServer;
import edu.wpi.first.vision.VisionThread;
import org.minutebots.lib.vision.camera.CameraSettings;
import org.minutebots.lib.vision.camera.FOV;
import org.minutebots.lib.vision.camera.Resolution;
import org.minutebots.lib.vision.targetConverters.SingleTargetConverter;
import org.minutebots.lib.vision.targetConverters.TargetUtils;
import org.minutebots.lib.vision.targeting.Target;
import org.opencv.core.MatOfPoint;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class VisionRunner {

    private final Object imgLock = new Object();
    private VisionThread thread;
    private double angleError = 0.0;

    private VisionRunner(){
        UsbCamera camera = CameraServer.getInstance().startAutomaticCapture();
        camera.setResolution(640, 480);
        camera.setExposureManual(20);

        thread = new VisionThread(camera, new Pipeline(), pipeline -> {
            synchronized (imgLock) {
                angleError = (!pipeline.findContoursOutput().isEmpty()) ? (detect2019Targets(pipeline.findContoursOutput()).get(0).getHorizontalAngle()) : 0;
            }
        });
        thread.start();
    }

    public double getAngle(){
        synchronized (imgLock){
            return angleError;
        }
    }

    static List<Target> detect2019Targets(List<MatOfPoint> contours) {
        CameraSettings cameraSettings = new CameraSettings(false,
                new FOV(50, 40)
                , new Resolution(320, 180));

        List<Target> targets = new SingleTargetConverter().convertContours(contours, cameraSettings);

        // Sort the targets by x coordinates
        targets.sort(Comparator.comparingDouble(target -> target.getBoundary().center.x));

        List<Target> bays = new ArrayList<>();
        // If the current target is a left and the next is a right, make it a pair
        for (int i = 0; i < targets.size() - 1; i++) {
            Target current = targets.get(i);
            Target next = targets.get(i + 1);

            // Determine if the targets are a left and right pair
            if (current.getSkew() < 0 && next.getSkew() > 0) {
                // Combine the targets
                Target bay = TargetUtils.combineTargets(current, next, cameraSettings);
                bays.add(bay);
                // Skip the next target
                i++;
            }
        }
        //bays.sort(Comparator.comparingDouble(target -> Math.abs(target.getBoundary().center.x - cameraSettings.getResolution().getWidth() / 2.0)));
        return bays;
    }

    private static VisionRunner instance = new VisionRunner();

    public static VisionRunner getInstance(){return instance;}
}
