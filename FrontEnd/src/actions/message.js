/**
 * Created by zhangxiaojing on 2017/11/8.
 */
import axios from 'axios';
import {
    ROOT_MESSAGE,
    FETCH_MESSAGES_NUMBER,
    FETCH_MESSAGE_NOTICE,
    FETCH_MESSAGE_SYSTEM,
    FETCH_MESSAGE_LETTER,
    REQUEST_ERROR,
    requestError,
    getAuthorizedHeader,
} from './types';

/**
 * 查询未读消息数量
 */
export function fetchMessageNumber({userId}){
    let tip=1;
    return function message(dispatch) {
        axios.get(`${ROOT_MESSAGE}/message/query/unReadCount?userId=${userId}&tip=${tip}`, { headers: getAuthorizedHeader() })
            .then(response => {
                dispatch({type: FETCH_MESSAGES_NUMBER, payload: response.data.data});
                tip=2;
                message(dispatch);
            })
            .catch(err => dispatch(requestError(err.message)));
    };
}
/**
 * 查询公告消息列表
 */
export function fetchMessageNotice({userId, pageNum, pageSize}){
    return function(dispatch) {
        axios.get(`${ROOT_MESSAGE}/message/query/noticeMsg?userId=${userId}&pageNum=${pageNum}&pageSize=${pageSize}`, { headers: getAuthorizedHeader() })
            .then(response => {
                dispatch({type: FETCH_MESSAGE_NOTICE, payload: response.data.data});
            })
            .catch(err => dispatch(requestError(err.message)));
    };
}
/**
 * 查询系统消息列表
 */
export function fetchMessageSystem({userId, pageNum, pageSize}){
    return function(dispatch) {
        axios.get(`${ROOT_MESSAGE}/message/query/globalMsg?userId=${userId}&pageNum=${pageNum}&pageSize=${pageSize}`, { headers: getAuthorizedHeader() })
            .then(response => {
                dispatch({type: FETCH_MESSAGE_SYSTEM, payload: response.data.data});
            })
            .catch(err => dispatch(requestError(err.message)));
    };
}
/**
 * 查询私信消息列表
 */
export function fetchMessageLetter({userId, pageNum, pageSize}){
    return function(dispatch) {
        axios.get(`${ROOT_MESSAGE}/message/query/privateMsg?userId=${userId}&pageNum=${pageNum}&pageSize=${pageSize}`, { headers: getAuthorizedHeader() })
            .then(response => {
                dispatch({type: FETCH_MESSAGE_LETTER, payload: response.data.data});
            }).catch((err) => {
                dispatch(requestError(err.message));
            });

    };
}