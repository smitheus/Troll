package za.co.sb.Troll.enums;

import com.google.common.base.Enums;
import com.google.common.base.Optional;
import com.google.common.base.Strings;

public enum EventEnum 
{
	INTER,
	INSTR,
	//SENT,
	//RECD,
	//SUBM,
	//MAX,
	UNKNOWN;
	
	public static EventEnum getEvent(String value) 
	{
		if (Strings.isNullOrEmpty(value))
		{
			return UNKNOWN;
		}
		
		Optional<EventEnum> possible = Enums.getIfPresent(EventEnum.class, value);
		
		if (!possible.isPresent()) 
		{
			return UNKNOWN;
		}
		
		return possible.get();
	}
}
