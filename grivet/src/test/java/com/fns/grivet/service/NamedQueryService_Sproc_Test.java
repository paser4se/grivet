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
package com.fns.grivet.service;

import java.io.IOException;
import java.sql.Timestamp;
import java.time.LocalDateTime;

import org.apache.commons.io.FileUtils;
import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fns.grivet.TestInit;
import com.fns.grivet.query.NamedQuery;

@WebAppConfiguration
@ActiveProfiles(value={"hsqldb"})
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = TestInit.class)
public class NamedQueryService_Sproc_Test {

private final PathMatchingResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
    
    @Autowired
    private ClassRegistryService classRegistryService;
    
    @Autowired
    private NamedQueryService namedQueryService;
    
    @Autowired
    private ObjectMapper objectMapper;
    
    @Before
    public void setUp() throws IOException {
        Resource r = resolver.getResource("classpath:TestType.json");
        String json = FileUtils.readFileToString(r.getFile());
        JSONObject payload = new JSONObject(json);
        classRegistryService.register(payload);
    }
    
    @Test
    public void testCreateThenGet_happyPath() throws IOException {
        Resource r = resolver.getResource("classpath:TestSprocQuery.json");
        String json = FileUtils.readFileToString(r.getFile());
        NamedQuery namedQuery = objectMapper.readValue(json, NamedQuery.class);
        namedQueryService.create(namedQuery);
        
        MultiValueMap<String, Object> params = new LinkedMultiValueMap<>();
        Timestamp tomorrow = Timestamp.valueOf(LocalDateTime.now().plusDays(1));
        params.add("createdTime", tomorrow);
        String result = namedQueryService.get("sproc.getAttributesCreatedBefore", params);
        JSONArray arrResult = new JSONArray(result);
        Assert.assertTrue("Result should contain 7 attributes", arrResult.length() == 7);
    }
    
    @Test(expected=IllegalArgumentException.class)
    public void testGet_queryNotFound() throws IOException {
        MultiValueMap<String, Object> params = new LinkedMultiValueMap<>();
        Timestamp tomorrow = Timestamp.valueOf(LocalDateTime.now().plusDays(1));
        params.add("createdTime", tomorrow);
        namedQueryService.get("sproc.getAttributesCreatedBefore", params);
    }
    
    @Test(expected=IllegalArgumentException.class)
    public void testCreateThenGet_paramsNotSupplied() throws IOException {
        Resource r = resolver.getResource("classpath:TestSprocQuery.json");
        String json = FileUtils.readFileToString(r.getFile());
        NamedQuery namedQuery = objectMapper.readValue(json, NamedQuery.class);
        namedQueryService.create(namedQuery);
        
        namedQueryService.get("sproc.getAttributesCreatedBefore", null);
    }
    
    @Test(expected=IllegalArgumentException.class)
    public void testCreateThenGet_incorrectParamsSupplied() throws IOException {
        Resource r = resolver.getResource("classpath:TestSprocQuery.json");
        String json = FileUtils.readFileToString(r.getFile());
        NamedQuery namedQuery = objectMapper.readValue(json, NamedQuery.class);
        namedQueryService.create(namedQuery);
        
        MultiValueMap<String, Object> params = new LinkedMultiValueMap<>();
        Timestamp tomorrow = Timestamp.valueOf(LocalDateTime.now().plusDays(1));
        params.add("timeCreated", tomorrow);
        namedQueryService.get("sproc.getAttributesCreatedBefore", params);
    }
    
    @After
    public void tearDown() {
        classRegistryService.deregister("TestType");
        namedQueryService.delete("sproc.getAttributesCreatedBefore");
    }
}
