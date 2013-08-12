package com.lazyloris.mcp.application;

import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class McpServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;

	private static final String TOKEN = "hzywind";

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String signature = req.getParameter("signature");
		String timestamp = req.getParameter("timestamp");
		String nonce = req.getParameter("nonce");
		String echostr = req.getParameter("echostr");
		
		System.out.println("============================");
		System.out.println("signature:" + signature);
		System.out.println("timestamp:" + timestamp);
		System.out.println("nonce:" + nonce);
		System.out.println("echostr:" + echostr);

		List<String> list = new ArrayList<String>();
		list.add(TOKEN);
		list.add(timestamp);
		list.add(nonce);

		

		String digested;
		try {
			digested = digest(list);
		} catch (Exception e) {
			throw new ServletException(e);
		}
		
		if (signature.equals(digested)) {
			resp.getWriter().write(echostr);
		}
	}

	private static String digest(List<String> list) throws Exception {
		
		Collections.sort(list);
		
		StringBuilder builder = new StringBuilder();

		for (String str : list) {
			builder.append(str);
		}

		MessageDigest digest = null;
		try {
			digest = MessageDigest.getInstance("SHA-1");
		} catch (NoSuchAlgorithmException e) {
			throw new ServletException(e);
		}
		digest.update(builder.toString().getBytes("UTF-8"));
		String digested = new String(digest.digest(), "UTF-8");
		return digested;
	}
	
	public static void main(String[] args) throws Exception {
		List<String> list = new ArrayList<String>();
		list.add("ccccccc");
		list.add("aaaaaaa");
		list.add("bbbbbbb");
		System.out.println(digest(list));
	}

}
