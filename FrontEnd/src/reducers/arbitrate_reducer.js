/**
 * Created by zhangxiaojing on 2017/10/31.
 */
import {
    FETCH_ARBITRATE_LIST,
    UPLOAD_EVIDENCE,
    FETCH_EVIDENCE,
    ARBITRATE_RESULT
} from '../actions/types';


const INITIAL_STATE = {arbitrate_list:null, get_evidence:null};

export default function (state = INITIAL_STATE, action) {
    switch (action.type) {
        case FETCH_ARBITRATE_LIST:
            return {...state, arbitrate_list: action.payload};
        case UPLOAD_EVIDENCE:
            return {...state, evidence: action.payload};
        case FETCH_EVIDENCE:
            return {...state, get_evidence: action.payload};
        case ARBITRATE_RESULT:
            return {...state, result: action.payload};
    }
    return state;
}