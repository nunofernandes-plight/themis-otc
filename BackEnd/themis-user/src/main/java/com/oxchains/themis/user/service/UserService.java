package com.oxchains.themis.user.service;

import com.oxchains.basicService.files.tfsService.TFSConsumer;
import com.oxchains.themis.common.auth.JwtService;
import com.oxchains.themis.common.constant.Status;
import com.oxchains.themis.common.constant.UserConstants;
import com.oxchains.themis.common.mail.Email;
import com.oxchains.themis.common.model.RestResp;
import com.oxchains.themis.common.param.ParamType;
import com.oxchains.themis.common.param.RequestBody;
import com.oxchains.themis.common.param.VerifyCode;
import com.oxchains.themis.common.util.*;
import com.oxchains.themis.repo.dao.*;
import com.oxchains.themis.repo.entity.*;
import com.oxchains.themis.user.domain.UserRelationInfo;
import com.oxchains.themis.user.domain.UserTrust;
import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.*;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.TimeUnit;

import static com.google.common.collect.Lists.newArrayList;

/**
 * @author ccl
 * @time 2017-10-12 17:24
 * @name UserService
 * @desc:
 */

//@Transactional
@Slf4j
@Service
public class UserService extends BaseService {

    //private static final Logger log = LoggerFactory.getLogger(UserService.class);

    @Resource
    private UserDao userDao;

    @Resource
    JwtService jwtService;

    @Resource
    private RoleDao roleDao;

    @Resource
    private UserTxDetailDao userTxDetailDao;

    @Resource
    private OrderDao orderDao;

    @Resource
    private UserRelationDao userRelationDao;

    @Resource
    MailService mailService;

    @Resource
    private RedisTemplate redisTemplate;

    @Resource
    TFSConsumer tfsConsumer;

    private String token;

//    @Resource
//    AccountService accountService;

    public RestResp addUser(User user) {
        boolean mail = false;
        if(null == user){
            return RestResp.fail("请正确提交的注册信息");
        }
        if(null == user.getLoginname() || !RegexUtils.match(user.getLoginname(),RegexUtils.REGEX_NAME_LEN32)){
            return RestResp.fail("请正确填写登录名，只能包含字母、数字、下划线，且只能以字母开头");
        }
        if(null != user.getMobilephone()){
            if(!RegexUtils.match(user.getMobilephone(),RegexUtils.REGEX_MOBILEPHONE)){
                return RestResp.fail("请正确填写手机号");
            }
        }
        if(null != user.getEmail()){
            if(!RegexUtils.match(user.getEmail(),RegexUtils.REGEX_EMAIL)){
                return RestResp.fail("请正确填写邮箱地址");
            }
            user.setEnabled(Status.EnableStatus.UNENABLED.getStatus());
            mail = true;
        }else {
            user.setEnabled(Status.EnableStatus.ENABLED.getStatus());
        }
        Optional<User> optional = getUser(user);
        if (optional.isPresent()) {
            User u = optional.get();
            if(null != user.getLoginname() && user.getLoginname().equals(u.getLoginname())){
                return RestResp.fail("用户名已经存在");
            }
            if(null != user.getMobilephone() && user.getMobilephone().equals(u.getMobilephone())){
                return RestResp.fail("该手机号已被注册");
            }
            if(null != user.getEmail() && user.getEmail().equals(u.getEmail())){
                return RestResp.fail("该邮箱已被注册");
            }
            return RestResp.fail("注册用户已经存在");
        }
        if(null==user.getPassword() || "".equals(user.getPassword().trim())){
            return RestResp.fail("请正确填写登录密码");
        }
        user.setPassword(EncryptUtils.encodeSHA256(user.getPassword()));
        if(null == user.getCreateTime()){
            user.setCreateTime(DateUtil.getPresentDate());
        }
        if(null == user.getRoleId()){
            user.setRoleId(4L);
        }
        if (null == user.getLoginStatus()){
            user.setLoginStatus(0);
        }

        user = userDao.save(user);
        if (user == null) {
            return RestResp.fail("操作失败");
        }

        UserTxDetail userTxDetail = new UserTxDetail(true);
        userTxDetail.setUserId(user.getId());

        try{
            userTxDetailDao.save(userTxDetail);
        }catch (Exception e){
            log.error("保存用户交易详情异常", e);
            userDao.delete(user.getId());
            return RestResp.fail("注册失败", e);
        }
        if(mail){
            String url = "http://"+frontEndUrl+"/islive?email="+user.getEmail();
            try {
                mailService.sendHtmlMail(user.getEmail(),"账号激活","请点击以下链接进行账号激活操作：\n" +
                        "<a href='"+url+"'>点击这里</a>");
                return RestResp.success("注册成功，验证信息已经发送到邮箱："+user.getEmail()+"中，请前往操作",null);
            }catch (Exception e){
                log.error("邮件发送异常",e);
                return RestResp.fail("邮件发送失败,请重新操作");
            }
        }else {
            return RestResp.success("注册成功",null);
        }
    }

    public RestResp updateUser(User user) {
        User u = userDao.findByLoginname(user.getLoginname());
        if(u==null){
            return RestResp.fail("提交信息有误");
        }
        u.setUsername(user.getUsername());
        user = userDao.save(u);
        if (user == null) {
            return RestResp.fail("操作失败");
        }
        return RestResp.success("操作成功",null);
    }
    public RestResp updateUser(User user, ParamType.UpdateUserInfoType uuit) {
        Object res = null;
        if(null == user){
            return RestResp.fail("参数不能为空");
        }
        if(user.getLoginname()==null){
            return RestResp.fail("用户名不能为空");
        }
        User u = userDao.findByLoginname(user.getLoginname());
        if(null == u){
            return RestResp.fail("用户信息不正确");
        }
        switch (uuit){
            case INFO:
                boolean flag = false;
                if(null!=user.getDescription() && !"".equals(user.getDescription().trim())) {
                    u.setDescription(user.getDescription());
                    flag = true;
                }
                if(!flag){
                    return RestResp.fail("没有需要修改的信息");
                }
                break;
            case PWD:
                if(null==user.getPassword() || "".equals(user.getPassword().trim())){
                    return RestResp.fail("旧密码不能为空");
                }
                if(null==user.getNewPassword() || "".equals(user.getNewPassword().trim())){
                    return RestResp.fail("新密码不能为空");
                }
                if(EncryptUtils.encodeSHA256(user.getPassword()).equals(u.getPassword())){
                    u.setPassword(EncryptUtils.encodeSHA256(user.getNewPassword()));
                }else {
                    return RestResp.fail("输入的旧密码错误");
                }
                break;
            case FPWD:
                u.setFpassword(EncryptUtils.encodeSHA256(user.getFpassword()));
                break;
            case EMAIL:
                if(null == user.getEmail() || "".equals(user.getEmail().trim()) || !RegexUtils.match(user.getEmail(),RegexUtils.REGEX_EMAIL)){
                    return RestResp.fail("请输入正确的邮箱地址");
                }
                u.setEmail(user.getEmail());
                break;
            case PHONE:
                if(null == user.getMobilephone() || "".equals(user.getMobilephone().trim()) || !RegexUtils.match(user.getMobilephone(),RegexUtils.REGEX_MOBILEPHONE)){
                    return RestResp.fail("请输入正确的手机号");
                }
                u.setMobilephone(user.getMobilephone());
                break;
                default:
                    break;
        }
        reSaveRedis(u, token);
        return save(u, res);
    }
    public RestResp avatar(User user){
        if(null == user){
            return RestResp.fail("参数不能为空");
        }
        if(user.getLoginname()==null){
            return RestResp.fail("用户名不能为空");
        }
        User u = userDao.findByLoginname(user.getLoginname());
        MultipartFile file = user.getFile();
        if(null != file) {
            String fileName = file.getOriginalFilename();
            String suffix = fileName.substring(fileName.lastIndexOf("."));
            String newFileName = tfsConsumer.saveTfsFile(file, u.getId());
            if (null == newFileName) {
                return RestResp.fail("头像上传失败");
            }
            u.setImage(newFileName);
            userDao.save(u);
            return RestResp.success("头像上传成功",newFileName);
        }
        return RestResp.fail("上传头像失败");
    }
    private RestResp save(User user,Object res){
        try {
            userDao.save(user);
            return RestResp.success("操作成功",res);
        }catch (Exception e){
            log.error("保存用户信息异常", e);
            return RestResp.fail("操作失败");
        }
    }

    public RestResp login(User user) {
        user.setPassword(EncryptUtils.encodeSHA256(user.getPassword()));
        try{
            Optional<User> optional = findUser(user);
            return optional.map(u -> {
                if(u.getEnabled().equals(Status.EnableStatus.UNENABLED.getStatus())){
                    return RestResp.fail("账号未激活");
                }
                if(u.getLoginStatus().equals(Status.LoginStatus.LOGIN.getStatus())){
                    return RestResp.fail("用户已经登录");
                }
                String originToken = jwtService.generate(u);
                token = "Bearer " + originToken;

                Role role = roleDao.findById(u.getRoleId());
                UserTxDetail userTxDetail = findUserTxDetailByUserId(u.getId());
                if(userTxDetail==null){
                    userTxDetail =new UserTxDetail(true);
                    userTxDetail.setUserId(u.getId());
                    userTxDetailDao.save(userTxDetail);
                }
                log.info("token = " + token);
                User userInfo = new User(u);
                userInfo.setRole(role);
                userInfo.setPassword(null);
                userInfo.setToken(token);

                userInfo.setUserTxDetail(userTxDetail);

                u.setLoginStatus(Status.LoginStatus.LOGOUT.getStatus());
                User save = userDao.save(u);
                // redis 存储
                boolean keyExist = redisTemplate.hasKey(save.getId().toString());
                if (!keyExist){
                    log.info("保存 TOKEN 到 REDIS");
                    saveRedis(save ,originToken);
                }
                ConstantUtils.USER_TOKEN.put(u.getLoginname(), token);

                //new UserToken(u.getUsername(),token)
                return RestResp.success("登录成功", userInfo);
            }).orElse(RestResp.fail("登录账号或密码错误"));
        }catch (Exception e){
            log.error("用户信息异常",e);
            return RestResp.fail("用户信息异常");
        }
    }

    @Deprecated
    public String _queryRedisValue(String key){
        ValueOperations operations = redisTemplate.opsForValue();
        String value = (String) operations.get(key);
        System.out.println("UserService：redis中的token = " + value);
        return value;
    }

    private void saveRedis(User save, String originToken){
        ValueOperations<String, String> operations = redisTemplate.opsForValue();
        operations.set(save.getId().toString(), originToken, 7, TimeUnit.DAYS);
    }

    private void reSaveRedis(User save, String originToken){
        log.info("重新保存 TOKEN 到 REDIS ");
        redisTemplate.delete(save.getId().toString());
        saveRedis(save, originToken);
    }

    public RestResp logout(User user){
        User u = userDao.findByLoginname(user.getLoginname());
        if(null != u && u.getLoginStatus().equals(Status.LoginStatus.LOGIN.getStatus())){
            u.setLoginStatus(Status.LoginStatus.LOGOUT.getStatus());
            userDao.save(u);
            redisTemplate.delete(u.getId().toString());
            return RestResp.success("退出成功",null);
        }else {
            return RestResp.fail("退出失败");
        }
    }

    public Optional<User> findUser(User user) {
        Optional<User> optional = null;
        if (null != user.getLoginname()) {
            optional = userDao.findByLoginnameAndPassword(user.getLoginname(), user.getPassword());
            if (optional.isPresent()) {
                return optional;
            }
        }
        if (null != user.getEmail()) {
            optional = userDao.findByEmailAndPassword(user.getEmail(), user.getPassword());
            if (optional.isPresent()) {
                return optional;
            }
        }
        if (null != user.getMobilephone()) {
            optional = userDao.findByMobilephoneAndPassword(user.getMobilephone(), user.getPassword());
            if (optional.isPresent()) {
                return optional;
            }
        }
        return Optional.empty();
    }

    public Optional<User> getUser(User user){
        User u = null;
        if (null != user.getLoginname()) {
            u = userDao.findByLoginname(user.getLoginname());
            if (null != u) {
                return  Optional.of(u);
            }
        }
        if (null != user.getEmail()) {
            u = userDao.findByEmail(user.getEmail());
            if (null != u) {
                return Optional.of(u);
            }
        }
        if (null != user.getMobilephone()) {
            u = userDao.findByMobilephone(user.getMobilephone());
            if (null != u) {
                return Optional.of(u);
            }
        }
        return Optional.empty();
    }

    public RestResp findUsers() {
        return RestResp.success(newArrayList(userDao.findAll()));
    }

    /**
     * 信任/屏蔽
     * @return
     */
    public RestResp trustUsers(RequestBody body, Status.TrustStatus status){
        com.oxchains.themis.common.model.Page<UserTrust> res =new com.oxchains.themis.common.model.Page(body.getPageNo(),body.getPageSize());
        Pageable pager=new PageRequest((body.getPageNo()-1)*body.getPageSize(),body.getPageSize(),new Sort(Sort.Direction.ASC,"toUserId"));
        Page<UserRelation> page = null;
        if(status.equals(Status.TrustStatus.SHIELD)){
            page = userRelationDao.findByFromUserIdAndStatus(body.getUserId(), Status.TrustStatus.SHIELD.getStatus(),pager);
        }else {
            page = userRelationDao.findByFromUserIdAndStatus(body.getUserId(), Status.TrustStatus.TRUST.getStatus(),pager);
        }

        res.setTotalCount((int)page.getTotalElements());
        res.setTotalPages(page.getTotalPages());
        List<UserTrust> list = new ArrayList<>();
        Iterator<UserRelation> it = page.iterator();
        UserTrust trustu = null;
        while (it.hasNext()){
            UserRelation relation = it.next();
            trustu = new UserTrust();
            User u = userDao.findOne(relation.getToUserId());
            int txToNum = orderDao.countByBuyerIdOrSellerId(body.getUserId(),relation.getToUserId()) + orderDao.countByBuyerIdOrSellerId(relation.getToUserId(),body.getUserId());
            trustu.setTxToNum(txToNum);
            trustu.setFromUserId(relation.getFromUserId());
            trustu.setFromUserName(u.getLoginname());
            trustu.setToUserId(relation.getToUserId());
            trustu.setToUserName(u.getLoginname());

            UserTxDetail detail = findUserTxDetailByUserId(relation.getToUserId());

            trustu.setTxNum(detail.getTxNum());
            trustu.setGoodDesc(detail.getGoodDesc());
            trustu.setBadDesc(detail.getBadDesc());
            trustu.setFirstBuyTime(detail.getFirstBuyTime());
            trustu.setBelieveNum(detail.getBelieveNum());
            trustu.setBuyAmount(detail.getBuyAmount());
            trustu.setSellAmount(detail.getSellAmount());


            list.add(trustu);
        }
        res.setResult(list);

        return RestResp.success(res);
    }

    /**
     * 被信任
     * @return
     */
    public RestResp trustedUsers(RequestBody body){
        com.oxchains.themis.common.model.Page<UserTrust> res =new com.oxchains.themis.common.model.Page(body.getPageNo(),body.getPageSize());
        Pageable pager=new PageRequest((body.getPageNo()-1)*body.getPageSize(),body.getPageSize(),new Sort(Sort.Direction.ASC,"fromUserId"));
        Page<UserRelation> page = userRelationDao.findByToUserIdAndStatus(body.getUserId(),Status.TrustStatus.TRUST.getStatus(),pager);
        res.setTotalCount((int)page.getTotalElements());
        res.setTotalPages(page.getTotalPages());
        List<UserTrust> list = new ArrayList<>();
        Iterator<UserRelation> it = page.iterator();
        UserTrust trustu = null;
        while (it.hasNext()){
            UserRelation relation = it.next();
            trustu = new UserTrust();
            User u = userDao.findOne(relation.getFromUserId());
            int txToNum = orderDao.countByBuyerIdOrSellerId(body.getUserId(),relation.getFromUserId()) + orderDao.countByBuyerIdOrSellerId(relation.getFromUserId(),body.getUserId());
            trustu.setTxToNum(txToNum);
            trustu.setFromUserId(relation.getFromUserId());
            trustu.setFromUserName(u.getLoginname());
            trustu.setToUserId(relation.getToUserId());
            trustu.setToUserName(relation.getToUserName());

            UserTxDetail detail = findUserTxDetailByUserId(relation.getFromUserId());

            trustu.setTxNum(detail.getTxNum());
            trustu.setGoodDesc(detail.getGoodDesc());
            trustu.setBadDesc(detail.getBadDesc());
            trustu.setFirstBuyTime(detail.getFirstBuyTime());
            trustu.setBelieveNum(detail.getBelieveNum());
            trustu.setBuyAmount(detail.getBuyAmount());
            trustu.setSellAmount(detail.getSellAmount());
            list.add(trustu);
        }
        res.setResult(list);

        return RestResp.success(res);
    }

    private UserTxDetail findUserTxDetailByUserId(Long userId){
        UserTxDetail userTxDetail = userTxDetailDao.findByUserId(userId);
        if(null == userTxDetail){
            return null;
        }
        List<Order> orders = orderDao.findByBuyerIdOrSellerId(userId, userId);
        double buyAmount = 0d;
        double sellAmount = 0d;
        for (Order order : orders) {
            if (userId.equals(order.getBuyerId())) {
                buyAmount += order.getAmount() == null ? 0d : order.getAmount().doubleValue();
            }
            if (userId.equals(order.getSellerId())) {
                sellAmount += order.getAmount() == null ? 0d : order.getAmount().doubleValue();
            }
        }
        userTxDetail.setBuyAmount(buyAmount);
        userTxDetail.setSellAmount(sellAmount);

        return userTxDetail;
    }

    public RestResp relation(UserRelation relation){
        UserRelation ur = userRelationDao.findByFromUserIdAndToUserId(relation.getFromUserId(),relation.getToUserId());
        try{
            if(null != ur){
                ur.setStatus(relation.getStatus());
                userRelationDao.save(ur);
            }else {
                userRelationDao.save(relation);
            }
            return RestResp.success("操作成功",null);
        }catch (Exception e){
            return RestResp.fail("操作失败");
        }
    }

    public RestResp getRelation(UserRelation relation){
        UserRelationInfo userRelationInfo = null;
        User user = userDao.findOne(relation.getToUserId());
        if(null == user){
            return RestResp.fail("无法查询相关用户信息");
        }
        userRelationInfo = new UserRelationInfo(user);
        UserTxDetail userTxDetail = userTxDetailDao.findByUserId(relation.getToUserId());
        userRelationInfo.setUserTxDetail(userTxDetail);
        UserRelation ur = userRelationDao.findByFromUserIdAndToUserId(relation.getFromUserId(),relation.getToUserId());
        if(null == ur){
            ur = new UserRelation();
            ur.setFromUserId(relation.getFromUserId());
            ur.setToUserId(relation.getToUserId());
            ur.setStatus(Status.TrustStatus.NONE.getStatus());
        }
        userRelationInfo.setUserRelation(ur);
        return RestResp.success(userRelationInfo);
    }

    public RestResp forgetPwd(RequestBody body){
        User user = userDao.findByLoginname(body.getLoginname());
        user.setPassword(EncryptUtils.encodeSHA256("123456"));
        userDao.save(user);
        try{
            String[] to = {body.getEmail()};
            mailService.send(new Email(to,"密码重置","密码重置为:123456,请尽快登录修改!"));
            return RestResp.success("操作成功",null);
        }catch (Exception e){
            log.error("操作失败: {}",e);
            return RestResp.fail("操作失败");
        }
    }

    public RestResp getArbitrations(){
        List<User> list = userDao.findByRoleId(UserConstants.UserRole.ARBITRATION.getRoleId());
        if(null != list && list.size()>0 ){
            for(int i= 0; i < list.size(); i++){
                list.get(i).setPassword(null);
            }
        }
        return RestResp.success(list);
    }

    public RestResp getUser(Long id){
        if(null == id){
            return RestResp.fail("用户id不能为空");
        }
        User user = userDao.findOne(id);
        if(user != null){
            user.setPassword(null);
        }
        return RestResp.success(user);
    }

    public boolean saveVcode(String key, String vcode){
        try {
            ValueOperations<String, String> ops = redisTemplate.opsForValue();
            ops.set(key, vcode, 5L, TimeUnit.MINUTES);
            return true;
        }catch (Exception e){
            log.error("Redis 操作异常:" ,e);
            return false;
        }
    }

    public String getVcodeFromRedis(String key){
        String val = null;
        try{
            boolean flag = redisTemplate.hasKey(key);
            if(!flag){
                return val;
            }
            ValueOperations<String,String> ops = redisTemplate.opsForValue();
            val = ops.get(key);
            redisTemplate.delete(key);
            return val;
        }catch (Exception e){
            log.error("Redis 操作异常", e);
            return null;
        }
    }

    @Value("${themis.frontend.url}")
    private String frontEndUrl;
    public RestResp sendVmail(VerifyCode vcode){
        if(null == vcode){
            return RestResp.fail("参数不能为空");
        }
        if(null==vcode.getKey()||"".equals(vcode.getKey().trim()) || !vcode.getKey().contains("@")){
            return RestResp.fail("输入的邮箱格式不正确");
        }
        if(null == vcode.getVcode() || "".equals(vcode.getKey().trim())){
            return RestResp.fail("验证码不能为空");
        }

        String vcodeVal = getVcodeFromRedis(vcode.getKey());
        if (vcodeVal.equals(vcode.getVcode())) {
            String[] to = {vcode.getKey()};
            String url = "http://"+frontEndUrl+"/resetpsw?email="+vcode.getKey()+"&vcode="+vcode.getVcode();
            try {
                //mailService.send(new Email(to,"修改密码","请点击以下链接进行密码修改操作：\n" +  url));
                mailService.sendHtmlMail(vcode.getKey(),"修改密码","请点击以下链接进行密码修改操作：\n" +
                        "<a href='"+url+"'>点击这里</a>");
                return RestResp.success("邮件已发送到："+vcode.getKey()+"，请尽快修改您的密码",null);
            }catch (Exception e){
                log.error("邮件发送异常",e);
                return RestResp.fail("邮件发送失败,请重新操作");
            }
        }
        return RestResp.fail("验证码错误");
    }

    public RestResp resetpwd(String resetkey,String password){
        User u = null;
        if(resetkey == null || "".equals(resetkey.trim())){
            return RestResp.fail("账号非法");
        }
        if(null == password || "".equals(password.trim())){
            return RestResp.fail("密码不能为空");
        }
        if(redisTemplate.hasKey(resetkey)){
            return RestResp.fail("链接失效");
        }
        if(resetkey.contains("@")){
            u = userDao.findByEmail(resetkey);
        }else {
            u = userDao.findByMobilephone(resetkey);
        }
        if(null == u){
            return RestResp.fail("重置密码失败");
        }
        if(null !=password){
            u.setPassword(EncryptUtils.encodeSHA256(password));
            userDao.save(u);
            return RestResp.success("重置密码成功!",null);
        }
        return RestResp.fail("重置密码失败");
    }

    public RestResp active(String email){
        if(email==null || "".equals(email) || !RegexUtils.match(email,RegexUtils.REGEX_EMAIL)){
            return RestResp.fail("邮箱格式不正确，激活失败");
        }
        User user = userDao.findByEmail(email);
        if(null == user){
            return RestResp.fail("该邮箱未注册，无法激活");
        }
        if(user.getEnabled().equals(Status.EnableStatus.ENABLED.getStatus())){
            return RestResp.fail("账号已经激活，请勿重复操作");
        }else {
            user.setEnabled(Status.EnableStatus.ENABLED.getStatus());
            userDao.save(user);
            return RestResp.success("账号激活成功",null);
        }
    }
    public RestResp sendMail(String email ,String subject,String content){
        if(email==null || "".equals(email) || !RegexUtils.match(email,RegexUtils.REGEX_EMAIL)){
            return RestResp.fail("请正确填写邮箱");
        }
        if(content==null || "".equals(content.trim()) ){
            return RestResp.fail("发送内容不能为空");
        }
        try {
            mailService.sendHtmlMail(email,subject,content);
            return RestResp.success("邮件已发送到："+email+"，请前往查收",null);
        }catch (Exception e){
            log.error("邮件发送异常",e);
            return RestResp.fail("邮件发送失败,请重新操作");
        }
    }

    public RestResp addBitcoinAddress(String loginname,String firstAddress){
        if(null == loginname || "".equals(loginname.trim())){
            return RestResp.fail("用户名不正确");
        }
        if(null == firstAddress || "".equals(firstAddress.trim()) || firstAddress.length()<26 || firstAddress.length()>34){
            return RestResp.fail("未正确填写收款地址,请重新填写");
        }
        firstAddress = firstAddress.trim();
        User user = userDao.findByLoginname(loginname);
        if(null == user){
            return RestResp.fail("用户名不正确");
        }
        if(firstAddress.equals(user.getFirstAddress())){
            return RestResp.fail("您未修改地址");
        }
        user.setFirstAddress(firstAddress);
        userDao.save(user);
        return RestResp.success("操作成功",firstAddress);

    }
}
