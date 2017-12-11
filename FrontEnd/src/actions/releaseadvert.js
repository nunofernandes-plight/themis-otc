/**
 * Created by oxchain on 2017/10/23.
 */
import axios from 'axios';
import { browserHistory, hashHistory } from 'react-router';
import {
    ROOT_URLL,
    ROOT_URLZ,
    ROOT_URLC,
    FETCH_ADVERT,
    FETCH_BUY_BTC,
    FETCH_SELL_BTC,
    FETCH_SELL_SECAT,
    FETCH_BUY_SECAT,
    FETCH_BUY_BTC_DETAIL,
    FETCH_SELL_BTC_DETAIL,
    FETCH_ARRAY,
    FETCH_HOME,
    FETCH_BUY_NOW,
    FETCH_SELL_NOW,
    FETCH_MY_ADVERT,
    FETCH_OFF_MYBTC,
    FETCH_BASE_INFO,
    FETCH_TRUSTED,
    OTHER_DETAIL,
    ISTRUST_OR_ISSHIELD,
    FETCH_ADDRESS_PAY,
    getAuthorizedHeader,
    requestError
} from './types';

// 首页

export function fetctHome() {
    return function (dispatch) {
        axios.get(`${ROOT_URLL}/notice/query/random`, { headers: getAuthorizedHeader() })
            .then(response => {
                console.log(response);
                dispatch({ type: FETCH_HOME, payload: response });
            })
            .catch(err => {
                dispatch(requestError(err.message));
            });
    };
}


// 发布公告

export function releaseAdvert({ userId, loginname, noticeType, location, currency, premium, price, minPrice, minTxLimit, maxTxLimit, payType, noticeContent }, callback) {
    return function (dispatch) {
        axios.post(`${ROOT_URLL}/notice/broadcast`, { userId, loginname, noticeType, location, currency, premium, price, minPrice, minTxLimit, maxTxLimit, payType, noticeContent }, { headers: getAuthorizedHeader() })
            .then(response => {
                console.log(response);
                if (response.data.status == 1) {
                    callback();
                } else {
                    callback(response.data.message);
                }
                // dispatch({type: FETCH_ADVERT, payload: response})
            })
            .catch(err => {
                dispatch(requestError(err.message));
            });
    };
}
// 购买比特币搜索
// 搜索广告

export function fetcAdvertSeach({ searchType, location, currency, payType, pageNum }, callback) {
    return function (dispatch) {
        axios.post(`${ROOT_URLL}/notice/search/page/buy`, { searchType, location, currency, payType, pageNum }, { headers: getAuthorizedHeader() })
            .then(response => {
                console.log(response);
                dispatch({ type: FETCH_BUY_SECAT, payload: response });
            })
            .catch(err => {
                dispatch(requestError(err.message));
            });
    };
}



// 购买比特币广告详情

export function fetctBuyBtcDetail({ noticeId }, callback) {
    return function (dispatch) {
        axios.post(`${ROOT_URLZ}/order/findUserTxDetailAndNotice`, { noticeId }, { headers: getAuthorizedHeader() })
            .then(response => {
                console.log(response);
                dispatch({ type: FETCH_BUY_BTC_DETAIL, payload: response });
            })
            .catch(err => {
                dispatch(requestError(err.message));
            });
    };
}

// 购买比特币详情下单

export function fetctBuynow({ formdata }, callback) {
    return function (dispatch) {
        axios({
            method: 'post',
            url: `${ROOT_URLZ}/order/addOrder `,
            data: formdata,
            headers: getAuthorizedHeader()
        }).then(response => {
            console.log(response);
            dispatch({ type: FETCH_BUY_NOW, payload: response });
            if (response.data.status == 1) {
                callback();
            } else {
                callback(response.data.message);
            }
        })
            .catch(err => {
                dispatch(requestError(err.message));
            });
    };
}

// 出售比特币详情下单
export function fetctSellnow({ formdata }, callback) {
    return function (dispatch) {
        axios({
            method: 'post',
            url: `${ROOT_URLZ}/order/addOrder `,
            data: formdata,
            headers: getAuthorizedHeader()
        }).then(response => {
            console.log(response);
            dispatch({ type: FETCH_SELL_NOW, payload: response });
            if (response.data.status == 1) {
                callback();
            } else {
                callback(response.data.message);
            }
        })
            .catch(err => {
                dispatch(requestError(err.message));
            });
    };
}

// 出售比特币广告详情

export function fetctSellBtcDetail({ noticeId }, callback) {
    return function (dispatch) {
        axios.post(`${ROOT_URLZ}/order/findUserTxDetailAndNotice`, { noticeId }, { headers: getAuthorizedHeader() })
            .then(response => {
                console.log(response);
                dispatch({ type: FETCH_SELL_BTC_DETAIL, payload: response });
            })
            .catch(err => {
                dispatch(requestError(err.message));
            });
    };
}

// 出售比特币搜索

export function fetctSellSeach({ searchType, location, currency, payType, pageNum }, callback) {
    return function (dispatch) {
        axios.post(`${ROOT_URLL}/notice/search/page/sell`, { searchType, location, currency, payType, pageNum }, { headers: getAuthorizedHeader() })
            .then(response => {
                console.log(response);
                dispatch({ type: FETCH_SELL_SECAT, payload: response });
            })
            .catch(err => {
                dispatch(requestError(err.message));
            });
    };
}
// 选择框数据

export function fetctArray() {
    return function (dispatch) {
        axios.get(`${ROOT_URLL}/notice/query/statusKV`, { headers: getAuthorizedHeader() })
            .then(response => {
                console.log(response);
                dispatch({ type: FETCH_ARRAY, payload: response });
            })
            .catch(err => {
                dispatch(requestError(err.message));
            });
    };
}

// 我的广告

export function fetctMyAdvert({ userId, noticeType, txStatus, pageNum }, callback) {
    console.log(` 我的广告: ${userId},${noticeType},${txStatus},${pageNum}`);
    return function (dispatch) {
        axios.get(`${ROOT_URLL}/notice/query/me2?userId=${userId}&pageNum=${pageNum}&noticeType=${noticeType}&txStatus=${txStatus}`, { headers: getAuthorizedHeader() })
            .then(response => {
                console.log(response);
                dispatch({ type: FETCH_MY_ADVERT, payload: response });
                if (response.data.status == 1) {
                    callback();
                } else {
                    callback(response.data.message);
                }
            })
            .catch(err => {
                dispatch(requestError(err.message));
            });
    };
}

// 下架我的广告

export function fetctOffMyAd({ id }, callback) {
    // console.log(`下架我的广告:${id} `);
    return function (dispatch) {
        axios.get(`${ROOT_URLL}/notice/stop?id=${id}`, { headers: getAuthorizedHeader() })
            .then(response => {
                console.log(response);
                // dispatch({type: FETCH_OFF_MYBTC, payload: response})
                if (response.data.status == 1) {
                    callback();
                } else {
                    callback(response.data.message);
                }
            })
            .catch(err => {
                dispatch(requestError(err.message));
            });
    };
}

//用户基本信息

export function fetctBaseInfo({ formdata }, callback) {
    return function (dispatch) {
        axios({
            method: 'post',
            url: `${ROOT_URLC}/user/info `,
            data: formdata,
            headers: { 'content-type': 'multipart/form-data', getAuthorizedHeader },
            withCredentials: true
        }).then(response => {
            // console.log(response);
            // dispatch({type: FETCH_BASE_INFO, payload: response})
            if (response.data.status == 1) {
                callback();
            } else {
                callback(response.data.message);
            }
        })
            .catch(err => {
                dispatch(requestError(err.message));
            });
    };
}
//用户中心受信任的

export function fetctTrusted({ userId, type, pageNo, pageSize }, callback) {
    return function (dispatch) {
        axios.get(`${ROOT_URLC}/user/trust?userId=${userId}&pageNo=${pageNo}&pageSize=${pageSize}&type=${type}`,
            { headers: getAuthorizedHeader() }).then(response => {
                // console.log(response);
                dispatch({ type: FETCH_TRUSTED, payload: response });

            })
            .catch(err => {
                dispatch(requestError(err.message));
            });
    };
}
//用户中心 收款地址

export function fetctAddressPay({loginname, firstAddress}, callback) {
    return function (dispatch) {
        axios.post(`${ROOT_URLC}/user/btcaddress?loginname=${loginname}&firstAddress=${firstAddress}`, { headers: getAuthorizedHeader() })
        .then(response => {
            console.log(response);
                dispatch({ type: FETCH_ADDRESS_PAY, payload: response });
                if (response.data.status == 1) {
                    callback();
                } else {
                    callback(response.data.message);
                }
            })
            .catch(err => {
                dispatch(requestError(err.message));
            });
    };
}



//查看别人详细信息

export function LookOthersdetail({fromUserId, toUserId}, callback) {
    console.log(`查看别人详细信息:${fromUserId},${toUserId}`);
    return function (dispatch) {
        axios.get(`${ROOT_URLC}/user/relation?fromUserId=${fromUserId}&toUserId=${toUserId}`, { headers: getAuthorizedHeader() })
        .then(response => {
                console.log(response);
                dispatch({ type: OTHER_DETAIL, payload: response });
            })
            .catch(err => {
                dispatch(requestError(err.message));
            });
    };
}
//是否信任或屏蔽
export function isTrustAndisShield({fromUserId, toUserId, status}, callback) {
    console.log(`isTrustAndisShield:${fromUserId}, ${toUserId}, ${status}`);
    return function (dispatch) {
        axios.post(`${ROOT_URLC}/user/trust?fromUserId=${fromUserId}&toUserId=${toUserId}&status=${status}`, { headers: getAuthorizedHeader() })
        .then(response => {
                console.log(response);
                dispatch({ type: ISTRUST_OR_ISSHIELD, payload: response });

            })
            .catch(err => {
                dispatch(requestError(err.message));
            });
    };
}