
package net.poweroak.bean;
import java.util.ArrayList;
import java.util.List;

/**
 * 查询结果封装类
 * @author zzze
 * @date 2010-4-23
 * @version 1.0
 */
public class Page<T> {
	// 每页最大记录数限制
	public static final Integer MAX_PAGE_SIZE = 100;

	String sort = "createDate"; // 排序
	String order = "desc"; // 升降序
	// 结果集，查询时自动填充
	private Integer rows=15;// 接收查询数据条数
	// 总数据量，查询时会自动填充
	private Integer total;// 总数据条数
	//当前页页码
	private Integer page = 1;
	// 总页数
	private Integer pageCount = 0;
	// 每页记录数
	private Integer pageSize = 15;
	// page对象返回结果
	private List<?> result;
	
	public Integer getTotal() {
		return total;
	}
	public void setTotal(final Integer total) {
		this.total = total;
	}
	public String getSort() {
		return sort;
	}
	public void setSort(String sort) {
		this.sort = sort;
	}
	public String getOrder() {
		return order;
	}
	public void setOrder(String order) {
		this.order = order;
	}
	public Integer getRows() {
		return rows;
	}
	public void setRows(Integer rows) {
		this.rows = rows;
	}
	public Integer getPage() {
		return page;
	}
	public void setPage(Integer page) {
		this.page = page;
	}
	public Integer getPageCount() {
//		Integer pageSize = rows;
		pageCount = total /pageSize;
		if (total % pageSize > 0) {
			pageCount++;
		}
		return pageCount;
	}
	public void setPageCount(Integer pageCount) {
		this.pageCount = pageCount;
	}
	public Integer getPageSize() {
		return pageSize;
	}

	public void setPageSize(Integer pageSize) {
		if (pageSize < 1) {
			pageSize = 1;
		}
		this.pageSize = pageSize;
	}
	public List<?> getResult() {
		return result;
	}
	public void setResult(List<?> result) {
		this.result = result;
	}
	
}
