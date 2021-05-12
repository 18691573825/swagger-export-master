package org.word.controller;

import java.io.BufferedOutputStream;
import java.io.IOException;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestTemplate;
import org.word.config.Modulel;
import org.word.config.ModulelList;
import org.word.service.WordService;

@Controller
public class WordController {

    private Integer count = 0;
    @Autowired
    private WordService tableService;

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private ModulelList modulelList;

    @Value("${server.port}")
    private Integer port;

    private static Map<String, Modulel> modulelMap = new HashMap<>();

    /**
     * @param model
     * @return
     */
    @RequestMapping("/getWord")
    public String getWord(Model model, @RequestParam(value = "tag", required = true) String tag) {
        count++;
        Modulel modulel = getModulel(tag);
        initModel(model, tag, modulel.getSwaggerUrl());
        return "word";
    }

    private Modulel getModulel(String tag) {
        if (modulelMap.isEmpty()) {
            List<Modulel> tmpList = modulelList.getList();
            for (Modulel m : tmpList) {
                modulelMap.put(m.getTag(), m);
            }
        }
        Modulel modulel = modulelMap.get(tag);
        return modulel;
    }

    private void initModel(Model model, String tag, String url) {
        Map<String, Object> result = tableService.tableList(url);
        model.addAttribute("tag", tag);
        model.addAttribute("download", 1);
        model.addAllAttributes(result);
    }

    /**
     * @Description: 获取当前时间戳
     * @return
     */
    public static String getCurrentTimeStr() {
        SimpleDateFormat df = new SimpleDateFormat("yyyyMMdd");
        return df.format(new Date());
    }

    /**
     * 导出word版
     *
     * @param response
     */
    @RequestMapping("/downloadWord")
    public void word(@RequestParam(value = "tag", required = true) String tag, HttpServletResponse response) {

        Modulel modulel = getModulel(tag);
        String wordName = modulel.getExportDocName() + "-" + getCurrentTimeStr() + ".doc";

        ResponseEntity<String> forEntity = restTemplate.getForEntity(
            "http://localhost:" + port + "/getWord?tag=" + modulel.getTag() + "&download=0", String.class);
        response.setContentType("application/octet-stream;charset=utf-8");
        response.setCharacterEncoding("utf-8");

        try (BufferedOutputStream bos = new BufferedOutputStream(response.getOutputStream())) {
            response.setHeader("Content-disposition", "attachment;filename=" + URLEncoder.encode(wordName, "utf-8"));
            byte[] bytes = forEntity.getBody().getBytes();
            bos.write(bytes, 0, bytes.length);
            bos.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
