package org.minutebots.robot.commands

import edu.wpi.first.wpilibj.command.Command

class CargoCollectionCommand : Command() {

    override fun initialize() {
        // this is where we would extend the arm and then start the wheels
    }

    override fun execute() {
        // this is what were we would do whatever we need to collect the cargo
    }

    override fun isFinished(): Boolean {
        return false
    }

    override fun end() {
        // this is where we would turn off the wheels and de-extend the arm
    }

    override fun interrupted() {}
}
