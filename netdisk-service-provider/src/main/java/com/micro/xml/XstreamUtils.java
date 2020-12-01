package com.micro.xml;

import com.thoughtworks.xstream.XStream;
import org.springframework.core.io.ClassPathResource;

import java.io.InputStream;
import java.util.List;

public class XstreamUtils {


	public static List<TypeXml> parseTypexml(){
		try{
			XStream stream=new XStream();
			stream.alias("type", TypeXml.class);
			stream.alias("types",List.class);

			ClassPathResource resource = new ClassPathResource("type.xml");
			InputStream input =resource.getInputStream();
			List<TypeXml> lists=(List<TypeXml>)stream.fromXML(input);

			return lists;
		}catch(Exception e){
			e.printStackTrace();
			throw new RuntimeException(e.getMessage());
		}
	}
	public static List<TypeSuffixXml> parseTypeDetailxml(){
		try{
			XStream stream=new XStream();
			stream.alias("detail",TypeSuffixXml.class);
			stream.alias("details",List.class);

			ClassPathResource resource = new ClassPathResource("typedetail.xml");
			InputStream input =resource.getInputStream();
			List<TypeSuffixXml> lists=(List<TypeSuffixXml>)stream.fromXML(input);

			return lists;
		}catch(Exception e){
			e.printStackTrace();
			throw new RuntimeException(e.getMessage());
		}
	}
}
