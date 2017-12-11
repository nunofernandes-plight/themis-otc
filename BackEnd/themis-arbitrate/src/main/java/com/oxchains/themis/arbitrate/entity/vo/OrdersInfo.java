package com.oxchains.themis.arbitrate.entity.vo;
import com.oxchains.themis.repo.entity.Notice;
import com.oxchains.themis.repo.entity.Orders;
import com.oxchains.themis.repo.entity.Payment;
import lombok.Data;

@Data
public class OrdersInfo extends Orders{
    private String p2shAddress;  //协商地址
    private String orderStatusName; //订单状态名称
    private Notice notice;  //相关联的公告信息
    private Payment payment; //相关联的 支付方式信息
    private String orderType;  //  交易类型     购买  或 出售
    private String friendUsername; //交易伙伴名称
    private String buyerUsername; //买家名称
    private String sellerUsername; //卖家名称
    private Orders orders;
    private Integer status;
    public OrdersInfo(Orders orders) {
        if(orders != null){
            this.setId(orders.getId());
            this.setMoney(orders.getMoney());
            this.setAmount(orders.getAmount());
            this.setArbitrate(orders.getArbitrate());
            this.setBuyerId(orders.getBuyerId());
            this.setSellerId(orders.getSellerId());
            this.setCreateTime(orders.getCreateTime());
            this.setCurrencyId(orders.getCurrencyId());
            this.setVcurrencyId(orders.getVcurrencyId());
            this.setFinishTime(orders.getFinishTime());
            this.setNoticeId(orders.getNoticeId());
            this.setPaymentId(orders.getPaymentId());
            this.setOrderStatus(orders.getOrderStatus());
            this.setUri(orders.getUri());

        }
    }

}
