package fawsl.free.algebra

import java.io.InputStream

import fawsl.model.s3.{ObjectMetadata, PutObjectResponse, S3Object}

object s3 {
  sealed trait S3Op[A]

  object `object` {

    final case class Put(bucketName: String,
                         key: String,
                         inputStream: InputStream,
                         metadata: ObjectMetadata)
        extends S3Op[PutObjectResponse]

    final case class Get(bucketName: String, key: String)
        extends S3Op[S3Object]

    final case class Delete(bucketName: String, key: String) extends S3Op[Unit]

  }
}
