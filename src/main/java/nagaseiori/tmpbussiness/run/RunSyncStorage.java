package nagaseiori.tmpbussiness.run;

import java.sql.Blob;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import nagaseiori.commons.SpringContextUtil;
import nagaseiori.tmpbussiness.dao.SyncMsg;
import nagaseiori.tmpbussiness.dao.SyncStorageDao;

import com.google.protobuf.ByteString;
import com.google.protobuf.InvalidProtocolBufferException;

import common.Common.chat_body_msg;
import common.Common.msg_content;

public class RunSyncStorage {

	public static void updateByTableName(String tableName) throws InvalidProtocolBufferException, SQLException{
		SyncStorageDao syncStorageDao = SpringContextUtil.getBean("syncStorageDao", SyncStorageDao.class);
		List<SyncMsg> list = syncStorageDao.selectList(tableName);
		if(list == null){
			return;
		}
		Map<String,String> urlCache = new HashMap<>();
		for(SyncMsg syncMsg : list){
			Blob pbBody = syncMsg.getPb_body();
			chat_body_msg body_msg = chat_body_msg.parseFrom(pbBody.getBytes(1, (int)pbBody.length()));
			msg_content msg = body_msg.getMsg();
			String avatar_url = msg.getAvatarUrl().toStringUtf8();
			String msgText = msg.getMsg().toStringUtf8();
			String new_avatar_url = null;
			String newMsgText = null;
			if(urlCache.containsKey(avatar_url)){
				new_avatar_url = urlCache.get(avatar_url);
			}else{
				new_avatar_url = TfsUploadUtil.uploadFile(avatar_url);
				urlCache.put(avatar_url, new_avatar_url);
			}
			if(urlCache.containsKey(msgText)){
				newMsgText = urlCache.get(msgText);
			}else{
				newMsgText = TfsUploadUtil.uploadFile(msgText);
				urlCache.put(msgText, newMsgText);
			}
			if(StringUtils.equals(avatar_url, new_avatar_url) && StringUtils.equals(msgText, newMsgText)){
				//如果上传前后的路径不变，则不需要更新数据库
				continue;
			}
			//上传完成，重新组包
			common.Common.msg_content.Builder msgContentBuilder = msg_content.newBuilder(msg);
			msgContentBuilder.setAvatarUrl(ByteString.copyFromUtf8(new_avatar_url));
			msgContentBuilder.setMsg(ByteString.copyFromUtf8(newMsgText));
			msg_content newMsgContent = msgContentBuilder.build();
			common.Common.chat_body_msg.Builder chatBodyMsgBuilder = chat_body_msg.newBuilder(body_msg);
			chatBodyMsgBuilder.setMsg(newMsgContent);
			chat_body_msg writeBody = chatBodyMsgBuilder.build();
			syncStorageDao.update(tableName, syncMsg, writeBody.toByteArray());
		}
	}
	
	public static void unPackPbBody(String tableName) throws InvalidProtocolBufferException, SQLException{
		SyncStorageDao syncStorageDao = SpringContextUtil.getBean("syncStorageDao", SyncStorageDao.class);
		List<SyncMsg> list = syncStorageDao.selectList(tableName);
		if(list == null){
			return;
		}
		for(SyncMsg syncMsg : list){
			Blob pbBody = syncMsg.getPb_body();
			byte[] pbArray = pbBody.getBytes(1, (int)pbBody.length());
			chat_body_msg body_msg = chat_body_msg.parseFrom(pbArray);
			msg_content msg = body_msg.getMsg();
			String avatar_url = msg.getAvatarUrl().toStringUtf8();
			String msgText = msg.getMsg().toStringUtf8();
			System.out.println("tableName:"+tableName);
			System.out.println("pbBodylen:"+pbBody.length());
			System.out.println("unpack avatar_url:" + avatar_url);
			System.out.println("unpack msgText:" + msgText);
		}
	}
	
	public static void main(String[] args) throws SQLException, Exception {
		SyncStorageDao syncStorageDao = SpringContextUtil.getBean("syncStorageDao", SyncStorageDao.class);
		List<String> tables = syncStorageDao.getAllTableNames();
		for(String tableName : tables){
			updateByTableName(tableName);
		}
	}
}
