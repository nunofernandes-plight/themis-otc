package com.oxchains.themis.arbitrate.common;

import com.tiemens.secretshare.engine.SecretShare;
import com.tiemens.secretshare.math.BigIntUtilities;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

public class ShamirUtil {
    public static final Integer K = 2;
    public static final Integer N = 3;
    private static final String DESCRIPTION = "this is 4096";
    private static final SecretShare.PublicInfo PUBLIC_INFO = new SecretShare.PublicInfo(N,K,SecretShare.getPrimeUsedFor4096bigSecretPayload(),DESCRIPTION);
    private static final SecretShare SPLIT = new SecretShare(PUBLIC_INFO);
    public static String[] splitAuth(String suth){
        BigInteger secret = BigIntUtilities.Human.createBigInteger(suth);
        SecretShare.SplitSecretOutput split = SPLIT.split(secret);
        List<SecretShare.ShareInfo> shareInfos = split.getShareInfos();
        String[] arr = {"","",""};
        for(int i = 0;i<shareInfos.size();i++){
            arr[i] = shareInfos.get(i).getShare().toString()+"_"+(i+1);
        }
        return arr;
    }
    public static String getAuth(String[] arr){
        List<SecretShare.ShareInfo> lists = new ArrayList<SecretShare.ShareInfo>();

        for(int i = 0;i<arr.length;i++){
            String str = arr[i];
            String st = str.substring(str.lastIndexOf("_")+1);
            String s = str.substring(0,str.lastIndexOf("_"));
            lists.add(new SecretShare.ShareInfo(Integer.parseInt(st) ,new BigInteger(s), new SecretShare.PublicInfo(N, K, SecretShare.getPrimeUsedFor4096bigSecretPayload(), DESCRIPTION)));
        }
        SecretShare.CombineOutput combine = SPLIT.combine(lists);
        BigInteger secret1 = combine.getSecret();
        byte[] bytes = secret1.toByteArray();
        String auth = new String(bytes);
        return auth;
    }

}
