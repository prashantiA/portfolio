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

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

public final class FindMeetingQuery {
  public Collection<TimeRange> query(Collection<Event> events, MeetingRequest request) {
    ArrayList<Event> sortedEvents = new ArrayList<Event>(events);
    Collections.sort(sortedEvents, Event.ORDER_BY_START); 
    int intervalStart = TimeRange.START_OF_DAY;
    ArrayList<TimeRange> available = new ArrayList<TimeRange>();

    for (Event e : sortedEvents) {
      if (intervalStart > e.getWhen().end() || Collections.disjoint(e.getAttendees(), request.getAttendees())) {
        continue;
      } else if (e.getWhen().start() - intervalStart >= request.getDuration()) {
        TimeRange free = TimeRange.fromStartEnd(intervalStart, e.getWhen().start(), false);
        available.add(free);
      }
      intervalStart = e.getWhen().end();
    }
    if (TimeRange.END_OF_DAY - intervalStart >= request.getDuration()) {
      TimeRange free = TimeRange.fromStartEnd(intervalStart, TimeRange.END_OF_DAY, true);
      available.add(free);
    }
    return available;
  }
}
