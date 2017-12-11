/**
 * Created by oxchain on 2017/10/25.
 */

import React, { Component } from 'react';
import { connect } from 'react-redux';
import { signoutUser } from '../../actions/auth';
import Signin from './signin';

class Signout extends Component {
    componentWillMount() {
        this.props.signoutUser();
    }

    render() {
        return (
            <div>
                {/*<section className="content">*/}
                {/*<div className="text-center"><h2>您已退出登录</h2></div>*/}
                {/*</section>*/}
                <div className="siginout">
                    <Signin location="{this.props.location}" />
                </div>
            </div>
        );
    }
}

export default connect(null, { signoutUser })(Signout);
