/**
 * Created by oxchain on 2017/10/20.
 */
import React, { Component } from 'react';
import { connect } from 'react-redux';
import { Upload } from 'antd';
import { fetctBaseInfo } from '../actions/releaseadvert';
import {
    Modal,
    ModalHeader,
    ModalTitle,
    ModalClose,
    ModalBody,
    ModalFooter
} from 'react-modal-bootstrap';

function getBase64(img, callback) {
    const reader = new FileReader();
    reader.addEventListener('load', () => callback(reader.result));
    reader.readAsDataURL(img);
}

class Baseinfo extends Component {
    constructor(props) {
        super(props);
        this.state = {
            isModalOpen: false,
            error: null,
            actionResult: '',
        };
        this.handleSave = this.handleSave.bind(this);
        this.handleChange = this.handleChange.bind(this);
    }

    hideModal = () => {
        this.setState({
            isModalOpen: false
        });
    };
    handleChange = (info) => {
        if (info.file.status === 'done') {
            // Get this url from response in real world.
            getBase64(info.file.originFileObj, imageUrl => this.setState({ imageUrl }));
        }
    }

    handleSave() {
        let formdata = new FormData();
        formdata.append("loginname", localStorage.getItem("loginname"));
        formdata.append("description", this.refs.description.value);
        this.props.fetctBaseInfo({ formdata }, err => {
            this.setState({ isModalOpen: true, error: err, actionResult: err || '保存成功!' });
        });
    }

    render() {
        const loginname = localStorage.getItem('loginname');//登录名
        const mobilephone = localStorage.getItem('mobilephone');//手机号
        const createTime = localStorage.getItem('createTime');//注册时间
        const email = localStorage.getItem('email');//邮箱
        const firstBuyTime = localStorage.getItem('firstBuyTime'); //第一次交易时间
        const txNum = localStorage.getItem('txNum'); //交易次数
        const believeNum = localStorage.getItem('believeNum'); //信任人数
        const sellAmount = localStorage.getItem('sellAmount'); //出售的累计交易数量
        const buyAmount = localStorage.getItem('buyAmount'); //购买的累计交易数量
        const imageUrl = this.state.imageUrl;
        const actionUrl = "http://192.168.1.111:8081/user/avatar?loginname=" + loginname;
        return (
            <div>
                <div className="maininfo">
                    <div className="display-info">
                        {/*<img className="baseinfoimg" src="./public/img/user.jpg" alt=""/>*/}
                        <Upload
                            className="avatar-uploader"
                            name="file"
                            showUploadList={false}
                            action={actionUrl}
                            onChange={this.handleChange}
                        >
                            {
                                imageUrl ?
                                    <img src={imageUrl} alt="" className="avatar" /> :
                                    <div className="touxiangStyle">
                                        <img src="./public/img/default.png" className="avatar" alt="" />
                                        <p>上传头像</p>
                                    </div>
                            }
                        </Upload>
                    </div>
                    <div className="display-info">
                        <h5 style={{ marginBottom: 20 + 'px', marginTop: 30 + 'px' }}>{loginname}</h5>
                    </div>
                </div>
                <div className="validateinfo">
                    <ul>
                        <li>身份证验证:未验证</li>
                        <li>电子邮件验证:{email ? email : "未验证"}</li>
                        <li>手机号码:{mobilephone}</li>
                        <li>注册时间: {createTime}</li>
                        <li>第一次交易时间：{firstBuyTime}</li>
                        <li>信任人数:被 {believeNum} 人信任</li>
                        <li>累计交易次数: {txNum}</li>
                        <li>累计交易量:（买）{buyAmount} —（卖）{sellAmount} BTC</li>
                    </ul>
                </div>
                <textarea className="textarea-info" name="" id="" cols="80" rows="5" placeholder="简介，在您的公共资料上展示您的介绍信息。纯文本，不超过200字" ref="description"></textarea>
                <div className="display-save">
                    <button className="form-save" onClick={this.handleSave}>保存</button>
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
                            <a className="close-modal" href="" >关闭</a>
                        </button>
                    </ModalFooter>
                </Modal>


            </div>
        );
    }
}



function mapStateToProps(state) {
    return {
        all: state.advert.all
    };
}
export default connect(mapStateToProps, { fetctBaseInfo })(Baseinfo);
