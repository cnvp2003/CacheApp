package cache

import java.net.InetAddress

import org.scalatest.concurrent.Eventually
import org.scalatest.{FlatSpec, Matchers}

import scala.collection.concurrent.TrieMap
import scala.concurrent.duration.{Duration, _}

class AddressCacheSpec extends FlatSpec with Matchers with Eventually {

  protected def cachedAddressesTrieMap: TrieMap[String, InetAddress] = new TrieMap[String, InetAddress]
  val addressCache = new AddressCache(100L, Duration(100, MILLISECONDS))
  val ip = InetAddress.getByName("www.agoda.com")
  val ip1 = InetAddress.getByName("www.google.com")
  val ip2 = InetAddress.getByName("www.yahoo.com")

  "add" should "return true on adding value to cache" in {
    addressCache.add(ip) shouldBe true
    addressCache.add(ip) shouldBe true
  }

  "remove" should "delete key-value pair from cache" in {
    addressCache.add(ip1) shouldBe true
    addressCache.remove(ip1) shouldBe true
  }

  "peek" should "return the most recently added element" in {
    addressCache.peek() shouldBe ""
  }

  "take" should "retrieves and removes the most recently added element" in {
    addressCache.take() shouldBe ""
  }
}