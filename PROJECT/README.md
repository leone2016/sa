# Terraform EC2 Basic Service Deployment

This Terraform configuration deploys a basic web service on an EC2 t2.micro instance in AWS. The instance runs Amazon Linux 2 with Apache HTTP Server.

## Prerequisites

Before you begin, ensure you have the following installed and configured:

1. **Terraform** (version >= 1.0)
   - Download from [terraform.io](https://www.terraform.io/downloads)
   - Verify installation: `terraform version`

2. **AWS CLI**
   - Install from [aws.amazon.com/cli](https://aws.amazon.com/cli/)
   - Verify installation: `aws --version`

3. **AWS Account and Credentials**
   - An active AWS account
   - AWS credentials configured either via:
     - AWS CLI: `aws configure`
     - Environment variables: `AWS_ACCESS_KEY_ID` and `AWS_SECRET_ACCESS_KEY`
     - IAM role (if running on EC2)

4. **AWS Permissions**
   - Your AWS credentials need permissions to create EC2 instances, security groups, and VPC resources
   - Minimum required permissions:
     - `ec2:RunInstances`
     - `ec2:CreateSecurityGroup`
     - `ec2:Describe*`
     - `ec2:AuthorizeSecurityGroupIngress`
     - `ec2:AuthorizeSecurityGroupEgress`

## Project Structure

```
.
├── main.tf          # Main Terraform configuration
├── variables.tf     # Variable definitions
├── outputs.tf       # Output values
├── .gitignore       # Git ignore file
└── README.md        # This file
```

## Configuration

### Variables

The following variables can be customized (defaults are set in `variables.tf`):

- `aws_region`: AWS region where resources will be created (default: `us-east-1`)
- `project_name`: Name prefix for resources (default: `basic-ec2-service`)
- `ssh_allowed_cidr`: CIDR block allowed to SSH (default: `0.0.0.0/0` - **change this for production!**)

### Customizing Variables

You can customize variables in three ways:

1. **Command line flags**:
   ```bash
   terraform apply -var="aws_region=us-west-2" -var="project_name=my-service"
   ```

2. **Variable file** (create `terraform.tfvars`):
   ```hcl
   aws_region      = "us-west-2"
   project_name    = "my-service"
   ssh_allowed_cidr = "10.0.0.0/8"
   ```

3. **Environment variables**:
   ```bash
   export TF_VAR_aws_region=us-west-2
   export TF_VAR_project_name=my-service
   ```

## Deployment Instructions

### Step 1: Initialize Terraform

Initialize Terraform and download the required providers:

```bash
terraform init
```

This command will:
- Download the AWS provider
- Initialize the backend

### Step 2: Review the Execution Plan

Before deploying, review what Terraform will create:

```bash
terraform plan
```

This will show you:
- Resources that will be created
- Expected configuration
- Any potential issues

### Step 3: Deploy the Infrastructure

Apply the Terraform configuration to create the resources:

```bash
terraform apply
```

You will be prompted to confirm. Type `yes` to proceed.

The deployment will create:
- A security group allowing HTTP (80), HTTPS (443), and SSH (22)
- An EC2 t2.micro instance with Amazon Linux 2
- Automatic installation and startup of Apache web server

### Step 4: Access Your Service

After deployment completes, Terraform will output:
- Instance ID
- Public IP address
- Public DNS name
- Website URL

Access your web service by visiting the `website_url` output in a browser.

Example output:
```
instance_public_ip = "54.123.45.67"
website_url = "http://54.123.45.67"
```

### Step 5: Verify the Deployment

1. **Check the web server**: Open the website URL in your browser
2. **SSH into the instance** (optional):
   ```bash
   ssh ec2-user@<PUBLIC_IP>
   ```
   Note: You'll need an SSH key pair configured if you want to SSH in.

## Managing the Infrastructure

### View Outputs

To see the outputs again after deployment:

```bash
terraform output
```

### Update Configuration

After modifying `.tf` files:

1. Review changes: `terraform plan`
2. Apply changes: `terraform apply`

### Destroy Infrastructure

To remove all created resources:

```bash
terraform destroy
```

**Warning**: This will permanently delete all resources created by this Terraform configuration.

## Resources Created

- **1 EC2 Instance**: t2.micro with Amazon Linux 2
- **1 Security Group**: Allows HTTP, HTTPS, and SSH traffic
- **Apache HTTP Server**: Automatically installed and running

## Cost Estimation

The t2.micro instance falls under the AWS Free Tier (for new AWS accounts):
- **750 hours/month** of t2.micro instances are free
- Data transfer costs may apply

For accounts not eligible for free tier or exceeding limits:
- t2.micro: ~$0.0116/hour (~$8.50/month if running 24/7)

## Troubleshooting

### Common Issues

1. **Authentication Errors**
   - Verify AWS credentials are configured: `aws sts get-caller-identity`
   - Check IAM permissions

2. **Region Availability**
   - Ensure the selected region has t2.micro instances available
   - Some regions may have capacity limits

3. **Security Group Rules**
   - If you can't access the web server, verify security group allows HTTP (port 80)

4. **Instance Not Responding**
   - Wait a few minutes after deployment for the user data script to complete
   - Check instance status in AWS Console
   - Verify the instance is in a public subnet (for public IP access)

### Viewing Logs

To check the user data script execution:

```bash
ssh ec2-user@<PUBLIC_IP>
sudo tail -f /var/log/cloud-init-output.log
```

## Security Considerations

⚠️ **Important Security Notes**:

1. **SSH Access**: The default `ssh_allowed_cidr` allows SSH from anywhere (`0.0.0.0/0`). For production:
   - Set `ssh_allowed_cidr` to your specific IP or network range
   - Or remove SSH ingress rule if not needed

2. **Key Pairs**: This configuration doesn't include an SSH key pair. Add one if you need SSH access:
   ```hcl
   resource "aws_instance" "web_server" {
     # ... other configuration ...
     key_name = "your-key-pair-name"
   }
   ```

3. **HTTPS**: Consider adding SSL/TLS certificate for HTTPS (using AWS Certificate Manager and Application Load Balancer)

## Next Steps

- Add an Application Load Balancer for high availability
- Configure auto-scaling groups
- Set up CloudWatch monitoring
- Add SSL/TLS certificates
- Implement backup strategies
- Add more EC2 instances for redundancy

## Support

For Terraform documentation: [terraform.io/docs](https://www.terraform.io/docs)
For AWS EC2 documentation: [docs.aws.amazon.com/ec2](https://docs.aws.amazon.com/ec2/)

## License

This is a basic example configuration for educational purposes.

