provider "aws" {
  version = "~> 2.0"
  profile = "terraform-user"
  region  = "${var.aws_region}"
}

locals {
  table_name = "${var.environment}-${var.app_name}"
  ssm_prefix = "/${var.environment}/${var.app_name}/"
  aliases    = ["${var.site_url}", "www.${var.site_url}"]
}

module "s3" {
  source = "./modules/s3"
  url    = "${var.site_url}"
}
module "acm" {
  source           = "./modules/acm"
  site_domain_name = "${var.site_url}"
}

module "route53" {
  source                    = "./modules/route53"
  domain_validation_options = "${module.acm.domain_validation_options}"
  site_domain_name          = "${var.site_url}"
  certificate_arn           = "${module.acm.certificate_arn}"
  cloudfront_domain_name    = "${module.cloudfront.domain_name}"
  cloudfront_hosted_zone_id = "${module.cloudfront.hosted_zone_id}"
}


module "cloudfront" {
  source             = "./modules/cloudfront"
  certificate_arn    = "${module.acm.certificate_arn}"
  bucket_domain_name = "${module.s3.bucket_regional_domain_name}"
  bucket_origin_id   = "S3-${var.site_url}"
  aliases            = "${local.aliases}"
}


module "dynamodb" {
  source         = "./modules/dynamodb"
  table_name     = "${local.table_name}"
  read_capacity  = "${var.dynamo_read_capacity}"
  write_capacity = "${var.dynamo_write_capacity}"
}

module "ssm" {
  source     = "./modules/ssm"
  prefix     = "${local.ssm_prefix}"
  table_name = "${local.table_name}"
}