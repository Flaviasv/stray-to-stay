package com.group7.straytostay.controller;

import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.annotation.WebServlet;

import java.io.IOException;

/**
 * Minimal endpoint used to confirm the WAR is deployed and Tomcat is serving requests.
 * GET /health -> 200 "OK"
 *
 * Servlets are stateless: no instance fields, all per-request data lives in local variables.
 */
@WebServlet("/health")
public class HealthServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.setContentType("text/plain");
        resp.setCharacterEncoding("UTF-8");
        resp.getWriter().write("OK");
    }
}
