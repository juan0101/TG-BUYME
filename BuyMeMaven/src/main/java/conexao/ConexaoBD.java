package conexao;

import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.mysql.jdbc.Connection;
import com.mysql.jdbc.Statement;

public class ConexaoBD {
	public Connection conn;
    private Statement statement;
    public static ConexaoBD db;
    private ConexaoBD() {
        String url= "jdbc:mysql://localhost:3306/";
        String dbName = "BUYME_TESTE";
        String driver = "com.mysql.jdbc.Driver";
        String userName = "root";
        String password = "123";
        try {
            Class.forName(driver).newInstance();
            this.conn = (Connection)DriverManager.getConnection(url+dbName,userName,password);
        }
        catch (Exception sqle) {
            sqle.printStackTrace();
        }
    }
    /**
     *
     * @return MysqlConnect Database connection object
     */
    public static synchronized ConexaoBD getDbCon() {
        if ( db == null ) {
            db = new ConexaoBD();
        }
        return db;
 
    }
    /**
     *
     * @param query String The query to be executed
     * @return a ResultSet object containing the results or null if not available
     * @throws SQLException
     */
    public ResultSet query(String query) throws SQLException{
        statement = (Statement) db.conn.createStatement();
        ResultSet res = statement.executeQuery(query);
        return res;
    }
    /**
     * @desc Method to insert data to a table
     * @param insertQuery String The Insert query
     * @return boolean
     * @throws SQLException
     */
    public int insert(String insertQuery) throws SQLException {
        statement = (Statement) db.conn.createStatement();
        int result = statement.executeUpdate(insertQuery);
        return result;
 
    }

}
