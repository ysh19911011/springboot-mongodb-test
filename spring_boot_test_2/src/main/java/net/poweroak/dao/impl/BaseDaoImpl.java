package net.poweroak.dao.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;

import com.mongodb.Mongo;

import net.poweroak.bean.Page;
import net.poweroak.dao.BaseDao;
import net.poweroak.utils.ReflectionUtils;

public abstract class BaseDaoImpl<T> implements BaseDao<T> {

	@Resource
	private MongoTemplate mongoDBTemplate;
	
	public List<T> find(Query query) {
		return mongoDBTemplate.find(query, this.getEntityClass());
	}

	public T findOne(Query query) {
		return mongoDBTemplate.findOne(query, this.getEntityClass());
	}

	public T update(Query query, Update update) {
		return mongoDBTemplate.findAndModify(query, update, this.getEntityClass());
	}
	
	public T save(T entity) {
		mongoDBTemplate.insert(entity);
		return entity;
	}
	
	public T findById(Object id) {
		return mongoDBTemplate.findById(id, this.getEntityClass());
	}

	public T findById(String id, String collectionName) {
		return mongoDBTemplate.findById(id, this.getEntityClass(), collectionName);
	}

	public Page<T> findPage(Page<T> page, Query query) {
		int count = (int) this.count(query);
		page.setTotal(count);
		int pageNumber = page.getPage();
		int pageSize = page.getPageSize();
		int pageCount = count /pageSize;
		if (count % pageSize > 0) {
			pageCount++;
		}
		page.setPageCount(pageCount);
		query.skip((pageNumber - 1) * pageSize).limit(pageSize);
		List<T> rows = this.find(query);
		page.setResult(rows);
		return page;
	}

	public void delete(String id) {
		mongoDBTemplate.remove(new Query().addCriteria(Criteria.where("id").is(id)), this.getEntityClass());
	}

	public long count(Query query) {
		return mongoDBTemplate.count(query, this.getEntityClass());
	}

	/**
	 * 获取需要操作的实体类class
	 * 
	 * @return
	 */
	private Class<T> getEntityClass() {
		return ReflectionUtils.getSuperClassGenricType(getClass());
	}
	
	public MongoTemplate getMongoTemplate(){
			return this.mongoDBTemplate;
	}

}