/**
 * Created by zhangxiaojing on 2017/10/25.
 */
import axios from 'axios';
import {
    ROOT_ORDER,
    FETCH_NOT_COMPLETED_ORDERS,
    FETCH_COMPLETED_ORDERS,
    FETCH_ORDERS_DETAILS,
    FETCH_TRADE_PARTNER_MESSAGE,
    ADD_PAYMENT_INFO,
    FETCH_KEYS,
    CONFIRM_ORDER,
    CONFIRM_SEND_MONEY,
    RELEASE_BTC,
    CONFIRM_GOODS,
    ADD_TRANSACTION_ID,
    CANCEL_ORDERS,
    SAVE_COMMENT,
    getAuthorizedHeader,
    requestError
} from './types';

/**
 * 获取未完成订单列表
 * @returns {Function}
 */
export function fetchNoCompletedOrders({formData}) {
    return function(dispatch) {
        axios({
            method:'post',
            url:`${ROOT_ORDER}/order/findNoCompletedOrders`,
            data:formData,
            headers: getAuthorizedHeader()
        }).then((res) => {
            if (res.data.status == 1) {
                dispatch({
                    type: FETCH_NOT_COMPLETED_ORDERS,
                    payload: res.data
                });
            }
        }).catch( err => dispatch(requestError(err.message)) );
    };
}
/**
 * 获取已完成订单列表
 * @returns {Function}
 */

export function fetchCompletedOrders({userId}) {
    return function(dispatch) {
        axios({
            method:'post',
            url:`${ROOT_ORDER}/order/findCompletedOrders`,
            data:userId,
            headers: getAuthorizedHeader()
        }).then((res) => {
            if (res.data.status == 1) {
                dispatch({
                    type: FETCH_COMPLETED_ORDERS,
                    payload: res.data
                });
            }
        }).catch( err => dispatch(requestError(err.message)) );
    };
}
/**
 * 获取订单详情
 * @returns {Function}
 */
export function fetchOrdersDetails({data}, callback) {
    return function (dispatch) {
        axios({
            method: 'post',
            url: `${ROOT_ORDER}/order/findOrdersDetails `,
            data: data,
            headers: getAuthorizedHeader()
        }).then((res) => {
            if (res.data.status == 1) {
                callback(res.data.data);
                dispatch({
                    type: FETCH_ORDERS_DETAILS,
                    payload: res.data.data
                });

            }
        }).catch(err => dispatch(requestError(err.message)));
    };
}
/**
 * 获取交易伙伴详情
 * @returns {Function}
 */
export function fetchTradePartnerMessage({data}) {
    return function (dispatch) {
        axios({
            method: 'post',
            url: `${ROOT_ORDER}/order/findUserTxDetail`,
            data: data,
            headers: getAuthorizedHeader()
        }).then((res) => {
            if (res.data.status == 1) {
                dispatch({
                    type: FETCH_TRADE_PARTNER_MESSAGE,
                    payload: res.data.data
                });
            }
        }).catch(err =>{
                dispatch(requestError(err.message));
            });
    };
}
/**
 * 获取卖家付款信息
 * @returns {Function}
 */
export function fetchKey({orderId}, callback) {
    return function (dispatch) {
        axios({
            method: 'post',
            url: `${ROOT_ORDER}/order/judgeSellerPubPriAuth`,
            data: orderId,
            headers: getAuthorizedHeader()
        }).then((res) => {
                dispatch({
                    type: FETCH_KEYS,
                    payload: res.data.data
                });
                callback(res.data);
        }).catch(err =>{
            dispatch(requestError(err.message));
        });
    };
}
/**
 * 卖家上传付款信息
 * @returns {Function}
 */
export function addPaymentInfo({paymentInfo}, callback) {
    return function (dispatch) {
        axios({
            method: 'post',
            url: `${ROOT_ORDER}/order/saveAddresskey`,
            data: paymentInfo,
            headers: getAuthorizedHeader()
        }).then((res) => {
            if (res.data.status == 1) {
                dispatch({
                    type: ADD_PAYMENT_INFO,
                    payload: res.data.data
                });
            }
            callback(res.data);
        }).catch(err =>{
            dispatch(requestError(err.message));
        });
    };
}
/**
 * 卖家上传交易id
 * @returns {Function}
 */
export function addTransactionId({txIdInfo}, callback) {
    return function (dispatch) {
        axios({
            method: 'post',
            url: `${ROOT_ORDER}/order/uploadTxId`,
            data: txIdInfo,
            headers: getAuthorizedHeader()
        }).then((res) => {
            if (res.data.status == 1) {
                dispatch({
                    type: ADD_TRANSACTION_ID,
                    payload: res.data.data
                });
            }
            callback(res.data);
        }).catch(err =>{
            dispatch(requestError(err.message));
        });
    };
}
/**
 * 卖家确认订单
 * @returns {Function}
 */
export function confirmOrder({orderId}, callback) {
    return function (dispatch) {
        axios({
            method: 'post',
            url: `${ROOT_ORDER}/order/confirmOrders`,
            data: orderId,
            headers: getAuthorizedHeader()
        }).then((res) => {
            dispatch({
                type: CONFIRM_ORDER,
                payload: res.data.data
            });
            callback(res.data);
        }).catch(err =>{
            dispatch(requestError(err.message));
        });
    };
}
/**
 * 买家付款
 * @returns {Function}
 */
export function confirmSendMoney({orderId}, callback) {
    return function (dispatch) {
        axios({
            method: 'post',
            url: `${ROOT_ORDER}/order/confirmSendMoney`,
            data: orderId,
            headers: getAuthorizedHeader()
        }).then((res) => {
            dispatch({
                type: CONFIRM_SEND_MONEY,
                payload: res.data.data
            });
            callback(res.data);
        }).catch(err =>{
            dispatch(requestError(err.message));
        });
    };
}
/**
 * 卖家释放比特币
 * @returns {Function}
 */
export function releaseBtc({releaseData}, callback) {
    return function (dispatch) {
        axios({
            method: 'post',
            url: `${ROOT_ORDER}/order/releaseBTC`,
            data: releaseData,
            headers: getAuthorizedHeader()
        }).then((res) => {
            dispatch({
                type: RELEASE_BTC,
                payload: res.data
            });
            callback(res.data);
        }).catch(err =>{
            dispatch(requestError(err.message));
        });
    };
}
/**
 * 买家确认收货
 * @returns {Function}
 */
export function confirmGoods({confirmGoodsData}, callback) {
    return function (dispatch) {
        axios({
            method: 'post',
            url: `${ROOT_ORDER}/order/confirmReciveBTC`,
            data: confirmGoodsData,
            headers: getAuthorizedHeader()
        }).then((res) => {
            if (res.data.status == 1) {
                dispatch({
                    type: CONFIRM_GOODS,
                    payload: res.data.data
                });
                callback(res.data);
            }
        }).catch(err =>{
            dispatch(requestError(err.message));
        });
    };
}
/**
 * 提交评价
 * @returns {Function}
 */
export function saveComment({commentData}, callback) {
    return function (dispatch) {
        axios({
            method: 'post',
            url: `${ROOT_ORDER}/order/saveComment`,
            data: commentData,
            headers: getAuthorizedHeader()
        }).then((res) => {
            if (res.data.status == 1) {
                dispatch({
                    type: SAVE_COMMENT,
                    payload: res.data.data
                });
                callback(res.data);
            }
        }).catch(err =>{
            dispatch(requestError(err.message));
        });
    };
}
/**
 * 取消订单
 * @returns {Function}
 */
export function cancelOrders({cancelData}, callback) {
    return function (dispatch) {
        axios({
            method: 'post',
            url: `${ROOT_ORDER}/order/cancelOrders`,
            data: cancelData,
            headers: getAuthorizedHeader()
        }).then((res) => {
            if (res.data.status == 1) {
                dispatch({
                    type: CANCEL_ORDERS,
                    payload: res.data.data
                });
                callback(res.data);
            }
        }).catch(err =>{
            dispatch(requestError(err.message));
        });
    };
}

