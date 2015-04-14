package nagaseiori.tmpbussiness.proto;

/**
 * 消息协议类型
 * 
 */
public class ProtoMessageType {
	private static final int Hexadecimal = 16;
	private static final int Hexadecimal_2 = Hexadecimal * Hexadecimal;
	private static final byte notification_pkg_type = 7;

	public enum TypeFrom {
		// 好友关系处理 已取消 此接口
		USER_FRIENDSHIP(notification_pkg_type, (byte) 11, (byte) 40),
		// 创建群组 请求小类是14 回复小类15
		GROUP_CREATE((byte) 5, (byte) 9, (byte) 14),
		// 预登录 发送小类是1 接收小类是2
		SYS_PRELOGIN((byte) 3, (byte) 6, (byte) 1),
		// 钱小二预登陆
		SYS_CUSTOMER_PRELOGIN((byte) 3, (byte) 6, (byte) 3),
		// 关注消息
		NOTICE_ACTION((byte) 0x0C, (byte) 15, (byte) 1),
		// 说说发布状态回复
		NOTICE_SNS_PUBLISH((byte) 0x0C, (byte) 15, (byte) 2),
		// 新说说推送
		NOTICE_SNS_MESSAGE((byte) 0x0C, (byte) 15, (byte) 3),
		// 评论点赞推送
		NOTICE_SNS_OTHER((byte) 0x0C, (byte) 15, (byte) 4),
		//成为业务好友
		BECOME_FRIEDNS((byte) 0x0C, (byte) 15, (byte) 5),
		// 去得红包消息
		GET_REDPACKET_ACTION((byte) 0x0C, (byte) 15, (byte) 6);
		
		public final byte type;
		public final byte bigType;
		public final byte smallType;
		public final int type_flag;

		TypeFrom(byte mType, byte mBigType, byte mSmallType) {
			this.type = mType;
			this.bigType = mBigType;
			this.smallType = mSmallType;
			this.type_flag = type * Hexadecimal_2 + bigType * Hexadecimal;
		}

		public byte getType() {
			return type;
		}

		public byte getBigType() {
			return bigType;
		}

		public byte getSmallType() {
			return smallType;
		}

		public final int getType_flag() {
			return type_flag;
		}
	}
}