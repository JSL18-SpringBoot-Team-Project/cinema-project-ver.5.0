package com.movie.config;

import com.movie.domain.SessionUser;
import jakarta.servlet.http.HttpSession;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

@ControllerAdvice
public class GlobalUserSessionAttribute {
    private final HttpSession session;

    public GlobalUserSessionAttribute(HttpSession session) {
        this.session = session;
    }

    @ModelAttribute("sessionUser")
    public SessionUser populateSessionUser() {
        return (SessionUser) session.getAttribute("USER");
    }
}
