package modelet.model;

import java.util.List;
import java.util.Map;
import java.util.SortedMap;

import modelet.entity.Entity;
import modelet.model.paging.PageContainer;
import modelet.model.paging.PagingElement;


public interface Model {

  public <E extends Entity> List<E> find(String sql, Object[] params, Class<E> clazz);
  public List<Map<String, Object>> find(String sql, Object[] params);
  
  public PageContainer<SortedMap<String, Object>> findWithPaging(String sql, Object[] params, final PagingElement pagingElement) throws ModelException;
  public <E extends Entity> PageContainer<E> findWithPaging(String sql, Object[] params, final Class<E> clazz, final PagingElement pagingElement) throws ModelException;
  
  public <E extends Entity> E findOne(String sql, Object[] params, Class<E> clazz);
  
  public int save(Entity entity) throws ModelException;
  public <E extends Entity> void save(List<E> entities) throws ModelException;
 
  public int executeSql(String sql, Object[] params) throws ModelException;
}
