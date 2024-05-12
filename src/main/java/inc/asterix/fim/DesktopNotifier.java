/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package inc.asterix.fim;

/**
 *
 * @author hp
 */
import java.awt.*;
import javax.swing.*;

public class DesktopNotifier {

    public void displayNotification(String title, String message) {
        // Veirfy if SystemTray is supported
        if (!SystemTray.isSupported()) {
            JOptionPane.showMessageDialog(null, "SystemTray n'est pas supporté", "Erreur", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // gets an instance of system tray
        SystemTray tray = SystemTray.getSystemTray();

        // Setting an icon for the Notifications
        Image image = Toolkit.getDefaultToolkit().createImage("C:\\Users\\mehdi\\OneDrive\\Documents\\NetBeansProjects\\UiDesign\\src\\main\\resources\\tray.png"); 

        // Creating a TrayIcon
        TrayIcon trayIcon = new TrayIcon(image, "FIM Notification");
        trayIcon.setImageAutoSize(true);

        try {
            tray.add(trayIcon);
        } catch (AWTException e) {
            JOptionPane.showMessageDialog(null, "Impossible to add the tray", "Erreur", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Showing the notification
        trayIcon.displayMessage(title, message, TrayIcon.MessageType.INFO);
    }
}
