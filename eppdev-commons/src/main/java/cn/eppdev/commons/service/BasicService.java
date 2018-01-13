/*
 * FileName: BasicService.java
 * Author: fan.hao fan.hao@eppdev.cn
 * Date: 2017-9-20
 */

package cn.eppdev.commons.service;

import cn.eppdev.commons.dao.BasicDao;
import cn.eppdev.commons.entity.BasicEntity;
import cn.eppdev.utils.uuid.UUIDUtils;
import com.github.pagehelper.PageInfo;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.beanutils.ConvertUtils;
import org.apache.commons.beanutils.converters.DateConverter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 基础Service类，提供了一些基础的方法
 *
 * @author fan.hao
 */
@Transactional(readOnly = true)
public abstract class BasicService<T extends BasicEntity> {

    private static Logger logger = LoggerFactory.getLogger(BasicService.class);

    public static final Integer DEL_FLAG_NORMAL = 0;

    static {
        ConvertUtils.register(new DateConverter(null), java.util.Date.class);
    }

    Class<T> cls;

    @Autowired
    BasicDao<T> dao;

    /**
     * 获取一条数据
     *
     * @param id 数据ID
     * @return 单条数据
     */
    public T get(String id) {
        return dao.get(id);
    }

    /**
     * 删除一条数据
     *
     * @param id 要删除的数据ID
     * @return 实际删除的数据条数
     */
    @Transactional(readOnly = false)
    public int delete(String id) {
        return dao.delete(id);
    }

    /**
     * 保存数据，若存在ID，则执行更新操作，若不存在，则执行插入操作
     *
     * @param entity 要保存的数据对象
     * @return 保存的数据条数
     */
    @Transactional(readOnly = false)
    public int save(T entity) {
        logger.debug("entity: {}", entity);
        if (!exists(entity)) {
            if (null == entity.getId() || entity.getId().trim().length() == 0) { // new entity，do insert
                _init(entity);
                return dao.insert(entity);
            } else {
                entity.setUpdateDate(new Date());
                return dao.update(entity);
            }
        } else {
            return 0;
        }
    }

    /**
     * 保存数据，若存在ID，则执行更新操作，若不存在，则执行插入操作
     *
     * @param map 要保存的数据对象
     * @return 保存的数据条数
     */
    @Transactional(readOnly = false)
    public int save(Map<String, Object> map) {
        return save(mapToBean(map));
    }


    public boolean exists(T entity) {
        String[] natureKeys = getNatureKeys();
        if (natureKeys == null || natureKeys.length == 0) {
            return false;
        }
        try {
            T t = getEntityClass().newInstance();
            for (String key : natureKeys) {
                Object value = BeanUtils.getProperty(entity, key);
                BeanUtils.copyProperty(t, key, value);
            }
            t.set_pageNum(1);
            t.set_pageSize(2);
            List<T> list = dao.listBy(t);
            if (list.size() == 0) {
                return false;
            }
            if (list.get(0).getId().equals(entity.getId())) {
                return false;
            }
            return true;
        } catch (InstantiationException e) {
            logger.error("Error: {}\n{}", e.getMessage(), e.getStackTrace());
            e.printStackTrace();
            throw new RuntimeException(e);
        } catch (IllegalAccessException e) {
            logger.error("Error: {}\n{}", e.getMessage(), e.getStackTrace());
            e.printStackTrace();
            throw new RuntimeException(e);
        } catch (NoSuchMethodException e) {
            logger.error("Error: {}\n{}", e.getMessage(), e.getStackTrace());
            e.printStackTrace();
            throw new RuntimeException(e);
        } catch (InvocationTargetException e) {
            logger.error("Error: {}\n{}", e.getMessage(), e.getStackTrace());
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    public abstract String[] getNatureKeys();

    public void _init(T entity) {
        Date date = new Date();
        entity.setId(UUIDUtils.getUUID());
        entity.setCreateDate(date);
        entity.setUpdateDate(date);
        entity.setDelFlag(DEL_FLAG_NORMAL);
        customeInit(entity);
    }

    public abstract void customeInit(T entity);

    public PageInfo<T> listAll() {
        return new PageInfo<>(dao.listAll(null));
    }

    /**
     * 获取分页列表
     *
     * @param pageNum
     * @param pageSize
     * @return
     */
    public PageInfo<T> listAll(Integer pageNum, Integer pageSize) {
        BasicEntity entity = new BasicEntity();
        entity.set_pageNum(pageNum);
        entity.set_pageSize(pageSize);
        return new PageInfo<>(dao.listAll(entity));
    }

    /**
     * 获取分页列表
     *
     * @param paramEntity 参数值
     * @param pageNum     页码
     * @param pageSize    每页数据条数
     * @return
     */
    public PageInfo<T> listBy(T paramEntity, Integer pageNum, Integer pageSize) {
        if (null != pageNum && null != pageSize) {
            paramEntity.set_pageNum(pageNum);
            paramEntity.set_pageSize(pageSize);
        }
        return new PageInfo<>(dao.listBy(paramEntity));
    }

    /**
     * 获取分页列表
     *
     * @param paramMap 参数
     * @param pageNum  页码
     * @param pageSize 每页数据条数
     * @return
     */
    public PageInfo<T> listBy(Map<String, Object> paramMap, Integer pageNum, Integer pageSize) {
        return listBy(mapToBean(paramMap), pageNum, pageSize);
    }

    /**
     * 获取分页列表
     *
     * @param paramEntity
     * @return
     */
    public PageInfo<T> listLike(T paramEntity, Integer pageNum, Integer pageSize) {
        if (null != pageNum && null != pageSize) {
            paramEntity.set_pageNum(pageNum);
            paramEntity.set_pageSize(pageSize);
        }
        return new PageInfo<>(dao.listLike(paramEntity));
    }

    /**
     * 获取分页列表
     *
     * @param paramMap 参数
     * @param pageNum  页码
     * @param pageSize 每页数据条数
     * @return
     */
    public PageInfo<T> listLike(Map<String, Object> paramMap, Integer pageNum, Integer pageSize) {
        return listLike(mapToBean(paramMap), pageNum, pageSize);
    }

    /**
     * 获取分页列表
     *
     * @param paramEntity
     * @return
     */
    public PageInfo<T> listLeftLike(T paramEntity, Integer pageNum, Integer pageSize) {
        if (null != pageNum && null != pageSize) {
            paramEntity.set_pageNum(pageNum);
            paramEntity.set_pageSize(pageSize);
        }
        return new PageInfo<>(dao.listLeftLike(paramEntity));
    }

    /**
     * 获取分页列表
     *
     * @param paramMap 参数
     * @param pageNum  页码
     * @param pageSize 每页数据条数
     * @return
     */
    public PageInfo<T> listLeftLike(Map<String, Object> paramMap, Integer pageNum, Integer pageSize) {
        return listLeftLike(mapToBean(paramMap), pageNum, pageSize);
    }

    /**
     * 获取分页列表
     *
     * @param paramEntity
     * @return
     */
    public PageInfo<T> listRawLike(T paramEntity, Integer pageNum, Integer pageSize) {
        if (null != pageNum && null != pageSize) {
            paramEntity.set_pageNum(pageNum);
            paramEntity.set_pageSize(pageSize);
        }
        return new PageInfo<>(dao.listRawLike(paramEntity));
    }

    /**
     * 获取分页列表
     *
     * @param paramMap 参数
     * @param pageNum  页码
     * @param pageSize 每页数据条数
     * @return
     */
    public PageInfo<T> listRawLike(Map<String, Object> paramMap, Integer pageNum, Integer pageSize) {
        return listRawLike(mapToBean(paramMap), pageNum, pageSize);
    }

    /**
     * 获取实体类对应的class
     *
     * @return
     */
    private Class<T> getEntityClass() {
        if (cls == null) {
            Type genType = getClass().getGenericSuperclass();
            Type[] params = ((ParameterizedType) genType).getActualTypeArguments();
            cls = (Class) params[0];
            logger.debug("cls: {}", cls);
        }
        return cls;
    }

    private T mapToBean(Map<String, Object> map) {
        T t = null;
        try {
            t = getEntityClass().newInstance();
            BeanUtils.populate(t, map);
        } catch (InvocationTargetException e) {
            logger.error("Error: {}\n{}", e.getMessage(), e.getStackTrace());
            e.printStackTrace();
            throw new RuntimeException(e);
        } catch (IllegalAccessException e) {
            logger.error("Error: {}\n{}", e.getMessage(), e.getStackTrace());
            e.printStackTrace();
            throw new RuntimeException(e);
        } catch (InstantiationException e) {
            logger.error("Error: {}\n{}", e.getMessage(), e.getStackTrace());
            e.printStackTrace();
            throw new RuntimeException(e);
        }
        return t;
    }

}
