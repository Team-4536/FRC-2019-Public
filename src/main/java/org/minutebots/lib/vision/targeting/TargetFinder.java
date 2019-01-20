package org.minutebots.lib.vision.targeting;

import org.minutebots.lib.vision.camera.CameraSettings;
import org.minutebots.lib.vision.contourFilters.ContourFilter;
import org.minutebots.lib.vision.contourFinders.ContourFinder;
import org.minutebots.lib.vision.contourFinders.StandardContourFinder;
import org.minutebots.lib.vision.filters.TargetFilter;
import org.minutebots.lib.vision.targetConverters.*;
import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.MatOfPoint;

import java.util.List;

/**
 * A class to find targets.
 */
public class TargetFinder {

    private CameraSettings cameraSettings;

    private TargetFilter thresholdFilter;

    private ContourFilter contourFilter;

    private ContourFinder contourFinder;

    private ContourToTargetConverter contourToTargetConverter;


    /**
     * Creates a target finder.
     * @param cameraSettings The settings of the camera.
     * @param thresholdFilter The target filter.
     * @param contourFilter The contour filter which is applied to the target filtered image.
     * @param grouping The way targets will be grouped.
     */
    public TargetFinder(CameraSettings cameraSettings, TargetFilter thresholdFilter, ContourFilter contourFilter, TargetGrouping grouping) {
        this.cameraSettings = cameraSettings;
        this.thresholdFilter = thresholdFilter;
        this.contourFilter = contourFilter;

        contourFinder = new StandardContourFinder();

        switch (grouping) {
            case SINGLE:
                contourToTargetConverter = new SingleTargetConverter();
                break;
            case DOUBLE:
                contourToTargetConverter = new DoubleTargetConverter();
                break;
            case TRIPLE:
                contourToTargetConverter = new TripleTargetConverter();
                break;
            default:
                contourToTargetConverter = new SingleTargetConverter();
        }
    }

    /**
     * Find targets within an image.
     * @param image The image to find targets in.
     * @return The targets.
     */
    public List<Target> findTargets(Mat image) {
        if(cameraSettings.isInverted()){
            Core.flip(image, image, -1);
        }
        Mat filtered = thresholdFilter.filter(image);
        List<MatOfPoint> contours = contourFinder.findContours(filtered);
        filtered.release();
        contours = contourFilter.filterContours(contours);
        return contourToTargetConverter.convertContours(contours, cameraSettings);
    }

}
