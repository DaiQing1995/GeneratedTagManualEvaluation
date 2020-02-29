package com.dq.controller;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;
import com.dq.server.ContainerInfoServer;

@Controller
public class TagASMTController {

	// TODO: add client cookie or uuid
	private ContainerInfoServer ctnServer;

	@ResponseBody
	@RequestMapping("/start")
	public String markStart() {
		ctnServer = new ContainerInfoServer();
		JSONObject ret;
		try {
			ret = ctnServer.getNextCtnTags();
		} catch (IOException e) {
			e.printStackTrace();
			return "error";
		}
		return ret.toJSONString();
	}

	@ResponseBody
	@RequestMapping(value = "/deny", method = RequestMethod.POST)
	public String denyTag(HttpServletRequest request) throws Exception {
		ctnServer.denyTag(request.getParameter("ctnname"), request.getParameter("tag"));
		return ctnServer.getNextCtnTags().toJSONString();
	}

	@ResponseBody
	@RequestMapping(value = "/accept", method = RequestMethod.POST)
	public String acceptTag(HttpServletRequest request) throws Exception {
		ctnServer.acceptTag(request.getParameter("ctnname"), request.getParameter("tag"));
		return ctnServer.getNextCtnTags().toJSONString();
	}
}
