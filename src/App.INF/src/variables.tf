variable "aws_region" {
  type = "string"
  description = "What AWS Region to provision resources in"
}

variable "environment" {
  type = "string"
  description = "The environment to provision resources in" 
}

variable "static_url" {
  type = "string"
  description = "The URL of the static website"
}
