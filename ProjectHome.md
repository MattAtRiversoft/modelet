# Origin #
This project is derived from several real world projects. No matter what kinds of application we develop, database is always the back-end, we need retrieve data and persist data into database, composing a lot of SQL statements and manage transactions. Now **we have Hibernate, iBatis, etc., these powerful and comprehensive frameworks help us to deal with database communication, but in many real cases, we don't need that big framework to be our skeleton while you are focusing on business competition; in other words, we just need an easy learning and quick development DB access framework**. Because of those reason, I decide to design a lite framework, _**Modelet**_ adopts the most significant concept from object-relational mapping framework. I make this framework easy to learn, save developer's time to compose SQL. **From the perspective of a manager, this does increase the productivity of developers but also reduce the error occurance when communication with database.**

# Modelet 1.0 announced #
Thanks to many developers feedback and test, after series of revision and bugfix, I am pleased to announce the first formal release of Modelet 1.0. Please check Quick Start section and give me more advises.

# Quick Start #
## Step 1. Spring XML configuration ##
Please have the following Spring _bean_ definitions in your Spring XML file. Because the core of _**Modelet**_ uses Spring's declarative transactional management, and be sure to use the same bean id as shown below. _**Modelet**_ cannot run without Spring correct definition. For more information about Spring Framework, please refer to [Spring web site](http://www.springsource.org/documentation)
```
   <bean id="defaultDataSource" class="org.apache.commons.dbcp.BasicDataSource" destroy-method="close">
    <property name="driverClassName">
      <value>${jdbc.driverClassName}</value>
    </property>
    <property name="url">
      <value>${jdbc.url}</value>
    </property>
    <property name="username">
      <value>${jdbc.username}</value>
    </property>
    <property name="password">
      <value>${jdbc.password}</value>
    </property>
    <property name="maxActive">
      <value>100</value>
    </property>
  </bean>
 
  <tx:annotation-driven transaction-manager="transactionManager" proxy-target-class="true"/>

  <bean id="jdbcTemplate" class="org.springframework.jdbc.core.JdbcTemplate">
    <property name="dataSource" ref="defaultDataSource" />
  </bean>
  
  <bean id="transactionManager" class="org.springframework.jdbc.datasource.DataSourceTransactionManager">
    <property name="dataSource" ref="defaultDataSource"/>
  </bean>

  <bean id="rowsPerPage" class="java.lang.Integer" scope="singleton">
    <constructor-arg>
      <value>${rowsPerPage}</value>
    </constructor-arg>
  </bean>
```

## Step 2. Define Entity Class ##
### Step 2.1 Know Entity Interface First ###
_**Modelet**_ persists entity content into database according to your Entity implementation. Please implements 'modelet.entity.Entity' Interface. Below is Entity source and its Java Doc, please take a glance of it.
```
import java.util.List;

public interface Entity {

  /**
   * Modelet supposes each entity must at lease have 'id' field as primary key or unique key to identify a record.
   * 
   * Be default, Modelet think the sequence number of id is generated within database, Modelet will retrieve the given it and 
   * set it back to entity after successfully insert; but if you prefer to generate id by your framework self, please just implements
   * SysemIncrementEntity, then Model won't try to retrieve id from database.
   * 
   * If your entity does not need this id field, please add it to getExclusiveFields() fields.
   */
	public Object getId();
	public void setId(Object id);
	
	/**
	 * Modelet converts your entity content into INSERT, UPDATE, DELETE sql statement depending on this fields setting.
	 * 
	 * Once successful insert, Modelet will convert entity's txnModel into UPDATE state. If an entity is retrieved from database, its txnMode
	 * will also be set to UPDATE automatically.
	 * 
	 * Just new created Entity should be at INSERT state.
	 * 
	 * If you want to delete a entity, please set txnMode to DELETE.
	 */
	public TxnMode getTxnMode();
	public void setTxnMode(TxnMode txnMode);

	/**
	 * Modelet maps your entity to database table by this method return. If your table has schema prefix, please return like this: schemaName.TableName
	 * @return a database table name.
	 */
	public String getTableName();
	
	/**
	 * This method is used in UPDATE and DELETE action. Modelet generates "where" criteria by parsing your key definition.
	 * If your entity extends modelet.entity.AbstractEntity, the "id" field is default used as key field.
	 */
	public List<String> getKeyNames();
	
	/**
	 * Sometimes you will have more complicated SQL, like 'join' statement, and convert its results into entity. You should exclude these extra joined
	 * fields if you want to persist your entity back to database again. This method is used to tell Modelet which fields not to be included into SQL statements.
	 */
	public List<String> getExclusiveFields();
	
	/**
	 * Before this entity is converted to SQL statement, this method will be called.
	 */
	public void beforeSave();
	
	/**
	 * After this entity is update to database, this method will be called. 
	 */
	public void afterSave();
}

```

**Modelet already has a abstract entity definition for you, you can just extends modelet.entity.AbstractEntity for quick use.**

### Step 2.2 Define Your Abstract/Default Entity ###
Let's take a Student as example, please see the following source code :
```
import java.math.BigDecimal;
import java.util.Date;

import modelet.entity.AbstractEntity;

import org.springframework.stereotype.Repository;

@Repository("student")
public class Student extends AbstractEntity {

	private String studentName;
	private Date birthday;
	private Long height;
	private BigDecimal weight;
	private boolean removed = false;
	
	//... setters and getters for fields above

	public String getTableName() {
		return "_Student";
	}

  public void afterSave() {
  }

  public void beforeSave() {
  }
}
```

This Student entity class is declared as Spring bean. Besides these fields definition, you just have to override the _getTableName()_ method. Please also make sure fields name is the same as table's column name in database.

### Step 2.3 Define Table Schema ###
After define your Java entity class, now let's define corresponding table schema. I take MySQL as example:
```
CREATE TABLE `_student` (
  `id` BIGINT NOT NULL AUTO_INCREMENT,
  `studentName` VARCHAR(20),
  `birthday` DATETIME,
  `height` BIGINT,
  `weight` DECIMAL(18,2),
  `removed` BOOLEAN,
  PRIMARY KEY (`id`)
)
ENGINE = InnoDB
CHARACTER SET utf8 COLLATE utf8_general_ci;
```
Please note that each field's data type should be the same with Java type.
| **Java Type** | **Database Data Type** |
|:--------------|:-----------------------|
| String | varchar, char |
| Date | datetime |
| Long, long | bigint |
| BigDecimal | decimal, number |
| Boolean, boolean | boolean, bit |
| Enum | String |
| Array of Enum | String |

## Step 3. Define Logic Class ##
_**Modelet**_ has a default logic class implementation for you to retrieve entity from database and persist entity content into database as well. You can write your own logic class, and use Spring injection to use _**Modelet**_ utility. Here is a StudentLogic implementation used to get all students data and save student data into database.
```
import java.util.List;

import javax.annotation.Resource;

import modelet.model.Model;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import demo.entity.Student;

@Service("studentModel")
@Transactional(readOnly = true)
public class StudentModel {

	@Resource(name = "defaultModel")	
	private Model model;

	public List<Student> getAllStudents() {
	
		String sql = "select * from _Student order by id";
		return model.find(sql, null, Student.class);
	}
	
	@Transactional(readOnly = false)
	public void saveStudent(Student student) {
		
		//add your business logic here
		getModel().save(student);
	}
	
	public Model getModel() {
		return model;
	}

	public void setModel(Model model) {
		this.model = model;
	}
}
```

Please see the most important code fragment
```
@Resource(name = "defaultModel")	
private Model model;
```
It would be better to use Model implementation as a property of your logic class. **Strategy design pattern** is highly recommended than for modern programming concept. Less inheritances, more interface design can make each module more decoupling.

Spring bean "defaultModel" is mapped to **modelet.model.DefaultModel** class that implements Model interface. You can see source code Java Doc for detailed usage.

## Step 4. Execute Your Code ##
With one XML definition and two Java class implementation, now you can unit test your code which has Spring context environment or migrate them into your web application. A demo web application will be delivered soon.

# Source #
Please use Subversion to checkout latest source code. Following is the URL:
[http://modelet.googlecode.com/svn/trunk/](http://modelet.googlecode.com/svn/trunk/)

# Issue or Request #
Please email [service@riversoft.com.tw](mailto:service@riversoft.com.tw) for any issue report or feature requests.