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
import java.util.Collections;
import java.util.ArrayList;

public final class FindMeetingQuery {
    Collection<String> attendees;
  public Collection<TimeRange> query(Collection<Event> events, MeetingRequest request) {
      int duration = (int) request.getDuration();
      attendees = request.getAttendees();
      Collection<TimeRange> availableTimes = new ArrayList<>();
      Collection<TimeRange> potentialTimes = new ArrayList<>();

      int startTime = 0;
      while (startTime + duration < TimeRange.END_OF_DAY) {
          TimeRange potentialMeetTime = TimeRange.fromStartDuration(startTime, duration);

          for(Event event: events) {
              TimeRange eventMeetTime = event.getWhen();
              Collection<String> eventAttendees = event.getAttendees();
              if(!eventMeetTime.overlaps(potentialMeetTime)) {
                  if(potentialTimeConflicts(eventAttendees)) {
                      availableTimes.add(potentialMeetTime);
                  }
              }
          }
          startTime++;
      }

      TimeRange lastTimeBlock = TimeRange.fromStartEnd(startTime, TimeRange.END_OF_DAY, true);
      if(lastTimeBlock.duration() >= request.getDuration()) {
          availableTimes.add(lastTimeBlock);
      }
      return availableTimes;
  }

  public boolean potentialTimeConflicts(Collection<String> eventAttendees) {
      return (Collections.disjoint(eventAttendees, attendees));
  }

}
