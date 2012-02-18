package hsqldb;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;


public class HsqldbServer implements Runnable {

  private HsqldbServer me = new HsqldbServer();
  
  private Connection hsqldb;
  private boolean keepsRunning = true;
  
  private List<String> initScript = new ArrayList<String>();
  
  private HsqldbServer() {
  }
  
  public HsqldbServer getInstance() {
    return me;
  }
  
  public void shutdownHSqlDb() {
    this.keepsRunning = false;
  }
  
  public int executeSql(String sql) throws SQLException {
    
    int rs = 0;
    Statement stmt = this.hsqldb.createStatement();
    rs = stmt.executeUpdate(sql);
    stmt.close();
    
    return rs;
  }
  
  public <T> T executeQuery(String sql, ResultsetHandler handler) throws SQLException {
    
    Statement stmt = this.hsqldb.createStatement();
    ResultSet rs = stmt.executeQuery(sql);
    T t = handler.handleResultset(rs);
    return t;
  }
  
  public void run() {
    
    try {
      startDatabase();
      initDatabase();
      while (keepsRunning) {
        
        Thread.sleep(200);
      }
      shutdown();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  private void startDatabase() throws Exception {
    
    Class.forName("org.hsqldb.jdbcDriver");
    hsqldb = DriverManager.getConnection("jdbc:hsqldb:file:demodb", "sa", "");
  }
  
  private void initDatabase() throws Exception {
    
    Statement stmt = hsqldb.createStatement();
    for (String sql : initScript) {
      stmt.executeUpdate(sql);
    }
    stmt.close();
  }
  
  private void shutdown() throws Exception {

    Statement st = hsqldb.createStatement();
    st.execute("SHUTDOWN");
    hsqldb.close(); 
  }
  
}
