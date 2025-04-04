package com.apdbank.user.utils;

import jakarta.servlet.http.HttpServletRequest;

import java.util.UUID;

public class Utils {

   public static  String generateRandomUUID(){
       return UUID.randomUUID().toString();
   }
   public static  String getApplicationUrl(HttpServletRequest request){

       return request.getRequestURI().toString().replace(request.getServletPath(),"");
   }
}
