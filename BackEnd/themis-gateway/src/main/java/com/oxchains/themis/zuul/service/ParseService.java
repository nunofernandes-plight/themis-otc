package com.oxchains.themis.zuul.service;

import com.oxchains.themis.common.util.ObjectByteUtil;
import com.oxchains.themis.repo.dao.TokenKeyDao;
import com.oxchains.themis.repo.entity.TokenKey;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.impl.DefaultJwtParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.security.PublicKey;
import java.util.concurrent.TimeUnit;

/**
 * 解析token
 *
 * @author luoxuri
 * @create 2017-11-28 14:49
 **/
@Service
public class ParseService {

    private final Logger LOG = LoggerFactory.getLogger(ParseService.class);

    private PublicKey publicKey;

    @Resource
    private TokenKeyDao tokenKeyDao;

    @Resource
    private RedisTemplate redisTemplate;

    public boolean parse(String token){
        try {
            if (publicKey == null){
                TokenKey tokenKey = tokenKeyDao.findOne(1L);
                if (tokenKey == null){
                    LOG.error("非法TOKEN，拒绝请求");
                    return false;
                } else {
                    LOG.info("正在获取公钥");
                    byte[] pubKey = tokenKey.getPubKey();
                    publicKey = (PublicKey) ObjectByteUtil.toObject(pubKey);
                }
            }

            Jws<Claims> jws = new DefaultJwtParser().setSigningKey(publicKey).parseClaimsJws(token);
            Claims claims = jws.getBody();
            String subject = claims.getSubject();

            ValueOperations operations = redisTemplate.opsForValue();
            String redisToken = (String) operations.get(subject);
            LOG.info("REDIS 中获取的 TOKEN ：" + redisToken);
            if (token.equals(redisToken)){
                LOG.info("TOKEN 验证成功");
                // 给当前访问token重新设置失效时间
                // redisTemplate.expire(subject, 7, TimeUnit.DAYS);
                return true;
            } else {
                LOG.info("TOKEN 验证失败");
                return false;
            }
        } catch (Exception e){
            LOG.error("解析Token异常", e);
        }
        return false;
    }


}
