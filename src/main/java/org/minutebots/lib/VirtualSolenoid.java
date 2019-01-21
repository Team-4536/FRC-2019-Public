package org.minutebots.lib;

import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard;

public class VirtualSolenoid extends DoubleSolenoid {

    private int fc, rc;
    private Value kValue = Value.kOff;
    private NetworkTableEntry disp;

    /**
     * Constructor. Uses the default PCM ID (defaults to 0).
     *
     * @param forwardChannel The forward channel number on the PCM (0..7).
     * @param reverseChannel The reverse channel number on the PCM (0..7).
     */
    public VirtualSolenoid(int forwardChannel, int reverseChannel) {
        super(forwardChannel, reverseChannel);
        fc = forwardChannel;
        rc = reverseChannel;
        disp = Shuffleboard.getTab("Virtual Motors")
                .add("Virtual Solenoid " + fc + ", " + rc, false).getEntry();
    }

    /**
     * Set the value of a solenoid.
     *
     * @param value The value to set (Off, Forward, Reverse)
     */
    @Override
    public void set(Value value) {
        kValue = value;
        if (kValue == Value.kForward) {
            disp.setBoolean(true);
        } else disp.setBoolean(false);
    }

    @Override
    public synchronized void close() {
    }

    /**
     * Read the current value of the solenoid.
     *
     * @return The current value of the solenoid.
     */
    @Override
    public Value get() {
        return kValue;
    }

    //Virtual solenoids never short!

    @Override
    public boolean isFwdSolenoidBlackListed() {
        return false;
    }

    @Override
    public boolean isRevSolenoidBlackListed() {
        return false;
    }
}

