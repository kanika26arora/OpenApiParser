To run the application:
1. Go to src>resources and start docker with command: docker-compose up -d
2. Next step is to run terraform within localstack. Run these commands on terminal:
  terraform init
  terraform apply


# To check if buckets have been created
  aws --endpoint-url=http://localhost:4566 s3 ls     - This should list both buckets

# To check aws credentials:
  aws configure list

# To go inside docker
  docker ps  
   docker logs <container-id>

# To login to localstack container
  docker exec -it localstack bash
