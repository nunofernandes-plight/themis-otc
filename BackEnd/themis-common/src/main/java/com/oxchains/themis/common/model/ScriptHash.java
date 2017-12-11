package com.oxchains.themis.common.model;

/**
 * @author ccl
 * @time 2017-10-25 18:44
 * @name ScriptHash
 * @desc:
 */
public class ScriptHash {
    private String address;
    private String redeemScript;
    private String URI;

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getRedeemScript() {
        return redeemScript;
    }

    public void setRedeemScript(String redeemScript) {
        this.redeemScript = redeemScript;
    }

    public String getURI() {
        return URI;
    }

    public void setURI(String URI) {
        this.URI = URI;
    }

    public ScriptHash(){}

    public ScriptHash(String address, String redeemScript, String URI) {
        this.address = address;
        this.redeemScript = redeemScript;
        this.URI = URI;
    }

    @Override
    public String toString() {
        return "ScriptHash{" +
                "address='" + address + '\'' +
                ", redeemScript='" + redeemScript + '\'' +
                ", URI='" + URI + '\'' +
                '}';
    }
}
