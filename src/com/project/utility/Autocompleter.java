/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.project.utility;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import org.apache.lucene.index.CorruptIndexException;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.Term;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.Sort;
import org.apache.lucene.search.SortField;
import org.apache.lucene.search.TermQuery;
import org.apache.lucene.search.TopDocs;

import org.apache.lucene.store.FSDirectory;
/**
 *
 * @author addie
 */
public class Autocompleter {
     private static final String GRAMMED_WORDS_FIELD = "words";

    private static final String SOURCE_WORD_FIELD = "sourceWord";

    private static final String COUNT_FIELD = "count";

    private static final String[] ENGLISH_STOP_WORDS = {
    "a", "an", "and", "are", "as", "at", "be", "but", "by",
    "for", "i", "if", "in", "into", "is",
    "no", "not", "of", "on", "or", "s", "such",
    "t", "that", "the", "their", "then", "there", "these",
    "they", "this", "to", "was", "will", "with"
    };

//    private final Directory autoCompleteDirectory;
    private final String autoCompleteDirectoryPath;

    private IndexReader autoCompleteReader;

    private IndexSearcher autoCompleteSearcher;

    public Autocompleter(String autoCompleteDir) throws IOException {
        this.autoCompleteDirectoryPath = autoCompleteDir;
//    	this.autoCompleteDirectory = FSDirectory.open(Paths.get(autoCompleteDir));

    	reOpenReader();
    }

    public List<String> suggestTermsFor(String term) throws IOException {
    	// get the top 5 terms for query
    	Query query = new TermQuery(new Term(GRAMMED_WORDS_FIELD, term));
//        new SortField(COUNT_FIELD, SortField.Type.INT);
    	Sort sort = new Sort(new SortField(COUNT_FIELD, SortField.Type.INT));

    	TopDocs docs = autoCompleteSearcher.search(query, null, 5, sort);
    	List<String> suggestions = new ArrayList<String>();
    	for (ScoreDoc doc : docs.scoreDocs) {
    		suggestions.add(autoCompleteReader.document(doc.doc).get(
    				SOURCE_WORD_FIELD));
    	}

    	return suggestions;
    }

//    @SuppressWarnings("unchecked")
//    public void reIndex(Directory sourceDirectory, String fieldToAutocomplete)
//    		throws CorruptIndexException, IOException {
//    	// build a dictionary (from the spell package)
//    	IndexReader sourceReader = IndexReader.open(sourceDirectory);
//
//    	LuceneDictionary dict = new LuceneDictionary(sourceReader,
//    			fieldToAutocomplete);
//
//    	// code from
//    	// org.apache.lucene.search.spell.SpellChecker.indexDictionary(
//    	// Dictionary)
//        
////    	IndexReader.unlock(autoCompleteDirectory);
//
//    	// use a custom analyzer so we can do EdgeNGramFiltering
//    	IndexWriter writer = new IndexWriter(autoCompleteDirectory,
//    	new Analyzer() {
//    		public TokenStream tokenStream(String fieldName,
//    				Reader reader) {
//    			TokenStream result = new StandardTokenizer(reader);
//
//    			result = new StandardFilter(result);
//    			result = new LowerCaseFilter(result);
//    			result = new ISOLatin1AccentFilter(result);
//    			result = new StopFilter(result,
//    				ENGLISH_STOP_WORDS);
//    			result = new EdgeNGramTokenFilter(
//    				result, Side.FRONT,1, 20);
//
//    			return result;
//    		}
//    	}, true);
//
//    	writer.setMergeFactor(300);
//    	writer.setMaxBufferedDocs(150);
//
//    	// go through every word, storing the original word (incl. n-grams) 
//    	// and the number of times it occurs
//    	Map<String, Integer> wordsMap = new HashMap<String, Integer>();
//
//    	Iterator<String> iter = (Iterator<String>) dict.getWordsIterator();
//    	while (iter.hasNext()) {
//    		String word = iter.next();
//
//    		int len = word.length();
//    		if (len < 3) {
//    			continue; // too short we bail but "too long" is fine...
//    		}
//
//    		if (wordsMap.containsKey(word)) {
//    			throw new IllegalStateException(
//    					"This should never happen in Lucene 2.3.2");
//    			// wordsMap.put(word, wordsMap.get(word) + 1);
//    		} else {
//    			// use the number of documents this word appears in
//    			wordsMap.put(word, sourceReader.docFreq(new Term(
//    					fieldToAutocomplete, word)));
//    		}
//    	}
//
//    	for (String word : wordsMap.keySet()) {
//    		// ok index the word
//    		Document doc = new Document();
//    		doc.add(new Field(SOURCE_WORD_FIELD, word, Field.Store.YES,
//    				Field.Index.UN_TOKENIZED)); // orig term
//    		doc.add(new Field(GRAMMED_WORDS_FIELD, word, Field.Store.YES,
//    				Field.Index.TOKENIZED)); // grammed
//    		doc.add(new Field(COUNT_FIELD,
//    				Integer.toString(wordsMap.get(word)), Field.Store.NO,
//    				Field.Index.UN_TOKENIZED)); // count
//
//    		writer.addDocument(doc);
//    	}
//
//    	sourceReader.close();
//
//    	// close writer
//    	writer.optimize();
//    	writer.close();
//
//    	// re-open our reader
//    	reOpenReader();
//    }

    private void reOpenReader() throws IOException {
    	if (autoCompleteReader == null) {
         
//    		autoCompleteReader = IndexReader.open(autoCompleteDirectory);
    		autoCompleteReader = DirectoryReader.open(FSDirectory.open(Paths.get(autoCompleteDirectoryPath)));
    	} else {
//    		autoCompleteReader.reopen();
    	}

    	autoCompleteSearcher = new IndexSearcher(autoCompleteReader);
    }

}
