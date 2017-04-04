package cache

import scala.collection.concurrent.TrieMap

import scala.concurrent.Future
import scala.concurrent.duration.Duration

/**
  * Created by pati80 on 25/03/17.
  */

class AddressTrieMapCache extends Cache{

  /*protected def cachedAddresses: TrieMap[String, String] = new TrieMap[String, String]

  override def get[V](key: String): Future[Option[V]] = {
    val result = {
      cachedAddresses.get(key) match {
        case null =>
          /*val addresses = loadAddresses // load addresses from DB to cache
           cachedAddresses.put(key, addresses, None)
           addresses*/
          None
        case cachedAddresses => cachedAddresses
      }
    }

    Future.successful(result)
  }

  override def put[V](key: String, value: V, ttl: Option[Duration]) = {
    cachedAddresses.put(key, value.toString)
    Future.successful(())
  }

  override def remove(key: String) = Future.successful(cachedAddresses.remove(key))

  override def removeAll() = Future.successful(cachedAddresses.clear())

  override def close(): Unit = {
    // Nothing to do
  }*/
  override def get[V](key: String): Future[Option[V]] = ???

  override def put[V](key: String, value: V, ttl: Option[Duration]): Future[Unit] = ???

  override def remove(key: String): Future[Unit] = ???

  override def removeAll(): Future[Unit] = ???

  override def close(): Unit = ???
}