package hsqldb;

import java.sql.ResultSet;


public interface ResultsetHandler {

  <T> T handleResultset(ResultSet rs);
}
