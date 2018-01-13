/*
 * FileName: SqlController.java
 * Author: fan.hao fan.hao@eppdev.cn
 * Date: 2017-11-09
 */

package cn.eppdev.codegenerator.manager.web;

import cn.eppdev.codegenerator.manager.service.SqlGeneratorService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/**
 * @author fan.hao
 */
@Controller
@RequestMapping("/sql")
public class SqlController {

    static Logger logger = LoggerFactory.getLogger(SqlController.class);

    @Autowired
    SqlGeneratorService sqlGeneratorService;

    @RequestMapping("/version/{id}")
    public String createSchema(RedirectAttributes redirectAttributes,
                               @PathVariable("id") String id) {
        try {
            sqlGeneratorService.generateCreateSqlByVersion(id);
            redirectAttributes.addFlashAttribute("message", "创建成功");
        } catch (Exception e) {
            logger.error("Error: {}\n{}", e.getMessage(), e.getStackTrace());
            e.printStackTrace();
            redirectAttributes.addFlashAttribute("message", "出错：" + e.getMessage());
        }
        return "redirect:/version/" + id;
    }
}
