
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

import com.formdev.flatlaf.FlatLightLaf;

import Gui.VentanaInicio;


// Reemplazo de App.java que era de consola
public class AppGUI {
    public static void main(String[] args) {
        FlatLightLaf.setup();       
        
        SwingUtilities.invokeLater(() -> new VentanaInicio().setVisible(true));
    }
}
