package thunderbirdsonly.nwhackbackend.interceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;
import thunderbirdsonly.nwhackbackend.Pojo.Result;
import thunderbirdsonly.nwhackbackend.Utility.JwtUtils;


@Component
public class LoginCheckInterceptor implements HandlerInterceptor {
    @Override //pre processing
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String requestURI = request.getRequestURI();
        if (requestURI.equals("/api/login") || requestURI.equals("/api/register")) {
            return true;
        }

        String jwt = request.getHeader("token");
        if (!StringUtils.hasLength(jwt)) {
            Result r = Result.error("Not Login");
            String json = "{"
                    + "\"status\": 0,"
                    + "\"message\": \"Not Login\","
                    + "\"data\": null"
                    + "}";
            response.getWriter().write(json);
            return false;
            //return true;
        }

        try {
            JwtUtils.decodeToken(jwt);
        } catch (Exception e) {
            Result r = Result.error("Invalid Jwt");
            String json = "{"
                    + "\"status\": 0,"
                    + "\"message\": \"Invalid Jwt\","
                    + "\"data\": null"
                    + "}";

            response.getWriter().write(json);
            // return true;
            return false;
        }

        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, @Nullable ModelAndView modelAndView) throws Exception {
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, @Nullable Exception ex) throws Exception {
    }

}
