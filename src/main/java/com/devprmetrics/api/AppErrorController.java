package com.devprmetrics.api;

import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.stereotype.Controller;

@Controller
public class AppErrorController implements ErrorController {
//
//    @GetMapping("/error")
//    public String handleError(HttpServletRequest request, Model model) {
//        Object status = request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);
//
//        if (status != null) {
//            int statusCode = Integer.parseInt(status.toString());
//
//            if (statusCode == HttpStatus.NOT_FOUND.value()) {
//                model.addAttribute("status", HttpStatus.NOT_FOUND);
//                return "404";
//            }
//        }
//
//        model.addAttribute("status", HttpStatus.INTERNAL_SERVER_ERROR);
//        return "404";
//    }
}
