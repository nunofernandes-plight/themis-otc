/**
 * Created by oxchain on 2017/11/09.
 */
import React, { Component } from 'react';
import { connect } from 'react-redux';
import { Route, Redirect } from 'react-router-dom';
import { EmialAction } from '../../actions/auth';
import { ROOT_URLC } from '../../actions/types';
import { Alert } from 'antd';
import { Link } from 'react-router-dom';
class Emialforget extends Component {
    constructor(props) {
        super(props);
        this.state = {
            Imgurl:''
        };
        this.handleSendEmail = this.handleSendEmail.bind(this);
    }
    handleEmailSubmit() {
        const email = this.refs.email.value;
        localStorage.setItem('email', email);
        if (email){
            var timestamp = Date.parse(new Date());
            this.setState({
                Imgurl : `${ROOT_URLC}/user/imgVcode?key=${email}&t=${timestamp}`
            });
           }else{
               alert('请先输入邮箱地址');
           }
        }

        handleSendEmail(){
            const email = this.refs.email.value;
            const vcode = this.refs.vcode.value;
            this.props.EmialAction({ email, vcode });
        }
        renderAlert(){
            const data = this.props.all || [];
            const { from } =  { from: { pathname: '/jumptip' } };
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
        const data = this.props.all || [];

        // localStorage.setItem("tip", data.data);
        localStorage.setItem("message", data.message);

        const srcurl =  this.state.Imgurl;

        return (
            <div className="login-box">
                <div className="login-box-body">
                    <div className="signinWay text-center g-pt-50">
                           <ul className="row loginul">
                                <li className="col-xs-6 loginli"> <Link className="g-pb-3" to="/forgetpsw">手机找回</Link></li>
                                <li className="col-xs-6 loginli"><Link className="signinTypeBar g-pb-3" to="/emailforget">邮箱找回</Link></li>
                            </ul>
                        {/* <ul className="row loginul">
                            <li className="col-xs-6 loginli"> <Link className="g-pb-3" to="/forgetpsw">手机找回</Link></li>
                            <li className="col-xs-6 loginli"><Link className="signinTypeBar g-pb-3" to="/emailforget">邮箱找回</Link></li>
                        </ul> */}
                    </div>
                    <div className="form-style">
                        <div className="form-signin" >
                            <input className="input inputwidth form-group" type="text" placeholder="请输入邮箱地址" ref="email" /> <br />
                            <input className="input vcode form-group" type="text" placeholder="请输入验证码" ref="vcode" />
                            <img src={srcurl ? srcurl : "./public/img/touxiang.png"} className="imgVcode" onClick={this.handleEmailSubmit.bind(this)} alt="" />
                            <div className="form-group">
                                <button className="btn form-login" onClick={this.handleSendEmail}>发送</button>
                            </div>
                        </div>
                    </div>
                    {this.renderAlert()}
                </div>
            </div>);
    }
}


function mapStateToProps(state) {
    console.log("666");
    return {
        all: state.auth.all
    };
}
export default connect(mapStateToProps, { EmialAction })(Emialforget);