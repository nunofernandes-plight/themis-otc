/**
 * Created by oxchain on 2017/10/18.
 */
import { combineReducers } from 'redux';
import { reducer as formReducer } from 'redux-form';
import authReducer from './auth_reducer';
import orderReducer from './order_reducer';
import releaseAdvert from './advert_reducer';
import arbitrateReducer from './arbitrate_reducer';
import messageReducer from './message_reducer';


const rootReducer = combineReducers({
    form: formReducer,
    auth: authReducer,
    order: orderReducer,
    advert: releaseAdvert,
    arbitrate: arbitrateReducer,
    message: messageReducer
});

export default rootReducer;