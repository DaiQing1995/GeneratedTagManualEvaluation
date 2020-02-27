package com.dq.entity;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Tags in Container
 * 
 * @author DaiQing
 */
public class ContainerInfo {
	// container name and short description
	private String name;
	private String shortDesc;
	// tag_type -> {tag -> valid}
	private Map<TagType, Map<String, Boolean>> multiTypesTags;

	// tag_type -> correct tags sum
	private Map<TagType, Integer> correctTagSum;
	// tag_type -> tags sum
	private Map<TagType, Integer> tagSum;

	// tag_type -> correct tags sum
	private static Map<TagType, Integer> globalTagCorrectSum = new HashMap<TagType, Integer>();
	// tag_type -> tags sum
	private static Map<TagType, Integer> globalTagSum = new HashMap<TagType, Integer>();

	public ContainerInfo(String name, String short_desc) {
		this.name = name;
		this.shortDesc = short_desc;
		multiTypesTags = new HashMap<TagType, Map<String, Boolean>>();
		for (TagType type : TagType.values()) {
			multiTypesTags.put(type, new HashMap<String, Boolean>());
		}
		tagSum = new HashMap<TagType, Integer>();
		correctTagSum = new HashMap<TagType, Integer>();
	}

	public void setTags(String tags, TagType type) throws Exception {
		if (tags == null) {
			return;
		}
		String[] tagArr = tags.split(",");
		Set<String> tagSet = new HashSet<String>();
		// remove duplicate tag
		for (String tag : tagArr) {
			tagSet.add(tag);
		}
		for (String tag : tagSet) {
			multiTypesTags.get(type).put(tag, false);
		}
		// Update tags sum
		if (tagSum.containsKey(type)) {
			tagSum.put(type, tagSum.get(type) + tagSet.size());
		} else {
			tagSum.put(type, tagSet.size());
		}
	}

	public String getName() {
		return name;
	}

	public String getShortDesc() {
		return shortDesc;
	}

}
