package org.gb.sample.spark.nytaxitrips;

import java.io.Serializable;
import java.time.LocalTime;

public class TimeBand implements Serializable {
	
	private static final long serialVersionUID = -5406619255276676078L;
	
	private LocalTime startTime;
	private LocalTime endTime;

	TimeBand(LocalTime startTime) {
		this(startTime, null);
	}

	TimeBand(LocalTime startTime, LocalTime endTime) {
		super();
		this.startTime = startTime;
		this.endTime = endTime;
	}

	LocalTime getStartTime() {
		return startTime;
	}

	LocalTime getEndTime() {
		return endTime;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder(startTime.toString());
		if (endTime == null) {
			sb = sb.append(" - ").append(LocalTime.MIDNIGHT.toString());
		} else {
			sb = sb.append(" - ").append(endTime.toString());
		}
		return sb.toString();
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((endTime == null) ? 0 : endTime.hashCode());
		result = prime * result + ((startTime == null) ? 0 : startTime.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		TimeBand other = (TimeBand) obj;
		if (endTime == null) {
			if (other.endTime != null)
				return false;
		} else if (!endTime.equals(other.endTime))
			return false;
		if (startTime == null) {
			if (other.startTime != null)
				return false;
		} else if (!startTime.equals(other.startTime))
			return false;
		return true;
	}
}
