/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.project.helper;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author addie
 */
public class SqliteHelper {

    Connection conn = null;
    String dbURL = null;

    public SqliteHelper(String dbPath, boolean create) {
        this.dbURL = "jdbc:sqlite:" + dbPath;
        if (create) {
            if (!new File(dbPath).exists()) {
                //System.out.println("Not exist");
                createTables();
            }
        }
    }

    public void createTables() {
        try {
            open();
            Statement stmt = conn.createStatement();
            stmt.execute("CREATE TABLE user_bookmarks (_id INTEGER PRIMARY KEY AUTOINCREMENT,doc_id VARCHAR (12) NOT NULL,title VARCHAR (300) NOT NULL,doc_type VARCHAR (15) DEFAULT NULL,category VARCHAR(30),note TEXT,created_date TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP)");
            stmt.execute("CREATE TABLE user_history (_id INTEGER PRIMARY KEY AUTOINCREMENT,title VARCHAR (300) NOT NULL,query TEXT,keyword TEXT,search_type VARCHAR (15) DEFAULT NULL,created_date TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP)");
            stmt.execute("CREATE TABLE user_details (agent_code VARCHAR NOT NULL,user_name VARCHAR NOT NULL,organization_name VARCHAR,client_number VARCHAR NOT NULL,client_email VARCHAR,city VARCHAR NOT NULL,lic_key VARCHAR NOT NULL,client_package VARCHAR NOT NULL,sub_Id VARCHAR NOT NULL,created_date TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP)");
            stmt.execute("CREATE TABLE print_setting (_id INTEGER PRIMARY KEY AUTOINCREMENT,page_type VARCHAR, print_font_size INTEGER, display_font_size INTEGER, print_logo BOOLEAN, use_native_browser BOOLEAN)");
            close();
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(SqliteHelper.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SQLException ex) {
            Logger.getLogger(SqliteHelper.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public void open() throws ClassNotFoundException, SQLException {
        Class.forName("org.sqlite.JDBC");
        if (conn == null || conn.isClosed()) {

            conn = DriverManager.getConnection(dbURL);
            //System.out.println("Open");
        }
    }

    public void close() {
        try {
            if (!conn.isClosed()) {
                conn.close();
                //System.out.println("Close");
            }
        } catch (SQLException ex) {
            Logger.getLogger(SqliteHelper.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public ResultSet select(String query) throws SQLException {
        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery(query);
        return rs;
    }

    public int insert(String query) throws SQLException {
        Statement stmt = conn.createStatement();
        int rs = stmt.executeUpdate(query);
        return rs;
    }

    public boolean delete(String query) throws SQLException {
        Statement stmt = conn.createStatement();
        boolean rs = stmt.execute(query);
        return rs;
    }

}
