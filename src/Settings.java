import javax.swing.*;
import java.awt.*;

/**
 * Created by Darshana Ariyarathna (acer) on 2019-10-17.
 */
public class Settings {

    /**
     * path to save settings JSON file
     * System.out.println(System.getProperty("user.dir"));
     */
    int appHeight = 900 ;
    int appWidth =  1500;
    int menuFontSize = 18;

    Font headerFont = new Font(null,Font.BOLD,50);
    Font footerFont = new Font(null,Font.PLAIN,20);
    Font menuFont = new Font(null,Font.PLAIN, menuFontSize);
    Font subMenuFont = new Font(null,Font.PLAIN,16);
    Color theamColor = Color.black;

    public int getMenuFontSize() {
        return menuFontSize;
    }

    public void setMenuFontSize(int menuFontSize) {
        this.menuFontSize = menuFontSize;
    }

    public Color getTheamColor() {
        return theamColor;
    }

    public Font getSubMenuFont() {
        return subMenuFont;
    }

    public Font getMenuFont(){
        return menuFont;
    }

    public Font getFooterFont() {
        return footerFont;
    }

    public int getAppHeight() {
        return appHeight;
    }

    public int getAppWidth() {
        return appWidth;
    }

    public Font getHeaderFont() {
        return headerFont;
    }

    public ImageIcon getScaledImage(String fileName, int w, int h){
        ImageIcon imageIcon = new ImageIcon(getClass().getResource(fileName)); // load the image to a imageIcon
        Image image = imageIcon.getImage(); // transform it
        Image newimg = image.getScaledInstance(w, h,  java.awt.Image.SCALE_SMOOTH); // scale it the smooth way
        return new ImageIcon(newimg);  // transform it back
    }
}
