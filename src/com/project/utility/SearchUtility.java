/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.project.utility;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Pattern;
import java.util.zip.DataFormatException;

import com.project.helper.Queries;
import com.project.helper.ServiceHelper;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.miscellaneous.PerFieldAnalyzerWrapper;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.CompressionTools;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.StoredField;
import org.apache.lucene.facet.FacetResult;
import org.apache.lucene.facet.Facets;
import org.apache.lucene.facet.FacetsCollector;
import org.apache.lucene.facet.FacetsConfig;
import org.apache.lucene.facet.taxonomy.FastTaxonomyFacetCounts;
import org.apache.lucene.facet.taxonomy.TaxonomyReader;
import org.apache.lucene.facet.taxonomy.directory.DirectoryTaxonomyReader;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexableField;
import org.apache.lucene.index.Term;
import org.apache.lucene.queryparser.classic.MultiFieldQueryParser;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.*;
import org.apache.lucene.search.SortField.Type;
import org.apache.lucene.search.highlight.Highlighter;
import org.apache.lucene.search.highlight.InvalidTokenOffsetsException;
import org.apache.lucene.search.highlight.QueryScorer;
import org.apache.lucene.search.highlight.SimpleFragmenter;
import org.apache.lucene.search.highlight.SimpleHTMLFormatter;
import org.apache.lucene.search.spell.SpellChecker;
import org.apache.lucene.search.spell.SuggestMode;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;

/**
 *
 * @author iconflux006
 */
public class SearchUtility {

    private String indexPath;
    private TaxonomyReader taxoReader;
    private PerFieldAnalyzerWrapper analyzer;
    private PerFieldAnalyzerWrapper hlAnalyzer;
    private IndexReader reader;

//    private String highlightPreTag = "<a class=\"highlight_class\" hreftag=\"#\">";
//    private String highlightPostTag = "</a>";
    private String highlightPreTag = "#hPreTag#";
    private String highlightPostTag = "#hPostTag#";
    private String fileSep = "\\";

    public SearchUtility(String indexPath) {
        this.indexPath = indexPath;
        if (!OSValidator.isWindows()) {
            this.fileSep = "/";
        }
//        String index = "E:\\Leena\\Projects\\vikasinfo\\Indexes";
    }

    public static String getMultiSearchQuery(String query) {
        try {
//            String[] fieldList = new String[]{"content"};
//            String[] fieldList = new String[]{"title", "content", "summary", "acts", "result","citation", "judge", "advocates"};
//            String[] fieldList = new String[]{"judgementHeader","content","summary","acts","result","citation"};
//            String[] fieldList = new String[]{"judgementHeader","content","summary","result","citation"};
            String[] fieldList = new String[]{"judgementHeader","content","summary","result"};

            if (Queries.IS_SUPREME_TODAY_APP == Boolean.FALSE){
                fieldList = new String[]{"judgementHeader", "content", "summary_new", "result", "citation"};
            }

            BooleanClause.Occur[] flags = new BooleanClause.Occur[fieldList.length];
            for (int i = 0; i < fieldList.length; i++) {
//                flags[i] = BooleanClause.Occur.MUST;
                flags[i] = BooleanClause.Occur.SHOULD;
            }
//            Query query1 = MultiFieldQueryParser.parse(query, fieldList, flags, AnalyzerHelper.getAnalyzer());

            HashMap<String,Float> boosts = new HashMap<String,Float>();
            boosts.put("title", Float.valueOf(10));
            boosts.put("acts", Float.valueOf(8));
            boosts.put("result", Float.valueOf(8));
            boosts.put("citation", Float.valueOf(8));
            if (Queries.IS_SUPREME_TODAY_APP == Boolean.FALSE){
                boosts.put("summary_new", Float.valueOf(7));
            }else {
                boosts.put("summary", Float.valueOf(7));
            }
            boosts.put("judge", Float.valueOf(3));
            boosts.put("advocates", Float.valueOf(3));
            boosts.put("content", Float.valueOf(1));
            MultiFieldQueryParser queryParser = new MultiFieldQueryParser(fieldList, AnalyzerHelper.getAnalyzer(true), boosts);
            queryParser.setDefaultOperator(QueryParser.Operator.AND);
            Query query1 = queryParser.parse(query);

//            Query query1 = MultiFieldQueryParser.parse(query, fieldList, flags, new StandardAnalyzer());
//            MultiFieldQueryParser mfqp = new MultiFieldQueryParser(fieldList, AnalyzerHelper.getAnalyzer(), new Map<String, Float>());
            return query1.toString();
        } catch (ParseException ex) {
            Logger.getLogger(ServiceHelper.class.getName()).log(Level.SEVERE, null, ex);
        }
        return "";
    }

    public static String getSingleFieldSearchQuery(String query, String singleField) {
        try {
//            String[] fieldList = new String[]{"content"};
//            String[] fieldList = new String[]{"title", "content", "summary", "acts", "result","citation", "judge", "advocates"};
//            String[] fieldList = new String[]{"judgementHeader","content","summary","acts","result","citation"};
            String[] fieldList = new String[]{singleField};

            BooleanClause.Occur[] flags = new BooleanClause.Occur[fieldList.length];
            for (int i = 0; i < fieldList.length; i++) {
//                flags[i] = BooleanClause.Occur.MUST;
                flags[i] = BooleanClause.Occur.SHOULD;
            }
//            Query query1 = MultiFieldQueryParser.parse(query, fieldList, flags, AnalyzerHelper.getAnalyzer());

            HashMap<String,Float> boosts = new HashMap<String,Float>();
            boosts.put("title", Float.valueOf(10));
            boosts.put("acts", Float.valueOf(8));
            boosts.put("result", Float.valueOf(8));
            boosts.put("citation", Float.valueOf(8));
            if (Queries.IS_SUPREME_TODAY_APP == Boolean.FALSE){
                boosts.put("summary_new", Float.valueOf(7));
            }else {
                boosts.put("summary", Float.valueOf(7));
            }
            boosts.put("judge", Float.valueOf(3));
            boosts.put("advocates", Float.valueOf(3));
            boosts.put("content", Float.valueOf(1));
            MultiFieldQueryParser queryParser = new MultiFieldQueryParser(fieldList, AnalyzerHelper.getAnalyzer(true), boosts);
            queryParser.setDefaultOperator(QueryParser.Operator.AND);
            Query query1 = queryParser.parse(query);

//            Query query1 = MultiFieldQueryParser.parse(query, fieldList, flags, new StandardAnalyzer());
//            MultiFieldQueryParser mfqp = new MultiFieldQueryParser(fieldList, AnalyzerHelper.getAnalyzer(), new Map<String, Float>());
            return query1.toString();
        } catch (ParseException ex) {
            Logger.getLogger(ServiceHelper.class.getName()).log(Level.SEVERE, null, ex);
        }
        return "";
    }

    public String getSpellSuggestion(String field, String content) throws Exception {
        boolean bool = false;
        PerFieldAnalyzerWrapper analyzer = AnalyzerHelper.getAnalyzer(true);
        QueryParser parser = new QueryParser(field, analyzer);
        Query query = parser.parse(content);
        String newContent = query.toString().replace(field + ":", "");
        for (String word : content.split(" ")) {
            IndexReader reader = DirectoryReader.open(FSDirectory.open(Paths.get(this.indexPath)));
            Directory dir = FSDirectory.open(Paths.get(this.indexPath + this.fileSep + "spell"));
            SpellChecker spellchecker = new SpellChecker(dir);
            String[] suggestions = spellchecker.suggestSimilar(word.trim(), 1, reader, field, SuggestMode.SUGGEST_MORE_POPULAR);
            //  String[] suggestions = spellchecker.suggestSimilar("murder", 5);
            if (suggestions.length > 0) {
                bool = true;
                newContent = newContent.replace(word, suggestions[0].trim());
            }
        }
        if (bool) {
            return newContent;
        } else {
            return "";
        }
    }

    public JsonObject search(String queryString, int start, int rows, String sortBy, String facet_fields, String fields, String hl_fields, String filter_query, String hl_query) throws Exception {
        BooleanQuery.setMaxClauseCount( Integer.MAX_VALUE );
        if (!OSValidator.isWindows()) {
            indexPath = indexPath.replace("\\", "/");
        }
        reader = DirectoryReader.open(FSDirectory.open(Paths.get(indexPath)));
        boolean isHl = true;
        if (hl_fields.equals("false")) {
            hl_fields = "";
            isHl = false;
        }
        String field = "";
        String queries = null;

        IndexSearcher searcher = new IndexSearcher(reader);
//        PerFieldAnalyzerWrapper 
        analyzer = AnalyzerHelper.getAnalyzer(false);
        hlAnalyzer = AnalyzerHelper.gethighlighterAnalyzer();
        BufferedReader in = null;
        if (queries != null) {
            in = Files.newBufferedReader(Paths.get(queries), StandardCharsets.UTF_8);
        } else {
            in = new BufferedReader(new InputStreamReader(System.in, StandardCharsets.UTF_8));
        }
        QueryParser parser = new QueryParser(field, analyzer);
        QueryParser hlParser = new QueryParser(field, hlAnalyzer);
        Query query = parser.parse(queryString);
        Query hlQuery = query;
        if (!hl_query.isEmpty()) {
            hlQuery = hlParser.parse(hl_query);
        }
        JsonObject retData = doSearch(in, searcher, query, start, rows, sortBy, fields, facet_fields, filter_query, hl_fields, isHl,hlQuery);
        reader.close();
        return retData;
    }

    public JsonObject doSearch(BufferedReader in, IndexSearcher searcher, Query query,
            int start, int rows, String sortBy, String fields, String facet_fields, String filter_query,
            String hl_fields, boolean isHl,Query hlQuery) throws IOException, ParseException,
            InvalidTokenOffsetsException, DataFormatException {
        JsonObject data = new JsonObject();
        TopDocs results = null;
        Filter filter = null;

//bq.add(new BooleanClause(keywordQuery, BooleanClause.Occur.MUST))
        if (filter_query != null && !filter_query.isEmpty()) {
            Query q = new QueryParser("", analyzer).parse(filter_query);
            filter = new QueryWrapperFilter(q);
        }
        if (facet_fields != null && !facet_fields.isEmpty()) {
            taxoReader = new DirectoryTaxonomyReader(FSDirectory.open(Paths.get(indexPath + this.fileSep + "taxo")));
            FacetsCollector fc = new FacetsCollector();
            FacetsCollector.search(searcher, query, filter, 10, fc);
//            FacetsCollector.search(searcher, query, filter, 10, fc);

            FacetsConfig facetConfig = new FacetsConfig();
            String[] facet_fields_array = facet_fields.split(",");
            for (String facet_field : facet_fields_array) {
                if (facet_field.equalsIgnoreCase("judge") || facet_field.equalsIgnoreCase("acts")) {
                    facetConfig.setMultiValued(facet_field, true);
                    facetConfig.setHierarchical(facet_field, false);
                } else {
                    facetConfig.setMultiValued(facet_field, false);
                    facetConfig.setHierarchical(facet_field, true);
                }
            }
            Facets facets = new FastTaxonomyFacetCounts(taxoReader, facetConfig, fc);
            JsonObject jo = new JsonObject();
            Gson gson = new Gson();
            for (String facet_field : facet_fields_array) {
                FacetResult fResult = facets.getTopChildren(1, facet_field);
                if (fResult != null) {
                    jo.add(facet_field, gson.fromJson(gson.toJson(facets.getTopChildren(1000, facet_field).labelValues), JsonElement.class));
                } else {
                    jo.add(facet_field, new JsonArray());
                }
            }
            data.add("facets", jo);
        }
//        Filter f=new FieldCacheTermsFilter("finalyear","1996");
//         TermQuery categoryQuery = new TermQuery(new Term("finalyear", "1996 1997")); 
//         QueryParser parser = new QueryParser((), null)
//        Fie

        if (fields != null && !fields.isEmpty()) {
            if ((sortBy != null || !sortBy.isEmpty()) && sortBy.trim().split(" ").length == 3) {
                String[] sortArray = sortBy.trim().split(" ");
                boolean reverse = true;
                if (sortArray[2].equalsIgnoreCase("asc")) {
                    reverse = false;
                }
                Type t = Type.STRING;
                if (sortArray[1].equalsIgnoreCase(Type.INT.toString())) {
                    t = Type.INT;
                }

                SortField sortField = new SortField(sortArray[0], t, reverse);
                Sort sort = new Sort(sortField);
                //results = searcher.search(query, filter, start + rows, Sort.RELEVANCE);
                results = searcher.search(query, filter, start + rows, sort);
            } else {

                results = searcher.search(query, filter, start + rows);
            }
            //SimpleHTMLFormatter htmlFormatter = new SimpleHTMLFormatter("<a class=\"hl_class\">", "</a>");
            SimpleHTMLFormatter htmlFormatter = new SimpleHTMLFormatter("<strong>", "</strong>");

//            QueryScorer qs = new QueryScorer(query);
//            qs.setExpandMultiTermQuery(true);
            QueryScorer qs1 = new QueryScorer(hlQuery);
            qs1.setExpandMultiTermQuery(true);
            Highlighter highlighter;// = new Highlighter(htmlFormatter, qs);

            //FOR JUDGEMENT HIGHLIGHTING
            if (rows == 1) {
                htmlFormatter = new SimpleHTMLFormatter(highlightPreTag, highlightPostTag);
                highlighter = new Highlighter(htmlFormatter, qs1);
//                highlighter.setTextFragmenter(new SimpleFragmenter(Integer.MAX_VALUE));
                highlighter.setTextFragmenter(new SimpleFragmenter(Integer.MAX_VALUE));
                highlighter.setMaxDocCharsToAnalyze(Integer.MAX_VALUE);
            } else {
                highlighter = new Highlighter(htmlFormatter, qs1);
                highlighter.setTextFragmenter(new SimpleFragmenter(100));
            }

            ScoreDoc[] hits = results.scoreDocs;

            int numTotalHits = results.totalHits;
            data.addProperty("numFound", numTotalHits);
            data.addProperty("q", query.toString());
            int end = Math.min(numTotalHits, start + rows);
            JsonArray docFound = new JsonArray();
            fields = fields + ",";
            for (int i = start; i < end; i++) {
                Document doc = searcher.doc(hits[i].doc);
                String frag = null;

                if (hl_fields != null && !hl_fields.isEmpty()) {
                    if (!doc.get("DocType").toString().equals("Commentary")) {
                        frag = "";
                        for (String hl_field : hl_fields.split(",")) {
//                            if (doc.get(hl_field) != null && !doc.get(hl_field).isEmpty()) {
                            if (doc.getBinaryValue(hl_field) != null) {
                                String text = "";
                                if (hl_field.contains("_store")) {
//                                    text = EncryptionHelper.Decrypt(new StringBuilder(doc.get(hl_field)), doc.get("caseId"));
                                    text = EncryptionHelper.Decrypt(new StringBuilder(CompressionTools.decompressString(doc.getBinaryValue(hl_field)).toString()), doc.get("caseId"));
                                } else {
                                    text = doc.get(hl_field);
                                }
                                text = text.replaceAll("<[^>]*>", "");
                                text = text.replaceAll("&nbsp;", " ");
                                TokenStream tokenStream = analyzer.tokenStream(hl_field.replace("_store", ""), text);
                                //String str = highlighter.getBestFragments(tokenStream, text, 3, "...");
                                if (!hl_field.equals("caseId")) {
//                                frag = frag + " " + highlighter.getBestFragment(analyzer, hl_field.replace("_store", ""), text);
//                                String[] str = highlighter.getBestFragments(analyzer, hl_field.replace("_store", ""), text, 5);
//                                 frag = frag + " " + Arrays.toString(str); 
//                                    System.out.println(text);
                                    String hlFrag = highlighter.getBestFragments(tokenStream, text, 5, "...");
                                    if (hlFrag != null && !hlFrag.isEmpty()) {
                                        frag = frag + " " + hlFrag;
                                    }
                                }
                            }
                        }
                    }
                }

                JsonObject jo = new JsonObject();
                List<IndexableField> labels = doc.getFields();
//                        //System.out.println(doc.get("judgement_store").toString());
                for (IndexableField ifld : labels) {
//                    //System.out.println(ifld.name());
                    if (fields.contains("all" + ",") || fields.contains(ifld.name() + ",")) {
                        if (ifld.name().toLowerCase().contains("store")) {
//                        //System.out.println(EncryptionHelper.Decrypt(new StringBuilder(doc.get("judgement_store").toString()), "2614"));
//                            //System.out.println(ifld.name() + " - " + doc.get("caseId"));
                            String strContentHighlight = "";
//                            COMMENTTED BELOW TO STOP HLTAG IN ALL FIELDS
                            if (rows == 1 && isHl) {
                                strContentHighlight = MakeContentHighlight(EncryptionHelper.Decrypt(new StringBuilder(CompressionTools.decompressString(doc.getBinaryValue(ifld.name())).toString()), doc.get("caseId")), ifld.name(), hlAnalyzer, highlighter, hl_fields, hlQuery);
//                                strContentHighlight = MakeContentHighlight(CompressionTools.decompressString(doc.getBinaryValue(ifld.name())).toString(), ifld.name(), hlAnalyzer, highlighter, hl_fields, hlQuery);
//                                strContentHighlight = MakeContentHighlight(EncryptionHelper.Decrypt(new StringBuilder(doc.get(ifld.name())), doc.get("caseId")), ifld.name(), hlAnalyzer, highlighter, hl_fields, hlQuery);
                            } else {
                                strContentHighlight = EncryptionHelper.Decrypt(new StringBuilder(CompressionTools.decompressString(doc.getBinaryValue(ifld.name())).toString()), doc.get("caseId"));
//                                strContentHighlight = CompressionTools.decompressString(doc.getBinaryValue(ifld.name())).toString();
//                                strContentHighlight = doc.get(ifld.name());
//                                strContentHighlight = EncryptionHelper.Decrypt(new StringBuilder(doc.get(ifld.name())), doc.get("caseId"));
                            }
                            jo.addProperty(ifld.name(), strContentHighlight);
                        } else {
                            String strContentHighlight = doc.get(ifld.name());
//                            if (rows == 1 && isHl) {
//                                strContentHighlight = MakeContentHighlight(doc.get(ifld.name()), ifld.name(), analyzer, highlighter, hl_fields);
//                            } else {
//                                strContentHighlight = doc.get(ifld.name());
//                            }
                            jo.addProperty(ifld.name(), strContentHighlight);
                        }


//                        if (ifld.name().toLowerCase().contains("store") && !doc.get(ifld.name()).isEmpty()) {
////                        //System.out.println(EncryptionHelper.Decrypt(new StringBuilder(doc.get("judgement_store").toString()), "2614"));
////                            //System.out.println(ifld.name() + " - " + doc.get("caseId"));
//                            String strContentHighlight = "";
////                            COMMENTTED BELOW TO STOP HLTAG IN ALL FIELDS
//                            if (rows == 1 && isHl) {
//                                strContentHighlight = MakeContentHighlight(EncryptionHelper.Decrypt(new StringBuilder(doc.get(ifld.name())), doc.get("caseId")), ifld.name(), hlAnalyzer, highlighter, hl_fields, hlQuery);
//                            } else {
//                                strContentHighlight = EncryptionHelper.Decrypt(new StringBuilder(doc.get(ifld.name())), doc.get("caseId"));
//                            }
//                            jo.addProperty(ifld.name(), strContentHighlight);
//                        } else {
//                            String strContentHighlight = doc.get(ifld.name());
////                            if (rows == 1 && isHl) {
////                                strContentHighlight = MakeContentHighlight(doc.get(ifld.name()), ifld.name(), analyzer, highlighter, hl_fields);
////                            } else {
////                                strContentHighlight = doc.get(ifld.name());
////                            }
//                            jo.addProperty(ifld.name(), strContentHighlight);
//                        }

                    }
                }
                if (frag != null) {
                    jo.addProperty("hl", frag);
                }
                docFound.add(jo);
            }
            data.add("docs", docFound);
        }
        return data;
//        //System.out.println(data);
    }

//    public String MakeContentHighlight(String inputtext, String hl_field, PerFieldAnalyzerWrapper analyzer, Highlighter highlighter, String hl_fields) throws IOException, IOException, IOException, InvalidTokenOffsetsException {
//        TokenStream tokenStream = analyzer.tokenStream(hl_field.replace("_store", ""), inputtext);
//        //String str = highlighter.getBestFragments(tokenStream, text, 3, "...");
//        String hl = highlighter.getBestFragment(tokenStream, inputtext);
//        if (hl != null) {
//            return hl.toString();
//        } else {
//            return inputtext;
//        }
//        //String str = highlighter.getBestFragments(tokenStream, inputtext, 3, "...");
//        //return null;
//    }
    public String MakeContentHighlight(String inputtext, String hl_field, 
            PerFieldAnalyzerWrapper analyzer, Highlighter highlighter, 
            String hl_fields, Query query) throws IOException, InvalidTokenOffsetsException {
//                                        System.out.println("hlQuery:"+query.toString());
//                                        System.out.println("hlQuery:"+inputtext);

//        TokenStream tokenStream = analyzer.tokenStream(hl_field.replace("_store", ""), inputtext);
        String hl = highlighter.getBestFragment(analyzer, hl_field, inputtext);
//        String hl = highlighter.getBestFragment(tokenStream, inputtext);
        if (hl != null) {
            return hl.toString();
        } else {
            if (hl_field.contains("content")) {
                try {
                    HashSet terms = new HashSet();
                    query.extractTerms(terms);
                    int i = 0;
                    for (Iterator iter = terms.iterator(); iter.hasNext();) {
                        i++;
                        Term term = (Term) iter.next();

                        if (term.text().trim().length()>1){
                            String regex = "\\s" + term.text() + "\\s";
                            inputtext = Pattern.compile(regex, Pattern.CASE_INSENSITIVE).matcher(inputtext).replaceAll(" " + highlightPreTag + term.text() + highlightPostTag + " ");
                        }



//                        String regex = "\\b" + term.text() + "\\b";
//                        inputtext = Pattern.compile(regex, Pattern.CASE_INSENSITIVE).matcher(inputtext).replaceAll(highlightPreTag + term.text() + highlightPostTag);


//                        String regex = " " + term.text() + " ";
//                        inputtext = Pattern.compile(regex, Pattern.CASE_INSENSITIVE).matcher(inputtext).replaceAll(" "+highlightPreTag + term.text() + highlightPostTag+" ");
                    }
                } catch (Exception ex) {
                    return inputtext;
                }
            }
            return inputtext;
        }
    }

}
