/*
 * Copyright 2015 - Chris Phillipson
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 *
 * You may obtain a copy of the License at
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.fns.grivet.controller;

import java.util.Arrays;

import javax.servlet.http.HttpServletRequest;

import org.json.JSONObject;

/**
 * Creates a message from a {@code JSONObject} or {@code HttpServletRequest}
 * and optionally supplied {@code String[]} arguments suitable for logging.
 *  
 * @author Chris Phillipson
 *
 */
class LogUtil {

    static String toLog(JSONObject jsonObject, String... args) {
        StringBuffer sb = new StringBuffer();
        if (args != null && args.length > 0) {
            Arrays.stream(args).forEach(a -> sb.append(a));
        }
        sb.append("--\n");
        sb.append(jsonObject.toString());
        sb.append("--\n");
        String message = sb.toString();
        return message;
    }
    
    static String toLog(HttpServletRequest request, String... args) {
        StringBuffer sb = new StringBuffer();
        if (args != null && args.length > 0) {
            Arrays.stream(args).forEach(a -> sb.append(a));
        }
        sb.append("--\n");
        sb.append(request.getParameterMap().toString());
        sb.append("--\n");
        String message = sb.toString();
        return message;
    }
} 
