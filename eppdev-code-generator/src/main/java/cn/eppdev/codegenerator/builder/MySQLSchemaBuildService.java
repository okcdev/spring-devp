/*
 * FileName: SQLCreateBuildService.java
 * Author: fan.hao fan.hao@eppdev.cn
 * Date: 2017-11-09
 */

package cn.eppdev.codegenerator.builder;

import cn.eppdev.codegenerator.manager.entity.EppdevColumn;
import cn.eppdev.codegenerator.manager.entity.EppdevIndex;
import cn.eppdev.codegenerator.manager.entity.EppdevTable;
import cn.eppdev.codegenerator.utils.conf.TypesMapperUtils;
import cn.eppdev.codegenerator.utils.file.TemplateFileUtils;
import cn.eppdev.utils.template.Template;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

/**
 * @author fan.hao
 */
@Service
public class MySQLSchemaBuildService extends BasicBuildService {

    private static Template columnTemplate = null;

    private static Template indexTemplate = null;


    public MySQLSchemaBuildService() {
        super();
        String templateContent = null;
        try {
            templateContent = TemplateFileUtils.read("/eppdev-code-generator/templates/sql/create/column.template");
            columnTemplate = new Template(new ByteArrayInputStream(templateContent.getBytes("UTF-8")));
            templateContent = TemplateFileUtils.read("/eppdev-code-generator/templates/sql/create/index.template");
            indexTemplate = new Template(new ByteArrayInputStream(templateContent.getBytes("UTF-8")));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected Template loadTemplate() throws UnsupportedEncodingException {
        if (this.template == null) {
            String templateContent = TemplateFileUtils.read("/eppdev-code-generator/templates/sql/create/schema.template");
            template = new Template(new ByteArrayInputStream(templateContent.getBytes("UTF-8")));
        }
        return template;
    }

    @Override
    public Map<String, String> buildFieldsMap(EppdevTable eppdevTable, String basicPackageName) throws UnsupportedEncodingException {
        Map<String, String> map = new HashMap<>();
        map.put("COLUMNS", buildColumns(eppdevTable));
        map.put("INDEX", buildIndex(eppdevTable));
        return map;
    }

    public String buildColumns(EppdevTable table) {
        StringBuilder sb = new StringBuilder();
        for (EppdevColumn column : table.getColumnList()) {
            Map<String, String> map = buildFieldsMap(column);
            if (TypesMapperUtils.needLength(column.getColumnType())) {
                map.put("COLUMN_DB_TYPE", TypesMapperUtils.getMysqlType(column.getColumnType()) + "(" + column.getColumnLength() + ")");
            } else {
                map.put("COLUMN_DB_TYPE", TypesMapperUtils.getMysqlType(column.getColumnType()));
            }
            columnTemplate.replace(map);
            columnTemplate.replace(buildTableMap(table, null));
            sb.append(columnTemplate.toString());
        }
        return sb.toString();
    }

    private String buildIndex(EppdevTable table) {
        StringBuilder sb = new StringBuilder();
        for (EppdevIndex index : table.getIndexList()) {
            if (!index.getColumnNames().toUpperCase().equals("ID")) {
                Map<String, String> map = new HashMap<>();
                map.put("INDEX_NAME", index.getIndexName());
                map.put("INDEX_COLUMN_NAMES", index.getColumnNames());
                indexTemplate.replace(map);
                indexTemplate.replace(buildTableMap(table, null));
                sb.append(indexTemplate.toString());
            }
        }
        return sb.toString();
    }

    @Override
    public String buildPackageName(String basicPackageName, EppdevTable table) {
        return table.getVersion().getVersionName() + "/schema";
    }

    @Override
    public String getSourceDir() {
        return "sql";
    }

    @Override
    public String getFileName(EppdevTable eppdevTable) {
        return eppdevTable.getTableName() + ".sql";
    }

    @Override
    public boolean needUpdate() {
        return true;
    }

    @Override
    public String update(String originContent, EppdevTable eppdevTable, String basicPackageName) throws IOException {
        Template template = loadTemplate();
        template.replace(buildTableMap(eppdevTable, basicPackageName), buildFieldsMap(eppdevTable, basicPackageName));
        return template.toString();
    }
}
