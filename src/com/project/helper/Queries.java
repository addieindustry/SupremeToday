/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.project.helper;

import com.project.model.PrintSettingModel;
import com.project.utility.EncryptionHelperLatest;

import java.nio.file.Paths;
import java.util.List;

/**
 *
 * @author addie
 */
public class Queries {
     public static long SESSION=0;
     public static int PRINT_LIMIT=20;
     public static int PRINT_COUNT=0;
     public static int PDF_LIMIT=200;
     public static int PDF_COUNT=0;
     public static int COPY_LIMIT=5000;
     public static String FILE_SEPERATOR="/";

    //Apply FALSE instead of TRUE on below line to Enable Copy
    public static final Boolean IS_COPY_DISABLE = Boolean.TRUE;

    public static final Boolean IS_SUPREME_TODAY_APP = Boolean.TRUE;
    public static final String APPLICATION_NAME = "Supreme Today";

 //  public static final Boolean IS_SUPREME_TODAY_APP = Boolean.FALSE;
  // public static final String APPLICATION_NAME = "Indian CaseLaw Finder";

//    public static final String CURRENT_PATH = Paths.get("").toString();
    public static final String CURRENT_PATH = Paths.get("F:", "Program Files", "SupremeToday-2.0").toString();
 //  public static final String CURRENT_PATH = Paths.get("D:", "Projects", "SupremeToday", "SupremeToday").toString();
//    public static final String CURRENT_PATH = Paths.get("E:", "Old_Backup", "SupremeToday", "SupremeToday").toString();
    public static final String DATA_PATH=Paths.get(CURRENT_PATH, "Data").toString();
    public static final String RESOURCE_PATH=Paths.get(CURRENT_PATH, "res").toString();
    public static final String TEMP_FILE_PATH=Paths.get(CURRENT_PATH, "lib", "temp.html").toString();
    public static final String TEMP_DIRECTORY_PATH=Paths.get(CURRENT_PATH, "lib").toString();
    public static final String AUTO_UPDATE_EXE_FILE_SINGLEFILEPATH="SupremeTodayAutoUpdate.exe";
    public static final String AUTO_UPDATE_EXE_FILE=Paths.get(Paths.get("").toAbsolutePath().toString(), "SupremeTodayAutoUpdate.exe").toString();
    public static final String INDEX_PATH=Paths.get(DATA_PATH, "Index").toString();
    public static final String KRUTI_FONT_PATH = Paths.get(RESOURCE_PATH, "K012.TTF").toString();
    public static final String HELP_MANUAL_PATH = Paths.get(RESOURCE_PATH, "help.pdf").toString();
    public static final String GOOGLE_TRANSLATOR_HTML = Paths.get(RESOURCE_PATH, "temp.html").toString();
    public static final String GOOGLE_TRANSLATOR_SCRIPT = Paths.get(RESOURCE_PATH, "f.txt").toString();

    public static final String JQUERY_UI_CSS_PATH = Paths.get(RESOURCE_PATH, "jquery-ui.css").toString();
    public static final String JQUERY_UI_MIN_JS_PATH = Paths.get(RESOURCE_PATH, "jquery-ui.min.js").toString();
    public static final String JQUERY_MIN_JS_PATH = Paths.get(RESOURCE_PATH, "jquery.min.js").toString();

//    public static final List<String> SUPREME_DICTIONARY = EncryptionHelperLatest.ReadObjectToFile();

//    public static final String JQUERY_UI_PATH = Paths.get(CURRENT_PATH, "jquery.tooltip").toString();
//    public static final String JQUERY_TOOLTIP_JS_PATH = Paths.get(CURRENT_PATH, "jquery.tooltip.js").toString();
//    public static final String JQUERY_MIN_JS_PATH = Paths.get(CURRENT_PATH, "jquery-2.2.3.min.js").toString();

    public static String DB_PATH=Paths.get(DATA_PATH, "master_db_desktop.sqlite").toString();
    public static String LOCAL_DB_PATH=Paths.get(DATA_PATH, "local_db.sqlite").toString();
    public static String splashFile = Paths.get(RESOURCE_PATH, "splash_background_small.png").toString();
    public static String homebackgroundFile = Paths.get(RESOURCE_PATH, "home_background_large.png").toString();
    public static String ERROR_LOG_FILE_PATH = Paths.get(RESOURCE_PATH, "errorLog.txt").toString();
    public static final String LOGO_BASE64_FILE_PATH = Paths.get(RESOURCE_PATH, "logo.txt").toString();
    public static final String LOGO_PATH_BY_APPLICATION=Paths.get(RESOURCE_PATH, "logo.png").toString();
    public static final String LOGO_PATH=Paths.get(RESOURCE_PATH, "logo.png").toString();

    public static String REMOVE_SPECIAL_CHAR = "[^a-zA-Z0-9\"\\s\\,-]";

    //Message Box Dialog Title
    public static String MESSAGE_DELETE_BOOKMARK = "Are you sure to delete bookmark?";
    public static String MESSAGE_LIVE_UPDATE_STARTING = "it will take sometime to update application, Are you sure to update?";
    public static String MESSAGE_ABOUT_US_ADDRESS = APPLICATION_NAME + " - Contact us";
    public static String MESSAGE_CONFIRM_TO_EXIT = "Are you sure you want to exit?";
    public static String TITLE_HISTORY_WINDOW = "History";
    public static String TITLE_BOOKMARK_WINDOW = "Bookmark";
    public static String TITLE_ABOUT_US_WINDOW = "About Us";
    public static String TITLE_DICTIONARY = "Dictionary";
    public static String TITLE_CONFIRM_TO_EXIT_WINDOW = "Confirm Exit";
    public static String COPYRIGHT_CONTENT = "\n Copyright(c) 2016";

    //Field Name Variable for Queries
    public static final String ASC_CAPITAL = "ASC";
    public static final String DESC_CAPITAL = "DESC";
    public static final String STRING = " STRING ";
    public static final String SORT_COURT_TITLE = "courttitle_sort";
    public static final String SORT_JDATE = "decisiondate_sort";
    public static final String SORT_BENCH = "judgebench_sort";

    public static final String OR = " OR ";
    public static final String AND = " AND ";
    public static final String TO = " TO ";
    public static final String STAR = "*";
    public static final String OPEN_BRACKET = "(";
    public static final String CLOSE_BRACKET = ")";
    public static final String COLON = ":";
    public static final String SEPERATOR = " | ";

//    public static final String OVERRULED_BACKGROUND_COLOR = "#ef999c";
    public static final String OVERRULED_BACKGROUND_COLOR = "red";
    public static final String DISTINGUISHED_BACKGROUND_COLOR = "green";
    public static boolean HIDE_AUTOSUGGET = false;


    public static final String LUCENEFIELD_SUMMARY = "summary";
    public static final String LUCENEFIELD_SUMMARY_STORE = "summary_store";
    public static final String LUCENEFIELD_SUMMARY_NEW = "summary_new";
    public static final String LUCENEFIELD_SUMMARY_NEW_STORE = "summary_new_store";
    public static final String LUCENEFIELD_CONTENT = "content";
    public static final String LUCENEFIELD_CONTENT_STORE = "content_store";
    public static final String LUCENEFIELD_CITATION = "citation";
    public static final String LUCENEFIELD_CITATION_STORE = "citation_s";
    public static final String LUCENEFIELD_JUDGE = "judge";
    public static final String LUCENEFIELD_ADVOCATES = "advocates";
    public static final String LUCENEFIELD_JUDGEMENTHEADER = "judgementHeader";
    public static final String LUCENEFIELD_ACTS = "acts";
    public static final String LUCENEFIELD_TITLE = "title";
    public static final String LUCENEFIELD_DECISIONDATE = "decisionDate";


    public static final int RESULT_START_FROM = 1;
    public static final int RESULT_TOTAL = 20;



    public static final String LUCENE_NUMFOUND = "numFound";

    public static final String CONFIG_FILE_PATH = Paths.get(CURRENT_PATH, "lib", "config.properties").toString();
    public static char[] EMAIL_USER_NAME_IN_CHAR;
    public static char[] EMAIL_PASSWORD_IN_CHAR;
    public static String DONGLE_API_URL = "http://supreme-today.com:8080/api/set_dongle_license";

    public static String EMAIL_SERVER_API = "http://www.supreme-today.com:8080/doc/sendemail/";
    public static String LICENSE_EXPIRE_DATE_API = "http://www.supreme-today.com:8080/api/subscribernextduedate/";
    public static String IS_TRUE_PRINT_AVAIL_API = "http://www.supreme-today.com:8080/api/trueprintcheck/";
    public static String TRUE_PRINT_URL = "http://www.supreme-today.com:8080/api/trueprintchecklog/";

//    //Server Authentication Information
//    public static final String IP = "supreme-today.com";
//    public static final String USER = "supremetoday";
//    public static final String PASSWORD = "1234567";
    public static final String APPLICATION_VERSION = "19.1";

    public static String ACT_RUEL_REG = "";
    public static String GLOBAL_OR_ADVANCE = "";

    public static final String[] GLOBAL_SEARCH_FIELDS=new String[]{"title", "content", "summary"};
    public static String AUTO_COMPLETE_TEXTFIELD="fulltext";


//    public static String DATA_PATH="F:\\ST\\Projects\\JavaFX\\Data";
//     public static String INDEX_PATH="F:\\ST\\Projects\\JavaFX\\Data\\Index";
//    public static String DB_PATH="F:\\ST\\Projects\\JavaFX\\Data\\master_db_desktop.sqlite";
//    public static String LOCAL_DB_PATH="F:\\ST\\Projects\\JavaFX\\Data\\local_db.sqlite";
//    public static final String[] GLOBAL_SEARCH_FIELDS=new String[]{"title", "content", "summary"};
//    public static String AUTO_COMPLETE_TEXTFIELD="fulltext";
//    public static String LOGO_PATH=getClass().getResource("/com/project/resources/logo.png").getPath();


//    public static PrintSettingModel PRINT_SETTING_MODEL = new PrintSettingModel("", "A4", "16.0", "12.0", 36.0F, 36.0F, 36.0F, 36.0F, true, false);
    public static PrintSettingModel PRINT_SETTING_MODEL = new PrintSettingModel("", "Letter", "18", "14", "16", 30.0F, 30.0F, 43.0F, 45.0F, true, false, 0.16F, 0.2F, 0.2F);


//   INDEX DATABASE QUERIES //
    public static final String GET_ALL_COURTS="SELECT * FROM court_master ORDER BY courtName='Supreme Court' DESC, court_master.courtName ASC";
    public static final String GET_CASES_REFERED_BY_CASEID="SELECT targetCaseId,caseReferred FROM final_casereferred where caseId = '%s'";
    public static final String GET_JUDICIAL_BY_CASEID="SELECT targetCaseId,caseReferred,referredBy FROM final_casereferred where caseId = '%s' AND referredBy IS NOT NULL ORDER BY referredBy";
    public static final String IS_CITATOR_AVAILABLE_BY_CASEID="SELECT COUNT(1) FROM final_casereferred WHERE targetCaseId = '%s'";
    public static final String IS_JUDICIAL_CITATOR_AVAILABLE_BY_CASEID="SELECT COUNT(1) FROM final_casereferred WHERE targetCaseId = '%s' AND referredBy IS NOT NULL";
    public static final String GET_CITATOR_BY_CASEID="SELECT caseCitator, caseId FROM final_casereferred WHERE targetCaseId = '%s'";
    public static final String GET_JUDICIAL_CITATOR_BY_CASEID="SELECT caseCitator, caseId, referredby FROM final_casereferred WHERE targetCaseId = '%s' ORDER BY referredBy='Referred' DESC, referredBy";
//    public static final String GET_JUDICIAL_CITATOR_BY_CASEID="SELECT caseCitator, caseId, referredby FROM final_casereferred WHERE targetCaseId = '%s' AND referredBy IS NOT NULL AND referredBy != '' ORDER BY referredBy";
//    public static final String GET_JUDICIAL_CITATOR_BY_CASEID="SELECT caseCitator, caseId, referredby FROM final_casereferred WHERE targetCaseId = '%s' AND referredBy IS NOT NULL ORDER BY referredBy";
    public static final String GET_CENTRAL_ACT_LIST="SELECT ID,Acts1 FROM centralActs ORDER BY Acts1";
    public static final String GET_ALL_JHARKHAND_POLIC_MANUAL="SELECT * FROM jharkhand_polic_manual";
    public static final String GET_COMMENTARY_LIST="SELECT DISTINCT cmtId,cmtType FROM commentary_master ORDER BY cmtType";
    public static final String GET_COMMENTARY_LIST_WITH_EXHAUSTIVE="SELECT DISTINCT cmtId,cmtType FROM commentary_master WHERE cmtType LIKE '%s' ORDER BY cmtType";
    public static final String GET_COMMENTARY_LIST_WITHOUT_EXHAUSTIVE="SELECT DISTINCT cmtId,cmtType FROM commentary_master WHERE cmtType NOT LIKE '%s' ORDER BY cmtType";
//    public static final String GET_ACT_REF_TITLE="SELECT actTitle FROM actTitle_master WHERE actTitle LIKE '%s'";
    public static final String GET_ACT_REF_TITLE="SELECT * ,(case WHEN actTitle LIKE '%s' THEN 1 ELSE 0 END) AS [priority] FROM actTitle_master WHERE actTitle LIKE '%s' ORDER BY [priority] DESC";

    public static final String GET_SHORT_DATA="SELECT full_title, short_title FROM short_data";
//    public static final String GET_SHORT_DATA="SELECT * FROM short_data";
    public static final String GET_ACT_REF_SECTION_BY_TITLE="SELECT actType COLLATE NOCASE FROM act_master WHERE actTitle='%s' AND actType LIKE '%s' COLLATE NOCASE ORDER BY actType";
    public static final String GET_ALL_JUDGES="SELECT judgeName COLLATE NOCASE FROM judge_master WHERE judgeName LIKE '%s'";

//    public static final String GET_ALL_JUDGES="SELECT judgeName COLLATE NOCASE FROM judge_master WHERE judgeName='%s'";

//    public static final String GET_ALL_PUBLISHERS_FROM_CITATION="SELECT DISTINCT publisher COLLATE NOCASE FROM citation_master";
    public static final String GET_ALL_PUBLISHERS_FROM_CITATION="SELECT publisher FROM publisher_master";
//    public static final String GET_YEAR_FROM_CITATION_BY_PUBLISHERS="SELECT DISTINCT year COLLATE NOCASE FROM citation_master WHERE publisher = '%s' COLLATE NOCASE";
    public static final String GET_YEAR_FROM_CITATION_BY_PUBLISHERS="SELECT DISTINCT year FROM citation_master WHERE publisher = '%s' ORDER BY year DESC";
//    public static final String GET_VOLUME_FROM_CITATION_BY_PUBLISHERS_YEAR="SELECT DISTINCT volume COLLATE NOCASE FROM citation_master WHERE publisher = '%s' COLLATE NOCASE AND year = '%s'";
    public static final String GET_VOLUME_FROM_CITATION_BY_PUBLISHERS_YEAR="SELECT DISTINCT volume FROM citation_master WHERE publisher = '%s' AND year = '%s'";
//    public static final String GET_PAGE_FROM_CITATION_BY_PUBLISHERS_YEAR_VOLUME="SELECT DISTINCT pageno COLLATE NOCASE FROM citation_master WHERE publisher = '%s' COLLATE NOCASE AND year = '%s' AND volume = '%s'";
    public static final String GET_PAGE_FROM_CITATION_BY_PUBLISHERS_YEAR_VOLUME="SELECT DISTINCT pageno FROM citation_master WHERE publisher = '%s' AND year = '%s' AND volume = '%s' ORDER BY CAST(pageno AS INTEGER)";
    public static final String GET_ALL_OVERRULED="SELECT courtId,white,red,whiteText,redText,'Overruled By' AS overruled FROM overruled";
    public static final String GET_OVERRULED_BY_CASEID="SELECT courtId,white FROM overruled WHERE red IN (%s)";
    public static final String GET_IS_OVERRULED="SELECT COUNT(1) FROM overruled WHERE red IN (%s)";
//    public static final String GET_IS_DISTINGUISHED="SELECT COUNT(1) FROM final_casereferred WHERE caseId = '%s' AND referredby = 'DISTINGUISHED' COLLATE NOCASE";
    public static final String GET_IS_DISTINGUISHED="SELECT COUNT(1) FROM final_casereferred WHERE targetCaseId = '%s' AND referredby = 'DISTINGUISHED' COLLATE NOCASE";
    public static final String GET_VERSION_MASTER="SELECT * FROM version_master";
    public static final String GET_COURTS_BY_IDS="SELECT * FROM court_master WHERE courtId IN (%s)";
    public static final String UPDATE_VERSION="UPDATE version_master SET versionId = %s";
//    public static final String GET_ACT_LIST="SELECT DISTINCT actTitle FROM act_master";

//    LOCAL DATABASE QUERIES //
    public static final String INSERT_USER_DETAILS="INSERT INTO user_details (agent_code,user_name,organization_name,client_number,client_email,city,lic_key,client_package,sub_id) VALUES ('%s','%s','%s','%s','%s','%s','%s','%s','%s')";
    public static final String GET_USER_DETAILS="SELECT * FROM user_details";
    public static final String GET_BOOKMARK_BY_CASEID="SELECT _id,doc_id,title,doc_type,category,note FROM user_bookmarks WHERE doc_id = '%s'";
    public static final String GET_ALL_BOOKMARK="SELECT _id,doc_id,title,doc_type,category,note FROM user_bookmarks";
    public static final String GET_BOOKMARK_CATEGORIES="SELECT DISTINCT category FROM user_bookmarks";
    public static final String INSERT_BOOKMARK="INSERT INTO user_bookmarks (doc_id,title,doc_type,category,note) VALUES ('%s','%s','%s','%s','%s')";
    public static final String UPDATE_BOOKMARK="UPDATE user_bookmarks SET doc_id='%s' ,title='%s',doc_type='%s',category='%s',note='%s' where _id = %s";
    public static final String DELETE_BOOKMARK="DELETE from user_bookmarks where _id IN (%s)";
    public static final String DELETE_USER="DELETE from user_details";

    public static final String GET_PRINT_SETTING="SELECT _id,page_type,print_font_size,display_font_size,result_font_size,margin_top, margin_bottom, margin_right, margin_left, print_logo,use_native_browser,filter_splitter,global_result_splitter,advance_result_splitter FROM print_setting LIMIT 1";
//    public static final String INSERT_PRINT_SETTING="INSERT INTO print_setting(page_type,print_font_size,display_font_size,margin_top,margin_bottom,margin_right,margin_left,print_logo,use_native_browser) VALUES ('%s','%s','%s','%s','%s','%s','%s','%s','%s')";
    public static final String INSERT_PRINT_SETTING="INSERT INTO print_setting(page_type,print_font_size,display_font_size,margin_top,margin_bottom,margin_right,margin_left,print_logo,use_native_browser,result_font_size,filter_splitter,global_result_splitter,advance_result_splitter) VALUES ('%s','%s','%s','%s','%s','%s','%s','%s','%s','%s','%s','%s','%s')";
    public static final String DELETE_PRINT_SETTING="DELETE FROM print_setting";
    public static final String UPDATE_DISPLAY_FONT_IN_PRINT_SETTING="UPDATE print_setting SET display_font_size=%s";
    public static final String UPDATE_RESULT_FONT_IN_PRINT_SETTING="UPDATE print_setting SET result_font_size=%s";

    public static final String UPDATE_FILTER_SPLITTER_IN_PRINT_SETTING="UPDATE print_setting SET filter_splitter=%s";
    public static final String UPDATE_GLOBAL_RESULT_SPLITTER_IN_PRINT_SETTING="UPDATE print_setting SET global_result_splitter=%s";
    public static final String UPDATE_ADVANCE_RESULT_SPLITTER_IN_PRINT_SETTING="UPDATE print_setting SET advance_result_splitter=%s";

    public static final String ALTER_PRINT_SETTING="ALTER TABLE print_setting ADD COLUMN margin_top FLOAT;ALTER TABLE print_setting ADD COLUMN margin_bottom FLOAT;ALTER TABLE print_setting ADD COLUMN margin_right FLOAT;ALTER TABLE print_setting ADD COLUMN margin_left FLOAT;";
    public static final String CREATE_PRINT_SETTING="CREATE TABLE print_setting (_id INTEGER PRIMARY KEY AUTOINCREMENT,page_type VARCHAR, print_font_size INTEGER, display_font_size INTEGER, result_font_size INTEGER, print_logo BOOLEAN, use_native_browser BOOLEAN, margin_top FLOAT, margin_bottom FLOAT, margin_right FLOAT, margin_left FLOAT, filter_splitter FLOAT, global_result_splitter FLOAT, advance_result_splitter FLOAT)";

    public static final String GET_HISTORY_LIST="SELECT _id,title,query,keyword,search_type,created_date FROM user_history ORDER BY created_date DESC";
    public static final String GET_HISTORY_KEYWORD_LIST="SELECT DISTINCT keyword FROM user_history WHERE keyword IS NOT NULL or keyword <> '' ORDER BY created_date DESC";
//    public static final String GET_HISTORY_KEYWORD_LIST="SELECT _id,title,query,keyword,search_type,created_date FROM user_history WHERE keyword IS NOT NULL or keyword <> '' ORDER BY created_date DESC";
    public static final String INSERT_HISTORY="INSERT INTO user_history (title,query,keyword,search_type) VALUES ('%s','%s','%s','%s')";
    public static final String INSERT_CITATION="INSERT INTO citation_master (publisher,year,volume,pageNo) VALUES ('%s','%s','%s','%s')";
//    public static final String DELETE_ACT_TITLE="DELETE FROM centralActs WHERE ID='%s' AND Acts1='%s'";
//    public static final String INSERT_ACT_TITLE="INSERT INTO centralActs (ID,Acts1) VALUES ('%s','%s')";
    public static final String INSERT_ACT_TITLE="INSERT INTO centralActs (ID,Acts1) SELECT '%s', '%s' WHERE NOT EXISTS(SELECT 1 FROM centralActs WHERE id = '%s' AND Acts1 = '%s')";
    public static final String INSERT_CASE_REFFERED="INSERT INTO final_casereferred(courtId,caseId,targetCaseId,targetCourtId,caseReferred,caseCitator) VALUES (%s,'%s','%s',%s,'%s','%s')";
    public static final String GET_COURTID_FROM_CASE_REFFERED_BY_BOTH_CASEID="SELECT courtId FROM final_casereferred WHERE caseId = '%s'";
//    public static final String INSERT_HISTORY="INSERT INTO user_history (title,query,keyword,search_type) VALUES ('%s','%s','%s','%s')";


    public static final String PROPERTIES = "{\n" +
"    \"schema\": [\n" +
"        {\n" +
"            \"tableName\": \"cases_all\",\n" +
"            \"cols\": [\n" +
"                {\"title\": \"courtId\", \"isIndexed\": true, \"isStore\": true,\"isFacet\":false,\"fieldType\":\"DOC\",\"isSort\":false,\"isEncrypt\":false,\"hl\":false},\n" +
"                {\"title\": \"caseId\", \"isIndexed\": true, \"isStore\": true,\"isFacet\":false,\"fieldType\":\"DOC\",\"isSort\":false,\"isEncrypt\":false,\"hl\":false},\n" +
"                {\"title\": \"courttitle\", \"isIndexed\": true, \"isStore\": true,\"isFacet\":true,\"fieldType\":\"DOC\",\"isSort\":false,\"isEncrypt\":false,\"hl\":false},\n" +
"                {\"title\": \"title\", \"isIndexed\": true, \"isStore\": true,\"isFacet\":false,\"fieldType\":\"DOC\",\"isSort\":false,\"isEncrypt\":false,\"hl\":false},\n" +
"                {\"title\": \"stitle\", \"isIndexed\": true, \"isStore\": false,\"isFacet\":false,\"fieldType\":\"STRING\",\"isSort\":true,\"isEncrypt\":false,\"hl\":false},\n" +
"                {\"title\": \"decisionDate\", \"isIndexed\": true, \"isStore\": true,\"isFacet\":false,\"fieldType\":\"STRING\",\"isSort\":true,\"isEncrypt\":false,\"hl\":false},\n" +
"                {\"title\": \"finalyear\", \"isIndexed\": true, \"isStore\": true,\"isFacet\":true,\"fieldType\":\"DOC\",\"isSort\":false,\"isEncrypt\":false,\"hl\":false},\n" +
"                {\"title\": \"acts\", \"isIndexed\": true, \"isStore\": false,\"isFacet\":true,\"fieldType\":\"DOC\",\"isSort\":false,\"isEncrypt\":false,\"hl\":false},\n" +
"                {\"title\": \"acts_store\", \"isIndexed\": false, \"isStore\": true,\"isFacet\":false,\"fieldType\":\"DOC\",\"isSort\":false,\"isEncrypt\":true,\"hl\":false},\n" +
"                {\"title\": \"judge\", \"isIndexed\": true, \"isStore\": true,\"isFacet\":true,\"fieldType\":\"DOC\",\"isSort\":false,\"isEncrypt\":false,\"hl\":false},\n" +
"                {\"title\": \"citation\", \"isIndexed\": true, \"isStore\": false,\"isFacet\":false,\"fieldType\":\"DOC\",\"isSort\":false,\"isEncrypt\":false,\"hl\":true},\n" +
"                {\"title\": \"citation_store\", \"isIndexed\": false, \"isStore\": true,\"isFacet\":false,\"fieldType\":\"DOC\",\"isSort\":false,\"isEncrypt\":true,\"hl\":false},\n" +
"                {\"title\": \"summary\", \"isIndexed\": true, \"isStore\": false,\"isFacet\":false,\"fieldType\":\"DOC\",\"isSort\":false,\"isEncrypt\":false,\"hl\":true},\n" +
"                {\"title\": \"summary_store\", \"isIndexed\": false, \"isStore\": true,\"isFacet\":false,\"fieldType\":\"DOC\",\"isSort\":false,\"isEncrypt\":true,\"hl\":false},\n" +
"                {\"title\": \"content\", \"isIndexed\": true, \"isStore\": false,\"isFacet\":false,\"fieldType\":\"DOC\",\"isSort\":false,\"isEncrypt\":false,\"hl\":true},\n" +
"                {\"title\": \"content_store\", \"isIndexed\": false, \"isStore\": true,\"isFacet\":false,\"fieldType\":\"DOC\",\"isSort\":false,\"isEncrypt\":true,\"hl\":false},\n" +
"                {\"title\": \"impNotes_store\", \"isIndexed\": false, \"isStore\": true,\"isFacet\":false,\"fieldType\":\"DOC\",\"isSort\":false,\"isEncrypt\":true,\"hl\":false},\n" +
"                {\"title\": \"judgementHeader\", \"isIndexed\": true, \"isStore\": false,\"isFacet\":false,\"fieldType\":\"DOC\",\"isSort\":false,\"isEncrypt\":false,\"hl\":false},\n" +
"                {\"title\": \"judgementHeader_store\", \"isIndexed\": false, \"isStore\": true,\"isFacet\":false,\"fieldType\":\"DOC\",\"isSort\":false,\"isEncrypt\":true,\"hl\":false},\n" +
"                {\"title\": \"advocates\", \"isIndexed\": false, \"isStore\": true,\"isFacet\":false,\"fieldType\":\"DOC\",\"isSort\":false,\"isEncrypt\":false,\"hl\":false},\n" +
"                {\"title\": \"cited\", \"isIndexed\": true, \"isStore\": true,\"isFacet\":false,\"fieldType\":\"INT\",\"isSort\":true,\"isEncrypt\":false,\"hl\":false},\n" +
"                {\"title\": \"bench\", \"isIndexed\": true, \"isStore\": true,\"isFacet\":true,\"fieldType\":\"DOC\",\"isSort\":false,\"isEncrypt\":false,\"hl\":false},\n" +
"                {\"title\": \"benchcounter\", \"isIndexed\": true, \"isStore\": false,\"isFacet\":false,\"fieldType\":\"INT\",\"isSort\":true,\"isEncrypt\":false,\"hl\":false},\n" +
"                {\"title\": \"whitelist\", \"isIndexed\": true, \"isStore\": true,\"isFacet\":false,\"fieldType\":\"INT\",\"isSort\":true,\"isEncrypt\":false,\"hl\":false},\n" +
"                {\"title\": \"result\", \"isIndexed\": true, \"isStore\": true,\"isFacet\":true,\"fieldType\":\"DOC\",\"isSort\":false,\"isEncrypt\":false,\"hl\":false},\n" +
"                {\"title\": \"DocType\", \"isIndexed\": true, \"isStore\": true,\"isFacet\":true,\"fieldType\":\"DOC\",\"isSort\":false,\"isEncrypt\":false,\"hl\":false},\n" +
"                {\"title\": \"summary_new\", \"isIndexed\": true, \"isStore\": false, \"isFacet\":false, \"fieldType\":\"DOC\", \"isSort\":false,\"isEncrypt\":false,\"hl\":true},\n" +
"                {\"title\": \"summary_new_store\", \"isIndexed\": false, \"isStore\": true,\"isFacet\":false, \"fieldType\":\"DOC\", \"isSort\":false,\"isEncrypt\":true,\"hl\":false}\n" +
"            ],\n" +
"            \"where\": \"courtId IN (%s)\"\n" +
"        }\n" +
"        ,\n" +
"         {\n" +
"            \"tableName\": \"BareActs\",\n" +
"            \"cols\": [{\"title\": \"id\", \"isIndexed\": true, \"isStore\": true,\"isFacet\":false,\"fieldType\":\"STRING\",\"isSort\":false,\"isEncrypt\":false,\"hl\":false},\n" +
"                {\"title\": \"stitle\", \"isIndexed\": true, \"isStore\": false,\"isFacet\":false,\"fieldType\":\"STRING\",\"isSort\":true,\"isEncrypt\":false,\"hl\":false},\n" +
"                {\"title\": \"Acts\", \"isIndexed\": true, \"isStore\": true,\"isFacet\":false,\"fieldType\":\"DOC\",\"isSort\":false,\"isEncrypt\":false,\"hl\":false},\n" +
"                {\"title\": \"title\", \"isIndexed\": true, \"isStore\": true,\"isFacet\":false,\"fieldType\":\"DOC\",\"isSort\":false,\"isEncrypt\":false,\"hl\":false},\n" +
"                {\"title\": \"Type\", \"isIndexed\": true, \"isStore\": true,\"isFacet\":false,\"fieldType\":\"STRING\",\"isSort\":true,\"isEncrypt\":false,\"hl\":false},\n" +
"                {\"title\": \"summary\", \"isIndexed\": true, \"isStore\": true,\"isFacet\":false,\"fieldType\":\"DOC\",\"isSort\":false,\"isEncrypt\":false,\"hl\":true},\n" +
"                {\"title\": \"content\", \"isIndexed\": true, \"isStore\": false,\"isFacet\":false,\"fieldType\":\"DOC\",\"isSort\":false,\"isEncrypt\":false,\"hl\":true},\n" +
"                {\"title\": \"content_store\", \"isIndexed\": false, \"isStore\": true,\"isFacet\":false,\"fieldType\":\"DOC\",\"isSort\":false,\"isEncrypt\":true,\"hl\":false},\n" +
"                {\"title\": \"caseId\", \"isIndexed\": true, \"isStore\": true,\"isFacet\":false,\"fieldType\":\"DOC\",\"isSort\":false,\"isEncrypt\":false,\"hl\":false},\n" +
"                {\"title\": \"DocType\", \"isIndexed\": true, \"isStore\": true,\"isFacet\":true,\"fieldType\":\"STRING\",\"isSort\":false,\"isEncrypt\":false,\"hl\":false}],\n" +
"            \"where\": \"\"\n" +
"        },\n" +
"        {\n" +
"            \"tableName\": \"WordPhrase\",\n" +
"            \"cols\": [{\"title\": \"caseId\", \"isIndexed\": true, \"isStore\": true,\"isFacet\":false,\"fieldType\":\"DOC\",\"isSort\":false,\"isEncrypt\":false,\"hl\":false},\n" +
"                {\"title\": \"title\", \"isIndexed\": true, \"isStore\": true,\"isFacet\":false,\"fieldType\":\"DOC\",\"isSort\":false,\"isEncrypt\":false,\"hl\":false},\n" +
"                {\"title\": \"stitle\", \"isIndexed\": true, \"isStore\": false,\"isFacet\":false,\"fieldType\":\"STRING\",\"isSort\":true,\"isEncrypt\":false,\"hl\":false},\n" +
"                {\"title\": \"content\", \"isIndexed\": true, \"isStore\": false,\"isFacet\":false,\"fieldType\":\"DOC\",\"isSort\":false,\"isEncrypt\":false,\"hl\":true},\n" +
"                {\"title\": \"content_store\", \"isIndexed\": false, \"isStore\": true,\"isFacet\":false,\"fieldType\":\"DOC\",\"isSort\":false,\"isEncrypt\":true,\"hl\":false},\n" +
"                {\"title\": \"DocType\", \"isIndexed\": true, \"isStore\": true,\"isFacet\":true,\"fieldType\":\"STRING\",\"isSort\":false,\"isEncrypt\":false,\"hl\":false}],\n" +
"            \"where\": \"\"\n" +
"        },\n" +
"        {\n" +
"            \"tableName\": \"Dictionary\",\n" +
"            \"cols\": [{\"title\": \"caseId\", \"isIndexed\": true, \"isStore\": true,\"isFacet\":false,\"fieldType\":\"DOC\",\"isSort\":false,\"isEncrypt\":false,\"hl\":false},\n" +
"                {\"title\": \"title\", \"isIndexed\": true, \"isStore\": true,\"isFacet\":false,\"fieldType\":\"DOC\",\"isSort\":true,\"isEncrypt\":false,\"hl\":false},\n" +
"                {\"title\": \"Hindi\", \"isIndexed\": false, \"isStore\": true,\"isFacet\":false,\"fieldType\":\"DOC\",\"isSort\":false,\"isEncrypt\":false,\"hl\":false},\n" +
"                {\"title\": \"DocType\", \"isIndexed\": true, \"isStore\": true,\"isFacet\":false,\"fieldType\":\"STRING\",\"isSort\":false,\"isEncrypt\":false,\"hl\":false}],\n" +
"            \"where\": \"\"\n" +
"        },\n" +
"        {\n" +
"            \"tableName\": \"Commentary\",\n" +
"            \"cols\": [{\"title\": \"id\", \"isIndexed\": true, \"isStore\": true,\"isFacet\":false,\"fieldType\":\"DOC\",\"isSort\":false,\"isEncrypt\":false,\"hl\":false},\n" +
"                {\"title\": \"stitle\", \"isIndexed\": true, \"isStore\": false,\"isFacet\":true,\"fieldType\":\"STRING\",\"isSort\":true,\"isEncrypt\":false,\"hl\":false},\n" +
"                {\"title\": \"title\", \"isIndexed\": true, \"isStore\": true,\"isFacet\":false,\"fieldType\":\"DOC\",\"isSort\":false,\"isEncrypt\":false,\"hl\":false},\n" +
"                {\"title\": \"Type\", \"isIndexed\": true, \"isStore\": true,\"isFacet\":false,\"fieldType\":\"STRING\",\"isSort\":true,\"isEncrypt\":false,\"hl\":false},\n" +
"                {\"title\": \"summary\", \"isIndexed\": true, \"isStore\": true,\"isFacet\":false,\"fieldType\":\"DOC\",\"isSort\":false,\"isEncrypt\":false,\"hl\":true},\n" +
"                {\"title\": \"content\", \"isIndexed\": true, \"isStore\": false,\"isFacet\":false,\"fieldType\":\"DOC\",\"isSort\":false,\"isEncrypt\":false,\"hl\":false},\n" +
"                {\"title\": \"content_store\", \"isIndexed\": false, \"isStore\": true,\"isFacet\":false,\"fieldType\":\"DOC\",\"isSort\":false,\"isEncrypt\":true,\"hl\":false},\n" +
"                {\"title\": \"caseId\", \"isIndexed\": true, \"isStore\": true,\"isFacet\":false,\"fieldType\":\"DOC\",\"isSort\":false,\"isEncrypt\":false,\"hl\":false},\n" +
"                {\"title\": \"DocType\", \"isIndexed\": true, \"isStore\": true,\"isFacet\":true,\"fieldType\":\"STRING\",\"isSort\":false,\"isEncrypt\":false,\"hl\":false}],\n" +
"            \"where\": \"\"\n" +
"        }\n" +
"    ]\n" +
"}";

}
