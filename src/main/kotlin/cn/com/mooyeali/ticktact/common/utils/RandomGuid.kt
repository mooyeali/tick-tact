package cn.com.mooyeali.ticktact.common.utils

import java.net.InetAddress
import java.security.SecureRandom
import java.util.*


/**
 * @author user
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
object RandomGuid {
    private val _rand: Random
    private val _secureRand = SecureRandom()
    private var _ipAddress = "10.234.0.1"

    /*
	    Static block to take care of one time secureRandom seed.
	    It takes a few seconds to initialize SecureRandom.  You might
	    want to consider removing this static block or replacing
	    it with a "time since first loaded" seed to reduce this time.
	    This block will run only once per JVM instance.
	   */
    init {
        val secureInitializer = _secureRand.nextLong()
        _rand = Random(secureInitializer)

        try {
            val id = InetAddress.getLocalHost()
            _ipAddress = id.toString()
        } catch (t: Throwable) {
            t.printStackTrace()
        }
    }

    /**
     * Generate a new GUID, indicating if it should be secure or not.
     *
     *
     * Secure GUIDs are more difficult to predict if previous GUIDs have already
     * been seen.
     *
     * @return the GUID in the standard format for GUIDs
     * (eg: 8CD4AFAA-1B48-0A78-CF43-32014A25A0EB)
     */
    fun generateGuid(secure: Boolean): String {
        var longPart = 0L

        val random = if ((secure)) _secureRand else _rand

        synchronized(RandomGuid::class.java) {
            longPart = random.nextLong()
        }

        val timePart = System.currentTimeMillis()

        val buf = StringBuffer(256)
        buf.append(_ipAddress)
        buf.append(':')
        buf.append(timePart)
        buf.append(':')
        buf.append(longPart)

        val preDigest = buf.toString()

        val postDigest = MD5Encoder.encode(MD5Encoder.digest(preDigest.toByteArray()))

        val raw = postDigest.uppercase(Locale.getDefault())
        buf.setLength(0)
        buf.append("K")
        buf.append(DateTimeUtil.getDateTime("yyyyMMddHHmmsss"))
        buf.append("-")
        buf.append(raw.substring(13))
        return buf.toString()
    }

    fun generateGuidTrail(secure: Boolean): String {
        var longPart = 0L

        val random = if ((secure)) _secureRand else _rand

        synchronized(RandomGuid::class.java) {
            longPart = random.nextLong()
        }

        val timePart = System.currentTimeMillis()

        val buf = StringBuffer(256)
        buf.append(_ipAddress)
        buf.append(':')
        buf.append(timePart)
        buf.append(':')
        buf.append(longPart)

        val preDigest = buf.toString()

        val postDigest = MD5Encoder.encode(MD5Encoder.digest(preDigest.toByteArray()))

        val raw = postDigest.uppercase(Locale.getDefault())
        buf.setLength(0)
        buf.append("K")
        buf.append(DateTimeUtil.getDateTime("yyyyMMddHHmmsss"))
        buf.append(raw.substring(12))
        return buf.toString()
    }
}
