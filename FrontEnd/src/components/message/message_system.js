/**
 * Created by zhangxiaojing on 2017/11/10.
 */

import React, { Component } from 'react';
import { Link } from 'react-router-dom';
// import {Pagination} from 'antd';
import { Pagination } from 'nl-design';
import { Badge } from 'antd';
import { connect } from 'react-redux';
import {fetchMessageSystem} from "../../actions/message";
class MessageSystem extends Component{
    constructor(props) {
        super(props);
        this.state = {
            pageNum:1,
            pageSize:8
        };
    }
    componentWillMount(){
        const userId=localStorage.getItem("userId");
        const pageNum=this.state.pageNum;
        const pageSize=this.state.pageSize;
        this.props.fetchMessageSystem({userId, pageNum, pageSize});
    }
    handlePagination(pageNum) {
        const userId=localStorage.getItem("userId");
        const pageSize=this.state.pageSize;
        this.props.fetchMessageSystem({userId, pageNum, pageSize});
    }
    renderList(){
        return this.props.message_system.pageList.map((item, index)=>{
            return(
                <li className="message-item-list clearfix" key={index}>
                    <div className="col-xs-2">
                        <div className="photo pull-right">
                            <img src="/public/img/touxiang.png" alt=""/>
                        </div>
                    </div>
                    <div className="col-xs-10 message-item-content">
                        <div className="message-item-tip"><span>{item.messageType == 2 ? "公告" :""}</span><span>{item.postDate}</span></div>
                        <Link className={`message-item-detail ${item.readStatus == 2  ? "readed":"" }`} to={`/order/progress/${item.orderId}`}>{item.content}</Link>
                    </div>
                </li>
            );
        });
    }
    render(){
        const totalNum = this.props.message_system && this.props.message_system.rowCount;
        const noticeUnRead=this.props.message_number && this.props.message_number.noticeUnRead;
        const globalUnRead=this.props.message_number && this.props.message_number.globalUnRead;
        const privateUnRead=this.props.message_number && this.props.message_number.privateUnRead;
        return (
            <div className="message-box">
                <div className="container">
                    <div className="row">
                        <div className="col-xs-12 message-list">
                            <ul>
                                <li className="col-xs-4 text-right">
                                    <Link className="text-center" to="/message/notice">公告 <Badge count={noticeUnRead} /></Link>
                                </li>
                                <li className="col-xs-4 text-center">
                                    <Link className="text-center active" to="/message/system">系统 <Badge count={globalUnRead} /></Link>
                                </li>
                                <li className="col-xs-4 text-left">
                                    <Link className="text-center" to="/message/letter">私信 <Badge count={privateUnRead} /></Link>
                                </li>
                            </ul>
                        </div>
                        <div className="col-xs-12 message-item-content">
                            <ul>
                                { totalNum == 0 || !this.props.message_system  ? <li className="text-center h4 no-message">目前没有新消息</li> : this.renderList()}
                            </ul>
                        </div>
                        {
                            totalNum == 0 || !this.props.message_system?'':
                                <div className="col-xs-12">
                                    <div className="pagecomponent">
                                        <Pagination  defaultPageSize={this.state.pageSize} total={totalNum}  onChange={e => this.handlePagination(e)}/>
                                    </div>
                                </div>
                        }
                    </div>
                </div>
            </div>
        );
    }
}
function mapStateToProps(state) {
    return {
        message_system:state.message.message_system,
        message_number: state.message.message_number
    };
}
export default connect(mapStateToProps, {fetchMessageSystem})(MessageSystem);