/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package asterix.Login;

import com.formdev.flatlaf.FlatClientProperties;
import inc.asterix.fim.Dbconnection;
import inc.asterix.fim.SessionManager;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import net.miginfocom.swing.MigLayout;
import asterix.manager.FormsManager;

/**
 *
 * @author mehdi
 */
public class SecretKey extends JPanel{
    
    private Dbconnection dbconnection; 
    
    public SecretKey(){
        this.dbconnection = new Dbconnection();
        init();
    }
    
    // Methode that will generate secret key
    private String generateAndSaveSecretCode() {
        List<String> chars = new ArrayList<>();
        chars.add("G");
        chars.add("4");
        chars.add("c");
        chars.add("&");
        chars.add("2");
        chars.add("5");
        chars.add("m");
        chars.add("c");
        chars.add("9");
        chars.add("J");
        chars.add("0");
        chars.add("9");
        chars.add("7");
        chars.add("Y");
        chars.add("9");
        chars.add("4");
        chars.add("c");
        chars.add("&");
        chars.add("2");
        chars.add("5");
        chars.add("m");
        chars.add("c");
        chars.add("9");
        chars.add("J");
        chars.add("0");

        

    // Shuffle the list to randomize the order
    Collections.shuffle(chars);

    // Take 20 characters to form the secret code
    List<String> selectedchars = chars.subList(0, 20);

    // Concatenate the selected fruit names
        String Code = String.join("", selectedchars);
         txtSecrekey.setText(Code.toString());
        return Code;
        
    }
   
    
    public void init(){
        setLayout(new MigLayout("fill,insets 20", "[center]", "[center]"));
        txtSecrekey = new JTextField();
        cmdgenrate =  new JButton("Generate Key");
        cmdSavekey =  new JButton("Save Key");
        cmdSavekey.addActionListener(e ->{
        String useremail = SessionManager.getCurrentUserEmail();
        String Code = txtSecrekey.getText();

        if (Code.equals("")) {
        JOptionPane.showMessageDialog(null, "All fields are required!", "Error", JOptionPane.ERROR_MESSAGE);
        } else {
        int confirmation = JOptionPane.showConfirmDialog(null, "Are you sure you want to proceed?", "Warning", JOptionPane.YES_NO_OPTION);
        if (confirmation == JOptionPane.YES_OPTION) {
            try {
                dbconnection.getsecretkey(Code, useremail);
                FormsManager.getInstance().showForm(new Login());
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(null, "Erreur while connecting to the database : " + ex.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
                ex.printStackTrace();
            }
        } else {
            // User chose not to proceed, handle it here if needed
        }
        }
        });
        cmdgenrate.addActionListener(e -> {
        String Code = generateAndSaveSecretCode();
        });
        JPanel panel = new JPanel(new MigLayout("wrap,fillx,insets 35 45 30 45", "[fill,360]"));
        panel.putClientProperty(FlatClientProperties.STYLE, "" +
                "arc:20;" +
                "[light]background:darken(@background,3%);" +
                "[dark]background:lighten(@background,3%)");
        
        txtSecrekey.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, "Enter Secret key");
        cmdSavekey.putClientProperty(FlatClientProperties.STYLE, "" +
                "[light]background:darken(@background,10%);" +
                "[dark]background:lighten(@background,10%);" +
                "borderWidth:0;" +
                "focusWidth:0;" +
                "innerFocusWidth:0");
         cmdgenrate.putClientProperty(FlatClientProperties.STYLE, "" +
                "[light]background:darken(@background,10%);" +
                "[dark]background:lighten(@background,10%);" +
                "borderWidth:0;" +
                "focusWidth:0;" +
                "innerFocusWidth:0");
        
        JLabel lbTitle = new JLabel("Step two authentificarion secret key!");
        lbTitle.putClientProperty(FlatClientProperties.STYLE, "" +
                "font:bold +10");
        
        panel.add(lbTitle);
        panel.add(txtSecrekey);
        panel.add(cmdSavekey, "gapy 20");
        panel.add(cmdgenrate, "gapy 20");
        add(panel);
    }
    
    
    private JTextField txtSecrekey;
    private JButton cmdgenrate;
    private JButton cmdSavekey;
    
}
