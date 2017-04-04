package cache

import scala.concurrent.Future
import scala.concurrent.duration.Duration

trait Cache {

  def get[V](key: String): Future[Option[V]]

  def put[V](key: String, value: V, ttl: Option[Duration]): Future[Unit]

  def remove(key: String): Future[Unit]

  def removeAll(): Future[Unit]

  def close(): Unit

}