package fawsl.interp.aws

import cats.~>
import fawsl.free.algebra.aws.AwsOp
import fawsl.interp.domain.S3
import fs2.util.{Catchable, Suspendable}

object AwsInterpreter {
  def apply[M[_]: Catchable: Suspendable](s3: S3): (AwsOp ~> M) =
    S3Interpreter(s3)
}
