package fawsl.free.algebra

import cats.free.Free
import fawsl.free.algebra.s3.S3Op

object aws {
  type AwsOp[A] = S3Op[A] // Coproduct as more API are added
  type AwsIO[A] = Free[AwsOp, A]
}
