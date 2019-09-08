provider "aws" {
  version = "~> 2.0"
  profile = "terraform-user"
  region  = "${var.aws_region}"
}

locals {
  table_name = "${var.environment}-${var.app_name}"
  ssm_prefix = "/${var.environment}/${var.app_name}/"
}


module "cdn" {
  source = "./modules/cdn"
  url    = "${var.site_url}"
}

module "dynamodb" {
  source     = "./modules/dynamodb"
  table_name = "${local.table_name}"
}

module "ssm" {
  source     = "./modules/ssm"
  prefix     = "${local.ssm_prefix}"
  table_name = "${local.table_name}"
}