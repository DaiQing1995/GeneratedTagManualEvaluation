package com.dq.server;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.alibaba.fastjson.JSONObject;
import com.dq.entity.ContainerInfo;
import com.dq.entity.TagType;
import com.dq.utils.DBUitls;
import com.dq.utils.UrlDecodeUtils;

/**
 * Server layer for container tag information
 * 
 * @author DaiQing
 *
 */
public class ContainerInfoServer {

	private List<ContainerInfo> ctns;
	private List<String> currentCtnTags;
	private ContainerInfo currentCtn;

	// index of currentCtnTags
	private int tagIndex;
	// index of ctns
	private int ctnIndex;

	/**
	 * Initialize all the fields.Including: container infos, All tags, Current
	 * container, Indexes
	 */
	public ContainerInfoServer() {
		currentCtnTags = new ArrayList<String>();
		ctns = DBUitls.Get_Tags();
		if (ctns.size() == 0)
			return;
		ctnIndex = 0;
		tagIndex = 0;
		currentCtn = ctns.get(ctnIndex);
		updateCurrentCtnTags();
	}

	/**
	 * According to current Container to update tag list
	 * 
	 * @return
	 */
	private void updateCurrentCtnTags() {
		Set<String> currentCtnTagSet = new HashSet<String>();
		for (Map<String, Boolean> tagValid : currentCtn.getMultiTypesTags().values()) {
			for (String tag : tagValid.keySet()) {
				currentCtnTagSet.add(tag);
			}
		}
		// Empty current tags for current container
		currentCtnTags.clear();
		for (String tag : currentCtnTagSet) {
			currentCtnTags.add(tag);
		}
	}

	/**
	 * return next tag to server for check.
	 * 
	 * @return
	 * @throws IOException save tag error
	 */
	public JSONObject getNextCtnTags() throws IOException {
		JSONObject ret = new JSONObject();
		// finished a container's tags
		if (tagIndex >= currentCtnTags.size()) {
			currentCtn.saveTagInfo();// save current one
			do {
				ctnIndex++;
				tagIndex = 1;
				currentCtn = ctns.get(ctnIndex);
				updateCurrentCtnTags();
			} while (currentCtnTags.size() == 0 && ctnIndex < ctns.size());
			if (ctnIndex >= ctns.size()) {
				return null;
			}
		} else {// Current container still has tag need to be check
			tagIndex++;
		}
		System.out.println("tag index:" + tagIndex);
		ret.put("name", currentCtn.getName());
		ret.put("short_desc", currentCtn.getShortDesc());
		ret.put("tag", currentCtnTags.get(tagIndex - 1));
		return ret;
	}

	/**
	 * set tag wrong
	 * 
	 * @param ctnname
	 *            container name
	 * @param tag
	 *            wrong tag
	 * @throws Exception
	 *             server is not synchronized with client
	 */
	public void denyTag(String ctnname, String tag) throws Exception {
		ctnname = UrlDecodeUtils.decodeData(ctnname);
		if (!ctnname.equals(currentCtn.getName())) {
			throw new Exception("client is not synchronized with server");
		}
		for (Map<String, Boolean> tags : currentCtn.getMultiTypesTags().values()) {
			if (tags.containsKey(tag)) {
				tags.put(tag, false);
			}
		}
	}

	/**
	 * set tag True
	 * 
	 * @param ctnname
	 *            container name
	 * @param tag
	 * @throws Exception
	 */
	public void acceptTag(String ctnname, String tag) throws Exception {
		ctnname = UrlDecodeUtils.decodeData(ctnname);
		if (!ctnname.equals(currentCtn.getName())) {
			throw new Exception("client is not synchronized with server");
		}
		Map<TagType, Map<String, Boolean>> multiTags = currentCtn.getMultiTypesTags();
		int[] correctTagSum = currentCtn.getCorrectTagSum();
		int[] globalTagCorrectSum = ContainerInfo.getGlobalTagCorrectSum();
		for (TagType key : multiTags.keySet()) {
			Map<String, Boolean> tag2Valid = multiTags.get(key);
			if (tag2Valid.containsKey(tag)) {
				correctTagSum[key.getValue()]++;
				globalTagCorrectSum[key.getValue()]++;
				tag2Valid.put(tag, true);
			}
		}
	}
}
