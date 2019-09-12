variable "table_name" {
  type        = "string"
  description = "The name of the DynamoDB Table"
}

variable "read_capacity" {
  type        = "string"
  description = "The read capacity of the DynamoDB table"
}

variable "write_capacity" {
  type        = "string"
  description = "The write capacity of the DynamoDB table"
}
