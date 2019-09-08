variable "aws_region" {
  type        = "string"
  description = "What AWS Region to provision resources in"
}

variable "app_name" {
  type = "string"
}

variable "site_url" {
  type        = "string"
  description = "The URL of the main website"
}

variable "environment" {
  type        = "string"
  description = "The environment to provision resources for"
}