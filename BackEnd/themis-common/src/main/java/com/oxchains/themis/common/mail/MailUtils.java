package com.oxchains.themis.common.mail;

import com.oxchains.themis.common.model.RestResp;
import com.oxchains.themis.common.util.RegexUtils;
import org.apache.commons.net.smtp.SMTPClient;
import org.apache.commons.net.smtp.SMTPReply;
import org.xbill.DNS.Lookup;
import org.xbill.DNS.Record;
import org.xbill.DNS.Type;

import java.io.IOException;

/**
 * @author oxchains
 * @time 2017-12-05 15:34
 * @name MailUtils
 * @desc:
 */
public class MailUtils {
    public static boolean checkMail(String email){
        if(email==null || "".equals(email) || !RegexUtils.match(email,RegexUtils.REGEX_EMAIL)){
            return false;
        }
        String host = null;
        String hostName = email.split("@")[1];
        Record[] result = null;
        SMTPClient client = new SMTPClient();
        try {
            Lookup lookup = new Lookup(hostName, Type.MX);
            lookup.run();
            if(lookup.getResult() != Lookup.SUCCESSFUL){
                return false;
            }else {
                result = lookup.getAnswers();
            }
            // 连接到邮箱服务器
            for (int i = 0; i < result.length; i++) {
                host = result[i].getAdditionalName().toString();
                client.connect(host);
                if (!SMTPReply.isPositiveCompletion(client.getReplyCode())) {
                    client.disconnect();
                    continue;
                } else {
                    break;
                }
            }
            //以下2项自己填写快速的，有效的邮箱
            client.login("@oxchains.com");
            client.setSender("chenchunlin@oxchains.com");
            client.addRecipient(email);
            if (250 == client.getReplyCode()) {
                return true;
            }
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            try {
                client.disconnect();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return false;

    }
    private MailUtils(){}


    public static void main(String[] args) {
        System.out.println(checkMail("528050239@qq.com"));
    }
}
