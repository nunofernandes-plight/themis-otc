/**
 * Created by zhangxiaojing on 2017/10/31.
 */
import axios from 'axios';
import {
    ROOT_ARBITRATE,
    FETCH_ARBITRATE_LIST,
    UPLOAD_EVIDENCE,
    FETCH_EVIDENCE,
    ARBITRATE_RESULT,
    getAuthorizedHeader,
    requestError
} from './types';
/**
 * 获取仲裁人消息列表
 * @returns {Function}
 */

export function fetchArbitrateList({userIdDate}) {

    return function(dispatch) {
        axios({
            method:'post',
            url:`${ROOT_ARBITRATE}/arbitrate/findArbitrareOrderById`,
            data:userIdDate,
            headers: getAuthorizedHeader()
        }).then((res) => {
            if (res.data.status == 1) {
                dispatch({
                    type: FETCH_ARBITRATE_LIST,
                    payload: res.data
                });
            }
        }).catch( err => dispatch(requestError(err.message)) );
    };
}

/**
 * 提交仲裁凭证
 * @returns {Function}
 */


export function uploadEvidence({formData}, callback) {
    return function(dispatch) {
        axios({
            method:'post',
            url:`${ROOT_ARBITRATE}/arbitrate/uploadEvidence`,
            data:formData,
            headers: {'content-type': 'multipart/form-data', 'authorization': localStorage.getItem('token')},
            withCredentials: true
        }).then((res) => {
            if (res.data.status == 1) {
                dispatch({
                    type: UPLOAD_EVIDENCE,
                    payload: res.data.data

                });
            }
            callback(res.data);
        }).catch( err => dispatch(requestError(err.message)) );
    };
}

/**
 * 获取仲裁凭证
 * @returns {Function}
 */

export function fetchEvidence({orderId}) {
    return function(dispatch) {
        axios({
            method:'post',
            url:`${ROOT_ARBITRATE}/arbitrate/getEvidence`,
            data:orderId,
            headers: getAuthorizedHeader()
        }).then((res) => {
            if (res.data.status == 1) {
                dispatch({
                    type: FETCH_EVIDENCE,
                    payload: res.data.data
                });
            }
        }).catch( err => dispatch(requestError(err.message)) );
    };
}

/**
 * 仲裁
 * @returns {Function}
 */

export function arbitrateResult({resultData}) {
    return function(dispatch) {
        axios({
            method:'post',
            url:`${ROOT_ARBITRATE}/arbitrate/arbitrateOrderToUser`,
            data:resultData,
            headers: getAuthorizedHeader()
        }).then((res) => {
            if (res.data.status == 1) {
                dispatch({
                    type: ARBITRATE_RESULT,
                    payload: res.data.data
                });
            }
        }).catch( err => dispatch(requestError(err.message)) );
    };
}