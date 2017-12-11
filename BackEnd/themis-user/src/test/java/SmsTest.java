import org.apache.commons.httpclient.Header;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.methods.PostMethod;

/**
 * @author oxchains
 * @time 2017-11-13 14:21
 * @name SmsTest
 * @desc:
 */
public class SmsTest {
    public static void main(String[] args) throws Exception{
        //sendSms();
        sendMail();
    }

    static void sendSms() throws Exception{
        HttpClient client = new HttpClient();
        PostMethod post = new PostMethod("http://utf8.api.smschinese.cn/");
        post.addRequestHeader("Content-Type",
                "application/x-www-form-urlencoded;charset=utf-8");// 在头文件中设置转码
        NameValuePair[] param = {
                new NameValuePair("Uid","cherrish_1991"),
                new NameValuePair("Key","fc89fcc6529f063a4e1f"),
                new NameValuePair("smsMob","18910313839"),
                new NameValuePair("smsText","验证码:123456"),
        };
        post.setRequestBody(param);
        client.executeMethod(post);
        Header[] headers = post.getRequestHeaders();
        int statusCode = post.getStatusCode();
        System.out.println("*** status *** "+statusCode);
        for (Header h : headers){
            System.out.println(h.toString());
        }
        String result = new String(post.getResponseBodyAsString().getBytes("UTF-8"));
        System.out.println("*** result *** "+result);
        post.releaseConnection();
    }

    static void sendMail() throws Exception{
        //MailService mailService =new MailService();
        //String[] to = {"chenchunlin@oxchains.com"};
        //mailService.send(new Email(to,"email","test"));

        String str =new String("123141414");
        System.out.println(str.toString());
    }
}
