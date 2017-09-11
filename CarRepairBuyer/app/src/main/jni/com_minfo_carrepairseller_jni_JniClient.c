#include "com_minfo_carrepairseller_jni_JniClient.h"
#include "Alg.h"
#include <string.h>

#ifdef __cplusplus   
extern "C"  
{   
#endif  
/*
 * Class:     com_minfo_issuenotification_JniClient
 * Method:    GetEncodeStr
 * Signature: (Ljava/lang/String;Ljava/lang/String;)Ljava/lang/String;
 */
JNIEXPORT jstring JNICALL Java_com_minfo_carrepairseller_jni_JniClient_GetEncodeStr
  (JNIEnv *env, jclass arg, jstring instring)
{    
    int ret;
    char strreval[128] = "";
    memset(strreval,0,sizeof(strreval));	
    char *strimsi = (*env)->GetStringUTFChars(env, instring, JNI_FALSE);    
    ret =encryptedString(strimsi,strreval);
    if(ret !=0)
    {
        return NULL;
    }

    (*env)->ReleaseStringUTFChars(env,instring,strimsi);   
    return (*env)->NewStringUTF(env,strreval);        
}

/*
 * Class:     com_minfo_happyshaking_JniClient
 * Method:    GetDecodeStr
 * Signature: (Ljava/lang/String;)Ljava/lang/String;
 */
JNIEXPORT jstring JNICALL Java_com_minfo_carrepairseller_jni_JniClient_GetDecodeStr
  (JNIEnv *env, jclass arg, jstring instring)
{
    int ret;
    char strreval[128] = "";
    memset(strreval,0,sizeof(strreval));
	
    char *strimsi = (*env)->GetStringUTFChars(env, instring, JNI_FALSE);

    ret =decryptedString(strimsi,strreval);
    if(ret !=0)
    {
        return NULL;
    }

    (*env)->ReleaseStringUTFChars(env,instring,strimsi);   
    return (*env)->NewStringUTF(env,strreval);
}

#ifdef __cplusplus   
}   
#endif
