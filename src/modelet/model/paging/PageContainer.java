package modelet.model.paging;

import java.util.List;

public interface PageContainer<E extends Object> {

	public int getTotalPages();
	public void setTotalPages(int totalPages);
	
	public List<E> getRows();
	public void setRows(List<E> entities);
	
	public int getTotalRecords();
	public void setTotalRecords(int totalRecords);
	
	public List<String> generatePageNumbers();
}
