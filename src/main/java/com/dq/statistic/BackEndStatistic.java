package com.dq.statistic;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import com.dq.constant.FileName;
import com.dq.entity.TagType;

/**
 * Used for back-end statistic counting
 * 
 * @author DaiQing
 *
 */
public class BackEndStatistic {

	// Only measure top 50 container infos
	private static final int COUNT_OF_RECORD = 50;

	/**
	 * this method return the info of tf-idf and hierarchical
	 */
	public static void CalculateTagAccurate() {
		File statisticFile = new File(FileName.TAG_STATISTIC);
		BufferedReader bReader;
		int[] tagCorrectSum = new int[TagType.values().length];
		int[] tagTotalSum = new int[TagType.values().length];
		try {
			bReader = new BufferedReader(new FileReader(statisticFile));
			for (int i = 0; i < COUNT_OF_RECORD; ++i) {
				String nameString = bReader.readLine();
				for (int j = 0; j < TagType.values().length; ++ j){
					String[] tagData = bReader.readLine().split(" ");
					tagCorrectSum[j] += Integer.parseInt(tagData[1]);
					tagTotalSum[j] += Integer.parseInt(tagData[2]);
				}
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		for (TagType type : TagType.values()){
			System.out.print(type + ": ");
			System.out.print("correct count: " + tagCorrectSum[type.getValue()] + ", total count: " + tagTotalSum[type.getValue()]);
			System.out.println(", correctness: " + (1.0 * tagCorrectSum[type.getValue()]) / (1.0 * tagTotalSum[type.getValue()]));
		}
	}
	
	
	public static void main(String[] args) {
		BackEndStatistic.CalculateTagAccurate();
	}
}
