package cache

import java.net.InetAddress
import scala.collection.concurrent.TrieMap


trait TrieMapCache {

  protected def cachedAddresses: TrieMap[String, InetAddress]

}