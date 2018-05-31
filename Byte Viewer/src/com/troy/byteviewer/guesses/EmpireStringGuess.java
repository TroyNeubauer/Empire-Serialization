package com.troy.byteviewer.guesses;

import java.util.List;

import com.troy.byteviewer.guess.AbstractGuess;
import com.troy.byteviewer.guess.AnnotatedSection;
import com.troy.empireserialization.EmpireConstants;
import com.troy.empireserialization.EmpireOpCodes;
import com.troy.empireserialization.charset.EmpireCharset;
import com.troy.empireserialization.charset.EmpireCharsets;
import com.troy.empireserialization.io.in.Input;
import com.troy.empireserialization.util.MiscUtil;

public class EmpireStringGuess extends AbstractGuess {

	@Override
	public long readImpl(Input input, long minBytes, List<AnnotatedSection> guesses) {
		long count = 0;
		while (count < minBytes) {
			try {
				byte b = input.readByte();
				count++;
				if (b == EmpireOpCodes.HELLO_WORLD_STRING_CONST) {
					guesses.add(new AnnotatedSection(count + getOffset(), 1, "String: \"Hello, World!\"",
							"The opcode " + EmpireOpCodes.HELLO_WORLD_STRING_CONST + " can indicate the constant Hello, World!", nextColor()));
				} else if (b == EmpireOpCodes.EMPTY_STRING_CONST) {
					guesses.add(new AnnotatedSection(count + getOffset(), 1, "Empty String: \"\"",
							"The opcode " + EmpireOpCodes.EMPTY_STRING_CONST + " indicates an empty string", nextColor()));
				} else if ((b & EmpireOpCodes.MAJOR_CODE_MASK) == EmpireOpCodes.STRING_MAJOR_CODE) {
					long start = count - 1 + getOffset();
					int charset = b >> 4 & 0b11;
					int length = b & 0b1111;
					if (length == 0) {
						int temp = input.readByte();
						count++;
						length = temp & EmpireConstants.VLE_MASK;
						if ((temp & EmpireConstants.NEXT_BYTE_VLE) != 0) {
							temp = input.readByte();
							count++;
							length |= (temp & EmpireConstants.VLE_MASK) << 7;
							if ((temp & EmpireConstants.NEXT_BYTE_VLE) != 0) {
								temp = input.readByte();
								count++;
								length |= (temp & EmpireConstants.VLE_MASK) << 14;
								if ((temp & EmpireConstants.NEXT_BYTE_VLE) != 0) {
									temp = input.readByte();
									count++;
									length |= (temp & EmpireConstants.VLE_MASK) << 21;
									if ((temp & EmpireConstants.NEXT_BYTE_VLE) != 0) {
										temp = input.readByte();
										count++;
										length |= (temp & EmpireConstants.VLE_MASK) << 28;
									}
								}
							}
						}
					}
					EmpireCharset set = EmpireCharsets.get(charset);
					if (length * set.getBytesPerCharacter() > input.remaining())
						continue;
					System.out.println(input.remaining());
					char[] chars = new char[length];
					long decoded = set.decode(input, chars, 0, length);
					count += decoded;
					String name = "String: \"" + MiscUtil.createString(chars) + "\"";
					String description = "Charset: " + set.getClass().getSimpleName() + "\nCharacters: " + length + "\nEncoded in " + decoded
							+ " bytes";
					guesses.add(new AnnotatedSection(start, (count + getOffset()) - start, name, description, nextColor()));
				}
			} catch (Throwable e) {
				// Ignore.. If an exception is thrown we don't care because it wasn't a string
				// anyway
				e.printStackTrace();
				throw new RuntimeException(e);
			}
		}
		return count;
	}

}
