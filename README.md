# Leovegas test application
## Description
There are 2 endpoints
1. /transaction/{id}/credit
2. /transaction/{id}/debit
, where id - is a player identifier

Microservice sends response dto
```
{
    "errorCode": <error Code>,
    "errorDescription": <error Description>
}
```
Then error code is 00 error description is null

### DB Schema
Two tables 
PLAYER (ID, BALANCE)
TRANSACTION_HISTORY(ID, PLAYER_ID, OPERATION_TYPE, OPERATION_AMOUNT)

### Credit Operation: 
Send to /transaction/{id}/credit RequestOperationDto
```
{
  "transactionId":<Transaction Identifier>,
  "operationAmount":<Operation Amount>
}
```
### Debit Operation
Send to /transaction/{id}/debit RequestOperationDto
```
{
  "transactionId":<Transaction Identifier>,
  "operationAmount":<Operation Amount>
}
```
### Exceptions
1. If transaction id is not unique, microservice sends response with error code "01"
2. If Player is not define in DB, microservice sends response with error code "02"
3. If operation type is debit and player balance is less than operation amount, microservice sends response with error code "03"


