package com.oxchains.themis.arbitrate.service;
import com.alibaba.fastjson.JSONObject;
import com.oxchains.themis.arbitrate.common.MessageCopywrit;
import com.oxchains.themis.arbitrate.common.ParamType;
import com.oxchains.themis.arbitrate.repo.OrderArbitrateRepo;
import com.oxchains.themis.common.constant.ThemisUserAddress;
import com.oxchains.themis.common.constant.message.MessageReadStatus;
import com.oxchains.themis.common.constant.message.MessageType;
import com.oxchains.themis.common.util.DateUtil;
import com.oxchains.themis.common.util.JsonUtil;
import com.oxchains.themis.repo.dao.MessageRepo;
import com.oxchains.themis.repo.dao.MessageTextRepo;
import com.oxchains.themis.repo.entity.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;
import java.text.MessageFormat;
import java.util.List;

@Service
public class MessageService {
    private final Logger LOG = LoggerFactory.getLogger(this.getClass());
    @Resource
    private RestTemplate restTemplate;
    @Resource
    private MessageRepo messageRepo;
    @Resource
    private MessageTextRepo messageTextRepo;
    @Resource
    private OrderArbitrateRepo orderArbitrateRepo;
    public static final Integer BUYER_SUCCESS = 1;
    //仲裁投票后给双方的站内信
    public void postArbitrateMessage(Orders orders,Long userId,Integer successId){
        try {
            String username = this.getUserById(userId).getLoginname();
            String successContent = MessageFormat.format(MessageCopywrit.ARBITRATE_SUCCESS,orders.getId(),username);
            MessageText successMessageText = new MessageText(0L,successContent,MessageType.GLOBAL,0L,DateUtil.getPresentDate(),orders.getId());
            MessageText successSave = messageTextRepo.save(successMessageText);
            Message message1 = new Message(successId.intValue() ==  BUYER_SUCCESS?orders.getBuyerId():orders.getSellerId(),successSave.getId(),MessageReadStatus.UN_READ,MessageType.GLOBAL);
            messageRepo.save(message1);

            String faildContent = MessageFormat.format(MessageCopywrit.ARBITRATE_FAILD,orders.getId(),username);
            MessageText messageText2 = new MessageText(0L,faildContent,MessageType.GLOBAL,0L,DateUtil.getPresentDate(),orders.getId());
            MessageText faildSave = messageTextRepo.save(messageText2);
            Message message2 = new Message(successId.intValue() ==  BUYER_SUCCESS?orders.getSellerId():orders.getBuyerId(),faildSave.getId(),MessageReadStatus.UN_READ,MessageType.GLOBAL);
            messageRepo.save(message2);
        } catch (Exception e) {
            LOG.error("MESSAGE -- post a arbitrateUser arbitrate finish message : {}",e);
        }
    }
    //仲裁完成后给双方的站内信
    public void postArbitrateFinish(Orders orders){
        try {
            Long successId = null;
            Long falidId = null;
            if(orders.getOrderStatus().longValue() == ParamType.OrderStatus.CANCEL.getStatus()){
                successId = orders.getSellerId();
                falidId = orders.getBuyerId();
            }
            if(orders.getOrderStatus().longValue() == ParamType.OrderStatus.FINISH.getStatus()){
                successId = orders.getBuyerId();
                falidId = orders.getSellerId();
            }
            //胜利方的站内信
            String successContent = MessageFormat.format(MessageCopywrit.ARBITRATE_FINISH_SUCCESS,orders.getId());
            MessageText successMessageText = new MessageText(0L,successContent,MessageType.GLOBAL,0L,DateUtil.getPresentDate(),orders.getId());
            successMessageText = messageTextRepo.save(successMessageText);
            Message message = new Message(successId,successMessageText.getId(),MessageReadStatus.UN_READ,MessageType.GLOBAL);
            messageRepo.save(message);

            //失败方的站内信
            String faildContent = MessageFormat.format(MessageCopywrit.ARBITRATE_FINISH_FAILD,orders.getId());
            MessageText messageText2 = new MessageText(0L,faildContent,MessageType.GLOBAL,0L,DateUtil.getPresentDate(),orders.getId());
            messageText2 = messageTextRepo.save(messageText2);
            Message message2 = new Message(falidId,messageText2.getId(),MessageReadStatus.UN_READ,MessageType.GLOBAL);
            messageRepo.save(message2);
        } catch (Exception e) {
            LOG.error("MESSAGE -- post this order arbitrate finish message : {}",e);
        }
    }
    //发起仲裁时的给三方的站内信
    public void postEvidenceMessage(Orders orders, Long userId){
        try {
            //发起提起仲裁的人的站内信
            String messageContent1 = MessageFormat.format(MessageCopywrit.GENERATE_ABRITRATE,orders.getId());
            MessageText messageText1 = new MessageText(0L,messageContent1,MessageType.GLOBAL,0L,DateUtil.getPresentDate(),orders.getId());
            MessageText save1 = messageTextRepo.save(messageText1);
            Message message1 = new Message(userId,save1.getId(),MessageReadStatus.UN_READ,MessageType.GLOBAL);
            messageRepo.save(message1);

            //被仲裁人的站内信
            String messageContent2 = MessageFormat.format(MessageCopywrit.BY_GENERATE_ABRITRATE,orders.getId());
            MessageText messageText2 = new MessageText(0L,messageContent2,MessageType.GLOBAL,0L,DateUtil.getPresentDate(),orders.getId());
            messageText2 = messageTextRepo.save(messageText2);
            Message message2 = new Message(orders.getSellerId().longValue() == userId?orders.getBuyerId():orders.getSellerId(),messageText2.getId(),MessageReadStatus.UN_READ,MessageType.GLOBAL);
            messageRepo.save(message2);

            String username = this.getUserById(userId).getLoginname();
            //通知仲裁人的站内信
            List<OrderArbitrate> list = orderArbitrateRepo.findByOrderId(orders.getId());
            String messageContent3 = MessageFormat.format(MessageCopywrit.ARBITRATE_USER_INFO,username,orders.getId());
            MessageText messageText3 = new MessageText(0L,messageContent3,MessageType.GLOBAL,0L,DateUtil.getPresentDate(),orders.getId());
            messageText3= messageTextRepo.save(messageText3);
            for (OrderArbitrate o : list){
                Message abritrateMessage = new Message(o.getUserId(),messageText3.getId(),MessageReadStatus.UN_READ,MessageType.GLOBAL);
                messageRepo.save(abritrateMessage);
            }
        } catch (Exception e) {
            LOG.error("MESSAGE -- post the seller or buyer start arbitrate message : {}",e);
        }
    }
    //上传仲裁凭据的站内信
    public void postUploadEvidence(Orders orders,Long userId){
        try {
            //上传交易凭据 附件的人的站内信
            String messageContent = MessageFormat.format(MessageCopywrit.UPLOD_EVIDENCE,orders.getId());
            MessageText messageText = new MessageText(0L,messageContent,MessageType.GLOBAL,0L,DateUtil.getPresentDate(),orders.getId());
            MessageText save = messageTextRepo.save(messageText);
            Message message = new Message(userId,save.getId(),MessageReadStatus.UN_READ,MessageType.GLOBAL);
            messageRepo.save(message);

            //仲裁人的站内信
            String messageContent1 = MessageFormat.format(MessageCopywrit.UPLOAD_EVIDENCE_ABRAITRATE,orders.getId(),this.getUserById(userId).getLoginname());
            MessageText messageText1 = new MessageText(0L,messageContent1,MessageType.GLOBAL,0L,DateUtil.getPresentDate(),orders.getId());
            messageText1= messageTextRepo.save(messageText1);
            List<OrderArbitrate> list = orderArbitrateRepo.findByOrderId(orders.getId());
            for (OrderArbitrate o : list){
                Message abritrateMessage = new Message(o.getUserId(),messageText1.getId(),MessageReadStatus.UN_READ,MessageType.GLOBAL);
                messageRepo.save(abritrateMessage);
            }
        } catch (Exception e) {
            LOG.error("MESSAGE -- post upload arbitrate evidence message : {}",e);
        }

    }
    public User getUserById(Long userId){
        User user = null;
        try {
            JSONObject str = restTemplate.getForObject(ThemisUserAddress.GET_USER+userId, JSONObject.class);
            if(null != str){
                Integer status = (Integer) str.get("status");
                if(status == 1){
                    user = JsonUtil.jsonToEntity(JsonUtil.toJson(str.get("data")), User.class);
                }
                return user;
            }
        } catch (Exception e) {
            LOG.error("get user by id from themis-user faild : {}",e);
            throw  e;
        }
        return null;
    }

}
