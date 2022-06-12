package com.sb.stock.service.repositories;

import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaDelete;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Root;

import org.hibernate.reactive.mutiny.Mutiny;
import org.springframework.stereotype.Component;

import com.sb.stock.service.domain.Stock;

import io.smallrye.mutiny.Uni;
import lombok.AllArgsConstructor;

@Component
@AllArgsConstructor
public class StockRepository extends GenericRepository<Stock, Long> {

    private final Mutiny.SessionFactory sessionFactory;

    public Uni<List<Stock>> findAll() {
	return super.findAll(sessionFactory);
    }

    public Uni<List<Stock>> findByKeyword(int offset, int limit, String keyword) {
	CriteriaBuilder cb = sessionFactory.getCriteriaBuilder();
	CriteriaQuery<Stock> query = cb.createQuery(entityClass);
	// set the root class
	Root<Stock> root = query.from(entityClass);

	// if keyword is provided
	if(keyword != null) {
	   // query.where(cb.or(cb.like(root.get(Stock_.name), "%" + keyword + "%")));
	}
	return super.findByKeyword(offset, limit, sessionFactory, query);
    }

    public Uni<Stock> findById(Long id) {
	return super.findById(id, sessionFactory);
    }

    public Uni<Stock> save(Stock Stock) {
	return super.save(Stock, sessionFactory);
    }
    
    public Uni<Stock> update(Stock Stock) {
	return super.update(Stock, sessionFactory);
    }

    public Uni<Stock[]> saveAll(List<Stock> data) {
	return super.saveAll(data, sessionFactory);
    }

    public Uni<Long> deleteById(Long id) {
	CriteriaBuilder cb = this.sessionFactory.getCriteriaBuilder();
        // create delete
        CriteriaDelete<Stock> delete = cb.createCriteriaDelete(entityClass);
        // set the root class
        Root<Stock> root = delete.from(entityClass);
        // set where clause
        delete.where(cb.equal(root.get("id"), id));
	return super.deleteById(sessionFactory,delete);
    }
    
    public Uni<Void> delete(Stock stock) {
	return super.delete(stock, sessionFactory);
    }

    public Uni<Long> deleteAll() {
	return super.deleteAll(sessionFactory);
    }
}
