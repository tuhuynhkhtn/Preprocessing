package main;

import java.io.File;
import java.util.ArrayList;
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
	
	private static List<Integer> randList(int size) {
		List<Integer> list=new ArrayList<Integer>();
		Random rd=new Random();
		for (int i = 0; i < size; i++) {
			list.add(Integer.valueOf((rd.nextInt())));
		}
		return list;
	}
	
	private static int indexList(Integer index,List<Integer> list) {
		int report = 0;
		
		for (int i = 0; i < list.size(); i++) {
			if(list.get(index) > list.get(i)) report++;
		}
		
		return report;
	}
	
	private static List<String> getDataSRSWOR(List<String> data,Float percent) {
        List<Integer> list_rand=randList(data.size());
        
        List<String> get=new ArrayList<>();
        
        for (Integer i = 0; i < data.size()*(percent>=1?1:percent); i++) {
			get.add(data.get(indexList(i, list_rand)));
		}
        
		return get;
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	private static List<String> getDataSRSWR(List<String> data,Float percent) {
        List<Integer> list_rand = new ArrayList();
        List<String> get = new ArrayList<>();
        Integer size = (int) (data.size()*(percent>=1?1:percent));
        
        for (int j = 0; j < data.get(0).split(",").length; j++) {
        	list_rand = randList(data.size());
        	
            for (Integer i = 0; i < size; i++) {
            	if(get.size()!=size)
            		get.add(data.get(indexList(i, list_rand)).split(",")[j]);
            	else get.set(i, get.get(i)+","+data.get(indexList(i,list_rand)).split(",")[j]);
    		}
            
            list_rand.clear();
		}
        
		return get;
	}
	
	public static boolean SimpleRandSampleWithoutReplacement(Float percent) {
		try {
			File file = getFileFromInput();
			if(file == null) return false;
			
	        List<String> data = CauD.readData(file);
	        List<String> headers = CauD.readAttribute(file);
	        
	        List<String> result = getDataSRSWOR(data,percent); 
	        
	        //System.out.println(data.size()+"..."+result.size());
	        
	        List<String> combinedResult = CauD.combinedFile(headers, result);
	        BaseApplication.writeFileOutput("heart-srsowr.arff", combinedResult);
			
			return true;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}
	
	/**
	 * chua tim duoc thuat toan nen se lay thuat toan tu Simple Rand Sample Without Replacemen 
	 * @return ket qua chuong trinh, neu co loi chuong trinh tra ve false
	 */
	public static boolean SimpleRandSampleWithReplacement(Float percent) {
		try {
			File file = getFileFromInput();
			if(file == null) return false;
			
	        List<String> data = CauD.readData(file);
	        List<String> headers = CauD.readAttribute(file);
	        
	        //List<String> result = CauD.standardlizedAllAtrributes(data, attributes);
	        List<String> result = getDataSRSWR(data, percent); 
	        List<String> combinedResult = CauD.combinedFile(headers, result);
	        BaseApplication.writeFileOutput("heart-srswr.arff", combinedResult);
			
			return true;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}
}
