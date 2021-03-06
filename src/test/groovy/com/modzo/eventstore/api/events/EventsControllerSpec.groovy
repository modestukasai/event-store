package com.modzo.eventstore.api.events

import com.modzo.eventstore.api.ApiSpec
import com.modzo.eventstore.api.Error
import com.modzo.eventstore.api.utils.DummyEvent
import com.modzo.eventstore.domain.event.Event
import com.modzo.eventstore.domain.event.Events
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity

class EventsControllerSpec extends ApiSpec {

    @Autowired
    private Events events

    @Autowired
    private DummyEvent dummyEvent

    def 'should find first event if no id provided'() {
        given:
            dummyEvent.create()
        and:
            Event firstEvent = events.findTop10ByOrderByIdAsc().first()
        when:
            ResponseEntity<EventBean> response = testContext.retrieveFirstEvent(EventBean)
        then:
            response.statusCode == HttpStatus.OK

            def retrievedEvent = response.body
            retrievedEvent.id == firstEvent.id
            retrievedEvent.uniqueId == firstEvent.uniqueId
            retrievedEvent.type == firstEvent.type
            retrievedEvent.data == firstEvent.data
    }

    def 'should find next event when previous id provided'() {
        given:
            dummyEvent.create(dummyEvent.sampleRequest())
            dummyEvent.create(dummyEvent.sampleRequest())
        and:
            Collection<Event> first10Events = events.findTop10ByOrderByIdAsc()
            Event firstEvent = first10Events.first()
            Event secondEvent = first10Events[1]
        when:
            ResponseEntity<EventBean> response = testContext.retrieveNextEvent(firstEvent.id, EventBean)
        then:
            response.statusCode == HttpStatus.OK
            def retrievedEvent = response.body
            retrievedEvent.id == firstEvent.id + 1
            retrievedEvent.uniqueId == secondEvent.uniqueId
            retrievedEvent.type == secondEvent.type
            retrievedEvent.data == secondEvent.data
    }

    def 'should throw error when newer event does not exists'() {
        given:
            long lastEventId = events.findTop10ByOrderByIdDesc().first().id
        when:
            ResponseEntity<Error> response = testContext.retrieveNextEvent(lastEventId, Error)
        then:
            response.statusCode == HttpStatus.NOT_FOUND
            response.body.id == 'EVENT_ID'
            response.body.message == 'Next event entry does not exist'
    }
}
