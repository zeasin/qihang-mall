package cn.qihangerp.mall.config;

import cn.qihangerp.utils.JwtUtils;
import com.alibaba.fastjson2.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@Component
public class JwtInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // 从请求头中获取 Token
        String token = request.getHeader("Authorization");
        log.info("======访问："+request.getRequestURI()+"====TOKEN:"+token);
        if (token != null ) {
            if( token.startsWith("Bearer ")) {
                token = token.substring(7);
            }
            if (JwtUtils.validateToken(token)) {
                // 如果 Token 有效，继续请求
                String username = JwtUtils.getUsernameFromToken(token);
                // 你可以将用户名或者其他信息存储到请求中
                request.setAttribute("username", username);
                return true;
            } else {
                // Token 无效，返回 401
                response.setStatus(HttpServletResponse.SC_OK);
                Map<String,Object> map = new HashMap<>();
                map.put("code",401);
                map.put("msg","Unauthorized");
                response.getWriter().write(JSONObject.toJSONString(map));
                return false;
            }
        } else {
            // 如果没有 Token，返回 401
//            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
//            response.getWriter().write("Unauthorized");
            response.setStatus(HttpServletResponse.SC_OK);
            Map<String,Object> map = new HashMap<>();
            map.put("code",401);
            map.put("msg","Unauthorized");
            response.getWriter().write(JSONObject.toJSONString(map));
            return false;
        }
    }
}
