package com.bdcor.util.test;

import org.eclipse.jetty.server.Connector;
import org.eclipse.jetty.server.Handler;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.DefaultHandler;
import org.eclipse.jetty.server.handler.HandlerCollection;
import org.eclipse.jetty.server.nio.SelectChannelConnector;
import org.eclipse.jetty.servlet.ServletContextHandler;

public class StartJetty {

	public static String servletip ="";
	public static void StartJetty(String ip) throws Exception {
		servletip = ip;
		Server server = new Server();
		SelectChannelConnector connector = new SelectChannelConnector();
		connector.setPort(9090);
	    server.setConnectors(new Connector[] { connector });
	    ServletContextHandler context = new ServletContextHandler();
	    context.setContextPath("/");
	    context.addServlet(Template2PdfServlet.class, "/template2Pdf");
	    HandlerCollection handlers = new HandlerCollection();
	    handlers.setHandlers(new Handler[] { context, new DefaultHandler() });
	    server.setHandler(handlers);
	    server.start();
	    server.join();
	}
}
