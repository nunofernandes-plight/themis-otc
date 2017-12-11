/**
 * Created by oxchain on 2017/10/17.
 */

import React, { Component } from 'react';
import { Link } from 'react-router';

class Footer extends Component {
    constructor(props) {
        super(props);
        this.state = {};
    }


    render() {
        const username = localStorage.getItem('username');
        return (
            <div className="footer">
                <p className="footer-content">themis 香港牛链科技</p>
            </div>
        );
    }

}
export default Footer;
