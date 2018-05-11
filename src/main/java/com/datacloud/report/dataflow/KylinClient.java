package com.datacloud.report.dataflow;

import java.sql.*;
import java.util.Calendar;
import java.util.Properties;
import java.util.TimeZone;

public class KylinClient {

    public static void main(String[] args) throws Exception {
//        System.setProperty("user.timezone","GMT +08");
        Driver driver = (Driver) Class.forName("org.apache.kylin.jdbc.Driver").newInstance();

        Properties info = new Properties();
        info.put("user", "ADMIN");
        info.put("password", "KYLIN");
//        info.put("useLegacyDatetimeCode", "false");
        Connection conn = driver.connect("jdbc:kylin://10.12.0.95:7070/datacloud_report", info);
        Statement state = conn.createStatement();
        ResultSet resultSet = state.executeQuery(
                "select CREDITDETAIL_CREDITDECISION, CREDITSCORE, CREDITSTRATEGYID, MINUTE_START from credit_invocation_history");
        if (resultSet != null) {
            ResultSetMetaData metaData = resultSet.getMetaData();
            for (int i=0; i<metaData.getColumnCount(); i++) {
                System.out.print(metaData.getColumnName(i+1));
                System.out.print("\t");
            }
            System.out.println();

            Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("GMT-8"));

            while (resultSet.next()) {
                System.out.print(
                    resultSet.getString(1) + "\t" +
                    resultSet.getDouble(2) + "\t" +
                    resultSet.getLong(3) + "\t" +
                    resultSet.getTimestamp(4, cal)
                );
                System.out.println();
            }
        }

    }

}
