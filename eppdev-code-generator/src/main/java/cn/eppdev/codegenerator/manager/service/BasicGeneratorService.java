/*
 * FileName: GeneratorService.java
 * Author: fan.hao fan.hao@eppdev.cn
 * Date: 2017-11-01
 */

package cn.eppdev.codegenerator.manager.service;

import cn.eppdev.codegenerator.builder.*;
import cn.eppdev.codegenerator.manager.entity.EppdevTable;
import cn.eppdev.codegenerator.utils.file.SourceFileUtils;
import cn.eppdev.codegenerator.utils.file.TemplateFileUtils;
import cn.eppdev.codegenerator.utils.name.NameUtils;
import cn.eppdev.utils.template.Template;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import javax.sql.DataSource;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.sql.SQLException;

/**
 * @author fan.hao
 */
@Service
public class BasicGeneratorService {

    @Autowired
    DataSource dataSource;

    @Autowired
    EppdevTableService tableService;

    @Autowired
    EppdevConfService confService;

    @Autowired
    DaoBuildService daoBuildService;

    @Autowired
    EntityBuildService entityBuildService;

    @Autowired
    ServiceBuildService serviceBuildService;

    @Autowired
    _EntityBuildService _entityBuildService;

    @Autowired
    MapperBuildService mapperBuildService;

    @Autowired
    MySQLSchemaBuildService mySQLSchemaBuildService;

    /**
     * 创建DAO文件
     *
     * @param tableId
     * @throws IOException
     */
    public void createDao(String tableId) throws IOException {
        EppdevTable table = tableService.get(tableId);
        daoBuildService.build(confService.getBasicMap(), table);
    }

    /**
     * 创建Entity文件
     *
     * @param tableId
     * @throws IOException
     */
    public void createEntity(String tableId) throws IOException {
        EppdevTable table = tableService.get(tableId);
        entityBuildService.build(confService.getBasicMap(), table);
    }


    /**
     * 创建_Entity文件
     * @param tableId
     * @throws IOException
     */
    public void create_Entity(String tableId) throws IOException {
        EppdevTable table = tableService.get(tableId);
        _entityBuildService.build(confService.getBasicMap(), table);
    }

    /**
     * 创建Service文件
     * @param tableId
     * @throws IOException
     */
    public void createService(String tableId) throws IOException {
        EppdevTable table = tableService.get(tableId);
        serviceBuildService.build(confService.getBasicMap(), table);
    }

    /**
     * 创建Service文件
     * @param tableId
     * @throws IOException
     */
    public void createMapper(String tableId) throws IOException {
        EppdevTable table = tableService.get(tableId);
        mapperBuildService.build(confService.getBasicMap(), table);
    }

    public void createSqlCreate(String tableId) throws IOException, SQLException {
        EppdevTable eppdevTable = tableService.get(tableId);

        String driverName = dataSource.getConnection().getMetaData().getDriverName();
        if(driverName.toLowerCase().contains("mysql")) {
            mySQLSchemaBuildService.build(confService.getBasicMap(), eppdevTable);
        }
    }
}
