/**
 * Created by oxchain on 2017/11/09.
 */
import React, { Component } from 'react';
import { Route, Redirect } from 'react-router-dom';
import { connect } from 'react-redux';
import { PhoneAction, GetverifyCode } from '../../actions/auth';
import { Alert } from 'antd';
import { Link } from 'react-router-dom';
class Forgetpsw extends Component {
    constructor(props) {
        super(props);
        this.state = {
            count: 60,
            liked: true,
        };
        this.handlesend = this.handlesend.bind(this);
        this.handlePhoneSubmit = this.handlePhoneSubmit.bind(this);
    }
    handlePhoneSubmit() {
        const mobilephone = this.refs.mobilephone.value;
        const vcode = this.refs.vcode.value;
        localStorage.setItem('mobilephone', mobilephone);
        if (mobilephone && vcode) {
            this.props.PhoneAction({ mobilephone, vcode });
        }
    }
    handlesend() {
        const mobilephone = localStorage.getItem("mobilephone");
        if (this.state.liked && mobilephone) {
            this.timer = setInterval(function () {
                var count = this.state.count;
                this.state.liked = false;
                count -= 1;

                // count < 1 ? this.setState({liked: true,  count : 60}) : this.setState({ count : count});

                if (count < 1) {
                    this.setState({
                        liked: true,
                        count : 60
                    });
                    clearInterval(this.timer);
                }else {
                    this.setState({
                        count: count
                    });
                }
            }.bind(this), 1000);
            this.props.GetverifyCode({ mobilephone }, () => { });
        }else{
            alert("请先输入手机号");
        }
    }
    phoneChange(e) {
       localStorage.setItem("mobilephone", e.target.value);

        var regex = /^1[3|4|5|7|8][0-9]\d{4,8}$/;
        if (regex.test(e.target.value)) {
        } else {
            alert('请输入正确的手机号码！');
        }
    }
    renderAlert(){
        const data = this.props.all || [];
        const { from } =  { from: { pathname: '/resetpsw' } };
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
        var text = this.state.liked ? '发送验证码' : this.state.count + ' s 后重新发';
        return (
            <div className="mainbgc">
                <div className="login-box">
                    <div className="login-box-body">
                        <div className=" signinWay text-center g-pt-50">
                        <ul className="row loginul">
                                <li className="col-xs-6 loginli"> <Link className="signinTypeBar g-pb-3" to="/forgetpsw">手机找回</Link></li>
                                <li className="col-xs-6 loginli"><Link className=" g-pb-3" to="/emailforget">邮箱找回</Link></li>
                            </ul>
                        </div>
                        <div className="form-style">
                            <div className="form-signin"  >
                                <select name="" id="" className="input inputwidth form-group"> +86
                                    <option value="1">中国 + 86</option>
                                    <option value="2">美国 + 22</option>
                                    <option value="3">英国 + 33</option>
                                    <option value="4">韩国 + 44</option>
                                </select>
                                <input className="input inputwidth form-group" type="text" onBlur={this.phoneChange} placeholder="请输入手机号" ref="mobilephone" /> <br />
                                <div className="form-style-test">
                                    <input  className="form-test " type="text" label="请输入验证码" ref="vcode" />
                                    <span className={`send-testcode  ${this.state.liked ? "" : "time-color"}`} onClick={this.handlesend}>{text}</span>
                                </div>
                                <div className="form-group">
                                    <button className="btn form-login" onClick={this.handlePhoneSubmit}>下一步</button>
                                </div>
                            </div>
                        </div>
                        {this.renderAlert()}
                    </div>
                </div>
            </div>
        );
    }
}
function mapStateToProps(state) {
    return {
        all: state.auth.data
    };
}
export default connect(mapStateToProps, { PhoneAction, GetverifyCode })(Forgetpsw);






