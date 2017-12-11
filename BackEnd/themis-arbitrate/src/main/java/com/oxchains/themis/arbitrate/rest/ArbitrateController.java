package com.oxchains.themis.arbitrate.rest;
import com.google.common.net.HttpHeaders;
import com.oxchains.basicService.files.entity.FileInfos;
import com.oxchains.themis.arbitrate.common.Pojo;
import com.oxchains.themis.arbitrate.common.RegisterRequest;
import com.oxchains.themis.arbitrate.service.ArbitrateService;
import com.oxchains.themis.common.model.RestResp;
import com.oxchains.themis.common.util.JsonUtil;
import com.oxchains.themis.repo.entity.OrderArbitrate;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.util.Base64;
import java.util.List;

import static javax.servlet.http.HttpServletResponse.SC_NOT_FOUND;
/**
 * Created by huohuo on 2017/10/25.
 * @author huohuo
 */
@RestController
public class ArbitrateController {
    private final Logger LOG = LoggerFactory.getLogger(this.getClass());
    @Resource
    private ArbitrateService arbitrateService;

    /**
     * 仲裁人查看自己可以仲裁的订单列表 或者是仲裁完成的订单
     * */
    @RequestMapping("/arbitrate/findArbitrareOrderById")
    public RestResp findArbitrareOrderById(@RequestBody Pojo pojo){
        this.checkPage(pojo);
        return arbitrateService.findArbitrareOrderById(pojo);
    }

    /**
    * 仲裁者对订单进行仲裁 仲裁者仲裁将密匙碎片给胜利者
    * */
    @RequestMapping("/arbitrate/arbitrateOrderToUser")
    public RestResp arbitrateOrderToUser(@RequestBody Pojo pojo){
        return arbitrateService.arbitrateOrderToUser(pojo);
    }

    /**
     * 上传聊天记录和附件
     */
    @RequestMapping("/arbitrate/uploadEvidence")
    public RestResp uploadEvidence(@Valid @ModelAttribute RegisterRequest registerRequest, BindingResult bindingResult) throws IOException {
        if(bindingResult.hasErrors()){
            return RestResp.fail(bindingResult.getAllErrors().get(0).getDefaultMessage());
        }
        return  arbitrateService.uploadEvidence(registerRequest);
    }

    /**
    * 仲裁者获取 卖家买家上传的聊天记录和转账记录附件列表
    * */
    @RequestMapping("/arbitrate/getEvidence")
    public RestResp getEvidence(@RequestBody Pojo pojo){
        return arbitrateService.getEvidence(pojo);
    }

    /**
    * 仲裁者在仲裁页面下载双方上传的附件
    * */
    @RequestMapping("/arbitrate/{fileName}/downloadfile")
    public void downloadfile(@PathVariable String fileName, HttpServletRequest request, HttpServletResponse response){
        try {
            FileInfos infos = arbitrateService.getFile(fileName);
            if(infos!=null){
                String filename = infos.getFilename();
                byte[] buff = infos.getFile();
                InputStream input = new ByteArrayInputStream(buff);
                byte[] buffer = new byte[input.available()];
                input.read(buffer);
                input.close();
                String agent = request.getHeader("USER-AGENT");
                if(StringUtils.isEmpty(filename) || buff.length < 1){
                    filename = "error";
                    buff = "no such file".getBytes();
                }
                if(agent != null && agent.toLowerCase().indexOf("firefox") > 0) {
                    filename = "=?UTF-8?B?" + (Base64.getEncoder().encodeToString(filename.getBytes("UTF-8"))) + "?=";
                } else {
                    filename =  java.net.URLEncoder.encode(filename, "UTF-8");
                }
                response.setHeader(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + filename);
                response.setContentType(HttpURLConnection.guessContentTypeFromName(filename));
                response.setContentLengthLong(buff.length);
                response.getOutputStream().write(buff);
                response.getOutputStream().flush();
                response.getOutputStream().close();
            }
        } catch (IOException e) {
            LOG.error("downloadfile faild: {}.", e.getMessage(), e);
        }
    }
    private void checkPage(Pojo pojo){
        if(pojo.getPageSize() == null){
            pojo.setPageSize(8);
        }
        if(pojo.getPageNum() == null){
            pojo.setPageNum(1);
        }
    }
    private void fileNotFound(HttpServletResponse response) {
        try {
            response.setStatus(SC_NOT_FOUND);
            response.getWriter().write("file not found");
        } catch (Exception e) {
            LOG.error("fileNotFoud faild: {}.", e.getMessage(), e);
        }
    }
    @RequestMapping("/arbitrate/saveOrderAbritrate")
    public String saveOrderAbritrate(@RequestBody List<OrderArbitrate> orderArbitrate){
        return JsonUtil.toJson(arbitrateService.saveOrderAbritrate(orderArbitrate));
    }
}
