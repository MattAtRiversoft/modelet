package modelet.model;

import java.util.List;

import modelet.entity.Entity;


public interface Model {

  public <E extends Entity> List<E> find(String sql, Object[] params, Class<E> clazz);
  public void save(Entity entity) throws ModelException;
  public <E extends Entity> void save(List<E> entities) throws ModelException;
//  public <E extends Entity> E load(Class<E> clazz, Object id) throws ModelException;
  
}
