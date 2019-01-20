package org.minutebots.lib.vision.targetConverters;

import org.minutebots.lib.vision.camera.CameraSettings;
import org.minutebots.lib.vision.targeting.Target;
import org.opencv.core.MatOfPoint;
import org.opencv.core.MatOfPoint2f;
import org.opencv.core.RotatedRect;
import org.opencv.imgproc.Imgproc;

import java.util.LinkedList;
import java.util.List;

/**
 * A class for converting contours into targets. Does not group contours.
 */
public class SingleTargetConverter implements ContourToTargetConverter {
    @Override
    public List<Target> convertContours(List<MatOfPoint> contours, CameraSettings cameraSettings) {
        List<Target> targets = new LinkedList<>();

        RectToTargetHelper rectToTargetHelper = new RectToTargetHelper(cameraSettings);

        for(MatOfPoint contour: contours){

            final RotatedRect boundary = Imgproc.minAreaRect(new MatOfPoint2f(contour.toArray()));

            Target target = rectToTargetHelper.convertRectToTarget(boundary);

            targets.add(target);
        }

        targets.sort((target, t1) -> Double.compare(t1.getPercentArea(), target.getPercentArea()));

        return targets;
    }
}
