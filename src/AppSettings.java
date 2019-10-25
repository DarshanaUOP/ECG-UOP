import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowEvent;

/**
 * Created by Darshana Ariyarathna (acer) on 2019-10-19.
 */
public class AppSettings extends JFrame {


    private JPanel sttBackPan,sttHeaderPan,sttBodyPan;
    private JLabel sttLbHead;

    //Dimensions
    private JPanel sttDimensionPane;
    private JLabel sttDimName,sttDimH,sttDimW;
    private JTextField sttDimTH,sttDimTW;

    //Fonts
    private JPanel sttFontPan;
    private JSlider sttFontSlider;
    private JLabel sttFontLbl;

    //Save buttons
    private JPanel sttSavePan;
    private JLabel sttSaveOk;
    private JLabel sttSaveCancel;


    Settings settings = new Settings();
    GridBagConstraints gbc = new GridBagConstraints();


    public AppSettings() throws HeadlessException {
        sttBackPan = new JPanel(new BorderLayout(10,10));
        sttHeaderPan = new JPanel(new BorderLayout(5,5));
        sttBodyPan = new JPanel(new GridBagLayout());

        //header
        sttLbHead = new JLabel("Settings");
        sttLbHead.setFont(settings.menuFont);
        sttLbHead.setIcon(settings.getScaledImage("images/configs.png",25,25));

        //dimension
        sttDimH = new JLabel("Height");
        sttDimH.setFont(settings.getSubMenuFont());
        sttDimH.setBorder(new EmptyBorder(2,10,2,10));

        sttDimTH = new JTextField(String.valueOf(settings.getAppHeight()));
        sttDimTH.setFont(settings.getSubMenuFont());
        sttDimTH.setBorder(new EmptyBorder(2,10,2,10));
        sttDimTH.setColumns(5);

        sttDimW = new JLabel("Width");
        sttDimW.setFont(settings.getSubMenuFont());
        sttDimW.setBorder(new EmptyBorder(2,10,2,10));

        sttDimTW = new JTextField(String.valueOf(settings.getAppWidth()));
        sttDimTW.setFont(settings.getSubMenuFont());
        sttDimTW.setBorder(new EmptyBorder(2,10,2,10));
        sttDimTW.setColumns(5);

        sttDimensionPane = new JPanel(new GridBagLayout());
        sttDimensionPane.setBorder(new CompoundBorder(new TitledBorder("Dimensions"), new EmptyBorder(4, 4, 4, 4)));
        sttDimensionPane.setFont(settings.getSubMenuFont());

        gbc.weightx = 1 ;
        gbc.weighty = 1 ;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.ipadx = 5;
        gbc.ipady = 5;

        gbc.gridx = 0;
        gbc.gridy = 0;
        sttDimensionPane.add(sttDimH,gbc);

        gbc.gridy = 1;
        sttDimensionPane.add(sttDimW,gbc);

        gbc.gridx = 1;
        sttDimensionPane.add(sttDimTW,gbc);

        gbc.gridy = 0;
        sttDimensionPane.add(sttDimTW,gbc);

        sttDimensionPane.add(sttDimH);
        sttDimensionPane.add(sttDimTH);
        sttDimensionPane.add(sttDimW);
        sttDimensionPane.add(sttDimTW);

        gbc.gridx = 0;
        gbc.gridy = 0;
        sttBodyPan.add(sttDimensionPane,gbc);

        //Fonts
        sttFontPan = new JPanel(new GridBagLayout());
        sttFontPan.setBorder(new CompoundBorder(new TitledBorder("Fonts"), new EmptyBorder(4, 4, 4, 4)));

        sttFontSlider = new JSlider();
        sttFontSlider.setMinimum(16);
        sttFontSlider.setMaximum(22);
        sttFontSlider.setSnapToTicks(false);
        sttFontSlider.setFont(settings.getSubMenuFont());
        sttFontSlider.setPaintTicks(true);
        sttFontSlider.setValue(settings.getMenuFontSize());
        sttFontSlider.setForeground(settings.getTheamColor());
        sttFontSlider.setBorder(new EmptyBorder(2,10,2,10));
        sttFontSlider.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        sttFontSlider.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                sttFontLbl.setText(String.valueOf(sttFontSlider.getValue()));
                sttFontLbl.setFont(settings.getMenuFont());
                settings.setMenuFontSize(sttFontSlider.getValue());
            }
        });
        gbc.gridx = 0;
        gbc.gridy = 0;
        sttFontPan.add(sttFontSlider,gbc);

        sttFontLbl = new JLabel(String.valueOf(sttFontSlider.getValue()));
        sttFontLbl.setFont(settings.getMenuFont());
        sttFontLbl.setBorder(new EmptyBorder(2,10,2,10));
        gbc.gridx = 1;
        gbc.gridy = 0;
        sttFontPan.add(sttFontLbl,gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        sttBodyPan.add(sttFontPan,gbc);

        //Save
        sttSavePan = new JPanel();
        sttSavePan.setBorder(new CompoundBorder(new TitledBorder("Save"), new EmptyBorder(4, 4, 4, 4)));


        sttSaveOk = new JLabel("Save");
        sttSaveOk.setBorder(new EmptyBorder(2,10,2,10));
        sttSaveOk.setFont(settings.getSubMenuFont());
        sttSaveOk.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        gbc.gridx = 0;
        gbc.gridy = 0;
        sttSavePan.add(sttSaveOk,gbc);

        sttSaveCancel = new JLabel("Cancel");
        sttSaveCancel.setBorder(new EmptyBorder(2,10,2,10));
        sttSaveCancel.setFont(settings.getSubMenuFont());
        sttSaveCancel.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        sttSaveCancel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                AppSettings.this.dispatchEvent(new WindowEvent(AppSettings.this,WindowEvent.WINDOW_CLOSING));
            }
        });
        gbc.gridx = 1;
        gbc.gridy = 0;
        sttSavePan.add(sttSaveCancel,gbc);


        gbc.gridx = 0;
        gbc.gridy = 2;
        sttBodyPan.add(sttSavePan,gbc);



        //add panes
        sttHeaderPan.setBorder(new EmptyBorder(10,150,10,150));
        sttHeaderPan.add(sttLbHead,BorderLayout.CENTER);

        sttBackPan.setBorder(new EmptyBorder(10,10,10,10));
        sttBackPan.add(sttHeaderPan,BorderLayout.NORTH);
        sttBackPan.add(sttBodyPan,BorderLayout.SOUTH);
        add(sttBackPan);


    }
}
