# Distributed locks demo

### How to start
1. Build client-manager: `gradle client-manager:build`
2. Run docker compose: `docker-compose up -d --build`

   It runs these containers:
   - postgres
   - client-manager (two replicas)
   - nginx (for load balancing between replicas)

3. Run source project `src/main/java/org/panyukovnn/distributedlocks/DistributedLocksApplication.java`

   It will make multiple parallel requests to client-manager replicas and log the results