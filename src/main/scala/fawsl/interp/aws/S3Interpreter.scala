package fawsl.interp.aws

import cats.~>
import fawsl.free.algebra.s3.S3Op
import fs2.util.{Catchable, Suspendable}
import fawsl.free.algebra
import fawsl.interp.domain.S3

class S3Interpreter[M[_]: Catchable: Suspendable](s3: S3) extends (S3Op ~> M) {
  val L = Predef.implicitly[Suspendable[M]]

  override def apply[A](fa: S3Op[A]): M[A] =
    L.delay(fa match {
      case algebra.s3.`object`.Put(bucketName, key, inputStream, metadata) =>
        s3.`object`.put(bucketName, key, inputStream, metadata)
      case algebra.s3.`object`.Get(bucketName, key) =>
        s3.`object`.get(bucketName, key)
      case algebra.s3.`object`.Delete(bucketName, key) =>
        s3.`object`.delete(bucketName, key)
    })
}

object S3Interpreter {
  def apply[M[_]: Catchable: Suspendable](s3: S3): S3Interpreter[M] =
    S3Interpreter(s3)
}
