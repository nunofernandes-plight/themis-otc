/**
 * Created by oxchain on 2017/10/20.
 */
import React, { Component } from 'react';
import { connect } from 'react-redux';
import { fetctAddressPay } from '../actions/releaseadvert';
import {
    Modal,
    ModalHeader,
    ModalTitle,
    ModalClose,
    ModalBody,
    ModalFooter
} from 'react-modal-bootstrap';
class AddressPay extends Component {
    constructor(props) {
        super(props);
        this.state = {
            isChange:false,
            isModalOpen: false,
            error: null,
            actionResult: '',
        };
        this.handleChange = this.handleChange.bind(this);
        this.handleAddresspay = this.handleAddresspay.bind(this);
    }
    hideModal = () => {
        this.setState({
            isModalOpen: false
        });
    };
    handleChange(){
        this.setState({
            isChange:true
        });
    }
    handleAddresspay(){
        const loginname = localStorage.getItem("loginname");
        const firstAddress = this.refs.addressPay.value;
        // console.log(loginname, firstAddress);
        this.props.fetctAddressPay({ loginname, firstAddress}, err => {
            this.setState({ isModalOpen: true, error: err, actionResult: err || '保存成功!', spin: false, isChange:false});
        });
        localStorage.setItem('firstAddress', firstAddress);
    }
    render() {
        const firstAddress = localStorage.getItem("firstAddress");
        const data = this.props.all || [];
        // console.log(data.status);
        return (
            <div className="address_pay text-center">
             <div className={` ${ firstAddress ? "":"hidden"} || ${ data.status == -1 || this.state.isChange == true ? "hidden":""}`}>
                <h1>{data.status ? data.data: firstAddress}</h1>
                <button className="form-seach changeAddress" onClick={this.handleChange}>修改</button>
             </div>
                <div className={` ${ data.status == -1 || this.state.isChange == true ? "":"hidden"} `}>
                    <input className={`input inputwidth`} type='text' placeholder="请输入收款地址" ref="addressPay" required/>
                    <button className="form-seach" onClick={this.handleAddresspay}><i className={`fa fa-spinner fa-spin ${this.state.spin ? '' : 'hidden'}`}></i> 保存</button>
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
                           关闭
                        </button>
                    </ModalFooter>
                </Modal>
            </div>
        );
    }
}

function mapStateToProps(state) {
    // console.log(state.advert.data);
    return {
        all: state.advert.data
    };
}
export default connect(mapStateToProps, { fetctAddressPay })(AddressPay);
