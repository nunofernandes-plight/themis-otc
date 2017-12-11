package com.oxchains.themis.chat.service;

import com.alibaba.fastjson.JSONObject;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.oxchains.themis.chat.entity.ChatContent;
import com.oxchains.themis.chat.entity.UploadTxIdPojo;
import com.oxchains.themis.chat.repo.MongoRepo;
import com.oxchains.themis.chat.websocket.ChannelHandler;
import com.oxchains.themis.common.constant.ThemisUserAddress;
import com.oxchains.themis.common.model.RestResp;
import com.oxchains.themis.common.util.JsonUtil;
import com.oxchains.themis.repo.entity.User;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.stereotype.Service;
import com.oxchains.themis.chat.websocket.ChatUtil;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;
import java.util.List;

/**
 * create by huohuo
 * @author huohuo
 */
@Service
public class ChatService {
    @Resource
    private MongoRepo mongoRepo;
    @Resource
    RestTemplate restTemplate;
    @Resource
    HashOperations hashOperations;
    @Value("${themis.user.redisInfo.hk}")
    private String userHK;
    private final Logger LOG = LoggerFactory.getLogger(this.getClass());
    public List<ChatContent> getChatHistroy(ChatContent chatContent){
        try{
            LOG.info("get chat history senderId ："+chatContent.getSenderId()+" reciverId :"+chatContent.getReceiverId()+" orderId: "+chatContent.getOrderId());
            String keyIDs = ChatUtil.getIDS(chatContent.getSenderId().toString(),chatContent.getReceiverId().toString());
            List<ChatContent> list = mongoRepo.findChatContentByChatIdAndOrderId(keyIDs,chatContent.getOrderId());
            for (ChatContent content:list) {
                if(content.getSenderId().longValue()==chatContent.getSenderId().longValue())
                {
                    content.setSenderName(this.getLoginNameByUserId(chatContent.getSenderId().longValue()));
                }
                else{content.setSenderName(this.getLoginNameByUserId(chatContent.getReceiverId().longValue()));}
            }
            return list;
        }
        catch (Exception e){
            LOG.error("faild get chat history : {}",e);
        }
        return null;
    }
    //从用户中心 根据用户id获取用户信息
    //从用户中心 根据用户id获取用户信息
    @HystrixCommand(fallbackMethod ="getUserByIdError" )
    public User getUserById(Long userId){

        try {
            String userInfo = (String) hashOperations.get(userHK, userId.toString());
            if(StringUtils.isNotBlank(userInfo)){
                return JsonUtil.jsonToEntity(userInfo,User.class);
            }
            String str = restTemplate.getForObject(ThemisUserAddress.GET_USER+userId, String.class);
            if(null != str){
                RestResp restResp = JsonUtil.jsonToEntity(str, RestResp.class);
                if(null != restResp && restResp.status == 1){
                    hashOperations.put(userHK,userId.toString(),JsonUtil.toJson(restResp.data));
                    return JsonUtil.objectToEntity(restResp.data,User.class);
                }
            }
        } catch (Exception e) {
            LOG.error("get user by id from themis-user faild : {}",e);
            throw  e;
        }
        return null;
    }
    public User getUserByIdError(Long userId){
        return null;
    }
    private String getLoginNameByUserId(Long userId){
        User userById = this.getUserById(userId);
        return userById != null?userById.getLoginname():null;
    }
    public boolean uploadTxInform(UploadTxIdPojo pojo){
        try {
            ChannelHandler channelHandler = ChatUtil.txChannels.get(pojo.getId());
            if(channelHandler != null){
                LOG.info("连接存在 :"+pojo.getId());
                String message = JsonUtil.toJson(pojo);
                channelHandler.getChannel().writeAndFlush(new TextWebSocketFrame(message));
                return true;
            }
        } catch (Exception e) {
            LOG.error("upload tx inform faild",e.getMessage());
        }
        return false;
    }
}
