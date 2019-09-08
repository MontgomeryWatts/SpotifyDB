variable "table_name" {
  type        = "string"
  description = "The name of the DynamoDB Table"
}

variable "prefix" {
  type        = "string"
  description = "The prefix to append to all SSM parameters"
}
