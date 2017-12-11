/**
 * Created by zhangxiaojing on 2017/11/8.
 */
import {
    FETCH_MESSAGES_NUMBER,
    FETCH_MESSAGE_NOTICE,
    FETCH_MESSAGE_SYSTEM,
    FETCH_MESSAGE_LETTER,
    REQUEST_ERROR
} from '../actions/types';


const INITIAL_STATE = {message_number: null, message_notice:null, message_system:null, message_letter:null};

export default function (state = INITIAL_STATE, action) {
    switch (action.type) {
        case FETCH_MESSAGES_NUMBER:
            return {...state, message_number: action.payload};
        case FETCH_MESSAGE_NOTICE:
            return {...state, message_notice: action.payload};
        case FETCH_MESSAGE_SYSTEM:
            return {...state, message_system: action.payload};
        case FETCH_MESSAGE_LETTER:
            return {...state, message_letter: action.payload};
        case REQUEST_ERROR:
            return{...state, error: action.payload};
    }
    return state;
}