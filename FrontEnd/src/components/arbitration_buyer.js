/**
 * Created by zhangxiaojing on 2017/10/24.
 */
import React, {Component} from 'react';
import {connect} from 'react-redux';
import Chat from './chat';
import {fetchOrdersDetails } from '../actions/order';

class ArbitrationBuyer extends Component{
    constructor(props) {
        super(props);
        this.orderMessageDetails=this.orderMessageDetails.bind(this);
    }
    componentWillMount() {
        const partnerName = localStorage.getItem("friendUsername");
        const userId = localStorage.getItem("userId");
        const message = this.props.location.state;
        const data = {id: message.id, userId: userId};
        this.setState({partnerName: partnerName});
        console.log(data);
        this.props.fetchOrdersDetails({data});
    }
    goBack(){
        history.back();
    }
    orderMessageDetails(msg){
        return(
            <div>
                <h4 className="h4">订单详情</h4>
                <hr/>
                <div>
                    <ul>
                        <li>交易数量:<span>{msg.quantity}</span>BTC</li>
                        <li>交易金额:<span>{msg.amount}</span>BTC</li>
                        <li>订单编号:<span>{msg.number}</span></li>
                        <li>支付方式:<span>{msg.way}</span></li>
                        <li>广告内容:<span>{msg.info}</span></li>
                    </ul>
                </div>
            </div>
        );
    }

    render(){
        const msg= {price:121, quantity:23, amount:232, number:3748937208457038, way:"支付宝", info:"100%信誉，在线10分钟迅速发货。"};
        return(
            <div className=" container g-pt-100 g-pb-100">
                <div className="row arbitration-buyer">
                    <div className="back g-pt-10 g-pb-20">
                        <span className="return" style={{"cursor":"pointer"}} onClick={this.goBack.bind(this)}><i className="fa fa-arrow-left g-pr-5" aria-hidden="true"></i>返回</span>
                    </div>
                    <div className="arbitration-buyer-box clearfix">
                        <div className="col-sm-8">
                            <h4 className="arbitration-buyer-box-title h4">THEMIS仲裁</h4>
                            <Chat/>
                        </div>
                        <div className="col-sm-4">
                            {this.orderMessageDetails(msg)}
                        </div>
                    </div>
                </div>
            </div>

        );
    }
}

function mapStateToProps(state) {
    return {
        orders_details: state.order.orders_details
    };
}

export default connect(mapStateToProps, {fetchOrdersDetails})(ArbitrationBuyer);