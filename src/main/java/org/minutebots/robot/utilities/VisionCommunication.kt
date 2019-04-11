package org.minutebots.robot.utilities

import org.minutebots.robot.OI

import edu.wpi.first.networktables.EntryListenerFlags
import edu.wpi.first.networktables.NetworkTable
import edu.wpi.first.networktables.NetworkTableEntry
import edu.wpi.first.networktables.NetworkTableInstance
import edu.wpi.first.wpilibj.command.InstantCommand
import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser

class VisionCommunication
//private TargetSelection selection = TargetSelection.MIDDLE;

private constructor() {
    internal var table = NetworkTableInstance.getDefault().getTable("Vision")
    private val angles = table.getEntry("Target Angles")
    private val exposure = table.getEntry("Exposure")
    private val switchCamera = table.getEntry("BackCamera")
    //brightness = table.getEntry("Brightness");
    private val visionMode = SendableChooser<VisionCommunication.TargetSelection>()

    var cargoMode = Shuffleboard.getTab("Drive")
            .add("Cargo Mode", false)
            .withWidget("Toggle Button")
            .entry

    private var angleOffsets: DoubleArray? = null

    /*
    public InstantCommand setSelection(TargetSelection s){
        return new InstantCommand("Target " + s.name(),() -> selection = s);
    }
    */

    val angle: Double
        get() {
            if (OI.visionOveride.get()) return 0.0
            exposure.setDouble(0.0)
            angleOffsets = angles.getDoubleArray(doubleArrayOf(0.0))
            val selection = visionMode.selected
            if (selection == TargetSelection.LEFT && angleOffsets!!.size > 0) return angleOffsets!![0]
            if (selection == TargetSelection.RIGHT && angleOffsets!!.size > 0) return angleOffsets!![angleOffsets!!.size - 1]
            return if (selection == TargetSelection.MIDDLE && angleOffsets!!.size > 0) angleOffsets!![angleOffsets!!.size / 2] else 0.0
        }

    init {
        NetworkTableInstance.getDefault()
                .getEntry("/CameraPublisher/PiCamera/streams")
                .setStringArray(arrayOf("http://frcvision.local:1181/stream.mjpg"))

        cargoMode.addListener({ event ->
            switchCamera.setBoolean(event.value.boolean)
            println("Back Camera Activated: " + event.value.boolean)
        }, EntryListenerFlags.kNew or EntryListenerFlags.kUpdate)

        table.getEntry("Connected").setBoolean(true)

        visionMode.name = "Vision Mode"
        visionMode.addOption("Left", TargetSelection.LEFT)
        visionMode.addOption("Right", TargetSelection.RIGHT)
        visionMode.setDefaultOption("Middle", TargetSelection.MIDDLE)

        Shuffleboard.getTab("Drive").add(visionMode)
        //.setStringArray(new String[]{"mjpeg:http://" + "10.0.0.9" + ":" + "1181" + "/?action=stream"});
    }

    fun update() {

    }

    fun highExposure(): InstantCommand {
        return InstantCommand { exposure.setDouble(40.0) }
    }

    enum class TargetSelection {
        LEFT,
        MIDDLE,
        RIGHT
    }

    companion object {

        val instance = VisionCommunication()
    }
}

