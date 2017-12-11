package com.oxchains.themis.arbitrate.service;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.oxchains.basicService.files.entity.FileInfos;
import com.oxchains.basicService.files.tfsService.TFSConsumer;
import com.oxchains.themis.arbitrate.common.*;
import com.oxchains.themis.arbitrate.entity.OrderEvidence;
import com.oxchains.themis.arbitrate.entity.vo.OrdersInfo;
import com.oxchains.themis.arbitrate.repo.*;
import com.oxchains.themis.common.constant.ThemisUserAddress;
import com.oxchains.themis.common.model.RestResp;
import com.oxchains.themis.common.model.RestRespPage;
import com.oxchains.themis.common.util.JsonUtil;
import com.oxchains.themis.repo.dao.OrderRepo;
import com.oxchains.themis.repo.dao.PaymentRepo;
import com.oxchains.themis.repo.dao.UserTxDetailDao;
import com.oxchains.themis.repo.entity.*;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;
import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Transactional(rollbackFor=Exception.class)
@Service
public class ArbitrateService {
    private final Logger LOG = LoggerFactory.getLogger(this.getClass());
    @Resource
    private OrderRepo orderRepo;
    @Resource
    private OrderArbitrateRepo orderArbitrateRepo;
    @Resource
    private PaymentRepo paymentRepo;
    @Resource
    private OrderEvidenceRepo orderEvidenceRepo;
    @Resource
    private RestTemplate restTemplate;
    @Resource
    private MessageService messageService;
    @Resource
    UserTxDetailDao userTxDetailDao;
    @Resource
    HashOperations hashOperations;
    @Resource
    TFSConsumer tfsConsumer;
    @Value("${themis.user.redisInfo.hk}")
    private String userHK;
    @Value("${themis.notice.redisInfo.hk}")
    private String noticeHk;
    public static final Integer BUYER_SUCCESS = 1;
    public static final Integer SELLER_SUCCESS = 2;
    private static final String remoteError = "服务器繁忙,请稍后重试!";
    /*
   * 根据仲裁者id查找哪些订单可以被自己仲裁的订单列表
   * */
    public RestResp findArbitrareOrderById(Pojo pojos){
        Pageable pageable = new PageRequest(pojos.getPageNum()-1,pojos.getPageSize(),new Sort(Sort.Direction.DESC,"id"));
        List<OrdersInfo> ordersInfoList = null;
        Page<OrderArbitrate> orderArbitratePage = null;
        try {
            OrdersInfo ordersInfo = null;
            orderArbitratePage = orderArbitrateRepo.findByUserIdAndAndStatusIsNot(pojos.getUserId(),ParamType.ArbitrateStatus.NOARBITRATE.getStatus(),pageable);
            ordersInfoList = new ArrayList<>();
            for (OrderArbitrate o: orderArbitratePage.getContent()) {
                ordersInfo = this.findOrdersDetails(o.getOrderId());
                ordersInfo.setBuyerUsername(this.getLoginNameByUserId(ordersInfo.getBuyerId()));
                ordersInfo.setSellerUsername(this.getLoginNameByUserId(ordersInfo.getSellerId()));
                this.setOrderStatusName(ordersInfo);
                ordersInfo.setStatus(o.getStatus());
                ordersInfoList.add(ordersInfo);
            }
        } catch (Exception e) {
            LOG.error("find arbitrate order faild : {}",e.getMessage(),e);
            return RestResp.fail(remoteError);
        }
        return RestRespPage.success(ordersInfoList,orderArbitratePage.getTotalPages());
    }
    /*
    * 根据订单编号查询订单的详细信息
    * */
    public OrdersInfo findOrdersDetails(String orderId){
        Orders o = null;
        OrdersInfo ordersInfo = null;
        try {
            o = orderRepo.findOne(orderId);
            ordersInfo = new OrdersInfo(o);
            ordersInfo.setNotice(this.findNoticeById(o.getNoticeId()));
            this.setOrderStatusName(ordersInfo);
            ordersInfo.setPayment(paymentRepo.findOne(ordersInfo.getPaymentId()));
            return ordersInfo;
        } catch (Exception e) {
            LOG.error("get order details faild : {}",e);
        }
        return ordersInfo;
    }
    /*
   * 这是一个工具类方法  为了给要返回到前台的orders 附上订单状态值
   * */
    public void setOrderStatusName(OrdersInfo o){
        if(o != null){
            if(o.getOrderStatus() != null){
                String orderStatusName = ParamType.OrderStatus.getName(o.getOrderStatus());
                o.setOrderStatusName(orderStatusName);
            }
        }
    }
    public RestResp uploadEvidence(RegisterRequest pojo){
        OrderEvidence orderEvidence = null;
        try {

            orderEvidence = orderEvidenceRepo.findByOrderId(pojo.getId());
            Orders orders = orderRepo.findOne(pojo.getId());
            if(orderEvidence == null){
                orderEvidence = new OrderEvidence();
                orderEvidence.setOrderId(pojo.getId());
                orderEvidence = orderEvidenceRepo.save(orderEvidence);
                orders.setArbitrate(ParamType.ArbitrateStatus.ARBITRATEING.getStatus());
                orders = orderRepo.save(orders);
                //订单仲裁表中的 对应订单的三条仲裁状态改为1 表示 仲裁者仲裁中
                List<OrderArbitrate> orderArbitrateList = orderArbitrateRepo.findByOrderId(orders.getId());
                for (OrderArbitrate o:orderArbitrateList) {
                    o.setStatus(ParamType.ArbitrateStatus.ARBITRATEING.getStatus());
                    orderArbitrateRepo.save(o);
                }
                messageService.postEvidenceMessage(orders,pojo.getUserId());
            }
            StringBuilder imageName = new StringBuilder();
            List<MultipartFile> multipartFileList = Arrays.asList(pojo.getFiles());
            int hasNum = 0;

            if(orders.getBuyerId() == pojo.getUserId().longValue()){
                if(orderEvidence.getBuyerFiles()!=null){
                    hasNum = orderEvidence.getBuyerFiles().split(",").length;
                }
            }
            if(orders.getSellerId() == pojo.getUserId().longValue()){
                if(orderEvidence.getSellerFiles()!=null){
                    hasNum = orderEvidence.getSellerFiles().split(",").length;
                }
            }
            if(hasNum+multipartFileList.size()>5){
                return RestResp.fail("对不起,你上传的凭据超出限额,系统上限为五张,你已上传"+hasNum+"张");
            }
            for(MultipartFile mf:multipartFileList){
                String filename = mf.getOriginalFilename();
                String suffix = filename.substring(filename.lastIndexOf("."));
                String fileName = tfsConsumer.saveTfsFile(mf,pojo.getUserId());
                imageName.append(",");
                imageName.append(fileName);
            }
            if(orders.getBuyerId() == pojo.getUserId().longValue()){
                orderEvidence.setBuyerContent(orderEvidence.getBuyerContent()!=null?orderEvidence.getBuyerContent()+"."+pojo.getContent():pojo.getContent());
                orderEvidence.setBuyerFiles(orderEvidence.getBuyerFiles()!=null?orderEvidence.getBuyerFiles()+imageName.toString():imageName.toString().substring(1));
            }
            if(orders.getSellerId() == pojo.getUserId().longValue()){
                orderEvidence.setSellerFiles(orderEvidence.getSellerFiles()!=null?orderEvidence.getSellerFiles()+imageName.toString():imageName.toString().substring(1));
                orderEvidence.setSellerContent(orderEvidence.getSellerContent()!=null?orderEvidence.getSellerContent()+"."+pojo.getContent():pojo.getContent());
            }
            orderEvidence = orderEvidenceRepo.save(orderEvidence);
            messageService.postUploadEvidence(orders,pojo.getUserId());
        } catch (Exception e) {
            LOG.error("upload evidence faild : {}",e);
            return RestResp.fail("申请仲裁失败");
        }
        return  orderEvidence!=null? RestResp.success():RestResp.fail();
    }

    /*
   * 仲裁者仲裁将密匙碎片给胜利者 并且判断一下是谁胜利了
   * */
    public RestResp arbitrateOrderToUser(Pojo pojo){
        OrderArbitrate orderArbitrate = null;
        try {
            orderArbitrate = orderArbitrateRepo.findByUserIdAndOrderId(pojo.getUserId(),pojo.getId());
            if(orderArbitrate.getStatus().longValue() == ParamType.ArbitrateStatus.ARBITRATEING.getStatus()){
                Orders orders = orderRepo.findOne(pojo.getId());
                if(pojo.getSuccessId().longValue() == BUYER_SUCCESS){
                    orderArbitrate.setBuyerAuth(orderArbitrate.getUserAuth());
                }
                if(pojo.getSuccessId().longValue() == SELLER_SUCCESS){
                    orderArbitrate.setSellerAuth(orderArbitrate.getUserAuth());
                }
                orderArbitrate.setStatus(ParamType.ArbitrateStatus.ARBITRATEEND.getStatus());
                orderArbitrate = orderArbitrateRepo.save(orderArbitrate);
                //仲裁完成后将系统通知发送到卖家买家两方
                messageService.postArbitrateMessage(orders,pojo.getUserId(),pojo.getSuccessId());
            }
        } catch (Exception e) {
            LOG.error("arbitrate orders to user faild : {}",e);
            return RestResp.fail("仲裁失败请稍后重试");
        }
        return orderArbitrate!=null?RestResp.success(orderArbitrate):RestResp.fail();
    }
    public HttpHeaders getHttpHeader(){
        HttpHeaders headers = null;
        try {
            headers = new HttpHeaders();
            MediaType type = MediaType.parseMediaType("application/json; charset=UTF-8");
            headers.setContentType(type);
            headers.add("Accept", MediaType.APPLICATION_JSON.toString());
        } catch (Exception e) {
            LOG.error("get http header faild : {}",e);
        }
        return  headers;
    }
    public RestResp getEvidence(Pojo pojo){
        return RestResp.success(orderEvidenceRepo.findByOrderId(pojo.getId()));
    }
    public RestResp saveOrderAbritrate(List<OrderArbitrate> arbitrateList){
        for (OrderArbitrate o:arbitrateList) {
            OrderArbitrate save = orderArbitrateRepo.save(o);
        }
        return RestResp.success();
    }
    //从用户中心 根据用户id获取用户信息
    @HystrixCommand(fallbackMethod = "getUserByIdError")
    public User getUserById(Long userId){
        try {
            System.out.println(userId);
            String userInfo = (String) hashOperations.get(userHK, userId.toString());
            System.out.println(userInfo);
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
            LOG.error("get user by id from themis-user faild : {}",e.getMessage(),e);
            return null;
        }
        return null;
    }
    public User getUserByIdError(Long userId){
        return null;
    }
    //从公告系统 获取公告
    @HystrixCommand(fallbackMethod = "remoteNoticeError")
    public Notice findNoticeById(Long id){
        Notice notice1 = null;
        try {
            String noticeStrs = (String) hashOperations.get(noticeHk, id.toString());
            if(StringUtils.isNotBlank(noticeStrs)){
                return JsonUtil.jsonToEntity(noticeStrs,Notice.class);
            }
            String noticeStr = restTemplate.getForObject(ThemisUserAddress.GET_NOTICE + id, String.class);
            if(noticeStr != null){
                RestResp restResp = JsonUtil.jsonToEntity(noticeStr, RestResp.class);
                if(null != restResp && restResp.status == 1){
                    notice1 = JsonUtil.objectToEntity(restResp.data, Notice.class);
                }
                hashOperations.put(noticeHk,id.toString(),JsonUtil.toJson(restResp.data));
                return notice1;
            }
        } catch (RestClientException e) {
            LOG.error("get notice faild : {}", e.getMessage(), e);
            return null;
        }
        return null;
    }

    private String remoteNoticeError(Long noticeId){
        return null;
    }
    public void userTxDetailHandle(Orders orders){
        UserTxDetail userTxDetails = userTxDetailDao.findByUserId(orders.getBuyerId());
        userTxDetails.setTxNum(userTxDetails.getTxNum()+1);
        userTxDetails.setSuccessCount(userTxDetails.getSuccessCount()+orders.getAmount().doubleValue());
        userTxDetailDao.save(userTxDetails);
        UserTxDetail noticeTx = userTxDetailDao.findByUserId(orders.getSellerId());
        noticeTx.setTxNum(noticeTx.getTxNum()+1);
        noticeTx.setSuccessCount(noticeTx.getSuccessCount()+orders.getAmount().doubleValue());
        userTxDetailDao.save(noticeTx);
    }
    private String getLoginNameByUserId(Long userId){
        User userById = this.getUserById(userId);
        return userById != null?userById.getLoginname():null;
    }
    public FileInfos getFile(String filename){
        FileInfos tfsFile = null;
        try {
            tfsFile = tfsConsumer.getTfsFile(filename);
        } catch (Exception e) {
            LOG.error("get file faild：{}",e.getMessage(),e);
        }
        return tfsFile;
    }
}
