package backend.main.swingtool;
import net.proteanit.sql.DbUtils;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.core.type.TypeReference;
import java.io.FileNotFoundException;
import java.util.Map;

import javax.swing.*;
import java.sql.*;
import java.io.InputStream;

public class ConnectSQL {
    public static void closeConnect(Connection con) {
        if (con != null) {
            try {
                con.close();
            } catch (SQLException e) {
                System.out.println("Error closing connection");
            }
        }
    }

    public static void showQuery(String query, JTable resultTable) {
        Connection con = null;
        PreparedStatement stmt;
        ResultSet rs;
        try {
            con = getConnection();
            stmt = con.prepareStatement(query);
            rs = stmt.executeQuery();
            if (!rs.isBeforeFirst()) {
                JOptionPane.showMessageDialog(null, "No data found", "Warning", JOptionPane.WARNING_MESSAGE);
            } else {
                resultTable.setModel(DbUtils.resultSetToTableModel(rs));
                JOptionPane.showMessageDialog(null, "Query success", "Info", JOptionPane.INFORMATION_MESSAGE);
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e.getMessage(), "Error", JOptionPane.WARNING_MESSAGE);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e.getMessage(), "Error", JOptionPane.WARNING_MESSAGE);
        } finally {
            closeConnect(con);
        }
    }

    /**
     * Load DB config from dbconfig.json and return a Connection.
     */
    public static Connection getConnection() throws Exception {
        InputStream is = ConnectSQL.class.getClassLoader().getResourceAsStream("dbconfig.json");
        if (is == null) {
            throw new FileNotFoundException("dbconfig.json not found in resources");
        }
        ObjectMapper mapper = new ObjectMapper();
        Map<String, String> cfg = mapper.readValue(is, new TypeReference<Map<String, String>>() {});
        Class.forName(cfg.get("driver"));
        return DriverManager.getConnection(cfg.get("url"), cfg.get("username"), cfg.get("password"));
    }

    public static void main(String[] args) {
        try (Connection con = getConnection()) {
            JOptionPane.showMessageDialog(null, "Connect OK", "Info", JOptionPane.INFORMATION_MESSAGE);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
