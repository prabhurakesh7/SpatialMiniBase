/* file Convert.java */

package global;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import spatial.bo.SpatialGeometry;
import spatial.util.SpatialUtil;

public class Convert
{
	
	/**
	 * read 4 bytes from given byte array at the specified position
	 * convert it to an integer
	 * 
	 * @param data
	 *            a byte array
	 * @param position
	 *            in data[]
	 * @exception java.io.IOException
	 *                I/O errors
	 * @return the integer
	 */
	public static int getIntValue(int position, byte[] data)
	        throws java.io.IOException
	{
		InputStream in;
		DataInputStream instr;
		int value;
		byte tmp[] = new byte[4];
		
		// copy the value from data array out to a tmp byte array
		System.arraycopy(data, position, tmp, 0, 4);
		
		/*
		 * creates a new data input stream to read data from the
		 * specified input stream
		 */
		in = new ByteArrayInputStream(tmp);
		instr = new DataInputStream(in);
		value = instr.readInt();
		
		return value;
	}
	
	/**
	 * read 4 bytes from given byte array at the specified position
	 * convert it to a float value
	 * 
	 * @param data
	 *            a byte array
	 * @param position
	 *            in data[]
	 * @exception java.io.IOException
	 *                I/O errors
	 * @return the float value
	 */
	public static float getFloValue(int position, byte[] data)
	        throws java.io.IOException
	{
		InputStream in;
		DataInputStream instr;
		float value;
		byte tmp[] = new byte[4];
		
		// copy the value from data array out to a tmp byte array
		System.arraycopy(data, position, tmp, 0, 4);
		
		/*
		 * creates a new data input stream to read data from the
		 * specified input stream
		 */
		in = new ByteArrayInputStream(tmp);
		instr = new DataInputStream(in);
		value = instr.readFloat();
		
		return value;
	}
	
	/**
	 * read 2 bytes from given byte array at the specified position
	 * convert it to a short integer
	 * 
	 * @param data
	 *            a byte array
	 * @param position
	 *            the position in data[]
	 * @exception java.io.IOException
	 *                I/O errors
	 * @return the short integer
	 */
	public static short getShortValue(int position, byte[] data)
	        throws java.io.IOException
	{
		InputStream in;
		DataInputStream instr;
		short value;
		byte tmp[] = new byte[2];
		
		// copy the value from data array out to a tmp byte array
		System.arraycopy(data, position, tmp, 0, 2);
		
		/*
		 * creates a new data input stream to read data from the
		 * specified input stream
		 */
		in = new ByteArrayInputStream(tmp);
		instr = new DataInputStream(in);
		value = instr.readShort();
		
		return value;
	}
	
	/**
	 * reads a string that has been encoded using a modified UTF-8 format from
	 * the given byte array at the specified position
	 * 
	 * @param data
	 *            a byte array
	 * @param position
	 *            the position in data[]
	 * @param length
	 *            the length of the string in bytes
	 *            (=strlength +2)
	 * @exception java.io.IOException
	 *                I/O errors
	 * @return the string
	 */
	public static String getStrValue(int position, byte[] data, int length)
	        throws java.io.IOException
	{
		InputStream in;
		DataInputStream instr;
		String value;
		byte tmp[] = new byte[length];
		
		// copy the value from data array out to a tmp byte array
		System.arraycopy(data, position, tmp, 0, length);
		
		/*
		 * creates a new data input stream to read data from the
		 * specified input stream
		 */
		in = new ByteArrayInputStream(tmp);
		instr = new DataInputStream(in);
		value = instr.readUTF();
		return value;
	}
	
	/**
	 * reads 2 bytes from the given byte array at the specified position
	 * convert it to a character
	 * 
	 * @param data
	 *            a byte array
	 * @param position
	 *            the position in data[]
	 * @exception java.io.IOException
	 *                I/O errors
	 * @return the character
	 */
	public static char getCharValue(int position, byte[] data)
	        throws java.io.IOException
	{
		InputStream in;
		DataInputStream instr;
		char value;
		byte tmp[] = new byte[2];
		// copy the value from data array out to a tmp byte array
		System.arraycopy(data, position, tmp, 0, 2);
		
		/*
		 * creates a new data input stream to read data from the
		 * specified input stream
		 */
		in = new ByteArrayInputStream(tmp);
		instr = new DataInputStream(in);
		value = instr.readChar();
		return value;
	}
	
	/**
	 * update an integer value in the given byte array at the specified position
	 * 
	 * @param data
	 *            a byte array
	 * @param value
	 *            the value to be copied into the data[]
	 * @param position
	 *            the position of tht value in data[]
	 * @exception java.io.IOException
	 *                I/O errors
	 */
	public static void setIntValue(int value, int position, byte[] data)
	        throws java.io.IOException
	{
		/*
		 * creates a new data output stream to write data to
		 * underlying output stream
		 */
		
		OutputStream out = new ByteArrayOutputStream();
		DataOutputStream outstr = new DataOutputStream(out);
		
		// write the value to the output stream
		
		outstr.writeInt(value);
		
		// creates a byte array with this output stream size and the
		// valid contents of the buffer have been copied into it
		byte[] B = ((ByteArrayOutputStream) out).toByteArray();
		
		// copies the first 4 bytes of this byte array into data[]
		System.arraycopy(B, 0, data, position, 4);
		
	}
	
	/**
	 * update a float value in the given byte array at the specified position
	 * 
	 * @param data
	 *            a byte array
	 * @param value
	 *            the value to be copied into the data[]
	 * @param position
	 *            the position of tht value in data[]
	 * @exception java.io.IOException
	 *                I/O errors
	 */
	public static void setFloValue(float value, int position, byte[] data)
	        throws java.io.IOException
	{
		/*
		 * creates a new data output stream to write data to
		 * underlying output stream
		 */
		
		OutputStream out = new ByteArrayOutputStream();
		DataOutputStream outstr = new DataOutputStream(out);
		
		// write the value to the output stream
		
		outstr.writeFloat(value);
		
		// creates a byte array with this output stream size and the
		// valid contents of the buffer have been copied into it
		byte[] B = ((ByteArrayOutputStream) out).toByteArray();
		
		// copies the first 4 bytes of this byte array into data[]
		System.arraycopy(B, 0, data, position, 4);
		
	}
	
	/**
	 * update a short integer in the given byte array at the specified position
	 * 
	 * @param data
	 *            a byte array
	 * @param value
	 *            the value to be copied into data[]
	 * @param position
	 *            the position of tht value in data[]
	 * @exception java.io.IOException
	 *                I/O errors
	 */
	public static void setShortValue(short value, int position, byte[] data)
	        throws java.io.IOException
	{
		/*
		 * creates a new data output stream to write data to
		 * underlying output stream
		 */
		
		OutputStream out = new ByteArrayOutputStream();
		DataOutputStream outstr = new DataOutputStream(out);
		
		// write the value to the output stream
		
		outstr.writeShort(value);
		
		// creates a byte array with this output stream size and the
		// valid contents of the buffer have been copied into it
		byte[] B = ((ByteArrayOutputStream) out).toByteArray();
		
		// copies the first 2 bytes of this byte array into data[]
		System.arraycopy(B, 0, data, position, 2);
		
	}
	
	/**
	 * Insert or update a string in the given byte array at the specified
	 * position.
	 * 
	 * @param data
	 *            a byte array
	 * @param value
	 *            the value to be copied into data[]
	 * @param position
	 *            the position of tht value in data[]
	 * @exception java.io.IOException
	 *                I/O errors
	 */
	public static void setStrValue(String value, int position, byte[] data)
	        throws java.io.IOException
	{
		/*
		 * creates a new data output stream to write data to
		 * underlying output stream
		 */
		
		OutputStream out = new ByteArrayOutputStream();
		DataOutputStream outstr = new DataOutputStream(out);
		
		// write the value to the output stream
		
		outstr.writeUTF(value);
		// creates a byte array with this output stream size and the
		// valid contents of the buffer have been copied into it
		byte[] B = ((ByteArrayOutputStream) out).toByteArray();
		
		int sz = outstr.size();
		// copies the contents of this byte array into data[]
		System.arraycopy(B, 0, data, position, sz);
		
	}
	
	/**
	 * Update a character in the given byte array at the specified position.
	 * 
	 * @param data
	 *            a byte array
	 * @param value
	 *            the value to be copied into data[]
	 * @param position
	 *            the position of tht value in data[]
	 * @exception java.io.IOException
	 *                I/O errors
	 */
	public static void setCharValue(char value, int position, byte[] data)
	        throws java.io.IOException
	{
		/*
		 * creates a new data output stream to write data to
		 * underlying output stream
		 */
		
		OutputStream out = new ByteArrayOutputStream();
		DataOutputStream outstr = new DataOutputStream(out);
		
		// write the value to the output stream
		outstr.writeChar(value);
		
		// creates a byte array with this output stream size and the
		// valid contents of the buffer have been copied into it
		byte[] B = ((ByteArrayOutputStream) out).toByteArray();
		
		// copies contents of this byte array into data[]
		System.arraycopy(B, 0, data, position, 2);
		
	}
	
	/**
	 * Converts {@link SpatialGeometry} to bytes to write to file
	 * 
	 * @param spatialGeometry
	 *            {@link SpatialGeometry}
	 * @param position
	 * @param data
	 * @throws IOException
	 */
	public static void setSpatialGeometryValue(SpatialGeometry spatialGeometry,
	        short position, byte[] data) throws IOException
	{
		OutputStream outputStream = new ByteArrayOutputStream();
		DataOutputStream dataOutputStream = new DataOutputStream(outputStream);
		
		dataOutputStream.writeShort((short) spatialGeometry.getGeometryShape()
		        .getShapeId());
		// outstr.writeChars(spatialGeometry.getGeometryShape().getShapeName());
		
		for (double coordinate : spatialGeometry.getCoordinates())
			dataOutputStream.writeDouble(coordinate);
		
		byte[] buffer = ((ByteArrayOutputStream) outputStream).toByteArray();
		int dataOutSize = dataOutputStream.size();
		System.arraycopy(buffer, 0, data, position, dataOutSize);
	}
	
	/**
	 * @param position
	 * @param data
	 * @return
	 * @throws IOException
	 */
	public static SpatialGeometry getSpatialGeometryShape(short position,
	        byte[] data) throws IOException
	{
		InputStream in;
		DataInputStream instr;
		byte tmp[] = new byte[50];
		System.arraycopy(data, position, tmp, 0, 50);
		
		in = new ByteArrayInputStream(tmp);
		instr = new DataInputStream(in);
		
		return SpatialUtil.getGeometryShape(instr);
	}
	
	/**
	 * @param key
	 * @param i
	 * @param data
	 * @throws IOException
	 */
	public static void setDoubleArrValue(double[] coordinates, int position,
	        byte[] data) throws IOException
	{
		/*
		 * creates a new data output stream to write data to
		 * underlying output stream
		 */
		
		OutputStream out = new ByteArrayOutputStream();
		DataOutputStream outstr = new DataOutputStream(out);
		
		// write the value to the output stream
		
		for (int j = 0; j < coordinates.length; j++)
		{
			double b = coordinates[j];
			outstr.writeDouble(b);
			
		}
		
		// creates a byte array with this output stream size and the
		// valid contents of the buffer have been copied into it
		byte[] B = ((ByteArrayOutputStream) out).toByteArray();
		
		// copies the first 2 bytes of this byte array into data[]
		System.arraycopy(B, 0, data, position, 32);
		
	}
	
	/**
	 * @param offset
	 * @param from
	 * @param i
	 * @return
	 * @throws IOException
	 */
	public static double[] getDoubleArrValue(int offset, byte[] from, int length)
	        throws IOException
	{
		InputStream in;
		DataInputStream instr;
		byte tmp[] = new byte[50];
		System.arraycopy(from, offset, tmp, 0, length);
		
		in = new ByteArrayInputStream(tmp);
		instr = new DataInputStream(in);
		double x1 = instr.readDouble();
		double y1 = instr.readDouble();
		double x2 = instr.readDouble();
		double y2 = instr.readDouble();
		double[] coordinates = new double[] { x1, y1, x2, y2 };
		return coordinates;
	}
	
}
