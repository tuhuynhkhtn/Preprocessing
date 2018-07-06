package main;

import java.io.File;
import java.util.List;
import java.util.Random;

public class CauE {
	private static File getFileFromInput() {
        File file = new File("heart-integration-auto.arff");
        if (file.exists()) {
            return file;
        }
        return null;
    }
	
	@SuppressWarnings("null")
	private static List<Integer> randList(int size) {
		List<Integer> list = null;
		Random rd=new Random();
		for (int i = 0; i < size; i++) {
			list.add(rd.nextInt());
		}
		return list;
	}
	
	private static int indexList(Integer index,List<Integer> list) {
		int report = 1;
		
		for (int i = 0; i < list.size(); i++) {
			if(list.get(index) > list.get(i)) report++;
		}
		
		return report;
	}
	
	@SuppressWarnings("null")
	private static List<String> getDataSRSOWR(List<String> data) {
        List<Integer> list_rand=randList(data.size());
        
        List<String> get=null;
        
        for (Integer i = 0; i < list_rand.size(); i++) {
			get.add(data.get(indexList(i, list_rand)));
		}
        
		return get;
	}
	
	@SuppressWarnings("unused")
	private static boolean SimpleRandSampleWithoutReplacement() {
		try {
			File file = getFileFromInput();
			if(file == null) return false;
			
	        List<String> data = CauD.readData(file);
	        List<String> headers = CauD.readAttribute(file);
	        
	        List<String> result = getDataSRSOWR(data); 
	        
	        List<String> combinedResult = CauD.combinedFile(headers, result);
	        BaseApplication.writeFileOutput("heart-srsowr.arff", combinedResult);
			
			return true;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}
	
	public static boolean SimpleRandSampleWithReplacement() {
		try {
			File file = getFileFromInput();
			if(file == null) return false;
			
	        List<String> data = CauD.readData(file);
	        List<String> headers = CauD.readAttribute(file);
	        
	        //List<String> result = CauD.standardlizedAllAtrributes(data, attributes);
	        List<String> result = getDataSRSOWR(data); 
	        List<String> combinedResult = CauD.combinedFile(headers, result);
	        BaseApplication.writeFileOutput("heart-srsowr.arff", combinedResult);
			
			return true;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}
}
