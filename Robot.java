/*----------------------------------------------------------------------------*/
/* Copyright (c) 2017-2018 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot;

import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.SpeedControllerGroup;
import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj.GenericHID.Hand;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

import edu.wpi.first.wpilibj.drive.DifferentialDrive;

import com.ctre.phoenix.motorcontrol.can.WPI_TalonFX;
import com.ctre.phoenix.motorcontrol.can.WPI_VictorSPX;


public class Robot extends TimedRobot {
  private static final String kDefaultAuto = "Default";
  private static final String kCustomAuto = "My Auto";
  private String m_autoSelected;
  private final SendableChooser<String> m_chooser = new SendableChooser<>();

  //DRIVE TRAIN DEFINITIONS

  private final WPI_VictorSPX leftMotor1 = new WPI_VictorSPX(RobotMap.m_leftMotor1);
  private final WPI_VictorSPX leftMotor2 = new WPI_VictorSPX(RobotMap.m_leftMotor2);
  private final WPI_VictorSPX rightMotor1 = new WPI_VictorSPX(RobotMap.m_rightMotor1);
  private final WPI_VictorSPX rightMotor2 = new WPI_VictorSPX(RobotMap.m_rightMotor2);

  private final SpeedControllerGroup m_leftMotors = new SpeedControllerGroup(leftMotor1, leftMotor2);
  private final SpeedControllerGroup m_rightMotors = new SpeedControllerGroup(rightMotor1, rightMotor2);

  private final DifferentialDrive m_driveTrain = new DifferentialDrive(m_leftMotors, m_rightMotors);

  private final Encoder m_leftEncoder = new Encoder(RobotMap.m_leftEnc1,RobotMap.m_leftEnc2);
  private final Encoder m_rightEncoder = new Encoder(RobotMap.m_rightEnc1,RobotMap.m_rightEnc2);

  //SHOOTER DEFINITIONS
  private final WPI_TalonFX firstMotor = new WPI_TalonFX(RobotMap.m_leftShooter);
  private final WPI_TalonFX secondMotor = new WPI_TalonFX(RobotMap.m_rightShooter);

  private final DifferentialDrive m_shooter = new DifferentialDrive(firstMotor, secondMotor);

  //CONTROLLER DEFINITIONS

  private final XboxController m_driverController = new XboxController(RobotMap.DRIVER_CONTROLLER);
  private final XboxController m_operatorController = new XboxController(RobotMap.OPERATOR_CONTROLLER);
  
  //GAME TIMER
  
  private final Timer m_timer = new Timer();

  /**
   * This function is run when the robot is first started up and should be
   * used for any initialization code.
   */
  @Override
  public void robotInit() {
    m_chooser.setDefaultOption("Default Auto", kDefaultAuto);
    m_chooser.addOption("My Auto", kCustomAuto);
    SmartDashboard.putData("Auto choices", m_chooser);
    SmartDashboard.putData(leftMotor1);
    SmartDashboard.putData(leftMotor2);
    SmartDashboard.putData(rightMotor1);
    SmartDashboard.putData(rightMotor2);
    SmartDashboard.putData(firstMotor);
    SmartDashboard.putData(secondMotor);
    



  }

  
  @Override
  public void robotPeriodic() {
  }

  
  @Override
  public void autonomousInit() {
    m_autoSelected = m_chooser.getSelected();
    // m_autoSelected = SmartDashboard.getString("Auto Selector", kDefaultAuto);
    System.out.println("Auto selected: " + m_autoSelected);

    //Resets the timer at the beginning of autonomous.
    m_timer.reset();

    //Starts the timer at the beginning of autonomous.
    m_timer.start();

  }

  /**
   * This function is called periodically during autonomous.
   */
  @Override
  public void autonomousPeriodic() {
    
    //Switch is used to switch the autonomous code to whatever was chosen
    //  on your dashboard.
    //Make sure to add the options under m_chooser in robotInit.
    switch (m_autoSelected) {
      case kCustomAuto:
        // Put custom auto code here
        break;
      case kDefaultAuto:
      default:
        if (m_timer.get() < 2.0){
          m_driveTrain.arcadeDrive(0.5, 0); //drive forward at half-speed
        } else {
          m_driveTrain.stopMotor(); //stops motors once 2 seconds have elapsed
        }
        break;
    }
  }

  /**
   * This function is called periodically during operator control.
   */
  @Override
  public void teleopPeriodic() {
  double triggerVal = 
    (m_driverController.getTriggerAxis(Hand.kRight)
    - m_driverController.getTriggerAxis(Hand.kLeft))
    * RobotMap.DRIVING_SPEED;

  double stick = 
    (m_driverController.getX(Hand.kLeft))
    * RobotMap.TURNING_RATE;
    
    m_driveTrain.tankDrive(triggerVal + stick, triggerVal - stick);

    if(m_driverController.getAButton()){
      m_shooter.arcadeDrive(RobotMap.SHOOTER_SPEED, 0.0);
    }
    
    else{
      m_shooter.arcadeDrive(0.0, 0.0);
    }
    
  }

  /**
   * This function is called periodically during test mode.
   */
  @Override
  public void testPeriodic() {
  }
}