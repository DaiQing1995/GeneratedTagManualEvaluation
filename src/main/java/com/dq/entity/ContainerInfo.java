package com.dq.entity;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import com.dq.constant.FileName;

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
	// used for current container(this)
	private int[] correctTagSum;
	// tag_type -> tags sum
	private int[] tagSum;

	// tag_type -> correct tags sum
	private static int[] globalTagCorrectSum = new int[TagType.values().length];
	// tag_type -> tags sum
	private static int[] globalTagSum = new int[TagType.values().length];

	/**
	 * init all fields
	 * 
	 * @param name
	 *            container name
	 * @param short_desc
	 *            container's short description
	 */
	public ContainerInfo(String name, String short_desc) {
		this.name = name;
		this.shortDesc = short_desc;
		correctTagSum = new int[TagType.values().length];
		tagSum = new int[TagType.values().length];
		multiTypesTags = new HashMap<TagType, Map<String, Boolean>>();
		for (TagType type : TagType.values()) {
			multiTypesTags.put(type, new HashMap<String, Boolean>());
		}
	}

	/**
	 * set tags to type
	 * 
	 * @param tags
	 *            tags from DB
	 * @param type
	 *            container tag type
	 * @throws Exception
	 */
	public void setTags(String tags, TagType type) throws Exception {
		if (tags == null || tags.trim().length() == 0) {
			return;
		}
		String[] tagArr = tags.split(",");
		Set<String> tagSet = new HashSet<String>();
		// remove duplicate tag
		for (String tag : tagArr) {
			if (tag.trim().length() < 2)
				continue;
			tagSet.add(tag.trim());
		}
		for (String tag : tagSet) {
			multiTypesTags.get(type).put(tag, false);
		}
		// Update tags sum
		tagSum[type.getValue()] += tagSet.size();
		// Update Global tag sum
		for (int i = 0; i < tagSum.length; ++i)
			ContainerInfo.globalTagSum[i] += tagSum[i];
	}

	/**
	 * print data info
	 * 
	 * @throws IOException
	 *             file open error
	 */
	public void saveTagInfo() throws IOException {
		File tagRecord = new File(FileName.TAG_RECORD);
		PrintWriter pWriter = new PrintWriter(new FileWriter(tagRecord, true));
		pWriter.println(name);
		/**
		 * record file format:
		 * --container name1
		 * --tagType1
		 * --tag {T/F}
		 * --tag {T/F}
		 * ...
		 * --tagType2 
		 * --tag {T/F}
		 * ...
		 * --container name2
		 */
		for (TagType tagType : TagType.values()) {
			pWriter.println(tagType);
			Map<String, Boolean> tagInfo = multiTypesTags.get(tagType);
			for (Map.Entry<String, Boolean> entry : tagInfo.entrySet()) {
				pWriter.println(entry.getKey() + " " + entry.getValue());
			}
		}
		pWriter.flush();
		pWriter.close();

		File tagStatistic = new File(FileName.TAG_STATISTIC);
		/**
		 * statistic file format:
		 * --container name
		 * --tagType {correct_tag_sum} {tagSum}
		 */
		pWriter = new PrintWriter(new FileWriter(tagStatistic, true));
		pWriter.println(name);
		for (TagType tagType : TagType.values()) {
			pWriter.println(tagType + " " + correctTagSum[tagType.getValue()] + " " + tagSum[tagType.getValue()]);
		}
		pWriter.flush();
		pWriter.close();
		/**
		 * tagfinishedFile record all finished tag info
		 */
		File tagfinishedFile = new File(FileName.TAG_FINISHED_CONTAINER);
		pWriter = new PrintWriter(new FileWriter(tagfinishedFile, true));
		pWriter.println(name);
		pWriter.flush();
		pWriter.close();
	}

	public String getName() {
		return name;
	}

	public String getShortDesc() {
		return shortDesc;
	}

	public int[] getCorrectTagSum() {
		return correctTagSum;
	}

	public static int[] getGlobalTagCorrectSum() {
		return globalTagCorrectSum;
	}

	public Map<TagType, Map<String, Boolean>> getMultiTypesTags() {
		return multiTypesTags;
	}

}
