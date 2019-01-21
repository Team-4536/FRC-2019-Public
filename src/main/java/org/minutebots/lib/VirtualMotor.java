package org.minutebots.lib;

import edu.wpi.first.wpilibj.SpeedController;

public class VirtualMotor implements SpeedController{

    private int port;
    private double speed = 0;
    private boolean inverted = false;

    public VirtualMotor(int port){
        this.port = port;
    }

    @Override
    public void set(double speed) {
        this.speed = speed;
        System.out.println("Virtual motor " + port + " set at " + ((inverted) ? this.speed : -this.speed));
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
        return false;
    }

    @Override
    public void disable() {

    }

    @Override
    public void stopMotor() {

    }

    @Override
    public void pidWrite(double output) {

    }
}