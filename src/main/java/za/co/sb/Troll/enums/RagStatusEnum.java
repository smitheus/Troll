package za.co.sb.Troll.enums;

import com.google.common.base.Enums;
import com.google.common.base.Optional;
import com.google.common.base.Strings;

public enum RagStatusEnum 
{
	R,
	A,
	G,
	UNKNOWN;
	
	public static RagStatusEnum getRagStatus(String value) 
	{
		if (Strings.isNullOrEmpty(value))
		{
			return UNKNOWN;
		}
		
		Optional<RagStatusEnum> possible = Enums.getIfPresent(RagStatusEnum.class, value);
		
		if (!possible.isPresent()) 
		{
			return UNKNOWN;
		}
		
		return possible.get();
	}
}