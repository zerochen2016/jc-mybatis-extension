package jc.mybatis.extension.util;

import java.io.Serializable;
import java.util.List;

/**
 * 
 * @author JC
 * @Date 2019年11月17日
 * @since
 * @param <T>
 */
public class PageModel<T> implements Serializable{
	
    /**
	 * 
	 */
	private static final long serialVersionUID = -2410343965381720697L;
	/**
	 * 总条数
	 */
	private Long totalCount = 0l;
	
	/**
	 * 当前页
	 */
	private Integer page = 1;
	
	/**
	 * 分页大小
	 */
	private Integer pageSize = 20;
	
	/**
	 * 数据内容
	 */
	private List<T>data;
	
	public PageModel<T> build(List<T> list,long totalCount,PageModel<T> pageModel){
		pageModel.setData(list);
		pageModel.setTotalCount(totalCount);
		return pageModel;
	}	
	
	public PageModel<T> build(int page,int pageSize,List<T> list,long totalCount){
		PageModel<T> pageModel = new PageModel<T>();
		pageModel.setPage(page);
		pageModel.setData(list);
		pageModel.setPageSize(pageSize);
		pageModel.setTotalCount(totalCount);
		return pageModel;
	}
	
	public int getLimitStart() {
		return (page-1)*pageSize;
	}
	
	public int getLimitStart(Integer page,Integer pageSize) {
		if(page==null||pageSize==null) {
			return 0;
		}
		if(page<1||pageSize<0) {
			return 0;
		}
		return (page-1)*pageSize;
	}
	
	public PageModel(){

	}
	public Long getTotalCount() {
		return totalCount;
	}

	public Integer getPageSize() {
		return pageSize;
	}

	public void setPageSize(Integer pageSize) {
		this.pageSize = pageSize;
	}

	public void setTotalCount(Long totalCount) {
		this.totalCount = totalCount;
	}

	public Integer getPage() {
		return page;
	}

	public void setPage(Integer page) {
		this.page = page;
	}

	public List<T> getData() {
		return data;
	}

	public void setData(List<T> data) {
		this.data = data;
	}
	
}
