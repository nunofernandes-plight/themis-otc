/**
 * Created by oxchain on 2017/12/04.
 */


import React, { Component } from 'react';

class JumpTip extends Component {

    render() {
        // const tip = localStorage.getItem("tip");
        const message = localStorage.getItem("message");
        return (
            <div className="jumptip">
                <div className="text-center">
                    <h1>{message}</h1>
                </div>
            </div>);
    }
}

export default JumpTip;