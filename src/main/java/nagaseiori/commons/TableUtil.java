package nagaseiori.commons;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;


public class TableUtil{

	// 过滤特殊字符  
    public static String specialStringAsEmpty(String str){
		String regEx="[`~!@#$%^&*()+=|{}':;',\\[\\].<>/?~！@#￥%……&*（）——+|{}【】‘；：”“’。，、？]";  
		Pattern pattern = Pattern.compile(regEx);  
		Matcher matcher = pattern.matcher(str);
		return  matcher.replaceAll("").trim();
    }
    
    
	/**
	 * 
	 * 方法说明：分1000时，取得表名使用这个东西
	 * @return
	 */
	public static String getMod1000SubTableName(String tableName, Object userId){
		int userIdInt = Integer.valueOf(userId.toString());
		String mod = userIdInt % 1000 + "";
		return tableName + "_" + StringUtils.leftPad(mod, 3, "0");
	}
	
	/**
	 * 
	 * 方法说明：分1000时，取得表名使用这个东西
	 * @return
	 */
	public static String getMod100SubTableName(String tableName, Object userId){
		String userIdStr = String.valueOf(userId);
		String mod = userIdStr.substring(userIdStr.length()-2);
		return tableName + "_" + StringUtils.leftPad(mod, 2, "0");
	}
	
    
    public static void main(String[] args) {
    	String str = "fdsa^&fsd021^*();'.,.'1239fs)($@#^~'";
		System.out.println(specialStringAsEmpty(str));
		
		List<Integer> list = new ArrayList<Integer>();
		list.add(1);
		list.add(2);
		String[] userIdArr = {"1","2","3"};
		Integer[] userIdIntArr = new Integer[userIdArr.length];
		for(int i=0; i<userIdArr.length; i++){
			userIdIntArr[i] = NumberUtils.toInt(userIdArr[i]);
		}
		CollectionUtils.addAll(list, userIdIntArr);
		System.out.println(list.toString());
		Set<Integer> set = new HashSet<Integer>();
		set.addAll(list);
		System.out.println(set.toString());
	}
}
