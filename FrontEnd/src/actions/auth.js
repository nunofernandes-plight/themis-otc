/**
 * Created by oxchain on 2017/10/18.
 */
import axios from 'axios';
import { browserHistory, hashHistory } from 'react-router';
import {
    ROOT_URLC,
    AUTH_USER,
    UNAUTH_USER,
    AUTH_ERROR,
    FETCH_VERIFY_CODE,
    FETCH_VERIFY_CODE_PHONE,
    FETCH_PHONE,
    FETCH_PASSWORD,
    EMAIL_FIND_PSW,
    RESET_PSW,
    PHONE_FIND_PSW,
    EMAIL_SIGNUP_USER,
    EMAIL_ISLIVE,
    getAuthorizedHeader
} from './types';

export function authError(error) {
    return {
        type: AUTH_ERROR,
        payload: error
    };
}

function setAuthToLocalStorage(data) {
    localStorage.setItem('token', data.token);
    localStorage.setItem('role', data.role.id);
    localStorage.setItem('userId', data.id); //用户ID
    localStorage.setItem('loginname', data.loginname); //用户登录名
    localStorage.setItem('mobilephone', data.mobilephone);//手机号
    localStorage.setItem('createTime', data.createTime);//注册时间
    localStorage.setItem('email', data.email);//邮箱
    localStorage.setItem('firstBuyTime', data.userTxDetail.firstBuyTime); //第一次交易时间
    localStorage.setItem('txNum', data.userTxDetail.txNum); //交易次数
    localStorage.setItem('believeNum', data.userTxDetail.believeNum);//信任人数
    localStorage.setItem('sellAmount', data.userTxDetail.sellAmount); //出售的累计交易数量
    localStorage.setItem('buyAmount', data.userTxDetail.buyAmount); //购买的累计交易数量
    localStorage.setItem('firstAddress', data.firstAddress); //用户中心 收款地址
}

/**
 * 手机登录
 */
export function signinAction({ mobilephone, password }) {
    console.log(`点击登录按钮传过来的数据是 ${mobilephone},${password}`);
    return function (dispatch) {
        axios.post(`${ROOT_URLC}/user/login`, { mobilephone, password })
            .then(response => {
                console.log(response);
                if (response.data.status == 1) {
                    setAuthToLocalStorage(response.data.data);
                    dispatch({ type: AUTH_USER });
                } else {
                    dispatch(
                        authError(response.data.message)
                    );
                    dispatch(authError(response.data.message));
                }
            })
            .catch((err) => {
                dispatch(authError(err.message));
            });
    };
}

/**
 * 邮箱登录
 */
export function EmailsigninAction({ email, password }) {
    console.log(`点击邮箱登录按钮传过来的数据是 ${email},${password}`);
    return function (dispatch) {
        axios.post(`${ROOT_URLC}/user/login`, { email, password })
            .then(response => {
                console.log(response);
                if (response.data.status == 1) {
                    setAuthToLocalStorage(response.data.data);
                    // browserHistory.push('/');
                    dispatch({ type: AUTH_USER });
                } else {
                    dispatch(authError(response.data.message));
                }
            })
            .catch((err) => {
                dispatch(authError(err.message));
            });
    };
}

// 登出
export function signoutUser() {
    localStorage.removeItem('token');
    localStorage.removeItem('userId');
    localStorage.removeItem('loginname');
    return { type: UNAUTH_USER };
}

/**
 * 手机注册
 */

export function signupUser({ loginname, mobilephone, vcode, password }, callback) {
    console.log(`手机注册传送的数据: ${loginname}, ${mobilephone},${vcode}, ${password}`);
    return function (dispatch) {
        axios.post(`${ROOT_URLC}/user/register`, { loginname, mobilephone, vcode, password })
            .then(response => {
                console.log(response);
                if (response.data.status == 1) {
                    callback();
                } else {
                    // console.log(response.data.message);
                    callback(response.data.message);
                }
            })
            .catch((err) => {
                dispatch(authError(err.message));
            });
    };
}
/**
 * 邮箱注册
 */
export function EmialsignupUser({ loginname, email, password }, callback) {
    console.log(`邮箱注册传送的数据: ${loginname}, ${email}, ${password}`);
    return function (dispatch) {
        axios.post(`${ROOT_URLC}/user/register`, { loginname, email, password })
            .then(response => {
                console.log(response);
                if (response.data.status == 1) {
                    callback();
                } else {
                    callback(response.data.message);
                }
                dispatch({ type: EMAIL_SIGNUP_USER, payload: response });
            })
            .catch((err) => {
                dispatch(authError(err.message));
            });
    };
}
/**
 * 注册获取验证码 && 手机找回获取验证码
 */

export function GetverifyCode({ mobilephone }) {
    console.log("点击发送验证码带过来的手机号" + mobilephone);
    return function (dispatch) {
        axios.get(`${ROOT_URLC}/user/phoneVcode?mobilephone=${mobilephone}`, { headers: getAuthorizedHeader() })
            .then(response => {
                console.log("获取验证码的接口通了");
                console.log(response);
                dispatch({ type: FETCH_VERIFY_CODE, payload: response });

            })
            .catch(err => (err.message));
    };
}
/**
 * 修改手机号获取验证码
 */

export function GetverifyCodePhone({ loginname, mobilephone }) {
    console.log("修改手机号获取验证码" + mobilephone, loginname);
    return function (dispatch) {
        axios.get(`${ROOT_URLC}/user/phoneVcode?loginname=${loginname}&mobilephone=${mobilephone}`, { headers: getAuthorizedHeader() })
            .then(response => {
                // console.log("修改手机号获取验证码的接口通了");
                console.log(response);
                dispatch({ type: FETCH_VERIFY_CODE_PHONE, payload: response });

            })
            .catch(err => (err.message));
    };
}


/**
 * 修改手机号
 */

export function ChangePhoneSave({ loginname, mobilephone }, callback) {
    console.log("修改手机号" + mobilephone, loginname);
    return function (dispatch) {
        axios.post(`${ROOT_URLC}/user/phone`, { loginname, mobilephone }, { headers: getAuthorizedHeader() })
            .then(response => {
                console.log(response);
                dispatch({ type: FETCH_PHONE, payload: response });
                if (response.data.status == 1) {
                    callback();
                } else {
                    callback(response.data.message);
                }

            })
            .catch(err => (err.message));
    };
}

/**
 * 修改密码
 */

export function ChangePasswordSave({ loginname, password, newPassword }, callback) {
    console.log("修改密码" + loginname, password, newPassword);
    return function (dispatch) {
        axios.post(`${ROOT_URLC}/user/password`, { loginname, password, newPassword }, { headers: getAuthorizedHeader() })
            .then(response => {
                console.log(response);
                dispatch({ type: FETCH_PASSWORD, payload: response });
                if (response.data.status == 1) {
                    callback();
                } else {
                    callback(response.data.message);
                }

            })
            .catch(err => (err.message));
    };
}

/**
 * 邮箱找回密码
 */

export function EmialAction({ email, vcode}, callback) {
    console.log("发送至邮箱" + email, vcode );
    return function (dispatch) {
        axios.get(`${ROOT_URLC}/user/sendVmail?vcode=${vcode}&key=${email}`, { headers: getAuthorizedHeader() })
            .then(response => {
                console.log(response);
                dispatch({ type: EMAIL_FIND_PSW, payload: response });
            })
            .catch(err => (err.message));
    };
}
/**
 * 手机找回密码
 */

export function PhoneAction({ mobilephone, vcode}, callback) {
    console.log("手机找回密码" + mobilephone, vcode );
    return function (dispatch) {
        axios.get(`${ROOT_URLC}/user/verifyICode?vcode=${vcode}&key=${mobilephone}`, { headers: getAuthorizedHeader() })
            .then(response => {
                console.log(response);
                dispatch({ type: PHONE_FIND_PSW, payload: response });
            })
            .catch(err => (err.message));
    };
}
/**
 * 重置密码
 */

export function ResetpswAction({resetkey, password}, callback) {
    console.log("重置密码" + resetkey, password );
    return function (dispatch) {
        axios.post(`${ROOT_URLC}/user/resetpwd?resetkey=${resetkey}&&password=${password} `, { headers: getAuthorizedHeader() })
            .then(response => {
                console.log(response);
                dispatch({ type: RESET_PSW, payload: response });
            })
            .catch(err => (err.message));
    };
}

/**
 * 邮箱注册提示激活成功与否
 */

export function RegisterJumptipAction({email}, callback) {
    console.log("邮箱注册是否激活提示" + email);
    return function (dispatch) {
        axios.get(`${ROOT_URLC}/user/active?email=${email} `, { headers: getAuthorizedHeader() })
            .then(response => {
                console.log(response);
                dispatch({ type: EMAIL_ISLIVE, payload: response });
            })
            .catch(err => (err.message));
    };
}

