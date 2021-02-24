/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.project.utility;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.core.KeywordAnalyzer;
import org.apache.lucene.analysis.miscellaneous.PerFieldAnalyzerWrapper;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.analysis.util.CharArraySet;

/**
 *
 * @author addie
 */
public class AnalyzerHelper {
           
    public static PerFieldAnalyzerWrapper getAnalyzer(boolean ignoreStopWords) {
        Map<String, Analyzer> analyzerPerField = new HashMap<String, Analyzer>();
        analyzerPerField.put("Type", new KeywordAnalyzer());
        analyzerPerField.put("DocType", new KeywordAnalyzer());
        analyzerPerField.put("stitle", new KeywordAnalyzer());
        analyzerPerField.put("title", new StandardAnalyzer());
        
//        String str = "able,able,about,above,almost,along,also,although,always,am,an,another,any,anyhow,anyway,are,as,at,away,be,best,both,but,by,can,could,do,due,during,each,either,else,elsewhere,etc,even,ever,few,for,from,further,furthermore,has,have,hello,hence,her,here,hereafter,hereby,herein,hereupon,herself,hi,him,himself,his,how,howbeit,however,if,in,inasmuch,inc,inner,insofar,instead,into,is,it,its,itself,just,lately,latterly,class,the,least,less,let,little,mainly,many,may,maybe,me,meanwhile,merely,might,more,moreover,most,mostly,much,must,my,myself,namely,near,neither,neverthe,less,nobody,nos,now,nowhere,off,often,oh,ok,okay,on,once,one,only,onto,of,other,ought,our,out,outside,overall,own,per,perhaps,please,plus,que,rather,really,right,same,self,selves,several,shall,she,should,show,since,so,some,somehow,someone,sometime,somewhat,somewhere,soon,still,such,sure,than,thank,that,their,them,themselves,then,thence,there,thereafter,thereby,therefore,therefrom,therein,thereof,thereon,theres,thereto,thereunder,thereupon,these,they,this,those,thus,to,too,took,toward,towards,truly,twice,unless,until,up,upon,upto,us,very,viz,was,we,well,what,whatever,whatsoever,when,whence,whenever,where,whereafter,whereas,whereby,wherein,whereon,whereupon,wherever,whether,which,whichever,while,who,whoever,whole,whom,whose,why,with,within,without,wonder,would,you,your,yours,yourself,yourselves";
        String str = "";
        if (!ignoreStopWords){
            str = "the,that";
        }
//        String str = "";
        List<String> stopWords = Arrays.asList(str.split(","));
       CharArraySet characterArraySet = new CharArraySet(stopWords, true);
        StandardAnalyzer sa = new StandardAnalyzer(characterArraySet);
//        //System.out.println("  -- Analyser Stop Words -- ");
        //System.out.println(sa.getStopwordSet().toString());
        PerFieldAnalyzerWrapper analyzer
                = new PerFieldAnalyzerWrapper(sa, analyzerPerField);
        return analyzer;
    }
    public static PerFieldAnalyzerWrapper gethighlighterAnalyzer() {
        Map<String, Analyzer> analyzerPerField = new HashMap<String, Analyzer>();
        analyzerPerField.put("Type", new KeywordAnalyzer());
        analyzerPerField.put("DocType", new KeywordAnalyzer());
        analyzerPerField.put("stitle", new KeywordAnalyzer());
        analyzerPerField.put("title", new StandardAnalyzer());

        String str = "able,about,above,almost,along,also,although,always,am,an,another,any,anyhow,anyway,are,as,at,away,be,best,both,but,by,can,could,do,due,during,each,either,else,elsewhere,etc,even,ever,few,for,from,further,furthermore,has,have,hello,hence,her,here,hereafter,hereby,herein,hereupon,herself,hi,him,himself,his,how,howbeit,however,if,in,inasmuch,inc,inner,insofar,instead,into,is,it,its,itself,just,lately,latterly,class,the,least,less,let,little,mainly,many,may,maybe,me,merely,might,more,moreover,most,mostly,much,must,my,myself,namely,near,neither,neverthe,less,nobody,nos,now,nowhere,off,often,oh,ok,okay,on,once,one,only,onto,of,other,ought,our,out,outside,overall,own,per,perhaps,please,plus,que,rather,really,right,same,self,selves,several,shall,she,should,show,since,so,some,somehow,someone,sometime,somewhat,somewhere,soon,still,such,sure,than,thank,that,their,them,themselves,then,thence,there,thereafter,thereby,therefore,therefrom,therein,thereof,thereon,theres,thereto,thereunder,thereupon,these,they,this,those,thus,to,too,took,toward,towards,truly,twice,unless,until,up,upon,upto,us,very,viz,was,we,well,what,whatever,whatsoever,when,whence,whenever,where,whereafter,whereas,whereby,wherein,whereon,whereupon,wherever,whether,which,whichever,while,who,whoever,whole,whom,whose,why,with,within,without,wonder,would,you,your,yours,yourself,yourselves";
//        String str = "a,an,the,that";
        List<String> stopWords = Arrays.asList(str.split(","));
        CharArraySet characterArraySet = new CharArraySet(stopWords, true);
        StandardAnalyzer sa = new StandardAnalyzer(characterArraySet);
//        //System.out.println("  -- Analyser Stop Words -- ");
        //System.out.println(sa.getStopwordSet().toString());
        PerFieldAnalyzerWrapper analyzer
                = new PerFieldAnalyzerWrapper(sa, analyzerPerField);
        return analyzer;
    }
}
