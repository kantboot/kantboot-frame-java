package com.kantboot.util.resolver.resolver;

import jakarta.servlet.ReadListener;
import jakarta.servlet.ServletInputStream;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletRequestWrapper;
import lombok.extern.slf4j.Slf4j;

import java.io.*;
import java.nio.charset.StandardCharsets;

/**
 * 重写HttpServletRequestWrapper
 * <p>
 *     重写HttpServletRequestWrapper的getInputStream方法,使其可以多次读取流
 * <p>
 */
@Slf4j
public class KantbootHttpServletRequestWrapper extends HttpServletRequestWrapper {
    private final byte[] body;

    public KantbootHttpServletRequestWrapper(HttpServletRequest request) {
        super(request);
        request.getParameterMap();
        body = readAsChars(request).getBytes(StandardCharsets.UTF_8);
    }

    /**
     * 解析流
     *
     * @param request ServletRequest
     * @return String
     */
    public static String readAsChars(ServletRequest request) {
        InputStream is = null;
        StringBuilder sb = new StringBuilder();
        try {
            is = request.getInputStream();

            byte[] b = new byte[4096];
            for (int n; (n = is.read(b)) != -1; ) {
                sb.append(new String(b, 0, n));
            }
        } catch (IOException e) {
            log.error("解析流失败", e);
        } finally {
            if (null != is) {
                try {
                    is.close();
                } catch (IOException e) {
                    log.error("解析流失败", e);
                }
            }
        }
        return sb.toString();
    }

    @Override
    public BufferedReader getReader() {
        return new BufferedReader(new InputStreamReader(getInputStream()));
    }

    @Override
    public ServletInputStream getInputStream(){

        final ByteArrayInputStream basis = new ByteArrayInputStream(body);

        return new ServletInputStream() {

            @Override
            public int read(){
                return basis.read();
            }

            @Override
            public boolean isFinished() {
                return false;
            }

            @Override
            public boolean isReady() {
                return false;
            }

            @Override
            public void setReadListener(ReadListener readListener) {

            }
        };
    }
}