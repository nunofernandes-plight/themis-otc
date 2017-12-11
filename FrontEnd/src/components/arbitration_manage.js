
/**
 * Created by zhangxiaojing on 2017/10/24.
 */

import React, {Component} from 'react';
import {connect} from 'react-redux';
import Chat from './chat';
import {Modal, Button, FormGroup, FormControl, Form} from 'react-bootstrap';
import Dropzone from 'react-dropzone';


class ArbitrationManage extends Component{
    constructor(props) {
        super(props);
        this.state={
            show: false,
            currentIndex:0,
            isApplyFileDone: false,
        };
        this.orderMessageDetails=this.orderMessageDetails.bind(this);
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
    applyFile(files) {
        console.log('files', files);
        this.setState({
            applyOFile: files
        });
    }
    renderRows(userList) {
        return userList.map((msg, index) => {
            return <li key={index} onClick={()=> { this.setState({ currentIndex : index }); } } className={ this.check_title_index( index ) }>
                {msg.name}{msg.msg>0 ? <span className="msg-tip">{msg.msg}</span>:""}</li>;
        });
    }
    check_title_index(index){

        return index === this.state.currentIndex ? "active" : " ";
    }

    render(){
        let close = () => this.setState({ show: false});
        const userList=[
            {name:1, msg:0}, {name:4, msg:2}, {name:"THEMIS", msg:3}
        ];
        const msg= {price:121, quantity:23, amount:232, number:3748937208457038, way:"支付宝", info:"100%信誉，在线10分钟迅速发货。"};
        return(
            <div className=" container g-pt-100 g-pb-100">
                <h3 className="h3 text-center">仲裁后台</h3>
                <div className="row arbitration-buyer">
                    <div className="arbitration-buyer-box clearfix">
                        <div className="col-sm-2 user-list">
                            <ul>
                                {this.renderRows(userList)}
                            </ul>
                        </div>
                        <div className="col-sm-7">
                            <h4 className="arbitration-buyer-box-title h4">买家THEMIS</h4>
                            <Chat/>
                        </div>
                        <div className="col-sm-3">
                            {this.orderMessageDetails(msg)}
                            <Button
                                bsStyle="primary"
                                bsSize="large"
                                onClick={() => this.setState({ show: true})}
                            >
                                证据存根
                            </Button>
                            <Modal
                                show={this.state.show}
                                onHide={close}
                                container={this}
                                aria-labelledby="contained-modal-title"
                            >
                                <Modal.Header closeButton>
                                    <Modal.Title id="contained-modal-title">证据存根</Modal.Title>
                                </Modal.Header>
                                <Modal.Body>
                                    <Form horizontal>
                                        <Dropzone onDrop={this.applyFile.bind(this)} className="sign-up">
                                            {({isDragActive, isDragReject, acceptedFiles, rejectedFiles}) => {
                                                return (
                                                    <div>
                                                        <div className="col-sm-4">
                            <span className="btn btn-default"
                                  style={{color: "white", background: '#a6a5a6', marginLeft: '-15px'}}>选择文件</span>
                                                        </div>
                                                        <div className="col-sm-6">
                                                            <p style={{
                                                                marginTop: '8px',
                                                                color: 'gray'
                                                            }}>{acceptedFiles.length > 0 ? acceptedFiles[0].name : '未选择任何文件'}</p>
                                                        </div>
                                                    </div>
                                                );
                                            }}
                                        </Dropzone>
                                        <FormGroup controlId="formControlsTextarea">
                                            <FormControl componentClass="textarea" placeholder="textarea" />
                                        </FormGroup>
                                    </Form>
                                </Modal.Body>
                                <Modal.Footer>
                                    <Button onClick={close}>取消</Button>
                                    <Button onClick={close}>确定</Button>
                                </Modal.Footer>
                            </Modal>
                        </div>
                    </div>
                </div>
            </div>
        );
    }
}
export default  ArbitrationManage;