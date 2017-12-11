package com.oxchains.themis.chat.repo;

import com.oxchains.themis.chat.entity.ChatContent;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * create by huohuo
 * @author huohuo
 */
@Repository
public interface MongoRepo extends MongoRepository<ChatContent, String> {
    List<ChatContent> findChatContentByChatIdAndOrderId(String chatId,String orderId);
}
