import java.math.BigInteger
import java.security.MessageDigest
import kotlin.io.path.Path
import kotlin.io.path.readLines
import kotlin.io.path.readText

fun readInput(name: String): List<String> = Path("src/$name.txt").readLines()

fun read(name: String): String = Path("src/$name.txt").readText()

fun String.md5() = BigInteger(1, MessageDigest.getInstance("MD5").digest(toByteArray()))
  .toString(16)
  .padStart(32, '0')

fun Any?.println() = println(this)
