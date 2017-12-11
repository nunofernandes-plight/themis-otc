/**
 * Created by oxchain on 2017/10/20.
 */

import React, { Component } from 'react';
import { connect } from 'react-redux';
import { fetctHome } from '../actions/releaseadvert';
class Home extends Component {
    constructor(props) {
        super(props);
        this.state = {};
        this.renderRows = this.renderRows.bind(this);
        this.renderArray = this.renderArray.bind(this);
    }
    componentWillMount() {
        this.props.fetctHome();
    }
    renderRows() {
        const arraydata = this.props.all || [];   //列表数组的数据
        console.log(arraydata);
        return arraydata.map((item, index) => {
            const actionUrl = "http://192.168.1.200/v1/tfs/" + item.imageName;
            return (
                <div key={index} className="list-border">
                    <div className="title-bgc">
                        <a href={`/otherInfodetail/${item.userId}`}> <img src={actionUrl ? actionUrl : "/public/img/touxiang.png"} alt="" /></a>
                        <p>
                            <a href={`/otherInfodetail/${item.userId}`}>{item.loginname}</a>
                        </p>
                    </div>
                    <div className="col-lg-4">
                        <p>{item.txNum}</p>
                        <p>交易次数</p>
                    </div>
                    <div className="col-lg-4">
                        <p>{item.trustNum}</p>
                        <p>信任人数</p>
                    </div>
                    <div className="col-lg-4">
                        <p>{item.trustPercent} %</p>
                        <p>信誉度</p>
                    </div>
                    <hr className="clear" />
                    <div className=" home-content">
                        <p>交易价格:{item.price}CNY</p>
                        <p>交易限额:{item.minTxLimit}-{item.maxTxLimit}CNY</p>
                        <p>付款方式:{item.payType == 1 ? "现金" : item.payType == 2 ? "转账" : item.payType == 3 ? "支付宝" : item.payType == 4 ? "微信" : item.payType == 5 ? "Apple Pay" : ""}</p>
                    </div>
                    <button className="home-button" >
                        <a href={`/buydetail/${item.id}`} className={`${item.noticeType == 1 ? 'hidden' : ''}`}>{item.noticeType == 2 ? "购买比特币" : ""}</a>
                        <a href={`/selldetail/${item.id}`} className={`${item.noticeType == 2 ? 'hidden' : ''}`}>{item.noticeType == 1 ? "出售比特币" : ""}</a>
                    </button>
                </div>
            );
        });
    }
    renderArray(item, index) {
        return (
            <div key={index} className="list-item">
                <img src={item.src} alt="" />
                <p>{item.title}</p>
                <div className="home-content">
                    <p>{item.content}</p>
                </div>
            </div>
        );
    }
    render() {
        const ArrayLinks = [
            { src: "/public/img/买卖-.png", title: "快速买卖", content: "themis是一个不涉及第三方的P2P交易平台，交易过程方便快捷" },
            { src: "/public/img/安全.png", title: "安全交易", content: "冷存储、SSL、多重加密等银行级别安全技术，十年金融安全经验安全团队" },
            { src: "/public/img/快速.png", title: "及时掌控", content: "行情及时掌握,交易随时随地" },
        ];
        return (
            <div className="clear">
                <div className="headermain">
                    <div className="bannertitle">
                        <h2>THEMIS</h2>
                        <h4>T H E M I S 比 特 币 场 外 交 易 平 台</h4>
                        <hr />
                        <h5>去中心化托管更安全</h5>
                    </div>
                </div>
                <div className="homemodle">
                    <div className="model-title">
                        <h4>多方验证 买卖自由 安全可靠</h4>
                        <h5><a href="/buybtc">查看更多的广告</a></h5>
                    </div>
                    <div className="modle-list">
                        {this.renderRows()}
                    </div>
                </div>
                <div className="home-bottom">
                    <div className="list-width">
                        {ArrayLinks.map(this.renderArray)}
                    </div>
                </div>
            </div>
        );
    }
}

function mapStateToProps(state) {
    // console.log(state.advert.all);
    return {
        all: state.advert.all
    };
}
export default connect(mapStateToProps, { fetctHome })(Home);
