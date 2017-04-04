package cache

import java.net.InetAddress
import java.util

import scala.concurrent.Future
import scala.concurrent.duration.Duration
import net.sf.ehcache.{Element, Cache => Ehcache}
import org.slf4j.LoggerFactory

class AddressEhCache(maxAge: Long, unit: Duration, ehcache: Ehcache)
    extends Cache {

  protected final val logger = LoggerFactory.getLogger(getClass.getName)

  def add(address: InetAddress): Boolean = {
    val key = buildKey(address, maxAge)
    try {
      ehcache.get(key) match {
        case null =>
           put(key, address, Some(unit))
          None
        case cachedAddresses => {
          Option(cachedAddresses.getObjectValue)
        }
      }
    } catch {
      case e: Exception => Some(e)
    }
    logger.info(s"Added value $address for key $key")
    true
  }

  /**
    * remove() method will return true if the address was successfully removed
    * @param address
    * @return
    */
  def remove(address: InetAddress): Boolean = {
    val key = buildKey(address, maxAge)
    remove(key)
    logger.info(s"Removed key-value pair for key $key")
    true
  }

  /**
    * The peek() method will return the most recently added element,
    * null if no element exists.
    * @return
    */
  def peek() = {
    import scala.collection.JavaConversions._
    val elements =ehcache.getAll(ehcache.getKeys).values
    elements.head
  }

  /**
    * take() method retrieves and removes the most recently added element
    * from the cache and waits if necessary until an element becomes available.
    * @return
    */
  def take() = {
    val key = ehcache.getKeys.get(0)
    ehcache.remove(key)
  }

  private def buildKey(address: InetAddress, maxAge: Long) = {
    s"${address.getHostName}_${maxAge}"
  }


  override def get[V](key: String) = {
    val result = {
      ehcache.get(key) match {
        case null =>
          /*val addresses = loadAddresses // load addresses from DB to cache
           put(key, addresses, None)
           addresses*/
          None
        case cachedAddresses => Option(cachedAddresses.getObjectValue.asInstanceOf[V])
      }
    }

    logger.info(s"Cache value $result for key $key")
    Future.successful(result)
  }

  override def put[V](key: String, value: V, ttl: Option[Duration]) = {
    val element = new Element(key, value)
    ttl.foreach(t => element.setTimeToLive(t.toSeconds.toInt))
    ehcache.put(element)

    logger.info(s"Value $value put in cache for key $key")
    Future.successful(())
  }

  override def remove(key: String) = Future.successful(ehcache.remove(key))

  override def removeAll() = Future.successful(ehcache.removeAll())

  override def close(): Unit = {
    // Nothing to do
  }
}

object AddressEhCache {
  def apply(maxAge: Long, unit: Duration, ehcache: Ehcache): AddressEhCache = new AddressEhCache(maxAge, unit, ehcache)
}