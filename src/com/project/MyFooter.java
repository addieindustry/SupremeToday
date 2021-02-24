//package com.project;
//
//import com.itextpdf.io.IOException;
//import com.itextpdf.io.font.FontConstants;
//import com.itextpdf.kernel.events.Event;
//import com.itextpdf.kernel.events.IEventHandler;
//import com.itextpdf.kernel.events.PdfDocumentEvent;
//import com.itextpdf.kernel.font.PdfFontFactory;
//import com.itextpdf.kernel.geom.Rectangle;
//import com.itextpdf.kernel.pdf.canvas.PdfCanvas;
//import com.itextpdf.text.Document;
//import com.itextpdf.text.Element;
//import com.itextpdf.text.Font;
//import com.itextpdf.text.Phrase;
//import com.itextpdf.text.pdf.ColumnText;
//import com.itextpdf.text.pdf.PdfContentByte;
//import com.itextpdf.text.pdf.PdfPageEventHelper;
//import com.itextpdf.text.pdf.PdfWriter;
//
//public class MyFooter extends PdfPageEventHelper implements IEventHandler {
//    protected com.itextpdf.layout.Document doc;
//    public MyFooter(com.itextpdf.layout.Document doc) {
//        this.doc = doc;
//    }
//
//    public void onEndPage(PdfWriter writer, Document document) {
//        PdfContentByte cb = writer.getDirectContent();
//        ColumnText.showTextAligned(cb, Element.ALIGN_CENTER, footer(),
//                (document.right() - document.left()) / 2 + document.leftMargin(),
//                document.top() + 10, 0);
//    }
//    private Phrase footer() {
//        Font ffont = new Font(Font.FontFamily.UNDEFINED, 5, Font.ITALIC);
//        Phrase p = new Phrase("this is a footer");
//        return p;
//    }
//
//    @Override
//    public void handleEvent(Event event) {
//        PdfDocumentEvent docEvent = (PdfDocumentEvent) event;
//        PdfCanvas canvas = new PdfCanvas(docEvent.getPage());
//        Rectangle pageSize = docEvent.getPage().getPageSize();
//        canvas.beginText();
//        try {
//            canvas.setFontAndSize(PdfFontFactory.createFont(FontConstants.HELVETICA_OBLIQUE), 5);
//        } catch (IOException | java.io.IOException e) {
//            e.printStackTrace();
//        }
//        canvas.moveText((pageSize.getRight() - doc.getRightMargin() - (pageSize.getLeft() + doc.getLeftMargin())) / 2 + doc.getLeftMargin(), pageSize.getTop() - doc.getTopMargin() + 10)
//                .showText("this is a header")
//                .moveText(0, (pageSize.getBottom() + doc.getBottomMargin()) - (pageSize.getTop() + doc.getTopMargin()) - 20)
//                .showText("this is a footer")
//                .endText()
//                .release();
//    }
//
////    @Override
////    public void handleEvent(Event event) {
////
////    }
//
////    @Override
////    public void handleEvent(Event event) {
////
////    }
//}
