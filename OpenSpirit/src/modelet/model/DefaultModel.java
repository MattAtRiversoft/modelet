package modelet.model;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;
import javax.sql.DataSource;

import modelet.entity.Entity;
import modelet.entity.SystemIncrementEntity;
import modelet.entity.TxnMode;

import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.ResultSetHandler;
import org.apache.commons.dbutils.handlers.BeanListHandler;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.log4j.Logger;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component("defaultModel")
@Transactional(readOnly = true)
public class DefaultModel {

	private static final Log LOG = LogFactory.getLog(DefaultModel.class);
  private static final Logger EXP_LOG = Logger.getLogger("exceptionLog");
  
	protected DataSource dataSource;
	protected JdbcTemplate jdbcTemplate;
	protected QueryRunner queryRunner;

	private boolean txnSuccessful = true;
  private String txnErrorStack = null;
  
  public String getTxnErrorStack() {
		return txnErrorStack;
	}

	private void setTxnErrorStack(String txnErrorStack) {
		this.txnErrorStack = txnErrorStack;
	}

	public boolean isTxnSuccessful() {
		return txnSuccessful;
	}

	private void setTxnSuccessful(boolean txnSuccessful) {
		this.txnSuccessful = txnSuccessful;
	}

	@Transactional(readOnly = false)
  public <E extends Entity> void save(List<E> entities) throws ModelException {
  	
  	for (E e : entities) {
  		save(e);
  	}
  }
  
  @Transactional(readOnly = false)
  public void save(Entity entity) throws ModelException {
  	
  	TxnMode txnMode = entity.getTxnMode();
  	if (txnMode == null)
  		throw new ModelException("Please assign action type(insert, update, delete) before persist entity to database.");

  	entity.beforeSave();
  	if (txnMode.equals(TxnMode.INSERT))
  		insert(entity);
  	else if (txnMode.equals(TxnMode.UPDATE))
  		update(entity);
  	else if (txnMode.equals(TxnMode.DELETE))
  		delete(entity);
  	
  	entity.afterSave();
  }
  
	private void insert(final Entity entity) {

		final StatementSet stmtSet = ModelUtil.buildPreparedCreateStatement(entity);
    try {
    	int returnCode;
    	if (entity instanceof SystemIncrementEntity) {
    		returnCode= jdbcTemplate.update(stmtSet.getSql(), stmtSet.getParams());
    	}
    	else {
	      KeyHolder keyHolder = new GeneratedKeyHolder();
	      returnCode = jdbcTemplate.update(new PreparedStatementCreator() {
	
	        public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
	        	
	          LOG.info("Model INFO :" + stmtSet.getSql() + " param : " + Arrays.toString(stmtSet.getParams()));
	          PreparedStatement ps = connection.prepareStatement(stmtSet.getSql(), new String[] { "id" });
	          appendParam(ps, stmtSet.getParams());
	          return ps;
	        }
	      }, keyHolder);
	
	      long keyValue = keyHolder.getKey().longValue();
	      entity.setId(keyValue);
    	}
    	LOG.info("INSERT returnCode : [" + returnCode + "]");
      entity.setTxnMode(TxnMode.UPDATE);
      
    }
    catch (DataAccessException e) {
    	e.printStackTrace();
    	setTxnSuccessful(false);
    	logSqlError(e, stmtSet.getSql(), stmtSet.getParams());
    	logExceptionStack(e);
    }
  }
	
	private void update(Entity entity) {
		
		StatementSet stmtSet = ModelUtil.buildPreparedUpdateStatement(entity);
		try {
			int returnCode = jdbcTemplate.update(stmtSet.getSql(), stmtSet.getParams());
			LOG.info("UPDATE returnCode : [" + returnCode + "]");
		}
    catch (DataAccessException e) {
    	e.printStackTrace();
    	setTxnSuccessful(false);
    	logSqlError(e, stmtSet.getSql(), stmtSet.getParams());
    	logExceptionStack(e);
    }
	}
	
	private void delete(Entity entity) {
		
		String sql = ModelUtil.buildDeleteStatement(entity);
		try {
			int returnCode = jdbcTemplate.update(sql);
			LOG.info("DELETE returnCode : [" + returnCode + "]");
		}
		catch (DataAccessException e) {
    	e.printStackTrace();
    	setTxnSuccessful(false);
    	logSqlError(e, sql);
    	logExceptionStack(e);
    }
	}
	
	public <E extends Entity> List<E> find(String sql, Object[] params, Class<E> clazz) {
    
    ResultSetHandler rsHandler = new BeanListHandler(clazz);
    List<E> entities = new ArrayList<E>(0);
    try {
    	convertDateIn(params);
    	entities = (List<E>) getQueryRunner().query(sql, params, rsHandler);
    	for (Entity e: entities) {
    		e.setTxnMode(TxnMode.UPDATE);
    	}
    }
    catch (SQLException e) {
    	e.printStackTrace();
    	logSqlError(e, sql, params);
    	logExceptionStack(e);
    }
    return entities;
  }

	@Resource(name = "defaultDataSource")
	public void setDataSource(DataSource dataSource) {
	  this.dataSource = dataSource;
	}
	
	@Resource(name = "jdbcTemplate")
	public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
	  this.jdbcTemplate = jdbcTemplate;
	}
	
	@Resource(name = "queryRunner")
	public void setQueryRunner(QueryRunner queryRunner) {
	  this.queryRunner = queryRunner;
	}
	
	protected DataSource getDataSource() {
		return dataSource;
	}

	protected JdbcTemplate getJdbcTemplate() {
		return jdbcTemplate;
	}

	protected QueryRunner getQueryRunner() {
		return queryRunner;
	}

	private <A> A executeSql(String sql, Object[] params, RstHandler<A> handler) throws SQLException  {
  	
  	A rs = null;
  	try {
  		Connection cnct = getDataSource().getConnection();
	    PreparedStatement stmt = cnct.prepareStatement(sql);
	    for (int i=0; params != null && (i<params.length); i++) {
	    	Object obj = params[i];
	    	if (obj instanceof Date) {
	    		obj = new Timestamp(((Date)obj).getTime());
	    		params[i] = obj;
	    	}
	      stmt.setObject(i+1, params[i]);
	    }
	    ResultSet rst = stmt.executeQuery();
	    rs = handler.handleRst(rst);
	    rst.close();
	    stmt.close();
  	}
  	catch (SQLException e) {
  		e.printStackTrace();
  		throw e;
  	}
		//  	finally {
		//  		closeConnection();
		//  	}
    return rs;
  }
  
  abstract class RstHandler<A> {
  	abstract A handleRst(ResultSet rst);
  }
	
	private void appendParam(PreparedStatement ps, Object param[]) throws SQLException {

    int i = 1;
    for (Object value : param) {
      if (value == null)
        ps.setNull(i, java.sql.Types.NULL);
      else if (value instanceof String)
        ps.setString(i, (String) value);
      else if (value instanceof Integer)
        ps.setInt(i, (Integer) value);
      else if (value instanceof Long)
        ps.setLong(i, (Long) value);
      else if (value instanceof BigDecimal)
      	ps.setBigDecimal(i, (BigDecimal) value);
      else if (value instanceof Timestamp)
      	ps.setTimestamp(i, (Timestamp) value);
      else
        ps.setObject(i, value);
      i++;
    }
  }
	
	private Object[] convertDateIn(Object[] params) {
  	
  	for (int i=0; params != null && (i<params.length); i++) {
    	Object obj = params[i];
    	if (obj instanceof Date) {
    		obj = new Timestamp(((Date)obj).getTime());
    		params[i] = obj;
    	}
    }
  	return params;
  }
	
	private void logSqlError(Exception e, String sql, Object... params) {
  	
  	StringBuffer paramsContent = new StringBuffer();
		for (int i=0; i<params.length; i++) {
			Object aparam = params[i];
			if (aparam == null)
				aparam = "[null]";
			paramsContent.append(aparam.toString()).append("  ");
		}
  	EXP_LOG.error("Fail to execute: " + sql + ", params: " + paramsContent.toString(), e);
  }
	
	private void logExceptionStack(Exception e) {
		
		StringBuffer content = new StringBuffer();
		StackTraceElement[] trace = e.getStackTrace();
  	if (trace != null) {
  		for (int i=0; i<trace.length; i++) {
  			content.append(trace[i].getClassName() + " : " + trace[i].getMethodName() + " : " + trace[i].getLineNumber()).append("\n");
  		}
  	}
  	setTxnErrorStack(content.toString());
	}
}
