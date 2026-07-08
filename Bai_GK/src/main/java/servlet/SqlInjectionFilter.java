package servlet;
 
import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
 
import java.io.IOException;
import java.util.Enumeration;
import java.util.regex.Pattern;
 

@WebFilter("/*")
public class SqlInjectionFilter implements Filter {
 
    private static final Pattern SQL_PATTERN = Pattern.compile(
            "(?i)(\\b(SELECT|INSERT|DROP|UNION|EXEC|ALTER|CREATE|TRUNCATE)\\b" +
            "|(--)|(;)|(/\\*)|(\\*/)|(\\bOR\\b\\s+1\\s*=\\s*1)|('\\s*OR\\s*'))"
    );
 
    @Override
    public void init(FilterConfig filterConfig) {
    }
 
    @Override
    public void doFilter(ServletRequest request, ServletResponse response,
                          FilterChain chain) throws IOException, ServletException {
 
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse resp = (HttpServletResponse) response;
 
        Enumeration<String> paramNames = req.getParameterNames();
        while (paramNames.hasMoreElements()) {
            String paramName = paramNames.nextElement();
            String[] values = req.getParameterValues(paramName);
            if (values == null) continue;
 
            for (String value : values) {
                if (value != null && SQL_PATTERN.matcher(value).find()) {
                    resp.setContentType("text/html;charset=UTF-8");
                    resp.getWriter().write(
                        "<h3>Yêu cầu bị từ chối: phát hiện nội dung nghi ngờ SQL Injection.</h3>"
                    );
                    return;
                }
            }
        }
 
        chain.doFilter(request, response);
    }
 
    @Override
    public void destroy() {
    }
}
 
