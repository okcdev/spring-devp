/*
 * FileName: SchemaService.java
 * Author: fan.hao fan.hao@eppdev.cn
 * Date: 2017-10-12
 */

package cn.eppdev.codegenerator.manager.service;

import cn.eppdev.codegenerator.builder.AppPropsBuildService;
import cn.eppdev.codegenerator.builder.ApplicationBuildService;
import cn.eppdev.codegenerator.builder.PomBuildService;
import cn.eppdev.codegenerator.manager.entity.EppdevTable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.sql.DataSource;
import java.io.IOException;
import java.util.*;

/**
 * @author fan.hao
 */
@Service
@Transactional(readOnly = false)
public class CodeGeneratorService {

    private static Logger logger = LoggerFactory.getLogger(CodeGeneratorService.class);


    @Autowired
    EppdevTableService tableService;

    @Autowired
    EppdevVersionService versionService;

    @Autowired
    BasicGeneratorService basicGeneratorService;

    @Autowired
    ApplicationBuildService applicationBuildService;

    @Autowired
    AppPropsBuildService appPropsBuildService;

    @Autowired
    PomBuildService pomBuildService;

    @Autowired
    EppdevConfService confService;



    public void generateSource(String versionId) throws IOException {
        List<EppdevTable> tableList = versionService.listTables(versionId).getList();
        for(EppdevTable table : tableList){
            if(table.getTableName().startsWith("_")){ // 下划线开通的配置表不生成
                continue;
            }
            logger.debug("Generate for table: {}", table);
            basicGeneratorService.createMapper(table.getId());
            basicGeneratorService.createEntity(table.getId());
            basicGeneratorService.create_Entity(table.getId());
            basicGeneratorService.createService(table.getId());
            basicGeneratorService.createDao(table.getId());
        }
    }

    public void generateSourceForTable(String tableId) throws IOException {
        EppdevTable table = tableService.get(tableId);
        if(null == table){
            throw new RuntimeException("表不存在");
        }
        basicGeneratorService.createMapper(table.getId());
        basicGeneratorService.createEntity(table.getId());
        basicGeneratorService.create_Entity(table.getId());
        basicGeneratorService.createService(table.getId());
        basicGeneratorService.createDao(table.getId());
    }



    public void init() throws IOException {
        EppdevTable table = new EppdevTable();
        applicationBuildService.build(confService.getBasicMap(), table);
        appPropsBuildService.build(confService.getBasicMap(), table);
        pomBuildService.build(confService.getBasicMap(), table);
    }
}
