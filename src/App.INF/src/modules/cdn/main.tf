resource "aws_s3_bucket" "spotifydb" {
  bucket = "${var.url}"
  acl    = "public-read"

  server_side_encryption_configuration {
    rule {
      apply_server_side_encryption_by_default {
        sse_algorithm = "AES256"
      }
    }
  }

  website {
    index_document = "index.html"
  }
}

resource "aws_s3_bucket" "www_spotifydb" {
  bucket = "www.${var.url}"

  website {
    redirect_all_requests_to = "${var.url}"
  }
}

data "aws_iam_policy_document" "spotifydb_policy_document" {
  version = "2012-10-17"
  statement {
    effect    = "Allow"
    actions   = ["s3:GetObject"]
    resources = ["arn:aws:s3:::${var.url}/*"]
    principals {
      type        = "*"
      identifiers = ["*"]
    }
  }
}


resource "aws_s3_bucket_policy" "spotifydb_policy" {
  bucket = "${aws_s3_bucket.spotifydb.id}"
  policy = "${data.aws_iam_policy_document.spotifydb_policy_document.json}"
}


locals {
  s3_origin_id = "S3-spotifydb.com"
}

resource "aws_cloudfront_distribution" "cloudfront" {
  origin {
    domain_name = "${aws_s3_bucket.spotifydb.bucket_regional_domain_name}"
    origin_id   = "${local.s3_origin_id}"
  }

  wait_for_deployment = false
  enabled             = true
  is_ipv6_enabled     = true
  default_root_object = "index.html"

  aliases = ["${var.url}", "www.${var.url}"]

  default_cache_behavior {
    allowed_methods  = ["GET", "HEAD", "OPTIONS"]
    cached_methods   = ["GET", "HEAD"]
    target_origin_id = "${local.s3_origin_id}"

    forwarded_values {
      query_string = false

      cookies {
        forward = "none"
      }
    }

    min_ttl     = 0
    default_ttl = 86400
    max_ttl     = 31536000

    compress               = true
    viewer_protocol_policy = "redirect-to-https"
  }

  price_class = "PriceClass_All"

  restrictions {
    geo_restriction {
      restriction_type = "none"
    }
  }

  viewer_certificate {
    acm_certificate_arn      = "${aws_acm_certificate.spotifydb_cert.arn}"
    minimum_protocol_version = "TLSv1.2_2018"
    ssl_support_method       = "sni-only"
  }
}

resource "aws_acm_certificate" "spotifydb_cert" {
  domain_name               = "${var.url}"
  subject_alternative_names = ["*.${var.url}"]
  validation_method         = "DNS"
}

resource "aws_route53_zone" "spotifydb_hosted_zone" {
  name = "${var.url}."
}

resource "aws_route53_record" "spotifydb_cert_record" {
  name    = "${aws_acm_certificate.spotifydb_cert.domain_validation_options.0.resource_record_name}"
  type    = "${aws_acm_certificate.spotifydb_cert.domain_validation_options.0.resource_record_type}"
  zone_id = "${aws_route53_zone.spotifydb_hosted_zone.zone_id}"
  records = ["${aws_acm_certificate.spotifydb_cert.domain_validation_options.0.resource_record_value}"]
  ttl     = 300
}

resource "aws_acm_certificate_validation" "spotifydb_cert_validation" {
  certificate_arn         = "${aws_acm_certificate.spotifydb_cert.arn}"
  validation_record_fqdns = ["${aws_route53_record.spotifydb_cert_record.fqdn}"]
}

resource "aws_route53_record" "apex_record" {
  zone_id = "${aws_route53_zone.spotifydb_hosted_zone.zone_id}"
  name    = "${var.url}"
  type    = "A"

  alias {
    name                   = "${aws_cloudfront_distribution.cloudfront.domain_name}"
    zone_id                = "${aws_cloudfront_distribution.cloudfront.hosted_zone_id}"
    evaluate_target_health = false
  }
}

resource "aws_route53_record" "www_record" {
  zone_id = "${aws_route53_zone.spotifydb_hosted_zone.zone_id}"
  name    = "www.${var.url}"
  type    = "A"

  alias {
    name                   = "${aws_cloudfront_distribution.cloudfront.domain_name}"
    zone_id                = "${aws_cloudfront_distribution.cloudfront.hosted_zone_id}"
    evaluate_target_health = false
  }
}
