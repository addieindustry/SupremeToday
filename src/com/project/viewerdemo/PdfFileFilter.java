package com.project.viewerdemo;

import java.io.File;

class PdfFileFilter extends javax.swing.filechooser.FileFilter
{
    public boolean accept(File file)
    {
        String filename = file.getName();
        
        if (file.isDirectory())
        {
            return true;
        }
        else if (filename.toLowerCase().endsWith(".pdf"))
        {
            return true;
        }
        
        return false;
    }

    public String getDescription()
    {
        return "*.pdf";
    }
}