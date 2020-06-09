// Copyright 2019 Google LLC
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//     https://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.

package com.google.sps;

import java.util.Collection;

public final class FindMeetingQuery {
  public Collection<TimeRange> query(Collection<Event> events, MeetingRequest request) {
    Collection<Event> sortedEvents = Collections.sort(events, Event.ORDER_BY_START); 
    int intervalStart = TimeRange.START_OF_DAY;
    Collection<TimeRange> available = new Collection<TimeRange>();
    for (Event e : sortedEvents) {
      if (intervalStart > e.getWhen().start() || Collections.disjoint(e.getAttendees(), request.getAttendees())) {
	continue;
      } else if (e.getWhen().start() - intervalStart >= request.getDuration()) {
        TimeRange free = new TimeRange(intervalStart, e.getWhen().start() - intervalStart);
        available.add(free);
      }
      intervalStart = e.getWhen().end();
    }
    if (TimeRange.END_OF_DAY - intervalStart >= request.getDuration()) {
      TimeRange free = new TimeRange(intervalStart, e.getWhen().start - intervalStart);
      available.add(free);
    }
    return available;
  }
}
