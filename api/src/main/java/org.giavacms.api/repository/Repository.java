package org.giavacms.api.repository;

import java.lang.Class;import java.lang.Exception;import java.lang.Object;import java.lang.String;import java.util.List;

import org.giavacms.api.model.Search;

/**
 * @param <T>
 */
public interface Repository<T> {

    /**
     * @param search
     * @param startRow
     * @param pageSize
     * @return
     */
    public List<T> getList(Search<T> search, int startRow, int pageSize)
            throws Exception;

    /**
     * @param search
     * @return
     */
    public int getListSize(Search<T> search) throws Exception;

    /**
     * Find by primary key
     *
     * @param key
     * @return
     */
    public T find(Object key) throws Exception;

    /**
     * Fetch by primary key
     *
     * @param key
     * @return
     */
    public T fetch(Object key) throws Exception;

    /**
     * create
     *
     * @param domainClass
     * @return
     */
    public T create(Class<T> domainClass) throws Exception;

    /**
     * Make an instance persistent.
     * <p/>
     *
     * @param object
     * @return
     */
    public T persist(T object) throws Exception;

    /**
     * @param object
     * @return
     */
    public T update(T object) throws Exception;

    /**
     * @param key
     * @return
     */
    public void delete(Object key) throws Exception;

    /**
     * @param key
     * @return boolean
     */
    public boolean exist(Object key) throws Exception;

    /**
     * @param key
     * @return deserialization of key to actual class of the key field
     */
    public Object castId(String key) throws Exception;

}
