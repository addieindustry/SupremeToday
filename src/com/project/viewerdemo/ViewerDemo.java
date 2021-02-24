package com.project.viewerdemo;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.InputEvent;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.security.CodeSource;
import java.security.ProtectionDomain;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;

import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JProgressBar;
import javax.swing.JRadioButtonMenuItem;
import javax.swing.JSeparator;
import javax.swing.JTextField;
import javax.swing.JToggleButton;
import javax.swing.JToolBar;
import javax.swing.JViewport;
import javax.swing.KeyStroke;
import javax.swing.SwingUtilities;
import javax.swing.TransferHandler;
import javax.swing.UIManager;
import javax.swing.UIManager.LookAndFeelInfo;

import com.gnostice.pdfone.PdfDocument;
import com.gnostice.pdfone.PdfException;
import com.gnostice.pdfone.PdfPasswordHandler;
import com.gnostice.pdfone.PdfPrinter;
import com.gnostice.pdfone.PdfRenderErrorHandler;
import com.gnostice.pdfone.PdfViewer;
import com.gnostice.pdfone.PdfViewerChangeHandler;
import com.gnostice.pdfone.PDFOne;
import com.gnostice.pdfone.encodings.PdfEncodings;
import com.gnostice.pdfone.fonts.PdfFont;
import com.project.helper.Queries;
import javafx.scene.text.Font;

public final class ViewerDemo extends JFrame implements
        ActionListener, ItemListener, WindowListener, PdfPasswordHandler,
        PdfViewerChangeHandler, PdfRenderErrorHandler {

    static {
        PDFOne.activate("RW204Z:O96T28G:16VKE4V:D9WV8", "044ORFGI:15A87ERQ5:NER2ZE8R1:0VAR10");
    }

    private static final long serialVersionUID = 1L;

    // Containers
    private PdfViewer viewer;

    private PdfPrinter pdfPrinter;

    private PdfDocument d;

    // Controls for top panel
    private JFileChooser fc;
    private JButton btnLoad;

    private JButton btnPrint;

//    private JButton btnDocInfo;
    private String docPath;

    private JToggleButton btnActualSize = null;

    private JToggleButton btnFitPage = null;

    private JToggleButton btnFitWidth = null;

    private JButton btnZoomIn = null;

    private JComboBox cboZoomFactor = null;

    private JButton btnZoomOut = null;

    private JButton btnRotateCounterClockwise = null;

    private JButton btnRotateClockwise = null;

    // Controls for bottom panel
    private JButton btnFirstPage = null;

    private JButton btnPreviousPage = null;

    private JTextField txtGoToPage = null;

    private JButton btnNextPage = null;

    private JButton btnLastPage = null;

    // JMenuItems
    private JMenuItem mnuOpenItem;

    private JMenuItem mnuPrintItem;

    private JMenuItem mnuDocInfo;

    private JMenuItem mnuCloseItem;

    private JMenuItem mnuExitItem;

    private JMenuItem mnuShowPageDisplaySettingsDialog;

    private JMenuItem mnuFirstPageItem;

    private JMenuItem mnuPreviousPageItem;

    private JMenuItem mnuNextPageItem;

    private JMenuItem mnuLastPageItem;

    private JMenuItem mnuPreviousView;

    private JMenuItem mnuNextView;

    private JMenuItem mnuActualSizeItem;

    private JMenuItem mnuFitPageItem;

    private JMenuItem mnuFitWidthItem;

    private JMenuItem mnuZoomInItem;

    private JMenuItem mnuZoomOutItem;

    private JMenuItem mnuRotateAntiClokwiseItem;

    private JMenuItem mnuRotateClokwiseItem;

    private JCheckBoxMenuItem chkMnuShowLabels;

    private JRadioButtonMenuItem radioMnuLayoutSinglePage;
    private JRadioButtonMenuItem radioMnuLayoutSinglePageContinuous;
    private JRadioButtonMenuItem radioMnuLayoutSideBySide;
    private JRadioButtonMenuItem radioMnuLayoutSideBySideContinuous;
    private JRadioButtonMenuItem radioMnuLayoutAutoFitColumnsInWindow;
    private JRadioButtonMenuItem radioMnuLayoutUserDefined;

    private JCheckBoxMenuItem chkMnuShowGapsBetweenPages;

    private JCheckBoxMenuItem chkMnuShowPageBorders;

    private JCheckBoxMenuItem chkMnuShowCoverPageDuringSideBySide;

    private JCheckBoxMenuItem chkMnuShowPageNumberLabelWhileTracking;

    private JCheckBoxMenuItem chkMnuShowPageImageWhileTracking;

    private JRadioButtonMenuItem radioMnuDPI_72;
    private JRadioButtonMenuItem radioMnuDPI_96;
    private JRadioButtonMenuItem radioMnuDPI_110;

    private JRadioButtonMenuItem radioMnuScrollMode_BLIT_SCROLL_MODE;
    private JRadioButtonMenuItem radioMnuScrollMode_BACKINGSTORE_SCROLL_MODE;

    private JRadioButtonMenuItem[] mnuLAFItems;

    private JMenuItem mnuAboutItem;

    //for export
    JMenuItem mnuItemExportAsImage;

    PdfExportAsImageDialog pdfAsImageDialog;

    private double currentZoomFactor;

    private Properties userPreferences;

    private String lookAndFeelClassName;

    private File recentDir;

    private int dpiPropertyVal;

//    private AboutGnosticePDFOneJava aboutDialog;
    private DocumentInfoDialog docInfoDialog;

    private JToolBar topToolBar;
    private JToolBar statusBar;
    private JProgressBar progress;

    private JToggleButton btnSinglePage = null;

    private JToggleButton btnContinuous = null;

    private JToggleButton btnSideBySide = null;

    private JToggleButton btnSideBySideContinuous = null;

    private JToggleButton btnAutoFitColumnsInWindow = null;

    private JToggleButton btnCustomLayout = null;

    private JTextField txtCustomColumnCount = null;

    private static String jarFilePath = "";

    private boolean canDropPDF = true;

    static {
        try {
            Class viewerDemoClass;
            viewerDemoClass = Class.forName("viewerdemo.ViewerDemo");
            ProtectionDomain protectionDomain = viewerDemoClass.getProtectionDomain();
            CodeSource codeSource = protectionDomain
                    .getCodeSource();
            URL location = codeSource.getLocation();
            URI jarURI = new URI(location.toString());
            File jarFile = new File(jarURI);

            jarFilePath = jarFile.getParentFile().getAbsolutePath();
        } catch (Throwable ex) {
        }
    }

    public ViewerDemo() {
        loadUserPreferences();

        fc = new JFileChooser();
        fc.setAcceptAllFileFilterUsed(false);
        fc.addChoosableFileFilter(new PdfFileFilter());
        fc.setCurrentDirectory(recentDir);

        if (recentDir == null) {
            recentDir = fc.getCurrentDirectory().getAbsoluteFile();
        }

//        fc.setCurrentDirectory(new File(System.getProperty("user.home")));
//        recentDir = fc.getCurrentDirectory();
        d = new PdfDocument();
        d.setOnPasswordHandler(this);

        viewer = new PdfViewer();
        viewer.setDocument(d);

//      Create a new PdfPrinter object
        pdfPrinter = new PdfPrinter();
        pdfPrinter.setRenderErrorHandler(this);

        if (Queries.PRINT_SETTING_MODEL.getPageType().equals("A4")){
            pdfPrinter.setPageSize(2);
        }else if (Queries.PRINT_SETTING_MODEL.getPageType().equals("Legal")) {
            pdfPrinter.setPageSize(7);
        }else if (Queries.PRINT_SETTING_MODEL.getPageType().equals("Letter")){
            pdfPrinter.setPageSize(0);
        }

        // Set the document object to the viewer
        pdfPrinter.setDocument(d);

        loadPageDisplaySettings();
        viewer.showNavigationPanel(true);
        viewer.setViewerChangeHandler(this);
        viewer.setRenderErrorHandler(this);

//        viewer.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
//        viewer.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        getContentPane().setLayout(new BorderLayout());

        getContentPane().add(viewer, BorderLayout.CENTER);
        topToolBar = getTopToolBar();
        getContentPane().add(topToolBar, BorderLayout.NORTH);
//        JPanel topToolBarAsPanel = getTopToolBarAsPanel();
//        getContentPane().add(topToolBarAsPanel, BorderLayout.NORTH);
//        getContentPane().add(getBottomToolBar(), BorderLayout.SOUTH);
        getContentPane().add(getStatusBar(), BorderLayout.SOUTH);
        createMenus();

        requestFocus();

//        aboutDialog = new AboutGnosticePDFOneJava();
//        aboutDialog.setModal(true);
        docInfoDialog = new DocumentInfoDialog();
        docInfoDialog.setModal(true);

        // Listen to windowClosing event so that user preferences can be stored
        addWindowListener(this);

        /*viewer.addComponentListener(new ComponentAdapter()
         {
         public void componentResized(ComponentEvent e)
         {
         SwingUtilities.invokeLater(new Runnable()
         {
         public void run()
         {
         try
         {
         viewer.setPageView(viewer.getPageView());
         }
         catch (Exception ex)
         {
         }
         }
         });
         }
         });*/
        // add TransferHandler to handle drop file
        viewer.setTransferHandler(new TransferHandler() {
            private static final long serialVersionUID = 1L;

            public boolean canImport(JComponent arg0,
                    DataFlavor[] arg1) {
                if (!canDropPDF) {
                    return false;
                }

                for (int i = 0; i < arg1.length; i++) {
                    DataFlavor flavor = arg1[i];
                    if (flavor.equals(DataFlavor.javaFileListFlavor)) {
                        return true;
                    } else if (flavor.equals(DataFlavor.stringFlavor)) {
                        return true;
                    }
                }
                // Didn't find any that match, so:
                return false;
            }

            public boolean importData(JComponent comp, Transferable t) {
                if (!canDropPDF) {
                    return false;
                }

                DataFlavor[] flavors = t.getTransferDataFlavors();
                for (int i = 0; i < flavors.length; i++) {
                    DataFlavor flavor = flavors[i];
                    try {
                        if (flavor
                                .equals(DataFlavor.javaFileListFlavor)) {
                            List l = (List) t
                                    .getTransferData(DataFlavor.javaFileListFlavor);
                            Iterator iter = l.iterator();
                            while (iter.hasNext()) {
                                File file = (File) iter.next();
//                                System.out.println("GOT FILE: "
//                                    + file.getCanonicalPath());
                                // Now do something with the file...

                                if (file.isFile()) {
                                    loadFile(file);
                                    return true;
                                }
                            }
                        } else if (flavor
                                .equals(DataFlavor.stringFlavor)) {
                            String fileOrURL = (String) t
                                    .getTransferData(flavor);
                            try {
                                URL url = new URL(fileOrURL);

                                String urlPath = url.getPath();

                                URI urlAsURI = null;

                                String javaSpecVer = System.getProperty("java.specification.version");

                                float javaSpecVerNum = -1;

                                try {
                                    javaSpecVerNum = Float.parseFloat(javaSpecVer);
                                } catch (Exception ex) {
                                    javaSpecVerNum = -1;
                                }

                                if (javaSpecVerNum != -1 && javaSpecVerNum >= 1.5) {
                                    try {
                                        Method methodToURI = url.getClass().getMethod("toURI", null);

                                        Object objURI = methodToURI.invoke(url, null);

                                        if (objURI != null && objURI instanceof URI) {
                                            urlAsURI = (URI) objURI;
                                        }
                                    } catch (NoSuchMethodException nsmEx) {
                                    } catch (IllegalAccessException iaEx) {
                                    } catch (InvocationTargetException itEx) {
                                    }
                                }

                                if (url.getProtocol().toLowerCase()
                                        .equals("file")) {
                                    File file = null;

                                    if (urlAsURI != null) {
                                        file = new File(urlAsURI);
                                    } else {
                                        file = new File(urlPath);
                                    }

//                                  // System.out.println("GOT FILE: "
//                                  // + file.getCanonicalPath());
//                                  // Now do something with the file...
                                    //
                                    if (file.isFile()) {
                                        loadFile(file);
                                        return true;
                                    }
                                }
                                return true;
                            } catch (MalformedURLException ex) {
                                return false;
                            }
                            // now do something with the String.

                        }
                    } catch (IOException ex) {
                    } catch (UnsupportedFlavorException e) {
                    } catch (SecurityException securityEx) {
                    }
                }
                // If you get here, I didn't like the flavor.
                Toolkit.getDefaultToolkit().beep();
                return false;
            }
        });

        setIconImage(new ImageIcon(getClass().getResource(
                "icons/pdfone_java_24_by_24.gif")).getImage());
        chkMnuShowLabels.setSelected(false);
        processChkMnuShowLabelsAction();

        // set the look and feel specified in the properties file
        try {
            UIManager.setLookAndFeel(lookAndFeelClassName);

            SwingUtilities.updateComponentTreeUI(fc);
            SwingUtilities.updateComponentTreeUI(this);
            SwingUtilities.updateComponentTreeUI(txtGoToPage);
//            SwingUtilities.updateComponentTreeUI(aboutDialog);
            SwingUtilities.updateComponentTreeUI(docInfoDialog);
        } catch (Exception e1) {
        }

        // set the dpi specified in the properties file
        try {
            switch (dpiPropertyVal) {
                case PdfViewer.DPI_72:
                    radioMnuDPI_72.setSelected(true);
                    viewer.setDpi(PdfViewer.DPI_72);
                    break;
                case PdfViewer.DPI_96:
                    radioMnuDPI_96.setSelected(true);
                    viewer.setDpi(PdfViewer.DPI_96);
                    break;
                case PdfViewer.DPI_110:
                    radioMnuDPI_110.setSelected(true);
                    viewer.setDpi(PdfViewer.DPI_110);
                    break;
                default:
                    radioMnuDPI_96.setSelected(true);
                    viewer.setDpi(PdfViewer.DPI_96);
                    break;
            }
        } catch (PdfException pdfex) {
        }
    }

    private void loadUserPreferences() {
        if (jarFilePath.equals("")
                || jarFilePath.trim().length() <= 0) {
            return;
        }

        File propertiesFile = new File(jarFilePath, "UserPreferences.properties");

        if (propertiesFile.exists()
                && propertiesFile.isFile()
                && propertiesFile.canRead()) {
            InputStream inStream = null;

            try {
                inStream = new BufferedInputStream(new FileInputStream(propertiesFile));
                userPreferences = new Properties();
                userPreferences.load(inStream);

                // Read RecentDir key
                String recentDirPath = userPreferences.getProperty("RecentDir");

                if (recentDirPath == null || recentDirPath.trim().equals("")) {
                    recentDirPath = System.getProperty("user.dir");
                }

                File tempFile = new File(recentDirPath);

                if (tempFile.exists() && tempFile.isDirectory()) {
                    recentDir = tempFile;
                } else {
                    recentDir = new File(System.getProperty("user.dir"));
                }

                // Read LookAndFeel key
                String tempLookAndFeelClassName = userPreferences.getProperty("LookAndFeel");

                if (tempLookAndFeelClassName == null
                        || tempLookAndFeelClassName.trim().equals("")) {
                    tempLookAndFeelClassName = UIManager
                            .getCrossPlatformLookAndFeelClassName();
                } else {
                    LookAndFeelInfo[] laf = UIManager
                            .getInstalledLookAndFeels();

                    int i = 0;
                    for (; i < laf.length; i++) {
                        if (laf[i].getClassName().equalsIgnoreCase(
                                tempLookAndFeelClassName)) {
                            break;
                        }
                    }

                    if (i >= laf.length) {
                        tempLookAndFeelClassName = UIManager
                                .getCrossPlatformLookAndFeelClassName();
                    }
                }

                lookAndFeelClassName = tempLookAndFeelClassName;

                // Read DPI key
                String strDpiVal = userPreferences.getProperty("DPI");

                if (strDpiVal == null || strDpiVal.trim().equals("")) {
                    dpiPropertyVal = PdfViewer.DPI_96;
                } else {
                    int tempDpiVal = PdfViewer.DPI_96;

                    try {
                        tempDpiVal = Integer.parseInt(strDpiVal);
                    } catch (NumberFormatException nfe) {
                    }

                    switch (tempDpiVal) {
                        case PdfViewer.DPI_72:
                            dpiPropertyVal = tempDpiVal;
                            break;
                        case PdfViewer.DPI_96:
                            dpiPropertyVal = tempDpiVal;
                            break;
                        case PdfViewer.DPI_110:
                            dpiPropertyVal = tempDpiVal;
                            break;
                        default:
                            dpiPropertyVal = PdfViewer.DPI_96;
                            break;
                    }
                }
            } catch (Exception ex) {
                userPreferences = null;
            } finally {
                if (inStream != null) {
                    try {
                        inStream.close();
                    } catch (IOException e) {
                    }
                }
            }
        } else {
            recentDir = new File(System.getProperty("user.dir"));
            dpiPropertyVal = PdfViewer.DPI_96;
            lookAndFeelClassName = UIManager
                    .getCrossPlatformLookAndFeelClassName();
        }
    }

    private void loadPageDisplaySettings() {
        if (jarFilePath.equals("")
                || jarFilePath.trim().length() <= 0) {
            return;
        }

        File propertiesFile = new File(jarFilePath, "PageDisplaySettings.properties");

        if (propertiesFile.exists()
                && propertiesFile.isFile()
                && propertiesFile.canRead()) {
            try {
                viewer.getPageDisplaySettings().loadSettings(propertiesFile);
            } catch (Exception e) {
            }
        }
    }

    private void saveUserPreferences() {
        if (jarFilePath.equals("")
                || jarFilePath.trim().length() <= 0) {
            return;
        }

        File propertiesFile = new File(jarFilePath, "UserPreferences.properties");

        if (userPreferences == null) {
            userPreferences = new Properties();
        }

        // put all changed settings/values to userPreferences Properties file
        userPreferences.setProperty("RecentDir", recentDir.getAbsolutePath());
        userPreferences.setProperty("LookAndFeel", lookAndFeelClassName);
        userPreferences.setProperty("DPI", String.valueOf(dpiPropertyVal));

        if (!propertiesFile.exists()
                || (propertiesFile.isFile()
                && propertiesFile.canWrite())) {
            OutputStream outStream = null;

            try {
                outStream = new BufferedOutputStream(new FileOutputStream(propertiesFile));

                userPreferences.store(outStream, "Supreme Today");

            } catch (Exception ex) {
                propertiesFile.delete();
            } finally {
                if (outStream != null) {
                    try {
                        outStream.close();
                    } catch (IOException e) {
                    }
                }
            }
        }
    }

    private void savePageDisplaySettings() {
        if (jarFilePath.equals("")
                || jarFilePath.trim().length() <= 0) {
            return;
        }

        File propertiesFile = new File(jarFilePath, "PageDisplaySettings.properties");
        try {
            viewer.getPageDisplaySettings().saveSettings(propertiesFile);
        } catch (Exception e) {
        }

    }

    private JToolBar getStatusBar() {
        progress = new JProgressBar();
        progress.setStringPainted(true);
//        progress.setIndeterminate(true);
        progress.setString("Loading...");
//        progress.setPreferredSize(new Dimension(200, 20));

        statusBar = new JToolBar("StatusBar", JToolBar.HORIZONTAL);
        statusBar.setLayout(new GridLayout(1, 5, 20, 0));
        statusBar.setFloatable(false);

        int preferredWidth = 150;
        int preferredHeight = 25;
        statusBar.setSize(preferredWidth, preferredHeight);
        statusBar.setBounds(0, 0, 100, 25);
        statusBar.setPreferredSize(new Dimension(preferredWidth, preferredHeight));
        statusBar.setBorder(BorderFactory
                .createLoweredBevelBorder());

        statusBar.add(progress, -1);
        statusBar.setOpaque(false);

//        gbc.gridx = 1;
//        gbc.gridy = 0;
//        gbc.gridwidth = 4;
//        gbc.weightx = 4.0;
//        gbc.anchor = GridBagConstraints.CENTER;
//        gbc.fill = GridBagConstraints.HORIZONTAL;
        statusBar.add(new JPanel(), -1);
        statusBar.add(new JPanel(), -1);
        statusBar.add(new JPanel(), -1);
        statusBar.add(new JPanel(), -1);

        progress.setVisible(false);

        return statusBar;
    }

//    private JComponent getViewerandOutlinePanel()
//    {        
//        outlinesTabbedPane = new JTabbedPane();
//       
//        viewerSplitPane = new JSplitPane(
//            JSplitPane.HORIZONTAL_SPLIT, outlinesTabbedPane , viewer);
//        viewerSplitPane.setBackground(Color.GRAY);
//        viewerSplitPane.setOneTouchExpandable(true);
//
//        return viewerSplitPane;            
//    }
    /*private JPanel getAllTopToolBars()
     {
     JPanel toolBarsPanel = new JPanel();
     GridBagLayout tb_gbl = new GridBagLayout();
     toolBarsPanel.setLayout(tb_gbl);
        
     GridBagConstraints gbc = new GridBagConstraints();
        
     gbc.gridx = 0;
     gbc.gridy = 0;
     gbc.weightx = 1.0;
     gbc.gridwidth = 1;
     gbc.fill = GridBagConstraints.BOTH;
     gbc.anchor = GridBagConstraints.WEST;
     toolBarsPanel.add(getBannerToolBar(), gbc);
        
     gbc.gridx = 0;
     gbc.gridy = 1;
     gbc.weightx = 1.0;
     gbc.gridwidth = 1;
     gbc.fill = GridBagConstraints.BOTH;
     gbc.anchor = GridBagConstraints.WEST;
     toolBarsPanel.add(getTopToolBar(), gbc);
        
     return toolBarsPanel;
     }*/
    private JToolBar getTopToolBar() {
        ImageIcon loadIcon = new ImageIcon(getClass().getResource(
                "icons/Open.gif"));
        btnLoad = new JButton(loadIcon);
        btnLoad.setToolTipText("Open");
        btnLoad.addActionListener(this);

        ImageIcon printIcon = new ImageIcon(getClass().getResource(
                "icons/Print.gif"));
        btnPrint = new JButton(printIcon);
        btnPrint.setToolTipText("Print");
        btnPrint.addActionListener(this);

        /*ImageIcon docInfoIcon = new ImageIcon(getClass().getResource(
         "icons/DocInfo.gif"));
         btnDocInfo = new JButton(docInfoIcon);
         btnDocInfo.setToolTipText("Document Properties");
         btnDocInfo.addActionListener(this);
         btnDocInfo.setEnabled(false);*/
        ImageIcon actualSizeIcon = new ImageIcon(getClass()
                .getResource("icons/ActualSize.gif"));
        btnActualSize = new JToggleButton("Actual Size",
                actualSizeIcon);
        btnActualSize.setToolTipText("Actual Size");
        btnActualSize.addActionListener(this);

        ImageIcon fitPageIcon = new ImageIcon(getClass().getResource(
                "icons/FitPage.gif"));
        btnFitPage = new JToggleButton("Fit Page", fitPageIcon);
        btnFitPage.setToolTipText("Fit Page");
        btnFitPage.addActionListener(this);

        ImageIcon fitWidthIcon = new ImageIcon(getClass()
                .getResource("icons/FitWidth.gif"));
        btnFitWidth = new JToggleButton("Fit Width", fitWidthIcon);
        btnFitWidth.setToolTipText("Fit Width");
        btnFitWidth.addActionListener(this);

        ImageIcon zoomOutIcon = new ImageIcon(getClass().getResource(
                "icons/ZoomOut.gif"));
        btnZoomOut = new JButton("Zoom Out", zoomOutIcon);
        btnZoomOut.setToolTipText("Zoom Out");
        btnZoomOut.addActionListener(this);

        ImageIcon zoomInIcon = new ImageIcon(getClass().getResource(
                "icons/ZoomIn.gif"));
        btnZoomIn = new JButton("Zoom In", zoomInIcon);
        btnZoomIn.setToolTipText("Zoom In");
        btnZoomIn.addActionListener(this);

        cboZoomFactor = new JComboBox();
        cboZoomFactor.setToolTipText("Zoom");
        cboZoomFactor.setMinimumSize(new Dimension(100, 25));
        cboZoomFactor.setSize(new Dimension(100, 25));
        cboZoomFactor.setPreferredSize(new Dimension(100, 25));
//        cboZoomFactor.setMaximumSize(new Dimension(10, 10));
        cboZoomFactor.addItem("6400%");
        cboZoomFactor.addItem("3200%");
        cboZoomFactor.addItem("2400%");
        cboZoomFactor.addItem("1600%");
        cboZoomFactor.addItem("1200%");
        cboZoomFactor.addItem("800%");
        cboZoomFactor.addItem("600%");
        cboZoomFactor.addItem("400%");
        cboZoomFactor.addItem("300%");
        cboZoomFactor.addItem("200%");
        cboZoomFactor.addItem("150%");
        cboZoomFactor.addItem("125%");
        cboZoomFactor.addItem("100%");
        cboZoomFactor.addItem("75%");
        cboZoomFactor.addItem("66.67%");
        cboZoomFactor.addItem("50%");
        cboZoomFactor.addItem("33.33%");
        cboZoomFactor.addItem("25%");
        cboZoomFactor.addItem("12.5%");
        cboZoomFactor.addItem("8.33%");
        cboZoomFactor.addItem("6.25%");
        cboZoomFactor.addItem("1%");
        cboZoomFactor.addItem("0.5%");
        cboZoomFactor.addItem("0.25%");
        cboZoomFactor.addItem("0.01%");

        cboZoomFactor.setEditable(true);
        cboZoomFactor.setSelectedItem("100%");
        currentZoomFactor = 100.0;
        cboZoomFactor.addItemListener(this);

        ImageIcon antiClockwiseIcon = new ImageIcon(getClass()
                .getResource("icons/CounterClockwise.gif"));
        btnRotateCounterClockwise = new JButton(
                "Rotate Counterclockwise", antiClockwiseIcon);
        btnRotateCounterClockwise
                .setToolTipText("Rotate Counterclockwise");
        btnRotateCounterClockwise.addActionListener(this);

        ImageIcon clockwiseIcon = new ImageIcon(getClass()
                .getResource("icons/Clockwise.gif"));
        btnRotateClockwise = new JButton("Rotate Clockwise",
                clockwiseIcon);
        btnRotateClockwise.setToolTipText("Rotate Clockwise");
        btnRotateClockwise.addActionListener(this);

        ImageIcon firstPageIcon = new ImageIcon(getClass().getResource("icons/FirstPage.gif"));
        btnFirstPage = new JButton(firstPageIcon);
        btnFirstPage.setToolTipText("First Page");
        btnFirstPage.addActionListener(this);

        ImageIcon previousPageIcon = new ImageIcon(getClass()
                .getResource("icons/PreviousPage.gif"));
        btnPreviousPage = new JButton(previousPageIcon);
        btnPreviousPage.setToolTipText("Previous Page");
        btnPreviousPage.addActionListener(this);

        txtGoToPage = new JTextField(10);
        txtGoToPage.setMinimumSize(new Dimension(100, 25));
        txtGoToPage.setToolTipText("Go to Page");
        txtGoToPage.setHorizontalAlignment(JTextField.CENTER);
        txtGoToPage.setSize(100, 25);
        txtGoToPage.setPreferredSize(new Dimension(100, 25));
        txtGoToPage.addActionListener(this);
        txtGoToPage.addFocusListener(new FocusAdapter() {
            public void focusGained(FocusEvent fe) {
                txtGoToPage.setSelectionStart(0);
                txtGoToPage.setSelectionEnd(txtGoToPage.getText().length());
            }
        });

        ImageIcon nextPageIcon = new ImageIcon(getClass()
                .getResource("icons/NextPage.gif"));
        btnNextPage = new JButton(nextPageIcon);
        btnNextPage.setToolTipText("Next Page");
        btnNextPage.addActionListener(this);

        ImageIcon lastPageIcon = new ImageIcon(getClass()
                .getResource("icons/LastPage.gif"));
        btnLastPage = new JButton(lastPageIcon);
        btnLastPage.setToolTipText("Last Page");
        btnLastPage.addActionListener(this);

        btnFirstPage.setEnabled(false);
        btnPreviousPage.setEnabled(false);
        txtGoToPage.setEnabled(false);
        btnNextPage.setEnabled(false);
        btnLastPage.setEnabled(false);

        JPanel pageLayoutPanel = new JPanel();
        pageLayoutPanel.setLayout(new FlowLayout());

        ImageIcon continuousLayoutIcon = new ImageIcon(getClass().getResource(
                "icons/ContinuousLayout.gif"));
        btnContinuous = new JToggleButton("Continuous Layout", continuousLayoutIcon);
        btnContinuous.setToolTipText("Continuous Layout");
        btnContinuous.addActionListener(this);

        ImageIcon singlePageLayoutIcon = new ImageIcon(getClass()
                .getResource("icons/SinglePageLayout.gif"));
        btnSinglePage = new JToggleButton("Single Page Layout",
                singlePageLayoutIcon);
        btnSinglePage.setToolTipText("Single Page Layout");
        btnSinglePage.addActionListener(this);

        ImageIcon sideBySideIcon = new ImageIcon(getClass()
                .getResource("icons/SideBySideLayout.gif"));
        btnSideBySide = new JToggleButton("Side-by-Side Layout",
                sideBySideIcon);
        btnSideBySide.setToolTipText("Side-by-Side Layout");
        btnSideBySide.addActionListener(this);

        ImageIcon sideBySideContinuousIcon = new ImageIcon(getClass()
                .getResource("icons/SideBySideLayoutContinuous.gif"));
        btnSideBySideContinuous = new JToggleButton("Side-by-Side Continuous Layout",
                sideBySideContinuousIcon);
        btnSideBySideContinuous.setToolTipText("Side-by-Side Continuous Layoutt");
        btnSideBySideContinuous.addActionListener(this);

        ImageIcon autoFitColumnsIcon = new ImageIcon(getClass()
                .getResource("icons/AutoFitInWindow.gif"));
        btnAutoFitColumnsInWindow = new JToggleButton("Auto Fit Columns in Window",
                autoFitColumnsIcon);
        btnAutoFitColumnsInWindow.setToolTipText("Auto Fit Columns in Window");
        btnAutoFitColumnsInWindow.addActionListener(this);

        ImageIcon customLayoutIcon = new ImageIcon(getClass()
                .getResource("icons/CustomLayout.gif"));
        btnCustomLayout = new JToggleButton("Custom Layout",
                customLayoutIcon);
        btnCustomLayout.setToolTipText("Custom Layout");
        btnCustomLayout.addActionListener(this);

        txtCustomColumnCount = new JTextField(3);
//        txtCustomColumnCount.setMinimumSize(new Dimension(20, 25));
        txtCustomColumnCount.setToolTipText("Number of page columns to be shown");
        txtCustomColumnCount.setHorizontalAlignment(JTextField.CENTER);
//        txtCustomColumnCount.setSize(20, 25);
//        txtCustomColumnCount.setPreferredSize(new Dimension(20, 25));
        txtCustomColumnCount.addActionListener(this);
        txtCustomColumnCount.addFocusListener(new FocusAdapter() {
            public void focusGained(FocusEvent fe) {
                txtCustomColumnCount.setSelectionStart(0);
                txtCustomColumnCount.setSelectionEnd(txtCustomColumnCount.getText().length());
            }
        });

        txtCustomColumnCount.addKeyListener(new KeyAdapter() {
            public void keyTyped(KeyEvent e) {
                if (!Character.isDigit(e.getKeyChar())) {
                    e.consume();
                }

            }
        });

// pageLayoutPanel.add(btnContinuous);
// pageLayoutPanel.add(btnSinglePage);
// pageLayoutPanel.add(btnSideBySide);
// pageLayoutPanel.add(btnSideBySideContinuous);
//        pageLayoutPanel.add(btnAutoFitColumnsInWindow);
//        pageLayoutPanel.add(btnCustomLayout);
//        Create a horizontal toolbar
        JToolBar topToolbar = new JToolBar("Tools", JToolBar.HORIZONTAL);
        topToolbar.setFloatable(false);

        // topToolbar with FlowLayout
        FlowLayout flowLayout = new FlowLayout(FlowLayout.LEFT);
        topToolbar.setLayout(flowLayout);

        topToolbar.add(btnLoad);
        topToolbar.add(btnPrint);
//        topToolbar.addSeparator();
//        topToolbar.add(btnDocInfo);

        topToolbar.addSeparator();
        topToolbar.add(btnFirstPage);
        topToolbar.add(btnPreviousPage);
        topToolbar.add(txtGoToPage);
        topToolbar.add(btnNextPage);
        topToolbar.add(btnLastPage);

        topToolbar.addSeparator();
        topToolbar.add(btnActualSize);
        topToolbar.add(btnFitPage);
        topToolbar.add(btnFitWidth);

        topToolbar.addSeparator();
        topToolbar.add(btnZoomOut);
        topToolbar.add(cboZoomFactor);
        topToolbar.add(btnZoomIn);

        topToolbar.addSeparator();
        topToolbar.add(btnRotateCounterClockwise);
        topToolbar.add(btnRotateClockwise);

        topToolbar.addSeparator();
        topToolbar.add(btnSinglePage);
        topToolbar.add(btnContinuous);
        topToolbar.add(btnSideBySide);
        topToolbar.add(btnSideBySideContinuous);
        topToolbar.add(btnAutoFitColumnsInWindow);
        topToolbar.add(btnCustomLayout);

        topToolbar.addSeparator();
        topToolbar.add(txtCustomColumnCount);

        // topToolbar with GridBagLayout
        /*topToolbar.setLayout(new GridBagLayout());
        
         GridBagConstraints gbc = new GridBagConstraints();
        
         gbc.gridx = 0;
         gbc.gridy = 0;
         gbc.weightx = 1.0;
         gbc.anchor = GridBagConstraints.CENTER;
         gbc.fill = GridBagConstraints.BOTH;
         topToolbar.add(btnLoad, gbc);
        
         gbc.gridx = 1;
         gbc.weightx = 1.0;
         gbc.fill = GridBagConstraints.BOTH;
         topToolbar.add(btnPrint, gbc);
        
         gbc.gridx = 2;
         gbc.fill = GridBagConstraints.NONE;
         topToolbar.add(new JToolBar.Separator(null), gbc);
        
         gbc.gridx = 3;
         gbc.fill = GridBagConstraints.BOTH;
         topToolbar.add(btnFirstPage, gbc);
        
         gbc.gridx = 4;
         topToolbar.add(btnPreviousPage, gbc);
        
        
         gbc.gridx = 5;
         topToolbar.add(txtGoToPage, gbc);
        
         gbc.gridx = 6;
         topToolbar.add(btnNextPage, gbc);
        
         gbc.gridx = 7;
         topToolbar.add(btnLastPage, gbc);
        
         gbc.gridx = 8;
         gbc.fill = GridBagConstraints.NONE;
         topToolbar.add(new JToolBar.Separator(null), gbc);
        
         gbc.gridx = 9;
         gbc.fill = GridBagConstraints.BOTH;
         topToolbar.add(btnActualSize, gbc);
        
         gbc.gridx = 10;
         topToolbar.add(btnFitPage, gbc);
        
         gbc.gridx = 11;
         topToolbar.add(btnFitWidth, gbc);
        
         gbc.gridx = 12;
         gbc.fill = GridBagConstraints.NONE;
         topToolbar.add(new JToolBar.Separator(null), gbc);
        
         gbc.gridx = 13;
         gbc.fill = GridBagConstraints.BOTH;
         topToolbar.add(btnZoomOut, gbc);
        
         gbc.gridx = 14;
         topToolbar.add(cboZoomFactor, gbc);
        
         gbc.gridx = 15;
         topToolbar.add(btnZoomIn, gbc);
        
         gbc.gridx = 16;
         gbc.fill = GridBagConstraints.NONE;
         topToolbar.add(new JToolBar.Separator(null), gbc);
        
         gbc.gridx = 17;
         gbc.fill = GridBagConstraints.BOTH;
         topToolbar.add(btnRotateCounterClockwise, gbc);
        
         gbc.gridx = 18;
         topToolbar.add(btnRotateClockwise, gbc);
        
         gbc.gridx = 19;
         gbc.fill = GridBagConstraints.NONE;
         topToolbar.add(new JToolBar.Separator(null), gbc);
        
         gbc.gridx = 20;
         gbc.fill = GridBagConstraints.BOTH;
         topToolbar.add(GnosticePDFOneBanner
         .getPoweredByURILabel(getClass().getResource(
         "icons/pdfone_java_pro_24_by_24.gif")), gbc);*/
        btnActualSize.setEnabled(false);
        btnFitPage.setEnabled(false);
        btnFitWidth.setEnabled(false);
        btnZoomOut.setEnabled(false);
        cboZoomFactor.setEnabled(false);
        btnZoomIn.setEnabled(false);
        btnRotateCounterClockwise.setEnabled(false);
        btnRotateClockwise.setEnabled(false);

        btnContinuous.setEnabled(false);
        btnSinglePage.setEnabled(false);
        btnSideBySide.setEnabled(false);
        btnSideBySideContinuous.setEnabled(false);
        btnAutoFitColumnsInWindow.setEnabled(false);
        btnCustomLayout.setEnabled(false);
        txtCustomColumnCount.setEnabled(false);

        // By default set the Actual Size fit mode as selected
        btnActualSize.setSelected(true);

        return topToolbar;
    }

    private void createMenus() {
        JMenuBar menuBar = new JMenuBar();

        // File Menu
        JMenu fileMenu = new JMenu("File");
        fileMenu.setMnemonic('F');

        mnuOpenItem = new JMenuItem("Open PDF...", 'O');
        mnuOpenItem.setAccelerator(KeyStroke.getKeyStroke(
                KeyEvent.VK_O, InputEvent.CTRL_MASK));
        mnuOpenItem.addActionListener(this);

        mnuPrintItem = new JMenuItem("Print...", 'P');
        mnuPrintItem.setAccelerator(KeyStroke.getKeyStroke(
                KeyEvent.VK_P, InputEvent.CTRL_MASK));
        mnuPrintItem.addActionListener(this);

//        mnuDocInfo = new JMenuItem("Document Properties", 'D');
//        mnuDocInfo.setAccelerator(KeyStroke.getKeyStroke(
//                KeyEvent.VK_D, InputEvent.CTRL_MASK));
//        mnuDocInfo.addActionListener(this);
//        mnuDocInfo.setEnabled(false);

        mnuCloseItem = new JMenuItem("Close", 'C');
        mnuCloseItem.setAccelerator(KeyStroke.getKeyStroke(
                KeyEvent.VK_W, InputEvent.CTRL_MASK));
        mnuCloseItem.addActionListener(this);
        mnuCloseItem.setEnabled(false);

        mnuExitItem = new JMenuItem("Exit", 'E');
        mnuExitItem.setAccelerator(KeyStroke.getKeyStroke(
                KeyEvent.VK_Q, InputEvent.CTRL_MASK));
        mnuExitItem.addActionListener(this);

        fileMenu.add(mnuOpenItem);
        fileMenu.add(mnuPrintItem);
//        fileMenu.add(mnuDocInfo);
        fileMenu.add(new JSeparator());
        fileMenu.add(mnuCloseItem);
        fileMenu.add(new JSeparator());
        fileMenu.add(mnuExitItem);

        menuBar.add(fileMenu);

        // Edit Menu
        JMenu editMenu = new JMenu("Edit");
        editMenu.setMnemonic('t');

        mnuShowPageDisplaySettingsDialog = new JMenuItem("Page Display Settings", 'S');
        mnuShowPageDisplaySettingsDialog.setAccelerator(KeyStroke.getKeyStroke(
                KeyEvent.VK_G, InputEvent.CTRL_MASK));
        mnuShowPageDisplaySettingsDialog.addActionListener(this);

        editMenu.add(mnuShowPageDisplaySettingsDialog);
        menuBar.add(editMenu);

        // View Menu
        JMenu viewMenu = new JMenu("View");
        viewMenu.setMnemonic('V');

        mnuFirstPageItem = new JMenuItem("First Page", 'F');
        mnuFirstPageItem.setAccelerator(KeyStroke.getKeyStroke(
                KeyEvent.VK_HOME, InputEvent.CTRL_DOWN_MASK
                | InputEvent.SHIFT_DOWN_MASK));
        mnuFirstPageItem.addActionListener(this);

        mnuPreviousPageItem = new JMenuItem("Previous Page", 'P');
        mnuPreviousPageItem.setAccelerator(KeyStroke.getKeyStroke(
                KeyEvent.VK_PAGE_UP, InputEvent.CTRL_DOWN_MASK
                | InputEvent.SHIFT_DOWN_MASK));
        mnuPreviousPageItem.addActionListener(this);

        mnuNextPageItem = new JMenuItem("Next Page", 'N');
        mnuNextPageItem.setAccelerator(KeyStroke.getKeyStroke(
                KeyEvent.VK_PAGE_DOWN, InputEvent.CTRL_DOWN_MASK
                | InputEvent.SHIFT_DOWN_MASK));
        mnuNextPageItem.addActionListener(this);

        mnuLastPageItem = new JMenuItem("Last Page", 'L');
        mnuLastPageItem.setAccelerator(KeyStroke.getKeyStroke(
                KeyEvent.VK_END, InputEvent.CTRL_DOWN_MASK
                | InputEvent.SHIFT_DOWN_MASK));
        mnuLastPageItem.addActionListener(this);

        mnuPreviousView = new JMenuItem("Previous View", 'V');
        mnuPreviousView.setAccelerator(KeyStroke.getKeyStroke(
                KeyEvent.VK_LEFT, InputEvent.ALT_DOWN_MASK));
        mnuPreviousView.addActionListener(this);

        mnuNextView = new JMenuItem("Next View", 'w');
        mnuNextView.setAccelerator(KeyStroke.getKeyStroke(
                KeyEvent.VK_RIGHT, InputEvent.ALT_DOWN_MASK));
        mnuNextView.addActionListener(this);

        // add all items for Go To subMenu
        JMenu viewGotoSubMenu = new JMenu("Go To");
        viewGotoSubMenu.setMnemonic('G');
        viewGotoSubMenu.add(mnuFirstPageItem);
        viewGotoSubMenu.add(mnuPreviousPageItem);
        viewGotoSubMenu.add(mnuNextPageItem);
        viewGotoSubMenu.add(mnuLastPageItem);
        viewGotoSubMenu.add(new JSeparator());
        viewGotoSubMenu.add(mnuPreviousView);
        viewGotoSubMenu.add(mnuNextView);

        mnuZoomInItem = new JMenuItem("Zoom In", 'I');
        mnuZoomInItem.setAccelerator(KeyStroke.getKeyStroke(
                KeyEvent.VK_EQUALS, InputEvent.CTRL_MASK));
        mnuZoomInItem.addActionListener(this);

        mnuZoomOutItem = new JMenuItem("Zoom Out", 'O');
        mnuZoomOutItem.setAccelerator(KeyStroke.getKeyStroke(
                KeyEvent.VK_MINUS, InputEvent.CTRL_MASK));
        mnuZoomOutItem.addActionListener(this);

        mnuActualSizeItem = new JMenuItem("Actual Size", 'A');
        mnuActualSizeItem.setAccelerator(KeyStroke.getKeyStroke(
                KeyEvent.VK_NUMPAD1, InputEvent.CTRL_MASK));
        mnuActualSizeItem.addActionListener(this);

        mnuFitPageItem = new JMenuItem("Fit Page", 'i');
        mnuFitPageItem.setAccelerator(KeyStroke.getKeyStroke(
                KeyEvent.VK_NUMPAD2, InputEvent.CTRL_MASK));
        mnuFitPageItem.addActionListener(this);

        mnuFitWidthItem = new JMenuItem("Fit Width", 'W');
        mnuFitWidthItem.setAccelerator(KeyStroke.getKeyStroke(
                KeyEvent.VK_NUMPAD3, InputEvent.CTRL_MASK));
        mnuFitWidthItem.addActionListener(this);

        JMenu viewZoomSubMenu = new JMenu("Zoom");
        viewZoomSubMenu.setMnemonic('Z');
        viewZoomSubMenu.add(mnuZoomInItem);
        viewZoomSubMenu.add(mnuZoomOutItem);
        viewZoomSubMenu.add(new JSeparator());
        viewZoomSubMenu.add(mnuActualSizeItem);
        viewZoomSubMenu.add(mnuFitPageItem);
        viewZoomSubMenu.add(mnuFitWidthItem);

        mnuRotateAntiClokwiseItem = new JMenuItem(
                "Rotate AntiClockwise", 'A');
        mnuRotateAntiClokwiseItem.setAccelerator(KeyStroke
                .getKeyStroke(KeyEvent.VK_LEFT, InputEvent.CTRL_MASK));
        mnuRotateAntiClokwiseItem.addActionListener(this);

        mnuRotateClokwiseItem = new JMenuItem("Rotate Clockwise", 'C');
        mnuRotateClokwiseItem.setAccelerator(KeyStroke.getKeyStroke(
                KeyEvent.VK_RIGHT, InputEvent.CTRL_MASK));
        mnuRotateClokwiseItem.addActionListener(this);

        JMenu viewRotateViewSubMenu = new JMenu("Rotate View");
        viewRotateViewSubMenu.setMnemonic('R');
        viewRotateViewSubMenu.add(mnuRotateAntiClokwiseItem);
        viewRotateViewSubMenu.add(mnuRotateClokwiseItem);

        // Create menu items for Page Layouts
        // Single Page menu item
        radioMnuLayoutSinglePage = new JRadioButtonMenuItem("Single Page");
        radioMnuLayoutSinglePage.setMnemonic('S');
        radioMnuLayoutSinglePage.setAccelerator(KeyStroke.getKeyStroke(
                KeyEvent.VK_NUMPAD1, InputEvent.CTRL_DOWN_MASK
                | InputEvent.ALT_DOWN_MASK));
        radioMnuLayoutSinglePage.addActionListener(this);

        // Single Page Continuous menu item
        radioMnuLayoutSinglePageContinuous = new JRadioButtonMenuItem("Single Page Continuous");
        radioMnuLayoutSinglePageContinuous.setMnemonic('C');
        radioMnuLayoutSinglePageContinuous.setAccelerator(KeyStroke.getKeyStroke(
                KeyEvent.VK_NUMPAD2, InputEvent.CTRL_DOWN_MASK
                | InputEvent.ALT_DOWN_MASK));
        radioMnuLayoutSinglePageContinuous.addActionListener(this);

        // Side By Side
        radioMnuLayoutSideBySide = new JRadioButtonMenuItem("Side By Side");
        radioMnuLayoutSideBySide.setMnemonic('B');
        radioMnuLayoutSideBySide.setAccelerator(KeyStroke.getKeyStroke(
                KeyEvent.VK_NUMPAD3, InputEvent.CTRL_DOWN_MASK
                | InputEvent.ALT_DOWN_MASK));
        radioMnuLayoutSideBySide.addActionListener(this);

        // Side By Side Continuous
        radioMnuLayoutSideBySideContinuous = new JRadioButtonMenuItem("Side By Side Continuous");
        radioMnuLayoutSideBySideContinuous.setMnemonic('y');
        radioMnuLayoutSideBySideContinuous.setAccelerator(KeyStroke.getKeyStroke(
                KeyEvent.VK_NUMPAD4, InputEvent.CTRL_DOWN_MASK
                | InputEvent.ALT_DOWN_MASK));
        radioMnuLayoutSideBySideContinuous.addActionListener(this);

        // AutoFitColumnsInWindow menu item
        radioMnuLayoutAutoFitColumnsInWindow = new JRadioButtonMenuItem("Auto Fit Columns In Window");
        radioMnuLayoutAutoFitColumnsInWindow.setMnemonic('F');
        radioMnuLayoutAutoFitColumnsInWindow.setAccelerator(KeyStroke.getKeyStroke(
                KeyEvent.VK_NUMPAD5, InputEvent.CTRL_DOWN_MASK
                | InputEvent.ALT_DOWN_MASK));
        radioMnuLayoutAutoFitColumnsInWindow.addActionListener(this);

        // User Defined Columns menu item
        radioMnuLayoutUserDefined = new JRadioButtonMenuItem("User Defined Columns");
        radioMnuLayoutUserDefined.setMnemonic('U');
        radioMnuLayoutUserDefined.setAccelerator(KeyStroke.getKeyStroke(
                KeyEvent.VK_NUMPAD6, InputEvent.CTRL_DOWN_MASK
                | InputEvent.ALT_DOWN_MASK));
        radioMnuLayoutUserDefined.addActionListener(this);

        ButtonGroup grpPageLayout = new ButtonGroup();
        grpPageLayout.add(radioMnuLayoutSinglePage);
        grpPageLayout.add(radioMnuLayoutSinglePageContinuous);
        grpPageLayout.add(radioMnuLayoutSideBySide);
        grpPageLayout.add(radioMnuLayoutSideBySideContinuous);
        grpPageLayout.add(radioMnuLayoutAutoFitColumnsInWindow);
        grpPageLayout.add(radioMnuLayoutUserDefined);

        chkMnuShowGapsBetweenPages = new JCheckBoxMenuItem("Show Gaps between pages");
        chkMnuShowGapsBetweenPages.addActionListener(this);

        chkMnuShowPageBorders = new JCheckBoxMenuItem("Show page borders when no pages gaps are set");
        chkMnuShowPageBorders.addActionListener(this);

        chkMnuShowCoverPageDuringSideBySide = new JCheckBoxMenuItem("Show cover page during Side-By-Side");
        chkMnuShowCoverPageDuringSideBySide.addActionListener(this);

        chkMnuShowPageNumberLabelWhileTracking = new JCheckBoxMenuItem("Show Page Number Label on Scrolling");
        chkMnuShowPageNumberLabelWhileTracking.addActionListener(this);

        chkMnuShowPageImageWhileTracking = new JCheckBoxMenuItem("Show Page Preview On Scrolling during Single-Page");
        chkMnuShowPageImageWhileTracking.addActionListener(this);

        JMenu viewPageLayoutSubMenu = new JMenu("Page Layout");
        viewPageLayoutSubMenu.setMnemonic('L');
        viewPageLayoutSubMenu.add(radioMnuLayoutSinglePage);
        viewPageLayoutSubMenu.add(radioMnuLayoutSinglePageContinuous);
        viewPageLayoutSubMenu.add(radioMnuLayoutSideBySide);
        viewPageLayoutSubMenu.add(radioMnuLayoutSideBySideContinuous);
        viewPageLayoutSubMenu.add(radioMnuLayoutAutoFitColumnsInWindow);
        viewPageLayoutSubMenu.add(radioMnuLayoutUserDefined);
        viewPageLayoutSubMenu.add(new JSeparator());
        viewPageLayoutSubMenu.add(chkMnuShowGapsBetweenPages);
        viewPageLayoutSubMenu.add(chkMnuShowPageBorders);
        viewPageLayoutSubMenu.add(chkMnuShowCoverPageDuringSideBySide);
        viewPageLayoutSubMenu.add(new JSeparator());
        viewPageLayoutSubMenu.add(chkMnuShowPageNumberLabelWhileTracking);
        viewPageLayoutSubMenu.add(chkMnuShowPageImageWhileTracking);

//      Scrolling Mode Submenu
        JMenu scrollModeMenu = new JMenu("Scrolling Mode");
        scrollModeMenu.setMnemonic('S');

        ButtonGroup grpScrollMode = new ButtonGroup();
        radioMnuScrollMode_BLIT_SCROLL_MODE = new JRadioButtonMenuItem("Performance");
        radioMnuScrollMode_BLIT_SCROLL_MODE.addActionListener(this);

        radioMnuScrollMode_BACKINGSTORE_SCROLL_MODE = new JRadioButtonMenuItem("Smooth");
        radioMnuScrollMode_BACKINGSTORE_SCROLL_MODE.addActionListener(this);

        grpScrollMode.add(radioMnuScrollMode_BLIT_SCROLL_MODE);
        grpScrollMode.add(radioMnuScrollMode_BACKINGSTORE_SCROLL_MODE);

        scrollModeMenu.add(radioMnuScrollMode_BLIT_SCROLL_MODE);
        scrollModeMenu.add(radioMnuScrollMode_BACKINGSTORE_SCROLL_MODE);

        ButtonGroup grpDPI = new ButtonGroup();
        radioMnuDPI_72 = new JRadioButtonMenuItem("72 DPI");
        radioMnuDPI_72.addActionListener(this);

        radioMnuDPI_96 = new JRadioButtonMenuItem("96 DPI");
        radioMnuDPI_96.setSelected(true);
        radioMnuDPI_96.addActionListener(this);

        radioMnuDPI_110 = new JRadioButtonMenuItem("110 DPI");
        radioMnuDPI_110.addActionListener(this);

        grpDPI.add(radioMnuDPI_72);
        grpDPI.add(radioMnuDPI_96);
        grpDPI.add(radioMnuDPI_110);

        JMenu viewDPISubMenu = new JMenu("Scale To DPI");
        viewDPISubMenu.setMnemonic('D');
        viewDPISubMenu.add(radioMnuDPI_72);
        viewDPISubMenu.add(radioMnuDPI_96);
        viewDPISubMenu.add(radioMnuDPI_110);

        chkMnuShowLabels = new JCheckBoxMenuItem("Show Labels");
        chkMnuShowLabels.addActionListener(this);

        mnuFirstPageItem.setEnabled(false);
        mnuPreviousPageItem.setEnabled(false);
        mnuNextPageItem.setEnabled(false);
        mnuLastPageItem.setEnabled(false);

        mnuPreviousView.setEnabled(false);
        mnuNextView.setEnabled(false);

        radioMnuLayoutSinglePage.setEnabled(false);
        radioMnuLayoutSinglePageContinuous.setEnabled(false);
        radioMnuLayoutSideBySide.setEnabled(false);
        radioMnuLayoutSideBySideContinuous.setEnabled(false);
        radioMnuLayoutAutoFitColumnsInWindow.setEnabled(false);
        radioMnuLayoutUserDefined.setEnabled(false);

        viewMenu.add(viewGotoSubMenu);
        viewMenu.add(new JSeparator());
        viewMenu.add(viewZoomSubMenu);
        viewMenu.add(viewPageLayoutSubMenu);
        viewMenu.add(viewRotateViewSubMenu);
        viewMenu.add(new JSeparator());
        viewMenu.add(viewDPISubMenu);
//        viewMenu.add(chkMnuShowLabels);
        viewMenu.add(new JSeparator());
        viewMenu.add(scrollModeMenu);

        menuBar.add(viewMenu);

        JMenu export = new JMenu("Export");
        export.setMnemonic('E');

        mnuItemExportAsImage = new JMenuItem("PDF As Image");
        mnuItemExportAsImage.addActionListener(this);
        mnuItemExportAsImage.setEnabled(false);
        mnuItemExportAsImage.setMnemonic('I');
        mnuItemExportAsImage.setAccelerator(KeyStroke.getKeyStroke(
                KeyEvent.VK_E, InputEvent.CTRL_MASK));
        export.add(mnuItemExportAsImage);
        menuBar.add(export);

        // Look and Feels
        JMenu mnuLAF = new JMenu("Look And Feel");
        mnuLAF.setMnemonic('L');

        LookAndFeelInfo[] lookAndFeels = UIManager.getInstalledLookAndFeels();
        ButtonGroup radioGroup = new ButtonGroup();
        mnuLAFItems = new JRadioButtonMenuItem[lookAndFeels.length];

        final JFrame thisFrame = this;
        for (int i = 0; i < lookAndFeels.length; i++) {
            mnuLAFItems[i] = new JRadioButtonMenuItem(lookAndFeels[i].getName());
            mnuLAFItems[i].setActionCommand(lookAndFeels[i].getClassName());
            mnuLAFItems[i].addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent ae) {
                    if (((JRadioButtonMenuItem) ae.getSource())
                            .isSelected()) {
                        try {
                            if (!UIManager.getLookAndFeel()
                                    .getName().equals(((JMenuItem) ae
                                            .getSource()).getText())) {
                                UIManager
                                        .setLookAndFeel(((JMenuItem) ae
                                                .getSource()).getActionCommand());

                                SwingUtilities
                                        .updateComponentTreeUI(fc);
                                SwingUtilities
                                        .updateComponentTreeUI(thisFrame);
                                SwingUtilities
                                        .updateComponentTreeUI(txtGoToPage);
//                                SwingUtilities
//                                        .updateComponentTreeUI(aboutDialog);
                                SwingUtilities
                                        .updateComponentTreeUI(docInfoDialog);
                                if (pdfAsImageDialog != null) {
                                    SwingUtilities
                                            .updateComponentTreeUI(pdfAsImageDialog);
                                }
                                lookAndFeelClassName = ((JMenuItem) ae
                                        .getSource()).getActionCommand();
                            }
                        } catch (Throwable e) {
                        }
                    }
                }
            });

            if (lookAndFeels[i].getClassName().equalsIgnoreCase(
                    lookAndFeelClassName)) {
                mnuLAFItems[i].setSelected(true);
            }

            radioGroup.add(mnuLAFItems[i]);
            mnuLAF.add(mnuLAFItems[i]);

        }

        menuBar.add(mnuLAF);

        JMenu mnuAbout = new JMenu("About");
        mnuAbout.setMnemonic('A');
        mnuAboutItem = new JMenuItem("About Viewer...");
        mnuAboutItem.setMnemonic('V');
        mnuAboutItem.addActionListener(this);

        mnuAbout.add(mnuAboutItem);

//        menuBar.add(mnuAbout);
        setJMenuBar(menuBar);

        mnuActualSizeItem.setEnabled(false);
        mnuFitPageItem.setEnabled(false);
        mnuFitWidthItem.setEnabled(false);
        mnuZoomInItem.setEnabled(false);
        mnuZoomOutItem.setEnabled(false);
        mnuRotateAntiClokwiseItem.setEnabled(false);
        mnuRotateClokwiseItem.setEnabled(false);
        chkMnuShowLabels.setSelected(true);
        chkMnuShowGapsBetweenPages.setSelected(true);
        chkMnuShowPageBorders.setSelected(true);
        chkMnuShowCoverPageDuringSideBySide.setSelected(false);
        chkMnuShowPageImageWhileTracking.setSelected(true);
        chkMnuShowPageNumberLabelWhileTracking.setSelected(true);
        viewer.setScrollMode(JViewport.BACKINGSTORE_SCROLL_MODE);
        radioMnuScrollMode_BACKINGSTORE_SCROLL_MODE.setSelected(true);
    }

    public void actionPerformed(ActionEvent ae) {
        Object sourceButton = ae.getSource();

        try {
            if (ae.getSource() == btnLoad) {
//                viewerSplitPane.setVisible(true);
                loadFile();
//                loadBookmarks();
            } else if (ae.getSource() == btnPrint) {
                printFile();
            } /*else if (ae.getSource() == btnDocInfo)
             {
             docInfoDialog.setVisible(true);
             }*/ else if (sourceButton == btnActualSize) {
                doActualSize();
            } else if (sourceButton == btnFitPage) {
                doFitPage();
            } else if (sourceButton == btnFitWidth) {
                doFitWidth();
            } else if (sourceButton == btnZoomOut) {
                doZoomOut();
            } else if (sourceButton == btnZoomIn) {
                doZoomIn();
            } else if (sourceButton == btnRotateCounterClockwise) {
                doRotateCounterClockwise();
            } else if (sourceButton == btnRotateClockwise) {
                doRotateClockwise();
            } else if (sourceButton == btnFirstPage) {
                viewFirstPage();
            } else if (sourceButton == btnPreviousPage) {
                viewPreviousPage();
            } else if (sourceButton == txtGoToPage) {
                NumberFormat nf = NumberFormat.getInstance();
                try {
                    Number num = nf.parse(txtGoToPage.getText());
                    int pNum = num.intValue();

                    if (d != null && (pNum >= 1 && pNum <= d.getPageCount())) {
                        goToPageNum(pNum);
                        updatePageNumAndZoomVal(viewer.getCurrentPage());
                    } else {
                        if (d == null) {
                            txtGoToPage.setText("");
                        } else {
                            JOptionPane.showMessageDialog(this
                                    .getParent(),
                                    "There is no page numbered '"
                                    + txtGoToPage.getText()
                                    + "' in this document",
                                    "Gnostice PDF Viewer",
                                    JOptionPane.INFORMATION_MESSAGE);
                            updatePageNumAndZoomVal(viewer.getCurrentPage());
                        }
                    }

                    if (viewer.getCurrentPage() == 1) {
                        btnFirstPage.setEnabled(false);
                        btnPreviousPage.setEnabled(false);

                        mnuFirstPageItem.setEnabled(false);
                        mnuPreviousPageItem.setEnabled(false);

                        if (viewer.getPageCount() > 1) {
                            btnNextPage.setEnabled(true);
                            btnLastPage.setEnabled(true);

                            mnuNextPageItem.setEnabled(true);
                            mnuLastPageItem.setEnabled(true);
                        }
                    } else if (viewer.getCurrentPage() != 0
                            && viewer.getCurrentPage() == viewer
                            .getPageCount()) {
                        btnNextPage.setEnabled(false);
                        btnLastPage.setEnabled(false);

                        mnuNextPageItem.setEnabled(false);
                        mnuLastPageItem.setEnabled(false);

                        if (viewer.getPageCount() > 1) {
                            btnFirstPage.setEnabled(true);
                            btnPreviousPage.setEnabled(true);

                            mnuFirstPageItem.setEnabled(true);
                            mnuPreviousPageItem.setEnabled(true);
                        }
                    } else {
                        btnFirstPage.setEnabled(true);
                        btnPreviousPage.setEnabled(true);
                        btnNextPage.setEnabled(true);
                        btnLastPage.setEnabled(true);

                        mnuFirstPageItem.setEnabled(true);
                        mnuPreviousPageItem.setEnabled(true);
                        mnuNextPageItem.setEnabled(true);
                        mnuLastPageItem.setEnabled(true);
                    }
                } catch (ParseException pe) {
                    if (d == null) {
                        txtGoToPage.setText("");
                    } else {
                        JOptionPane.showMessageDialog(this
                                .getParent(),
                                "There is no page numbered '"
                                + txtGoToPage.getText()
                                + "' in this document",
                                "Gnostice PDF Viewer",
                                JOptionPane.INFORMATION_MESSAGE);
                        updatePageNumAndZoomVal(viewer.getCurrentPage());
                    }
                }

                txtGoToPage.transferFocusUpCycle();
            } else if (sourceButton == btnNextPage) {
                viewNextPage();
            } else if (sourceButton == btnLastPage) {
                viewLastPage();
            } else if (sourceButton == btnContinuous) {
                setViewerPageLayout(PdfViewer.LAYOUT_SINGLE_PAGE_CONTINUOUS);
            } else if (sourceButton == btnSinglePage) {
                setViewerPageLayout(PdfViewer.LAYOUT_SINGLE_PAGE);
            } else if (sourceButton == btnSideBySide) {
                setViewerPageLayout(PdfViewer.LAYOUT_SIDE_BY_SIDE);
            } else if (sourceButton == btnSideBySideContinuous) {
                setViewerPageLayout(PdfViewer.LAYOUT_SIDE_BY_SIDE_CONTINUOUS);
            } else if (sourceButton == btnAutoFitColumnsInWindow) {
                setViewerPageLayout(PdfViewer.LAYOUT_AUTO_FIT_COLUMNS_IN_WINDOW);
            } else if (sourceButton == btnCustomLayout) {
                setViewerPageLayout(PdfViewer.LAYOUT_USER_DEFINED);
            } else if (sourceButton == txtCustomColumnCount) {
                NumberFormat nf = NumberFormat.getInstance();
                try {
                    Number num = nf.parse(txtCustomColumnCount.getText());
                    int customColsCount = num.intValue();

                    if (d != null) {
                        viewer.setPageColumnsCustomCount(customColsCount);
                        viewer.setPageLayout(PdfViewer.LAYOUT_USER_DEFINED);
                    } else {
                        txtCustomColumnCount.setText("");
                    }
                } catch (ParseException pe) {
                    if (d == null) {
                        txtCustomColumnCount.setText("");
                    } else {
                        JOptionPane.showMessageDialog(this
                                .getParent(),
                                "Invalid number for custom columns: '"
                                + txtCustomColumnCount.getText()
                                + "'",
                                "Gnostice PDF Viewer",
                                JOptionPane.INFORMATION_MESSAGE);
                    }
                }

                txtCustomColumnCount.transferFocusUpCycle();
            } else if (sourceButton == mnuOpenItem) {
                loadFile();

//                loadBookmarks();
            } else if (sourceButton == mnuPrintItem) {
                printFile();
            } else if (sourceButton == mnuCloseItem) {
                closeFile();
            } else if (sourceButton == mnuExitItem) {
                closeFile();
                saveUserPreferences();
                savePageDisplaySettings();
                System.exit(0);
            } else if (sourceButton == mnuShowPageDisplaySettingsDialog) {
                viewer.getPageDisplaySettings().showPageDisplaySettingsDialog(true);
            } else if (sourceButton == mnuFirstPageItem) {
                viewFirstPage();
            } else if (sourceButton == mnuPreviousPageItem) {
                viewPreviousPage();
            } else if (sourceButton == mnuNextPageItem) {
                viewNextPage();
            } else if (sourceButton == mnuLastPageItem) {
                viewLastPage();
            } else if (sourceButton == mnuPreviousView) {
                viewPreviousView();
            } else if (sourceButton == mnuNextView) {
                viewNextView();
            } else if (sourceButton == mnuActualSizeItem) {
                doActualSize();
            } else if (sourceButton == mnuFitPageItem) {
                doFitPage();
            } else if (sourceButton == mnuFitWidthItem) {
                doFitWidth();
            } else if (sourceButton == chkMnuShowLabels) {
                processChkMnuShowLabelsAction();
            } else if (sourceButton == chkMnuShowGapsBetweenPages) {
                processShowPageGapsAction();
            } else if (sourceButton == chkMnuShowPageBorders) {
                processShowPageBordersAction();
            } else if (sourceButton == chkMnuShowCoverPageDuringSideBySide) {
                processShowCoverPageAction();
            } else if (sourceButton == chkMnuShowPageImageWhileTracking) {
                if (chkMnuShowPageImageWhileTracking.isSelected()) {
                    viewer.setShowPagePreviewOnScrolling(true);
                } else {
                    viewer.setShowPagePreviewOnScrolling(false);
                }
            } else if (sourceButton == chkMnuShowPageNumberLabelWhileTracking) {
                if (chkMnuShowPageNumberLabelWhileTracking.isSelected()) {
                    viewer.setShowPageNumberLabelOnScrolling(true);
                } else {
                    viewer.setShowPageNumberLabelOnScrolling(false);
                }
            } else if (sourceButton == radioMnuScrollMode_BACKINGSTORE_SCROLL_MODE) {
                if (radioMnuScrollMode_BACKINGSTORE_SCROLL_MODE.isSelected()) {
                    viewer.setScrollMode(JViewport.BACKINGSTORE_SCROLL_MODE);
                }
            } else if (sourceButton == radioMnuScrollMode_BLIT_SCROLL_MODE) {
                if (radioMnuScrollMode_BLIT_SCROLL_MODE.isSelected()) {
                    viewer.setScrollMode(JViewport.BLIT_SCROLL_MODE);
                }
            } else if (sourceButton == radioMnuDPI_72) {
                if (radioMnuDPI_72.isSelected()) {
                    viewer.setDpi(PdfViewer.DPI_72);
                    dpiPropertyVal = PdfViewer.DPI_72;
                }
            } else if (sourceButton == radioMnuDPI_96) {
                if (radioMnuDPI_96.isSelected()) {
                    viewer.setDpi(PdfViewer.DPI_96);
                    dpiPropertyVal = PdfViewer.DPI_96;
                }
            } else if (sourceButton == radioMnuDPI_110) {
                if (radioMnuDPI_110.isSelected()) {
                    viewer.setDpi(PdfViewer.DPI_110);
                    dpiPropertyVal = PdfViewer.DPI_110;
                }
            } else if (sourceButton == mnuZoomInItem) {
                doZoomIn();
            } else if (sourceButton == mnuZoomOutItem) {
                doZoomOut();
            } else if (sourceButton == mnuRotateAntiClokwiseItem) {
                viewer.rotateCounterClockwise();
            } else if (sourceButton == mnuRotateClokwiseItem) {
                viewer.rotateClockwise();
            } else if (sourceButton == radioMnuLayoutSinglePage) {
                if (radioMnuLayoutSinglePage.isSelected()) {
                    setViewerPageLayout(PdfViewer.LAYOUT_SINGLE_PAGE);
                }
            } else if (sourceButton == radioMnuLayoutSinglePageContinuous) {
                if (radioMnuLayoutSinglePageContinuous.isSelected()) {
                    setViewerPageLayout(PdfViewer.LAYOUT_SINGLE_PAGE_CONTINUOUS);
                }
            } else if (sourceButton == radioMnuLayoutSideBySide) {
                if (radioMnuLayoutSideBySide.isSelected()) {
                    setViewerPageLayout(PdfViewer.LAYOUT_SIDE_BY_SIDE);
                }
            } else if (sourceButton == radioMnuLayoutSideBySideContinuous) {
                if (radioMnuLayoutSideBySideContinuous.isSelected()) {
                    setViewerPageLayout(PdfViewer.LAYOUT_SIDE_BY_SIDE_CONTINUOUS);
                }
            } else if (sourceButton == radioMnuLayoutAutoFitColumnsInWindow) {
                if (radioMnuLayoutAutoFitColumnsInWindow.isSelected()) {
                    setViewerPageLayout(PdfViewer.LAYOUT_AUTO_FIT_COLUMNS_IN_WINDOW);
                }
            } else if (sourceButton == radioMnuLayoutUserDefined) {
                if (radioMnuLayoutUserDefined.isSelected()) {
                    setViewerPageLayout(PdfViewer.LAYOUT_USER_DEFINED);
                }
            } else if (sourceButton == mnuAboutItem) {
//                aboutDialog.setVisible(true);
            } else if (sourceButton == mnuDocInfo) {
                docInfoDialog.setVisible(true);
            } else if (sourceButton == mnuItemExportAsImage) {
                if (d != null) {
                    if (pdfAsImageDialog == null) {
                        pdfAsImageDialog = new PdfExportAsImageDialog(
                                this, d, viewer);
                        SwingUtilities
                                .updateComponentTreeUI(pdfAsImageDialog);
                        pdfAsImageDialog.setModal(true);
                    } else {
                        if (this.d != pdfAsImageDialog.d) {
                            pdfAsImageDialog.setDocument(d);
                        }
                        pdfAsImageDialog.setVisible(true);
                    }
                } else {
                    JOptionPane
                            .showMessageDialog(
                                    this,
                                    "PDF Document cannot be null, Load a document to the viewer to export it as image");
                }
            }
        } catch (PdfException pdfEx) {
            JOptionPane.showMessageDialog(this.getParent(), pdfEx
                    .getMessage(), "Gnostice PDF Viewer",
                    JOptionPane.ERROR_MESSAGE);
        } catch (IOException ioEx) {
            JOptionPane.showMessageDialog(this.getParent(), ioEx
                    .getMessage(), "Gnostice PDF Viewer",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private void setViewerPageLayout(int pageLayout) {
        final int pageLayoutVal = pageLayout;
        final Container thisParent = this.getParent();
        Thread t = new Thread(new Runnable() {
            public void run() {
                try {
                    startProgressBar();

                    viewer.setPageLayout(pageLayoutVal);
                } catch (PdfException pdfEx) {
                    JOptionPane.showMessageDialog(thisParent, pdfEx
                            .getMessage(), "Gnostice PDF Viewer",
                            JOptionPane.ERROR_MESSAGE);
                } finally {
                    stopProgressBar();
                }
            }
        });
        t.start();
    }

    private void processChkMnuShowLabelsAction() {
        if (chkMnuShowLabels.isSelected()) {
            btnLoad.setText("Open");
            btnPrint.setText("Print");
            btnActualSize.setText("Actual Size");
            btnFitPage.setText("Fit Page");
            btnFitWidth.setText("Fit Width");
            btnZoomOut.setText("Zoom Out");
            btnZoomIn.setText("Zoom In");
            btnRotateCounterClockwise.setText("Rotate Counterclockwise");
            btnRotateClockwise.setText("Rotate Clockwise");

            btnContinuous.setText("Continuous Layout");
            btnSinglePage.setText("Single Page Layout");
            btnSideBySide.setText("Side-by-Side Layout");
            btnSideBySideContinuous.setText("Side-by-Side Continuous Layout");
            btnAutoFitColumnsInWindow.setText("Auto Fit Columns in Window");
            btnCustomLayout.setText("Custom Layout");

        } else {
            btnLoad.setText("");
            btnPrint.setText("");
            btnActualSize.setText("");
            btnFitPage.setText("");
            btnFitWidth.setText("");
            btnZoomOut.setText("");
            btnZoomIn.setText("");
            btnRotateCounterClockwise.setText("");
            btnRotateClockwise.setText("");

            btnContinuous.setText("");
            btnSinglePage.setText("");
            btnSideBySide.setText("");
            btnSideBySideContinuous.setText("");
            btnAutoFitColumnsInWindow.setText("");
            btnCustomLayout.setText("");
        }
    }

    private void processShowPageGapsAction() {
        final Container thisParent = this.getParent();
        Thread t = new Thread(new Runnable() {
            public void run() {
                try {
                    startProgressBar();

                    if (chkMnuShowGapsBetweenPages.isSelected()) {
                        viewer.setShowGapsBetweenPages(true);
                    } else {
                        viewer.setShowGapsBetweenPages(false);
                    }
                } catch (PdfException pdfEx) {
                    JOptionPane.showMessageDialog(thisParent, pdfEx
                            .getMessage(), "Gnostice PDF Viewer",
                            JOptionPane.ERROR_MESSAGE);
                } finally {
                    stopProgressBar();
                }
            }
        });
        t.start();
    }

    private void processShowPageBordersAction() {
        final Container thisParent = this.getParent();
        Thread t = new Thread(new Runnable() {
            public void run() {
                try {
                    startProgressBar();

                    if (chkMnuShowPageBorders.isSelected()) {
                        viewer.setShowPageBordersWhenNoPageGaps(true);
                    } else {
                        viewer.setShowPageBordersWhenNoPageGaps(false);
                    }
                } catch (PdfException pdfEx) {
                    JOptionPane.showMessageDialog(thisParent, pdfEx
                            .getMessage(), "Gnostice PDF Viewer",
                            JOptionPane.ERROR_MESSAGE);
                } finally {
                    stopProgressBar();
                }
            }
        });
        t.start();
    }

    private void processShowCoverPageAction() {
        final Container thisParent = this.getParent();
        Thread t = new Thread(new Runnable() {
            public void run() {
                try {
                    startProgressBar();

                    if (chkMnuShowCoverPageDuringSideBySide.isSelected()) {
                        viewer.setShowCoverPageDuringSideBySide(true);
                    } else {
                        viewer.setShowCoverPageDuringSideBySide(false);
                    }
                } catch (PdfException pdfEx) {
                    JOptionPane.showMessageDialog(thisParent, pdfEx
                            .getMessage(), "Gnostice PDF Viewer",
                            JOptionPane.ERROR_MESSAGE);
                } finally {
                    stopProgressBar();
                }
            }
        });
        t.start();
    }

    private void doRotateClockwise() {
        final Container thisParent = this.getParent();
        Thread t = new Thread(new Runnable() {
            public void run() {
                try {
                    startProgressBar();

                    viewer.rotateClockwise();
                } catch (PdfException pdfEx) {
                    JOptionPane.showMessageDialog(thisParent, pdfEx
                            .getMessage(), "Gnostice PDF Viewer",
                            JOptionPane.ERROR_MESSAGE);
                } finally {
                    stopProgressBar();
                }
            }
        });
        t.start();
    }

    private void doRotateCounterClockwise() {
        final Container thisParent = this.getParent();
        Thread t = new Thread(new Runnable() {
            public void run() {
                try {
                    startProgressBar();

                    viewer.rotateCounterClockwise();
                } catch (PdfException pdfEx) {
                    JOptionPane.showMessageDialog(thisParent, pdfEx
                            .getMessage(), "Gnostice PDF Viewer",
                            JOptionPane.ERROR_MESSAGE);
                } finally {
                    stopProgressBar();
                }
            }
        });
        t.start();
    }

    private void doActualSize() {
        final Container thisParent = this.getParent();
        Thread t = new Thread(new Runnable() {
            public void run() {
                try {
                    startProgressBar();

                    viewer.setPageView(PdfViewer.PAGEVIEW_ACTUAL_SIZE);
                } catch (PdfException pdfEx) {
                    JOptionPane.showMessageDialog(thisParent, pdfEx
                            .getMessage(), "Gnostice PDF Viewer",
                            JOptionPane.ERROR_MESSAGE);
                } finally {
                    stopProgressBar();
                }
            }
        });
        t.start();

        currentZoomFactor = viewer.getZoom();
        cboZoomFactor.setSelectedItem((new Double(viewer.getZoom()))
                .toString());

//        btnActualSize.setSelected(true);
//        btnFitPage.setSelected(false);
//        btnFitWidth.setSelected(false);
    }

    private void doFitPage() throws IOException, PdfException {
        final Container thisParent = this.getParent();
        Thread t = new Thread(new Runnable() {
            public void run() {
                try {
                    startProgressBar();

                    viewer.setPageView(PdfViewer.PAGEVIEW_FIT_PAGE);
                } catch (PdfException pdfEx) {
                    JOptionPane.showMessageDialog(thisParent, pdfEx
                            .getMessage(), "Gnostice PDF Viewer",
                            JOptionPane.ERROR_MESSAGE);
                } finally {
                    stopProgressBar();
                }
            }
        });
        t.start();

        currentZoomFactor = viewer.getZoom();
        cboZoomFactor.setSelectedItem((new Double(viewer.getZoom()))
                .toString());
    }

    private void doFitWidth() throws IOException, PdfException {
        final Container thisParent = this.getParent();
        Thread t = new Thread(new Runnable() {
            public void run() {
                try {
                    startProgressBar();

                    viewer.setPageView(PdfViewer.PAGEVIEW_FIT_WIDTH);
                } catch (PdfException pdfEx) {
                    JOptionPane.showMessageDialog(thisParent, pdfEx
                            .getMessage(), "Gnostice PDF Viewer",
                            JOptionPane.ERROR_MESSAGE);
                } finally {
                    stopProgressBar();
                }
            }
        });
        t.start();

        currentZoomFactor = viewer.getZoom();
        cboZoomFactor.setSelectedItem((new Double(viewer.getZoom()))
                .toString());
    }

    private void doZoomOut() {
        int nextPossibleZoomOutIndex = getNextPossibleZoomOutIndex(currentZoomFactor);
        cboZoomFactor.setSelectedIndex(nextPossibleZoomOutIndex);
    }

    private void doZoomIn() {
        int nextPossibleZoomInIndex = getNextPossibleZoomInIndex(currentZoomFactor);
        cboZoomFactor.setSelectedIndex(nextPossibleZoomInIndex);
    }

    private void viewFirstPage() {
        final Container thisParent = this.getParent();
        Thread t = new Thread(new Runnable() {
            public void run() {
                try {
                    startProgressBar();

                    viewer.firstPage();
                } catch (PdfException pdfEx) {
                    JOptionPane.showMessageDialog(thisParent, pdfEx
                            .getMessage(), "Gnostice PDF Viewer",
                            JOptionPane.ERROR_MESSAGE);
                } catch (IOException ioEx) {
                    JOptionPane.showMessageDialog(thisParent, ioEx
                            .getMessage(), "Gnostice PDF Viewer",
                            JOptionPane.ERROR_MESSAGE);
                } finally {
                    stopProgressBar();
                }
            }
        });
        t.start();
    }

    private void viewPreviousPage() {
        final Container thisParent = this.getParent();
        Thread t = new Thread(new Runnable() {
            public void run() {
                try {
                    startProgressBar();

                    viewer.previousPage();
                } catch (PdfException pdfEx) {
                    JOptionPane.showMessageDialog(thisParent, pdfEx
                            .getMessage(), "Gnostice PDF Viewer",
                            JOptionPane.ERROR_MESSAGE);
                } catch (IOException ioEx) {
                    JOptionPane.showMessageDialog(thisParent, ioEx
                            .getMessage(), "Gnostice PDF Viewer",
                            JOptionPane.ERROR_MESSAGE);
                } finally {
                    stopProgressBar();
                }
            }
        });
        t.start();
    }

    private void goToPageNum(int pNum) {
        final Container thisParent = this.getParent();
        final int gotoPageNum = pNum;
        Thread t = new Thread(new Runnable() {
            public void run() {
                try {
                    startProgressBar();

                    viewer.setCurrentPage(gotoPageNum);
                } catch (PdfException pdfEx) {
                    JOptionPane.showMessageDialog(thisParent, pdfEx
                            .getMessage(), "Gnostice PDF Viewer",
                            JOptionPane.ERROR_MESSAGE);
                } catch (IOException ioEx) {
                    JOptionPane.showMessageDialog(thisParent, ioEx
                            .getMessage(), "Gnostice PDF Viewer",
                            JOptionPane.ERROR_MESSAGE);
                } finally {
                    stopProgressBar();
                }
            }
        });
        t.start();

    }

    private void viewNextPage() {
        final Container thisParent = this.getParent();
        Thread t = new Thread(new Runnable() {
            public void run() {
                try {
                    startProgressBar();

                    viewer.nextPage();
                } catch (PdfException pdfEx) {
                    JOptionPane.showMessageDialog(thisParent, pdfEx
                            .getMessage(), "Gnostice PDF Viewer",
                            JOptionPane.ERROR_MESSAGE);
                } catch (IOException ioEx) {
                    JOptionPane.showMessageDialog(thisParent, ioEx
                            .getMessage(), "Gnostice PDF Viewer",
                            JOptionPane.ERROR_MESSAGE);
                } finally {
                    stopProgressBar();
                }
            }
        });
        t.start();
    }

    private void viewLastPage() {
        final Container thisParent = this.getParent();
        Thread t = new Thread(new Runnable() {
            public void run() {
                try {
                    startProgressBar();

                    viewer.lastPage();
                } catch (PdfException pdfEx) {
                    JOptionPane.showMessageDialog(thisParent, pdfEx
                            .getMessage(), "Gnostice PDF Viewer",
                            JOptionPane.ERROR_MESSAGE);
                } catch (IOException ioEx) {
                    JOptionPane.showMessageDialog(thisParent, ioEx
                            .getMessage(), "Gnostice PDF Viewer",
                            JOptionPane.ERROR_MESSAGE);
                } finally {
                    stopProgressBar();
                }
            }
        });
        t.start();
    }

    private void viewPreviousView() {
        final Container thisParent = this.getParent();
        Thread t = new Thread(new Runnable() {
            public void run() {
                try {
                    startProgressBar();

                    viewer.previousView();
                } catch (PdfException pdfEx) {
                    JOptionPane.showMessageDialog(thisParent, pdfEx
                            .getMessage(), "Gnostice PDF Viewer",
                            JOptionPane.ERROR_MESSAGE);
                } catch (IOException ioEx) {
                    JOptionPane.showMessageDialog(thisParent, ioEx
                            .getMessage(), "Gnostice PDF Viewer",
                            JOptionPane.ERROR_MESSAGE);
                } finally {
                    stopProgressBar();
                }
            }
        });
        t.start();
    }

    private void viewNextView() {
        final Container thisParent = this.getParent();
        Thread t = new Thread(new Runnable() {
            public void run() {
                try {
                    startProgressBar();

                    viewer.nextView();
                } catch (PdfException pdfEx) {
                    JOptionPane.showMessageDialog(thisParent, pdfEx
                            .getMessage(), "Gnostice PDF Viewer",
                            JOptionPane.ERROR_MESSAGE);
                } catch (IOException ioEx) {
                    JOptionPane.showMessageDialog(thisParent, ioEx
                            .getMessage(), "Gnostice PDF Viewer",
                            JOptionPane.ERROR_MESSAGE);
                } finally {
                    stopProgressBar();
                }
            }
        });
        t.start();
    }

    public void itemStateChanged(ItemEvent ie) {
        if (ie.getSource() == cboZoomFactor) {
            if (ie.getStateChange() == ItemEvent.SELECTED) {
                String selectedItem = (String) ie.getItem();

                if (selectedItem
                        .charAt(selectedItem.length() - 1) != '%') {
                    try {
                        Double.parseDouble(selectedItem);
                        cboZoomFactor
                                .setSelectedItem(selectedItem + "%");
                    } catch (NumberFormatException nfe) {
                        cboZoomFactor.setSelectedItem(String
                                .valueOf(currentZoomFactor)
                                + "%");
                    }
                } else {
                    try {
                        double zoomFactor = Double
                                .parseDouble(selectedItem.substring(
                                                0, selectedItem.length() - 1));

                        if (zoomFactor > 6400) {
                            cboZoomFactor.setSelectedIndex(0);
                        } else if (zoomFactor < 0.01) {
                            cboZoomFactor
                                    .setSelectedIndex(cboZoomFactor
                                            .getItemCount() - 1);
                        } else // if the value of zoomFactor ranges from
                        // 1 to 6400
                        {
                            // do not set if same zoom factor is
                            // entered once again
                            if (currentZoomFactor != zoomFactor) {
                                currentZoomFactor = zoomFactor;

                                setRequiredZoom(zoomFactor);

                                // selected zoom is 0.01%
                                if (cboZoomFactor
                                        .getSelectedIndex() == cboZoomFactor
                                        .getItemCount() - 1) {
                                    btnZoomOut.setEnabled(false);
                                } else {
                                    btnZoomOut.setEnabled(true);
                                }

                                // selected zoom is 6400%
                                if (cboZoomFactor
                                        .getSelectedIndex() == 0) {
                                    btnZoomIn.setEnabled(false);
                                } else {
                                    btnZoomIn.setEnabled(true);
                                }
                            }
                        }
                    } catch (NumberFormatException nfe) {
                        cboZoomFactor.setSelectedItem(String
                                .valueOf(currentZoomFactor)
                                + "%");
                    }
                }
            }
        }
    }

    private int getNextPossibleZoomOutIndex(double zoomPercentage) {
        int itemIndex = -1;
        double zoomFactor = 0.0;
        String selectedItem = null;

        for (int i = 0; i < cboZoomFactor.getItemCount(); i++) {
            selectedItem = (String) cboZoomFactor.getItemAt(i);

            try {
                zoomFactor = Double.parseDouble(selectedItem
                        .substring(0, selectedItem.length() - 1));

                if (zoomFactor < zoomPercentage) {
                    itemIndex = i;
                    break;
                }
            } catch (NumberFormatException nfe) {

            }
        }

        return itemIndex;
    }

    private int getNextPossibleZoomInIndex(double zoomPercentage) {
        int itemIndex = -1;
        double zoomFactor = 0.0;
        String selectedItem = null;

        for (int i = cboZoomFactor.getItemCount() - 1; i >= 0; i--) {
            selectedItem = (String) cboZoomFactor.getItemAt(i);

            try {
                zoomFactor = Double.parseDouble(selectedItem
                        .substring(0, selectedItem.length() - 1));

                if (zoomFactor > zoomPercentage) {
                    itemIndex = i;
                    break;
                }
            } catch (NumberFormatException nfe) {

            }
        }

        return itemIndex;
    }

    private void setRequiredZoom(double zoomFacter) {
        final double requiredZoomFacter = zoomFacter;
        final Container thisParent = this.getParent();
        Thread t = new Thread(new Runnable() {
            public void run() {
                try {
                    startProgressBar();

                    viewer.setZoom(requiredZoomFacter);
                } catch (PdfException pdfEx) {
                    JOptionPane.showMessageDialog(thisParent, pdfEx
                            .getMessage(), "Gnostice PDF Viewer",
                            JOptionPane.ERROR_MESSAGE);
                } finally {
                    stopProgressBar();
                }
            }
        });
        t.start();
    }

    private void updatePageNumAndZoomVal(int pageNum) {
        if (pageNum == 0) {
            txtGoToPage.setText("");
        } else {
            txtGoToPage.setText(String.valueOf(pageNum) + " of "
                    + String.valueOf(viewer.getPageCount()));
        }

        currentZoomFactor = viewer.getZoom();
        cboZoomFactor.setSelectedItem(String
                .valueOf(viewer.getZoom())
                + "%");

        int pageViewVal = viewer.getPageView();

        switch (pageViewVal) {
            case PdfViewer.PAGEVIEW_ACTUAL_SIZE:
                btnFitWidth.setSelected(false);
                btnActualSize.setSelected(true);
                btnFitPage.setSelected(false);
                break;
            case PdfViewer.PAGEVIEW_FIT_PAGE:
                btnFitWidth.setSelected(false);
                btnActualSize.setSelected(false);
                btnFitPage.setSelected(true);
                break;
            case PdfViewer.PAGEVIEW_FIT_WIDTH:
                btnFitWidth.setSelected(true);
                btnActualSize.setSelected(false);
                btnFitPage.setSelected(false);
                break;
            case PdfViewer.PAGEVIEW_CUSTOM:
                btnFitWidth.setSelected(false);
                btnActualSize.setSelected(false);
                btnFitPage.setSelected(false);
                break;
        }
    }

    /* This is the password event handler */
    public String onPassword(PdfDocument d, boolean[] flags) {
        JPanel panel = new JPanel(new FlowLayout());

        JPasswordField field = new JPasswordField(10);
        panel.add(new JLabel("Password: "));
        panel.add(field);

        field.requestFocus();

        JOptionPane.showMessageDialog(this, panel,
                "Gnostice PDF Viewer", JOptionPane.OK_OPTION
                | JOptionPane.QUESTION_MESSAGE);

        String pwd = "";

        char[] pin = field.getPassword();
        try {
            pwd = new String(pin);
        } finally {
            Arrays.fill(pin, ' ');
            field.setText("");
        }

        return pwd;
    }

    private void loadFile() {
        if (recentDir != null
                && recentDir.exists()
                && recentDir.isDirectory()) {
            fc.setCurrentDirectory(recentDir);
        }

        int fcState = fc.showOpenDialog(this);

        if (fcState != JFileChooser.APPROVE_OPTION) {
            return;
        }

        recentDir = fc.getCurrentDirectory();

        File selectedFile = fc.getSelectedFile();

        if (!(selectedFile.exists() && selectedFile.isFile())) {
            JOptionPane.showMessageDialog(this, "The File \""
                    + selectedFile.getAbsoluteFile()
                    + "\" does not exist", "Gnostice PDF Viewer",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        closeFile();

        loadFile(selectedFile);
    }

    protected void loadFile(File fileToBeLoaded) {
        closeFile();

        docPath = fileToBeLoaded.getAbsolutePath();
        recentDir = fileToBeLoaded.getParentFile();
        setTitle("Supreme Today");
//        setTitle("Gnostice PDFOne");

//      Show the Progress
        startProgressBar();

        Thread t = new Thread(new Runnable() {
            public void run() {
                loadDocument(docPath);
            }
        });
        t.start();
    }

    private void printFile() throws IOException, PdfException {
        if (d != null && d.isLoaded()) {
            if (pdfPrinter != null) {
                // Obtain print preference from user and
                // print document accordingly
                pdfPrinter.showPrintDialog(this, true);
                System.out.println(pdfPrinter.getPageSize());
            } else {
                JOptionPane.showMessageDialog(this,
                        "Could not create PdfPrinter object",
                        "PdfPrinter object creation failed",
                        JOptionPane.ERROR_MESSAGE);
            }
        } else {
            JOptionPane.showMessageDialog(this,
                    "Document not loaded, Load a PDF Document to Print",
                    "Gnostice PDF Viewer", JOptionPane.ERROR_MESSAGE);
            loadFile();
        }
    }

    private void loadDocument(String docPath) {
        try {
            btnLoad.setEnabled(false);
            mnuOpenItem.setEnabled(false);

            btnPrint.setEnabled(false);
            mnuPrintItem.setEnabled(false);

            // do not allow drag & drop while loading a document
            canDropPDF = false;

            File fileToBeLoaded = new File(docPath);

            // Load the PDF document
            d.load(docPath);

            fc.setSelectedFile(fileToBeLoaded);

            // calling refresh() method on the viewer is optionally as
            // it will be refreshed automatically the document is
            // associated with the viewer using setDocument() method
//            viewer.refresh();
            docInfoDialog.updateDocumentInfo(d);

            btnActualSize.setEnabled(true);
            btnFitPage.setEnabled(true);
            btnFitWidth.setEnabled(true);
            btnZoomOut.setEnabled(true);
            cboZoomFactor.setEnabled(true);
            btnZoomIn.setEnabled(true);
            btnRotateCounterClockwise.setEnabled(true);
            btnRotateClockwise.setEnabled(true);
            txtGoToPage.setEnabled(true);

            btnContinuous.setEnabled(true);
            btnSinglePage.setEnabled(true);
            btnSideBySide.setEnabled(true);
            btnSideBySideContinuous.setEnabled(true);
            btnAutoFitColumnsInWindow.setEnabled(true);
            btnCustomLayout.setEnabled(true);
            txtCustomColumnCount.setEnabled(true);

            mnuActualSizeItem.setEnabled(true);
            mnuFitPageItem.setEnabled(true);
            mnuFitWidthItem.setEnabled(true);
            mnuZoomInItem.setEnabled(true);
            mnuZoomOutItem.setEnabled(true);
            mnuRotateAntiClokwiseItem.setEnabled(true);
            mnuRotateClokwiseItem.setEnabled(true);
            mnuItemExportAsImage.setEnabled(true);

            radioMnuLayoutSinglePage.setEnabled(true);
            radioMnuLayoutSinglePageContinuous.setEnabled(true);
            radioMnuLayoutSideBySide.setEnabled(true);
            radioMnuLayoutSideBySideContinuous.setEnabled(true);
            radioMnuLayoutAutoFitColumnsInWindow.setEnabled(true);
            radioMnuLayoutUserDefined.setEnabled(true);

            cboZoomFactor.setSelectedItem((new Double(viewer
                    .getZoom())).toString());

            mnuCloseItem.setEnabled(true);
            mnuDocInfo.setEnabled(true);

            setTitle(fileToBeLoaded.getName() + " - Gnostice PDFOne");
        } catch (PdfException pdfEx) {
            JOptionPane.showMessageDialog(this, pdfEx.getMessage(),
                    "Gnostice PDF Viewer", JOptionPane.ERROR_MESSAGE);
        } catch (IOException ioEx) {
            JOptionPane.showMessageDialog(this, ioEx.getMessage(),
                    "Gnostice PDF Viewer", JOptionPane.ERROR_MESSAGE);
        } finally {
            // Make the progress bar invisible
            stopProgressBar();

            btnLoad.setEnabled(true);
            mnuOpenItem.setEnabled(true);

            btnPrint.setEnabled(true);
            mnuPrintItem.setEnabled(true);

            // allow drag & drop once after document loading process completed
            canDropPDF = true;
        }
    }

    private void startProgressBar() {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                if (!progress.isVisible()) {
                    progress.setVisible(true);
                    progress.setIndeterminate(true);
                }
            }
        });
    }

    private void stopProgressBar() {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                if (progress.isVisible()) {
                    progress.setIndeterminate(false);
                    progress.setVisible(false);
                }
            }
        });
    }

    private void closeFile() {
//        try
//        {
//            viewer.closeDocument();
//        }
//        catch(IOException ioEx)
//        {
//            JOptionPane.showMessageDialog(this,
//                ioEx.getMessage(), "Gnostice PDF Viewer",
//                JOptionPane.ERROR_MESSAGE);
//        }

        txtGoToPage.setText("");

        btnFirstPage.setEnabled(false);
        btnPreviousPage.setEnabled(false);
        txtGoToPage.setEnabled(false);
        btnNextPage.setEnabled(false);
        btnLastPage.setEnabled(false);

        mnuFirstPageItem.setEnabled(false);
        mnuPreviousPageItem.setEnabled(false);
        mnuNextPageItem.setEnabled(false);
        mnuLastPageItem.setEnabled(false);

        btnActualSize.setEnabled(false);
        btnFitPage.setEnabled(false);
        btnFitWidth.setEnabled(false);
        btnZoomOut.setEnabled(false);
        cboZoomFactor.setEnabled(false);
        btnZoomIn.setEnabled(false);
        btnRotateCounterClockwise.setEnabled(false);
        btnRotateClockwise.setEnabled(false);

        btnContinuous.setEnabled(false);
        btnSinglePage.setEnabled(false);
        btnSideBySide.setEnabled(false);
        btnSideBySideContinuous.setEnabled(false);
        btnAutoFitColumnsInWindow.setEnabled(false);
        btnCustomLayout.setEnabled(false);
        txtCustomColumnCount.setEnabled(false);

        mnuActualSizeItem.setEnabled(false);
        mnuFitPageItem.setEnabled(false);
        mnuFitWidthItem.setEnabled(false);
        mnuZoomInItem.setEnabled(false);
        mnuZoomOutItem.setEnabled(false);
        mnuRotateAntiClokwiseItem.setEnabled(false);
        mnuRotateClokwiseItem.setEnabled(false);
        mnuItemExportAsImage.setEnabled(false);

        radioMnuLayoutSinglePage.setEnabled(false);
        radioMnuLayoutSinglePageContinuous.setEnabled(false);
        radioMnuLayoutSideBySide.setEnabled(false);
        radioMnuLayoutSideBySideContinuous.setEnabled(false);
        radioMnuLayoutAutoFitColumnsInWindow.setEnabled(false);
        radioMnuLayoutUserDefined.setEnabled(false);

        setTitle("Gnostice PDFOne");

        if (d != null) {
            try {
                d.close();
            } catch (Exception ioEx) {
                JOptionPane.showMessageDialog(this,
                        ioEx.getMessage(), "Gnostice PDF Viewer",
                        JOptionPane.ERROR_MESSAGE);
            }
        }

        mnuCloseItem.setEnabled(false);
//        mnuDocInfo.setEnabled(false);

        btnLoad.requestFocusInWindow();
    }

    public static void openFile(String fileName) {
        ViewerDemo vd = new ViewerDemo();

        int screenWidth = Toolkit.getDefaultToolkit()
                .getScreenSize().width - 20;
        int screenHeight = Toolkit.getDefaultToolkit()
                .getScreenSize().height - 20;
        vd.pack();

        vd.setSize(screenWidth, screenHeight);
        vd.setExtendedState(JFrame.MAXIMIZED_BOTH);
        vd.setTitle("Supreme Today");
//        vd.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        vd.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        vd.setVisible(true);

        File f = new File(fileName);
        vd.loadFile(f);
    }

    public void notifyOutlineAction() {
        updatePageNumAndZoomVal(viewer.getCurrentPage());

        if (viewer.getCurrentPage() == 1 && viewer.getPageCount() == 1) {
            return;
        }

        if (viewer.getCurrentPage() == viewer.getPageCount()) {
            btnNextPage.setEnabled(false);
            btnLastPage.setEnabled(false);

            mnuNextPageItem.setEnabled(false);
            mnuLastPageItem.setEnabled(false);

            if (viewer.getPageCount() > 1) {
                btnFirstPage.setEnabled(true);
                btnPreviousPage.setEnabled(true);

                mnuFirstPageItem.setEnabled(true);
                mnuPreviousPageItem.setEnabled(true);
            }
        } else if (viewer.getCurrentPage() == 1) {
            btnFirstPage.setEnabled(false);
            btnPreviousPage.setEnabled(false);

            mnuFirstPageItem.setEnabled(false);
            mnuPreviousPageItem.setEnabled(false);

            btnNextPage.setEnabled(true);
            btnLastPage.setEnabled(true);

            mnuNextPageItem.setEnabled(true);
            mnuLastPageItem.setEnabled(true);
        } else {
            btnFirstPage.setEnabled(true);
            btnPreviousPage.setEnabled(true);
            btnNextPage.setEnabled(true);
            btnLastPage.setEnabled(true);

            mnuFirstPageItem.setEnabled(true);
            mnuPreviousPageItem.setEnabled(true);
            mnuNextPageItem.setEnabled(true);
            mnuLastPageItem.setEnabled(true);
        }
    }

    public void onPageChange(PdfViewer viewer, int changedPageNum) {
    }

    public void onZoomChange(PdfViewer viewer,
            double changedZoomFacter) {
    }

    public void onRotationChange(PdfViewer viewer, int rotationAngle) {
    }

    public void onPageViewChange(PdfViewer viewer, int changedPageView) {
    }

    public void onDpiChange(PdfViewer viewer, int dpi) {
    }

    public void onChange(PdfViewer viewer, int pageNum,
            double zoomFacter, int rotationAngle, int pageView) {

    }

    public void windowActivated(WindowEvent e) {
    }

    public void windowClosed(WindowEvent e) {
    }

    public void windowClosing(WindowEvent e) {
        saveUserPreferences();
        savePageDisplaySettings();

//        aboutDialog.dispose();
        docInfoDialog.dispose();
//docInfoDialog.hide();
        try {
            viewer.dispose();
        } catch (IOException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        } catch (PdfException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }

    }

    public void windowDeactivated(WindowEvent e) {
    }

    public void windowDeiconified(WindowEvent e) {
    }

    public void windowIconified(WindowEvent e) {
    }

    public void windowOpened(WindowEvent e) {
    }

    public void onPageLayoutChange(PdfViewer viewer, int changedPageLayout) {
    }

    public void onChange(PdfViewer viewer, int pageNum,
            double zoomFacter, int rotationAngle, int pageView,
            int pageLayout, int pageColumnsCustomCount,
            boolean showGapsBetweenPages,
            boolean showCoverPageDuringSideBySide,
            boolean showPageBordersWhenNoPageGaps) {
        if (pageNum == 0) {
            txtGoToPage.setText("");
        } else {
            txtGoToPage.setText(String.valueOf(pageNum) + " of "
                    + String.valueOf(viewer.getPageCount()));
        }

        txtCustomColumnCount.setText(String.valueOf(pageColumnsCustomCount));

        currentZoomFactor = zoomFacter;
        cboZoomFactor.getEditor().setItem(String
                .valueOf(zoomFacter)
                + "%");

        switch (pageView) {
            case PdfViewer.PAGEVIEW_ACTUAL_SIZE:
                btnFitWidth.setSelected(false);
                btnActualSize.setSelected(true);
                btnFitPage.setSelected(false);
                break;
            case PdfViewer.PAGEVIEW_FIT_PAGE:
                btnFitWidth.setSelected(false);
                btnActualSize.setSelected(false);
                btnFitPage.setSelected(true);
                break;
            case PdfViewer.PAGEVIEW_FIT_WIDTH:
                btnFitWidth.setSelected(true);
                btnActualSize.setSelected(false);
                btnFitPage.setSelected(false);
                break;
            case PdfViewer.PAGEVIEW_CUSTOM:
                btnFitWidth.setSelected(false);
                btnActualSize.setSelected(false);
                btnFitPage.setSelected(false);
                break;
        }

        switch (pageLayout) {
            case PdfViewer.LAYOUT_SINGLE_PAGE:
                btnContinuous.setSelected(false);
                btnSinglePage.setSelected(true);
                btnSideBySide.setSelected(false);
                btnSideBySideContinuous.setSelected(false);
                btnAutoFitColumnsInWindow.setSelected(false);
                btnCustomLayout.setSelected(false);

                radioMnuLayoutSinglePage.setSelected(true);
                radioMnuLayoutSinglePageContinuous.setSelected(false);
                radioMnuLayoutSideBySide.setSelected(false);
                radioMnuLayoutSideBySideContinuous.setSelected(false);
                radioMnuLayoutAutoFitColumnsInWindow.setSelected(false);
                radioMnuLayoutUserDefined.setSelected(false);
                break;
            case PdfViewer.LAYOUT_SINGLE_PAGE_CONTINUOUS:
                btnContinuous.setSelected(true);
                btnSinglePage.setSelected(false);
                btnSideBySide.setSelected(false);
                btnSideBySideContinuous.setSelected(false);
                btnAutoFitColumnsInWindow.setSelected(false);
                btnCustomLayout.setSelected(false);

                radioMnuLayoutSinglePage.setSelected(false);
                radioMnuLayoutSinglePageContinuous.setSelected(true);
                radioMnuLayoutSideBySide.setSelected(false);
                radioMnuLayoutSideBySideContinuous.setSelected(false);
                radioMnuLayoutAutoFitColumnsInWindow.setSelected(false);
                radioMnuLayoutUserDefined.setSelected(false);
                break;
            case PdfViewer.LAYOUT_SIDE_BY_SIDE:
                btnContinuous.setSelected(false);
                btnSinglePage.setSelected(false);
                btnSideBySide.setSelected(true);
                btnSideBySideContinuous.setSelected(false);
                btnAutoFitColumnsInWindow.setSelected(false);
                btnCustomLayout.setSelected(false);

                radioMnuLayoutSinglePage.setSelected(false);
                radioMnuLayoutSinglePageContinuous.setSelected(false);
                radioMnuLayoutSideBySide.setSelected(true);
                radioMnuLayoutSideBySideContinuous.setSelected(false);
                radioMnuLayoutAutoFitColumnsInWindow.setSelected(false);
                radioMnuLayoutUserDefined.setSelected(false);
                break;
            case PdfViewer.LAYOUT_SIDE_BY_SIDE_CONTINUOUS:
                btnContinuous.setSelected(false);
                btnSinglePage.setSelected(false);
                btnSideBySide.setSelected(false);
                btnSideBySideContinuous.setSelected(true);
                btnAutoFitColumnsInWindow.setSelected(false);
                btnCustomLayout.setSelected(false);

                radioMnuLayoutSinglePage.setSelected(false);
                radioMnuLayoutSinglePageContinuous.setSelected(false);
                radioMnuLayoutSideBySide.setSelected(false);
                radioMnuLayoutSideBySideContinuous.setSelected(true);
                radioMnuLayoutAutoFitColumnsInWindow.setSelected(false);
                radioMnuLayoutUserDefined.setSelected(false);
                break;
            case PdfViewer.LAYOUT_AUTO_FIT_COLUMNS_IN_WINDOW:
                btnContinuous.setSelected(false);
                btnSinglePage.setSelected(false);
                btnSideBySide.setSelected(false);
                btnSideBySideContinuous.setSelected(false);
                btnAutoFitColumnsInWindow.setSelected(true);
                btnCustomLayout.setSelected(false);

                radioMnuLayoutSinglePage.setSelected(false);
                radioMnuLayoutSinglePageContinuous.setSelected(false);
                radioMnuLayoutSideBySide.setSelected(false);
                radioMnuLayoutSideBySideContinuous.setSelected(false);
                radioMnuLayoutAutoFitColumnsInWindow.setSelected(true);
                radioMnuLayoutUserDefined.setSelected(false);
                break;
            default:
                btnContinuous.setSelected(false);
                btnSinglePage.setSelected(false);
                btnSideBySide.setSelected(false);
                btnSideBySideContinuous.setSelected(false);
                btnAutoFitColumnsInWindow.setSelected(false);
                btnCustomLayout.setSelected(true);

                radioMnuLayoutSinglePage.setSelected(false);
                radioMnuLayoutSinglePageContinuous.setSelected(false);
                radioMnuLayoutSideBySide.setSelected(false);
                radioMnuLayoutSideBySideContinuous.setSelected(false);
                radioMnuLayoutAutoFitColumnsInWindow.setSelected(false);
                radioMnuLayoutUserDefined.setSelected(true);
                break;

        }

        if (pageNum == 0
                || (pageNum == 1
                && viewer.getPageCount() == 1)) {
            return;
        }

        if (pageNum == viewer.getPageCount()) {
            btnNextPage.setEnabled(false);
            btnLastPage.setEnabled(false);

            mnuNextPageItem.setEnabled(false);
            mnuLastPageItem.setEnabled(false);

            if (viewer.getPageCount() > 1) {
                btnFirstPage.setEnabled(true);
                btnPreviousPage.setEnabled(true);

                mnuFirstPageItem.setEnabled(true);
                mnuPreviousPageItem.setEnabled(true);
            }
        } else if (viewer.getCurrentPage() == 1) {
            btnFirstPage.setEnabled(false);
            btnPreviousPage.setEnabled(false);

            mnuFirstPageItem.setEnabled(false);
            mnuPreviousPageItem.setEnabled(false);

            btnNextPage.setEnabled(true);
            btnLastPage.setEnabled(true);

            mnuNextPageItem.setEnabled(true);
            mnuLastPageItem.setEnabled(true);
        } else {
            btnFirstPage.setEnabled(true);
            btnPreviousPage.setEnabled(true);
            btnNextPage.setEnabled(true);
            btnLastPage.setEnabled(true);

            mnuFirstPageItem.setEnabled(true);
            mnuPreviousPageItem.setEnabled(true);
            mnuNextPageItem.setEnabled(true);
            mnuLastPageItem.setEnabled(true);
        }

        if (showGapsBetweenPages) {
            chkMnuShowGapsBetweenPages.setSelected(true);
        } else {
            chkMnuShowGapsBetweenPages.setSelected(false);
        }

        if (showCoverPageDuringSideBySide) {
            chkMnuShowCoverPageDuringSideBySide.setSelected(true);
        } else {
            chkMnuShowCoverPageDuringSideBySide.setSelected(false);
        }

        if (showPageBordersWhenNoPageGaps) {
            chkMnuShowPageBorders.setSelected(true);
        } else {
            chkMnuShowPageBorders.setSelected(false);
        }
    }

    public synchronized void onRenderError(Object sender,
            Throwable throwable) {
        /*String senderName = "";
        
         if (sender instanceof PdfViewer)
         {
         senderName = "Viewer ";
         }
         else if (sender instanceof PdfPrinter)
         {
         senderName = "Printer ";
         }
         else if (sender instanceof PdfDocument)
         {
         senderName = "";
         } 
        
         JOptionPane.showMessageDialog(this
         .getParent(),
         throwable.getMessage(),
         senderName + "Error Occurred",
         JOptionPane.ERROR_MESSAGE);*/

        throwable.printStackTrace();
    }

    public void onViewHistoryChange(PdfViewer viewer,
            boolean hasPreviousView, boolean hasNextView) {
        if (hasPreviousView) {
            mnuPreviousView.setEnabled(true);
        } else {
            mnuPreviousView.setEnabled(false);
        }

        if (hasNextView) {
            mnuNextView.setEnabled(true);
        } else {
            mnuNextView.setEnabled(false);
        }

    }
}
