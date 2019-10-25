import com.fazecast.jSerialComm.SerialPort;
import javafx.scene.shape.Shape;
import jdk.nashorn.internal.runtime.Property;
import jdk.nashorn.internal.runtime.regexp.joni.Warnings;
import org.jetbrains.annotations.NotNull;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Scanner;

import static javax.swing.JFileChooser.DIRECTORIES_ONLY;

/**
 * Created by Darshana Ariyarathna (acer) on 2019-10-17.
 */
public class GUI extends JFrame {

    static SerialPort choosenPort;

    private JPanel backPan;
    //header components
    private JPanel headerPan, namePan, menuPan, headerSouth, showConfigPan, showCollectPan;
    private JLabel lbName, lbMenuConfig, lbMenuCollect, lbMenuSettings;
    private JLabel lbConfigName, lbCollectName;                 //components of showConfigPan
    private JComboBox combCOMPort;                             //config components
    private JLabel lbCCTCheck, lbCCTRefresh, lbCCTConn;          //config components
    private JLabel lbFILESelect, lbFILESelectButton;

    //body Components
    private JPanel bodyPan, graphPan, controllerPan;
    private JLabel lbGRAPHRun, lbGRAPHClear;

    //footer components
    private JPanel footerPan;
    private JLabel lbFooter;

    //thread components
    private boolean suspendFlg = false;
    int t = 0;

    @NotNull GridBagConstraints gb = new GridBagConstraints();

    @NotNull Settings settings = new Settings();
    @NotNull Values values = new Values();

    public GUI() {
        backPan = new JPanel(new BorderLayout(10, 10));
        backPan.setBorder(new EmptyBorder(10, 10, 10, 10));
        add(backPan);

        //header
        headerPan = new JPanel(new BorderLayout());
        namePan = new JPanel(new FlowLayout());
        menuPan = new JPanel(new FlowLayout());

        //creating name pan
        lbName = new JLabel(values.getAppName());
        lbName.setIcon(settings.getScaledImage("images/heart.png", 100, 100));
        lbName.setFont(settings.getHeaderFont());
        namePan.add(lbName);

        //creating show panes
        headerSouth = new JPanel(new BorderLayout());
        showCollectPan = new JPanel(new FlowLayout());
        showConfigPan = new JPanel(new FlowLayout());
        lbConfigName = new JLabel("Configurations ");
        lbCollectName = new JLabel("Collect Data");

        lbCCTCheck = new JLabel("Find Your Circuit");
        combCOMPort = new JComboBox();

        lbCCTCheck.setBorder(BorderFactory.createEmptyBorder(2, 50, 2, 2));

        SerialPort[] portNames = SerialPort.getCommPorts(); //get comports when the app loading
        for (int i = 0; i < portNames.length; i++)
            combCOMPort.addItem(portNames[i].getSystemPortName() + "-" + portNames[i].getDescriptivePortName());

        lbCCTRefresh = new JLabel("Refresh");
        lbCCTRefresh.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        lbCCTRefresh.setBorder(BorderFactory.createEmptyBorder(2, 5, 2, 5));
        lbCCTRefresh.setIcon(settings.getScaledImage("images/refresh2.png",16,16));

        lbCCTConn = new JLabel("Connect");
        lbCCTConn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        lbCCTConn.setBorder(BorderFactory.createEmptyBorder(2, 5, 2, 5));
        lbCCTConn.setIcon(settings.getScaledImage("images/connt.png",16,16));

        if (portNames.length > 0) {
            lbCCTConn.setEnabled(true);
        } else {
            lbCCTConn.setEnabled(false);
        }

        lbCCTCheck.setFont(settings.getSubMenuFont());
        combCOMPort.setFont(settings.getSubMenuFont());
        lbCCTRefresh.setFont(settings.getSubMenuFont());
        lbCCTConn.setFont(settings.getSubMenuFont());

        //configurations panel
        lbConfigName.setIcon(settings.getScaledImage("images/dash.png", 30, 30));
        lbConfigName.setFont(settings.getSubMenuFont());
        showConfigPan.add(lbConfigName);
        showConfigPan.add(lbCCTCheck);
        showConfigPan.add(combCOMPort);
        showConfigPan.add(lbCCTRefresh);
        showConfigPan.add(lbCCTConn);

        lbCCTRefresh.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) { //get comports if first time isn't not detected.
                combCOMPort.removeAllItems();
                SerialPort[] portNames = SerialPort.getCommPorts();
                for (int i = 0; i < portNames.length; i++)
                    combCOMPort.addItem(portNames[i].getSystemPortName() + "-" + portNames[i].getDescriptivePortName());

                if (portNames.length > 0) {
                    lbCCTConn.setEnabled(true);
                } else {
                    lbCCTConn.setEnabled(false);
                }
            }
        });
        lbCCTConn.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (lbCCTConn.getText().equals("Connect")) {
                    lbCCTConn.setText("Connecting...");
                    String port = combCOMPort.getSelectedItem().toString();

                    try {
                        choosenPort = SerialPort.getCommPort(port.split("-")[0]);
                        choosenPort.setComPortTimeouts(SerialPort.TIMEOUT_SCANNER, 0, 0);
                    } catch (Exception e1) {
                        JOptionPane.showMessageDialog(null, "Error in connect to Device \n" + e1.getMessage(), "Communication Error", JOptionPane.PLAIN_MESSAGE);
                        lbCCTConn.setText("Connect");
                    }
                    if (choosenPort.openPort()) {
                        lbCCTConn.setText("Disconnect");
                        lbGRAPHClear.setEnabled(true);
                        lbGRAPHRun.setEnabled(true);
                    } else {
                        lbCCTConn.setText("Connect");
                    }

                } else if (lbCCTConn.getText().equals("Disconnect")) {
                    if (choosenPort.closePort()) {
                        lbCCTConn.setText("Connect");
                        lbGRAPHRun.setEnabled(false);
                        lbGRAPHClear.setEnabled(false);
                    }
                }
            }
        });

        //collect data panel

        lbCollectName.setIcon(settings.getScaledImage("images/collect.png", 30, 30));
        lbCollectName.setFont(settings.getSubMenuFont());
        lbFILESelect = new JLabel("Select a path to save file");
        lbFILESelect.setFont(settings.getSubMenuFont());
        lbFILESelect.setToolTipText("Click to Select a path to save file");
        lbFILESelect.setBorder(BorderFactory.createEmptyBorder(2, 50, 2, 2));
        lbFILESelect.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        lbFILESelect.setSize(settings.getAppHeight() / 2, lbCollectName.getHeight());
        lbFILESelect.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                final JFileChooser fc = new JFileChooser();
                fc.setFileSelectionMode(DIRECTORIES_ONLY);
                int response = fc.showOpenDialog(null);
                if (response == JFileChooser.APPROVE_OPTION) {
                    values.setFileName(fc.getSelectedFile().toString());
                    lbFILESelect.setText("File location : " + values.getFileName());
                }
            }
        });

        showCollectPan.add(lbCollectName);
        showCollectPan.add(lbFILESelect);

        showCollectPan.setVisible(false);

        headerSouth.setBorder(BorderFactory.createLineBorder(settings.getTheamColor()));
        headerSouth.add(showCollectPan, BorderLayout.NORTH);
        headerSouth.add(showConfigPan, BorderLayout.SOUTH);

        //creating menu pan
        menuPan.setBorder(new EmptyBorder(10, 10, 10, 10));
        lbMenuConfig = new JLabel("Configurations");
        lbMenuConfig.setIcon(settings.getScaledImage("images/dash.png", 30, 30));
        lbMenuConfig.setFont(settings.getMenuFont());
        lbMenuConfig.setToolTipText("Connect your Hardware to PC");
        lbMenuConfig.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        lbMenuConfig.setBorder(BorderFactory.createEmptyBorder(2, 10, 2, 10));
        lbMenuConfig.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
//                System.out.println("Config");
//                headerPan.add(showConfigPan,BorderLayout.SOUTH);
                showCollectPan.setVisible(false);
                showConfigPan.setVisible(true);
            }
        });

        lbMenuCollect = new JLabel("Collect");
        lbMenuCollect.setIcon(settings.getScaledImage("images/collect.png", 30, 30));
        lbMenuCollect.setFont(settings.getMenuFont());
        lbMenuCollect.setToolTipText("Save your data to post processing");
        lbMenuCollect.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        lbMenuCollect.setBorder(BorderFactory.createEmptyBorder(2, 10, 2, 10));
        lbMenuCollect.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
//                System.out.println("Collect");
//                headerPan.add(showCollectPan,BorderLayout.SOUTH);
                showCollectPan.setVisible(true);
                showConfigPan.setVisible(false);
//                System.out.println(headerPan.getComponents());
            }
        });

        lbMenuSettings = new JLabel("Settings");
        lbMenuSettings.setIcon(settings.getScaledImage("images/configs.png", 30, 30));
        lbMenuSettings.setFont(settings.getMenuFont());
        lbMenuSettings.setToolTipText("Change your User Interface Appearances");
        lbMenuSettings.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        lbMenuSettings.setBorder(BorderFactory.createEmptyBorder(2, 10, 2, 10));
        lbMenuSettings.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                AppSettings appSettings = new AppSettings();
                appSettings.setDefaultCloseOperation(HIDE_ON_CLOSE);
                appSettings.setVisible(true);
                appSettings.setTitle("Settings");
                appSettings.setResizable(false);
                appSettings.setSize(525 ,350);
            }
        });
        menuPan.add(lbMenuConfig);
        menuPan.add(lbMenuCollect);
        menuPan.add(lbMenuSettings);


        //add components to header pan
        headerPan.setBorder(new EmptyBorder(50, 50, 50, 50));
//        headerPan.add(imagePan,BorderLayout.CENTER);
        headerPan.add(namePan, BorderLayout.NORTH);
        headerPan.add(menuPan, BorderLayout.CENTER);
//        headerPan.add(showCollectPan,BorderLayout.LINE_END);
        headerPan.add(headerSouth, BorderLayout.SOUTH);

        //body
        bodyPan = new JPanel(new BorderLayout(10, 10));
        graphPan = new JPanel(new FlowLayout());
        controllerPan = new JPanel(new FlowLayout());
        lbGRAPHClear = new JLabel("Clear");
        lbGRAPHClear.setIcon(settings.getScaledImage("images/clean.png",16,16));
        lbGRAPHRun = new JLabel("Run");

        lbGRAPHClear.setFont(settings.getMenuFont());
        lbGRAPHClear.setBorder(BorderFactory.createEmptyBorder(2, 20, 2, 20));
        lbGRAPHClear.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        lbGRAPHClear.setEnabled(false);

        lbGRAPHRun.setFont(settings.getMenuFont());
        lbGRAPHRun.setBorder(BorderFactory.createEmptyBorder(2, 20, 2, 20));
        lbGRAPHRun.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        lbGRAPHRun.setEnabled(false);

        //the graph 1
        XYSeries seriesECG = new XYSeries("ECG data");
        XYSeriesCollection dataset = new XYSeriesCollection(seriesECG);
        JFreeChart chart = ChartFactory.createXYLineChart("ECG", "Samples", "Amplitude", dataset);

        //the graph 2
        XYSeries seriesECGInastance = new XYSeries("ECG Instance");
        XYSeriesCollection datasetIns = new XYSeriesCollection(seriesECGInastance);
        JFreeChart chart2 = ChartFactory.createXYLineChart("ECG Instance","Samples","Amplitude",datasetIns);


        //thread for show the data
        Thread thread = new Thread() {
            @Override
            public void run() {
                t = 0;
                Scanner scanner = null;
                try {
                    scanner = new Scanner(choosenPort.getInputStream());
                } catch (Exception e) {
                    System.out.println("Scanner Exception : " + e.getMessage());
                    JOptionPane.showMessageDialog(null,"Scanner Exception : " + e.getMessage(),"Warning",JOptionPane.WARNING_MESSAGE);
                }
                try{
                    while (scanner.hasNextLine()) {
                        String rec = scanner.nextLine();
                        int ecg = Integer.valueOf(rec);
//                        System.out.println(rec +","+ ecg);
                        seriesECG.add(t++, ecg);
                    }
                }catch (Exception e1){
                    System.out.println("Scanner Exception (No2) : " + e1.getMessage());
                }

            }
        };

        lbGRAPHClear.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                t=0;
                seriesECG.clear();
            }
        });
        lbGRAPHRun.setIcon(settings.getScaledImage("images/play.png", 20, 20));
        lbGRAPHRun.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (thread.getState().equals(Thread.State.NEW)) {
                    thread.start();
                    lbGRAPHRun.setText("Pause");

                } else {
                    if (thread.getState().equals(Thread.State.RUNNABLE) && !suspendFlg) {
                        thread.suspend();
                        suspendFlg = true;
                        lbGRAPHRun.setText("Resume");
//                        System.out.println("Suspending Thread " + thread.getState());
                    } else {
                        thread.resume();
                        suspendFlg = false;
                        lbGRAPHRun.setText("Pause");
//                        System.out.println("notify thread");
                    }
                }
            }
        });
        lbGRAPHRun.addPropertyChangeListener(new PropertyChangeListener() {
            @Override
            public void propertyChange(PropertyChangeEvent evt) {
                if (evt.getNewValue().equals("Resume")) {
                    lbGRAPHRun.setIcon(settings.getScaledImage("images/play.png", 20, 20));
                }else if (evt.getNewValue().equals("Pause")){
                    lbGRAPHRun.setIcon(settings.getScaledImage("images/pause.png", 20, 20));
                }
            }
        });

        graphPan.add(new ChartPanel(chart));
        graphPan.add(new ChartPanel(chart2));
        controllerPan.add(lbGRAPHRun);
        controllerPan.add(lbGRAPHClear);
        bodyPan.add(graphPan, BorderLayout.NORTH);
        bodyPan.add(controllerPan, BorderLayout.SOUTH);

        //footer
        footerPan = new JPanel(new FlowLayout());
        lbFooter = new JLabel(values.getAppOwner());
        lbFooter.setIcon(settings.getScaledImage("images/uop.png", 25, 25));
        lbFooter.setFont(settings.getFooterFont());

        footerPan.add(lbFooter);
        //add to back
        backPan.add(headerPan, BorderLayout.NORTH);
        backPan.add(bodyPan, BorderLayout.CENTER);
        backPan.add(lbFooter, BorderLayout.SOUTH);

    }
}
