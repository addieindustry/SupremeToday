///*
// * To change this license header, choose License Headers in Project Properties.
// * To change this template file, choose Tools | Templates
// * and open the template in the editor.
// */
//package com.project;
//
//import com.google.gson.Gson;
//import com.google.gson.JsonObject;
//import com.google.gson.JsonParser;
//import com.project.helper.CommanHelper;
//import com.project.helper.ResponseMaster;
//import com.project.helper.ServiceHelper;
//import java.io.BufferedReader;
//import java.io.IOException;
//import java.io.PrintWriter;
//import java.util.logging.Level;
//import java.util.logging.Logger;
//import javax.servlet.ServletException;
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
//
//import org.eclipse.jetty.server.Request;
//import org.eclipse.jetty.server.handler.AbstractHandler;
//
///**
// *
// * @author addie
// */
//public class WebHandler extends AbstractHandler {
//
//    public WebHandler() {
//    }
//
//    public void handle(String target, Request baseRequest, HttpServletRequest request,
//            HttpServletResponse response)
//            throws IOException, ServletException {
//
//        response.setContentType("text/html;charset=utf-8");
//        response.setStatus(HttpServletResponse.SC_OK);
//        baseRequest.setHandled(true);
//        PrintWriter out = response.getWriter();
//
////            if (target.equals("/api")) {
////
////            }
//        String[] contextArr = request.getPathInfo().split("/");
////                out.println(contextArr.length+"");
////                out.print("<br/>");
////        if (contextArr.length == 3 && contextArr[1].equals("api")) {
//////                String method_name = request.getPathInfo();
//////                if (method_name.contains("/")) {
//////                    method_name = method_name.replace("/", "");
//////                }
////            if (request.getMethod().equals("GET")) {
////                doGet(contextArr[2], request, response);
////            } else if (request.getMethod().equals("POST")) {
////                doPost(contextArr[2], request, response);
////
////            }
////        } else {
////            out.println(target);
////            out.print("<br/>");
////            out.println(request.getPathInfo());
////            out.print("<br/>");
////            out.println(request.getMethod());
////            out.print("<br/>");
////            out.println(baseRequest.getRealPath(""));
////            out.print("<br/>");
////            out.println(request.getParameter("test"));
//
//        String content="<!DOCTYPE HTML><html><head><meta http-equiv=\"X-UA-Compatible\" content=\"IE=edge\"><meta http-equiv='Content-Type' content='text/html;charset=UTF-8'/><script>var viewThis=null;var globalSearchString=\"\";var globalSearchWithin=\"\";var advanceSearchSection=null;</script><script src=\"resources/sap-ui-core.js\" id=\"sap-ui-bootstrap\"data-sap-ui-libs=\"sap.m,sap.ui.commons,sap.ui.table,sap.ui.ux3\"data-sap-ui-theme=\"sap_bluecrystal\"data-sap-ui-resourceroots='{\"com.supremetoday\":\"./\",\"supremetoday\":\"./supremetoday\"}'>//\"com.supremetest\":\"./\",//\"supremetest\":\"./supremetest\",</script><script type=\"text/javascript\" src=\"js/mousetrap.js\"></script><script type=\"text/javascript\" src=\"js/hilitor.js\"></script><style>.hl_class{background: yellow;}.sapUiUx3ShellHeadStandard>.sapUiUx3ShellWorksetBar{margin: -10px 40px 0;}.sapUiUx3ShellHeader{height: 0px;}.sapUiUx3ShellHeadStandard>.sapUiUx3ShellCanvas{top: 41px;}.sapUiUx3ShellHeader>hr{background-color: #741919;}.sapUiRrPtb,.sapUiRrFtr{display:none}@font-face{font-family: Kruti Dev 010; src: url(Fonts/K010.ttf);}@font-face{font-family: Kruti Dev 011; src: url(Fonts/K11.TTF);}</style><script>/* sap.ui.getCore().attachInit(function(){new sap.m.Shell({app: new sap.ui.core.ComponentContainer({height : \"100%\", name : \"com.supremetest\"})}).placeAt(\"content\");}); *///sap.ui.localResources(\"supremetest\"); sap.ui.localResources(\"supremetoday\");//var view=sap.ui.view({id:\"idmain1\", viewName:\"supremetest.main\",/* */ type:sap.ui.core.mvc.ViewType.JS});var view=sap.ui.view({id : \"idmain1\",viewName : \"supremetoday.Main\",type : sap.ui.core.mvc.ViewType.XML});var YearWiseView=sap.ui.view({viewName : \"supremetoday.YearWise\",type : sap.ui.core.mvc.ViewType.XML});var BareActsView=sap.ui.view({viewName : \"supremetoday.BareActs\",type : sap.ui.core.mvc.ViewType.XML});var CommentaryView=sap.ui.view({viewName : \"supremetoday.Commentary\",type : sap.ui.core.mvc.ViewType.XML});var WordPhrasesView=sap.ui.view({viewName : \"supremetoday.WordPhrases\",type : sap.ui.core.mvc.ViewType.XML});//OverruledViewvar OverruledView=sap.ui.view({viewName : \"supremetoday.Overruled\",type : sap.ui.core.mvc.ViewType.XML});var GlobalSearchView=sap.ui.view({viewName : \"supremetoday.GlobalSearch\",type : sap.ui.core.mvc.ViewType.XML});var AdvanceSearchView=sap.ui.view({viewName : \"supremetoday.AdvanceSearch\",type : sap.ui.core.mvc.ViewType.XML});var CriminalMinorActsView=sap.ui.view({viewName : \"supremetoday.administrator.CriminalMinorActs\",type : sap.ui.core.mvc.ViewType.XML});var DictionaryView=sap.ui.view({viewName : \"supremetoday.administrator.Dictionary\",type : sap.ui.core.mvc.ViewType.XML});var OpenBookmarkView=sap.ui.view({viewName : \"supremetoday.administrator.OpenBookmark\",type : sap.ui.core.mvc.ViewType.XML});var HistoryView=sap.ui.view({viewName : \"supremetoday.administrator.History\",type : sap.ui.core.mvc.ViewType.XML});var ConfigureView=sap.ui.view({viewName : \"supremetoday.administrator.Configure\",type : sap.ui.core.mvc.ViewType.XML});var LiveUpdateView=sap.ui.view({viewName : \"supremetoday.administrator.LiveUpdate\",type : sap.ui.core.mvc.ViewType.XML});var CircularsNotificationsView=sap.ui.view({viewName : \"supremetoday.administrator.CircularsNotifications\",type : sap.ui.core.mvc.ViewType.XML});var FeedbackView=sap.ui.view({viewName : \"supremetoday.administrator.Feedback\",type : sap.ui.core.mvc.ViewType.XML});var HelpManualView=sap.ui.view({viewName : \"supremetoday.administrator.HelpManual\",type : sap.ui.core.mvc.ViewType.XML});var SupremeCourtOfIndiaView=sap.ui.view({viewName : \"supremetoday.administrator.SupremeCourtOfIndia\",type : sap.ui.core.mvc.ViewType.XML});var CauseListView=sap.ui.view({viewName : \"supremetoday.administrator.CauseList\",type : sap.ui.core.mvc.ViewType.XML});var AboutView=sap.ui.view({viewName : \"supremetoday.administrator.About\",type : sap.ui.core.mvc.ViewType.XML});var CentralView=sap.ui.view({viewName : \"supremetoday.administrator.Central\",type : sap.ui.core.mvc.ViewType.XML});var StateView=sap.ui.view({viewName : \"supremetoday.administrator.State\",type : sap.ui.core.mvc.ViewType.XML});var ConfigureSystemView=sap.ui.view({viewName : \"supremetoday.administrator.ConfigureSystem\",type : sap.ui.core.mvc.ViewType.XML});var UpdateRegistrationView=sap.ui.view({viewName : \"supremetoday.administrator.UpdateRegistration\",type : sap.ui.core.mvc.ViewType.XML});var SupremePadView=sap.ui.view({viewName : \"supremetoday.administrator.SupremePad\",type : sap.ui.core.mvc.ViewType.XML});var PrintSettingView=sap.ui.view({viewName : \"supremetoday.administrator.PrintSetting\",type : sap.ui.core.mvc.ViewType.XML});var oNavigationBar1=new sap.ui.ux3.NavigationBar({items : [ new sap.ui.ux3.NavigationItem({key : \"item1\",text : \"Item 1\"}), new sap.ui.ux3.NavigationItem({key : \"item2\",text : \"Item with some text 2\"}), new sap.ui.ux3.NavigationItem({key : \"item3\",text : \"Item with some text 3\"})]});//oNavigationBar1.placeAt(\"content\");//view.placeAt(\"content\");var oShell=new sap.ui.ux3.Shell(\"myShell1\",{appTitle : \"\",appIcon : \"\",appIconTooltip : \"\",showLogoutButton : false,showSearchTool : false,fullHeightContent : true,showInspectorTool : false,showFeederTool : false,worksetItems : [ new sap.ui.ux3.NavigationItem(\"WI_home\",{key : \"wi_home\",text : \"Administration\",subItems : [ new sap.ui.ux3.NavigationItem(\"WI_Desktop\",{key : \"WI_Desktop\",text : \"Home\"}),new sap.ui.ux3.NavigationItem(\"WI_Dictionary\",{key : \"WI_Dictionary\",text : \"Dictionary\"}),new sap.ui.ux3.NavigationItem(\"WI_Open_Bookmark\",{key : \"WI_Open_Bookmark\",text : \"OpenBookmark\"}),new sap.ui.ux3.NavigationItem(\"WI_History\",{key : \"WI_History\",text : \"History\"}),new sap.ui.ux3.NavigationItem(\"WI_Configure\",{key : \"WI_Configure\",text : \"Configure\"}),new sap.ui.ux3.NavigationItem(\"WI_Live_Update\",{key : \"WI_Live_Update\",text : \"Live Update\"}),new sap.ui.ux3.NavigationItem(\"WI_Feedback\",{key : \"WI_FeedBack\",text : \"FeedBack\"}),new sap.ui.ux3.NavigationItem(\"WI_Help_Manual\",{key : \"WI_Help_Manual\",text : \"Help Manual\"}),new sap.ui.ux3.NavigationItem(\"WI_Supreme_Court_Of_India\",{key : \"WI_Supreme_Court_Of_India\",text : \"Supreme Court Of India\"}),new sap.ui.ux3.NavigationItem(\"WI_Cause_List\",{key : \"WI_Cause_List\",text : \"Cause List\"}),new sap.ui.ux3.NavigationItem(\"WI_About\",{key : \"WI_About\",text : \"About\"})],}), new sap.ui.ux3.NavigationItem(\"WI_year_wise\",{key : \"wi_year_wise\",text : \"Year Wise\"}), new sap.ui.ux3.NavigationItem(\"WI_Bare_Acts\",{key : \"wi_Bare_Acts\",text : \"Bare Acts\"}),new sap.ui.ux3.NavigationItem(\"WI_Commentary\",{key : \"wi_Commentary\",text : \"Commentary\"}),new sap.ui.ux3.NavigationItem(\"WI_Word_Phrases\",{key : \"wi_Word_Phrases\",text : \"Word & Phrases\"}),new sap.ui.ux3.NavigationItem(\"WI_OverRuled\",{key : \"wi_OverRuled\",text : \"OverRuled\"}), new sap.ui.ux3.NavigationItem(\"WI_Global_Search\",{key : \"WI_Global_Search\",text : \"Global Search\"}), new sap.ui.ux3.NavigationItem(\"WI_Advance_Search\",{key : \"wi_Advance_Search\",text : \"Advance Search\"}) /* new sap.ui.ux3.NavigationItem(\"WI_Supreme_Search\",{key : \"wi_api\",text : \"Supreme Search\",subItems : [ new sap.ui.ux3.NavigationItem(\"WI_Global_Search\",{key : \"WI_Global_Search\",text : \"Global Search\"}), new sap.ui.ux3.NavigationItem(\"WI_Advance_Search\",{key : \"wi_Advance_Search\",text : \"Advance Search\"})],}) */],/*paneBarItems : [ new sap.ui.core.Item(\"PI_Date\",{key : \"pi_date\",text : \"date\"}), new sap.ui.core.Item(\"PI_Browser\",{key : \"pi_browser\",text : \"browser\"})],*/content : view,/*toolPopups : [ new sap.ui.ux3.ToolPopup(\"contactTool\",{title : \"New Contact\",tooltip : \"Create New Contact\",icon : \"images/Contact_regular.png\",iconHover : \"images/Contact_hover.png\",content : [ new sap.ui.commons.TextView({text : \"Here could be a contact sheet.\"})],buttons : [ new sap.ui.commons.Button(\"cancelContactButton\",{text : \"Cancel\",press : function(oEvent){sap.ui.getCore().byId(\"contactTool\").close();}})]})],*//*headerItems : [ new sap.ui.commons.TextView({text : \"User Name\",tooltip : \"U.Name\"}),new sap.ui.commons.Button({text : \"Personalize\",tooltip : \"Personalize\",press : function(oEvent){alert(\"Here could open an personalize dialog\");}}), new sap.ui.commons.MenuButton({text : \"Help\",tooltip : \"Help Menu\",menu : new sap.ui.commons.Menu(\"menu1\",{items : [ new sap.ui.commons.MenuItem(\"menuitem1\",{text : \"Help\"}), new sap.ui.commons.MenuItem(\"menuitem2\",{text : \"Report Incident\"}), new sap.ui.commons.MenuItem(\"menuitem3\",{text : \"About\"})]})})],*/worksetItemSelected : function(oEvent){var sId=oEvent.getParameter(\"id\");var oShell=oEvent.oSource;switch (sId){case \"WI_home\":oShell.setContent(view);break;case \"WI_year_wise\":oShell.setContent(YearWiseView);break;case \"WI_Bare_Acts\":oShell.setContent(BareActsView);break;case \"WI_Commentary\":oShell.setContent(CommentaryView);break;case \"WI_Word_Phrases\":oShell.setContent(WordPhrasesView);break;case \"WI_OverRuled\":oShell.setContent(OverruledView);break;case \"WI_Global_Search\":oShell.setContent(GlobalSearchView);break;case \"WI_Advance_Search\":oShell.setContent(AdvanceSearchView);break;case \"WI_Desktop\":oShell.setContent(view);break;case \"WI_CriminalMinorActs\":oShell.setContent(CriminalMinorActsView);break;case \"WI_Dictionary\":oShell.setContent(DictionaryView);break;case \"WI_Open_Bookmark\":oShell.setContent(OpenBookmarkView);break;case \"WI_History\":oShell.setContent(HistoryView);break;case \"WI_Configure\":oShell.setContent(ConfigureView);break;case \"WI_Live_Update\":oShell.setContent(LiveUpdateView);break;case \"WI_Circulars_Notifications\":oShell.setContent(CircularsNotificationsView);break;case \"WI_Feedback\":oShell.setContent(FeedbackView);break;case \"WI_Help_Manual\":oShell.setContent(HelpManualView);break;case \"WI_Supreme_Court_Of_India\":oShell.setContent(SupremeCourtOfIndiaView);break;case \"WI_Cause_List\":oShell.setContent(CauseListView);break;case \"WI_About\":oShell.setContent(AboutView);break;case \"WI_Central\":oShell.setContent(CentralView);break;case \"WI_State\":oShell.setContent(StateView);break;case \"WI_Configure_System\":oShell.setContent(ConfigureSystemView);break;case \"WI_Update_Registration\":oShell.setContent(UpdateRegistrationView);break;case \"WI_Supreme_Pad\":oShell.setContent(SupremePadView);break;case \"WI_Print_Setting\":oShell.setContent(PrintSettingView);break;default:break;}},/*paneBarItemSelected : function(oEvent){var sKey=oEvent.getParameter(\"key\");var oShell=oEvent.oSource;switch (sKey){case \"pi_date\":var oDate=new Date();oShell.setPaneContent(new sap.ui.commons.TextView({text : oDate.toLocaleString()}), true);break;case \"pi_browser\":oShell.setPaneContent(new sap.ui.commons.TextView({text : \"You browser provides the following information:\\n\"+ navigator.userAgent}), true);break;default:break;}},*/logout : function(){alert(\"Logout Button has been clicked.\\nThe application can now do whatever is required.\");},search : function(oEvent){alert(\"Search triggered: \" + oEvent.getParameter(\"text\"));},feedSubmit : function(oEvent){alert(\"Feed entry submitted: \"+ oEvent.getParameter(\"text\"));},paneClosed : function(oEvent){alert(\"Pane has been closed: \" + oEvent.getParameter(\"id\"));}});oShell.placeAt(\"content\");//jQuery.sap.require(\"com.supremetest.util.menuControl\");//oRowRepeater.placeAt(\"content\");Mousetrap.bind([ 'alt+a', 'option+a'], function(e){oShell.setSelectedWorksetItem(\"WI_home\");oShell.setContent(view);});Mousetrap.bind([ 'alt+y', 'option+y'], function(e){oShell.setSelectedWorksetItem(\"WI_year_wise\");oShell.setContent(YearWiseView);});Mousetrap.bind(['command+c', 'ctrl+c'], function(e){e.preventDefault();return false;});function hyperLink1(){alert(\"hyper link click\");var currentshell=oShell.getSelectedWorksetItem();console.log(currentshell);}$(window).on(\"click\", \".sapUiBtn\", function(){alert('clicked!');});$(document).ready(function(){});function temptest(vThis){console.log($(vThis).attr(\"data\"));//var selected=$.parseHTML(vThis);//console.log(selected);var caseId=$(vThis).attr(\"data\");console.log(caseId); //$.parseHTMLvar GlobalThis=viewThis;console.log(GlobalThis);GlobalThis.searchByCaseId(caseId);}function GoToGlobalSearch(vThis){//alert(\"called\");var GlobalThis=viewThis;//globalSearchString=string; //console.log(globalSearchString);oShell.setSelectedWorksetItem(\"WI_Global_Search\");oShell.setContent(GlobalSearchView);//GlobalThis.searchByCaseId(caseId);}function GoToAdvanceSearch(vThis){//alert(\"called\");var GlobalThis=viewThis;//globalSearchString=string; //console.log(globalSearchString);oShell.setSelectedWorksetItem(\"WI_Advance_Search\");oShell.setContent(AdvanceSearchView);//GlobalThis.searchByCaseId(caseId);}function globalsearchWithin(data){var GlobalThis=viewThis;globalSearchWithin=data; viewThis.addSearchWithin(data);// oShell.setSelectedWorksetItem(\"WI_Global_Search\");// GlobalThis.OverlayContainerClose();// oShell.setContent(GlobalSearchView);}function globalSearch(data){var GlobalThis=viewThis;var goToTop=false;var searchWithin=false;if(viewThis.oPersonalizationDialog.isOpen()){var goToTop=true;if(GlobalThis==\"\"){var searchWithin=true;}}var GlobalThis=viewThis; globalSearchString=data; oShell.setSelectedWorksetItem(\"WI_Global_Search\"); GlobalThis.OverlayContainerClose(); oShell.setContent(GlobalSearchView);}function menuItemsVisible(){var GlobalThis=viewThis;var goToTop=false;var searchWithin=false;if(viewThis.oPersonalizationDialog.isOpen()){var goToTop=true;if(GlobalThis==\"\"){var searchWithin=true;}}}function goToTop(){if($('#FullpageHeaderImage')){$('#FullpageHeaderImage').focus();$( \"#FullpageHeaderImage\" ).delay( 800 ).blur();}var GlobalThis=viewThis;GlobalThis.goToTop();}</script><link rel=\"stylesheet\" type=\"text/css\" href=\"css/style.css\"></head><body class=\"sapUiBody\" role=\"application\"><div id=\"content\"></div></body><script>$(window).scroll(function(event){console.log('scroll');});</script></html>";
//         content="TEST";
//            out.println(content);
////        }
//    }
//
//    protected void doGet(String method_name, HttpServletRequest request, HttpServletResponse response)
//            throws ServletException, IOException {
////        processRequest(request, response);
//        PrintWriter out = response.getWriter();
//
//        if (method_name.contains("/")) {
//            method_name = method_name.replace("/", "");
//        }
//        ResponseMaster data = null;
////        response.setContentType("application/json");
//        response.setContentType("application/json");
//        response.setCharacterEncoding("UTF-8");
//        response.setHeader("Access-Control-Allow-Origin", "*");//cross domain request/CORS
////        response.getWriter().write("Hello");
//        System.out.println(method_name + " call ");
////response.getWriter().write(method_name);
//        if (method_name.equals("test")) {
//
//            data = new ResponseMaster("test " + request.getRealPath(""), 200);
//        } else if (method_name.equals("getAllCourts")) {
//            data = new ServiceHelper().getAllCourts();
//        } else if (method_name.equals("getYearListByCourtId")) {
//            String courtId = request.getParameter("courtId");
//            data = new ServiceHelper().getYearListByCourtId(courtId);
//        } else if (method_name.equals("searchByCourtYearMonth")) {
//            String courtId = request.getParameter("courtId");
//            String year = request.getParameter("year");
//            String month = request.getParameter("month");
//            data = new ServiceHelper().searchByCourtYearMonth(courtId, year, month);
//        } else if (method_name.equals("search")) {
////            String courtId = request.getParameter("courtId");
////            String year = request.getParameter("year");
////            String month = request.getParameter("month");
////            data = new ServiceHelper().searchByCourtYearMonth(courtId, year, month);
//        } else if (method_name.equals("getCentralActList")) {
//            data = new ServiceHelper().getCentralActList();
//        } else if (method_name.equals("getCommentaryList")) {
//            data = new ServiceHelper().getCommentaryList();
//        } else if (method_name.equals("getAllActsRefTitle")) {
//            data = new ServiceHelper().getAllActsRefTitle();
//        } else if (method_name.equals("getAllActsRefSectionByTitle")) {
//            String title = request.getParameter("title");
//            System.out.println(title);
//            data = new ServiceHelper().getAllActsRefSectionByTitle(title);
//        } else if (method_name.equals("getAllJudges")) {
//            data = new ServiceHelper().getAllJudges();
//        } else if (method_name.equals("getAllPublishers")) {
//            data = new ServiceHelper().getAllPublishers();
//        } else if (method_name.equals("getAllYearByPublisher")) {
//            String publisher = request.getParameter("publisher");
//            data = new ServiceHelper().getAllYearByPublisher(publisher);
//        } else if (method_name.equals("getAllVolumeByPublisherYear")) {
//            String publisher = request.getParameter("publisher");
//            String year = request.getParameter("year");
//            data = new ServiceHelper().getAllVolumeByPublisherYear(publisher, year);
//        } else if (method_name.equals("getAllPageByPublisherYearVolume")) {
//            String publisher = request.getParameter("publisher");
//            String year = request.getParameter("year");
//            String volume = request.getParameter("volume");
//            data = new ServiceHelper().getAllPageByPublisherYearVolume(publisher, year, volume);
//        } else if (method_name.equals("getAllOverruled")) {
//            data = new ServiceHelper().getAllOverruled();
//        } else if (method_name.equals("getCaseReferedByCaseId")) {
//            String caseId = request.getParameter("id");
//            data = new ServiceHelper().getCaseReferedByCaseId(caseId);
//        } else if (method_name.equals("getCitatorByCaseId")) {
//            String caseId = request.getParameter("id");
//            data = new ServiceHelper().getCitatorByCaseId(caseId);
//        } else if (method_name.equals("getBookmarkByCaseId")) {
//            String caseId = request.getParameter("id");
//            data = new ServiceHelper().getBookmarkByCaseId(caseId);
//        } else if (method_name.equals("getAllBookmarks")) {
//            data = new ServiceHelper().getAllBookmarks();
//        } else if (method_name.equals("getHistory")) {
//            data = new ServiceHelper().getHistoryList();
//        } else if (method_name.equals("getHistoryListByKeywordOnly")) {
//            data = new ServiceHelper().getHistoryListByKeywordOnly();
//        } else if (method_name.equals("getUserDetails")) {
//            data = new ServiceHelper().getUserDetails();
//        } else if (method_name.equals("getCurrentVersion")) {
//            data = new ServiceHelper().getCurrentVersion();
//        } else if (method_name.equals("getSpellSuggestion")) {
//            String field = request.getParameter("field");
//            String content = request.getParameter("content");
//            data = new ServiceHelper().getSpellSuggestion(field, content);
//        } else if (method_name.equals("checkUpdate")) {
//            System.out.println("checkUpdate");
//            data = new ServiceHelper().checkUpdate();
//        } else if (method_name.equals("getUpdate")) {
//            System.out.println("getUpdate");
//            String version = request.getParameter("version");
//            String court = request.getParameter("court");
//            if (CommanHelper.isInternetAvail()) {
//                data = new ServiceHelper().getUpdate(version, court);
//            }else{
//                data = new ResponseMaster("Internet Not Available!", 201);
//            }
//        } else if (method_name.equals("versionUpdate")) {
//        String version = request.getParameter("version");
//            data= new ServiceHelper().versionUpdate(version);
//        } else {
//        }
//        out.print(new Gson().toJson(data));
//    }
//
//    /**
//     * Handles the HTTP <code>POST</code> method.
//     *
//     * @param request servlet request
//     * @param response servlet response
//     * @throws ServletException if a servlet-specific error occurs
//     * @throws IOException if an I/O error occurs
//     */
//    protected void doPost(String method_name, HttpServletRequest request, HttpServletResponse response)
//            throws ServletException, IOException {
//        PrintWriter out = response.getWriter();
////        String method_name = request.getParameter("method_name");
//        ResponseMaster data = null;
//
////System.out.println(method_name + " call ");
//        if (method_name.contains("/")) {
//            method_name = method_name.replace("/", "");
//        }
//        response.setContentType("application/json");
//        response.setCharacterEncoding("UTF-8");
//        response.setHeader("Access-Control-Allow-Origin", "*");//cross domain request/CORS
////System.out.println(method_name + " call ");
//        //  out.print(method_name);
//        if (method_name.equals("search")) {
//            JsonObject input = getRawData(request.getReader());
////            out.print(input.get("").getAsInt());
////            String queryString = request.getParameter("q");
////            int start = Integer.parseInt(request.getParameter("start"));
////            int rows = Integer.parseInt(request.getParameter("rows"));
////            String sortBy = request.getParameter("sort_by");//input.get("sort_by").getAsString();
////            String facet_fields=request.getParameter("facet_fields");//input.get("facet_fields").getAsString();
////            String fields=request.getParameter("fields");//input.get("fields").getAsString();
////            String hl_fields=request.getParameter("hl_fields");//input.get("hl_fields").getAsString();
////            String filter_query=request.getParameter("filter_query");//input.get("filter_query").getAsString();
//            String queryString = input.get("q").getAsString();
//            int start = input.get("start").getAsInt();
//            int rows = input.get("rows").getAsInt();
//            String sortBy = input.get("sort_by").getAsString();
//            String facet_fields = input.get("facet_fields").getAsString();
//            String fields = input.get("fields").getAsString();
//            String hl_fields = input.get("hl_fields").getAsString();
//
//            String filter_query = input.get("filter_query").getAsString();
//            String search_type = "1";
//            if (input.get("search_type") != null) {
//                search_type = input.get("search_type").getAsString();
//            }
//
////           out.print(input.get("q").getAsInt());
////            System.out.println(queryString + "  " + start + "  " + rows + "  " + sortBy + "  " + facet_fields + "  " + fields + "  " + hl_fields + "  " + filter_query);
//            data = new ServiceHelper().search(queryString, start, rows, sortBy, facet_fields, fields, hl_fields, filter_query, search_type);
////            System.out.println(new Gson().toJson(data));
//        } else if (method_name.equals("setBookmark")) {
//            JsonObject input = getRawData(request.getReader());
//            String _id = input.get("_id").getAsString();
//            String doc_id = input.get("doc_id").getAsString();
//            String title = input.get("title").getAsString();
//            String doc_type = input.get("doc_type").getAsString();
//            String category = input.get("category").getAsString();
//            String note = input.get("note").getAsString();
//            data = new ServiceHelper().setBookmark(_id, doc_id, title, doc_type, category, note);
//        } else if (method_name.equals("setHistory")) {
//            JsonObject input = getRawData(request.getReader());
////            String _id = input.get("_id").getAsString();
////            String doc_id = input.get("doc_id").getAsString();
//            String title = input.get("title").getAsString();
//            String query = input.get("query").getAsString();
//            String keyword = input.get("keyword").getAsString();
//            String search_type = input.get("search_type").getAsString();
//            data = new ServiceHelper().setHistory(title, query, keyword, search_type);
//        } else if (method_name.equals("removeBookmark")) {
//            String input = request.getReader().toString();
////            String _id = input.get("_id").getAsString();
////            String doc_id = input.get("doc_id").getAsString();
////            String title = input.get("title").getAsString();
////            String doc_type = input.get("doc_type").getAsString();
////            String category = input.get("category").getAsString();
////            String note = input.get("note").getAsString();
//            data = new ServiceHelper().removeBookmark(input);
//        } else if (method_name.equals("setEmail")) {
//            JsonObject input = getRawData(request.getReader());
//            System.out.println(input.toString());
//            String email = input.get("email").getAsString();
//            String user_name = input.get("user_name").getAsString();
//            String subject = input.get("subject").getAsString();
//            String content = input.get("content").getAsString();
//            System.out.println(email + user_name + content);
////            data = new ResponseMaster(email+user_name+content, 200);
//            data = new ServiceHelper().sendEmail(email, user_name, subject, content);
//
//        } else if (method_name.equals("translate")) {
//            JsonObject input = getRawData(request.getReader());
//            String language = input.get("language").getAsString();
//            String content = input.get("content").getAsString();
//            System.out.println(language + content);
//            //data = new ResponseMaster(language +" "+ content,200);
//            data = new ServiceHelper().translate(language, content);
//        }
//        out.print(new Gson().toJson(data));
////        out.print(data.getData().toString());
//    }
//
//    public JsonObject getRawData(BufferedReader reader) {
//        try {
//            StringBuilder buffer = new StringBuilder();
////        BufferedReader reader = request.getReader();
//            String line;
//            while ((line = reader.readLine()) != null) {
//                buffer.append(line);
//            }
//            JsonObject data = (JsonObject) new JsonParser().parse(buffer.toString());
//            return data;
////        obj.
//        } catch (IOException ex) {
//            Logger.getLogger(WebHandler.class.getName()).log(Level.SEVERE, null, ex);
//        }
//        return null;
//    }
//
//}
