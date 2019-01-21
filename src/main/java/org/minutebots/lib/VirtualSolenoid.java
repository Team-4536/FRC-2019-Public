package org.minutebots.lib;

import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.smartdashboard.SendableBuilder;

public class VirtualSolenoid extends DoubleSolenoid {

    private int fc,rc;
    private Value kValue = Value.kOff;

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
    }

    /**
     * Set the value of a solenoid.
     *
     * @param value The value to set (Off, Forward, Reverse)
     */
    @Override
    public void set(Value value) {
        switch (value){
            case kForward:
                System.out.println("Solenoid " + fc + ", " + rc + " extended");
                break;
            case kOff:
                System.out.println("Solenoid " + fc + ", " + rc + " turned off.");
                break;
            case kReverse:
                System.out.println("Solenoid " + fc + ", " + rc + " retracted.");
                break;
        }
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

    @Override
    public void initSendable(SendableBuilder builder) {
        super.initSendable(builder);
    }
}
