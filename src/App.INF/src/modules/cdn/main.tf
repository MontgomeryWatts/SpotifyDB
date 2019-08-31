resource "aws_s3_bucket" "spotifydb" {
  bucket = "${var.url}"
  acl = "public-read"

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
    effect = "Allow"
    actions = ["s3:GetObject"]
    resources = ["arn:aws:s3:::${var.url}/*"]
    principals {
      type = "*"
      identifiers = ["*"]
    }
  }
}


resource "aws_s3_bucket_policy" "spotifydb_policy" {
  bucket = "${aws_s3_bucket.spotifydb.id}"
  policy = "${data.aws_iam_policy_document.spotifydb_policy_document.json}"
}


locals {
  s3_origin_id = "testID"
}

resource "aws_cloudfront_distribution" "cloudfront" {
  origin {
    domain_name = "${aws_s3_bucket.spotifydb.bucket_regional_domain_name}"
    origin_id = "${local.s3_origin_id}"
  }

  enabled = true
  is_ipv6_enabled = true
  default_root_object = "index.html"

  default_cache_behavior {
    allowed_methods = ["GET", "HEAD", "OPTIONS"]
    cached_methods = ["GET", "HEAD"]
    target_origin_id = "${local.s3_origin_id}"

    forwarded_values {
      query_string = false

      cookies {
        forward = "none"
      }
    }

    min_ttl = 0
    default_ttl = 86400
    max_ttl = 31536000

    compress = true
    viewer_protocol_policy = "redirect-to-https"
  }

  price_class ="PriceClass_All"

  restrictions {
    geo_restriction {
      restriction_type = "none"
    }
  }

  viewer_certificate {
    cloudfront_default_certificate = true
  }
}

