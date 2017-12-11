/**
 * Created by oxchain on 2017/10/26.
 */


import React, { Component } from 'react';
import { Field } from 'redux-form';
import { connect } from 'react-redux';
import { EmailsigninAction } from '../../actions/auth';
import { Route, Redirect } from 'react-router-dom';
import { Alert } from 'antd';
import { Link } from 'react-router-dom';
class Emiallogin extends Component {
    // let Emiallogin = React.createClass({
    handleEmailSubmit() {
        const email = this.refs.email.value;
        const password = this.refs.password.value;
        if (email && password)
            this.props.EmailsigninAction({ email, password }, () => { });
    }
    renderAlert() {
        const { from } =  { from: { pathname: '/' } };
        if (this.props.loggedIn) {
            // location.reload();
            return (
                <Redirect to={from} />
            );
        } else if (this.props.errorMessage){
            const text = this.props.errorMessage == 'Network Error'?"网络连接错误":this.props.errorMessage == "Request failed with status code 500"?"服务器错误":this.props.errorMessage;
            return (
                <Alert message= {text} type="error" showIcon />
            );
        }
    }
    render() {
        return (
            <div className="login-box">
                <div className="login-box-body">
                    <div className="signinWay text-center g-pt-50">
                        <ul className="row loginul">
                            <li className="col-xs-6 loginli"> <Link className="g-pb-3" to="/signin">手机登录</Link></li>
                            <li className="col-xs-6 loginli"><Link className="signinTypeBar g-pb-3" to="/signinemail">邮箱登录</Link></li>
                        </ul>
                    </div>
                    <div className="form-style">
                        <div className="form-signin" >
                            <input className="input inputwidth form-group" type="text" placeholder="请输入邮箱" ref="email" /> <br />
                            <input className="input inputwidth form-group" type="password" placeholder="请输入密码" ref="password" /><br />
                            <div className="form-group">
                                <button className="btn form-login" onClick={this.handleEmailSubmit.bind(this)}>登录</button>
                            </div>
                            <div className="form-group forgetpwd">
                                <a className="" href="/forgetpsw">忘记密码 ?</a>
                            </div>
                            {this.renderAlert()}
                        </div>
                    </div>
                </div>
            </div>);
    }
}


function mapStateToProps(state) {
    return {
        loggedIn: state.auth.authenticated,
        errorMessage: state.auth.error
    };
}
export default connect(mapStateToProps, { EmailsigninAction })(Emiallogin);