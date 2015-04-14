package nagaseiori.tmpbussiness.proto;

import java.math.BigInteger;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.Arrays;

/**
 * 
 * @author Aiven
 * @email aiven163@sina.com
 * @date 2014-11-11 上午11:18:27
 * @Copyright Copyright (c) 2014 Tuandai Inc. All Rights Reserved.
 * @desc
 */
public class DataFormatUtil {

	/**
	 * short to bytes
	 * 
	 * @param value
	 * @return
	 */
	public static byte[] shortToBytes(short value) {
		ByteBuffer bbuf = ByteBuffer.allocate(2);
		bbuf.order(ByteOrder.LITTLE_ENDIAN);
		bbuf.putShort(value);
		return bbuf.array();
	}

	/**
	 * int to bytes
	 * 
	 * @param value
	 * @return
	 */
	public static byte[] intToBytes(int value) {
		ByteBuffer bbuf = ByteBuffer.allocate(4);
		bbuf.order(ByteOrder.LITTLE_ENDIAN);
		bbuf.putInt(value);
		return bbuf.array();
	}

	/**
	 * long to bytes
	 * 
	 * @param value
	 * @return
	 */
	public static byte[] longToBytes(long value) {
		ByteBuffer bbuf = ByteBuffer.allocate(8);
		bbuf.order(ByteOrder.LITTLE_ENDIAN);
		bbuf.putLong(value);
		return bbuf.array();
	}

	/**
	 * bytes To Int
	 * 
	 * @param bts
	 * @return
	 */
	public static int bytesToInt(byte[] bts) {
		if (bts == null || bts.length != 4)
			return 0;
		int value = ByteBuffer.wrap(bts).order(ByteOrder.LITTLE_ENDIAN).getInt();
		return value;
	}

	/**
	 * bytes To Short
	 * 
	 * @param bts
	 * @return
	 */
	public static short bytesToShort(byte[] bts) {
		if (bts == null || bts.length != 2)
			return 0;
		byte[] tmps = new byte[2];
		tmps[1] = bts[0];
		tmps[0] = bts[1];
		BigInteger bi = new BigInteger(tmps);
		return bi.shortValue();
	}

	/**
	 * bytes To Long
	 * 
	 * @param bts
	 * @return
	 */
	public static long bytesToLong(byte[] bts) {
		if (bts == null || bts.length != 8)
			return 0;
		byte[] tmps = new byte[8];
		for (int i = 0; i < 8; i++) {
			tmps[7 - i] = bts[i];
		}
		BigInteger bi = new BigInteger(tmps);
		return bi.longValue();
	}

	/**
	 * int to bytes
	 * 
	 * @param value
	 * @return
	 */
	public static byte[] intToBytes2(int value) {
		ByteBuffer bbuf = ByteBuffer.allocate(4);
		bbuf.order(ByteOrder.BIG_ENDIAN);
		bbuf.putInt(value);
		return bbuf.array();
	}

	public static void main(String[] args) {
		byte[] intToBytes2 = intToBytes(111111);
		int bytesToInt = bytesToInt(intToBytes2);
		System.err.println(bytesToInt);
	}
}
