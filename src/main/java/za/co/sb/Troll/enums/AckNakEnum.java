package za.co.sb.Troll.enums;

import com.google.common.base.Enums;
import com.google.common.base.Optional;
import com.google.common.base.Strings;

public enum AckNakEnum 
{
	ACK,
	NAK,
	UNKNOWN;
	
	public static AckNakEnum getAckNak(String value) 
	{
		if (Strings.isNullOrEmpty(value))
		{
			return UNKNOWN;
		}
		
		Optional<AckNakEnum> possible = Enums.getIfPresent(AckNakEnum.class, value);
		
		if (!possible.isPresent()) 
		{
			return UNKNOWN;
		}
		
		return possible.get();
	}
}
