/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package asterix.main;

import com.formdev.flatlaf.FlatLaf;
import com.formdev.flatlaf.extras.components.FlatMenu;
import com.formdev.flatlaf.fonts.roboto.FlatRobotoFont;
import com.formdev.flatlaf.themes.FlatMacDarkLaf;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Font;
import javax.swing.JFrame;
import javax.swing.UIManager;
import asterix.Login.Login;
import asterix.manager.FormsManager;
/**
 *
 * @author mehdi
 */
public class Application extends JFrame{
    public Application(){
        init();;
    }
    
    private void init(){
        setTitle("Login");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(new Dimension(1200, 700));
        setLocationRelativeTo(null);
        setContentPane(new Login());
        FormsManager.getInstance().initApplication(this);
    }
    
    public static  void  main(String[] ags) throws InterruptedException{
        FlatRobotoFont.install();
        FlatLaf.registerCustomDefaultsSource("raven.themes");
        UIManager.put("defaultFont",  new Font(FlatRobotoFont.FAMILY, Font.PLAIN, 13));
        FlatMacDarkLaf.setup();
        EventQueue.invokeLater(()-> new Application().setVisible(true));
    }
}
