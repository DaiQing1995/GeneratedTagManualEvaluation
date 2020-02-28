package com.dq.controller;


import java.io.IOException;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;
import com.dq.server.ContainerInfoServer;

@Controller
public class TagASMTController {

	//TODO: add client cookie or uuid
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
	@RequestMapping(value = "/deny/{ctnname}/{tag}", method = RequestMethod.GET)
	public String denyTag(@PathVariable("ctnname") String ctnname, @PathVariable("tag") String tag) throws Exception{
		ctnServer.denyTag(ctnname, tag);
		return ctnServer.getNextCtnTags().toJSONString();
	}
	
	@ResponseBody
	@RequestMapping(value = "/accept/{ctnname}/{tag}", method = RequestMethod.GET)
	public String acceptTag(@PathVariable("ctnname") String ctnname, @PathVariable("tag") String tag) throws Exception{
		ctnServer.acceptTag(ctnname, tag);
		return ctnServer.getNextCtnTags().toJSONString();
	}

}
