package com.oxchains.themis.zuul;

import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.oxchains.themis.repo.dao.UserDao;
import com.oxchains.themis.zuul.service.ParseService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

/**
 * @author ccl
 * @time 2017-11-09 17:36
 * @name AccessFilter
 * @desc:
 */

@Component
public class AccessFilter extends ZuulFilter{

    private final Logger LOG = LoggerFactory.getLogger(AccessFilter.class);

    @Resource
    private ParseService parseService;

    /**
     * pre：请求执行之前的filter
     * route：处理请求，进行路由
     * post：请求处理完成后执行的filter
     * error：出现错误是执行的filter
     */
    @Override
    public String filterType() {
        return "pre";
    }

    /**
     * filter执行顺序，通过数字指定，优先级,数字越大,优先级越低
     */
    @Override
    public int filterOrder() {
        return 0;
    }

    /**
     * filter是否需要执行，true：执行，false：不执行
     */
    @Override
    public boolean shouldFilter() {
        return true;
    }

    /**
     * filter具体逻辑
     */
    @Override
    public Object run() {
        try {
            RequestContext rcx = RequestContext.getCurrentContext();
            HttpServletRequest request = rcx.getRequest();
            String url = request.getRequestURI();

            //String token = request.getParameter("Authorization");
            String token = request.getHeader("Authorization");
//        LOG.log(Level.FINE,"Authorization token: {}",token);
            if(null == token){
                LOG.info("当前请求没有携带 TOKEN");
                if("/themis-user/user/login".equals(url) || "themis-user/user/register".equals(url)){
                    LOG.info("请求有效，放行");
                }else {
                    //过滤该请求，不往下级服务去转发请求，到此结束
                    rcx.setSendZuulResponse(false);
                    rcx.setResponseStatusCode(401);
                    rcx.setResponseBody("{}");
                    LOG.error("请求无效");
                    return null;

                }
            } else {
                LOG.info("当前请求携带 TOKEN ：{}" , token);
                boolean isSuccess = parseService.parse(token);
                if (isSuccess){
                    LOG.info("请求有效，放行");
                } else {
                    rcx.setSendZuulResponse(false);
                    rcx.setResponseStatusCode(401);
                    rcx.setResponseBody("{}");
                    LOG.error("请求无效");
                    return null;
                }
            }
            //如果有token，则进行路由转发
            LOG.info("Authorized,continue...");
            //这里return的值没有意义，zuul框架没有使用该返回值
            return null;
        }catch (Exception e){
            LOG.error("Zuul filter 异常", e);
        }
        return null;
    }
}
