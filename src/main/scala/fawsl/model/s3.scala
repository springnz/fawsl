package fawsl.model

import java.io.InputStream
import java.time.Instant

object s3 {
  final case class ObjectMetadata(
      expirationRuleId: Option[String] = None,
      lastModified: Option[Instant] = None,
      contentLength: Option[Long] = None,
      contentType: Option[String],
      language: Option[String] = None,
      encoding: Option[String] = None,
      cacheControl: Option[String] = None,
      md5Hash: Option[String] = None,
      disposition: Option[String] = None,
      encryptionAlgorithm: Option[String] = None,
      httpExpiration: Option[Instant] = None,
      userMetadata: Map[String, String] = Map.empty)

  final case class PutObjectResponse(md5Hash: String,
                                     etag: String,
                                     versionId: String,
                                     requesterCharged: Boolean = false,
                                     metadata: Option[ObjectMetadata] = None,
                                     expirationTime: Option[Instant] = None,
                                     expirationTypeRuleId: Option[String] =
                                       None)

  final case class S3Object(bucketName: String,
                            key: String,
                            inputStream: InputStream,
                            metadata: ObjectMetadata,
                            redirectLocation: Option[String],
                            taggingCount: Option[Integer],
                            isRequesterCharged: Boolean = false)
}
