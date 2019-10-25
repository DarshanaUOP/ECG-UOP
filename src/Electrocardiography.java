import javax.swing.*;

/**
 * Created by Darshana Ariyarathna (acer) on 2019-10-17.
 */
public class Electrocardiography {
    public static void main(String[] args) {
        Values values = new Values();
        Settings settings = new Settings();

        GUI ui = new GUI();
        ui.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        ui.setTitle(values.getAppTitle());
        ui.setSize(settings.getAppWidth(), settings.getAppHeight());
        ui.setResizable(false);
        ui.setVisible(true);

    }
}
