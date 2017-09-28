package org.schors.filegen;

import com.vaadin.annotations.VaadinServletConfiguration;
import com.vaadin.server.VaadinServlet;

import javax.servlet.annotation.WebServlet;

@WebServlet(asyncSupported = true, urlPatterns = {"/*", "/VAADIN/*"}, name = "filegen")
@VaadinServletConfiguration(ui = TheApp.class, productionMode = false)
public class FGServlet extends VaadinServlet {
}
