/**
 * Created by oxchain on 2017/10/17.
 */
import React from 'react';
import { createStore, applyMiddleware, compose } from 'redux';
import ReactDOM from 'react-dom';
import { Provider } from 'react-redux';
import { Router, browserHistory } from 'react-router';
import reduxThunk from 'redux-thunk';
import reducers from './reducers';
import { AUTH_USER } from './actions/types';
import { Route, BrowserRouter, Switch, Redirect } from 'react-router-dom';
import Header from './components/common/header';
import Footer from './components/common/footer';
import Singin from './components/auth/signin';
import Signinemail from './components/auth/signin_email';
import Singup from './components/auth/signup';
import Signupemail from './components/auth/signup_email';

import Signout from './components/auth/signout';
import Forgetpsw from './components/auth/forget_psw';
import Emialforget from './components/auth/forget_email';
import Resetpsw from './components/auth/reset_psw';
import JumpTip from './components/email_Jumptip';
import RegisterJumptip from './components/register_jumptip';
import EmailIslive from './components/IsLive';

import Usercenter from './components/user_center';
import Buybtc from './components/buy_btc';
import Sellbtc from './components/sell_btc';
import Selldetail from './components/sell_detail';
import Buydetail from './components/buy_detail';
import Myadvert from './components/my_advert';
import Home from './components/home';

import MessageNotice from './components/message/message_notice';
import MessageSystem from './components/message/message_system';
import MessageLetter from './components/message/message_letter';
import OrderInProgress from './components/order_inprogress';
import OrderCompleted from './components/order_completed';
import OrderProgress from './components/order_process';
import ArbitrationBuyer from './components/arbitration_buyer';
import ArbitrationManage from './components/arbitration_manage';
import RefereeList from './components/referee_list';
import Releaseadvert from './components/release_advert';
import OtherInfodetail from './components/other_infodetail';

const createStoreWithMiddleware = compose(
    applyMiddleware(reduxThunk),
    window.devToolsExtension ? window.devToolsExtension() : f => f
)(createStore);
const store = createStoreWithMiddleware(reducers);

const token = localStorage.getItem('token');
// If token exist, singin automatic
if (token) {
    store.dispatch({ type: AUTH_USER });
}

ReactDOM.render(
    <Provider store={store} >
        <BrowserRouter>
            <div>
                <main>
                    <Header />
                    <Switch>
                        <Route path="/signin" component={Singin} />
                        <Route path="/signinemail" component={Signinemail} />
                        <Route path="/signup" component={Singup} />
                        <Route path="/signupemail" component={Signupemail} />
                        <Route path="/signout" component={Signout} />
                        <Route path="/usercenter" component={Usercenter} />
                        <Route path="/order/inprogress" component={OrderInProgress} />
                        <Route path="/order/completed" component={OrderCompleted} />
                        <Route path="/order/progress/:id" component={OrderProgress} />
                        <Route path="/arbitrationbuyer" component={ArbitrationBuyer} />
                        <Route path="/arbitrationmanage" component={ArbitrationManage} />
                        <Route path="/refereelist" component={RefereeList} />
                        <Route path="/releaseadvert" component={Releaseadvert} />
                        <Route path="/buybtc" component={Buybtc} />
                        <Route path="/sellbtc" component={Sellbtc} />
                        <Route path="/selldetail/:id" component={Selldetail} />
                        <Route path="/buydetail/:id" component={Buydetail} />
                        <Route path="/myadvert" component={Myadvert} />
                        <Route path="/message/notice" component={MessageNotice} />
                        <Route path="/message/system" component={MessageSystem} />
                        <Route path="/message/letter" component={MessageLetter} />
                        <Route path="/forgetpsw" component={Forgetpsw} />
                        <Route path="/resetpsw" component={Resetpsw} />
                        <Route path="/emailforget" component={Emialforget} />
                        <Route path="/otherInfodetail/:id" component={OtherInfodetail} />
                        <Route path="/jumptip" component={JumpTip} />
                        <Route path="/registerjumptip" component={RegisterJumptip} />
                        <Route path="/islive" component={EmailIslive} />
                        <Route path="/" component={Home} />
                    </Switch>
                    <Footer />
                </main>
            </div>
        </BrowserRouter>
    </Provider>
    , document.querySelector('.wrapper')
);

