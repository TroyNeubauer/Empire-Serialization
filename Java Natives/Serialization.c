#include <jni.h>
#include <stdio.h>
#include <stdlib.h>

#pragma once
#define OUT_OF_MEMORY -1
#define UNSUPPORTED_CHARACTER -2
#define ERROR -1
#define INVALID_ARGUMENT -10

const jbyte FOUR_BIT_ENCODING_CACHE[] = { -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, 2, -1, 11, 9, 0, 15, -1, 7, 4, -1, -1, 10, 13, 5, 3, -1, -1, 8, 6, 1, 12, -1, 14 };
const jbyte SIX_BIT_ENCODING_CACHE[] = { -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, 9, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, 0, 2, 6, -1, -1, -1, -1, 5, -1, -1, -1, -1, 4, 10, 1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, 8, 7, -1, -1, -1, 3, -1, 38, 39, 40, 41, 42, 43, 44, 45, 46, 47, 48, 49, 50, 51, 52, 53, 54, 55, 56, 57, 58, 59, 60, 61, 62, 63, -1, -1, -1, -1, 11, -1, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25, 26, 27, 28, 29, 30, 31, 32, 33, 34, 35, 36, 37 };

#define VLE8_INFO_MASK 0b10000000
#define VLE8_DATA_MASK 0b01111111
#define VLE8_HAS_NEXT_BYTE 0b10000000
#define VLE8_DOESNT_HAVE_NEXT_BYTE 0b00000000

void putError(jlong pointer, jbyte code, jchar character, jint index) {
	*((jbyte*)pointer) = code;
	*((jchar*)(pointer + 1)) = character;
	*((jint*)(pointer + 3)) = index;
}

JNIEXPORT jint JNICALL Java_com_troy_empireserialization_util_NativeUtils_fflush
(JNIEnv * env, jclass class, jlong fd) {
	if (fd == 0) return INVALID_ARGUMENT;
	return fflush((FILE*)fd);
}

JNIEXPORT jint JNICALL Java_com_troy_empireserialization_util_NativeUtils_fputc
(JNIEnv * env, jclass class, jbyte c, jlong fd) {
	if (fd == 0) return INVALID_ARGUMENT;
	return fputc(c, (FILE*)fd);
}

JNIEXPORT jint JNICALL Java_com_troy_empireserialization_util_NativeUtils_fclose
(JNIEnv * env, jclass class, jlong fd) {
	if (fd == 0) return INVALID_ARGUMENT;
	return fclose((FILE*)fd);
}

JNIEXPORT jlong JNICALL Java_com_troy_empireserialization_util_NativeUtils_fopen
(JNIEnv * env, jclass class, jstring name, jstring accessJ) {
	if (name == 0 || accessJ == 0) return 0;
	const char* file = (*env)->GetStringUTFChars(env, name, NULL);
	const char* access = (*env)->GetStringUTFChars(env, accessJ, NULL);
	if (file == NULL) return OUT_OF_MEMORY;
	if (access == NULL) return OUT_OF_MEMORY;
	FILE* result = fopen(file, access);
	if (result == NULL) return 0;
	(*env)->ReleaseStringUTFChars(env, name, file);
	(*env)->ReleaseStringUTFChars(env, accessJ, access);
	return (jlong)result;
}



inline jshort swapJshort(jshort value) {
#if 1
	return _byteswap_ushort(value);
#else
	return  ((value & 0x00FF) << 8) |
			((value & 0xFF00) >> 8);
#endif
}

inline jchar swapJchar(jchar value) {
#if 1
	return _byteswap_ushort(value);
#else
	return  ((value & 0x00FF) << 8) |
			((value & 0xFF00) >> 8);
#endif
}

inline jint swapJint(jint value) {
#if 1
	return _byteswap_ulong(value);
#else
	return  ((value & 0x000000FF) << 24) |
			((value & 0x0000FF00) << 8) |
			((value & 0x00FF0000) >> 8) |
			((value & 0xFF000000) >> 24);
#endif
}

inline jfloat swapJfloat(jfloat value) {
#if 1
	return _byteswap_ulong(value);
#else
	return  ((value & 0x000000FF) << 24) |
			((value & 0x0000FF00) << 8) |
			((value & 0x00FF0000) >> 8) |
			((value & 0xFF000000) >> 24);
#endif
}

inline jlong swapJlong(jlong value) {
#if 1
	return _byteswap_uint64(value);
#else
	return  ((value & 0x00000000000000FF) << 56) |
			((value & 0x000000000000FF00) << 40) |
			((value & 0x0000000000FF0000) << 24) |
			((value & 0x00000000FF000000) <<  8) |
			((value & 0x000000FF00000000) >>  8) |
			((value & 0x0000FF0000000000) >> 24) |
			((value & 0x00FF000000000000) >> 40) |
			((value & 0xFF00000000000000) >> 56);
#endif
}

inline jdouble swapJdouble(jdouble value) {
#if 1
	return _byteswap_uint64(value);
#else
	return  ((value & 0x00000000000000FF) << 56) |
			((value & 0x000000000000FF00) << 40) |
			((value & 0x0000000000FF0000) << 24) |
			((value & 0x00000000FF000000) <<  8) |
			((value & 0x000000FF00000000) >>  8) |
			((value & 0x0000FF0000000000) >> 24) |
			((value & 0x00FF000000000000) >> 40) |
			((value & 0xFF00000000000000) >> 56);
#endif
}

JNIEXPORT jint JNICALL Java_com_troy_empireserialization_util_NativeUtils_nativeToFWrite(JNIEnv * env, jclass class, jlong fd, jlong srcJ, jlong bytes) {
	if (srcJ == 0 || fd == 0)
		return INVALID_ARGUMENT;
	
	fwrite((jbyte*)srcJ, 1, (size_t)bytes, (FILE*)fd);
}

#define xsToFWrite(type, swapFunc) \
JNIEXPORT jint JNICALL Java_com_troy_empireserialization_util_NativeUtils_##type##sToFWrite(JNIEnv * env, jclass class, jlong fd, j##type##Array srcJ, jint srcOffset, jint elements, jboolean swapEndianess) {\
	if (fd == 0 || srcJ == NULL) {											\
		return INVALID_ARGUMENT;											\
	}																		\
	j##type* src = (*env)->GetPrimitiveArrayCritical(env, srcJ, NULL);		\
	if (src == NULL) return OUT_OF_MEMORY;									\
	if (swapEndianess && (sizeof(j##type) != 1)) {							\
		j##type dest;														\
		for (int i = 0; i < elements; i++) {								\
			int srcIndex = i + srcOffset;									\
			dest = swapFunc(src[srcIndex]);									\
			fwrite(&dest, sizeof(j##type), 1, (FILE*)fd);					\
		}																	\
	}																		\
	else {																	\
		fwrite(src, (size_t)(elements * sizeof(j##type)), 1, (FILE*)fd);   	\
	}																		\
	(*env)->ReleasePrimitiveArrayCritical(env, srcJ, src, JNI_ABORT);		\
	return 0;																\
}
xsToFWrite(byte, )
xsToFWrite(boolean, )
xsToFWrite(short, swapJshort)
xsToFWrite(int, swapJint)
xsToFWrite(long, swapJlong)
xsToFWrite(float, swapJfloat)
xsToFWrite(double, swapJdouble)
xsToFWrite(char, swapJchar)

#define xToFWrite(type, swapFunc) \
JNIEXPORT j##type JNICALL Java_com_troy_empireserialization_util_NativeUtils_##type##ToFWrite(JNIEnv * env, jclass class, jlong fd, j##type value, jboolean swapEndianess) {\
	if (fd == 0) {													\
		return INVALID_ARGUMENT;									\
	}																\
	if(sizeof(j##type) > 1 && swapEndianess) {						\
		value = swapFunc(value);									\
	}																\
	fwrite(&value, (size_t)(sizeof(j##type)), 1, (FILE*)fd);		\
	return 0;														\
}

xToFWrite(byte, )
xToFWrite(short, swapJshort)
xToFWrite(int, swapJint)
xToFWrite(long, swapJlong)
xToFWrite(float, swapJfloat)
xToFWrite(double, swapJdouble)
xToFWrite(char, swapJchar)
xToFWrite(boolean, )

#define xToNative(type, swapFunc)														\
JNIEXPORT void JNICALL Java_com_troy_empireserialization_util_NativeUtils_##type##ToNative	\
(JNIEnv * env, jclass class, jlong addressJ, j##type value, jboolean swapEndianness) {	\
	if (addressJ == NULL) return;														\
	j##type* address = (j##type*) addressJ;												\
	if (sizeof(j##type) > 1 && swapEndianness) {										\
		value = swapFunc(value);														\
	}																					\
	*address = value;																	\
}

xToNative(byte, )
xToNative(boolean, )
xToNative(short, swapJshort)
xToNative(int, swapJint)
xToNative(long, swapJlong)
xToNative(float, swapJfloat)
xToNative(double, swapJdouble)
xToNative(char, swapJchar)

//For VLE

JNIEXPORT jint JNICALL Java_com_troy_empireserialization_util_NativeUtils_shortToVLENative
(JNIEnv * env, jclass class, jlong addressJ, jshort value) {
	if (addressJ == NULL) return;
}

JNIEXPORT jint JNICALL Java_com_troy_empireserialization_util_NativeUtils_intToVLENative
(JNIEnv * env, jclass class, jlong addressJ, jint value) {
	if (addressJ == NULL) return;
}

JNIEXPORT jint JNICALL Java_com_troy_empireserialization_util_NativeUtils_longToVLENative
(JNIEnv * env, jclass class, jlong addressJ, jlong value) {
	if (addressJ == NULL) return;
}

JNIEXPORT jbyteArray JNICALL Java_com_troy_empireserialization_io_NativeOutput_ngetBuffer
(JNIEnv * env, jclass class, jlong addressJ, jint capacity)
{
	if (addressJ == 0) return NULL;
	const jbyte* address = (jbyte*)addressJ;
	jbyteArray result = (*env)->NewByteArray(env, capacity);
	if (result == NULL) return NULL;
	(*env)->SetByteArrayRegion(env, result, 0, capacity, address);
	return result;
}

JNIEXPORT jint JNICALL Java_com_troy_empireserialization_charset_FourBitCharset_nEncodeImpl
(JNIEnv * env, jobject charset, jcharArray srcJ, jlong destJ, jint srcOffset, jint chars, jint info)
{
	jchar* src = (*env)->GetPrimitiveArrayCritical(env, srcJ, NULL);
	jbyte* dest = (jbyte*)destJ;
	if (src == NULL) return OUT_OF_MEMORY;
	if (dest == NULL) return INVALID_ARGUMENT;
	int bytesWritten = 0;
	int i = 0;
	const int end = (chars / 2) * 2;//round down to next mutiple of two
	while (i < end) {
		dest[bytesWritten++] = (FOUR_BIT_ENCODING_CACHE[src[srcOffset + i]] << 4) | FOUR_BIT_ENCODING_CACHE[src[srcOffset + i + 1]];
		i += 2;
	}
	if (chars % 2 != 0) {
		dest[bytesWritten++] = FOUR_BIT_ENCODING_CACHE[src[end]] << 4;
	}
	(*env)->ReleasePrimitiveArrayCritical(env, srcJ, src, 0);
	return bytesWritten;
}

JNIEXPORT jint JNICALL Java_com_troy_empireserialization_charset_SixBitCharset_nEncodeImpl
(JNIEnv * env, jobject charset, jcharArray srcJ, jlong destJ, jint srcOffset, jint chars, jint info)
{
	jchar* src = (*env)->GetPrimitiveArrayCritical(env, srcJ, NULL);
	jbyte* dest = (jbyte*)destJ;
	const jbyte* inital = dest;
	if (src == NULL) return OUT_OF_MEMORY;
	if (dest == NULL) return INVALID_ARGUMENT;
	int i = srcOffset;
	int result;
	const jint end = (chars / 4) * 4;// round down to next multiple of two
	while (i < end) {
		int c0 = SIX_BIT_ENCODING_CACHE[src[i++]];
		int c1 = SIX_BIT_ENCODING_CACHE[src[i++]];
		int c2 = SIX_BIT_ENCODING_CACHE[src[i++]];
		int c3 = SIX_BIT_ENCODING_CACHE[src[i++]];
		result = c3;
		result |= c2 << 6;
		result |= c1 << 12;
		result |= c0 << 18;

		*dest = (result >> 16) & 0xFF;
		dest++;
		*dest = (result >>  8) & 0xFF;
		dest++;
		*dest = (result >>  0) & 0xFF;
		dest++;
	}
	if (chars % 4 == 3) {// Write the remaining byte if there was an odd number
		int c0 = SIX_BIT_ENCODING_CACHE[src[i++]];
		int c1 = SIX_BIT_ENCODING_CACHE[src[i++]];
		int c2 = SIX_BIT_ENCODING_CACHE[src[i++]];
		result = 0;
		result |= c2 << 0;
		result |= c1 << 6;
		result |= c0 << 12;

		*dest = (result >> 16) & 0xFF;
		dest++;
		*dest  = (result >> 8) & 0xFF;
		dest++;
		*dest = (result >>  0) & 0xFF;
		dest++;
	}
	else if (chars % 4 == 2) {// Write the remaining byte if there was an odd number
		int c0 = SIX_BIT_ENCODING_CACHE[src[i++]];
		int c1 = SIX_BIT_ENCODING_CACHE[src[i++]];
		result = c1 << 0;
		result |= c0 << 6;

		*dest = (result >> 8) & 0xFF;
		dest++;
		*dest = (result >> 0) & 0xFF;
		dest++;

	}
	else if (chars % 4 == 1) {// Write the remaining byte if there was an odd number
		*dest = SIX_BIT_ENCODING_CACHE[src[end]];
		dest++;
	}
	return dest - inital;
}

JNIEXPORT jint JNICALL Java_com_troy_empireserialization_charset_EmpireCharsets_nIdentifyCharset
(JNIEnv * env, jclass class, jcharArray array, jint offset, jint length) {
	jchar* src = (*env)->GetPrimitiveArrayCritical(env, array, NULL);
	unsigned int fourOK = 1;
	unsigned int sixOK = 1;
	unsigned int allASCII = 1;
	for (int i = offset; i < offset + length; i++) {
		jchar c = src[i];
		if (allASCII && (c >> 7) != 0)
			allASCII = 0;
		if (fourOK && (c > sizeof(FOUR_BIT_ENCODING_CACHE) || FOUR_BIT_ENCODING_CACHE[c] == -1)) {
			fourOK = 0;
		}
		if (sixOK && (c > sizeof(SIX_BIT_ENCODING_CACHE) || SIX_BIT_ENCODING_CACHE[c] == -1)) {
			sixOK = 0;
		}
		if (!fourOK && !sixOK && !allASCII) {
			break;
		}
	}
	(*env)->ReleasePrimitiveArrayCritical(env, array, src, JNI_ABORT);
	unsigned int result = (allASCII ? 0 : 1) << 2;
	if (fourOK)
		result |= 0b00;
	else if (sixOK)
		result |= 0b01;
	else
		result |= 0b10;
	return result;
}


JNIEXPORT jint JNICALL Java_com_troy_empireserialization_charset_VLE8Charset_nEncodeImpl
(JNIEnv * env, jobject charset, jcharArray srcJ, jlong destJ, jint srcOffset, jint chars, jint info)
{
	jchar* src = (*env)->GetPrimitiveArrayCritical(env, srcJ, NULL);
	jbyte* dest = (jbyte*)destJ;
	if (src == NULL) return OUT_OF_MEMORY;
	if (dest == NULL) return INVALID_ARGUMENT;
	int bytesWritten = 0;
	int i = srcOffset;
	const int end = srcOffset + chars;//round down to next mutiple of two
	if (info == 0) {
		while (i < end) {
			dest[bytesWritten++] = src[i++];
		}
	}
	else {
		while (i < end) {
			jchar value = src[i++];
			printf("char %c %i\n", value, value);
			if (value >> 7 == 0) {
				dest[bytesWritten++] = value;
			}
			else if (value >> 14 == 0) {
				dest[bytesWritten++] = (value & VLE8_DATA_MASK) | VLE8_HAS_NEXT_BYTE;
				dest[bytesWritten++] = value >> 7;
			}
			else if (value >> 21 == 0) {
				dest[bytesWritten++] = (value & VLE8_DATA_MASK) | VLE8_HAS_NEXT_BYTE;
				dest[bytesWritten++] = ((value >> 7) & VLE8_DATA_MASK) | VLE8_HAS_NEXT_BYTE;
				dest[bytesWritten++] = value >> 14;
			}
		}
	}
	(*env)->ReleasePrimitiveArrayCritical(env, srcJ, src, 0);
	return bytesWritten;
}


//public static native void shortsToBytes(byte[] dest, short[] src, int srcOffset, int destOffset, int elements);

#define xToBytes(type, swapFunc) \
JNIEXPORT j##type JNICALL Java_com_troy_empireserialization_util_NativeUtils_##type##sToBytes(JNIEnv * env, jclass class, jbyteArray destJ, j##type##Array srcJ, jint srcOffset, jint destOffset, jint elements, jboolean swapEndianess) {\
	if (destJ == NULL || destJ == NULL) {									\
		return INVALID_ARGUMENT;											\
	}																		\
	j##type* src = (*env)->GetPrimitiveArrayCritical(env, srcJ, NULL);		\
	jbyte* dest = (*env)->GetPrimitiveArrayCritical(env, destJ, NULL);		\
	if (src == NULL || dest == NULL) return OUT_OF_MEMORY;					\
	if (swapEndianess && (sizeof(j##type) != 1)) {							\
		j##type* destS = (j##type*)dest;									\
		for (int i = 0; i < elements; i++) {								\
			int srcIndex = i + srcOffset;									\
			(*destS) = swapFunc(src[srcIndex]);								\
			destS++;														\
		}																	\
	}																		\
	else {																	\
		memcpy(dest, src, (size_t)(elements * sizeof(j##type)));			\
	}																		\
	(*env)->ReleasePrimitiveArrayCritical(env, srcJ, src, JNI_ABORT);		\
	(*env)->ReleasePrimitiveArrayCritical(env, destJ, dest, 0);				\
	return 0;																\
}
xToBytes(short, swapJshort)
xToBytes(int, swapJint)
xToBytes(long, swapJlong)
xToBytes(float, swapJint)
xToBytes(double, swapJlong)
xToBytes(char, swapJshort)
xToBytes(boolean, )


JNIEXPORT jint JNICALL Java_com_troy_empireserialization_util_NativeUtils_booleansToBytesCompact(JNIEnv * env, jclass class, jbyteArray destJ, jbooleanArray srcJ, jint srcOffset, jint destOffset, jint elements) {
	if (destJ == NULL || destJ == NULL) {
		return INVALID_ARGUMENT;
	}
	jboolean* src = (*env)->GetPrimitiveArrayCritical(env, srcJ, NULL);
	jbyte* dest = (*env)->GetPrimitiveArrayCritical(env, destJ, NULL);
	if (src == NULL || dest == NULL) return OUT_OF_MEMORY;
	jbyte current = 0;
	jint count = 0;
	for (int i = srcOffset; i < elements + srcOffset; i++) {
		int mod = i % 8;
		current |= (src[i] & 0b1) << (7 - mod);
		if (mod == 0 && i != 0) {
			(*dest) = current;
			dest++;
			current = 0;
			count++;
		}
	}
	if (current != 0) {
		(*dest) = current;
		count++;
	}
	(*env)->ReleasePrimitiveArrayCritical(env, srcJ, src, JNI_ABORT);
	(*env)->ReleasePrimitiveArrayCritical(env, destJ, dest, 0);
	return count;
}


//Methods for manipulating java arrays
#define xToNative(type, capitalType, swapFunc) \
JNIEXPORT jint JNICALL Java_com_troy_empireserialization_util_NativeUtils_##type##sToNative(JNIEnv * env, jclass class, jlong dest, j##type##Array src, jint offset, jint elements, jboolean swapEndianess) {\
	if(src == 0 || dest == 0) return INVALID_ARGUMENT;\
	(*env)->Get##capitalType##ArrayRegion(env, src, offset, elements, (j##type *)dest); \
	if(swapEndianess && sizeof(j##type) > 1) {\
/*FIXME actually swap enginess if neccsary*/\
	}\
	return 0;\
}
//Theese methods copy bytes from an array to native memory

xToNative(byte, Byte, )
xToNative(short, Short, swapJshort)
xToNative(int, Int, swapJint)
xToNative(long, Long, swapJlong)
xToNative(float, Float, swapJint)
xToNative(double, Double, swapJlong)
xToNative(char, Char, swapJshort)
xToNative(boolean, Boolean, )

//Theese methods copy bytes native memory to an array
#define nativeToX(type, capitalType) \
JNIEXPORT jint JNICALL Java_com_troy_empireserialization_util_NativeUtils_nativeTo##capitalType##s(JNIEnv * env, jclass class, j##type##Array dest, jlong src, jint offset, jint elements) {\
	if(src == 0 || dest == 0) return INVALID_ARGUMENT;\
	(*env)->Set##capitalType##ArrayRegion(env, dest, offset, elements, (jbyte*) src);\
	return 0;\
}

nativeToX(byte, Byte)
nativeToX(short, Short)
nativeToX(int, Int)
nativeToX(long, Long)
nativeToX(float, Float)
nativeToX(double, Double)
nativeToX(char, Char)



//wrapper for memcpy
JNIEXPORT void JNICALL Java_com_troy_empireserialization_util_NativeUtils_memcpy(JNIEnv * env, jclass class, jlong dest, jlong src, jlong bytes) {
	if (src == 0 || dest == 0) return;
	memcpy((jbyte*)dest, (jbyte*)src, (size_t)bytes);
}
