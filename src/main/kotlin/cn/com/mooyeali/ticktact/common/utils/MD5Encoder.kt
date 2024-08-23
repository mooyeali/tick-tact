package cn.com.mooyeali.ticktact.common.utils

import java.security.MessageDigest
import java.security.NoSuchAlgorithmException


/**
 * A utility class for encoding a binary MD5 digest as a string.
 */
object MD5Encoder {
    private val _hexadecimal = charArrayOf(
        '0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
        'a', 'b', 'c', 'd', 'e', 'f'
    )

    private var _digest: MessageDigest? = null

    /**
     * Encodes the 128 bit (16 bytes) MD5 into a 32 character String.
     *
     * @param binaryData The byte array containing the digest.
     *
     * @return the encoded MD5.
     *
     * @throws NullPointerException if the given binary data is null
     * @throws IllegalArgumentException if the given binary data is not
     * 16 bytes in length.
     */
    fun encode(binaryData: ByteArray): String {
        require(binaryData.size == 16)

        val buffer = CharArray(32)

        for (i in 0..15) {
            buffer[i * 2] = _hexadecimal[((binaryData[i].toInt() and 0xf0) shr 4)]
            buffer[i * 2 + 1] = _hexadecimal[(binaryData[i].toInt() and 0x0f)]
        }

        return String(buffer)
    }

    /**
     * Produce an MD5 digest the given byte array.
     *
     * @param binaryData The bytes to be digested.
     *
     * @return the digest as a 128 bit (16 bytes) MD5.
     *
     * @throws NullPointerException if the given binary data is null.
     *
     * @see java.security.MessageDigest
     */
    fun digest(binaryData: ByteArray?): ByteArray {
        if (_digest == null) {
            synchronized(MD5Encoder::class.java) {
                if (_digest == null) {
                    try {
                        _digest = MessageDigest.getInstance("MD5")
                    } catch (nsae: NoSuchAlgorithmException) {
                        throw IllegalStateException(nsae.javaClass.name + ": " + nsae.message)
                    }
                }
            }
        }
        return _digest!!.digest(binaryData)
    }
}
