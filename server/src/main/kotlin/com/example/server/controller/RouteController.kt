//
//package com.example.server.controller
//
//import jakarta.persistence.criteria.Path
//import org.springframework.beans.factory.annotation.Value
//import org.springframework.stereotype.Controller
//import org.springframework.web.bind.annotation.PathVariable
//import org.springframework.web.bind.annotation.RequestMapping
//import org.springframework.web.servlet.view.RedirectView
//
//private val RouteController.ENVIRONMENT: String
//
//@Controller
//class RouteController {
//    @Value("$ENVIRONMENT")
//    var activeProfile = ""
//
//
//    @RequestMapping( "/{path:[^\\.]*}")
//    fun index(@PathVariable path: String): Any {
//        if (activeProfile = "dev"){
//            val redirectUrl = "http://localhost:8080/$path"
//            return RedirectView(redirectUrl)
//        }
//        return "forward:/"
//    }
//}