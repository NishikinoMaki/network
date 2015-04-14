package nagaseiori.tmpbussiness.proto;

/**
 * 报文头部信息
 * 
 * @Copyright Copyright (c) 2014 Tuandai Inc. All Rights Reserved.
 * @desc
 */
public class ProtoHeader {
	public static final int PROTOCOL_FIXED_LENGTH = 33;
	public static final byte FLAG_PKG_BEGIN = (byte) 0xff;
	public static final byte FLAG_PKG_END = 0x00;

	private byte beginFlag = FLAG_PKG_BEGIN;
	private int pkgLength; // 报文长度 必填 包头长度 33字节+包体不定长
	private byte pkgType = 7; // 报文类型 必填
	private byte bigType; // 大类 必填
	private byte smallType; // 小类 必填
	private byte version = (byte) 0; // 版本

	private byte encryptType = (byte) 0; // 加密类型
	private int sessionId; // 代理sessionId
	private int clientId; // 客户端发送者ID 必填 fromId
	private int pkgPipeIndex; // 包序号
	private int reciverId; // 接受者ID，扩展字段前四位 m_expand 必填
	private int groupId; // 群主ID,扩展字段后四位 m_expand or 必填
	// private byte[] extendsBehinds;// 扩展字段后四位
	private short pkgCrc; // 包体Src
	private byte endFlag = FLAG_PKG_END;

	public byte getBeginFlag() {
		return beginFlag;
	}

	public void setBeginFlag(byte beginFlag) {
		this.beginFlag = beginFlag;
	}

	public int getPkgLength() {
		return pkgLength;
	}

	public void setPkgLength(int pkgLength) {
		this.pkgLength = pkgLength;
	}

	public byte getPkgType() {
		return pkgType;
	}

	public void setPkgType(byte pkgType) {
		this.pkgType = pkgType;
	}

	public byte getBigType() {
		return bigType;
	}

	public void setBigType(byte bigType) {
		this.bigType = bigType;
	}

	public byte getSmallType() {
		return smallType;
	}

	public void setSmallType(byte smallType) {
		this.smallType = smallType;
	}

	public byte getVersion() {
		return version;
	}

	public void setVersion(byte version) {
		this.version = version;
	}

	public byte getEncryptType() {
		return encryptType;
	}

	public void setEncryptType(byte encryptType) {
		this.encryptType = encryptType;
	}

	public int getClientId() {
		return clientId;
	}

	public void setClientId(int clientId) {
		this.clientId = clientId;
	}

	public int getPkgPipeIndex() {
		return pkgPipeIndex;
	}

	public void setPkgPipeIndex(int pkgPipeIndex) {
		this.pkgPipeIndex = pkgPipeIndex;
	}

	public int getReciverId() {
		return reciverId;
	}

	public void setReciverId(int reciverId) {
		this.reciverId = reciverId;
	}

	// public byte[] getExtendsBehinds() {
	// return extendsBehinds;
	// }
	//
	// public void setExtendsBehinds(byte[] extendsBehinds) {
	// this.extendsBehinds = extendsBehinds;
	// }

	public byte getEndFlag() {
		return endFlag;
	}

	public void setEndFlag(byte endFlag) {
		this.endFlag = endFlag;
	}

	public int getSessionId() {
		return sessionId;
	}

	public void setSessionId(int sessionId) {
		this.sessionId = sessionId;
	}

	public int getGroupId() {
		return groupId;
	}

	public void setGroupId(int groupId) {
		this.groupId = groupId;
	}

	public short getPkgCrc() {
		return pkgCrc;
	}

	public void setPkgCrc(short pkgCrc) {
		this.pkgCrc = pkgCrc;
	}

}
