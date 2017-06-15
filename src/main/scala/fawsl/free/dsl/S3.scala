package fawsl.free.dsl

import java.io.InputStream

import cats.free.{Free, Inject}
import fawsl.free.algebra
import fawsl.free.algebra.s3.S3Op
import fawsl.model.s3.ObjectMetadata

class S3[F[_]](implicit i: Inject[S3Op, F]) {
  implicit class S3OpOps[A](s3Op: S3Op[A]) {
    val injectF: Free[F, A] = Free.inject[S3Op, F](s3Op)
  }

  object `object` {
    def put(bucketName: String,
            key: String,
            inputStream: InputStream,
            metadata: ObjectMetadata) =
      algebra.s3.`object`.Put(bucketName, key, inputStream, metadata)

    def get(bucketName: String, key: String) =
      algebra.s3.`object`.Get(bucketName, key)

    def delete(bucketName: String, key: String) =
      algebra.s3.`object`.Delete(bucketName, key)
  }
}
