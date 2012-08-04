package com.rossallenbell.avatr;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.DefaultServlet;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.eclipse.jetty.webapp.WebAppContext;

public class AvatrServer {

	public static void main(String[] args) throws Exception {

		Server server = new Server(8080);

		ServletContextHandler handler = new ServletContextHandler();
		handler.setResourceBase("src/com/rossallenbell/avatr/webapp");

		DefaultServlet defaultServlet = new DefaultServlet();
		ServletHolder defaultServletHolder = new ServletHolder(defaultServlet);
		defaultServletHolder.setInitParameter("useFileMappedBuffer", "false");

		handler.addServlet(defaultServletHolder, "/");
		handler.addServlet(new ServletHolder(new AvatrWebSocketServlet()), "/ws");

		server.setHandler(handler);

		server.start();
		server.join();
	}

}
