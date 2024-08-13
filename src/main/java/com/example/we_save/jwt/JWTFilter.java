package com.example.we_save.jwt;

import com.example.we_save.domain.user.entity.User;
import com.example.we_save.jwt.dto.CustomUserDetails;
import com.example.we_save.jwt.exception.code.JWTErrorCode;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.security.SignatureException;

public class JWTFilter extends OncePerRequestFilter {

    private final JWTUtil jwtUtil;

    public JWTFilter(JWTUtil jwtUtil) {

        this.jwtUtil = jwtUtil;
    }


    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        try {
            //request에서 Authorization 헤더를 찾음
            String authorization = request.getHeader("Authorization");

            //Authorization 헤더 검증
            if (authorization == null || !authorization.startsWith("Bearer ")) {
                filterChain.doFilter(request, response);

                //조건이 해당되면 메소드 종료 (필수)
                return;
            }

            String token = authorization.split(" ")[1];

            //토큰 소멸 시간 검증
            if (jwtUtil.isExpired(token)) {

                System.out.println("token expired");
                filterChain.doFilter(request, response);

                //조건이 해당되면 메소드 종료 (필수)
                return;
            }


            String username = jwtUtil.getUsername(token);
            String role = jwtUtil.getRole(token);

            User userEntity = new User();
            userEntity.setPhoneNum(username);
            userEntity.setPassword("temppassword");
            userEntity.setRole(role);

            CustomUserDetails customUserDetails = new CustomUserDetails(userEntity);

            Authentication authToken = new UsernamePasswordAuthenticationToken(customUserDetails, null, customUserDetails.getAuthorities());

            SecurityContextHolder.getContext().setAuthentication(authToken);
        }catch (SecurityException | MalformedJwtException e) {
            request.setAttribute("exception", JWTErrorCode.WRONG_TYPE_TOKEN.getCode());
        } catch (ExpiredJwtException e) {
            request.setAttribute("exception", JWTErrorCode.EXPIRED_TOKEN.getCode());
        } catch (UnsupportedJwtException e) {
            request.setAttribute("exception", JWTErrorCode.UNSUPPORTED_TOKEN.getCode());
        } catch (IllegalArgumentException e) {
            request.setAttribute("exception", JWTErrorCode.WRONG_TYPE_TOKEN.getCode());
        } catch (Exception e) {
            System.out.println(e.getMessage());
            request.setAttribute("exception", JWTErrorCode.UNKNOWN_ERROR.getCode());
        }
        filterChain.doFilter(request, response);
    }

}
