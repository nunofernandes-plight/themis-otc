package com.oxchains.themis.message.rest;

import com.oxchains.themis.common.model.RestResp;
import com.oxchains.themis.message.service.MessageService;
import com.oxchains.themis.repo.entity.MessageText;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * @author luoxuri
 * @create 2017-11-06 15:01
 **/
@RestController
@RequestMapping(value = "/message")
public class MessageController {

    @Resource
    private MessageService messageService;

    /**
     * Admin发送系统消息
     */
    @PostMapping(value = "/send/noticeMsg")
    public RestResp sendGlobalMessage(@RequestBody MessageText messageText){
        return messageService.sendNoticeMessage(messageText);

    }

    /**
     * 显示系统信息
     */
    @GetMapping(value = "/query/globalMsg")
    public RestResp queryGlobalMsg(@RequestParam Long userId, @RequestParam Integer pageNum, @RequestParam Integer pageSize){
        return messageService.queryGlobalMsg(userId, pageNum, pageSize);
    }

    /**
     * 显示私信
     */
    @GetMapping(value = "/query/privateMsg")
    public RestResp queryPrivateMsg(@RequestParam Long userId, @RequestParam Integer pageNum, @RequestParam Integer pageSize){
        return messageService.queryPrivateMsg(userId, pageNum, pageSize);
    }

    /**
     * 显示公告信息
     */
    @GetMapping(value = "/query/noticeMsg")
    public RestResp queryNoticeMsg(@RequestParam Long userId, @RequestParam Integer pageNum, @RequestParam Integer pageSize){
        return messageService.queryNoticeMsg(userId, pageNum, pageSize);
    }

    /**
     * 所有未读信息数量
     */
    @GetMapping(value = "/query/unReadCount")
    public RestResp unReadCount(@RequestParam Long userId, @RequestParam Integer tip){
        return messageService.unReadCount(userId, tip);
    }

}
