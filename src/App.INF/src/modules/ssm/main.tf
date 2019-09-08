resource "aws_ssm_parameter" "dynamo_table_name" {
  name  = "${var.prefix}table"
  type  = "SecureString"
  value = "${var.table_name}"
}
