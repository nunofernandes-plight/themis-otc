/**
 * Created by oxchain on 2017/10/17.
 */

import React, { Component } from 'react';
import { Link } from 'react-router-dom';
import { Badge } from 'antd';
import { connect } from 'react-redux';
import { fetchMessageNumber } from "../../actions/message";
import { signinAction } from '../../actions/auth';

class Header extends Component {
    constructor(props) {
        super(props);
        this.state = {};
        this.renderUserInfo = this.renderUserInfo.bind(this);
    }
    componentWillMount() {
        const userId = localStorage.getItem("userId");
        if (this.props.authenticated) {
            this.props.fetchMessageNumber({ userId });
        }
    }
    renderUserInfo() {
        const role = localStorage.getItem('role');
        let allUnRead=this.props.message_number && this.props.message_number.allUnRead;
        if (this.props.authenticated) {
            const loginname = localStorage.getItem('loginname');
            return (
                <div className="navbar-custom-menu">
                    <ul className="nav navbar-nav">
                        {role == 3 ? <li className="order-style" style={{ width: "135px" }}><Link to="/refereelist">仲裁列表</Link></li> : ""}
                        <li className="order-style">
                            <Link to="/message/notice">
                                消息<Badge count={allUnRead} />
                            </Link>
                        </li>
                        <li className="order-style"><Link to="/order/inprogress">订单</Link></li>
                        <li className="order-style"><Link to="/order/inprogress">钱包</Link></li>
                        <li className="ordermenu-style dropdown user user-menu">
                            <a href="#" className="dropdown-toggle" data-toggle="dropdown">
                                <span className="hidden-xs">{loginname}</span>
                            </a>
                            <ul className="dropdown-menu">
                                <li className="info-self">
                                    <div className="info-style">
                                        <Link to="/usercenter" >用户中心</Link>
                                    </div>
                                    <div className="info-style">
                                        <Link to="/myadvert" >我的广告</Link>
                                    </div>
                                    <div className="info-style">
                                        <Link to="/signout" >退出登录</Link>
                                    </div>
                                </li>
                            </ul>
                        </li>
                    </ul>
                </div>
            );
        }
    }
    render() {
        const username = localStorage.getItem('username');
        return (
            <div className="headerwidth">
                <nav className="header ">
                    <div className="header-position">
                        <div className="navdivimg">
                            <img src="/public/img/logo4.png" className="navimg" alt="" />
                        </div>
                        <ul className="headerul" >
                            <li ><a href="/" >首页</a></li>
                            <li ><Link to="/buybtc"  >购买比特币</Link></li>
                            <li ><Link to="/sellbtc" >出售比特币</Link></li>
                            <li ><Link to="/releaseadvert" >发布广告</Link></li>
                        </ul>
                    </div>
                    <div className={`navbar-custom-menu ${this.props.authenticated ? "hidden" : ""}`}>
                        <ul className="nav navbar-nav">
                            <li className={`registerlia order-style `} ><Link to="/signup" >注册</Link></li>
                            <li className={`loginlia order-style `}><Link to="/signin"  >登录</Link></li>
                        </ul>
                    </div>
                    {this.renderUserInfo()}
                </nav>
            </div>
        );
    }
}
function mapStateToProps(state) {
    return {
        errorMessage: state.auth.error,
        authenticated: state.auth.authenticated,
        message_number: state.message.message_number
    };
}

export default connect(mapStateToProps, { fetchMessageNumber, signinAction })(Header);
