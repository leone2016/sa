# Circuit Breaker Testing Guide

## Overview
The circuit breaker has been added to the ProductService's remote call to StockService using Resilience4j. This guide explains how to test if it's working correctly.

## Circuit Breaker Configuration

The circuit breaker is configured with the following settings:
- **Sliding Window Size**: 5 calls
- **Minimum Number of Calls**: 3 calls before circuit can open
- **Failure Rate Threshold**: 50% (circuit opens if 50% of calls fail)
- **Wait Duration in Open State**: 3 seconds
- **Fallback**: Returns 0 when circuit is open

## Testing Scenarios

### Test 1: Normal Operation (Circuit Closed)
**Objective**: Verify that the circuit breaker allows normal calls when StockService is healthy.

**Steps**:
1. Start all services (ProductService, StockService1, StockService2, Gateway, Consul)
2. Make a request to get a product:
   ```bash
   curl http://localhost:8901/product/1
   ```
3. **Expected Result**: 
   - Product is returned with actual stock value
   - No fallback is triggered
   - Circuit remains CLOSED

**Verification**:
- Check logs: No fallback warning messages
- Check actuator endpoint: Circuit state should be CLOSED

### Test 2: Circuit Breaker Opens (Service Failure)
**Objective**: Verify that the circuit breaker opens when StockService fails.

**Steps**:
1. Start ProductService and Gateway
2. **Stop both StockService instances** (or make them unavailable)
3. Make 3-5 consecutive requests:
   ```bash
   curl http://localhost:8901/product/1
   curl http://localhost:8901/product/1
   curl http://localhost:8901/product/1
   ```
4. **Expected Result**:
   - First few calls may fail or timeout
   - After minimum calls (3) with 50% failure rate, circuit opens
   - Subsequent calls immediately return fallback value (0)
   - Log shows: `⚠️ Circuit breaker activated! Fallback method called for product number: ...`

**Verification**:
- Check ProductService logs for fallback messages
- Check actuator endpoint: Circuit state should be OPEN
- Response should have `numberInStock: 0`

### Test 3: Circuit Breaker Half-Open State
**Objective**: Verify that the circuit breaker transitions to half-open and can recover.

**Steps**:
1. After circuit is OPEN (from Test 2)
2. Wait 3 seconds (waitDurationInOpenState)
3. **Start one StockService instance** (make service available again)
4. Make a request:
   ```bash
   curl http://localhost:8901/product/1
   ```
5. **Expected Result**:
   - Circuit transitions to HALF_OPEN
   - If call succeeds, circuit closes
   - If call fails, circuit opens again

**Verification**:
- Check actuator endpoint: Circuit state transitions
- Logs show successful calls after recovery

### Test 4: Monitor Circuit Breaker State via Actuator
**Objective**: Check circuit breaker metrics and state programmatically.

**Steps**:
1. Check circuit breaker state:
   ```bash
   curl http://localhost:8901/actuator/circuitbreakers
   ```
2. Check circuit breaker events:
   ```bash
   curl http://localhost:8901/actuator/circuitbreakerevents
   ```
3. **Expected Result**:
   - JSON response showing circuit breaker instances
   - State: CLOSED, OPEN, or HALF_OPEN
   - Metrics: failure rate, number of calls, etc.

### Test 5: Simulate Intermittent Failures
**Objective**: Test circuit breaker with partial failures.

**Steps**:
1. Create a test endpoint in StockService that randomly fails:
   - Modify StockController to throw exceptions randomly
2. Make multiple requests:
   ```bash
   for i in {1..10}; do curl http://localhost:8901/product/1; sleep 0.5; done
   ```
3. **Expected Result**:
   - Circuit opens when failure rate exceeds 50%
   - Fallback is used when circuit is open
   - Circuit recovers when service becomes stable

## Monitoring Endpoints

### Actuator Endpoints
- **Circuit Breaker State**: `GET /actuator/circuitbreakers`
- **Circuit Breaker Events**: `GET /actuator/circuitbreakerevents`
- **Health Check**: `GET /actuator/health`

### Example Response from `/actuator/circuitbreakers`:
```json
{
  "circuitBreakers": [
    {
      "name": "stock-service",
      "state": "CLOSED",
      "failureRate": 0.0,
      "slowCallRate": 0.0,
      "numberOfSuccessfulCalls": 10,
      "numberOfFailedCalls": 0,
      "numberOfNotPermittedCalls": 0
    }
  ]
}
```

## Quick Test Script

```bash
#!/bin/bash

echo "=== Test 1: Normal Operation ==="
curl -s http://localhost:8901/product/1 | jq

echo -e "\n=== Test 2: Stop StockService and trigger circuit breaker ==="
# Stop StockService instances first, then:
for i in {1..5}; do
  echo "Request $i:"
  curl -s http://localhost:8901/product/1 | jq '.numberInStock'
  sleep 0.5
done

echo -e "\n=== Test 3: Check Circuit Breaker State ==="
curl -s http://localhost:8901/actuator/circuitbreakers | jq

echo -e "\n=== Test 4: Check Circuit Breaker Events ==="
curl -s http://localhost:8901/actuator/circuitbreakerevents | jq
```

## Expected Log Output

### When Circuit Opens:
```
⚠️ Circuit breaker activated! Fallback method called for product number: P001
```

### When Circuit is Closed:
```
🟢 Fetching stock for product number: P001
```
(or)
```
🔵 Fetching stock for product number: P001
```

## Troubleshooting

1. **Circuit breaker not activating?**
   - Check if `feign.circuitbreaker.enabled=true` in application.yml
   - Verify Resilience4j dependency is in pom.xml
   - Ensure minimum number of calls (3) have been made

2. **Fallback not working?**
   - Verify fallback class is annotated with `@Component`
   - Check that `@FeignClient` has `fallback = StockServiceClientFallback.class`
   - Ensure fallback class implements the Feign client interface

3. **Cannot access actuator endpoints?**
   - Verify endpoints are exposed in `management.endpoints.web.exposure.include`
   - Check that actuator dependency is in pom.xml

