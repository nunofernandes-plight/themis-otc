/**
 * Created by oxchain on 2017/10/20.
 */

import React, { Component } from 'react';
import { Select } from 'antd';
import { Pagination } from 'antd';
// import { Pagination } from 'nl-design';
import 'antd/dist/antd.css';
import { connect } from 'react-redux';

import { fetctSellSeach, fetctArray } from '../actions/releaseadvert';

class Sellbtc extends Component {
    constructor(props) {
        super(props);
        this.state = {
            pageSize: 8, //每页显示的条数8条
            current: 1, //默认的当前第一页
            searchtype: 1,
            country: '',
            currency: '',
            payway: ''
        };
        this.handleRow = this.handleRow.bind(this);
        this.renderRowscountry = this.renderRowscountry.bind(this);
        this.renderRowscurrency = this.renderRowscurrency.bind(this);
        this.renderRowspayway = this.renderRowspayway.bind(this);
        this.onPagination = this.onPagination.bind(this);
        this.handleSeach = this.handleSeach.bind(this);
    }

    componentWillMount() {

        const pageNum = this.state.current;
        this.props.fetctSellSeach({ pageNum }, () => { });

        this.props.fetctArray({}, () => { }); //选择框的数据

    }
    onPagination(pageNum) {
        // console.log("当前页数" + pageNum)  //当前页数
        this.props.fetctSellSeach({ pageNum }, () => { });
    }

    handleSeach() {
        const pageNum = this.state.current;
        const searchType = this.state.searchtype;
        const location = this.state.country;
        const currency = this.state.currency;
        const payType = this.state.payway;

        this.props.fetctSellSeach({ searchType, location, currency, payType, pageNum }, () => { });
    }
    handleRow() {
        const arraydata = this.props.all.pageList || [];
        return arraydata.map((item, index) => {
            return (
                <tr key={index} className="contentborder">
                    <td className="tabletitle"><a href={`/otherInfodetail/${item.userId}`}>{item.loginname}</a></td>
                    <td className="tabletitle">交易 {item.txNum} | 好评度 {item.goodPercent} | 信任 {item.trustNum}</td>
                    <td className="tabletitle"> {item.payType == 1 ? "现金" : item.payType == 2 ? "转账" : item.payType == 3 ? "支付宝" : item.payType == 4 ? "微信" : item.payType == 5 ? "Apple Pay" : ""} </td>
                    <td className="tabletitle"> {item.minTxLimit} - {item.maxTxLimit} CNY</td>
                    <td className="tabletitle">{item.price}</td>
                    <td className="tabletitle ">
                        <button className="tablebuy" ><a href={`/selldetail/${item.id}`}>出售</a></button>
                    </td>
                </tr>

            );
        });
    }
    renderRowstype() {
        const searchTypeList = this.props.array.searchTypeList || [];
        return searchTypeList.map(({ id, name }) => {
            var ID = id.toString();
            const Option = Select.Option;
            return (
                <Option key={id} label={name} value={ID}>{name}</Option>
            );
        });
    }
    renderRowscountry() {
        const locationList = this.props.array.locationList || [];
        return locationList.map(({ id, name }) => {
            var ID = id.toString();
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
            var ID = id.toString();
            return (
                <Option key={id} label={currency_name} value={ID}>{currency_name}</Option>
            );
        });
    }
    renderRowspayway() {
        const paymentList = this.props.array.paymentList || [];
        return paymentList.map(({ id, paymentName }) => {
            const Option = Select.Option;
            var ID = id.toString();
            return (
                <Option key={id} label={paymentName} value={ID}>{paymentName}</Option>
            );
        });
    }
    render() {
        const totalNum = this.props.all.rowCount;

        const Option = Select.Option;
        return (
            <div className="mainbuy">
                <div className="slece-style">
                    <Select defaultValue="搜公告" style={{ width: 120 }} onChange={(value) => this.state.searchtype = value}>
                        {this.renderRowstype()}
                    </Select>
                    <Select defaultValue="选择国家" style={{ width: 120 }} onChange={(value) => this.state.country = value}>
                        {this.renderRowscountry()}
                    </Select>
                    <Select defaultValue="选择币种" style={{ width: 120 }} onChange={(value) => this.state.currency = value}>
                        {this.renderRowscurrency()}
                    </Select>
                    <Select defaultValue="选择支付方式" style={{ width: 120 }} onChange={(value) => this.state.payway = value}>
                        {this.renderRowspayway()}
                    </Select>
                    <button type="submit" className="form-seach" onClick={this.handleSeach}>搜索</button>
                </div>
                <table className="tableborder">
                    <tbody>
                        <tr className="titlemargin">
                            <th className="tabletitle">昵称</th>
                            <th className="tabletitle">信用</th>
                            <th className="tabletitle">付款方式</th>
                            <th className="tabletitle">限额</th>
                            <th className="tabletitle">价格</th>
                        </tr>

                        {this.handleRow()}
                    </tbody>

                </table>
                <div className="pagecomponent">
                    <Pagination defaultPageSize={this.state.pageSize} total={totalNum} onChange={e => this.onPagination(e)} />
                </div>

            </div>
        );
    }
}



function mapStateToProps(state) {
    // console.log(state.advert.all);
    return {
        array: state.advert.array,   //select选择框
        all: state.advert.all,       //表格数据
    };
}
export default connect(mapStateToProps, { fetctSellSeach, fetctArray })(Sellbtc);
