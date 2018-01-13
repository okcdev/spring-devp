/*
 * FileName: TableService.java
 * Author: fan.hao fan.hao@eppdev.cn
 * Date: 2017-10-10
 */

package cn.eppdev.codegenerator.manager.service;

import cn.eppdev.codegenerator.manager.entity.EppdevColumn;
import cn.eppdev.codegenerator.manager.entity.EppdevIndex;
import cn.eppdev.codegenerator.manager.entity.EppdevTable;
import cn.eppdev.codegenerator.utils.name.NameUtils;
import cn.eppdev.commons.service.BasicService;
import cn.eppdev.utils.string.StringUtils;
import com.github.pagehelper.PageInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author: fan.hao
 */
@Service
@Transactional(readOnly = true)
public class EppdevTableService extends BasicService<EppdevTable> {

    private static Logger logger = LoggerFactory.getLogger(EppdevTableService.class);

    public static final String DEFAULT_VERSION_ID = "00000000000000000000000000000000";

    private static final int DEFAULT_TABLE_TYPE = 200;

    @Autowired
    EppdevColumnService columnService;

    @Autowired
    EppdevIndexService indexService;

    @Autowired
    CodeGeneratorService codeService;

    private String[] natureKeys = {"tableName"};

    @Override
    public String[] getNatureKeys() {
        return natureKeys;
    }


    @Transactional(readOnly = false)
    public int insert(EppdevTable entity) {
        logger.debug("entity: {}", entity);

        // 新增的时候进行必要的处理
        entity.setId(null);
        int result = super.save(entity);

        // 插入默认的字段
        columnService.save(EppdevColumnService.buildId(entity.getId()));
        columnService.save(EppdevColumnService.buildCreateBy(entity.getId()));
        columnService.save(EppdevColumnService.buildCreateDate(entity.getId()));
        columnService.save(EppdevColumnService.buildUpdateDate(entity.getId()));
        columnService.save(EppdevColumnService.buildDelFlag(entity.getId()));
        columnService.save(EppdevColumnService.buildUpdateBy(entity.getId()));
        columnService.save(EppdevColumnService.buildRemarks(entity.getId()));
        return result;
    }

    @Override
    @Transactional
    public void customeInit(EppdevTable entity) {
        // 生成实体名称
        if (null == entity.getEntityName() || entity.getEntityName().trim().length() == 0) {
            entity.setEntityName(NameUtils.buildEntityName(entity.getTableName()));
        }
        // 设置对应的版本ID
        if (null == entity.getVersionId() || entity.getVersionId().trim().length() == 0) {
            entity.setVersionId(DEFAULT_VERSION_ID);
        }
        // 设置默认的表类型
        if (null == entity.getTableType()) {
            entity.setTableType(DEFAULT_TABLE_TYPE);
        }

        // 设置默认的模块名
        if (null == entity.getModuleName()) {
            entity.setModuleName(StringUtils.removeBefore(entity.getTableName(), "_").split("_")[0]);
        }
        entity.setOriginTableId(entity.getId());

    }

    @Override
    public PageInfo<EppdevTable> listLike(EppdevTable paramEntity, Integer pageNum, Integer pageSize) {
        if (null == paramEntity.getVersionId()) {
            paramEntity.setVersionId(DEFAULT_VERSION_ID);
        }
        paramEntity.buildOrderBy("table_name");
        return super.listLike(paramEntity, pageNum, pageSize);
    }

    @Override
    public PageInfo<EppdevTable> listBy(EppdevTable paramEntity, Integer pageNum, Integer pageSize) {
        if (null == paramEntity.getVersionId()) {
            paramEntity.setVersionId(DEFAULT_VERSION_ID);
        }
        paramEntity.buildOrderBy("table_name");
        return super.listBy(paramEntity, pageNum, pageSize);
    }

    @Override
    public PageInfo<EppdevTable> listAll(Integer pageNum, Integer pageSize) {
        EppdevTable eppdevTable  = new EppdevTable();
        eppdevTable.buildOrderBy("table_name");
        return listBy(new EppdevTable(), pageNum, pageSize);
    }

    @Override
    @Transactional
    public EppdevTable get(String id) {
        // 获取表的基础信息
        EppdevTable eppdevTable = super.get(id);

        // 获取此表的索引
        EppdevIndex indexParm = new EppdevIndex();
        indexParm.setTableId(id);
        eppdevTable.setIndexList(indexService.listBy(indexParm, null, null).getList());

        // 获取此表的列信息
        EppdevColumn columnParam = new EppdevColumn();
        columnParam.setTableId(id);
        columnParam.buildOrderBy("sort_index asc", "create_date");
        eppdevTable.setColumnList(columnService.listBy(columnParam, null, null).getList());

        return eppdevTable;
    }
}
