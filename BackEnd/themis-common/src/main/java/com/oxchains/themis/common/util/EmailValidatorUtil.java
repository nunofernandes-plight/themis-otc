package com.oxchains.themis.common.util;

import org.apache.commons.net.smtp.SMTPClient;
import org.apache.commons.net.smtp.SMTPReply;
import org.apache.log4j.Logger;
import org.xbill.DNS.Lookup;
import org.xbill.DNS.Record;
import org.xbill.DNS.Type;

import java.io.IOException;

/**
 * 验证邮箱有效性和真实性工具类
 *
 * @author luoxuri
 * @create 2017-10-30 14:15
 **/
public class EmailValidatorUtil {

    private static final Logger logger = Logger.getLogger(EmailValidatorUtil.class);
    /**
     * @param email 待校验的邮箱地址 
     * @return 
     */
    public static boolean isEmailValid(String email) {
        if(!email.matches("[\\w\\.\\-]+@([\\w\\-]+\\.)+[\\w\\-]+")){
            logger.error("邮箱（"+email+"）校验未通过，格式不对!");
            return false;
        }
        String host = "";
        String hostName = email.split("@")[1];
        Record[] result = null;
        SMTPClient client = new SMTPClient();
        try {
            // 查找DNS缓存服务器上为MX类型的缓存域名信息  
            Lookup lookup=new Lookup(hostName, Type.MX);
            lookup.run();

            if(lookup.getResult() != Lookup.SUCCESSFUL) {//查找失败  
                logger.error("邮箱（"+email+"）校验未通过，未找到对应的MX记录!");
                return false;
            }else{//查找成功  
                result = lookup.getAnswers();
            }
            //尝试和SMTP邮箱服务器建立Socket连接  
            for(int i=0;i<result.length;i++){
                host=result[i].getAdditionalName().toString();
                logger.info("SMTPClient try connect to host:"+host);
                //此connect()方法来自SMTPClient的父类
                //继承关系结构
                //尝试Socket连接到SMTP服务器
                client.connect(host);
                //（查看响应码是否正常）.   
                //（所有以2开头的响应码都是正常的响应）.   
                //The SMTP server will send a positive completion response on the final successful completion of a command.   
                if(!SMTPReply.isPositiveCompletion(client.getReplyCode())){

                    //断开socket连接
                    client.disconnect();
                    continue;
                }else{
                    logger.info("找到MX记录:"+hostName);
                    logger.info("建立链接成功："+hostName);
                    break;
                }
            }
            logger.info("SMTPClient ReplyString:"+client.getReplyString());
            String emailSuffix="126.com";
            String emailPrefix="txwwze";
            String fromEmail=emailPrefix+"@"+emailSuffix;
            //Login to the SMTP server by sending the HELO command with the given hostname as an argument.   
            //Before performing any mail commands, you must first login.   
            //尝试和SMTP服务器建立连接,发送一条消息给SMTP服务器  
            client.login(emailPrefix);
            logger.info("SMTPClient login:"+emailPrefix+"...");
            logger.info("SMTPClient ReplyString:"+client.getReplyString());
            //Set the sender of a message using the SMTP MAIL command, 
            //specifying a reverse relay path.   
            //The sender must be set first before any recipients may be specified,   
            //otherwise the mail server will reject your commands.   
            //设置发送者，在设置接受者之前必须要先设置发送者  
            client.setSender(fromEmail);
            logger.info("设置发送者 :"+fromEmail);
            logger.info("SMTPClient ReplyString:"+client.getReplyString());

            //Add a recipient for a message using the SMTP RCPT command,   
            //specifying a forward relay path. The sender must be set first before any recipients may be specified,   
            //otherwise the mail server will reject your commands.   
            //设置接收者,在设置接受者必须先设置发送者，否则SMTP服务器会拒绝你的命令  
            client.addRecipient(email);
            logger.info("设置接收者:"+email);
            logger.info("SMTPClient ReplyString:"+client.getReplyString());
            logger.info("SMTPClient ReplyCode："+client.getReplyCode()+"(250表示正常)");
            if (250==client.getReplyCode()) {
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                client.disconnect();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return false;
    }

}
