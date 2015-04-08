package nagaseiori.tmpbussiness.run;

import java.io.IOException;
import java.util.List;

import nagaseiori.commons.SpringContextUtil;
import nagaseiori.tmpbussiness.dao.ChatGroupMsg;
import nagaseiori.tmpbussiness.dao.DetailStorageDao;


public class TestRun {

	public static void main(String[] args) throws IOException {
		
		DetailStorageDao detailStorageDao = SpringContextUtil.getBean("detailStorageDao", DetailStorageDao.class);
		String tableName = DetailStorageDao.chat_group_msg + "30";
		List<ChatGroupMsg> list = detailStorageDao.selectChatGroupMsgList(tableName);
		for(ChatGroupMsg msg : list){
			System.out.println(msg);
		}
	}
}