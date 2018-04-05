package com.bdcor.bio.data.service;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.avro.file.DataFileReader;
import org.apache.avro.file.DataFileWriter;
import org.apache.avro.file.SeekableByteArrayInput;
import org.apache.avro.file.SeekableFileInput;
import org.apache.avro.file.SeekableInput;
import org.apache.avro.io.DatumReader;
import org.apache.avro.io.DatumWriter;
import org.apache.avro.specific.SpecificDatumReader;
import org.apache.avro.specific.SpecificDatumWriter;

import com.bdcor.bio.data.avro.Tube;

public class TubeDeSeriralizeService {

	public static byte[] serializingAvroData(List<Tube> tubes) throws IOException{
		ByteArrayOutputStream outS = new ByteArrayOutputStream();
		DatumWriter<Tube> userDatumWriter = new SpecificDatumWriter<Tube>(Tube.class);
		
		DataFileWriter<Tube> dataFileWriter = new DataFileWriter<Tube>(userDatumWriter);
		
		dataFileWriter.create(new Tube().getSchema(), outS);
		for(Tube d:tubes){
			dataFileWriter.append(d);
		}
		dataFileWriter.close();
		return outS.toByteArray();
	}
	
	public static List<Tube> deserializingAvroData(File file) throws IOException{
		return deserializingAvroData(new SeekableFileInput(file));
	}
	
	public static List<Tube> deserializingAvroData(byte[] bytes) throws IOException{
		SeekableByteArrayInput input = new SeekableByteArrayInput(bytes);
		return deserializingAvroData(input);
	}
	
	public static List<Tube> deserializingAvroData(SeekableInput in) throws IOException{
		List<Tube> tubes = new ArrayList<Tube>();
		DatumReader<Tube> userDatumReader = new SpecificDatumReader<Tube>(Tube.class);
		DataFileReader<Tube> dataFileReader = new DataFileReader<Tube>(in,userDatumReader);
		while(dataFileReader.hasNext()){
			tubes.add(dataFileReader.next());
		}
		dataFileReader.close();
		in.close();
		return tubes;
	}
			
}
