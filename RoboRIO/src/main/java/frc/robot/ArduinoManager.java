package frc.robot;
import edu.wpi.first.wpilibj.SerialPort;
import edu.wpi.first.wpilibj.SerialPort.Parity;
import edu.wpi.first.wpilibj.SerialPort.Port;
import edu.wpi.first.wpilibj.SerialPort.StopBits;

public class ArduinoManager {
    private static SerialPort arduino = new SerialPort(115200, Port.kMXP, 8, Parity.kOdd, StopBits.kOne);

    static void printData() {
        System.out.println(arduino.readString());
    }
}
