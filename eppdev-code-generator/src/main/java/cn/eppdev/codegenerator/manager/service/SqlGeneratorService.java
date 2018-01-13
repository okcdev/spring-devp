/*
 * FileName: SqlGeneratorService.java
 * Author: fan.hao fan.hao@eppdev.cn
 * Date: 2017-11-09
 */

package cn.eppdev.codegenerator.manager.service;

import cn.eppdev.codegenerator.manager.entity.EppdevTable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

/**
 * @author fan.hao
 */
@Service
public class SqlGeneratorService {
    @Autowired
    EppdevTableService tableService;

    @Autowired
    EppdevVersionService versionService;

    @Autowired
    BasicGeneratorService basicGeneratorService;

    public void generateCreateSqlByVersion(String versionId) throws IOException, SQLException {
        List<EppdevTable> tableList = versionService.listTables(versionId).getList();
        for(EppdevTable eppdevTable: tableList){
            if(!eppdevTable.getTableName().startsWith("_")) {
                basicGeneratorService.createSqlCreate(eppdevTable.getId());
            }
        }
    }

}
