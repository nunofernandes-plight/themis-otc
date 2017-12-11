package com.oxchains.themis.common.model;

import java.io.Serializable;

/**
 * @author ccl
 * @time 2017-10-20 18:21
 * @name AddressKeys
 * @desc:
 */
public class AddressKeys implements Serializable{
    private String address;
    private String publicKey;
    private String privateKey;

    public AddressKeys(){}

    public AddressKeys(String address,String publicKey,String privateKey){
        this.address=address;
        this.publicKey=publicKey;
        this.privateKey=privateKey;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPublicKey() {
        return publicKey;
    }

    public void setPublicKey(String publicKey) {
        this.publicKey = publicKey;
    }

    public String getPrivateKey() {
        return privateKey;
    }

    public void setPrivateKey(String privateKey) {
        this.privateKey = privateKey;
    }
}
