package cache

import java.net.InetAddress

import org.slf4j.LoggerFactory

import scala.collection.concurrent.TrieMap
import scala.concurrent.duration.Duration

/**
  * Created by pati80 on 25/03/17.
  */

class AddressCache(maxAge: Long, unit: Duration) {

  protected def cachedAddressesTrieMap: TrieMap[String, InetAddress] = new TrieMap[String, InetAddress] // wrong way it wll always give new object
  protected final val logger = LoggerFactory.getLogger(getClass.getName)

  /**
    * add() method must store unique elements only (existing elements must be ignored).
    * This will return true if the element was successfully added.
    * @return
    */

  def add(address: InetAddress): Boolean = {
    val key = buildKey(address, maxAge, unit)
    val inetAddress = try {
      cachedAddressesTrieMap.getOrElseUpdate(key,address)
    } catch {
      case e: Exception => Some(e)
    }
    logger.info(s"Cache value $address for key $key")
    if(inetAddress != null) true else false
  }

  /**
    * remove() method will return true if the address was successfully removed
    * @param address
    * @return
    */
  def remove(address: InetAddress): Boolean = {
    val key = buildKey(address, maxAge, unit)
    logger.info(s"Removed key-value pair for key $key")
    !cachedAddressesTrieMap.remove(key).isDefined
  }

  /**
    * The peek() method will return the most recently added element,
    * null if no element exists.
    * @return
    */
  def peek():InetAddress = {
    cachedAddressesTrieMap.head._2
  }

  /**
    * take() method retrieves and removes the most recently added element
    * from the cache and waits if necessary until an element becomes available.
    * @return
    */
  def take():Option[InetAddress] = {
    val key = cachedAddressesTrieMap.head._1
    cachedAddressesTrieMap.remove(key)
  }

  private def buildKey(address: InetAddress, maxAge: Long, unit: Duration) = {
    s"${address.getHostName}_${maxAge}_${unit}"
  }

}