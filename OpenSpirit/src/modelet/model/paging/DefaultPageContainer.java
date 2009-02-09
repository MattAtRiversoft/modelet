package modelet.model.paging;

import java.util.ArrayList;
import java.util.List;

public class DefaultPageContainer<E extends Object> implements PageContainer<E> {
	
	int totalPages = 0;
	List<E> rows = new ArrayList<E>();
	int totalRecords = 0;
	
	public int getTotalPages() {
		return totalPages;
	} 
	
	public void setTotalPages(int totalPasges) {
		this.totalPages = totalPasges;
	}
	
	public List<E> getRows() {
		return rows;
	}

	public void setRows(List<E> rows) {
		this.rows = rows;
	}

	public int getTotalRecords() {
		return this.totalRecords;
	}

	public void setTotalRecords(int totalRecords) {
		this.totalRecords = totalRecords;
	}
}
