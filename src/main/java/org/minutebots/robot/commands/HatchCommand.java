package org.minutebots.robot.commands;

import edu.wpi.first.wpilibj.command.Command;

public class HatchCommand extends Command {

    boolean hasPanel;

    public HatchCommand(boolean hatchPanel) {

        hasPanel = hatchPanel;

    }

    protected void execute() {

        if(hasPanel) {

            // this is the method for realising the panel

        }
        else {

            // this is the method for getting the panel

        }

    }

        protected boolean isFinished() {

            return true;

    }

}
