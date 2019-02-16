package org.minutebots.lib;

import edu.wpi.first.wpilibj.Sendable;
import edu.wpi.first.wpilibj.SpeedController;
import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard;
import edu.wpi.first.wpilibj.smartdashboard.SendableBuilder;

public class VirtualMotor implements SpeedController, Sendable {

    private int port;
    private double speed = 0;
    private boolean inverted = false;
    private String name, subsystem = "";

    public VirtualMotor(String name, int port){
        System.out.println("Virtual motor " + name + " at port " + port + " created");
        this.name = name;
        this.port = port;

        Shuffleboard.getTab("Virtual Motors")
                .add(((this.name.isEmpty()) ? "Virtual Motor" : this.name) + " " + this.port, this);
    }

    public VirtualMotor(int port){
        this("", port);
    }

    @Override
    public void set(double speed) {
        this.speed = speed;
    }

    @Override
    public double get() {
        return speed;
    }

    @Override
    public void setInverted(boolean isInverted) {
        inverted = isInverted;
    }

    @Override
    public boolean getInverted() {
        return inverted;
    }

    @Override
    public void disable() {
        this.speed = 0;
    }

    @Override
    public void stopMotor() {
        this.speed = 0;
    }

    @Override
    public void pidWrite(double output) {
        this.speed = output;
    }

    /**
     * Gets the name of this {@link Sendable} object.
     *
     * @return Name
     */
    @Override
    public String getName() {
        return name;
    }

    /**
     * Sets the name of this {@link Sendable} object.
     *
     * @param name name
     */
    @Override
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Sets both the subsystem name and device name of this {@link Sendable} object.
     *
     * @param subsystem subsystem name
     * @param name      device name
     */
    @Override
    public void setName(String subsystem, String name) {
        this.subsystem = subsystem;
        this.name = name;
    }

    /**
     * Gets the subsystem name of this {@link Sendable} object.
     *
     * @return Subsystem name
     */
    @Override
    public String getSubsystem() {
        return subsystem;
    }

    /**
     * Sets the subsystem name of this {@link Sendable} object.
     *
     * @param subsystem subsystem name
     */
    @Override
    public void setSubsystem(String subsystem) {
        this.subsystem = subsystem;
    }

    /**
     * Initializes this {@link Sendable} object.
     *
     * @param builder sendable builder
     */
    @Override
    public void initSendable(SendableBuilder builder) {
        builder.setSmartDashboardType("Speed Controller");
        builder.setActuator(true);
        builder.setSafeState(this::disable);
        builder.addDoubleProperty("Value", this::get, this::set);
    }
}
