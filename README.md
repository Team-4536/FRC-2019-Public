# Asimov - 2019
Repository for the code of our FIRST 2019:Deep Space Robot, Asimov

## Setup Instructions

### IntelliJ
- Clone this repo
- Run `./gradlew` to download gradle and needed FRC libraries
- Run `./gradlew tasks` to see available build options
- Run `./gradlew idea`
- Open the `Asimov.ipr` file with IntelliJ
- Grofit!

### VSCode
- Clone this repo
- Open project folder, containing build.gradle file in VSCode editor
- Grofit!

### Building/Deploying to the Robot
- Run `./gradlew build` to build the code. Use the `--info` flag for more details
- Run `./gradlew deploy` to deploy to the robot in Terminal (Mac) or Powershell (Windows)

## Code Highlights
- Building with Gradle

Instead of working with Ant, we used GradleRIO, which is a powerful Gradle plugin that allows us to build and deploy our code for FRC. It automatically fetches WPILib, CTRE Toolsuite, and other libraries, and is easier to use across different IDEs. 

- Hardware Abstraction

Uses an interface to abstact away all hardware-specific tasks. Edit the line in `Robot.java` to deploy to pre-configured robots or simulate. We can deploy it on many different robots and it will still work.  By doing this we can test the code on one of our old robots while Asimov is in the hands of the build team, or after bag day has occurred and we can’t access it. 
  
- Simulation Support

Run `./gradlew simulateJava` in order to run the robot code as a simulation. Open the driver station to enable autonomous or teleop and joystick input.
  
- Mecanum Drivetrain

Mecanum drive with PID-controlled rotation. Gyroscope assists robot in driving in a straight and predictable fashion. 
  
- Vision Processing Support
  
  Communicates with a Raspberry Pi through NetworkTables and is able to rotate or strafe to a vision target once given an angle.
  
- Two-stick drive system
  
  The driver stick controls robot movement as well as hatch and cargo mechanisms.
  
  The operator stick has finer control of rotation, as well as vision and cargo arm controls.
