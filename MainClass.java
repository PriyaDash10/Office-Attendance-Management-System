package attendancesystem;

import java.awt.EventQueue;
import javax.swing.UIManager;

// ✅ Import FlatLaf
import com.formdev.flatlaf.FlatLightLaf;
// (Optional) for dark mode:
// import com.formdev.flatlaf.FlatDarkLaf;

public class MainClass {

    public static void main(String[] args) {

        EventQueue.invokeLater(() -> {
            try {
                // 🔥 APPLY MODERN UI
                UIManager.setLookAndFeel(new FlatLightLaf());

                // 👉 If you want DARK mode, use this instead:
                // UIManager.setLookAndFeel(new FlatDarkLaf());

                // Open Login Page
                Loginpage frame = new Loginpage();
                frame.setVisible(true);

            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }
}