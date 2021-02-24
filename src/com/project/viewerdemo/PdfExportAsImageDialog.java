package com.project.viewerdemo;

import java.awt.Dimension;
import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyEvent;
import java.io.File;
import java.util.Hashtable;
import java.util.Vector;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.imageio.ImageIO;
import javax.swing.AbstractAction;
import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.InputVerifier;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JSlider;
import javax.swing.JTextField;
import javax.swing.KeyStroke;
import javax.swing.border.Border;

import com.gnostice.pdfone.PdfDocument;
import com.gnostice.pdfone.PdfException;
import com.gnostice.pdfone.PdfPage;
import com.gnostice.pdfone.PdfViewer;

public class PdfExportAsImageDialog extends JDialog implements
    ItemListener, ActionListener
{

    private static final long serialVersionUID = 1L;

    private JPanel saveAsImageContainer = null;

    private JLabel labPageRange = null;

    private JTextField txtfldPageRange = null;

    private JLabel labImageCQ = null;

    private JButton btnBrowse = null;

    private JButton btnOk = null;

    private JTextField txtfldSelectedFile = null;

    String selectedFormat = null;

    File selectedFile = null;

    protected PdfDocument d;

    private JButton cancelBtn;
    
    private JPanel btnPan;

    private JLabel labRes;

	private JPanel panelImageFormat = null;

	private JComboBox comboboxImageFormats = null;

	private JPanel panelScaleandResolution = null;

	private JLabel labWidth = null;

	JTextField txtfldWidth = null;

	private JLabel labHeight = null;

	JTextField txtFldHeight = null;

	private JPanel panelOutputFolder = null;

	private JLabel labOutputFilePath = null;

	private JComboBox comboBoxRes = null;

    private JRadioButton radioBtnScaleRes;

    private JRadioButton radioBtnAsInviewer;

    private int scaleToResolution = 72;
    
    private PdfViewer viewer;

    private boolean saveAsMultiPageTiff;

    private double zoom = 1.0;

    private boolean asInViewer = true;
    
    private TextFieldInputVerifier txtfldInputVerifier;

    public double custWidth;

    public double custHeight;

    private JLabel labCompressionType;

    private JComboBox comboboxCompressionTypes;

    private boolean isCompressionSupported;

    private String compressionTypeSelected;

    private JFileChooser fileChooser;

    private JSlider sliderCQLevel;

    private String[] compTypes;

    private ButtonGroup selImageFormatGrp;

    private JRadioButton rbNonTiff;

    private JRadioButton rbMultiTiff;

//    private JLabel labOutputFileName;

//    private JTextField txtFldOutputFileName;

    /**
     * @param owner
     */
    public PdfExportAsImageDialog(Frame owner, PdfDocument document, PdfViewer pdfViewer)
    {
        super(owner,true);
        this.d = document;
        this.viewer = pdfViewer;
        initialize();
        setModal(true);
        setVisible(true);
    }

    /**
     * This method initializes this
     * 
     * @return void
     */
    private void initialize()
    {
        setResizable(false);
        this.setSize(550, 400);
        this.setTitle("Export As Image");
        this.setContentPane(getJContentPane());
        int x = (int) ((Toolkit.getDefaultToolkit().getScreenSize().width - this
            .getWidth()) / 2);
        int y = (int) ((Toolkit.getDefaultToolkit().getScreenSize().height - this
            .getHeight()) / 2);
        this.setLocation(x, y);
        
        KeyStroke escapeKeyStroke = KeyStroke.getKeyStroke(
            KeyEvent.VK_ESCAPE, 0, false);

        getRootPane().getInputMap(
            JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT).put(
            escapeKeyStroke, "escape");
        getRootPane().getActionMap().put("escape",
            new AbstractAction()
            {
                private static final long serialVersionUID = 1L;

                public void actionPerformed(ActionEvent e)
                {
                    dispose();
                }
            });
        this.pack();
        setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
    }
    
    private JPanel getJContentPane()
    {
        if (saveAsImageContainer == null)
        {

            saveAsImageContainer = new JPanel();
            saveAsImageContainer.setLayout(new GridBagLayout());
            GridBagConstraints gbc = new GridBagConstraints();
            gbc.insets = new Insets(5, 5, 5, 5);
            gbc.fill = GridBagConstraints.HORIZONTAL;

            gbc.gridx = 0;
            gbc.gridy = 0;
            saveAsImageContainer.add(getPanelImageFormat(), gbc);

            gbc.gridx = 0;
            gbc.gridy = 1;
            saveAsImageContainer.add(getPanelScaleandResolution(),
                gbc);

            gbc.gridx = 0;
            gbc.gridy = 2;
            saveAsImageContainer.add(getPanelOutputFolder(), gbc);

            gbc.gridx = 0;
            gbc.gridy = 3;
            saveAsImageContainer.add(getBtnPan(), gbc);
        }
        return saveAsImageContainer;
    }

    private JComboBox getImageFormatsComboBox()
    {
        if (comboboxImageFormats == null)
        {
            String[] formats = getSupportedImageFormats();
            comboboxImageFormats = new JComboBox(formats);
            selectedFormat = new String(formats[0]);
            setCompTypesOfSelFormat();
            comboboxImageFormats.addItemListener(this);
        }
        return comboboxImageFormats;
    }

    private String[] getSupportedImageFormats()
    {
        String[] formats = ImageIO.getWriterFormatNames();

        String[] outputFormats = new String[formats.length / 2];
        int pos = 0;
        for (int i = 0; i < formats.length; i++)
        {
            if (Character.isUpperCase(formats[i].charAt(0)))
            {
                outputFormats[pos] = formats[i];
                pos++;
            }
        }

        return outputFormats;
    }

    private JTextField getPageRangeTxtFld()
    {
        if (txtfldPageRange == null)
        {
            txtfldPageRange = new JTextField();
            txtfldPageRange.setText("1-" + d.getPageCount());
        }
        return txtfldPageRange;
    }
    
    private JSlider getSliderCQLevel()
    {
        if (sliderCQLevel == null)
        {
            sliderCQLevel = new JSlider(JSlider.HORIZONTAL, 0, 10, 10);
            Dimension dimension = new Dimension(175, 50);
            sliderCQLevel.setPreferredSize(dimension);
            sliderCQLevel.setMinimumSize(dimension);
            sliderCQLevel.setMaximumSize(dimension);
            sliderCQLevel.setMajorTickSpacing(1);
            Hashtable hashtable = new Hashtable();
            hashtable.put(new Integer(0), new JLabel("0.0"));
            hashtable.put(new Integer(5), new JLabel("0.5"));
            hashtable.put(new Integer(10), new JLabel("1.0"));
            sliderCQLevel.setLabelTable(hashtable);
            sliderCQLevel.setPaintLabels(true);
            sliderCQLevel.setMinorTickSpacing(1);
            sliderCQLevel.setSnapToTicks(true);
            sliderCQLevel.setPaintTicks(true);
            checkToShowCQSlider();
        }
        return sliderCQLevel;
    }

    private JButton getBrowseButton()
    {
        if (btnBrowse == null)
        {
            btnBrowse = new JButton();
            btnBrowse.setText("Browse");
            btnBrowse.addActionListener(this);
        }
        return btnBrowse;
    }
    
    private JPanel getBtnPan() {
        if (btnPan == null) {
            GridBagConstraints gridBagConstraints9 = new GridBagConstraints();
            gridBagConstraints9.insets = new Insets(5, 5, 5, 5);
            gridBagConstraints9.gridy = 0;
            gridBagConstraints9.gridx = 1;
            GridBagConstraints gridBagConstraints8 = new GridBagConstraints();
            gridBagConstraints8.insets = new Insets(5, 5, 5, 5);
            gridBagConstraints8.gridy = 0;
            gridBagConstraints8.gridx = 0;
            btnPan = new JPanel();
            btnPan.setLayout(new GridBagLayout());
            btnPan.add(getOkButton(), gridBagConstraints8);
            btnPan.add(getCancelBtn(), gridBagConstraints9);
        }
        return btnPan;
    }
    
    private JButton getOkButton()
    {
        if (btnOk == null)
        {
            btnOk = new JButton();
            btnOk.setText("OK");
            btnOk.addActionListener(this);
        }
        return btnOk;
    }

    private JButton getCancelBtn()
    {
        if (cancelBtn == null)
        {
            cancelBtn = new JButton();
            cancelBtn.setText("Cancel");
            cancelBtn.addActionListener(this);
        }
        return cancelBtn;
    }

    private JTextField getOutputTxtFld()
    {
        if (txtfldSelectedFile == null)
        {
            txtfldSelectedFile = new JTextField();
            txtfldSelectedFile.setEditable(false);
        }
        return txtfldSelectedFile;
    }
    
    private JPanel getPanelImageFormat() {
        if (panelImageFormat == null)
        {
            GridBagConstraints gbc = new GridBagConstraints();
            
            Insets insets0 = new Insets(0, 0, 0, 0);
            gbc.insets = insets0;
            
            panelImageFormat = new JPanel();
            Border border = BorderFactory
                .createTitledBorder("Image Format, Compression & Page Range");
            panelImageFormat.setLayout(new GridBagLayout());
            panelImageFormat.setBorder(border);

            selImageFormatGrp = new ButtonGroup();
            selImageFormatGrp.add(getRadBtnNonMultiPageTiff());
            selImageFormatGrp.add(getRadBtnMultipageTiff());

            JPanel formatsPanel = new JPanel();
            formatsPanel.setLayout(new GridBagLayout());
            
            gbc.gridx = 0;
            gbc.gridy = 0;
            gbc.anchor = GridBagConstraints.WEST;
            formatsPanel.add(getRadBtnNonMultiPageTiff(), gbc);
            
            gbc.gridx = 1;
            gbc.gridy = 0;
            insets0 = new Insets(2, 2, 2, 2);
            formatsPanel.add(getImageFormatsComboBox(), gbc);
            
            gbc.gridx = 2;
            gbc.gridy = 0;
            formatsPanel.add(getRadBtnMultipageTiff(), gbc);

            gbc.gridx = 0;
            gbc.gridy = 0;
            insets0 = new Insets(5, 2, 2, 2);
            panelImageFormat.add(formatsPanel, gbc);

            // compression type and level
            JPanel compPanel = new JPanel();
            compPanel.setLayout(new GridBagLayout());
            
            gbc.gridx = 0;
            gbc.gridy = 0;
            insets0 = new Insets(0, 0, 0, 0);
            labCompressionType = new JLabel("Compression Type:");
            compPanel.add(labCompressionType, gbc);

            insets0 = new Insets(2, 5, 2, 2);
            gbc.gridx = 1;
            gbc.gridy = 0;
            compPanel.add(getComboboxCompressionTypes(), gbc);

            gbc.gridx = 2;
            gbc.gridy = 0;
            labImageCQ = new JLabel("Level:");
            compPanel.add(labImageCQ, gbc);
            
            insets0 = new Insets(2, 0, 2, 2);
            gbc.gridx = 3;
            gbc.gridy = 0;
            compPanel.add(getSliderCQLevel(), gbc);

            insets0 = new Insets(2, 2, 2, 2);
            gbc.gridx = 0;
            gbc.gridy = 1;
            panelImageFormat.add(compPanel, gbc);

            // page range
            JPanel pageRangePanel = new JPanel();
            pageRangePanel.setLayout(new GridBagLayout());
        
            gbc.gridx = 0;
            gbc.gridy = 0;
            insets0 = new Insets(0, 0, 0, 0);
            labPageRange = new JLabel("Page Range:");
            pageRangePanel.add(labPageRange, gbc);

            insets0 = new Insets(2, 2, 2, 2);
            gbc.gridx = 1;
            gbc.gridy = 0;
            gbc.ipadx = 100;
            pageRangePanel.add(getPageRangeTxtFld(), gbc);
            gbc.ipadx = 0;

            gbc.gridx = 2;
            gbc.gridy = 0;
            JLabel labPageRangeExample = new JLabel(
                "(Example:1,2,5-10)");
            pageRangePanel.add(labPageRangeExample, gbc);

            gbc.gridx = 0;
            gbc.gridy = 2;
            gbc.insets = new Insets(2, 2, 5, 2);
            panelImageFormat.add(pageRangePanel, gbc);
        }
        return panelImageFormat;
    }


    private JComboBox getComboboxCompressionTypes()
    {
        if(comboboxCompressionTypes == null)
        {
            comboboxCompressionTypes = new JComboBox();    
            Dimension dimension = new Dimension(100, 25);
            comboboxCompressionTypes.setPreferredSize(dimension);
            comboboxCompressionTypes.setMinimumSize(dimension);
            comboboxCompressionTypes.setMaximumSize(dimension);
            addItemsCBCompTypes();
            comboboxCompressionTypes.addItemListener(this);
        }
       
        return comboboxCompressionTypes;
    }
    private void addItemsCBCompTypes()
    {
        if(compTypes != null)
        {
            if(selectedFormat.equalsIgnoreCase("BMP"))
            {
                comboboxCompressionTypes.removeAllItems();
                for (int i = 0; i < compTypes.length; i++)
                {
                    if (!compTypes[i].equalsIgnoreCase("BI_JPEG")
                        && !compTypes[i].equalsIgnoreCase("BI_PNG")
                        && !compTypes[i].equalsIgnoreCase("BI_RLE4"))
                    {
                        comboboxCompressionTypes.addItem(compTypes[i]);                        
                    }
                }   
                compressionTypeSelected = comboboxCompressionTypes
                .getItemAt(0).toString();
            }
            else
            {
                comboboxCompressionTypes.removeAllItems();
                
                for (int i = 0; i < compTypes.length; i++)
                {
                    comboboxCompressionTypes.addItem(compTypes[i]);
                }   
                compressionTypeSelected = comboboxCompressionTypes
                    .getItemAt(0).toString();
            }
            comboboxCompressionTypes.setEnabled(true);
            isCompressionSupported = true;
        }
        else
        {
            comboboxCompressionTypes.removeAllItems();
            comboboxCompressionTypes.setEnabled(false);
            comboboxCompressionTypes.addItem("Not_Supported");
        }
    }
    private void setCompTypesOfSelFormat()
    {
        isCompressionSupported = false;
        try
        {
            compTypes = d.getSupportedCompressionTypes(selectedFormat);            
        }
        catch (PdfException pdfe)
        {
        }
    }
    
    private void checkToShowCQSlider()
    {
        if (isCompressionSupported)
        {
            try
            {
                if (d.isImageFomatandCompressionLossless(selectedFormat,
                    compressionTypeSelected))
                {
                    labImageCQ.setEnabled(false);
                    sliderCQLevel.setEnabled(false);
                }
                else
                {
                    labImageCQ.setEnabled(true);
                    sliderCQLevel.setEnabled(true);
                }
            }
            catch (PdfException pdfe)
            {
            }
        }
        else
        {
            labImageCQ.setEnabled(false);
            sliderCQLevel.setEnabled(false);
        }
    }

    private JRadioButton getRadBtnNonMultiPageTiff()
    {
        if(rbNonTiff == null)
        {
            rbNonTiff = new JRadioButton();
            rbNonTiff.setSelected(true);
            rbNonTiff.addItemListener(this);
        }
        return rbNonTiff;
    }
    
    private JRadioButton getRadBtnMultipageTiff()
    {
        if(rbMultiTiff == null)
        {
            rbMultiTiff = new JRadioButton("Multipage TIFF");
            rbMultiTiff.addItemListener(this);
            rbMultiTiff.setSelected(false);
        }
        return rbMultiTiff;
    }

    private JPanel getPanelScaleandResolution() {
        if (panelScaleandResolution == null) {
        
            panelScaleandResolution = new JPanel();
            Border border = BorderFactory.createTitledBorder("Image Size");
            panelScaleandResolution.setBorder(border);
            panelScaleandResolution.setLayout(new GridBagLayout());
            
            GridBagConstraints gbc = new GridBagConstraints();           
            gbc.anchor = GridBagConstraints.WEST;
            gbc.insets = new Insets(2, 2, 0, 2);
            
            gbc.gridx = 0;
            gbc.gridy = 0;
            radioBtnScaleRes = new JRadioButton("Scale");
            panelScaleandResolution.add(radioBtnScaleRes, gbc);
            
            gbc.gridx = 0;
            gbc.gridy = 1;
            gbc.insets = new Insets(2, 2, 2, 2);
            gbc.anchor = GridBagConstraints.EAST;
            
            radioBtnAsInviewer = new JRadioButton("As in Viewer");
            radioBtnAsInviewer.setSelected(true);
            panelScaleandResolution.add(radioBtnAsInviewer,gbc);

            ButtonGroup btnGroupRadioScaleRes = new ButtonGroup();
            btnGroupRadioScaleRes.add(radioBtnScaleRes);
            btnGroupRadioScaleRes.add(radioBtnAsInviewer);
            radioBtnScaleRes.addItemListener(this);
            radioBtnAsInviewer.addItemListener(this);
            
            gbc.anchor = GridBagConstraints.EAST;
            gbc.gridx = 1;
            gbc.gridy = 0;
            gbc.ipadx = 0;
            labRes = new JLabel();
            labRes.setText("To Resolution:");
            panelScaleandResolution.add(labRes, gbc);
            
            gbc.fill = GridBagConstraints.BOTH;
            gbc.gridx = 2;
            gbc.gridy = 0;
            gbc.anchor = GridBagConstraints.WEST;
            panelScaleandResolution.add(getComboBoxRes(), gbc);
            gbc.fill = GridBagConstraints.NONE;
            
            gbc.gridx = 3;
            gbc.gridy = 0;
            labWidth = new JLabel();
            labWidth.setText("Width:");
            panelScaleandResolution.add(labWidth, gbc);
            
            gbc.gridx = 4;
            gbc.gridy = 0;
            gbc.ipadx = 50;
            gbc.anchor = GridBagConstraints.WEST;
            panelScaleandResolution.add(getTxtfldWidth(), gbc);
            gbc.ipadx = 0;
            
            gbc.gridx = 5;
            gbc.gridy = 0;
            gbc.anchor = GridBagConstraints.EAST;
            labHeight = new JLabel();
            labHeight.setText("Height:");
            panelScaleandResolution.add(labHeight, gbc);
            
            gbc.gridx = 6;
            gbc.gridy = 0;
            gbc.ipadx = 50;
            gbc.anchor = GridBagConstraints.WEST;
            panelScaleandResolution.add(getTxtFldHeight(), gbc);
            gbc.ipadx = 0;
            
            try
            {
                PdfPage page = d.getPage(viewer.getCurrentPage());  
                double width =  page.getCropBoxWidth();
                double height = page.getCropBoxHeight();
                
                txtfldWidth.setText(String.valueOf(width));
                txtFldHeight.setText(String.valueOf(height));
            }
            catch (Exception e)
            {
                
            }
            disableScaleRes();
            zoom = viewer.getZoom()/100;
            scaleToResolution = viewer.getDpi();
        }
        return panelScaleandResolution;
    }
    private void disableScaleRes()
    {
        if (comboBoxRes != null && txtfldWidth != null
            && txtFldHeight != null)
        {
            comboBoxRes.setEnabled(false);
            txtfldWidth.setEnabled(false);
            txtFldHeight.setEnabled(false);
        }
    }
    private void enableScaleRes()
    {
        if (comboBoxRes != null && txtfldWidth != null
            && txtFldHeight != null)
        {
            comboBoxRes.setEnabled(true);
            txtfldWidth.setEnabled(true);
            txtFldHeight.setEnabled(true);
            
            scaleToResolution = Integer.parseInt(comboBoxRes
                .getSelectedItem().toString());            
        }
    }

    private JTextField getTxtfldWidth() {
        if (txtfldWidth == null) {
            txtfldWidth = new JTextField();
            if(txtfldInputVerifier == null)
            {
                txtfldInputVerifier = new TextFieldInputVerifier();
            }
            txtfldWidth.setInputVerifier(txtfldInputVerifier);
        }
        return txtfldWidth;
    }
    private JTextField getTxtFldHeight() {
        if (txtFldHeight == null) {
            txtFldHeight = new JTextField();
            if(txtfldInputVerifier == null)
            {
                txtfldInputVerifier = new TextFieldInputVerifier();
            }
            txtFldHeight.setInputVerifier(txtfldInputVerifier);
        }
        return txtFldHeight;
    }

    private JPanel getPanelOutputFolder() {
        if (panelOutputFolder == null) {
            
            panelOutputFolder = new JPanel();
            Border border = BorderFactory.createTitledBorder("Ouput Folder");
            panelOutputFolder.setLayout(new GridBagLayout());
            panelOutputFolder.setBorder(border);

            GridBagConstraints gbc = null;
            gbc = new GridBagConstraints();
            gbc.gridx = 0;
            gbc.gridy = 0;
            gbc.insets = new Insets(2, 2, 2, 2);
            gbc.anchor = GridBagConstraints.EAST;  
            labOutputFilePath = new JLabel();
            labOutputFilePath.setText("Output Path:");
            panelOutputFolder.add(labOutputFilePath, gbc);
            
            gbc = new GridBagConstraints();
            gbc.anchor = GridBagConstraints.WEST;
            gbc.gridx = 1;
            gbc.gridy = 0;
            gbc.ipadx = 285;
            gbc.insets = new Insets(2, 2, 2, 2);
            panelOutputFolder.add(getOutputTxtFld(), gbc);

            gbc = new GridBagConstraints();
            gbc.gridx = 2;
            gbc.gridy = 0;
            gbc.anchor = GridBagConstraints.EAST;
            gbc.insets = new Insets(2, 2, 2, 2);
            panelOutputFolder.add(getBrowseButton(), gbc);
            
//            gbc = new GridBagConstraints();
//            gbc.gridx = 0;
//            gbc.gridy = 1;
//            gbc.anchor = GridBagConstraints.EAST;
//            gbc.insets = new Insets(2, 2, 2, 2);
//            labOutputFileName = new JLabel("Filename Pattern:");
//            panelOutputFolder.add(labOutputFileName, gbc);
//            
//            gbc = new GridBagConstraints();
//            gbc.gridx = 1;
//            gbc.gridy = 1;
//            gbc.anchor = GridBagConstraints.WEST;
//            gbc.ipadx = 200;
//            gbc.insets = new Insets(2, 2, 2, 2);
//            txtFldOutputFileName = new JTextField();
//            panelOutputFolder.add(txtFldOutputFileName, gbc);
        }
        return panelOutputFolder;
    }

    private JComboBox getComboBoxRes() {
        if (comboBoxRes == null) {
            String[] resolutions = new String[] { "72", "96", "110" };
            comboBoxRes = new JComboBox(resolutions);
            comboBoxRes.addItemListener(this);
            int v_dpi = viewer.getDpi();
            if(v_dpi == 72)
            {
                comboBoxRes.setSelectedIndex(0);
            }
            else if(v_dpi == 96)
            {
                comboBoxRes.setSelectedIndex(1);
            }
            else
            {
                comboBoxRes.setSelectedIndex(2);
            }
        }
        return comboBoxRes;
    }

    public void itemStateChanged(ItemEvent ie)
    {
        if (ie.getSource() == comboboxImageFormats)
        {
            if(ie.getStateChange() == ItemEvent.DESELECTED)
            {
                selectedFormat = comboboxImageFormats.getSelectedItem().toString();
                setCompTypesOfSelFormat();
                addItemsCBCompTypes();
                checkToShowCQSlider();
            }
        }
        else if (ie.getSource() == rbMultiTiff)
        {
            if (rbMultiTiff.isSelected())
            {
                saveAsMultiPageTiff = true;
                selectedFormat = "TIFF";
                comboboxImageFormats.setEnabled(false);
                setCompTypesOfSelFormat();
                addItemsCBCompTypes();
                checkToShowCQSlider();
            }
        }
        else if (ie.getSource() == rbNonTiff)
        {
            if (rbNonTiff.isSelected())
            {
                comboboxImageFormats.setEnabled(true);
                saveAsMultiPageTiff = false;
                selectedFormat = comboboxImageFormats
                    .getSelectedItem().toString();
                setCompTypesOfSelFormat();
                addItemsCBCompTypes();
                checkToShowCQSlider();
            }
        }
        else if (ie.getSource() == comboBoxRes)
        {
            if(ie.getStateChange() == ItemEvent.DESELECTED)
            {
                scaleToResolution = Integer.parseInt((String) comboBoxRes
                    .getSelectedItem());                
            }
        }
        else if(ie.getSource() == radioBtnAsInviewer)
        {
            if(radioBtnAsInviewer.isSelected())
            {
                asInViewer = true;
                scaleToResolution = viewer.getDpi();
                zoom = viewer.getZoom()/100;
                disableScaleRes();                
            }
        }
        else if (ie.getSource() == radioBtnScaleRes)
        {
            if(radioBtnScaleRes.isSelected())
            {
                asInViewer = false;
                zoom = 1.0;
                enableScaleRes();                
            }
        }
        else if (ie.getSource() == comboboxCompressionTypes)
        {
            if(ie.getStateChange() == ItemEvent.DESELECTED )
            {
                if(isCompressionSupported)
                {
                    compressionTypeSelected = comboboxCompressionTypes
                        .getSelectedItem().toString();  
                    checkToShowCQSlider();
                }                
            }
        }
    }

    public void actionPerformed(ActionEvent ae)
    {
        if (ae.getSource() == btnBrowse)
        {
            if(fileChooser == null)
            {
              fileChooser = new JFileChooser(System
                    .getProperty("user.home"));                
            }
            
            fileChooser
                .setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);

            if (fileChooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION)
            {
                selectedFile = fileChooser.getSelectedFile();
                txtfldSelectedFile.setText(selectedFile.getPath());
            }
        }
        else if (ae.getSource() == btnOk)
        {
            try
            {
                if (txtfldPageRange.getText().equals(""))
                {
                    JOptionPane.showMessageDialog(this,
                        "Invalid Page Range cannot be empty.");
                    return;
                }
                Vector pages = getPages(txtfldPageRange.getText()
                    .trim());
                if (pages == null || pages.size() == 0)
                {
                    txtfldPageRange.setText("1-" + d.getPageCount());
                    JOptionPane.showMessageDialog(this,
                        "Invalid page range.");
                    return;
                }

                if (selectedFile == null)
                {
                    txtfldSelectedFile.setText("");
                    JOptionPane.showMessageDialog(this,
                        "File Output location cannot be null.");
                    return;
                }

                if (d != null)
                {
                    if (!saveAsMultiPageTiff)
                    {
                        float cq = (float)sliderCQLevel.getValue()/10;
                        if(asInViewer)
                        {
                            zoom = viewer.getZoom()/100;
                            if(isCompressionSupported)
                            {
                                d.saveAsImage(selectedFormat,
                                    txtfldPageRange.getText(), "",
                                    cq, compressionTypeSelected,
                                    selectedFile.getPath(),
                                    scaleToResolution, zoom);                            
                            }
                            else
                            {
                                d.saveAsImage(selectedFormat,
                                    txtfldPageRange.getText(), "",
                                    cq, selectedFile.getPath(),
                                    scaleToResolution, zoom);                            
                            }
                        }
                        else
                        {
                            if(isCompressionSupported)
                            {
                                d.saveAsImage(selectedFormat,
                                    txtfldPageRange.getText(), "",
                                    (int) custWidth,
                                    (int) custHeight, cq,
                                    compressionTypeSelected,
                                    selectedFile.getPath(),
                                    scaleToResolution);
                            }
                            else
                            {
                                d.saveAsImage(selectedFormat,
                                    txtfldPageRange.getText(), "",
                                    (int)custWidth, (int)custHeight, cq,
                                    selectedFile.getPath(),scaleToResolution);
                            }
                        }
                    }
                    else
                    {
                        float cq = (float)sliderCQLevel.getValue()/10;
                        if (asInViewer)
                        {
                            zoom = viewer.getZoom()/100;
                            if(isCompressionSupported)
                            {
                                int index = comboboxCompressionTypes.getSelectedIndex();
                                d.saveDocAsTiffImage(selectedFile
                                    .getPath(),
                                    txtfldPageRange.getText(),
                                    index,cq, scaleToResolution, zoom);                                
                            }
                            else
                            {
                                d.saveDocAsTiffImage(selectedFile
                                    .getPath(),
                                    txtfldPageRange.getText(),
                                    PdfDocument.TIFF_Compression_NONE,
                                    cq, scaleToResolution, zoom);                                
                            }
                        }
                        else
                        {
                            if(isCompressionSupported)
                            {
                                int index = comboboxCompressionTypes.getSelectedIndex();
                                d.saveDocAsTiffImage(
                                    selectedFile.getPath(),
                                    txtfldPageRange.getText(),
                                    (int)custWidth, 
                                    (int)custHeight,
                                    index,
                                    cq, scaleToResolution);
                            }
                            else
                            {
                                d.saveDocAsTiffImage(
                                    selectedFile.getPath(),
                                    txtfldPageRange.getText(),
                                    (int)custWidth, 
                                    (int)custHeight,
                                    PdfDocument.TIFF_Compression_NONE,
                                    cq, scaleToResolution);

                            }
                        }
                    }
                }
                dispose();
            }
            catch (NumberFormatException nfe)
            {
                JOptionPane
                    .showMessageDialog(
                        this,
                        "Invalid Compression quality level must be a float value ranging between 0.0-1.0");
            }
            catch (Exception e)
            {
                JOptionPane.showMessageDialog(this,
                    "Error occured while trying to export PDF as image: "
                        + e.getMessage());
                dispose();
            }
            catch(OutOfMemoryError outOfMemoryError)
            {
                JOptionPane.showMessageDialog(this, "Insufficient JVM heap size to perform this operation.");
            }
        }
        else if (ae.getSource() == cancelBtn)
        {
            dispose();
        }
    }

    void setDocument(PdfDocument document)
    {
        this.d = document;

        this.txtfldPageRange.setText("1-" + d.getPageCount());
    }
    
    private Vector getPages(String pageRange) throws PdfException
    {
        if (pageRange == null || pageRange == "") 
        {
            return null;
        }
        pageRange = pageRange.trim();
        Pattern pat = Pattern.compile("(-\\s*(\\d++))|" + 
            "((\\d++)\\s*-\\s*(\\d++))|((\\d++)\\s*-)|" + 
            "(\\d++)|(-[^\\d]*)"); 
        Matcher mat = pat.matcher(pageRange.subSequence(
            0, pageRange.length()));
        Integer start = null;
        Integer end = null;
        Vector v = new Vector();
        int pageCount = d.getPageCount();
        if (pageRange.toUpperCase().equals("ODD")) 
        {
            for (int i = 1; i <= pageCount; i += 2)
            {
                v.add(new Integer(i));
            }
        }
        else if (pageRange.toUpperCase().equals("EVEN")) 
        {
            for (int i = 2; i <= pageCount; i += 2)
            {
                v.add(new Integer(i));
            }
        }
        else if (pageRange.toUpperCase().equals("ALL")) 
        {
            for (int i = 1; i <= pageCount; ++i)
            {
                v.add(new Integer(i));
            }
        }
        else while (mat.find())
        {
            start = mat.group(1) != null ? 
                    new Integer(1) :
                mat.group(3) != null ? 
                    new Integer(Integer.parseInt(mat.group(4))) : 
                mat.group(6) != null ? 
                    new Integer(Integer.parseInt(mat.group(7))) :
                mat.group(8) != null ? 
                    new Integer(Integer.parseInt(mat.group(8))) : 
                mat.group(9) != null ? 
                    new Integer(1) : null;
            
            end = mat.group(1) != null ? 
                  new Integer(Integer.parseInt(mat.group(2))) :
              mat.group(3) != null ? 
                  new Integer(Integer.parseInt(mat.group(5))) : 
              mat.group(6) != null ? 
                  new Integer(pageCount) :
              mat.group(9) != null ? new Integer(pageCount) : null;
            
            int i = start != null ? Math.abs(start.intValue()) : 0;
            int j = end != null ? Math.abs(end.intValue()) : 0;
            if (j < i && j != 0) //swap i and j
            {
                i -= j;
                j += i;
                i = (j - i);
            }
            do if (i != 0)
            {
                start = new Integer(i);
                if ( !(v.contains(start))
                            && start.intValue() <= pageCount)
                {
                    v.add(start);
                }
            } while (++i <= j);
        }
        
        return v;
    }
    class TextFieldInputVerifier extends InputVerifier implements
        FocusListener
    {
        
        public boolean shouldYieldFocus(JComponent input)
        {
            boolean inputOK = verify(input);
            if (inputOK)
            {
                return true;
            }
            else
            {
                return false;
            } 
        }
        
        public boolean verify(JComponent input)
        {
            if(input == txtfldWidth)
            {
                if (txtfldWidth.getText().equals(""))
                {
                   showMessageInvalidInput();
                   return false;
                }
                try
                {
                    custWidth = Double.valueOf(txtfldWidth.getText())
                        .doubleValue();
                    if(custWidth <= 0)
                    {
                        showMessageInvalidInput();
                        return false;
                    }
                    return true;
                }
                catch (NumberFormatException nfe)
                {
                    showMessageInvalidInput();
                    return false;
                }
            }
            else if(input == txtFldHeight)
            {
                if (txtFldHeight.getText().equals(""))
                {
                   showMessageInvalidInput();
                   return false;
                }
                try
                {
                    custHeight = Double.valueOf(txtFldHeight.getText())
                        .doubleValue();
                    if(custHeight <= 0)
                    {
                        showMessageInvalidInput();
                        return false;
                    }
                    return true;
                }
                catch (NumberFormatException nfe)
                {
                    showMessageInvalidInput();
                }
            }
            return false;  
        }

        public void focusGained(FocusEvent e)
        {
            
        }

        public void focusLost(FocusEvent e)
        {
            shouldYieldFocus((JTextField) e.getSource());
        }
        
        private void showMessageInvalidInput()
        {
            JOptionPane
                .showMessageDialog(
                    null,
                    new String(
                        "Invlaid Width or Height must be of type int and > 0"),
                    "Error Message", JOptionPane.ERROR_MESSAGE);
        }
    }
}  
