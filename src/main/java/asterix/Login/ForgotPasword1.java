/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package asterix.Login;

import com.formdev.flatlaf.FlatClientProperties;
import inc.asterix.fim.Dbconnection;
import inc.asterix.fim.SessionManager;
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
public class ForgotPasword1 extends JPanel{
    
    private Dbconnection dbconnection; // db class
    
    public ForgotPasword1(){
        this.dbconnection = new Dbconnection(); // db initialasation
        init();
    }
    
    public void init(){
        setLayout(new MigLayout("fill,insets 20", "[center]", "[center]"));
        txtsecretkey = new JTextField();
        cmdVerify =  new JButton("Verify");
        cmdVerify.addActionListener(e -> {
        String secretcode = txtsecretkey.getText();
        String useremail = SessionManager.getCurrentUserEmail();
        
        // Check if all the text fields are not empty
        if(secretcode.equals("")) {
            
            JOptionPane.showMessageDialog(null, "All fields are required"); 
        }
        else
        {
        try{
            // preforming action
            if(dbconnection.checkkey(useremail, secretcode)){
                SessionManager.loginUser(useremail);
                FormsManager.getInstance().showForm(new ForgotPassword2());
            }else{
                
            }
           
        }catch (Exception ex) {
            JOptionPane.showMessageDialog(null, "Erreur lors de la connexion à la base de données : " + ex.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }
        }
            
        });
        JPanel panel = new JPanel(new MigLayout("wrap,fillx,insets 35 45 30 45", "[fill,360]"));
        panel.putClientProperty(FlatClientProperties.STYLE, "" +
                "arc:20;" +
                "[light]background:darken(@background,3%);" +
                "[dark]background:lighten(@background,3%)");
        
        txtsecretkey.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, "Enter Secret key to verify your identity");
        cmdVerify.putClientProperty(FlatClientProperties.STYLE, "" +
                "[light]background:darken(@background,10%);" +
                "[dark]background:lighten(@background,10%);" +
                "borderWidth:0;" +
                "focusWidth:0;" +
                "innerFocusWidth:0");
        
        JLabel lbTitle = new JLabel("Identity Verification");
        lbTitle.putClientProperty(FlatClientProperties.STYLE, "" +
                "font:bold +10");
        
        panel.add(lbTitle);
        panel.add(txtsecretkey);
        panel.add(cmdVerify, "gapy 20");
        add(panel);
    }
     private JTextField txtsecretkey;
     private JButton cmdVerify;
    
}
