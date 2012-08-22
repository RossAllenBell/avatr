package com.rossallenbell.avatr;

import java.io.FileInputStream;
import java.util.Properties;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.DefaultServlet;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;

public class AvatrServer {

	public final static Properties SERVER_PROPERTIES = new Properties();

	public static void main(String[] args) throws Exception {

		SERVER_PROPERTIES.load(new FileInputStream("etc/server.properties"));

		Server server = new Server(Integer.parseInt(SERVER_PROPERTIES.getProperty("port", "8080")));

		ServletContextHandler handler = new ServletContextHandler();
		handler.setResourceBase("webapp");

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
