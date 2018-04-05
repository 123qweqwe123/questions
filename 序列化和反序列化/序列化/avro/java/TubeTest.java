package com.test;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.bdcor.bio.data.avro.Tube;
import com.bdcor.bio.data.service.TubeDeSeriralizeService;

public class TubeTest {

	public static void main(String[] args) throws IOException {

		List<Tube> tubes = new ArrayList<Tube>();
		Tube t1 = new Tube();
		t1.setId("id1");
		t1.setProjectId("projid01");
		t1.setLccCode("lcc01");
		t1.setTubeCode("tcode1");
		t1.setTubeType("ttype1");
		t1.setBoxCode("bcode1");
		t1.setBoxType("btype01");
		t1.setBoxColNo("bcoleno01");
		t1.setBoxRowNo("brow01");
		t1.setBloodCode("blood01");
		t1.setCreateBy("c001");
		t1.setCreateDate("2017-01-01");
		t1.setRemark("remark1");
		t1.setFromWork("from-work-1");
		t1.setHemolysisIs(false);
		t1.setLipidIs(true);
		t1.setEmptyIs(false);
		t1.setErrorCode("error-1");
		
		tubes.add(t1);
		
		byte[] bytes = TubeDeSeriralizeService.serializingAvroData(tubes);
		File f = new File("asdasd.avro");
		FileOutputStream out = new FileOutputStream(f);
		out.write(bytes);
		out.close();
		/*
		List<Tube> lll = TubeDeSeriralizeService.deserializingAvroData(bytes);
		for(Tube tube:lll){
			System.out.println(tube);
		}
		*/
	}

}
