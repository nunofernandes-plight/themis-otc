/**
 * Created by oxchain on 2017/10/20.
 */

import React, { Component } from 'react';
import { Field, reduxForm } from 'redux-form';
import { Select } from 'antd';
import { connect } from 'react-redux';
import { releaseAdvert, fetctArray } from '../actions/releaseadvert';
import {
    Modal,
    ModalHeader,
    ModalTitle,
    ModalClose,
    ModalBody,
    ModalFooter
} from 'react-modal-bootstrap';

class Releaseadvert extends Component {
    constructor(props) {
        super(props);
        this.state = {
            isModalOpen: false,
            error: null,
            actionResult: '',
            currentIndex: 0,
            status: 1,
            premium: '',
            price: ''
        };
        this.handleRowstype = this.handleRowstype.bind(this);
        this.renderRowscountry = this.renderRowscountry.bind(this);
        this.renderRowscurrency = this.renderRowscurrency.bind(this);
        this.renderRowspayway = this.renderRowspayway.bind(this);
        this.handleFormSubmit = this.handleFormSubmit.bind(this);
        this.handelChange = this.handelChange.bind(this);
    }
    hideModal = () => {
        this.setState({
            isModalOpen: false
        });
    };
    componentWillMount() {
        this.props.fetctArray();
    }
    handleRowstype(status) {
        this.setState({
            status: status
        });
    }
    handleFormSubmit(e) {
        e.preventDefault();
        const userId = localStorage.getItem("userId");
        const loginname = localStorage.getItem("loginname");
        const premium = this.state.premium;
        const price = this.state.price;

        const minPrice = this.refs.minPrice.value;
        const minTxLimit = this.refs.minTxLimit.value;
        const maxTxLimit = this.refs.maxTxLimit.value;
        const noticeContent = this.refs.noticeContent.value;
        const noticeType = this.state.status;
        const location = this.state.country;
        const currency = this.state.currency;
        const payType = this.state.payway;
        if (this.props.authenticated) {
            this.props.releaseAdvert({ userId, loginname, noticeType, location, currency, premium, price, minPrice, minTxLimit, maxTxLimit, payType, noticeContent }, err => {
                this.setState({ isModalOpen: true, error: err, actionResult: err || '发布成功!' });
            });
        }
        else {
            alert("请先登录!");
        }
    }
    renderRowscountry() {
        const locationList = this.props.array.locationList || [];
        return locationList.map(({ id, name }) => {
            const ID = id.toString();
            const Option = Select.Option;
            return (
                <Option key={id} label={name} value={ID}>{name}</Option>
            );
        });
    }
    renderRowscurrency() {
        const currencyList = this.props.array.currencyList || [];
        return currencyList.map(({ id, currency_name }) => {
            const Option = Select.Option;
            const ID = id.toString();
            return (
                <Option key={id} label={currency_name} value={ID}>{currency_name}</Option>
            );
        });
    }
    renderRowspayway() {
        const paymentList = this.props.array.paymentList || [];
        return paymentList.map(({ id, paymentName }) => {
            const Option = Select.Option;
            const ID = id.toString();
            return (
                <Option key={id} label={paymentName} value={ID}>{paymentName}</Option>
            );
        });
    }

    handelChange(e) {
        const money = this.props.array.cnyDetailList || {};
        const price = parseFloat(money.buy);
        this.setState({
            premium: e.target.value,
            price: ((e.target.value) / 100 + 1) * price
        });
    }

    // renderField({ input, label, type, meta: { touched, error } }) {
    //     return (
    //         <div className={` ${touched && error ? 'has-error' : ''}`}>
    //             <input {...input} placeholder={label} type={type} className="display slectoption "/>
    //             {/*<span className={`glyphicon glyphicon-${icon} form-control-feedback`}></span>*/}
    //             <div className="help-block ">{touched && error ? error : ''}</div>
    //         </div>
    //     )}

    render() {
        // const { handleSubmit } = this.props;
        const userId = localStorage.getItem("userId");
        const premium = this.state.premium;
        const money = this.props.array.cnyDetailList || {};
        const price = parseFloat(this.state.price || money.buy).toFixed(2);
console.log(userId);

console.log(this.state.error);
        const url = this.state.error ? `/releaseadvert`:this.state.status == 1 ? `/buydetail/${userId}`: `/selldetail/${userId}`;




        return (
            <div className="maincontent">
                <h2 className="h2title">发布一个比特币交易广告</h2>
                <div className="tipcontent">
                    <p>如果您经常交易比特币，可以发布自己的比特币交易广告。如果您只想购买或者出售一次，我们建议您直接从购买或出售列</p>
                    <p>表中下单交易。</p>
                    <p>发布一则交易广告是免费的。</p>
                    <p>您THEMIS钱包中至少需要有0.05 BTC,您的广告才会显示在交易列表中。</p>
                    <p>发布交易广告的THEMIS用户每笔完成的交易需要缴纳0.7%的费用。</p>
                    <p>您必须在交易广告或交易聊天中提供您的付款详细信息，发起交易后，价格会锁定，除非定价有明显错误。</p>
                    <p>所有交流必须在THEMIS上进行，请注意高风险有欺诈的付款方式。</p>
                </div>
                <form action="" method="post" onSubmit={this.handleFormSubmit.bind(this)}>
                    <h4 className="h4title">交易类型</h4>
                    <h5 className="h3title">*选择广告类型</h5>
                    <span className="tipspan"> &nbsp;&nbsp;您想要创建什么样的交易广告？如果您希望出售比特币,请确保您在THEMIS的钱包中有比特币。</span>
                    <ul className=" buytype">
                        <li className={` ${this.state.status == 1 ? "tab-way-item active" : " tab-way-item"} `} onClick={() => this.handleRowstype(1)}>在线购买比特币</li>
                        <li className={`${this.state.status == 2 ? "tab-way-item active" : " tab-way-item "}`} onClick={() => this.handleRowstype(2)}>在线出售比特币</li>
                    </ul>
                    <div className="clear display">
                        <h5 className="h3title clear">*所在地</h5>
                        <span className="tipspan"> 请选择你要发布广告的国家。</span>
                        <Select defaultValue="选择国家" style={{ width: 240 }} onChange={(value) => this.state.country = value}>
                            {this.renderRowscountry()}
                        </Select>
                    </div>
                    <h4 className="h4title">更多信息</h4>
                    <h5 className="h3title clear">*货币:</h5>
                    <span className="tipspan"> 您希望交易付款的货币类型。</span>
                    <Select defaultValue="选择货币" style={{ width: 240 }} onChange={(value) => this.state.currency = value}>
                        {this.renderRowscurrency()}
                    </Select>
                    <h5 className="h3title clear">*溢价: </h5>
                    <span className="tipspan">基于市场价的溢出比例,市场价是根据部分大型交易所实时价格得出的,确保您的报价趋于一个相对合理的范围,比如当前价格为7000,溢价比例为10%,那么价格为7700。</span>

                    <input type="number" placeholder="%" className="display slectoption" onChange={this.handelChange} value={premium} ref="premium" />
                    {/*<Field name="premium" component={this.renderField} onChange={this.handelChange} value={premium}  type="text"  label="%"  />*/}

                    <h5 className="h3title clear">*价格: </h5>
                    <span className="tipspan">基于溢价比例得出的报价,10分钟更新一次。</span>

                    <input type="text" placeholder="CNY" className="display slectoption" onChange={this.handelChange} value={price} disabled ref="price" />
                    {/*<Field name="price" component={this.renderField} onChange={this.handelChange} value={price}  type="text"  label="CNY"  />*/}

                    <h5 className="h3title clear">*最低价: (选填)</h5>
                    <span className="tipspan">最低可成交的价格,可帮助您在价格剧烈波动时保持稳定的盈利,比如最低价为12000,市场价处于12000以下时,您的广告将依旧以12000的价格展示出来。</span>

                    <input type="number" placeholder="CNY" className="display slectoption" ref="minPrice" />
                    {/*<Field name="minPrice" component={this.renderField} type="text"  label="CNY"  />*/}

                    <h5 className="h3title clear">*最小限额: </h5>
                    <span className="tipspan">一次交易的最低交易限制。</span>

                    <input type="number" placeholder="请输入最小限额 CNY" className="display slectoption" ref="minTxLimit" />
                    {/*<Field name="minTxLimit" component={this.renderField} type="text"  label="请输入最小限额 CNY"  />*/}

                    <h5 className="h3title clear">*最大限额: </h5>
                    <span className="tipspan">一次交易中的最大交易限制,您的钱包余额也会影响最大量的设置。</span>

                    <input type="number" placeholder="请输入最大限额 CNY" className="display slectoption" ref="maxTxLimit" />
                    {/*<Field name="maxTxLimit" component={this.renderField} type="text"  label="请输入最大限额 CNY"  />*/}


                    <h5 className="h3title clear">*收款方式:</h5>
                    <span className="tipspan"> 您希望交易付款的货币类型。</span>
                    <Select defaultValue="支付方式" style={{ width: 240 }} onChange={(value) => this.state.payway = value}>
                        {this.renderRowspayway()}
                    </Select>
                    <h5 className="h3title clear">*广告内容:(选填)</h5>
                    <textarea name="" id="" cols="150" rows="6" className="display text-content" ref="noticeContent" placeholder="请说明有关您交易的相关条款或备注您的支付方式，如微信号，支付宝号等，以便对方可以快速和您交易。(下单前后都可见)" ></textarea>
                    <button type="submit" className="  form-apply">申请发布</button>
                </form>

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
                            <a className="close-modal" href={url} >关闭</a>
                        </button>
                    </ModalFooter>
                </Modal>
            </div>
        );
    }
}

// const validate = values => {
//     const errors = {};
//
//     if(!values.premium) {
//         errors.premium = ' *不能为空';
//     }
//
//     if(!values.price) {
//         errors.price = ' *不能为空';
//     }
//
//     if(!values.minTxLimit) {
//         errors.minTxLimit = ' *不能为空';
//     }
//     if(!values.maxTxLimit) {
//         errors.maxTxLimit = ' *不能为空';
//     }
//     return errors
// };

function mapStateToProps(state) {
    // console.log(state.advert.array)
    return {
        authenticated: state.auth.authenticated,
        errorMessage: state.auth.error,
        // all:state.advert.all,
        array: state.advert.array
    };
}

// const reduxSignupForm = reduxForm({
//     form: 'form',
//      validate
// })(Releaseadvert);

export default connect(mapStateToProps, { releaseAdvert, fetctArray })(Releaseadvert);
