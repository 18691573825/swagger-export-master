package org.word.controller;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Date;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class TestController {
    /**
     * @param model
     * @return
     */

    @RequestMapping("/agreement/{tag}/{name}.html")
    public void getAgreement(@PathVariable(name = "tag") String tag, @PathVariable(name = "name") String name,
        HttpServletRequest request, HttpServletResponse httpResponse) {

        // https://cloudbroker-dev-test.obs.cn-north-4.myhuaweicloud.com:443/protocol/patch/华夏云网云市场用户协议.html

        System.out.println(tag);
        System.out.println(name);
        ServletOutputStream outputStream = null;

        String url =
            "https://cloudbroker-dev-test.obs.cn-north-4.myhuaweicloud.com:443/protocol/" + tag + "/" + name + ".html";

        InputStream decryptInputStream = getInputStream(url);
        try {
            httpResponse.reset();

            httpResponse.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");
            httpResponse.setHeader("Pragma", "no-cache");
            httpResponse.setHeader("Expires", "0");
            httpResponse.setHeader("Last-Modified", new Date().toString());
            httpResponse.setHeader("ETag", String.valueOf(System.currentTimeMillis()));
            httpResponse.setHeader("Content-Type", "text/html");

            outputStream = httpResponse.getOutputStream();
            byte[] cache = new byte[1024];
            int nRead = 0;
            while ((nRead = decryptInputStream.read(cache)) != -1) {
                outputStream.write(cache, 0, nRead);
                outputStream.flush();
            }
            outputStream.flush();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // 获取输入流
    public static InputStream getInputStream(String urlStr) {

        InputStream inputStream = null;
        HttpURLConnection httpURLConnection = null;
        try {
            URL url = new URL(urlStr);
            httpURLConnection = (HttpURLConnection)url.openConnection();
            httpURLConnection.setConnectTimeout(3000);
            httpURLConnection.setDoInput(true);
            httpURLConnection.setRequestMethod("GET");
            int responseCode = httpURLConnection.getResponseCode();
            if (responseCode == 200) {
                inputStream = httpURLConnection.getInputStream();
            }
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return inputStream;
    }
}
