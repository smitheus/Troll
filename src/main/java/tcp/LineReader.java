package tcp;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public class LineReader
{
	private File file = null ;
	private int currentPosition ;
	BufferedReader reader = null ;
	FileReader freader ;
	
	private static int BUFFER_SIZE = 2048 ;
	
	private int thisLineStartOffset ;
	
	private int lastCharOffset ;
	private int nextCharOffset ;
	private char [] buffer ;
	
	public LineReader (String filePath)
	{
		this (filePath, 0) ;
	}

	public LineReader (String filePath, int startingPosition)
	{
		this (new File (filePath), startingPosition) ;
	}

	public LineReader (File file)
	{
		this (file, 0) ;
	}

	public LineReader(File file, int startingPosition)
	{
		thisLineStartOffset = 0 ;
		
		lastCharOffset = 0 ;
		nextCharOffset = 0 ;
		buffer = new char [BUFFER_SIZE] ;
		
		this.file = file ;
		currentPosition = startingPosition ;
		
		try
		{
			freader = new FileReader(file) ;
			//reader = new BufferedReader();
			
			if (currentPosition > 0)
			{
				freader.skip(currentPosition);
				
				// read and discard the first line at the starting position
				// in case this is a partial line
				readLine() ;
			}
		}
		catch (IOException ioEx)
		{
			
		}
	}
	
	public void close()
	{
		try
		{
			freader.close() ;
		}
		catch (Exception ex)
		{
			
		}
	}
	
	public String readLine ()
	{
		if (freader == null)
			return null ;
		
		String line = null ;
		
		// note where this line starts in the buffer
		
		thisLineStartOffset = nextCharOffset ;
		
		int endOfLineOffset ;
		
		endOfLineOffset = findEndOfLine (buffer, nextCharOffset, lastCharOffset) ;

		if (endOfLineOffset == -1)
			readIntoBuffer () ;
		else
		{
			line = copyLine (buffer, thisLineStartOffset, endOfLineOffset) ;

			nextCharOffset = skipLineTerminators (buffer, endOfLineOffset, lastCharOffset) ;
		}
		
		//System.out.println("This line at " + currentPosition + " " + thisLineStartOffset + " Next line at " + nextCharOffset);
		
		return line ;
	}

	private void readIntoBuffer()
	{
		//System.out.println("ReadIntoBuffer CP " + currentPosition + " N " + nextCharOffset + " L " + lastCharOffset + " " + thisLineStartOffset + " ");
		
		if ( (nextCharOffset > 0) && (lastCharOffset == BUFFER_SIZE))
		{
			// the buffer has content but no line terminator, so shuffle the buffer down and read into the end

			// move the file pointer by the number of chars we have processed
			
			//System.out.println("CP before shuffle " + currentPosition);
			currentPosition += nextCharOffset ;
			
			// do the shuffle
			
			//System.out.println("Shuffling " + nextCharOffset + " " + lastCharOffset);
			int  i = 0 ;

			while (nextCharOffset < lastCharOffset)
			{
				buffer [i] = buffer [nextCharOffset] ;
				++i ;
				++nextCharOffset ;
			}

			lastCharOffset = i ;
			nextCharOffset = 0 ;
			thisLineStartOffset = 0 ;

			//System.out.println("CP after " + currentPosition + " " + lastCharOffset);
			readIntoBuffer () ;
		}
		else if ( (nextCharOffset == lastCharOffset) || (lastCharOffset != BUFFER_SIZE) )
		{
			// buffer is only partially filled, so read some more
			
			//System.out.println("Reading " + (BUFFER_SIZE - lastCharOffset));
			try
			{
				int numCharsRead = freader.read (buffer, lastCharOffset, BUFFER_SIZE - lastCharOffset) ;
				if (numCharsRead != -1)
					lastCharOffset += numCharsRead ;
				else
				{
					freader.close () ;
					
					// sleep a while, hoping the file extends
					
					Thread.sleep(2000);
					
					//System.out.println("Reopen reader & skip to " + (currentPosition + lastCharOffset) );
					freader = new FileReader(file) ;
					freader.skip(currentPosition + lastCharOffset) ;
				}
			}
			catch (IOException e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
				
				System.exit(-1);
			}
			catch (InterruptedException e)
			{
				System.out.println("Interrupted");
				System.exit(0);
			}	
		}
	}

	private int skipLineTerminators(char[] buffer, int nextCharOffset, int lastCharOffset)
	{
		while (nextCharOffset < lastCharOffset)
		{
			if ('\r' == buffer [nextCharOffset])
				++nextCharOffset ;
			else if ('\n' == buffer [nextCharOffset])
				++nextCharOffset ;
			else
				return nextCharOffset ;
		}
		
		return nextCharOffset ;
	}

	private String copyLine(char[] buffer, int nextCharOffset, int endOfLineOffset)
	{
		int lineLength = endOfLineOffset - nextCharOffset ;
		char line [] = new char [ lineLength ] ;
		
		int i = 0 ;
		while (nextCharOffset < endOfLineOffset)
		{
			line [i] = buffer [nextCharOffset] ;
			
			++nextCharOffset ;
			++i ;
		}
		
		return new String (line) ;
	}

	private int findEndOfLine(char[] buffer, int nextCharOffset, int lastCharOffset)
	{
		while (nextCharOffset < lastCharOffset)
		{
			if ('\r' == buffer [nextCharOffset])
				return nextCharOffset ;
			
			if ('\n' == buffer [nextCharOffset])
				return nextCharOffset ;
			
			++nextCharOffset ;
		}
		
		return -1 ;
	}
}
