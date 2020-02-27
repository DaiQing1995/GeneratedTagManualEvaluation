package com.dq.server;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.dq.entity.ContainerInfo;
import com.dq.utils.DBUitls;

@Controller
public class TagASMTController {

	private List<ContainerInfo> ctns;

	@ResponseBody
	@RequestMapping("/start")
	public String markStart() {
		ctns = DBUitls.Get_All_Tags();
		return "ctn sum:" + ctns.size();
	}

	@ResponseBody
	@RequestMapping("/getCtnNameDesc")
	public String getShortDesc(String ctnName) {
		return "test get short desc" + ctns.get(0).getName() + ": " + ctns.get(0).getShortDesc();
	}

}
