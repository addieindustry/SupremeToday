package com.project;

import com.itextpdf.html2pdf.attach.ITagWorker;
import com.itextpdf.html2pdf.attach.ProcessorContext;
import com.itextpdf.html2pdf.attach.impl.DefaultTagWorkerFactory;
import com.itextpdf.html2pdf.attach.impl.tags.HtmlTagWorker;
import com.itextpdf.html2pdf.html.TagConstants;
import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.styledxmlparser.node.IElementNode;
//import com.itextpdf.text.Document;
import com.itextpdf.layout.Document;
import com.project.helper.Queries;

public class ITextProperties {

    public static class CustomTagWorkerFactory extends DefaultTagWorkerFactory {
        public ITagWorker getCustomTagWorker(IElementNode tag, ProcessorContext context) {
            if (TagConstants.HTML.equals(tag.name())) {
                return new ZeroMarginHtmlTagWorker(tag, context);
            }
            return null;
        }
    }

    public static class ZeroMarginHtmlTagWorker extends HtmlTagWorker {
        public ZeroMarginHtmlTagWorker(IElementNode element, ProcessorContext context) {
            super(element, context);
            Document doc = (Document) getElementResult();
            doc.setMargins(Queries.PRINT_SETTING_MODEL.getMarginLeft(), Queries.PRINT_SETTING_MODEL.getMarginRight(), Queries.PRINT_SETTING_MODEL.getMarginTop(), Queries.PRINT_SETTING_MODEL.getMarginBottom());
        }
    }

}
