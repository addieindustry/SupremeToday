/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.project.utility;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.core.SimpleAnalyzer;
import org.apache.lucene.analysis.miscellaneous.PerFieldAnalyzerWrapper;
import org.apache.lucene.codecs.lucene50.Lucene50Codec;
import org.apache.lucene.codecs.lucene50.Lucene50StoredFieldsFormat.Mode;
import org.apache.lucene.document.*;
import org.apache.lucene.document.Field.Store;
import org.apache.lucene.facet.FacetField;
import org.apache.lucene.facet.FacetsConfig;
import org.apache.lucene.facet.taxonomy.directory.DirectoryTaxonomyWriter;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.IndexableField;
import org.apache.lucene.index.MultiFields;
import org.apache.lucene.index.Terms;
import org.apache.lucene.index.TermsEnum;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.*;
import org.apache.lucene.search.spell.LuceneDictionary;
import org.apache.lucene.search.spell.PlainTextDictionary;
import org.apache.lucene.search.spell.SpellChecker;
import org.apache.lucene.search.spell.SuggestMode;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.BytesRef;
import org.apache.lucene.util.Version;

/**
 *
 * @author Vishal
 */
public class IndexCreator {

    String indexPath = "";
//    String data = "";
//    boolean create;
    IndexWriter writer;
    DirectoryTaxonomyWriter taxoWriter;
    FacetsConfig facetConfig;
    private static final byte[] KEY = new byte[] { 'T', 'h', 'e', 'B', 'e', 's', 't','S', 'e', 'c', 'r','e', 't', 'K', 'e', 'y' };

    public IndexCreator(String indexPath, boolean create) {
        this.indexPath = indexPath;
        // this.create = create;

    }

    public void open(boolean create) {
        try {

//            ////System.out.println(indexPath);
            String taxonomyDirectory = indexPath + File.separator+"taxo";

            if (!new File(taxonomyDirectory).exists()) {
                new File(taxonomyDirectory).mkdirs();
            }
            Directory dir = FSDirectory.open(Paths.get(indexPath));
            Directory FacetDir = FSDirectory.open(Paths.get(taxonomyDirectory));
//            Analyzer analyzer = new StandardAnalyzer();
            PerFieldAnalyzerWrapper analyzer = AnalyzerHelper.getAnalyzer(false);
            IndexWriterConfig iwc = new IndexWriterConfig(analyzer);
            taxoWriter = new DirectoryTaxonomyWriter(FacetDir);

////            TaxonomyWriter taxonomyWriter = new DirectoryTaxonomyWriter(new MMapDirectory(new File(taxonomyDirectory)), OpenMode.CREATE);
//            TaxonomyWriter taxonomyWriter = new DirectoryTaxonomyWriter(FSDirectory.open(Paths.get(taxonomyDirectory)), OpenMode.CREATE);
//        CategoryDocumentBuilder categoryDocumentBuilder = new CategoryDocumentBuilder(taxonomyWriter, new DefaultFacetIndexingParams());
// 
//           
            iwc.setCodec(new Lucene50Codec(Mode.BEST_SPEED));
            if (create) {
                iwc.setOpenMode(IndexWriterConfig.OpenMode.CREATE);
            } else {
                // Add new documents to an existing index:
                iwc.setOpenMode(IndexWriterConfig.OpenMode.CREATE_OR_APPEND);
            }

            writer = new IndexWriter(dir, iwc);
//            List<DocMaster> sourceDocList = new ArrayList<DocMaster>();
//            sourceDocList.add(new DocMaster("courtId", "123", true, true));

        } catch (Exception e) {
            ////System.out.println(e.toString());
        }
    }

public void delete(String queryString){
        try {
            
//        reader = DirectoryReader.open(FSDirectory.open(Paths.get(indexPath)));
        
            PerFieldAnalyzerWrapper analyzer = AnalyzerHelper.getAnalyzer(false);
            QueryParser parser = new QueryParser(queryString, analyzer);
            Query query = parser.parse(queryString);
//            ////System.out.println(query);
            writer.deleteDocuments(query);
            analyzer.close();
//        parser.setDefaultOperator(QueryParser.Operator.AND);
        } catch (Exception ex) {
            Logger.getLogger(SearchUtility.class.getName()).log(Level.SEVERE, null, ex);
        }
}
    public void createAutoSuggest() {
        try {
            String autoDirectory = indexPath + File.separator+"auto";
            if (!new File(autoDirectory).exists()) {
                new File(autoDirectory).mkdirs();
            }
            Directory dir = FSDirectory.open(Paths.get(autoDirectory));
            PerFieldAnalyzerWrapper analyzer = AnalyzerHelper.getAnalyzer(false);
            IndexWriterConfig iwc = new IndexWriterConfig(analyzer);
            IndexReader reader = DirectoryReader.open(FSDirectory.open(Paths.get(indexPath)));
            Terms terms = MultiFields.getTerms(reader, "content");
            TermsEnum iterator = terms.iterator(TermsEnum.EMPTY);
            BytesRef byteRef = null;
            writer = new IndexWriter(dir, iwc);
            while ((byteRef = iterator.next()) != null) {
                if (iterator.totalTermFreq() > 2)
                {
                String term = new String(byteRef.bytes, byteRef.offset, byteRef.length);
                Document doc = new Document();
//                ////System.out.println(term + " : " + iterator.totalTermFreq() + " : " + iterator.docFreq());
                doc.add(new TextField("term", term, Store.YES));
                doc.add(new SortedDocValuesField("term_sort", new BytesRef(term)));
                doc.add(new IntField("term_freq", Integer.parseInt(iterator.totalTermFreq() + ""), Store.NO));
                doc.add(new NumericDocValuesField("term_freq_sort", iterator.totalTermFreq()));
                writer.addDocument(doc);
                }
            }
            writer.close();
            reader.close();
        } catch (Exception ex) {
            LogHelper.add(ex.toString() + "###############3\n");
            ////System.out.println(ex.toString());
            Logger.getLogger(IndexCreator.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void createSpellChecker() {
        try {
            String autoDirectory = indexPath + File.separator+"spell";
            if (!new File(autoDirectory).exists()) {
                new File(autoDirectory).mkdirs();
            }
            Directory dir = FSDirectory.open(Paths.get(autoDirectory));
            PerFieldAnalyzerWrapper analyzer = AnalyzerHelper.getAnalyzer(false);
            
            SpellChecker spellchecker = new SpellChecker(dir);
              IndexReader reader = DirectoryReader.open(FSDirectory.open(Paths.get(indexPath)));
              
              // To index a field of a user index:
              spellchecker.indexDictionary(new LuceneDictionary(reader, "title"), new IndexWriterConfig(analyzer), false);
              spellchecker.indexDictionary(new LuceneDictionary(reader, "content"), new IndexWriterConfig(analyzer), false);
              spellchecker.indexDictionary(new LuceneDictionary(reader, "summary"), new IndexWriterConfig(analyzer), false);
  
   String[] suggestions = spellchecker.suggestSimilar("marder", 5, reader, "content", SuggestMode.SUGGEST_MORE_POPULAR, new Float(98));
//  String[] suggestions = spellchecker.suggestSimilar("marder", 5);
//  for (String suggest : suggestions) {
//      ////System.out.println("For Content : ");
//      ////System.out.println(suggest);
//  }
  
  suggestions = spellchecker.suggestSimilar("marder", 5, reader, "title", SuggestMode.SUGGEST_ALWAYS, new Float(98));
//  for (String suggest : suggestions) {
//      ////System.out.println("For title : ");
//      ////System.out.println(suggest);
//  }
  

//            Terms terms = MultiFields.getTerms(reader, "judgement");
//            TermsEnum iterator = terms.iterator(TermsEnum.EMPTY);
//            BytesRef byteRef = null;
//            writer = new IndexWriter(dir, iwc);
//            while ((byteRef = iterator.next()) != null) {
//                ////System.out.println();
//                ////System.out.println();
//                String term = new String(byteRef.bytes, byteRef.offset, byteRef.length);
//                Document doc = new Document();
//                ////System.out.println(term + " : " + iterator.totalTermFreq() + " : " + iterator.docFreq());
//                doc.add(new TextField("term", term, Store.YES));
//                doc.add(new SortedDocValuesField("term_sort", new BytesRef(term)));
//                doc.add(new IntField("term_freq", Integer.parseInt(iterator.totalTermFreq() + ""), Store.NO));
//                doc.add(new NumericDocValuesField("term_freq_sort", iterator.totalTermFreq()));
//                writer.addDocument(doc);
//            }
//            writer.close();
//            reader.close();
        } catch (Exception ex) {
            LogHelper.add(ex.toString() + "###############4\n");
//            ////System.out.println(ex.toString());
            Logger.getLogger(IndexCreator.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void close() throws IOException {
        if (writer != null) {
            writer.close();
            taxoWriter.close();
        }
    }

    public void create(List<DocMaster> sourceDocList) throws IOException {
        try {
            Document doc = new Document();
            String caseId = "";
            for (DocMaster sourceDoc : sourceDocList) {
                if (sourceDoc.getKey().equals("caseId")) {
                    caseId = sourceDoc.getData();
                }
            }
//            ////System.out.println(caseId);

            facetConfig = new FacetsConfig();
            for (DocMaster sourceDoc : sourceDocList) {
                Store store = Field.Store.NO;
                if (sourceDoc.isIsStore()) {
                    store = Field.Store.YES;
                }
//                if (sourceDoc.getKey().equals("finalyear")) {
                if (sourceDoc.isIsFacet()) {

//                    facetConfig.setHierarchical(sourceDoc.getKey(), true);
//                            facetConfig.setMultiValued(sourceDoc.getKey(), true);
                    if (sourceDoc.getData() != null && !sourceDoc.getData().isEmpty()) {

                        if (sourceDoc.getData().contains(";")) {
                            String data = sourceDoc.getData().trim();                            
                            facetConfig.setMultiValued(sourceDoc.getKey(), true);
                            facetConfig.setHierarchical(sourceDoc.getKey(), false);
                            if (data.endsWith(";")) {
                                data = data.substring(0, data.length() - 1);
                            }
                            for (String s : data.split(";")) {
                                doc.add(new FacetField(sourceDoc.getKey(), s.trim()));
                            }
                        } else {
                            facetConfig.setMultiValued(sourceDoc.getKey(), false);
                            facetConfig.setHierarchical(sourceDoc.getKey(), true);
                             doc.add(new FacetField(sourceDoc.getKey(), sourceDoc.getData().trim()));
                        }
                    }
                }

                if (sourceDoc.getFieldType().equals(SortField.Type.INT.toString())) {
                    if (sourceDoc.isIsSort()) {
                        doc.add(new NumericDocValuesField(sourceDoc.getKey() + "_sort", Long.parseLong(sourceDoc.getData())));
                    }
                    doc.add(new IntField(sourceDoc.getKey(), Integer.parseInt(sourceDoc.getData()), store));
                } else if (sourceDoc.getFieldType().equals(SortField.Type.STRING.toString())) {
                    if (sourceDoc.isIsSort()) {
                        doc.add(new SortedDocValuesField(sourceDoc.getKey() + "_sort", new BytesRef(sourceDoc.getData())));
                    }
                    doc.add(new StringField(sourceDoc.getKey(), sourceDoc.getData(), store));
                } else {
                    if (sourceDoc.isIsSort()) {
                        doc.add(new SortedDocValuesField(sourceDoc.getKey() + "_sort", new BytesRef(sourceDoc.getData())));
                    }
                    if (sourceDoc.isIsEncrypt()) {
                        doc.add(new StoredField(sourceDoc.getKey(), CompressionTools.compressString(EncryptionHelper.encrypt(new StringBuilder(sourceDoc.getData()), caseId))));
//                        doc.add(new StoredField(sourceDoc.getKey(), EncryptionHelper.encrypt(new StringBuilder(sourceDoc.getData()), caseId)));
                    } else {
                        if (sourceDoc.isHl()) {
                            doc.add(new Field(sourceDoc.getKey(), sourceDoc.getData(), store, Field.Index.ANALYZED, Field.TermVector.WITH_POSITIONS_OFFSETS));
                        } else {
                            doc.add(new TextField(sourceDoc.getKey(), sourceDoc.getData(), store));
                        }
                    }
                }
            }
            ////System.out.println(doc.get("caseId"));
            writer.addDocument(facetConfig.build(taxoWriter, doc));
        } catch (Exception e) {
            LogHelper.add(e.toString() + "###############\n");
            ////System.out.println(e.toString());
        }
    }

}
