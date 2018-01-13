/*
 * FileName: EppdevIndexService.java
 * Author: fan.hao fan.hao@eppdev.cn
 * Date: 2017-10-10
 */

package cn.eppdev.codegenerator.manager.service;

import cn.eppdev.codegenerator.manager.entity.EppdevIndex;
import cn.eppdev.commons.service.BasicService;
import com.github.pagehelper.PageInfo;
import org.springframework.stereotype.Service;

/**
 * @author: fan.hao
 */
@Service
public class EppdevIndexService extends BasicService<EppdevIndex> {

    private String[] natureKeys = {};

    @Override
    public String[] getNatureKeys() {
        return natureKeys;
    }

    @Override
    public boolean exists(EppdevIndex entity) {
        if(null != entity.getIndexName()){
            EppdevIndex param = new EppdevIndex();
            param.setTableId(entity.getTableId());
            param.setIndexName(entity.getIndexName());
            PageInfo<EppdevIndex> pageInfo = super.listBy(param, 1, 1);
            if(pageInfo.getTotal() > 0){
                return true;
            }
        }
        if(null != entity.getColumnNames()){
            EppdevIndex param = new EppdevIndex();
            param.setTableId(entity.getTableId());
            param.setColumnNames(entity.getColumnNames());
            PageInfo<EppdevIndex> pageInfo = super.listBy(param, 1, 1);
            if(pageInfo.getTotal() > 0){
                return true;
            }
        }
        return false;
    }

    @Override
    public void customeInit(EppdevIndex entity) {

    }
}
