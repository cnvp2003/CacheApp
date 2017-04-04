package app

import java.net.InetAddress

import cache.AddressCache
import org.slf4j.LoggerFactory

import scala.concurrent.duration.{Duration, MILLISECONDS}
import scala.collection.JavaConverters._
import scala.collection.mutable

/**
  * Created by pati80 on 25/02/17.
  */
object Application extends App {

 protected final val logger = LoggerFactory.getLogger(getClass.getName)

  val ip = InetAddress.getByName("www.agoda.com")
  val ip1 = InetAddress.getByName("www.google.com")
  val ip2 = InetAddress.getByName("www.yahoo.com")
  new AddressCache(100L, Duration(100, MILLISECONDS)).add(ip)

}
