package com.sb.stock.service.repositories;

import java.lang.reflect.Array;
import java.util.List;
import java.util.Objects;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaDelete;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Root;

import org.hibernate.reactive.mutiny.Mutiny;

import com.sb.stock.service.exception.NotFound;
import com.sb.stock.service.utils.GenericUtils;

import io.smallrye.mutiny.Uni;


public abstract class GenericRepository<T, ID> {

    private static final GenericUtils genericUtil = new GenericUtils();

    @SuppressWarnings("unchecked")
    final Class<T> entityClass = (Class<T>) genericUtil.getGenericClass(0);
    @SuppressWarnings("unchecked")
    final Class<ID> idClass = (Class<ID>) genericUtil.getGenericClass(1);

    protected Uni<List<T>> findAll(final Mutiny.SessionFactory sessionFactory) {
	CriteriaBuilder cb = sessionFactory.getCriteriaBuilder();
	// create query
	CriteriaQuery<T> query = cb.createQuery(entityClass);
	// set the root class
	Root<T> root = query.from(entityClass);
	return sessionFactory.withSession(session -> session.createQuery(query).getResultList());
    }

    protected Uni<List<T>> findByKeyword(int offset, int limit, final Mutiny.SessionFactory sessionFactory,final CriteriaQuery<T> query) {

	// perform query
	return sessionFactory.withSession(
		session -> session.createQuery(query).setFirstResult(offset).setMaxResults(limit).getResultList());
    }

    protected Uni<T> findById(ID id, final Mutiny.SessionFactory sessionFactory) {
	Objects.requireNonNull(id, "id can not be null");
	return sessionFactory.withSession(session -> session.find(entityClass, id)).onItem().ifNull()
		.failWith(() -> new NotFound(id.toString()));
    }

    protected Uni<T> save(T entity, final Mutiny.SessionFactory sessionFactory) {
	return sessionFactory
		.withSession(session -> session.persist(entity).chain(session::flush).replaceWith(entity));
    }

    protected Uni<T> update(T entity, final Mutiny.SessionFactory sessionFactory) {
	return sessionFactory.withSession(session -> session.merge(entity).onItem().call(session::flush));
    }

    protected Uni<T[]> saveAll(List<T> data, final Mutiny.SessionFactory sessionFactory) {
	T[] array = data.toArray((T[]) Array.newInstance(entityClass, 0));
	return sessionFactory.withSession(session -> {
	    session.persistAll(array);
	    session.flush();
	    return Uni.createFrom().item(array);
	});
    }

//    @Transactional
//    public Uni<Integer> updateStatus(UUID id, Post.Status status) {
//        CriteriaBuilder cb = sessionFactory.getCriteriaBuilder();
//        // create update
//        CriteriaUpdate<T> delete = cb.createCriteriaUpdate(entityClass);
//        // set the root class
//        Root<T> root = delete.from(entityClass);
//        // set where clause
//        delete.set(root.get(Post_.status), status);
//        delete.where(cb.equal(root.get(Post_.id), id));
//        // perform update
//        return session.createQuery(delete).executeUpdate();
//    }

    protected Uni<ID> deleteById(final Mutiny.SessionFactory sessionFactory,final CriteriaDelete<T> delete) {
	// perform update
	return (Uni<ID>) sessionFactory.withTransaction((session, tx) -> session.createQuery(delete).executeUpdate());
    }
    
    protected Uni<Void> delete(T entity, final Mutiny.SessionFactory sessionFactory) {
	// perform update
	return  sessionFactory.withTransaction((session, tx) -> session.remove(entity));
    }

    protected Uni<ID> deleteAll(final Mutiny.SessionFactory sessionFactory) {
	CriteriaBuilder cb = sessionFactory.getCriteriaBuilder();
	// create delete
	CriteriaDelete<T> delete = cb.createCriteriaDelete(entityClass);
	// set the root class
	Root<T> root = delete.from(entityClass);
	// perform update
	return (Uni<ID>) sessionFactory.withTransaction((session, tx) -> session.createQuery(delete).executeUpdate());
    }

}
