/**
 * Created by oxchain on 2017/12/01.
 */
import React, { Component } from 'react';
import { Field, reduxForm } from 'redux-form';
import { Link } from 'react-router-dom';
import { connect } from 'react-redux';
import { EmialsignupUser } from '../../actions/auth';
import { Route, Redirect } from 'react-router-dom';
import { ROOT_URLC } from '../../actions/types';
import { Alert } from 'antd';
class SignupEmial extends Component {
    constructor(props) {
        super(props);
        this.state = {
            spin: false,
        };
    }
    handleFormSubmit() {
        const loginname = this.refs.loginname.value;
        const email = this.refs.email.value;
        const password = this.refs.password.value;
        // console.log(loginname, email, password);
        if (loginname && email && password){
            this.props.EmialsignupUser({ loginname, email, password }, err => {
                this.setState({spin: false });
            });
          }
         }
         renderAlert(){
            const data = this.props.all || [];
            const { from } =  { from: { pathname: '/registerjumptip' } };
            if(data.status == 1){
                return(
                    <Redirect to={from} />
                );
            }else if(data.status == -1 ){
                return(
                    <Alert className="form-control-error" message= {data.message} type="error" showIcon />
                );
            }
        }
    render() {
        const data = this.props.all || [];
        console.log(data);
        const url = data.status === -1 ? "/signup" : "/signin";
        localStorage.setItem("registertip", data.message);
        return (
            <div className="login-box">
                    <div className="signinWay text-center g-pt-50">
                        <ul className="row loginul">
                            <li className="col-xs-6 loginli"> <Link className="g-pb-3" to="/signup">手机注册</Link></li>
                            <li className="col-xs-6 loginli"><Link className="signinTypeBar g-pb-3" to="/signupemail">邮箱注册</Link></li>
                        </ul>
                    </div>
                    <div className="login-box-body">
                        <div className="form-signin">
                            <input ref="loginname" className="form-control form-control-email" type="text" placeholder="请输入用户名" />
                            <input ref="email" className="form-control form-control-email" type="text" placeholder="请输入邮箱" />
                            <input ref="password" className="form-control form-control-email " type="password" placeholder="请输入密码" icon="lock" />
                            <div style={{width:85 +'%'}}>
                                <div className=" checkbox-margin">
                                    <input type="checkbox" defaultChecked className="checkbox-width" /><span> 我已阅读themis用户手册及相关法律</span>
                                </div>
                                <div className="">
                                    <button  className="btn  form-register" onClick={this.handleFormSubmit.bind(this)}><i className={`fa fa-spinner fa-spin ${this.state.spin ? '' : 'hidden'}`}></i> 注册</button>
                                </div>
                                <div className="form-group clicklogin">
                                    <a className="" href="/signin">已有账户 ? 点击登录</a>
                                </div>
                            </div>
                        </div>
                        {this.renderAlert()}
                    </div>
            </div>);
    }
}
function mapStateToProps(state) {
    // console.log(state.auth.all);
    return {
        all: state.auth.all,
        errorMessage: state.auth.error,
    };
}
export default connect(mapStateToProps, { EmialsignupUser })(SignupEmial);