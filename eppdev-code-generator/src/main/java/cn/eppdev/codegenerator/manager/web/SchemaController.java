/*
 * FileName: SchemaController.java
 * Author: fan.hao fan.hao@eppdev.cn
 * Date: 2017-11-03
 */

package cn.eppdev.codegenerator.manager.web;

import cn.eppdev.codegenerator.manager.service.CodeGeneratorService;
import cn.eppdev.codegenerator.manager.service.SchemaService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;

/**
 * @author fan.hao
 */
@Controller
public class SchemaController {

    static Logger logger = LoggerFactory.getLogger(SchemaController.class);

    @Autowired
    CodeGeneratorService codeService;

    @Autowired
    SchemaService schemaService;

    @RequestMapping("/init")
    public String init(RedirectAttributes redirectAttributes) {
        try {
            codeService.init();
            redirectAttributes.addFlashAttribute("message", "加载成功");
        } catch (IOException e) {
            logger.error("Error: {}\n{}", e.getMessage(), e.getStackTrace());
            e.printStackTrace();
            redirectAttributes.addFlashAttribute("message", "出错：" + e.getMessage());
        }
        return "redirect:/table/";
    }


    @RequestMapping("/loadSchema")
    public String loadExistsSchema(RedirectAttributes redirectAttributes) {
        try {
            schemaService.updateTableInfo();
            redirectAttributes.addFlashAttribute("message", "加载成功");
        } catch (Exception e) {
            logger.error("Error: {}\n{}", e.getMessage(), e.getStackTrace());
            e.printStackTrace();
            redirectAttributes.addFlashAttribute("message", "出错：" + e.getMessage());
        }
        return "redirect:/table/";
    }

    @RequestMapping("/generateSource/{versionId}")
    public String generateSourceFiles(RedirectAttributes redirectAttributes,
                                      @PathVariable("versionId") String versionId) {
        try {
            codeService.generateSource(versionId);
            redirectAttributes.addFlashAttribute("message", "生成成功");
        } catch (Exception e) {
            logger.error("Error: {}\n{}", e.getMessage(), e.getStackTrace());
            e.printStackTrace();
            redirectAttributes.addFlashAttribute("message", "出错：" + e.getMessage());
        }
        return "redirect:/table/";
    }

    @RequestMapping("/generateSource/table/{tableId}")
    public String generateSourceFileForTable(RedirectAttributes redirectAttributes,
                                             @PathVariable("tableId") String tableId){
        try {
            codeService.generateSourceForTable(tableId);
            redirectAttributes.addFlashAttribute("message", "生成成功");
        } catch (Exception e) {
            logger.error("Error: {}\n{}", e.getMessage(), e.getStackTrace());
            e.printStackTrace();
            redirectAttributes.addFlashAttribute("message", "出错：" + e.getMessage());
        }
        return "redirect:/table/";
    }
}
