package com.liudonghua.tutorials.simpleweb.controller;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import com.liudonghua.tutorials.simpleweb.dao.UserDao;
import com.liudonghua.tutorials.simpleweb.dao.UserDaoImpl;
import com.liudonghua.tutorials.simpleweb.model.User;

/**
 * Servlet implementation class UserController
 */
@WebServlet(name="UserController", urlPatterns="/users/*")
public class UserController extends HttpServlet {
	private static final long serialVersionUID = 1L;

	private Pattern regexpAllPattern = Pattern.compile("/users");
	private Pattern regexpSinglePattern = Pattern.compile("/users/([0-9]+)");
	private Pattern regexpUserStatisticsPattern = Pattern.compile("/users/statistics");

	private UserDao userDao = new UserDaoImpl();
	
	private boolean isUserStatisticsRequest(HttpServletRequest request) {
		Matcher matcher;
		String pathInfo = getPathInfo(request);
		matcher = regexpUserStatisticsPattern.matcher(pathInfo);
		if (matcher.find()) {
			return true;
		} 
		return false;
	}

	private int isSingleUserInfoRequest(HttpServletRequest request) {
		Matcher matcher;
		String pathInfo = getPathInfo(request);
		matcher = regexpSinglePattern.matcher(pathInfo);
		int id = -1;
		if (matcher.find()) {
			id = Integer.parseInt(matcher.group(1));
		} else {
			matcher = regexpAllPattern.matcher(pathInfo);
			if (matcher.find()) {
				id = 0;
			}
		}

		return id;
	}

	private String getPathInfo(HttpServletRequest request) {
		//		String pathInfo = request.getPathInfo();
				String requestURI = request.getRequestURI();
				String contextPath = request.getContextPath();
				String pathInfo = requestURI.substring(requestURI.lastIndexOf(contextPath) + contextPath.length());
		return pathInfo;
	}

	public static String getBody(HttpServletRequest request) throws IOException {

		String body = null;
		StringBuilder stringBuilder = new StringBuilder();
		BufferedReader bufferedReader = null;

		try {
			InputStream inputStream = request.getInputStream();
			if (inputStream != null) {
				bufferedReader = new BufferedReader(new InputStreamReader(
						inputStream, StandardCharsets.UTF_8));
				char[] charBuffer = new char[128];
				int bytesRead = -1;
				while ((bytesRead = bufferedReader.read(charBuffer)) > 0) {
					stringBuilder.append(charBuffer, 0, bytesRead);
				}
			} else {
				stringBuilder.append("");
			}
		} catch (IOException ex) {
			throw ex;
		} finally {
			if (bufferedReader != null) {
				try {
					bufferedReader.close();
				} catch (IOException ex) {
					throw ex;
				}
			}
		}

		body = stringBuilder.toString();
		return body;
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		// set HTTP ContentType header of "application/json"
		response.setContentType("application/json");
		response.addHeader("Access-Control-Allow-Origin", "*");
		PrintWriter out = response.getWriter();
		boolean isUserStatisticsRequest = isUserStatisticsRequest(request);
		int id = isSingleUserInfoRequest(request);
		// request user statistics info
		if (isUserStatisticsRequest) {
			Map<String,String> statisticsInfo = userDao.getUserStatisticsInfo();
			JSONObject jsonObject = JSONObject.fromObject(statisticsInfo);
			out.print(jsonObject.toString());
		}
		// request all user info
		else if (id == 0) {
			List<User> users = userDao.getAllUsers();
			JSONArray jsonObject = JSONArray.fromObject(users);
			out.print(jsonObject.toString());
		}
		// request a single user info
		else if (id != -1) {
			User user = userDao.getUserById(id);
			JSONObject jsonObject = JSONObject.fromObject(user);
			out.print(jsonObject.toString());
		}
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("application/json");
		response.addHeader("Access-Control-Allow-Origin", "*");
		PrintWriter out = response.getWriter();
		String json = getBody(request);
		JSONObject jsonObject = JSONObject.fromObject(json);
		User user = (User) JSONObject.toBean(jsonObject, User.class);
		int id = userDao.addUser(user);
		User insertedUser = userDao.getUserById(id);
		JSONObject userJson = JSONObject.fromObject(insertedUser);
		out.print(userJson.toString());
	}

	/**
	 * @see HttpServlet#doPut(HttpServletRequest, HttpServletResponse)
	 */
	protected void doPut(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("application/json");
		response.addHeader("Access-Control-Allow-Origin", "*");
		PrintWriter out = response.getWriter();
		String json = getBody(request);
		JSONObject jsonObject = JSONObject.fromObject(json);
		User user = (User) JSONObject.toBean(jsonObject, User.class);
		userDao.updateUser(user);
		User insertedUser = userDao.getUserById(user.getId());
		JSONObject userJson = JSONObject.fromObject(insertedUser);
		out.print(userJson.toString());
	}

	/**
	 * @see HttpServlet#doDelete(HttpServletRequest, HttpServletResponse)
	 */
	protected void doDelete(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("application/json");
		response.addHeader("Access-Control-Allow-Origin", "*");
		PrintWriter out = response.getWriter();
		int id = isSingleUserInfoRequest(request);
		if (id > 0) {
			userDao.deleteUser(id);
			out.print("{\"msg\":\"successful\"}");
		}
	}

	@Override
	protected void doOptions(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// http://stackoverflow.com/questions/1099787/jquery-ajax-post-sending-options-as-request-method-in-firefox
		response.addHeader("Access-Control-Allow-Origin", "*");
		response.addHeader("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS");
		response.addHeader("Access-Control-Max-Age", "86400");
		response.addHeader("Access-Control-Allow-Headers", "User-Agent,Origin,Cache-Control,Content-Type,x-zd,Date,Server,withCredentials");
		super.doOptions(request, response);
	}

}
