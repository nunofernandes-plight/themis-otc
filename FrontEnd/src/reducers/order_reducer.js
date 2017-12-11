/**
 * Created by zhangxiaojing on 2017/10/25.
 */

import {
    FETCH_NOT_COMPLETED_ORDERS,
    FETCH_COMPLETED_ORDERS,
    FETCH_ORDERS_DETAILS,
    FETCH_TRADE_PARTNER_MESSAGE,
    ADD_PAYMENT_INFO,
    FETCH_KEYS,
    CONFIRM_ORDER,
    ADD_TRANSACTION_ID,
    CONFIRM_SEND_MONEY,
    RELEASE_BTC,
    CONFIRM_GOODS,
    CANCEL_ORDERS,
    SAVE_COMMENT
} from '../actions/types';


const INITIAL_STATE = {not_completed_orders: null, completed_orders:null};

export default function (state = INITIAL_STATE, action) {
    switch (action.type) {
        case FETCH_NOT_COMPLETED_ORDERS:{
            return {...state, not_completed_orders: action.payload};
        }
        case FETCH_COMPLETED_ORDERS: {
            return {...state, completed_orders: action.payload};
        }
        case FETCH_ORDERS_DETAILS: {
            return {...state, orders_details: action.payload};
        }
        case FETCH_TRADE_PARTNER_MESSAGE:{
            return {...state, partner_message: action.payload};
        }
        case ADD_PAYMENT_INFO:{
            return {...state, payment_info: action.payload};
        }
        case FETCH_KEYS:{
            return {...state, keys: action.payload};
        }
        case CONFIRM_ORDER:{
            return {...state, confirm_order: action.payload};
        }
        case ADD_TRANSACTION_ID:{
            return {...state, transaction_id: action.payload};
        }
        case CONFIRM_SEND_MONEY:{
            return {...state, send_money: action.payload};
        }
        case RELEASE_BTC:{
            return {...state, release_btc: action.payload};
        }
        case CONFIRM_GOODS:{
            return {...state, confirm_goods: action.payload};
        }
        case CANCEL_ORDERS:{
            return {...state, cancel_orders: action.payload};
        }
        case SAVE_COMMENT:{
            return {...state, comment: action.payload};
        }
    }
    return state;
}