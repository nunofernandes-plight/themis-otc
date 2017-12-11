/**
 * Created by zhangxiaojing on 2017/10/23.
 */

import React, { Component } from 'react';
import { connect } from 'react-redux';
import {withRouter} from 'react-router';
import {ROOT_CHAT, ROOT_SOCKET} from '../actions/types';
import $ from 'jquery';

class Chat extends Component{
    constructor(props) {
        super(props);
    }
    componentWillUpdate(nextProps){
        const token=localStorage.getItem("token"); //token
        const senderId=localStorage.getItem("userId"); //当前发送者id
        const senderName=localStorage.getItem("loginname"); //当前用户名
        const receiverId = nextProps.partnerId;// 当前接收者id
        const receiverName=nextProps.partnerName ;//   当前接收者name
        let orderId =nextProps.orderId;
        if(nextProps.partnerId && nextProps.orderId){
            let ws = new WebSocket(`${ROOT_SOCKET}/ws?`+senderId +"_"+nextProps.partnerId+"_"+nextProps.orderId); //链接websocket
            let flag=true;
            let reconnect = new Date().getTime();
            let timeFlag1=true, timeFlag2=true;
            let connect=true;
            $(".getMore").on("click", function(){
                if(flag){
                    //获取聊天记录
                    $.ajax({
                        type :"POST",
                        url :`${ROOT_CHAT}/chat/getChatHistroy`,
                        data:{senderId:senderId, receiverId:receiverId, orderId:orderId},
                        beforeSend: function(request) {
                            request.setRequestHeader("Authorization", 'Bearer '+token);
                        },
                        success:function(data){
                            if(data.data!=null){
                                var list=data.data;
                                $.each(list, function(index){
                                    if(list[index].senderId == senderId){
                                        sendMessage(list[index].senderName, list[index].chatContent);
                                    }
                                    else{
                                        receiveMessage(list[index].senderName, list[index].chatContent);
                                    }
                                });
                            }
                        },
                        complete:function(){
                            scrollTop();
                            flag=false;
                        },
                        error:function(XMLHttpRequest, textStatus, errorThrown){
                            console.log(textStatus);
                        },
                    });
                }
                $(this).hide();
            });
            ws.onopen = () => {
                clearInterval(ws.keepAliveTimer);
                clearTimeout(ws.receiveMessageTimer);
                console.log("Connection open...");
                if(ws.readyState === 1) { // 为1表示连接处于open状态
                    ws.keepAliveTimer=setInterval(function(){
                        var heart=JSON.stringify({msgType: 2, senderId: senderId, receiverId: receiverId});
                        // console.log(heart);
                        ws.send(heart);
                    }, 2000);
                }
                if(connect){
                    // alert(1);
                    document.onkeydown = (e) => {
                        if (e && e.keyCode == 13) {
                            sendMessageBtn(orderId);
                        }
                    };
                }
            };
            //监听 messages
            ws.onmessage = (e) => {
                var data=JSON.parse(e.data);
                // 收到消息，重置定时器
                clearTimeout(ws.receiveMessageTimer);
                clearTimeout(ws.time1);
                switch (data.msgType) {
                    case 1:
                        //聊天消息
                        if (data.receiverId == senderId && data.senderId == receiverId) {
                            if(timeFlag1){
                                showTime();
                                scrollTop();
                                timeFlag1 = false;
                                ws.time1=setTimeout(()=>{
                                    timeFlag1 = true;
                                }, 600000);
                            }
                            receiveMessage(data.senderName, data.chatContent);
                            scrollTop();
                        }
                        break;
                    case 2:
                        //心跳
                        ws.receiveMessageTimer = setTimeout(() => {
                            reconnect=new Date().getTime();
                            ws.close();
                            $(".chat-head").html("连接断开，请刷新页面");
                            reconnect=false;
                        }, 30000); // 30s没收到信息，代表服务器出问题了，关闭连接。
                        break;
                    case 3:
                        //系统消息
                        break;
                }
            };
            //监听errors
            ws.onerror = () => {
                clearInterval(ws.keepAliveTimer);
                clearTimeout(ws.receiveMessageTimer);
                reconnect=false;
                console.log('onerror');
            };
            ws.onclose = () => {
                reconnect=false;
                clearTimeout(ws.receiveMessageTimer);
                clearInterval(ws.keepAliveTimer);
                let tempWs = ws; // 保存ws对象
                if(new Date().getTime() - reconnect >= 10000) { // 10秒中重连，连不上就不连了
                    ws.close();
                } else {
                    $(".chat-head").html("重新连接中...");
                    let ws = new WebSocket(`${ROOT_SOCKET}/ws?`+senderId +"_"+receiverId+"_"+orderId); //链接websocket
                    ws.onopen = tempWs.onopen;
                    ws.onmessage = tempWs.onmessage;
                    ws.onerror = tempWs.onerror;
                    ws.onclose = tempWs.onclose;
                    reconnect=true;
                }
            };
            const sendMessageBtn = (partner)=>{
                console.log(this.state);
                clearTimeout(ws.time2);
                //发送一个文本消息
                var chatContent = $(".message").val();
                if(chatContent){
                    if(timeFlag2){
                        showTime();
                        scrollTop();
                        timeFlag2 = false;
                        ws.time2=setTimeout(()=>{
                            timeFlag2 = true;
                        }, 600000);
                    }
                    var message = JSON.stringify({msgType:1, senderId: senderId, senderName: senderName, receiverId: receiverId, chatContent: chatContent, orderId:partner});
                    sendMessage(senderName, chatContent);
                    ws.send(message);
                    $(".message").val('');
                    scrollTop();
                }
            };
            //发送消息
            const sendMessage = (senderName, chatContent) =>{
                $(".chat-message").append('<li class="send-message rightd"><div class="sender rightd_h"><span>'+senderName+'</span></div><div class="content speech right">'+chatContent+'</div></li>');
            };
            //接收消息
            const receiveMessage = (receiverName, chatContent) => {
                $(".chat-message").append('<li class="receive-message leftd"><div class="sender leftd_h"><span>'+receiverName+'</span></div><div class="speech left">'+chatContent+'</div></li>');
            };
            const scrollTop = () => {
                $('.chat-body').scrollTop($('.chat-message').height());
            };
            const showTime = () => {
                let time = new Date().toLocaleString();
                $(".chat-message").append('<li class="time"><div class="">'+time+'</div></li>');
            };
        }
    }
    render() {
        return (
            <div className="chat">
                <span className="chat-head col-sm-12 h5 text-center"></span>
                <div className="chat-body g-mb-10">
                    <ul className="chat-message clearfix">
                        <li className="text-center"><span className="gray g-pt-10 g-pb-10 getMore">获取更多聊天记录</span></li>
                    </ul>
                </div>
                <div className="clearfix">
                    <input type="text" className="message" ref="message" placeholder="请输入你要说的话"/>
                </div>
            </div>
        );
    }
}

export default Chat;

