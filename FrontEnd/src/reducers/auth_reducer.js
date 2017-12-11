/**
 * Created by oxchain on 2017/10/18.
 */

import {
    AUTH_USER,
    UNAUTH_USER,
    AUTH_ERROR,
    FETCH_VERIFY_CODE,
    FETCH_VERIFY_CODE_PHONE,
    FETCH_PHONE,
    FETCH_PASSWORD,
    EMAIL_FIND_PSW,
    RESET_PSW,
    PHONE_FIND_PSW,
    EMAIL_SIGNUP_USER,
    EMAIL_ISLIVE
} from '../actions/types';

const INITIAL_STATE = { all: null, array: [], data: null };

export default function (state = INITIAL_STATE, action) {
    switch (action.type) {
        case AUTH_USER:
            return { ...state, error: '', authenticated: true };
        case UNAUTH_USER:
            return { ...state, authenticated: false };
        case AUTH_ERROR:
            return { ...state, error: action.payload, authenticated: false };
        case FETCH_VERIFY_CODE:
            return { ...state, all: action.payload.data.data };
        case FETCH_VERIFY_CODE_PHONE:
            return { ...state, all: action.payload.data.data };
        case FETCH_PHONE:
            return { ...state, data: action.payload.data.data };
        case FETCH_PASSWORD:
            return { ...state, error: '', authenticated: false };
        case EMAIL_FIND_PSW:
            return { ...state, all: action.payload.data };
        case RESET_PSW:
            return { ...state, all: action.payload.data };
        case PHONE_FIND_PSW:
            return { ...state, data: action.payload.data };
        case EMAIL_SIGNUP_USER:
            return { ...state, all: action.payload.data };
        case EMAIL_ISLIVE:
            return { ...state, all: action.payload.data };
    }

    return state;
}