package CSVIO;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Collections;
import java.util.List;

import org.supercsv.io.ICsvListReader;
import org.supercsv.io.CsvListReader;
import org.supercsv.prefs.CsvPreference;

/**
 * Class for reading csv files. It behaves like it was static.
 * @author Bartek
 *
 */
public class CSVReader 
{
	//Reader for csv files.
	private static ICsvListReader _listReader = null;
	private static File _lastOpened =null;
	private static List<String> _lastRead=null;
	private static boolean _checked=false;
	private static int _count;
	
	/**
	 * Opens csv output file stream.
	 * @param file File to be opened.
	 */
	public static void open(File file)
	{
		reopen(file);
		_count=countRows();
	}
	
	private static void reopen(File file)
	{
		try 
		{
			close();
			_listReader= new CsvListReader(new FileReader(file), CsvPreference.STANDARD_PREFERENCE);
			_lastOpened=file;
			_checked=false;
		} 
		catch (IOException e) 
		{
			System.out.println("Cannot open the file");
			e.printStackTrace();
		}
	}
	
	/**
	 * Closes stream.
	 */
	public static void close()
	{
		try 
		{
			if(_listReader!=null)
				_listReader.close();
		} catch (IOException e) 
		{
			System.out.println("Cannot close reader");
			e.printStackTrace();
		}
	}
	
	/**
	 * Returns number of read lines.
	 * @return Number of read lines.
	 */
	public static int getLineNumber()
	{
		return _listReader.getLineNumber();
	}
	
	/**
	 * Returns number of read rows.
	 * @return Number of read rows.
	 */
	public static int getRowNumber()
	{
		return _listReader.getRowNumber();
	}
	
	/**
	 * Returns number of columns.
	 * @return Number of columns.
	 */
	public static int getColumnsNumber()
	{
		return _listReader.length();
	}
	
	/**
	 * Reads current row.
	 * @return List of String containing each column.
	 * @throws IOException
	 */
	public static List<String> readRow() throws IOException
	{
		List<String> result;
		//Has next was called before read.
		if(_checked)
		{
			result=_lastRead;
			_checked=false;
		}
		else
			result=_listReader.read();
		if(result!=null)
			result.removeAll(Collections.singleton(null));
		return result;
	}
	
	/**
	 * Read n-th column of current row.
	 * @param n Number of column to be read.
	 * @return Value of n-th column.
	 */
	public static String getColumn(int n)
	{
		return _listReader.get(n);
	}
	
	/**
	 * Read choosen row.
	 * @param n Row to be read.
	 * @return List of String containing each column.
	 * @throws IOException
	 * @Warning Possibly slow as hell.
	 */
	public static List<String> readRow(int n) throws IOException
	{
		_checked=false;
		goToRow(n);
		return readRow();
	}
	
	/**
	 * Goes to n-th row.
	 * @param n Row to be opened
	 * @Warning Possibly slow as hell.
	 */
	public static void goToRow(int n)
	{
		if(n<getRowNumber())
		{
			reopen();
			goToRow(n);
		}
		else 
			if(n>getRowNumber())
			{
				while(n!=getRowNumber())
				{
					try 
					{
						readRow();
					} 
					catch (IOException e) 
					{
						System.out.println("Cannot open n-th row");
						e.printStackTrace();
					}
				}
			}
	}
	
	/**
	 * Checks if EOF was reached.
	 * @return True if EOF was reached, false otherwise.
	 */
	public static boolean hasNext()
	{
		if(!_checked)
		{
			try 
			{
				_lastRead=readRow();
				_checked=true;
			} 
			catch (IOException e) 
			{
				_lastRead=null;
				e.printStackTrace();
			}
		}
		return _lastRead!=null;
	}
	
	/**
	 * Returns total number of rows.
	 * @return Total number of rows.
	 */
	public static int rowsCount()
	{
		return _count;
	}
	
	//count rows
	private static int countRows()
	{
		int count=0;
		try
		{			
			while(hasNext())
			{
				readRow();
				count++;
			}
			reopen();
		} catch (IOException e) 
		{
			count=0;
			System.out.println("Error counting rows");
			e.printStackTrace();
		}
		return count;
	}

	//opens last used file.
	private static void reopen() 
	{
		reopen(_lastOpened);		
	}
}
