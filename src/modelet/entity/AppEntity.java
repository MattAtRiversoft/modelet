package modelet.entity;

import java.util.Date;

public interface AppEntity extends Entity {

	public Date getCreateDate();
	public void setCreateDate(Date createDate);
	
	public Date getModifyDate();
	public void setModifyDate(Date modifyDate);
	
	public String getCreator();
	public void setCreator(String loginId);
	
	public String getModifier();
	public void setModofier(String loginId);
	
}
