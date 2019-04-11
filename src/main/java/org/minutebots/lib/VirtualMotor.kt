package org.minutebots.lib

import edu.wpi.first.wpilibj.Sendable
import edu.wpi.first.wpilibj.SpeedController
import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard
import edu.wpi.first.wpilibj.smartdashboard.SendableBuilder

class VirtualMotor(private var name: String?, private val port: Int) : SpeedController, Sendable {
    private var speed = 0.0
    private var inverted = false
    private var subsystem = ""

    init {
        println("Virtual motor $name at port $port created")

        Shuffleboard.getTab("Virtual Motors")
                .add((if (this.name!!.isEmpty()) "Virtual Motor" else this.name) + " " + this.port, this)
    }

    constructor(port: Int) : this("", port) {}

    override fun set(speed: Double) {
        this.speed = speed
    }

    override fun get(): Double {
        return speed
    }

    override fun setInverted(isInverted: Boolean) {
        inverted = isInverted
    }

    override fun getInverted(): Boolean {
        return inverted
    }

    override fun disable() {
        this.speed = 0.0
    }

    override fun stopMotor() {
        this.speed = 0.0
    }

    override fun pidWrite(output: Double) {
        this.speed = output
    }

    /**
     * Gets the name of this [Sendable] object.
     *
     * @return Name
     */
    override fun getName(): String? {
        return name
    }

    /**
     * Sets the name of this [Sendable] object.
     *
     * @param name name
     */
    override fun setName(name: String) {
        this.name = name
    }

    /**
     * Sets both the subsystem name and device name of this [Sendable] object.
     *
     * @param subsystem subsystem name
     * @param name      device name
     */
    override fun setName(subsystem: String, name: String) {
        this.subsystem = subsystem
        this.name = name
    }

    /**
     * Gets the subsystem name of this [Sendable] object.
     *
     * @return Subsystem name
     */
    override fun getSubsystem(): String {
        return subsystem
    }

    /**
     * Sets the subsystem name of this [Sendable] object.
     *
     * @param subsystem subsystem name
     */
    override fun setSubsystem(subsystem: String) {
        this.subsystem = subsystem
    }

    /**
     * Initializes this [Sendable] object.
     *
     * @param builder sendable builder
     */
    override fun initSendable(builder: SendableBuilder) {
        builder.setSmartDashboardType("Speed Controller")
        builder.setActuator(true)
        builder.setSafeState { this.disable() }
        builder.addDoubleProperty("Value", DoubleSupplier { this.get() }, DoubleConsumer { this.set(it) })
    }
}
