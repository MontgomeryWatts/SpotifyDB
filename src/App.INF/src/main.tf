provider "aws" {
  version = "~> 2.0"
  profile = "terraform-user"
  region  = "${var.aws_region}"
}

module "cdn" {
  source = "./modules/cdn"
  url    = "${var.static_url}"
}