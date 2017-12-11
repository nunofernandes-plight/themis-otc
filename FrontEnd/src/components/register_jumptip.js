/**
 * Created by oxchain on 2017/12/04.
 */


import React, { Component } from 'react';

class RegisterJumptip extends Component {
    constructor(props) {
        super(props);
        this.state = {};
    }
    render() {
       const register = localStorage.getItem("registertip");
        return (
            <div className="jumptip">
                <div className="text-center">
                    <h1>{register}</h1>
                </div>
            </div>);
    }
}

export default RegisterJumptip ;