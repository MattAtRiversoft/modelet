package modelet.entity;

import java.util.Date;

public abstract class AbstractAppEntity extends AbstractEntity implements AppEntity {

	private Date createDate;
	private String creator;
	private Date modifyDate;
	private String modifier;

	public void setModofier(String loginId) {
		this.modifier = loginId;
	}

	public Date getCreateDate() {
		return this.createDate;
	}

	public String getCreator() {
		return this.creator;
	}

	public String getModifier() {
		return this.modifier;
	}

	public Date getModifyDate() {
		return this.modifyDate;
	}

	public void setModifyDate(Date modifyDate) {
		this.modifyDate = modifyDate;
	}

	public void setModifier(String modifier) {
		this.modifier = modifier;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	public void setCreator(String loginId) {
		this.creator = loginId;
	}
}
