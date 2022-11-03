import java.net.InetSocketAddress
import java.nio.ByteBuffer
import java.nio.channels.SocketChannel

fun main() {
    val client = SocketChannel.open(InetSocketAddress("localhost", 2232))
    repeat(100) {
        val str = (0..20).map {
            "Hello world $it\n"
        }.reduceRight { a, b ->
            a + b
        }
        val buffer = ByteBuffer.wrap(str.toByteArray())
        client.write(buffer)
        buffer.clear()
        client.read(buffer)
        println(String(buffer.array()))
    }
    client.close()
}