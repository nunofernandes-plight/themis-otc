/**
 * Created by oxchain on 2017/10/20.
 */
import React, { Component } from 'react';
import { Route, Redirect } from 'react-router-dom';
import { connect } from 'react-redux';
import { Modal, Button } from 'antd';
import { GetverifyCodePhone, ChangePhoneSave, ChangePasswordSave, signoutUser } from '../actions/auth';
class Safeset extends Component {
    constructor(props) {
        super(props);
        this.state = {
            loading: false,
            visible: false,
            loadingpsw: false,
            visiblepsw: false,
            count: 60,
            liked: true,
        };
        this.handlesend = this.handlesend.bind(this);
    }
    showModal = () => {
        this.setState({
            visible: true,
        });
    }
    showModalPSW = () => {
        this.setState({
            visiblepsw: true,
        });
    }
    handleOk = () => {
        const loginname = localStorage.getItem("loginname");
        const mobilephone = localStorage.getItem("phonenum");

        this.props.ChangePhoneSave({ loginname, mobilephone }, err => {
            this.setState({ loading: true });
            setTimeout(() => {
                this.setState({ loading: false, visible: false });
            }, 3000);
        });
    };
    handleOkPSW = () => {
        const loginname = localStorage.getItem("loginname");
        const password = this.refs.password.value;
        const newPassword = this.refs.newPassword.value;
        const newPasswordagain = this.refs.newPasswordagain.value;

        if (newPassword === newPasswordagain) {
            this.props.ChangePasswordSave({ loginname, password, newPassword }, err => {
                this.setState({ loadingpsw: true });
                setTimeout(() => {
                    this.setState({ loadingpsw: false, visiblepsw: false });
                }, 1000);
            });
        } else {
            alert('两次新密码输入不一致');
        }
        this.props.signoutUser();
    };
    handleCancel = () => {
        this.setState({ visible: false });
    };

    handleCancelPSW = () => {
        this.setState({ visiblepsw: false });
    };
    handlesend() {
        const loginname = localStorage.getItem("loginname");
        const mobilephone = localStorage.getItem("mobilephone");
        if (this.state.liked && mobilephone) {
            this.timer = setInterval(function () {
                var count = this.state.count;
                this.state.liked = false;
                count -= 1;
                if (count < 1) {
                    this.setState({
                        liked: true
                    });
                    count = 60;
                    clearInterval(this.timer);
                }
                this.setState({
                    count: count
                });
            }.bind(this), 1000);
            this.props.GetverifyCodePhone({ loginname, mobilephone });
        }else{
            alert("请先输入手机号");
        }
    }
    phoneChange(e) {
        // console.log(e.target.value);
        const mobilephone = localStorage.setItem("mobilephone", e.target.value);
        var regex = /^1[3|4|5|7|8][0-9]\d{4,8}$/;
        if (regex.test(e.target.value)) {
        } else {
            alert('请输入正确的手机号码！');
        }
    }
    render() {
        var text = this.state.liked ? '发送验证码' : this.state.count + ' s ';
        const { visible, loading, visiblepsw, loadingpsw } = this.state;

        return (
            <div >
                <div className="changeStyle">
                    <span className="fa fa-mobile"></span>
                    <div className="bindPhone">
                        <p>绑定手机</p>
                        <p className="bindinfo">提现,修改密码,及安全设置时用以收验证短信</p>
                    </div>
                    <Button className="changePhone" onClick={this.showModal}>修改</Button>
                    <Modal
                        visible={visible}
                        title="请输入新的手机号"
                        onOk={this.handleOk}
                        onCancel={this.handleCancel}
                        footer={[
                            <Button className="confirmStyle" key="submit" type="primary" size="large" loading={loading} onClick={this.handleOk}>
                                确认
                           </Button>,
                        ]}
                    >
                        <div className="modalInput">
                            <input className="formChange" type="text" placeholder="请输入新的手机号码" onBlur={this.phoneChange} />
                            <div className="Verifycodewidth">
                                <input className="formVerifycode " type="text" placeholder=" 请输入验证码" />
                                <span className={`send-testcode  ${this.state.liked ? "" : "time-color"}`} onClick={this.handlesend}>{text}</span>
                            </div>
                        </div>

                    </Modal>
                </div>
                <div className="changeStyle">
                    <span> <i className="fa fa-unlock-alt"></i></span>
                    <div className="bindPhone">
                        <p>登录密码</p>
                        <p className="bindinfo">用于登录账户时输入</p>
                    </div>
                    <Button className="changePhone" onClick={this.showModalPSW}>修改</Button>
                    <Modal
                        visible={visiblepsw}
                        title="修改登录密码"
                        onOk={this.handleOkPSW}
                        onCancel={this.handleCancelPSW}
                        footer={[
                            <Button className="confirmStyle" key="submit" type="primary" size="large" loading={loadingpsw} onClick={this.handleOkPSW}>
                                确认
                            </Button>,
                        ]}
                    >
                        <input className="formChange" type="text" placeholder="请输入旧密码" ref="password" />
                        <input className="formChange " type="password" placeholder=" 请输入新密码" ref="newPassword" />
                        <input className="formChange " type="password" placeholder=" 确认新密码" ref="newPasswordagain" />
                    </Modal>
                </div>

            </div>
        );
    }
}



function mapStateToProps(state) {
    // console.log(state.auth.authenticated);
    return {
        all: state.auth.all,
        authenticated: state.auth.authenticated
    };
}
export default connect(mapStateToProps, { GetverifyCodePhone, ChangePhoneSave, ChangePasswordSave, signoutUser })(Safeset);
