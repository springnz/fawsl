package fawsl.interp.domain

import java.io.InputStream
import java.util.Date

import com.amazonaws.services.s3.AmazonS3
import fawsl.model.s3.{ObjectMetadata, PutObjectResponse, S3Object}
import com.amazonaws.services.s3.model.{
  PutObjectResult,
  ObjectMetadata => AwsObjectMetadata,
  S3Object => AwsS3Object
}

import scala.collection.JavaConverters._

class S3(s3Client: AmazonS3) {
  import S3._

  object `object` {
    def put(bucketName: String,
            key: String,
            inputStream: InputStream,
            metadata: ObjectMetadata): PutObjectResponse = {
      s3Client.putObject(bucketName, key, inputStream, metadata.toAws).toFawsl
    }

    def get(bucketName: String, key: String): S3Object = {
      s3Client.getObject(bucketName, key).toFawsl
    }

    def delete(bucketName: String, key: String): Unit = {
      s3Client.deleteObject(bucketName, key)
    }
  }
}

object S3 {
  def apply(s3Client: AmazonS3): S3 = new S3(s3Client)

  implicit class ObjectMetadataOps(metadata: ObjectMetadata) {
    val toAws: AwsObjectMetadata = {
      val aMetadata = new AwsObjectMetadata()
      metadata.cacheControl.foreach(aMetadata.setCacheControl)
      metadata.contentLength.foreach(aMetadata.setContentLength)
      metadata.contentType.foreach(aMetadata.setContentType)
      metadata.disposition.foreach(aMetadata.setContentDisposition)
      metadata.encoding.foreach(aMetadata.setContentEncoding)
      metadata.encryptionAlgorithm.foreach(aMetadata.setSSEAlgorithm)
      metadata.expirationRuleId.foreach(aMetadata.setExpirationTimeRuleId)
      metadata.httpExpiration.foreach(i =>
        aMetadata.setHttpExpiresDate(new Date(i.toEpochMilli)))
      metadata.language.foreach(aMetadata.setContentLanguage)
      metadata.lastModified.foreach(i =>
        aMetadata.setLastModified(new Date(i.toEpochMilli)))
      metadata.md5Hash.foreach(aMetadata.setContentMD5)
      metadata.userMetadata.foreach {
        case (key, value) => aMetadata.addUserMetadata(key, value)
      }
      aMetadata
    }
  }

  implicit class AwsObjectMetadataOps(m: AwsObjectMetadata) {
    def toFawsl: ObjectMetadata = {
      ObjectMetadata(Option(m.getExpirationTimeRuleId),
                     Option(m.getLastModified).map(_.toInstant),
                     Option(m.getContentLength),
                     Option(m.getContentType),
                     Option(m.getContentLanguage),
                     Option(m.getContentEncoding),
                     Option(m.getCacheControl),
                     Option(m.getContentMD5),
                     Option(m.getContentDisposition),
                     Option(m.getSSEAlgorithm),
                     Option(m.getHttpExpiresDate).map(_.toInstant),
                     m.getUserMetadata.asScala.toMap)
    }
  }

  implicit class AwsS3ObjectOps(o: AwsS3Object) {
    val toFawsl: S3Object = S3Object(o.getBucketName,
                                     o.getKey,
                                     o.getObjectContent,
                                     o.getObjectMetadata.toFawsl,
                                     Option(o.getRedirectLocation),
                                     Option(o.getTaggingCount),
                                     o.isRequesterCharged)
  }

  implicit class AwsPutObjectResultOps(o: PutObjectResult) {
    val toFawsl: PutObjectResponse = PutObjectResponse(
      o.getContentMd5,
      o.getETag,
      o.getVersionId,
      o.isRequesterCharged,
      Option(o.getMetadata).map(_.toFawsl),
      Option(o.getExpirationTime).map(_.toInstant),
      Option(o.getExpirationTimeRuleId))
  }
}
