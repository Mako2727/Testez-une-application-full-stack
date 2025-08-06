package com.openclassrooms.starterjwt.security.jwt;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.core.AuthenticationException;

import java.io.IOException;
import java.util.Map;

import javax.servlet.ServletException;

public class AuthEntryPointJwtTest {

    @Test
    void testCommence_shouldSetResponseAndLogError() throws IOException, ServletException {
        AuthEntryPointJwt entryPoint = new AuthEntryPointJwt();

        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setServletPath("/api/test");

        MockHttpServletResponse response = new MockHttpServletResponse();

        AuthenticationException authException = mock(AuthenticationException.class);
        when(authException.getMessage()).thenReturn("Unauthorized error message");

        entryPoint.commence(request, response, authException);

       
        assertEquals(401, response.getStatus());

        
        assertEquals("application/json", response.getContentType());

        
        String content = response.getContentAsString();

        ObjectMapper mapper = new ObjectMapper();
        Map<String, Object> jsonMap = mapper.readValue(content, java.util.Map.class);

        assertEquals(401, jsonMap.get("status"));
        assertEquals("Unauthorized", jsonMap.get("error"));
        assertEquals("Unauthorized error message", jsonMap.get("message"));
        assertEquals("/api/test", jsonMap.get("path"));
    }
}