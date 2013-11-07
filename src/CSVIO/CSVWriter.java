package CSVIO;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

import org.supercsv.io.ICsvListWriter;
import org.supercsv.io.CsvListWriter;
import org.supercsv.prefs.CsvPreference;

public class CSVWriter 
{
	private ICsvListWriter _listWriter = null;
	private static File _lastOpened =null;
	
	/**
	 * Opens csv input file stream.
	 * @param file File to be opened.
	 */
	public void open(File file)
	{
		try 
		{
			if(!file.exists())
				file.createNewFile();
			else
			{
	    		file.delete();
	    		file.createNewFile();
			}
			close();
			_listWriter = new CsvListWriter(new FileWriter(file), CsvPreference.STANDARD_PREFERENCE);
			_lastOpened=file;
			
		} 
		catch (IOException e) 
		{
			System.out.println("Cannot create or open file");
			e.printStackTrace();
		}
		
	}
	
	/**
	 * Closes stream.
	 */
	public void close()
	{
		try 
		{
			if(_listWriter!=null)
				_listWriter.close();
		} catch (IOException e) 
		{
			System.out.println("Cannot close writer");
			e.printStackTrace();
		}
	}
	
	/**
	 * Writes list as new new row in csv file.
	 * @param list List of objects to be saved.
	 * @throws IOException
	 */
	public void writer(List<?> list) throws IOException
	{
		_listWriter.write(list);
	}
	
	/**
	 * Opens last used file.
	 */
	public void reopen() 
	{
			open(_lastOpened);		
	}
}
