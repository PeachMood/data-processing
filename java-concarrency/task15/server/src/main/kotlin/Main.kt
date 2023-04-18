import java.io.File
import java.net.InetSocketAddress
import java.nio.ByteBuffer
import java.nio.channels.SelectionKey
import java.nio.channels.Selector
import java.nio.channels.ServerSocketChannel
import java.nio.channels.SocketChannel
import java.nio.file.Path
import java.util.TreeMap

fun main() {
    val selector = Selector.open()
    val serverSocketChanel = ServerSocketChannel.open()
    serverSocketChanel.bind(InetSocketAddress("localhost", 3343))
    serverSocketChanel.configureBlocking(false)
    serverSocketChanel.register(selector, SelectionKey.OP_ACCEPT)

    val path = ProcessBuilder()
    while (true) {
        selector.select()
        val selectedKeys = selector.selectedKeys()
        val iterator = selectedKeys.iterator()
        while (iterator.hasNext()) {
            val key = iterator.next()
            if (key.isAcceptable) {
                val connection = serverSocketChanel.accept()
                connection.configureBlocking(false)
                connection.register(selector, SelectionKey.OP_READ)
                println("new connection established")
            }
            if (key.isReadable) {
                val buffer = ByteBuffer.allocate(1024)
                val client = key.channel() as SocketChannel
                try{
                    val n = client.read(buffer)
                    if (n != -1) {
                        buffer.flip()
                        client.write(buffer)
                    } else {
                        client.close()
                    }
                } catch (ignored:Exception){
                    client.close()
                }
            }
            iterator.remove()
        }
        println("iteration")
    }
}