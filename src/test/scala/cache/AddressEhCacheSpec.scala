package cache

import java.net.InetAddress

import net.sf.ehcache.{CacheManager, Element, Cache => Ehcache}
import org.scalatest.concurrent.Eventually
import org.scalatest.time.{Seconds, Span}
import org.scalatest.{FlatSpec, Matchers}

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration._

class AddressEhCacheSpec extends FlatSpec with Matchers with Eventually {

  var ehcache = {
    val cacheManager = new CacheManager
    val cache = new Ehcache("test", 1000, false, false, 0, 0)
    cacheManager.addCache(cache)
    cache
  }

  val addressCache = new AddressCache(100L, Duration(100, MILLISECONDS))
  val ip = InetAddress.getByName("www.agoda.com")
  val ip1 = InetAddress.getByName("www.google.com")
  val ip2 = InetAddress.getByName("www.yahoo.com")
  val addressEhCache = AddressEhCache(10, Duration(1000, MILLISECONDS), ehcache)


  "get" should "return the value from Ehcache" in {
    ehcache.put(new Element("key1", 123))

    addressEhCache.get[String]("key1").map { result =>
      result should be(Some(123))
    }
  }

  it should "return None if the given key does not exist" in {
    addressEhCache.get[String]("nokey").map { result =>
      result should be(None)
    }
  }

  "add" should "return true on adding element to cache" in {
    addressEhCache.add(ip) shouldBe(true)
    addressEhCache.add(ip1) shouldBe(true)
  }

  "peek" should "return all key-value pair from cache" in {
    addressEhCache.add(ip2) shouldBe(true)
    //AddressEhCache(10, Duration(10, MILLISECONDS), ehcache).peek() should be(Some(""))
  }

  "put" should "put given key-value pair in cache" in {
    addressEhCache.put("key1", 123, None)
    ehcache.get("key1").getObjectValue should be(123)
  }

  it should "put given key-value pair in cache with time" in {
    addressEhCache.put("key1", 123, Some(1 second))
    ehcache.get("key1").getObjectValue should be(123)

    // Should expire after 1 second
    eventually(timeout(Span(2, Seconds))) {
      ehcache.get("key1") should be(null)
    }
  }


  "remove" should "remove given key and its value from cache" in {
    ehcache.put(new Element("key1", 123))
    ehcache.get("key1").getObjectValue should be(123)

    addressEhCache.remove("key1")
    ehcache.get("key1") should be(null)
  }
}