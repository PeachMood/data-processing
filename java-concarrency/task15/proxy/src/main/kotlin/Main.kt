import java.net.InetSocketAddress
import java.nio.ByteBuffer
import java.nio.channels.SelectionKey
import java.nio.channels.Selector
import java.nio.channels.ServerSocketChannel
import java.nio.channels.SocketChannel

fun main(args: Array<String>) {
    val destinationPort = 3343
    val destinationHost = "localhost"

    val proxyPort = 2232
    val proxyHost = "localhost"

    val selector = Selector.open()
    val serverSocketChannel = ServerSocketChannel.open()
    serverSocketChannel.bind(InetSocketAddress(proxyHost, proxyPort))
    serverSocketChannel.configureBlocking(false)
    serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT)

    while (true) {
        selector.select()

        val selectedKeys = selector.selectedKeys()
        val iterator = selectedKeys.iterator()
        val buffer = ByteBuffer.allocate(1024)

        while (iterator.hasNext()) {
            val key = iterator.next()
            if (key.isAcceptable) {
                // connection from client
                val fromClient = serverSocketChannel.accept()
                fromClient.configureBlocking(false)
                val fromClientKey = fromClient.register(selector, SelectionKey.OP_READ)

                // connection to server
                val toServer = SocketChannel.open(
                    InetSocketAddress(destinationHost, destinationPort)
                )
                toServer.configureBlocking(false)
                val toServerKey = toServer.register(selector, SelectionKey.OP_READ)

                // attach this keys to each other
                fromClientKey.attach(toServerKey)
                toServerKey.attach(fromClientKey)

            } else if (key.isReadable) {
                val client = key.channel() as SocketChannel
                val pairClient = ((key.attachment() as SelectionKey).channel() as SocketChannel)

                if (client.isOpen && pairClient.isOpen) {
                    try {
                        val n = client.read(buffer)
                        if(n != -1){
                            buffer.flip()
                            pairClient.write(buffer)
                        } else {
                            client.close()
                            pairClient.close()
                        }
                    } catch (ignored: Exception) {
                        client.close()
                        pairClient.close()
                    }
                }

            }
            iterator.remove()
            println("iteration")
        }
    }

}