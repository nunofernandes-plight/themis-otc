/**
 * Created by oxchain on 2017/12/04.
 */


import React, { Component } from 'react';
import { connect } from 'react-redux';
import { RegisterJumptipAction } from '../actions/auth';

class EmailIslive extends Component {
    constructor(props) {
        super(props);
        this.state = {};
    }
    componentWillMount(){
        const email = this.props.location.search.slice(7);
     this.props.RegisterJumptipAction({email});
    }
    renderAlert(){
        const data = this.props.all || [];
        const islive = data.message;
        if(data){
            return (
                <h1>{islive}</h1>
            );
        }else{
            return (
                <h1>loading...</h1>
            );
        }
    }
    render() {
        return (
            <div className="islivetip">
                <div className="text-center">
                   {this.renderAlert()}
                    <div className="gologin">
                        <a href='/signin'>立即登录</a>
                     </div>
                </div>
            </div>);
    }
}
function mapStateToProps(state) {
    console.log(state.auth.all);
    return {
        all: state.auth.all
    };
}

export default connect(mapStateToProps, {RegisterJumptipAction})(EmailIslive) ;