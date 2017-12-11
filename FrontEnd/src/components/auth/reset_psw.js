/**
 * Created by oxchain on 2017/11/09.
 */
import React, { Component } from 'react';
import { Field } from 'redux-form';
import { connect } from 'react-redux';
import { ResetpswAction } from '../../actions/auth';
import { Modal, Button } from 'antd';
import { Alert } from 'antd';
import { Route, Redirect } from 'react-router-dom';

class Resetpsw extends Component {
    constructor(props) {
        super(props);
        this.state = {};
    }
    handleEmailSubmit() {
        const resetkey = localStorage.getItem("email") ? localStorage.getItem("email") : localStorage.getItem("mobilephone");
        // console.log(resetkey);
        const newpassword = this.refs.newpassword.value;
        const password = this.refs.password.value;
        if (newpassword === password){
            this.props.ResetpswAction({ resetkey, password });

        }else{
            alert("两次密码输入不一致");
        }
    }
    renderAlert(){
        const data = this.props.all || [];
        const { from } =  { from: { pathname: '/signin' } };
        if(data.status == 1){
            return(
                <Redirect to={from} />
            );
        }else if(data.status == -1 ){
            return(
                <Alert message= {data.message} type="error" showIcon />
            );
        }
    }
    render() {
        return (
            <div className="login-box">
                <div className="login-box-body">
                    <div className="form-style">
                        <div className=" login-box-msg" style={{ fontSize: 24 + 'px' }}>重置密码</div>
                    </div>
                    <div className="form-style">
                        <div className="form-signin" >
                            <input className="input inputwidth form-group" type="password" placeholder="请输入新密码" ref="newpassword" /> <br />
                            <input className="input inputwidth form-group" type="password" placeholder="请再次输入新密码" ref="password" /><br />
                            <div className="form-group">
                                <button className="btn form-login" onClick={this.handleEmailSubmit.bind(this)}>确定</button>
                            </div>
                        </div>
                    </div>
                    {this.renderAlert()}
                </div>
            </div>);
    }
}


function mapStateToProps(state) {
    return {
        all: state.auth.all
    };
}
export default connect(mapStateToProps, { ResetpswAction })(Resetpsw);