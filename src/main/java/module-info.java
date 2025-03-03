module RHCONNECT {
    requires javafx.graphics;
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires java.mail;
    requires twilio;
    requires com.google.zxing;
    requires com.google.zxing.javase;
    requires com.google.api.client.extensions.java6.auth;
    requires com.google.api.client.extensions.jetty.auth;
    requires com.google.api.client.auth;
    requires google.api.services.oauth2.v2.rev157;
    requires google.api.client;
    requires com.google.api.client;
    requires com.google.api.client.json.jackson2;
    requires javafx.web;
    requires org.apache.httpcomponents.client5.httpclient5;
    requires org.apache.httpcomponents.core5.httpcore5;
    requires org.json;
    requires java.desktop;
    opens  controllers;
    opens entities to javafx.base;
}