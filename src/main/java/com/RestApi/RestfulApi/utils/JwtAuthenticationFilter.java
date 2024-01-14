package com.RestApi.RestfulApi.utils;

import com.RestApi.RestfulApi.serviceImpl.UserServiceImpl;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    private JwtUtils utils;
    private UserServiceImpl userService;

    @Autowired
    public JwtAuthenticationFilter(JwtUtils utils, @Lazy UserServiceImpl userService) {
        this.utils = utils;
        this.userService = userService;
    }
    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response, @NonNull FilterChain filterChain) throws ServletException, IOException {


        //FIRST STEP - CHECK THE AUTHORIZATION HEADER IS NOT EMPTY AND STARTS WITH BEARER, THEN SET TOKEN AND USERNAME EXTRACTED FROM THEM
        //We need to initialize some variables as Null
        String token = null;
        String authorizationHeader = null;
        String username = null;
        UserDetails userDetail = null;

        //SET THE AUTHORIZATION HEADER
        //WE SET THE VALUE WE GOT FROM THE "AUTHORIZATION" INTO THE authorizationHeader
        authorizationHeader = request.getHeader("Authorization");

        if(authorizationHeader != null && authorizationHeader.startsWith("Bearer ")){
            token = authorizationHeader.substring(3);
            username = utils.extractUsername.apply(token);//USERNAME GOTTEN FROM THE TOKEN
        }

        //SECOND STEP - CHECK IF THE USERNAME EXTRACTED FROM THE TOKEN IS NOT SET IN SECURITYCONTEXTHOLDER, IF TRUE THEN SET THE USERDETAILS GOTTEN FROM DATABASE FOR THAT USER TO USERDETAILS
        if(username != null && SecurityContextHolder.getContext().getAuthentication() == null){
            userDetail = userService.loadUserByUsername(username);

            // THIRD STEP - CHECK THE VALIDITY OF THE TOKEN AGAINST THE USERNAME EXTRACTED FROM THE USER DETAILS ; IF  THE TOKEN IS STILL VALID,
            //THEN CREATE A NEW UPATOKEN WITH THE USERDETAILS, PASSWORD SET AS NULL, AND AUTHORITIES OF THE USER
            if(userDetail != null && utils.isTokenValid.apply(token, userDetail.getUsername())){
                UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken =
                        new UsernamePasswordAuthenticationToken(userDetail, null, userDetail.getAuthorities());

                //FOURTH STEP - SET MORE INFORMATION SUCH AS THE REMOTE ADDRESS AND SESSION IO TO THE UPATOKEN USING THE "WEBAUTHENTICATIONDETAILSOURCE".
                usernamePasswordAuthenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                //FIFTH STEP - FINALLY SET THE UPAToken WITH THE userDetails and other information to the "SecurityContextHolder" so Spring Security knows this user is now authenticated
                SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);

                //Using filterChain.doFilter(request, response) to continue to next request
                filterChain.doFilter(request, response);

            }
        }

    }
}
