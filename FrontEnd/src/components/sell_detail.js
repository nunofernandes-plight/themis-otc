/**
 * Created by oxchain on 2017/10/23.
 */

import React, { Component } from 'react';
import { Link } from 'react-router-dom';
import { Field } from 'redux-form';
import { connect } from 'react-redux';
import { fetctSellBtcDetail, fetctSellnow } from '../actions/releaseadvert';
import {
    Modal,
    ModalHeader,
    ModalTitle,
    ModalClose,
    ModalBody,
    ModalFooter
} from 'react-modal-bootstrap';

class Selldetail extends Component {
    constructor(props) {
        super(props);
        this.state = {
            isModalOpen: false,
            error: null,
            actionResult: '',
            messmoney: '',
            messnum: '',
        };
        this.handelChange = this.handelChange.bind(this);
        this.handleSubmit = this.handleSubmit.bind(this);
    }
    handelChange(e) {
        const data = this.props.all.notice || [];
        let type = e.target.name;
        if (type == "money") {
            this.setState({
                messmoney: e.target.value,
                messnum: (e.target.value) / data.price
            });
        } else if (type == "btc") {
            this.setState({
                messmoney: (e.target.value) * data.price,
                messnum: e.target.value
            });
        }
    }
    hideModal = () => {
        this.setState({
            isModalOpen: false
        });
    };
    componentWillMount() {
        const noticeId = this.props.match.params.id;
        // console.log(this.props.match.params.id);
        this.props.fetctSellBtcDetail({ noticeId });
    }

    handleSubmit() {
        const formdata = {
            userId: localStorage.getItem("userId"),
            noticeId: this.props.match.params.id,
            money: this.state.messmoney,
            amount: this.state.messnum
        };
        if(this.props.authenticated){
            this.props.fetctSellnow({ formdata }, err => {
                this.setState({ isModalOpen: true, error: err, actionResult: err || '下单成功!' });
            });
        }else {
            alert("请先登录!");
        }
    }

    renderAlert() {
        const { from } = this.props.location.state || { from: { pathname: '/' } };
        if (this.props.data) {
            return (
                <Redirect to={from} />
            );
        } else if (this.props.data) {
            return (
                <div className="alert alert-danger alert-dismissable">
                    {this.props.errorMessage}
                </div>
            );
        }
    }
    showOrderDetail(item) {
        const userId = localStorage.getItem('userId');
        const orderData = { id: item.id, userId: userId, partnerId: item.sellerId == userId ? item.buyerId : item.sellerId };
        localStorage.setItem("partner", JSON.stringify(orderData));
        window.location.href = '/order/progress';
    }
    render() {
        const userId = localStorage.getItem("userId");
        const messmoney = this.state.messmoney;
        const messnum = this.state.messnum;
        const data = this.props.all.notice || [];
        const datanum = this.props.all ||[];
        const time = data.validPayTime / 1000 / 60;
        const imgUrl = 'http://192.168.1.200/v1/tfs/' + datanum.imageName;
        return (
            <div className="maincontent">
                <div className="detail-title">
                    <div className="detailTitle" style={{ padding: 0 }}>

                        <img src={imgUrl} style={{ width: 100 + 'px', borderRadius: 50 + '%' }} alt="" />
                        <h4 style={{ marginBottom: 10 + 'px', paddingLeft: 15 + 'px' }}>{datanum.loginname}</h4>
                        <ul className="detailul">
                            <li>
                                <p>{datanum.txNum}</p>
                                <p>交易次数</p>
                            </li>
                            <li>
                                <p>{datanum.believeNum}</p>
                                <p>信任人数</p>
                            </li>
                            <li>
                                <p>{datanum.goodDegree}</p>
                                <p>好评度</p>
                            </li>
                            <li>
                                <p>{datanum.successCount} BTC</p>
                                <p>历史成交数</p>
                            </li>
                        </ul>

                    </div>
                </div>
                <div className="price-detail clear">
                    <div className="col-lg-9 col-xs-9 col-md-9 detailPrice">
                        <div>
                            <ul className="priceul">
                                <li>报价 : &#x3000;&#x3000;&#x3000;&#x3000;&#x3000;{data.price}CNY/BTC</li>
                                <li>交易额度 : &#x3000;&#x3000;&#x3000;{data.minTxLimit}-{data.maxTxLimit} CNY</li>
                                <li>付款方式 : &#x3000;&#x3000;&#x3000;{data.payType == 1 ? "现金" : data.payType == 2 ? "转账" : data.payType == 3 ? "支付宝" : data.payType == 4 ? "微信" : data.payType == 5 ? "Apple Pay" : ""}</li>
                                <li>付款期限 : &#x3000;&#x3000;&#x3000;{time}分钟</li>
                            </ul>
                            <h4 className="sellwhat">你想出售多少？</h4>
                            <input type="text" className="input inputmoney sellmoney" onChange={this.handelChange} name="money" value={messmoney} placeholder="请输入你想出售的金额" />
                            <i className="fa fa-exchange" aria-hidden="true"></i>
                            <input type="text" className="input inputmoney sellmoney" onChange={this.handelChange} name="btc" value={messnum} placeholder="请输入你想出售的数量" />
                            <button className="form-sell" onClick={this.handleSubmit}>立刻出售</button>
                        </div>
                    </div>

                    <div className="col-lg-3 col-xs-3 col-md-3 advertContent">
                        <h5 className="adcontent">广告内容</h5>
                        <div className="ad-info">
                            <p> {data.noticeContent}</p>
                        </div>
                    </div>
                </div>

                <div className="detail-notice">
                    <h4 className="sellwhat">交易须知</h4>
                    <p>1.交易前请详细了解对方的交易信息。</p>
                    <p>2.请通过平台进行沟通约定，并保存好相关聊天记录。</p>
                    <p>3.如遇到交易纠纷，可通过申诉来解决问题。</p>
                    <p>4.在您发起交易请求后，比特币被锁定在托管中，受到themis保护。如果您是买家，发起交易请求后，请在付款周期内付款并把交易标记为付款已完成。卖家在收到付款后将会放行处于托管中的比特币。</p>
                    <p>交易前请阅读《themis服务条款》以及常见问题，交易指南等帮助文档。</p>
                    <p>5.请注意欺诈风险，交易前请检查该用户收到的评价信息和相关信用信息，并对新近创建的账户多加留意。</p>
                    <p>6.托管服务保护网上交易的买卖双方。在双方发生争议的情况下，我们将评估所提供的所有信息，并将托管的比特币放行给其合法所有者。</p>
                </div>

                <Modal isOpen={this.state.isModalOpen} onRequestHide={this.hideModal}>
                    <ModalHeader>
                        <ModalClose onClick={this.hideModal} />
                        <ModalTitle>提示:</ModalTitle>
                    </ModalHeader>
                    <ModalBody>
                        <p className={this.state.error ? 'text-red' : 'text-green'}>
                            {this.state.actionResult}
                        </p>
                    </ModalBody>
                    <ModalFooter>
                        <button className='btn btn-default' onClick={this.hideModal}>
                            {/*<a href="/myadvert" >关闭</a>*/}
                            <Link className="close-modal" to={`/order/progress/${this.props.data.id}`}>关闭</Link>
                            {/*<div className="close-modal" onClick={this.showOrderDetail.bind(this, this.props.data)}>关闭</div>*/}
                            {/*<a className="close-modal" href="/orderprogress" >关闭</a>*/}
                        </button>
                    </ModalFooter>
                </Modal>
            </div>
        );
    }
}



function mapStateToProps(state) {
    return {
        data: state.advert.data,     //点击出售返回的data
        all: state.advert.all,       //广告详情页面加载时的数据
        authenticated: state.auth.authenticated
    };
}
export default connect(mapStateToProps, { fetctSellBtcDetail, fetctSellnow })(Selldetail);
