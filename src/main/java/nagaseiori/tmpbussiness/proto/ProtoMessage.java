package nagaseiori.tmpbussiness.proto;


/**
 * 协议解释
 * <code>
typedef struct _packHead
{
0	unsigned char pk_begin_flag;
1	unsigned int  pk_len;
5	unsigned char server_id;
6	unsigned char big_type;
7	unsigned char small_type;
8	unsigned char version;
9	unsigned char encrypt_mode;
10	unsigned int  session_id;
14	unsigned int  client_id;
18	unsigned int  pkIndex;
22	unsigned char expand[8];
30	unsigned char body[1];
}PACKED packHead;

 * </code>
 * @Copyright Copyright (c) 2014 Tuandai Inc. All Rights Reserved.
 * @desc
 */
public final class ProtoMessage {
	private ProtoHeader headerVol = null;
	private byte[] bodyData = null;
	private final static int SIZE = 30;
	
	public ProtoMessage() {
	}

	/**
	 * 设置包头信息
	 * 
	 * @param headerVol
	 */
	public void setHeaderVol(ProtoHeader headerVol) {
		this.headerVol = headerVol;
	}

	public ProtoHeader getHeaderVol() {
		return headerVol;
	}

	/**
	 * 设置包体数据
	 * 
	 * @param bodyMsg
	 */
	public void setBodyData(byte[] bodyData) {
		this.bodyData = bodyData;
	}

	public byte[] getBodyData() {
		return bodyData;
	}

	/**
	 * 将包转换成byte数组
	 * 
	 * @return
	 */
	public byte[] toByteArray() throws RuntimeException {
		if (null == headerVol) {
			throw new RuntimeException("协议头部不能为空 headerVol.");
		}
		if (null == bodyData) {
			bodyData = new byte[0];
		}

		headerVol.setPkgLength(bodyData.length + ProtoHeader.PROTOCOL_FIXED_LENGTH);
		byte[] pkgBt = new byte[headerVol.getPkgLength()];
		pkgBt[0] = headerVol.getBeginFlag();
		// 报文长度
		byte[] pkgLengthBt = DataFormatUtil.intToBytes(headerVol.getPkgLength());
		pkgBt[1] = pkgLengthBt[0];
		pkgBt[2] = pkgLengthBt[1];
		pkgBt[3] = pkgLengthBt[2];
		pkgBt[4] = pkgLengthBt[3];

		pkgBt[5] = headerVol.getPkgType();
		pkgBt[6] = headerVol.getBigType();
		pkgBt[7] = headerVol.getSmallType();
		pkgBt[8] = headerVol.getVersion();
		pkgBt[9] = headerVol.getEncryptType();

		byte[] sessionBt = DataFormatUtil.intToBytes(headerVol.getSessionId());
		pkgBt[10] = sessionBt[0];
		pkgBt[11] = sessionBt[1];
		pkgBt[12] = sessionBt[2];
		pkgBt[13] = sessionBt[3];

		byte[] clientIdBt = DataFormatUtil.intToBytes(headerVol.getClientId());
		pkgBt[14] = clientIdBt[0];
		pkgBt[15] = clientIdBt[1];
		pkgBt[16] = clientIdBt[2];
		pkgBt[17] = clientIdBt[3];

		byte[] pipIndexBt = DataFormatUtil.intToBytes(headerVol.getPkgPipeIndex());
		pkgBt[18] = pipIndexBt[0];
		pkgBt[19] = pipIndexBt[1];
		pkgBt[20] = pipIndexBt[2];
		pkgBt[21] = pipIndexBt[3];

		// 扩展前四位
		byte[] reciveBt = DataFormatUtil.intToBytes(headerVol.getReciverId());
		pkgBt[22] = reciveBt[0];
		pkgBt[23] = reciveBt[1];
		pkgBt[24] = reciveBt[2];
		pkgBt[25] = reciveBt[3];

		// 扩展后四位
		byte[] groupBt = DataFormatUtil.intToBytes(headerVol.getGroupId());
		pkgBt[26] = groupBt[0];
		pkgBt[27] = groupBt[1];
		pkgBt[28] = groupBt[2];
		pkgBt[29] = groupBt[3];

		byte[] pkgSrcBt = DataFormatUtil.shortToBytes(headerVol.getPkgCrc());
		pkgBt[30] = pkgSrcBt[0];
		pkgBt[31] = pkgSrcBt[0];
		System.arraycopy(bodyData, 0, pkgBt, SIZE, bodyData.length);
		pkgBt[headerVol.getPkgLength() - 1] = headerVol.getEndFlag();
		return pkgBt;
	}

	/**
	 * 解析返回包体数据
	 */
	public static ProtoMessage analysisPkgBytes(byte[] bts) throws Exception {
		if (bts == null || bts.length < ProtoHeader.PROTOCOL_FIXED_LENGTH)
			throw new Exception("package too short");

		byte[] pkgLengthBt = new byte[4];
		pkgLengthBt[0] = bts[1];
		pkgLengthBt[1] = bts[2];
		pkgLengthBt[2] = bts[3];
		pkgLengthBt[3] = bts[4];
		int pkgLength = DataFormatUtil.bytesToInt(pkgLengthBt);// 包体长度

		ProtoHeader headerVol = new ProtoHeader();
		headerVol.setPkgLength(pkgLength);

		headerVol.setPkgType(bts[5]);
		headerVol.setBigType(bts[6]);
		headerVol.setSmallType(bts[7]);
		headerVol.setVersion(bts[8]);
		headerVol.setEncryptType(bts[9]);

		// Session
		byte[] SessionBt = new byte[4];
		SessionBt[0] = bts[10];
		SessionBt[1] = bts[11];
		SessionBt[2] = bts[12];
		SessionBt[3] = bts[13];
		headerVol.setSessionId(DataFormatUtil.bytesToInt(SessionBt));

		// ClientId
		byte[] clientBt = new byte[4];
		clientBt[0] = bts[14];
		clientBt[1] = bts[15];
		clientBt[2] = bts[16];
		clientBt[3] = bts[17];
		headerVol.setClientId(DataFormatUtil.bytesToInt(clientBt));

		// PipIndex
		byte[] pkgPipIndexBt = new byte[4];
		pkgPipIndexBt[0] = bts[18];
		pkgPipIndexBt[1] = bts[19];
		pkgPipIndexBt[2] = bts[20];
		pkgPipIndexBt[3] = bts[21];
		// System.err.println( Hex.encodeHexString(pkgPipIndexBt));
		headerVol.setPkgPipeIndex(DataFormatUtil.bytesToInt(pkgPipIndexBt));

		byte[] recieveIdBts = new byte[4];
		recieveIdBts[0] = bts[22];
		recieveIdBts[1] = bts[23];
		recieveIdBts[2] = bts[24];
		recieveIdBts[3] = bts[25];
		headerVol.setReciverId(DataFormatUtil.bytesToInt(recieveIdBts));

		// 跳过扩展位后四位
		byte[] groupIdBts = new byte[4];
		groupIdBts[0] = bts[26];
		groupIdBts[1] = bts[27];
		groupIdBts[2] = bts[28];
		groupIdBts[3] = bts[29];
		headerVol.setGroupId(DataFormatUtil.bytesToInt(groupIdBts));

		byte[] pkgSrcBts = new byte[2];
		pkgSrcBts[0] = bts[30];
		pkgSrcBts[1] = bts[31];
		headerVol.setPkgCrc(DataFormatUtil.bytesToShort(pkgSrcBts));

		int dataLength = pkgLength - ProtoHeader.PROTOCOL_FIXED_LENGTH;
		byte[] bodyData = new byte[dataLength];
		System.arraycopy(bts, SIZE, bodyData, 0, dataLength);

		// 构建新的对象传回
		ProtoMessage pme = new ProtoMessage();
		pme.setBodyData(bodyData);
		pme.setHeaderVol(headerVol);
		return pme;
	}
}
