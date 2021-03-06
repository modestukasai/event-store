# Event store

[![Build Status](https://travis-ci.org/modestukasai/event-store.svg?branch=master)](https://travis-ci.org/modestukasai/event-store)

[![codecov](https://codecov.io/gh/modestukasai/event-store/branch/master/graph/badge.svg)](https://codecov.io/gh/modestukasai/event-store)

Note: This is not event store which is described by Greg Young in 
[https://cqrs.wordpress.com/documents/building-event-storage/](here)

This is simple implementation of persisting events to relational database. 
It does not ensures aggregate root versioning and other cool stuff which [https://geteventstore.com/](this event store) has.
 
## How to run?

1. `./gradlew build`
2. `docker-compose up --build`
3. Access application via [http://localhost:8080](http://localhost:8080)

## Api usage

* Add event POST `/events` 

Sample request
```
{
   "uniqueId": "uniqueId",
   "type"   : "my_event_type",
   "data"   : "any Value"
}
```

* Retrieve first GET `/events/next`

* Retrieve next event GET `/events/next?id={currentEventId}` 
