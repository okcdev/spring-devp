/*
 * FileName: BasicDao.java
 * Author: fan.hao fan.hao@eppdev.cn
 * Date: 17-9-20
 */

package cn.eppdev.commons.dao;

import cn.eppdev.commons.entity.BasicEntity;

import java.util.List;

/**
 * MYBATIS DAO的基础接口类
 * @author fan.hao
 */
public interface BasicDao<T extends BasicEntity> {

    /**
     * 插入数据
     * @param entity 要插入的数据
     * @return 插入数据条数
     */
    public int insert(T entity);

    /**
     * 删除数据
     * @param id 数据的ID.
     * @return 删除的数据条数
     */
    public int delete(String id);


    /**
     * 更新数据
     * @param Entity 要更新的数据
     * @return 更新成功的数据条数
     */
    public int update(T Entity);

    /**
     * 获取数据
     * @param id 数据ID
     * @return 数据对象
     */
    public T get(String id);

    /**
     * 查询所有数据
     * @param entity 参数对象，只需有pageSize和pageNum两个属性即可
     * @return 结果列表
     */
    public List<T> listAll(BasicEntity entity);

    /**
     * 查询数据，其中对于字符串参数采用“等于”条件
     * @param entity 参数对象
     * @return 返回的结果列表
     */
    public List<T> listBy(T entity);


    /**
     * 查询数据，其中对于字符串采用“like”条件，前后自动添加“%”
     * @param entity 参数对象
     * @return 返回的结果列表
     */
    public List<T> listLike(T entity);

    /**
     * 查询数据，其中对于字符串采用“like”条件，但是只在右边拼接“%”符号(使索引可以起作用)
     * @param entity 参数对象
     * @return 返回的结果列表
     */
    public List<T> listLeftLike(T entity);


    /**
     * 查询数据，其中对于字符串采用“like”条件，不自动添加“%”，满足查询的需要
     * @param entity 参数对象
     * @return 返回的结果列表
     */
    public List<T> listRawLike(T entity);


    /**
     * 使用PageHelper后已经不需要使用这个方法。<br />
     * 只需要调用listALL，并将其返回值转换为PageInfo对象即可通过getPages()方法获取总数量
     * @return 本表所有数据条数
     */
    @Deprecated
    public int countAll();


    /**
     * <b>Deprecated: 使用PageHelper后请使用#listBy方法代替</b><br />
     * 根据条件查询数据条数，其中对于字符串参数采用“等于”条件。
     * @param entity 参数对象
     * @return 满足条件的数据条数
     */
    @Deprecated
    public int countBy(T entity);


    /**
     * <b>Deprecated: 使用PageHelper后请使用#listLike方法代替</b><br />
     * 根据条件查询数据条数，其中对于字符串参数采用“Like”条件，前后自动拼接“%”。
     * @param entity 参数对象
     * @return 满足条件的数据条数
     */
    @Deprecated
    public int countLike(T entity);

    /**
     * <b>Deprecated: 使用PageHelper后请使用#listLeftLike方法代替</b><br />
     * 根据条件查询数据条数，其中对于字符串参数采用“Like”条件，后面自动拼接“%”。
     * @param entity 参数对象
     * @return 满足条件的数据条数
     */
    @Deprecated
    public int countLeftLike(T entity);
}
