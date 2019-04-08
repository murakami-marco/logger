package com.belatrixsf.handlers;

import java.sql.*;
import java.util.logging.Handler;
import java.util.logging.LogRecord;

public class JDBCHandler extends Handler {

    private String driverName;
    private String connectionUrl;
    private String username;
    private String password;
    private Connection connection;
    private PreparedStatement prepInsert;
    private final static String insertSQL = "insert into Log_Values (message, level) values(?,?)";

    public JDBCHandler(String driverName,  String connectionUrl, String username, String password) {
        this.driverName = driverName;
        this.connectionUrl = connectionUrl;
        this.username = username;
        this.password = password;
        initConn();
    }

    private void initConn() {
        try {
            Class.forName(driverName);
            connection = DriverManager.getConnection(connectionUrl);
            prepInsert = connection.prepareStatement(insertSQL);
        } catch (Exception e) {
            System.err.println("Error on open: " + e);
        }
    }

    private static String truncate(String str, int length) {
        if ( str.length() < length ) {
            return str;
        }
        return( str.substring(0, length) );
    }

    @Override
    public void publish(LogRecord logRecord) {
        // first see if this entry should be filtered out
        if (getFilter()!=null) {
            if (!getFilter().isLoggable(logRecord))
                return;
        }

        try {
            prepInsert.setString(1, truncate(logRecord.getMessage(), 255));
            prepInsert.setInt(2, logRecord.getLevel().intValue());
            prepInsert.executeUpdate();
        } catch ( SQLException e ) {
            System.err.println("Error on open: " + e);
        }
    }

    @Override
    public void flush() {

    }

    @Override
    public void close() throws SecurityException {
        try {
            if (connection!=null) {
                connection.close();
            }
        } catch (SQLException e) {
            System.err.println(String.format("CLOSE ERROR - Error code: %s on SQL State: %s, Message: %s", e.getErrorCode(), e.getSQLState(), e.getLocalizedMessage()));
        }
    }
}
