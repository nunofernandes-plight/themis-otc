package com.oxchains.themis.chat.service;

import com.oxchains.themis.chat.entity.ChatContent;
import com.oxchains.themis.common.constant.message.MessageReadStatus;
import com.oxchains.themis.common.constant.message.MessageType;
import com.oxchains.themis.common.util.DateUtil;
import com.oxchains.themis.repo.dao.MessageRepo;
import com.oxchains.themis.repo.dao.MessageTextRepo;
import com.oxchains.themis.repo.entity.Message;
import com.oxchains.themis.repo.entity.MessageText;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * Created by xuqi on 2017/11/9.
 */
@Service
public class MessageService {
    @Resource
    private MessageRepo messageRepo;
    @Resource
    private MessageTextRepo messageTextRepo;

    public void postPriChatMessage(ChatContent chatContent){
        MessageText messageText = new MessageText(chatContent.getSenderId().longValue(),chatContent.getChatContent(), MessageType.PRIVATE_LETTET,null, DateUtil.getPresentDate(),chatContent.getOrderId());
        MessageText save = messageTextRepo.save(messageText);
        Message message = new Message(chatContent.getReceiverId().longValue(),save.getId(), MessageReadStatus.UN_READ,MessageType.PRIVATE_LETTET);
        Message save1 = messageRepo.save(message);
    }



}
