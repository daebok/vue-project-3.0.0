package com.rd.ifaes.common.util.code;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import com.rd.util.base64.CharacterDecoder;

/**
 * <p>
 * Description:base64解码类
 * </p>
 */
public class BASE64Decoder extends CharacterDecoder {
	
	protected byte decodeBuffer[];
	
	public BASE64Decoder() {
		decodeBuffer = new byte[4];
	}

	@Override
	protected int bytesPerAtom() {
		return 4;
	}

	@Override
	protected int bytesPerLine() {
		return 72;
	}

	@Override
	protected void decodeAtom(InputStream inputstream, OutputStream outputstream, int i) throws IOException {
		int index = i;
		byte byte0 = -1;
		byte byte1 = -1;
		byte byte2 = -1;
		byte byte3 = -1;
		if (index < 2)
			throw new IOException("BASE64Decoder: Not enough bytes for an atom.");
		int j;
		do {
			j = inputstream.read();
			if (j == -1)
				throw new IOException("StreamExhausted");
		} while (j == 10 || j == 13);
		decodeBuffer[0] = (byte) j;
		j = readFully(inputstream, decodeBuffer, 1, index - 1);
		if (j == -1)
			throw new IOException("StreamExhausted");
		if (index > 3 && decodeBuffer[3] == 61)
			index = 3;
		if (index > 2 && decodeBuffer[2] == 61)
			index = 2;
		switch (index) {
			case 4: // '\004'
				byte3 = PEM_CONVERT_ARRAY[decodeBuffer[3] & 0xff];
				// fall through

			case 3: // '\003'
				byte2 = PEM_CONVERT_ARRAY[decodeBuffer[2] & 0xff];
				// fall through

			case 2: // '\002'
				byte1 = PEM_CONVERT_ARRAY[decodeBuffer[1] & 0xff];
				byte0 = PEM_CONVERT_ARRAY[decodeBuffer[0] & 0xff];
				// fall through

			default:
				switch (index) {
					case 2: // '\002'
						outputstream.write((byte) (byte0 << 2 & 0xfc | byte1 >>> 4 & 3));
						break;

					case 3: // '\003'
						outputstream.write((byte) (byte0 << 2 & 0xfc | byte1 >>> 4 & 3));
						outputstream.write((byte) (byte1 << 4 & 0xf0 | byte2 >>> 2 & 0xf));
						break;

					case 4: // '\004'
						outputstream.write((byte) (byte0 << 2 & 0xfc | byte1 >>> 4 & 3));
						outputstream.write((byte) (byte1 << 4 & 0xf0 | byte2 >>> 2 & 0xf));
						outputstream.write((byte) (byte2 << 6 & 0xc0 | byte3 & 0x3f));
						break;
				}
				break;
		}
	}

	private static final char PEM_ARRAY[] = { 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N',
			'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z', 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i',
			'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z', '0', '1', '2', '3',
			'4', '5', '6', '7', '8', '9', '+', '/' };
	private static final byte PEM_CONVERT_ARRAY[];
	
	

	static {
		PEM_CONVERT_ARRAY = new byte[256];
		for (int i = 0; i < 255; i++)
			PEM_CONVERT_ARRAY[i] = -1;

		for (int j = 0; j < PEM_ARRAY.length; j++)
			PEM_CONVERT_ARRAY[PEM_ARRAY[j]] = (byte) j;

	}
}
