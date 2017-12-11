/**
 * Created by oxchain on 2017/10/18.
 */
import React, { Component } from 'react';
import { Route, Redirect } from 'react-router-dom';
import { connect } from 'react-redux';
import { signinAction } from '../../actions/auth';
import { Alert } from 'antd';
import { Link } from 'react-router-dom';
class Signin extends Component {
    constructor(props) {
        super(props);
        this.state = {};
    }
    handlePhoneSubmit() {
        const mobilephone = this.refs.loginname.value;
        const password = this.refs.password.value;
        if (mobilephone && password) {
            this.props.signinAction({ mobilephone, password });
        }
    }
    renderAlert() {
        const { from } =  { from: { pathname: '/' } };
        if (this.props.loggedIn) {
            // location.reload();
            // alert('密码错误');
            return (
                <Redirect to={from} />
            );
        } else if (this.props.errorMessage){
            const text = this.props.errorMessage == 'Network Error'?"网络连接错误":this.props.errorMessage == "Request failed with status code 500"?"服务器繁忙 稍后重试":this.props.errorMessage;
            return (
                <Alert message= {text} type="error" showIcon />
            );
        }
    }
    render() {
        console.log(this.props.errorMessage);
        return (
            <div className="mainbgc">
                <div className="login-box">
                    <div className="login-box-body">
                        <div className=" signinWay text-center g-pt-50">
                            <ul className="row loginul">
                                <li className="col-xs-6 loginli"> <Link className="signinTypeBar g-pb-3" to="/signin">手机登录</Link></li>
                                <li className="col-xs-6 loginli"><Link className=" g-pb-3" to="/signinemail">邮箱登录</Link></li>
                            </ul>
                        </div>
                        <div className="form-style">
                            <div className="form-signin"  >
                                <select name="" id="" className="input inputwidth form-group"> +86
                                    <option value="1">中国 + 86</option>
                                    <option value="2">美国 + 1</option>
                                    <option value="3">英国 + 44</option>
                                    <option value="4">日本 + 81</option>
                                </select>
                                <input className="input inputwidth form-group" type="text" placeholder="请输入手机号" ref="loginname" /> <br />
                                <input className="input inputwidth form-group" type="password" placeholder="请输入密码" ref="password" /><br />
                                <div className="form-group">
                                    <button className="btn form-login" onClick={this.handlePhoneSubmit.bind(this)}>登录</button>
                                </div>
                                <div className="form-group forgetpwd">
                                    <a className="" href="/forgetpsw">忘记密码 ?</a>
                                </div>
                                {this.renderAlert()}
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        );
    }
}
function mapStateToProps(state) {
    // console.log(state.auth.error);
    return {
        loggedIn: state.auth.authenticated,
        errorMessage: state.auth.error
    };
}
export default connect(mapStateToProps, { signinAction })(Signin);






