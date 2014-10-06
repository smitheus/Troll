package za.co.sb.Troll.csv;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import za.co.sb.Troll.dto.TransactionViewItemDto;
import au.com.bytecode.opencsv.CSVWriter;

public class ExportToCsvHandler 
{
	private static final Logger LOG = LogManager.getLogger(ExportToCsvHandler.class.getName());
	
	public static void writeTransactionViewCsvFile(List<TransactionViewItemDto> exportTransactionViewItemDtoList, File csvExprtFile) throws Exception
	{
		try 
		{
			BufferedWriter out = new BufferedWriter(new FileWriter(csvExprtFile));
			CSVWriter writer = new CSVWriter(out);
			
			writer.writeNext(TransactionViewItemDto.getCsvExportHeaders().toArray(new String[TransactionViewItemDto.getCsvExportHeaders().size()]));
			
			for (TransactionViewItemDto transactionViewItemDto : exportTransactionViewItemDtoList) 
			{
				writer.writeNext(transactionViewItemDto.getCsvExportValues().toArray(new String[TransactionViewItemDto.getCsvExportHeaders().size()]));
			}
			
			writer.close();
		}
		catch (Exception ex)
		{
			LOG.error("Exception writing CSV file", ex);
			throw(ex);
		}
	}
}
