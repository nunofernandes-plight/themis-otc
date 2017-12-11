package com.oxchains.themis.common.bitcoin;

/**
 * @author ccl
 * @time 2017-10-31 14:10
 * @name BitcoinConst
 * @desc:
 */
public interface BitcoinConst {
    /**
     * 中继费账户
     */
     String OXCHAINS_DEFAULT_FEE_ACCOUNT = "oxchainsfees";

     String OXCHAINS_DEFAULT_FEE_ADDRESS = "n3hZbBiMHFvyqVTjFcPC2nTsArNzV128vF";
    /**
     * 签名地址账户
     */
    String OXCHAINS_DEFAULT_MULTISIG_ACCOUNT = "multisig";
    /**
     * 公私钥账户
     */
     String OXCHAINS_DEFAULT_KEYS_ACCOUNT = "AllKeys";
    /**
     * 交易费
     */
    double OXCHAINS_DEFAULT_TX_FEE = 0.0001D;
    /**
     * 矿工
     */
    double OXCHAINS_DEFAULT_MINER_FEE = 0.0001D;
    /**
     * 确认block数
     */
     int DEFAULT_CONFIRMATIONS = 6;
    enum VoutHashType implements BitcoinConst{
        PUB_KEY_HASH("pubkeyhash",1),SCRIPT_HASH("scripthash",2);
        private String name;
        private int index;

        VoutHashType(String name, int index){
            this.name=name;
            this.index = index;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public int getIndex() {
            return index;
        }

        public void setIndex(int index) {
            this.index = index;
        }
    }
}
