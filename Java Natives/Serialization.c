#include <jni.h>
#include <stdio.h>
#include <stdlib.h>

#pragma once
#define OUT_OF_MEMORY -1
#define UNSUPPORTED_CHARACTER -2
#define ERROR -1
#define INVALID_ARGUMENT -10

JNIEXPORT jint JNICALL
JNI_OnLoad(JavaVM *vm, void *reserved)
{
	int* ptr = 0;
	*ptr = 3;
	printf("HELO WORLD!!!");
	/*
	void** envRaw = NULL;
	(*vm)->GetEnv(vm, envRaw, JNI_VERSION_1_8);
	JNIEnv* env = *(envRaw);
	jclass cls = (*env)->FindClass(env, "com/troy/serialization/nativelibrary/LibraryProvider");
	jfieldID id = (*env)->GetStaticFieldID(env, cls, "native_load", "Z");
	(*env)->SetStaticBooleanField(env, cls, id, JNI_TRUE);
	*/
	return JNI_VERSION_1_8;
}

const jbyte FOUR_BIT_ENCODING_CACHE[] = { -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, 2, -1, 11, 9, 0, 15, -1, 7, 4, -1, -1, 10, 13, 5, 3, -1, -1, 8, 6, 1, 12, -1, 14 };

void putError(jlong pointer, jbyte code, jchar character, jint index) {
	*((jbyte*)pointer) = code;
	*((jchar*)(pointer + 1)) = character;
	*((jint*)(pointer + 3)) = index;
}

JNIEXPORT jint JNICALL Java_com_troy_serialization_io_NativeFileOutput_fflush
(JNIEnv * env, jclass class, jlong fd) {
	fflush((FILE*)fd);
}

JNIEXPORT jint JNICALL Java_com_troy_serialization_io_NativeFileOutput_fputc
(JNIEnv * env, jclass class, jbyte c, jlong fd) {
	fputc(c, (FILE*)fd);
}

JNIEXPORT jint JNICALL Java_com_troy_serialization_io_NativeFileOutput_fclose
(JNIEnv * env, jclass class, jlong fd) {
	return fclose((FILE*)fd);
}

JNIEXPORT jlong JNICALL Java_com_troy_serialization_io_NativeFileOutput_fopen
(JNIEnv * env, jclass class, jstring name, jstring accessJ) {
	const char* file = (*env)->GetStringUTFChars(env, name, NULL);
	const char* access = (*env)->GetStringUTFChars(env, accessJ, NULL);
	if (file == NULL) return OUT_OF_MEMORY;
	if (access == NULL) return OUT_OF_MEMORY;
	FILE* result = fopen(file, access);
	if (result == NULL) return 0;
	(*env)->ReleaseStringUTFChars(env, name, file);
	(*env)->ReleaseStringUTFChars(env, accessJ, access);
	return (jlong) result;

}



inline jshort swapJshort(jshort value) {
#ifdef VISUAL_CPP
	return _byteswap_ushort(value);
#else
	return  ((value & 0x00FF) << 8) |
		((value & 0xFF00) >> 8);
#endif
}

inline jint swapJint(jint value) {
#ifdef VISUAL_CPP
	return _byteswap_ulong(value);
#else
	return  ((value & 0x000000FF) << 24) |
		((value & 0x0000FF00) << 8) |
		((value & 0x00FF0000) >> 8) |
		((value & 0xFF000000) >> 24);
#endif
}

inline jlong swapJlong(jlong value) {
#ifdef VISUAL_CPP
	return _byteswap_uint64(value);
#else
	return  ((value & 0x00000000000000FF) << 56) |
		((value & 0x000000000000FF00) << 40) |
		((value & 0x0000000000FF0000) << 24) |
		((value & 0x00000000FF000000) << 8) |
		((value & 0x000000FF00000000) >> 8) |
		((value & 0x0000FF0000000000) >> 24) |
		((value & 0x00FF000000000000) >> 40) |
		((value & 0xFF00000000000000) >> 56);
#endif
}

#define xsToFWrite(type, swapFunc) \
JNIEXPORT j##type JNICALL Java_com_troy_serialization_util_NativeUtils_##type##sToFWrite(JNIEnv * env, jclass class, jlong fd, j##type##Array srcJ, jint srcOffset, jint elements, jboolean swapEndianess) {\
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
xsToFWrite(float, swapJint)
xsToFWrite(double, swapJlong)
xsToFWrite(char, swapJshort)




#define xToFWrite(type, swapFunc) \
JNIEXPORT j##type JNICALL Java_com_troy_serialization_util_NativeUtils_##type##ToFWrite(JNIEnv * env, jclass class, jlong fd, j##type value, jboolean swapEndianess) {\
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
xToFWrite(boolean, )
xToFWrite(short, swapJshort)
xToFWrite(int, swapJint)
xToFWrite(long, swapJlong)
xToFWrite(float, swapJint)
xToFWrite(double, swapJlong)
xToFWrite(char, swapJshort)

JNIEXPORT jbyteArray JNICALL Java_com_troy_serialization_io_NativeOutput_ngetBuffer
(JNIEnv * env, jclass class, jlong addressJ, jint capacity)
{
	const jbyte* address = (jbyte*)addressJ;
	jbyteArray result = (*env)->NewByteArray(env, capacity);
	if (result == NULL) return NULL;
	(*env)->SetByteArrayRegion(env, result, 0, capacity, address);
	return result;
}

/*
* Class:     com_troy_serialization_FourBitEncodingCharset
* Method:    nEncode
* Signature: ([C[BIIIJZ)I
*/
JNIEXPORT jint JNICALL Java_com_troy_serialization_io_FourBitEncodingCharset_nEncode
(JNIEnv * env, jobject charset, jcharArray srcJ, jbyteArray destJ, jint srcOffset, jint destOffset, jsize srcLength, jlong errorAddress, jboolean checkForErrors)
{
	jboolean copy = 0;
	jchar* src = (*env)->GetPrimitiveArrayCritical(env, srcJ, &copy);
	jbyte* dest = (*env)->GetPrimitiveArrayCritical(env, destJ, &copy);
	if (src == NULL || dest == NULL) {
		putError(errorAddress, OUT_OF_MEMORY, 0, 0);
		return ERROR;
	}
	if (checkForErrors) {
		for (int i = srcOffset; i < srcLength; i++) {
			if (src[i] >= sizeof(FOUR_BIT_ENCODING_CACHE) || FOUR_BIT_ENCODING_CACHE[src[i]] == -1) {
				putError(errorAddress, UNSUPPORTED_CHARACTER, src[i], i);
				return ERROR;
			}
		}
	}
	int bytesWritten = 0;

	int i = 0;
	const int end = (srcLength / 2) * 2;//round down to next mutiple of two
	while (i < end) {
		dest[bytesWritten++] = (FOUR_BIT_ENCODING_CACHE[src[i]] << 4) | FOUR_BIT_ENCODING_CACHE[src[i + 1]];
		i += 2;
	}
	if (srcLength % 2 != 0) {
		dest[bytesWritten++] = FOUR_BIT_ENCODING_CACHE[src[end]] << 4;
	}
	(*env)->ReleasePrimitiveArrayCritical(env, srcJ, src, 0);
	(*env)->ReleasePrimitiveArrayCritical(env, destJ, dest, 0);
	return bytesWritten;
}
#define VISUAL_CPP


//public static native void shortsToBytes(byte[] dest, short[] src, int srcOffset, int destOffset, int elements);

#define xToBytes(type, swapFunc) \
JNIEXPORT j##type JNICALL Java_com_troy_serialization_util_NativeUtils_##type##sToBytes(JNIEnv * env, jclass class, jbyteArray destJ, j##type##Array srcJ, jint srcOffset, jint destOffset, jint elements, jboolean swapEndianess) {\
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


JNIEXPORT jint JNICALL Java_com_troy_serialization_util_NativeUtils_booleansToBytesCompact(JNIEnv * env, jclass class, jbyteArray destJ, jbooleanArray srcJ, jint srcOffset, jint destOffset, jint elements) {
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
JNIEXPORT jint JNICALL Java_com_troy_serialization_util_NativeUtils_##type##sToNative(JNIEnv * env, jclass class, jlong dest, j##type##Array src, jint offset, jint elements, jboolean swapEndianess) {\
	if(src == 0 || dest == 0) return INVALID_ARGUMENT;\
	(*env)->Get##capitalType##ArrayRegion(env, src, offset, elements, (j##type *)dest); \
	if(swapEndianess) {\
\
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
JNIEXPORT jint JNICALL Java_com_troy_serialization_util_NativeUtils_nativeTo##capitalType##s(JNIEnv * env, jclass class, j##type##Array dest, jlong src, jint offset, jint elements) {\
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
JNIEXPORT void JNICALL Java_com_troy_serialization_util_NativeUtils_memcpy(JNIEnv * env, jclass class, jlong dest, jlong src, jlong bytes) {
	memcpy((jbyte*)dest, (jbyte*)src, (size_t)bytes);
}
