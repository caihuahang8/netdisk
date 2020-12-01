package com.micro;

import com.aspose.pdf.Document;
import com.aspose.pdf.License;
import com.aspose.pdf.SaveFormat;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

@Component(value="aspose")
public class AsposePreviewServiceImpl implements PreviewService{

	static {
		try {
        	ClassPathResource resource = new ClassPathResource("license.xml");
        	InputStream input =resource.getInputStream();
            License aposeLic = new License();
            aposeLic.setLicense(input);
            input.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
	}

	@Override
	public byte[] converToPdf(String filename, byte[] bytes) {
		InputStream input=null;
		ByteArrayOutputStream os=null;
		try{
			//设置一个字体目录
			//FontSettings.setFontsFolder("/home/server/fonts", false);
			//设置多个字体目录
			//FontSettings.setFontsFolders(new String[] {"/home/server/fonts1", "/home/server/fonts2"}, true);
			os=new ByteArrayOutputStream();
			input=new ByteArrayInputStream(bytes);

			Document doc = new Document(input);
			doc.save(os, SaveFormat.Pdf);
			return os.toByteArray();
		}catch(Exception e){
			e.printStackTrace();
			throw new RuntimeException(e.getMessage());
		}finally{
			try {
				input.close();
				os.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}



}
