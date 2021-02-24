/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.project;

import com.project.helper.CommanHelper;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.event.*;
import java.awt.print.*;
import java.text.MessageFormat;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.border.*;
import javax.swing.text.html.HTMLEditorKit;

class PrintPreview extends JFrame implements ActionListener, ChangeListener, ItemListener {

    JButton printBtn = new JButton("Print"),
            printThisPage = new JButton("Print Current Page"),
            cancel = new JButton("Close");
    Pageable pg = null;
    double scale = 1.0;
    JSlider slider = new JSlider();
    Page page[] = null;
    JComboBox jcb = new JComboBox();
    CardLayout cl = new CardLayout();
    JPanel p = new JPanel(cl);
    JButton back = new JButton("<<"), forward = new JButton(">>");
    JEditorPane tp = null;
    String html = "";
    JSplitPane sp = null;

    public void start() {
//     super("Test of Print Preview");
        sp = new JSplitPane(JSplitPane.VERTICAL_SPLIT);

        sp.setTopComponent(createTextPane());
//        sp.setBottomComponent(createTable());
        java.awt.Dimension d = this.getToolkit().getScreenSize();
        this.setSize(d.width / 2, d.height);
//        this.setSize(0, 0);
        this.getContentPane().add(sp);
        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
//        this.set
        this.setVisible(true);

        sp.setDividerLocation(0.5);

        this.hide();

        this.validate();
        try {
            Thread.sleep(3000L);
        } catch (InterruptedException ex) {
            Logger.getLogger(PrintPreview.class.getName()).log(Level.SEVERE, null, ex);
        }
        PrinterJob pj = PrinterJob.getPrinterJob();
        javax.print.attribute.HashPrintRequestAttributeSet att
                = new javax.print.attribute.HashPrintRequestAttributeSet();
        new PrintPreview(tp.getPrintable(new MessageFormat(""),
                new MessageFormat("{0}")), pj.getPageFormat(att));
        this.dispose();
    }

    private JPanel createTextPane() {
        tp = new JEditorPane("text/html", "");
//String imgsrc = PrintPreview.class.getClassLoader().get;
//        System.out.println("Source:" + ClassLoader.getSystemResource("t.png").toString());
        HTMLEditorKit kit = new HTMLEditorKit();
        tp.setEditorKit(kit);
        tp.setEditable(false);
        try {
//            tp.setVi;
//            tp.setPage(new java.io.File("prod02.htm").toURI().toURL());
//            tp.setPage(new java.io.File("C:/Users/ronak/Desktop/t.html").toURI().toURL());
//            tp.setPage(new java.io.File("C:/Users/iconflux006/Desktop/18689.htm").toURI().toURL());
////            tp.setPage(new java.io.File("C:\\Users\\iconflux006\\Desktop\\abc.html").toURI().toURL());
//            tp.setPage(new java.io.File("C:\\Users\\iconflux006\\Desktop\\abc.html").toURI().toURL());
//            tp.setPage("<html><head><title>Example of a simple HTML page</title></head><body>Hello</body></html>");
//            tp.setPage(new java.io.File("C:/Users/ronak/Desktop/sample.pdf").toURI().toURL());
//            String data = "<img src=\"C:/Users/ronak/Desktop/test.png\" >";
//            tp.setText("<html><h1><img src=\"" + ClassLoader.getSystemResource("t.png").toString() + "\" /></h1></html>");
//            tp.setText("<html><head><title>Example of a simple HTML page</title></head><body>Hello</body></html>");
//            tp.setText(this.html);
//            System.out.println("this.html");
//            System.out.println(this.html);
//            tp.setText("<div style=\"width: 400px;text-align: center\" ><img src=\"http://localhost:8989/img/LogoHeader.JPG\" /> </div>" +this.html);
//            System.out.println(this.html);

//            CommanHelper.writeFile("log.txt", "<html><head><meta charset=\"UTF-8\"></head><body><div style=\"height: 30px;background: #741819;width: 200px;color: white;line-height: 30px;text-align: center;font-weight: bold;font-size: 19px;\">SUPREME TODAY</div>" +this.html+"</body></html>");
            tp.setText("<html><head><meta charset=\"UTF-8\"></head><body><div style=\"height: 21px;background: #741819;width: 133px;color: white;line-height: 20px;text-align: center;font-weight: bold;font-size: 14px;\">SUPREME TODAY</div>" + this.html + "</body></html>");
        } catch (Exception ex) {
        }
        JButton ps = new JButton("Page Setup"), b = new JButton("Preview Text");
        ps.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // display/center the jdialog when the button is pressed
//                JDialog d = new JDialog(frame, "Hello", true);
//                d.setLocationRelativeTo(frame);
//                d.setVisible(true);
                PrinterJob pj = PrinterJob.getPrinterJob();
                javax.print.attribute.HashPrintRequestAttributeSet att
                        = new javax.print.attribute.HashPrintRequestAttributeSet();
                new PrintPreview(tp.getPrintable(new MessageFormat(""), new MessageFormat("{0}")), pj.getPageFormat(att));
            }
        });

        b.addActionListener(this);
        JPanel p = new JPanel(new java.awt.BorderLayout()), top = new JPanel(new java.awt.FlowLayout());
        top.add(ps);
        top.add(b);
        p.add(top, "North");
        p.add(new JScrollPane(tp), "Center");

//        tp.hide();
        return p;
    }

    public PrintPreview(String html) {
        this.html = html;
    }

    public PrintPreview(Pageable pg) {
        super("Print Preview");
        this.pg = pg;
        createPreview();
    }

    public PrintPreview(final Printable pr, final PageFormat p) {
        super("Print Preview");
        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
//        this.add
//        sp.addAncestorListener(new AncestorListener() {
//
//            @Override
//            public void ancestorAdded(AncestorEvent event) {
//                throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
//            }
//
//            @Override
//            public void ancestorRemoved(AncestorEvent event) {
//                System.out.println("ancestorRemoved");
////                throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
//            }
//
//            @Override
//            public void ancestorMoved(AncestorEvent event) {
//                throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
//            }
//        });
        this.pg = new Pageable() {
            public int getNumberOfPages() {
                Graphics g = new java.awt.image.BufferedImage(2, 2, java.awt.image.BufferedImage.TYPE_INT_RGB).getGraphics();
//                Graphics g = new java.awt.image.BufferedImage(100, 100, java.awt.image.BufferedImage.TYPE_INT_RGB).getGraphics();
//                g.
//                ImageIcon i = new ImageIcon("");

                int n = 0;
//                pr.
//                p.
//                pr.printBtn(g, p, n)

                try {
                    while (pr.print(g, p, n) == pr.PAGE_EXISTS) {
//                        g.se
//                        g.set
                        n++;
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
                return n;
            }

            public PageFormat getPageFormat(int x) {
                return p;
            }

            public Printable getPrintable(int x) {
                return pr;
            }
        };
        createPreview();
    }

    private void createPreview() {
        page = new Page[pg.getNumberOfPages()];

        FlowLayout fl = new FlowLayout();
        PageFormat pf = pg.getPageFormat(0);
        Dimension size = new Dimension((int) pf.getPaper().getWidth(), (int) pf.getPaper().getHeight());
        if (pf.getOrientation() != PageFormat.PORTRAIT) {
            size = new Dimension(size.height, size.width);
        }
        JPanel temp = null;
        for (int i = 0; i < page.length; i++) {
            jcb.addItem("" + (i + 1));
            page[i] = new Page(i, size);
            p.add("" + (i + 1), new JScrollPane(page[i]));
        }
        setTopPanel();
        this.getContentPane().add(p, "Center");
        Dimension d = this.getToolkit().getScreenSize();
        this.setSize(d.width, d.height - 60);
        slider.setSize(this.getWidth() / 2, slider.getPreferredSize().height);
        this.setVisible(true);
        page[jcb.getSelectedIndex()].refreshScale();
    }

    private void setTopPanel() {
        FlowLayout fl = new FlowLayout();
        GridBagLayout gbl = new GridBagLayout();
        GridBagConstraints gbc = new GridBagConstraints();
        JPanel topPanel = new JPanel(gbl), temp = new JPanel(fl);
        slider.setBorder(new TitledBorder("Percentage Zoom"));
        slider.setPaintTicks(true);
        slider.setPaintLabels(true);
        slider.setMinimum(0);
        slider.setMaximum(500);
        slider.setValue(100);
        slider.setMinorTickSpacing(20);
        slider.setMajorTickSpacing(100);
        slider.addChangeListener(this);
        back.addActionListener(this);
        forward.addActionListener(this);
        back.setEnabled(false);
        forward.setEnabled(page.length > 1);
        gbc.gridx = 0;
        gbc.gridwidth = 1;
        gbl.setConstraints(slider, gbc);
        topPanel.add(slider);
        temp.add(back);
        temp.add(jcb);
        temp.add(forward);
        temp.add(cancel);
        temp.add(printBtn);
//        temp.add(printThisPage);
        gbc.gridx = 1;
        gbc.gridwidth = 2;
        gbl.setConstraints(temp, gbc);
        topPanel.add(temp);
        printBtn.addActionListener(this);
        printThisPage.addActionListener(this);
        cancel.addActionListener(this);
        jcb.addItemListener(this);
        printBtn.setMnemonic('P');
        cancel.setMnemonic('C');
        printThisPage.setMnemonic('U');
        this.getContentPane().add(topPanel, "North");
    }

    public void itemStateChanged(ItemEvent ie) {
        cl.show(p, (String) jcb.getSelectedItem());
        page[jcb.getSelectedIndex()].refreshScale();
        back.setEnabled(jcb.getSelectedIndex() != 0);
        forward.setEnabled(jcb.getSelectedIndex() != jcb.getItemCount() - 1);
        this.validate();
    }

    public void actionPerformed(ActionEvent ae) {
        Object o = ae.getSource();
        if (o == printBtn) {
            try {
                PrinterJob pj = PrinterJob.getPrinterJob();
                pj.defaultPage(pg.getPageFormat(0));
                pj.setPageable(pg);
                if (pj.printDialog()) {
                    pj.print();
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(null, ex.toString(), "Error in Printing", 1);
            }
        } else if (o == printThisPage) {
            printCurrentPage();
        } else if (o == back) {
            jcb.setSelectedIndex(jcb.getSelectedIndex() == 0 ? 0 : jcb.getSelectedIndex() - 1);
            if (jcb.getSelectedIndex() == 0) {
                back.setEnabled(false);
            }
        } else if (o == forward) {
            jcb.setSelectedIndex(jcb.getSelectedIndex() == jcb.getItemCount() - 1 ? 0 : jcb.getSelectedIndex() + 1);
            if (jcb.getSelectedIndex() == jcb.getItemCount() - 1) {
                forward.setEnabled(false);
            }
        } else if (o == cancel) {
            this.dispose();
        }
    }

    public void printCurrentPage() {
        try {
            PrinterJob pj = PrinterJob.getPrinterJob();
            pj.defaultPage(pg.getPageFormat(0));
            pj.setPrintable(new PsuedoPrintable());

            javax.print.attribute.HashPrintRequestAttributeSet pra
                    = new javax.print.attribute.HashPrintRequestAttributeSet();

            if (pj.printDialog(pra)) {
                pj.print(pra);
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, ex.toString(), "Error in Printing", 1);
        }
    }

    public void stateChanged(ChangeEvent ce) {
        double temp = (double) slider.getValue() / 100.0;
        if (temp == scale) {
            return;
        }
        if (temp == 0) {
            temp = 0.01;
        }
        scale = temp;
        page[jcb.getSelectedIndex()].refreshScale();
        this.validate();
    }

    class Page extends JLabel {

        final int n;
        final PageFormat pf;
        java.awt.image.BufferedImage bi = null;
        Dimension size = null;

        public Page(int x, Dimension size) {
            this.size = size;
            bi = new java.awt.image.BufferedImage(size.width, size.height, java.awt.image.BufferedImage.TYPE_INT_RGB);
            n = x;
            pf = pg.getPageFormat(n);

            Graphics g = bi.getGraphics();
            Color c = g.getColor();
            g.setColor(Color.white);
            g.fillRect(0, 0, (int) pf.getWidth(), (int) pf.getHeight());
            g.setColor(c);
            try {
                g.clipRect(0, 0, (int) pf.getWidth(), (int) pf.getHeight());
                pg.getPrintable(n).print(g, pf, n);
            } catch (Exception ex) {
            }
            this.setIcon(new ImageIcon(bi));
        }

        public void refreshScale() {
            if (scale != 1.0) {
                this.setIcon(new ImageIcon(bi.getScaledInstance((int) (size.width * scale), (int) (size.height * scale), Image.SCALE_FAST)));
            } else {
                this.setIcon(new ImageIcon(bi));
            }
            this.validate();
        }
    }

    class PsuedoPrintable implements Printable {

        public int print(Graphics g, PageFormat fmt, int index) {
            if (index > 0) {
                return Printable.NO_SUCH_PAGE;
            }
            int n = jcb.getSelectedIndex();
            try {
                return pg.getPrintable(n).print(g, fmt, n);
            } catch (Exception ex) {
            }
            return Printable.PAGE_EXISTS;
        }
    }

}
