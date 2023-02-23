# microservice-springboot
product microservice using springboot

![product_microservices drawio](https://user-images.githubusercontent.com/49813515/220804921-46a27981-e51f-46b2-b201-afeafded3e39.png)

## Details

- Product Service: using MongDB as database and has api GET and POST `/api/product`.
POST JSON body format:

```
{
  "name": String
  "description": String
  "price": Int
 }
```
- Order Serivec: using MySQL as database, api POST `/api/order`
POST JSON format:
```
{
    "orderLineItemsDtoList": Array [
        {
            "skuCode": String,
            "price": Int,
            "quantity": Int
        }
    ]
 }
```
Order Service also does synchronous communication to Inventory Service using Resilience4J and asynchronous communication to Notification Service using Kafka by sending `OrderPlacedEvent`.
- Inventory Service: using MySQL as database and has api GET `api/inventory?skuCode={SKU CODE HERE}`
- API Gateway which uses KeyCloak as authentication server

Credential for KeyCloak:
```
username: admin
password: admin
```
- Discovery Server: A registry using Netflix Eureka
- Distributed Tracing: using Zipkin for better user experience

Credential for Zipkin:
```
username: eureka
password: password
```
