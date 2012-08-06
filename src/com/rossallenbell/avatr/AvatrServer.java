package com.rossallenbell.avatr;

import java.io.FileInputStream;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.DefaultServlet;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;

import com.couchbase.client.CouchbaseClient;

public class AvatrServer {

	public final static Properties PROPERTIES = new Properties();

	public static void main(String[] args) throws Exception {

		PROPERTIES.load(new FileInputStream("etc/server.properties"));

		List<URI> uris = new ArrayList<URI>();
	    uris.add(URI.create("http://127.0.0.1:8091/pools"));
		CouchbaseClient client = null;
		try {
			client = new CouchbaseClient(uris, "default", "");
		} catch (Exception e) {
			System.err.println("Error connecting to Couchbase: " + e.getMessage());
			System.exit(0);
		}
		Long lastStartTime = (Long) client.get("LASTSTARTTIME");
		System.out.println(lastStartTime);
		client.set("LASTSTARTTIME", 0, System.currentTimeMillis());
		client.flush();
		client.shutdown();

		Server server = new Server(Integer.parseInt(PROPERTIES.getProperty("port", "8080")));

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
