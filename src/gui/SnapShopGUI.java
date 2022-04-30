// Patrick Tibbals
// Program #3
// Kivanic Dincer
package gui;

import filters.*;
import image.PixelImage;
import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.io.File;
import java.io.IOException;
import java.util.Locale;

/**
 *  Image GUI
 * @author Patrick Tibbals
 * @version 1.0
 */
public class SnapShopGUI {

    /**
     * Start the GUI build
     */
    public void start() {
        javax.swing.JOptionPane.showMessageDialog(null, "SnapShop placeholder");
        new MainWindow().mainWindow();
    }
}

/**
 * Class for managing main Frame
 */
class MainWindow{
    /**
     * Create the Frame and call out to button methods
     */
    void mainWindow(){
        JFrame frame = new JFrame();
        frame.setLayout(new BorderLayout());

        frame.setTitle("TCSS 305 – Programming Assignment 3 - Patrick Tibbals");
        frame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
// Generate buttons and listeners
        new buttonCreation().imgOPSGenerator(frame);
        new buttonCreation().windowOPSGenerator(frame);
        frame.setSize(800,200);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}

/**
 * Creation of buttons and listeners
 */
class buttonCreation {
    // Flag to set buttons
    boolean buttonsEnabled = false;
    // Image storage
    static PixelImage image;
    // Panels for buttons and image
    static JPanel imgOps = new JPanel();
    static JPanel imgPanel = new JPanel();
    static JPanel windowOps = new JPanel();
    // Buttons to be added to GUI
    final static String[] imageOperations = new String[]{"Edge Detect", "Edge Highlight", "Flip Horizontal", "Flip Vertical", "Grayscale", "Sharpen", "Soften",};
    final static String[] windowOperations = new String[]{"Open", "Save As", "Close Image", "Undo"};
    // File chooser for open and save buttons
    JFileChooser fileChooser = new JFileChooser("sample_images");
    // Image Icon for the label
    ImageIcon img;
    // Label for image Icon storage
    JLabel label = new JLabel();
    // File name storage
    String fileName;

    /**
     * Create the Image filter buttons
     * @param frame JFrame window
     */
    void imgOPSGenerator(JFrame frame) {
        for (String button : imageOperations) {
            JButton b = new JButton(button);
            b.setEnabled(buttonsEnabled);
            imgOps.add(b);
            frame.add(imgOps, BorderLayout.NORTH);
            b.addActionListener(e -> method(button, b, frame));
        }
        frame.add(imgPanel, BorderLayout.CENTER);
    }

    /**
     * Create the window operation buttons; open,save,close,undo.
     * @param frame JFrame window
     */
    void windowOPSGenerator(JFrame frame) {
        for (String button : windowOperations) {
            String temp;
            if (button.contains(" ")) {
                temp = button.toLowerCase(Locale.ROOT).substring(0, button.indexOf(' '));
            } else {
                temp = button;
            }
            if(temp.equals("Undo")){
                img = new ImageIcon("icons/" + temp + ".png");
            }else {
                img = new ImageIcon("icons/" + temp + ".gif");
            }
            JButton b = new JButton(img);
            b.setText(button);
            b.setHorizontalTextPosition(AbstractButton.TRAILING);
            b.setVerticalTextPosition(AbstractButton.CENTER);
            if (button.equals("Open")) {
                b.setEnabled(true);
            } else {
                b.setEnabled(buttonsEnabled);
            }
            windowOps.add(b);
            frame.add(windowOps, BorderLayout.SOUTH);
            b.addActionListener(e -> method(button, b, frame));
        }
    }

    /**
     * Direct button action listeners to the appropriate methods
     * @param button button name
     * @param b the JButton
     * @param frame JFrame window
     */
    private void method(String button, JButton b, JFrame frame) {
        switch (button) {
            case "Edge Detect" -> edgeDetect();
            case "Edge Highlight" -> edgeHighlight();
            case "Flip Horizontal" -> flipHorizontal();
            case "Flip Vertical" -> flipVertical();
            case "Grayscale" -> grayscale();
            case "Sharpen" -> sharpen();
            case "Soften" -> soften();
            case "Open" -> open(b, frame);
            case "Save As" -> saveAs();
            case "Close Image" -> closeImage(frame);
            case "Undo" -> undo(frame);
            default -> {
            }
        }
    }

    /**
     * Edge Detect Update method
     */
    private void edgeDetect() {
        new EdgeDetectFilter().filter(image);
        updateImage();
    }
    /**
     * Edge Highlight Update method
     */
    private void edgeHighlight() {
        new EdgeHighlightFilter().filter(image);
        updateImage();
    }
    /**
     * Flip Horizontal Update method
     */
    private void flipHorizontal() {
        new FlipHorizontalFilter().filter(image);
        updateImage();
    }
    /**
     * Flip vertical Update method
     */
    private void flipVertical() {
        new FlipVerticalFilter().filter(image);
        updateImage();
    }
    /**
     * Grayscale Update method
     */
    private void grayscale() {
        new GrayscaleFilter().filter(image);
        updateImage();
    }
    /**
     * Sharpen Update method
     */
    private void sharpen() {
        new SharpenFilter().filter(image);
        updateImage();
    }
    /**
     * Soften Update method
     */
    private void soften() {
        new SoftenFilter().filter(image);
        updateImage();
    }

    /**
     * Open first image and initiate the other buttons
     * @param button button type
     * @param frame current JFrame
     */
    private void open(JButton button, JFrame frame) {
// Create the file type filter
        FileFilter filter = new FileNameExtensionFilter("Graphics File", "jpg", "jpeg", "gif", "png", "GIF");
        fileChooser.setFileFilter(filter);
        fileChooser.showOpenDialog(button);
// Set the buttons to be enabled now
        buttonsEnabled = true;
// Remove all the current components
        imgOps.removeAll();
        windowOps.removeAll();
// Add the working buttons
        imgOPSGenerator(frame);
        windowOPSGenerator(frame);
// Pick the correct file path
        fileName = fileChooser.getSelectedFile().toString();
// Set the frame image and make visible
        frame.add(imgPanel);
        setImage(frame);
        setFrameSize(frame);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    /**
     * Close the current image and reset the frame to empty
     * @param frame current JFrame
     */
    private void closeImage(JFrame frame) {
        imgPanel.removeAll();
        imgPanel.repaint();
// Set the buttons to be disabled now
        buttonsEnabled = false;
// Remove all the current components
        label.removeAll();
        imgOps.removeAll();
        windowOps.removeAll();
        frame.remove(imgPanel);
// Add the working buttons
        imgOPSGenerator(frame);
        windowOPSGenerator(frame);
// Set frame visible without image
        frame.pack();
        setFrameSize(frame);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    /**
     *  Save the image; default to png file
     */
    private void saveAs() {
        int sf = fileChooser.showSaveDialog(fileChooser);
        if(sf == JFileChooser.APPROVE_OPTION){
            try {
                ImageIO.write(image,"png",fileChooser.getSelectedFile());
            } catch (IOException e) {
                JOptionPane.showMessageDialog(null,"That is not a usable file type." + "\n"+ fileName);
            }
        }else if(sf == JFileChooser.CANCEL_OPTION){
            JOptionPane.showMessageDialog(null, "File save has been canceled");
        }
    }

    /**
     * Undo the all applied filters
     * @param frame current JFrame
     */
    private void undo(JFrame frame){
        setImage(frame);
        setFrameSize(frame);
    }

    /**
     * Set the frame size according the all elements and return to center of screen
     * @param frame current JFrame
     */
    private void setFrameSize(JFrame frame){
        // Dimension for frame size
        Dimension fMin = frame.getPreferredSize();
        frame.setMinimumSize(fMin);
        frame.setLocationRelativeTo(null);
        frame.pack();
    }

    /**
     *  Update the image with the current filter change
     */
    private void updateImage(){
        label.setIcon(new ImageIcon(image));
        label.setSize(image.getWidth(), image.getHeight());
        label.setMinimumSize(label.getSize());
        System.out.println("Image Added");
    }

    /**
     *  Open the initial image choice and set the image.
     * @param frame current JFrame
     */
    private void setImage(JFrame frame) {
//Gather the Image
        label.removeAll();
        imgPanel.removeAll();
        try {
            image = null;
            image = PixelImage.load(new File(fileName));
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(frame, "The Image at location:" + "\n" + fileName + "\n" + "has failed to load.");
        }
//Place image in icon
        updateImage();
        JPanel center = new JPanel();
        center.add(label);
        imgPanel.add(center);
        setFrameSize(frame);
    }
}