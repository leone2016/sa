a. Write a producer that produces 5 Orders with the fields ordernumber,
customername, customercountry, amount and status. Write a consumer that
consumes these Order objects

b. Write a new consumer that consumes all published Order objects starting from the
first published Order. Let the consumer also print the offset and the groupid to the
console.

c. Create a consumer group of 2 Order consumers and test how the Orders are
distributed between the consumers. Let the consumer also print the offset and the
groupid to the console. Notice that it is always the same consumer who gets the
messages.